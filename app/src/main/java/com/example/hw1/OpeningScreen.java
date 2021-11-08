package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class OpeningScreen extends AppCompatActivity {

    private ImageButton play;
    private MediaPlayer opening_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        opening_sound = MediaPlayer.create(OpeningScreen.this, R.raw.opening);
        opening_sound.start();
        findViews();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGame();
            }
        });
    }

    private void findViews() {
        play = this.findViewById(R.id.opening_screen_play);
    }

    private void startGame() {
        Intent i = new Intent(this, Activity_Panel.class);
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