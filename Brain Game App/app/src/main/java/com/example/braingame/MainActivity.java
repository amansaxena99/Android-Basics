package com.example.braingame;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button playAgainButton;
    TextView  quesTextView;
    TextView  scoreTextView;
    TextView  timeLeftTextView;
    ArrayList<Integer> answers = new ArrayList<>();
    Button ansButton0, ansButton1, ansButton2, ansButton3;
    int tagForCorrectAns;
    TextView resultTextView;
    int score = 0, quesAttemted = 0;

    public void playAgain(View view){
        playAgainButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        score = 0;
        quesAttemted = 0;
        timeLeftTextView.setText("30s");
        scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(quesAttemted));
        resultTextView.setVisibility(View.INVISIBLE);
        gameStart(view);
    }

    public void gameStart(View view){
        startButton.setVisibility(View.INVISIBLE);
        quesTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        timeLeftTextView.setVisibility(View.VISIBLE);
        ansButton0.setVisibility(View.VISIBLE);
        ansButton1.setVisibility(View.VISIBLE);
        ansButton2.setVisibility(View.VISIBLE);
        ansButton3.setVisibility(View.VISIBLE);
        newQues();
        new CountDownTimer(30100, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftTextView.setText(String.valueOf((millisUntilFinished/1000) + "s"));
            }

            @Override
            public void onFinish() {
                resultTextView.setText("Time's up!");
                resultTextView.setVisibility(View.VISIBLE);
                playAgainButton.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void answer(View view){
        if (timeLeftTextView.getText().equals("0s")){

        } else {
            if (Integer.toString(tagForCorrectAns).equals(view.getTag().toString())) {
                resultTextView.setText("Correct! :)");
                resultTextView.setVisibility(View.VISIBLE);
                score++;
                quesAttemted++;
                scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(quesAttemted));
                newQues();
            } else {
                resultTextView.setText("Incorrect! :/");
                resultTextView.setVisibility(View.VISIBLE);
                quesAttemted++;
                scoreTextView.setText(Integer.toString(score) + "/" + Integer.toString(quesAttemted));
                newQues();
            }
        }
    }

    public void newQues(){
        Random rand = new Random();
        int a = rand.nextInt(51);
        int b = rand.nextInt(50);
        quesTextView.setText(Integer.toString(a) + " + " + Integer.toString(b));
        int ans = a + b;
        tagForCorrectAns = rand.nextInt(4);
        answers.clear();
        for (int i=0;i<4;i++){
            if (i == tagForCorrectAns){
                answers.add(ans);
            } else {
                int wrongAns = rand.nextInt(100);
                while (wrongAns == ans){
                    wrongAns = rand.nextInt(100);
                }
                for (int j=0;j<i;j++){
                    if (answers.get(j) == wrongAns){
                        while (wrongAns == ans){
                            wrongAns = rand.nextInt(100);
                        }
                    }
                }
                answers.add(wrongAns);
            }
        }
        ansButton0.setText(Integer.toString(answers.get(0)));
        ansButton1.setText(Integer.toString(answers.get(1)));
        ansButton2.setText(Integer.toString(answers.get(2)));
        ansButton3.setText(Integer.toString(answers.get(3)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setVisibility(View.VISIBLE);
        quesTextView = (TextView) findViewById(R.id.quesTextView);
        quesTextView.setVisibility(View.INVISIBLE);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        resultTextView.setVisibility(View.INVISIBLE);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        scoreTextView.setVisibility(View.INVISIBLE);
        timeLeftTextView = (TextView) findViewById(R.id.timeLeftTextView);
        timeLeftTextView.setVisibility(View.INVISIBLE);

        ansButton0 = (Button) findViewById(R.id.ansButton0);
        ansButton1 = (Button) findViewById(R.id.ansButton1);
        ansButton2 = (Button) findViewById(R.id.ansButton2);
        ansButton3 = (Button) findViewById(R.id.ansButton3);
        ansButton0.setVisibility(View.INVISIBLE);
        ansButton1.setVisibility(View.INVISIBLE);
        ansButton2.setVisibility(View.INVISIBLE);
        ansButton3.setVisibility(View.INVISIBLE);

        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        playAgainButton.setVisibility(View.INVISIBLE);

    }
}
