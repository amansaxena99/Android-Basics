package com.example.higherorlower;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int number;

    public void generateNewNumber(){
        Random rand = new Random();
        number = rand.nextInt(20) + 1;
    }

    public void guessFunction(View view){
        Log.i("Random number", Integer.toString(number));
        EditText guessNumberText = (EditText) findViewById(R.id.guessNumberText);
        String msg;
        if (guessNumberText.getText().toString().isEmpty()){
            msg = "Please enter a number";
        } else {
            int guessNumber = Integer.parseInt(guessNumberText.getText().toString());
            if (guessNumber > number) {
                msg = "LOWER";
            } else if (guessNumber < number) {
                msg = "HIGHER";
            } else {
                msg = "You got it! Let's go again";
                generateNewNumber();
            }
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateNewNumber();
    }
}
