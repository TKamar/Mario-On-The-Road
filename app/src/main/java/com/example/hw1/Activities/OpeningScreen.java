package com.example.hw1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.hw1.R;

public class OpeningScreen extends AppCompatActivity {

    private ImageButton sensors_mode;
    private ImageButton buttons_mode;
    private MediaPlayer opening_sound;
    private ImageButton exit_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        opening_sound = MediaPlayer.create(OpeningScreen.this, R.raw.opening);
        opening_sound.start();
        findViews();

        sensors_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGame("sensors");
            }
        });

        buttons_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGame("buttons");
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }

    private void findViews() {
        sensors_mode = this.findViewById(R.id.opening_screen_play_sensorButton);
        buttons_mode = this.findViewById(R.id.opening_screen_play_buttonsButton);
        exit_button = findViewById(R.id.exit_button_opening_screen);
    }

    private void startGame(String mode) {

        Intent i = new Intent(this, Activity_Panel.class);
        i.putExtra("mode", mode);
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