package com.example.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 0: Tick, 1: Cross, 2: Empty
    int[] gameStat = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    int currentPlayer = 0;
    boolean gameEnd = false;


    public void placeImage(View view){
        if (!gameEnd) {
            ImageView placePlayerImage = (ImageView) view;
            int tappedPlace = Integer.parseInt(placePlayerImage.getTag().toString());
            if (gameStat[tappedPlace] == 2) {
                gameStat[tappedPlace] = currentPlayer;
                if (currentPlayer == 0) {
                    placePlayerImage.setImageResource(R.drawable.cross);
                    currentPlayer = 1;
                } else {
                    placePlayerImage.setImageResource(R.drawable.tick);
                    currentPlayer = 0;
                }
                placePlayerImage.animate().alpha(1).setDuration(1000);

                for (int[] winningPositions : winningPositions) {
                    String winner;
                    if (currentPlayer == 0) {
                        winner = "Tick";
                    } else {
                        winner = "Cross";
                    }
                    if (gameStat[winningPositions[0]] != 2 && gameStat[winningPositions[0]] == gameStat[winningPositions[1]] && gameStat[winningPositions[1]] == gameStat[winningPositions[2]]) {
                        Toast.makeText(this, "Player with " + winner + " has Won", Toast.LENGTH_SHORT).show();
                        gameEnd = true;

                        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
                        TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
                        winnerTextView.setText("Player with " + winner + " has Won");
                        playAgainButton.setText("Play Again");
                        playAgainButton.setVisibility(View.VISIBLE);
                        winnerTextView.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                Toast.makeText(this, "Pick another place", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Game has ended click on Play Again", Toast.LENGTH_SHORT).show();
        }

    }

    public void playAgain(View view){
        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
        TextView winnerTextView = (TextView) findViewById(R.id.winnerTextView);
        playAgainButton.setText("Restart");
        winnerTextView.setVisibility(View.INVISIBLE);

        GridLayout myGrid = (GridLayout) findViewById(R.id.gridLayout);
        for (int i=0;i<myGrid.getChildCount();i++){
            ImageView counter = (ImageView) myGrid.getChildAt(i);
            counter.setImageDrawable(null);
        }

        for (int i=0;i<gameStat.length;i++) {
            gameStat[i] = 2;
        }
        currentPlayer = 0;
        gameEnd = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
