package com.example.hackernews;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView newsTitleListView;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> content = new ArrayList<>();
    ArrayAdapter<String> titlesArrayAdapter;
    SQLiteDatabase articlesDB;

    public class downloadTopStoriesIdTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                JSONArray jsonArray = new JSONArray(result);
                int noOfItems = 20;
                if (jsonArray.length() < 20){
                    noOfItems = jsonArray.length();
                }
                articlesDB.execSQL("DELETE FROM articles");
                for (int i=0;i<noOfItems;i++){
                    String articleID = jsonArray.getString(i);
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + articleID + ".json?print=pretty");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    reader = new InputStreamReader(inputStream);
                    data = reader.read();
                    String articleResult = "";
                    while (data != -1){
                        char current = (char) data;
                        articleResult += current;
                        data = reader.read();
                    }
                    JSONObject jsonObject = new JSONObject(articleResult);
                    if (!jsonObject.isNull("title") && !jsonObject.isNull("url")){
                        String articleTitle = jsonObject.getString("title");
                        String articleUrl = jsonObject.getString("url");
                        Log.i("article", articleTitle + ":" + articleUrl);
                        /*url = new URL(articleUrl);
                        BufferedReader reader1 = new BufferedReader(new InputStreamReader(url.openStream()));
                        String inputLine, articleHTTPContent ="";
                        while ((inputLine = reader1.readLine()) != null){
                            articleHTTPContent += inputLine;
                        }
                        Log.i("article", articleHTTPContent);*/
                        String sqlInsertQuery = "INSERT INTO articles (articleID, title, url) VALUES (?,?,?)";
                        SQLiteStatement insertStatement = articlesDB.compileStatement(sqlInsertQuery);
                        insertStatement.bindString(1, articleID);
                        insertStatement.bindString(2, articleTitle);
                        insertStatement.bindString(3, articleUrl);
//                        insertStatement.bindString(4, articleHTTPContent);
                        insertStatement.execute();
                    }
                }
                return result;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
        }
    }

    public void updateListView(){
        Cursor c = articlesDB.rawQuery("SELECT * FROM articles", null);
        int titleIndex = c.getColumnIndex("title");
        int contentIndex = c.getColumnIndex("url");

        if (c.moveToFirst()){
            titles.clear();
            content.clear();
            do {
                titles.add(c.getString(titleIndex));
                content.add(c.getString(contentIndex));
            } while (c.moveToNext());
        }
        titlesArrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articlesDB = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleID INTEGER, title VARCHAR, url VARCHAR, content VARCHAR)");

        downloadTopStoriesIdTask idTask = new downloadTopStoriesIdTask();
        try {
            idTask.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        } catch (Exception e) {
            e.printStackTrace();
        }

        newsTitleListView = findViewById(R.id.newsTitleListView);
        titlesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        newsTitleListView.setAdapter(titlesArrayAdapter);
        newsTitleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("content", content.get(position));
                startActivity(intent);
            }
        });
        updateListView();
    }
}
