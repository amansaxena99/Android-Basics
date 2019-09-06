package com.example.timerapp;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView time;
    SeekBar timeSeekBar;
    boolean active = false;
    Button startStop;
    CountDownTimer countDownTimer;

    public void restart(){
        active = false;
        startStop.setText("Start");
        timeSeekBar.setProgress(300);
        time.setText("05:00");
        countDownTimer.cancel();
        timeSeekBar.setEnabled(true);
    }

    public void startStop(View view){
        if(!active) {
            active = true;
            startStop.setText("Stop");
            timeSeekBar.setEnabled(false);
            countDownTimer = new CountDownTimer(timeSeekBar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    MediaPlayer horn = MediaPlayer.create(getApplicationContext(), R.raw.horn);
                    horn.start();
                    restart();
                }
            }.start();
        } else {
            restart();
        }

    }

    public void updateTimer(int timeLeft){
        int min = timeLeft/60;
        int sec = timeLeft - (min * 60);
        String min1 = Integer.toString(min);
        if (min1.equals("10")){
            min1 = min1;
        } else {
            min1 = "0" + min1;
        }
        String sec1 = Integer.toString(sec);
        if (sec1.equals("0") || sec1.equals("1") || sec1.equals("2") || sec1.equals("3") || sec1.equals("4") || sec1.equals("5") || sec1.equals("6") || sec1.equals("7") || sec1.equals("8") || sec1.equals("9")){
            sec1 = "0" + sec1;
        }
        time.setText(min1 + ":" + sec1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startStop = (Button) findViewById(R.id.startStopButton);

        timeSeekBar = (SeekBar) findViewById(R.id.seekBar);
        timeSeekBar.setMax(600);
        time = (TextView) findViewById(R.id.timeRemainingTextView);
        time.setText("05:00");
        timeSeekBar.setProgress(300);
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
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
