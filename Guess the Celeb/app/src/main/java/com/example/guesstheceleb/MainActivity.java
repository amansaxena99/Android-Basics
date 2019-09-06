package com.example.guesstheceleb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebImageURL = new ArrayList<>();
    ArrayList<String> celebNames = new ArrayList<>();
    int choosenCelebNo = 0;
    String[] options = new String[4];
    int correctOption = 0;

    ImageView celebImageView;
    Button optButton0, optButton1, optButton2, optButton3;

    public void guessClick(View view){
        if (view.getTag().toString().equals(Integer.toString(correctOption))){
            Toast.makeText(this, "Correct! :)", Toast.LENGTH_SHORT).show();
            createQuestion();
        } else {
            Toast.makeText(this, "Incorrect! :/ It was " + celebNames.get(choosenCelebNo), Toast.LENGTH_SHORT).show();
            createQuestion();
        }
    }

    public void createQuestion(){
        Random rand = new Random();
        choosenCelebNo = rand.nextInt(celebNames.size());
        Image getImage = new Image();
        try {
            Bitmap image = getImage.execute(celebImageURL.get(choosenCelebNo)).get();
            celebImageView.setImageBitmap(image);
            correctOption = rand.nextInt(4);
            for (int i=0;i<4;i++){
                if (i==correctOption){
                    options[i] = celebNames.get(choosenCelebNo);
                } else {
                    int wrongAns = rand.nextInt(celebNames.size());
                    while (wrongAns == choosenCelebNo){
                        wrongAns = rand.nextInt(celebNames.size());
                    }
                    for (int j=0;j<i;j++){
                        if (options[j].equals(celebNames.get(choosenCelebNo))){
                            while (wrongAns == choosenCelebNo){
                                wrongAns = rand.nextInt(celebNames.size());
                            }
                        }
                    }
                    options[i] = celebNames.get(wrongAns);
                }
            }
            optButton0.setText(options[0]);
            optButton1.setText(options[1]);
            optButton2.setText(options[2]);
            optButton3.setText(options[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Image extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream input = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public class download extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            try {
                url = new URL(urls[0]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine;
                while ((inputLine = reader.readLine()) != null){
                    Log.i("Result:", inputLine);
                    result += inputLine;
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        download task = new download();
        String result;
        try {
            result = task.execute("https://www.imdb.com/list/ls052283250/").get();
            String[] splitResult0 = result.split("class=\"footer filmosearch\"");
            String[] splitResult1 = splitResult0[0].split("lister list detail sub-list");
            Pattern p = Pattern.compile("<img alt=\"(.*?)\"");
            Matcher m = p.matcher(splitResult1[1]);
            while (m.find()){
                celebNames.add(m.group(1));
            }
            p = Pattern.compile("src=\"(.*?)\"");
            m = p.matcher(splitResult1[1]);
            while (m.find()){
                celebImageURL.add(m.group(1));
            }

            celebImageView = findViewById(R.id.celebImageView);
            optButton0 = findViewById(R.id.optButton0);
            optButton1 = findViewById(R.id.optButton1);
            optButton2 = findViewById(R.id.optButton2);
            optButton3 = findViewById(R.id.optButton3);

            createQuestion();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
