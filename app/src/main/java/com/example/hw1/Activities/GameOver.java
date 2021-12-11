package com.example.hw1.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.hw1.R;
import com.example.hw1.objects.MyDB;

import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class GameOver extends AppCompatActivity {

    private ImageButton rePlay;
    private ImageButton scoreList;
    private MediaPlayer game_over_sound;
    private String score;
    private MyDB myDB;
    double[] myCoordinates = new double[2];
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    private ImageButton exit_button;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        game_over_sound = MediaPlayer.create(GameOver.this, R.raw.mario_dies);
        game_over_sound.start();
        findViews();

        Bundle data = getIntent().getExtras();
        if (data != null) {
            score = data.getString("finalScore");
            myCoordinates[0] = data.getDouble("lat");
            myCoordinates[1] = data.getDouble("lon");
        }

        rePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToOpenScreen();
            }
        });

        scoreList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScoreList();
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

    }

    private void findViews() {
        rePlay = this.findViewById(R.id.opening_screen_play_again_button);
        scoreList = this.findViewById(R.id.opening_screen_score_list_button);
        exit_button = this.findViewById(R.id.exit_button);
    }

    private void returnToOpenScreen() {
        Intent i = new Intent(this, OpeningScreen.class);
        i.putExtra("finalScore", String.valueOf(score));
        i.putExtra("lat", Double.valueOf(myCoordinates[0]));
        i.putExtra("lon", Double.valueOf(myCoordinates[1]));
        finish();
        startActivity(i);
    }

    private void openScoreList() {
        Intent i = new Intent(this, Activity_Score_List.class);
        i.putExtra("finalScore", String.valueOf(score));
        i.putExtra("lat", Double.valueOf(myCoordinates[0]));
        i.putExtra("lon", Double.valueOf(myCoordinates[1]));
        finish();
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}