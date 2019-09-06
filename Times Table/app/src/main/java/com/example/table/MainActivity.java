package com.example.table;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SeekBar tableSeekBar;
    ListView tableListView;

    public void generateTable(int tablefor){
        ArrayList<Integer> table = new ArrayList<>();
        for (int i=1;i<11;i++){
            table.add(i*tablefor);
        }
        ArrayAdapter tableAddapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, table);
        tableListView.setAdapter(tableAddapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableSeekBar = (SeekBar) findViewById(R.id.tableSeekBar);
        tableSeekBar.setMax(20);
        int startingPoint = 1;
        tableSeekBar.setProgress(startingPoint);
        tableListView = (ListView) findViewById(R.id.tableListView);
        generateTable(startingPoint);

        tableSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 1;
                int tablefor;
                if (progress < min){
                    tablefor = min;
                } else {
                    tablefor = progress;
                }
                Log.i("Table for:", Integer.toString(tablefor));
                Toast.makeText(getApplicationContext(), "Table of " + tablefor, Toast.LENGTH_SHORT).show();
                generateTable(tablefor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
