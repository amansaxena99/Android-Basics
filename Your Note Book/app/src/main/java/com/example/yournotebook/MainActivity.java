package com.example.yournotebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences notesSharedPreferences;
    ListView notesListView;
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> notesAdapter;
    TextView msgTextView;

    public static void saveData(ArrayList<String> notes1){
        HashSet<String> notesSet = new HashSet<>(notes1);
        notesSharedPreferences.edit().putStringSet("notes", notesSet).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.addNote:
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                startActivity(intent);
                msgTextView.setVisibility(View.INVISIBLE);
                return true;
            case R.id.help:
                Intent intentHelp = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intentHelp);
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msgTextView = findViewById(R.id.msgTextView);
        notesSharedPreferences = getSharedPreferences("com.example.yournotebook", Context.MODE_PRIVATE);
        notesListView = findViewById(R.id.notesListView);

        HashSet<String> set = (HashSet<String>) notesSharedPreferences.getStringSet("notes", null);
        if (set != null){
            notes = new ArrayList<>(set);
        }

        notesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = 110;
                view.setLayoutParams(params);
                return view;
            }
        };

        notesListView.setAdapter(notesAdapter);
        if (notes.size() == 0){
            msgTextView.setVisibility(View.VISIBLE);
        }

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
                intent.putExtra("notePosition", position);
                startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                notesAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Note Deleted!", Toast.LENGTH_SHORT).show();
                                saveData(notes);
                                if (notes.size() == 0){
                                    msgTextView.setVisibility(View.VISIBLE);
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }
}
