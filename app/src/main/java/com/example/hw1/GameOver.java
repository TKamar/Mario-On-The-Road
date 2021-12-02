package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GameOver extends AppCompatActivity {

    private ImageButton rePlay;
    private MediaPlayer game_over_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        game_over_sound = MediaPlayer.create(GameOver.this, R.raw.mario_dies);
        game_over_sound.start();
        findViews();

        rePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToOpenScreen();
            }
        });
    }

    private void findViews() {
        rePlay = this.findViewById(R.id.opening_screen_play_button);
    }

    private void returnToOpenScreen() {
        Intent i = new Intent(this, OpeningScreen.class);
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