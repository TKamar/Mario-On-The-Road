package com.example.hw1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.objects.MyDB;
import com.example.hw1.objects.Record;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

class Player {

    private int hearts;
    private int marioPosition;
    private ImageView playerPic;
    private ArrayList<ImageView> playerPosition;

    public Player(ArrayList<ImageView> playerPosition) {
        this.playerPosition = playerPosition;
        this.marioPosition = 1;
        this.hearts = 3;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public int getMarioPosition() {
        return marioPosition;
    }

    public void setMarioPosition(int marioPosition) {
        this.marioPosition = marioPosition;
    }

    public ImageView getPlayerPic() {
        return playerPic;
    }

    public void setPlayerPic(ImageView playerPic) {
        this.playerPic = playerPic;
    }

    public ArrayList<ImageView> getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(ArrayList<ImageView> playerPosition) {
        this.playerPosition = playerPosition;
    }
}

public class Activity_Panel extends AppCompatActivity {

    private MaterialTextView panel_LBL_info;


    private ImageView[][] screen;
    private int[][] values;

    private Player mario;
    private ArrayList<ImageView> marioPosition = new ArrayList<>();

    private ArrayList<ImageView> hearts;
    private ImageView heart1;
    private ImageView heart2;
    private ImageView heart3;
    private ImageView heart4;

    private TextView scoreTxt;
    private int score = 0;

    private ImageButton rightButton;
    private ImageButton leftButton;

    Random obstacleLine = new Random();

    private MediaPlayer collect_coin;

    private MediaPlayer move_left;
    private MediaPlayer move_right;
    private MediaPlayer obstacle;
    private MediaPlayer opening;

    final Handler handler = new Handler();
    private int clock = 10;

    private int clockCounter;
    private int DELAY = 600;

    private SensorManager sensorManager;
    private Sensor accSensor;


    private Runnable r = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(r, DELAY);
            checkCrash();
            moveScreen();

            if (mario.getHearts() == -1) {
                Toast.makeText(Activity_Panel.this, "Game Over", Toast.LENGTH_SHORT).show();
                gameStartingPoint();

            }

            if (clockCounter % 3 == 0) {
                generateScreen();
            }
            clockCounter++;

            if (clockCounter % 10 == 0 && DELAY > 300) {
                increaseSpeed();
            }
        }


    };

    @Override
    protected void onStart() {
        super.onStart();
        startTicker();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void restoreHearts() {
        mario.setHearts(3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        findViews();
        initSensor();
        gameStartingPoint();


        mario = new Player(marioPosition);

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                move_right = MediaPlayer.create(Activity_Panel.this, R.raw.move_right);
                move_right.start();
                moveRight();
            }
        });

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                move_left = MediaPlayer.create(Activity_Panel.this, R.raw.move_left);
                move_left.start();
                moveLeft();
            }
        });


        MyDB myDB = new MyDB();
        for (int i = 0; i < 10; i++) {
            myDB.getRecords().add(new Record()
                    .setTime(System.currentTimeMillis() - (i*1000*60*60*24))
                    .setScore(new Random().nextInt(100))
                    .setLat(i*10)
                    .setLon(i*10)
            );
        }



        String json = new Gson().toJson(myDB);
        MSP.getMe().putString("MY_DB", json);


        String js = MSP.getMe().getString("MY_DB", "");
        MyDB md = new Gson().fromJson(js, MyDB.class);

        int x = 0;

    }


    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            DecimalFormat df = new DecimalFormat("##.##");
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            panel_LBL_info.setText(
                    df.format(x) + "\n" + df.format(y) + "\n" + df.format(z)
            );

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accSensorEventListener);
    }


    private void moveRight() {
        int currentPosition = mario.getMarioPosition() + 1;
        if (currentPosition < 5) {
            mario.setMarioPosition(currentPosition);
            int newPosition = mario.getMarioPosition();
            moveMario(currentPosition - 1, newPosition);
        }

    }

    private void moveLeft() {
        int currentPosition = mario.getMarioPosition() - 1;
        if (currentPosition > -3) {
            mario.setMarioPosition(currentPosition);
            int newPosition = mario.getMarioPosition();
            moveMario(currentPosition + 1, newPosition);
        }
    }

    private void moveMario(int currentPosition, int newPosition) {
        values[5][currentPosition] = 0;
        values[5][newPosition] = 5;
        updateUI();
    }


    private void startTicker() {
        handler.postDelayed(r, DELAY);
    }

    private void stopTicker() {
        handler.removeCallbacks(r);
    }


    private void findViews() {
        hearts = new ArrayList<>();
        screen = new ImageView[][]{
                {findViewById(R.id.panel_IMG_mashroom1), findViewById(R.id.panel_IMG_mashroom2), findViewById(R.id.panel_IMG_mashroom3), findViewById(R.id.panel_IMG_mashroom4), findViewById(R.id.panel_IMG_mashroom5)},
                {findViewById(R.id.panel_IMG_mashroom6), findViewById(R.id.panel_IMG_mashroom7), findViewById(R.id.panel_IMG_mashroom8), findViewById(R.id.panel_IMG_mashroom9), findViewById(R.id.panel_IMG_mashroom10)},
                {findViewById(R.id.panel_IMG_mashroom11), findViewById(R.id.panel_IMG_mashroom12), findViewById(R.id.panel_IMG_mashroom13), findViewById(R.id.panel_IMG_mashroom14), findViewById(R.id.panel_IMG_mashroom15)},
                {findViewById(R.id.panel_IMG_mashroom16), findViewById(R.id.panel_IMG_mashroom17), findViewById(R.id.panel_IMG_mashroom18), findViewById(R.id.panel_IMG_mashroom19), findViewById(R.id.panel_IMG_mashroom20)},
                {findViewById(R.id.panel_IMG_mashroom21), findViewById(R.id.panel_IMG_mashroom22), findViewById(R.id.panel_IMG_mashroom23), findViewById(R.id.panel_IMG_mashroom24), findViewById(R.id.panel_IMG_mashroom25)},
                {findViewById(R.id.panel_IMG_mario1), findViewById(R.id.panel_IMG_mario2), findViewById(R.id.panel_IMG_mario3), findViewById(R.id.panel_IMG_mario4), findViewById(R.id.panel_IMG_mario5)}
        };
        values = new int[screen.length][screen[0].length];

        leftButton = this.findViewById(R.id.panel_IMG_left);
        rightButton = this.findViewById(R.id.panel_IMG_right);

        heart1 = findViewById(R.id.panel_IMG_heart1);
        heart2 = findViewById(R.id.panel_IMG_heart2);
        heart3 = findViewById(R.id.panel_IMG_heart3);
        heart4 = findViewById(R.id.panel_IMG_heart4);

        hearts.add(0, heart1);
        hearts.add(1, heart2);
        hearts.add(2, heart3);
        hearts.add(3, heart4);

        marioPosition.add(findViewById(R.id.panel_IMG_mario1));
        marioPosition.add(findViewById(R.id.panel_IMG_mario2));
        marioPosition.add(findViewById(R.id.panel_IMG_mario3));
        marioPosition.add(findViewById(R.id.panel_IMG_mario4));
        marioPosition.add(findViewById(R.id.panel_IMG_mario5));

        scoreTxt = findViewById(R.id.panel_TXT_score);
    }

    public void gameStartingPoint() {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j] = 0;
            }
        }
        findViews();
        DELAY = 700;
        mario = new Player(marioPosition);
        values[5][1] = 5;
        score = 0;
        updateScore(0);
        mario.setHearts(3);
        resetHearts();
        updateUI();
    }

    private void updateScore(int points) {
        score += points;
        scoreTxt.setText("" + score);
    }


    public void checkCrash() {
        for (int i = 0; i < values[0].length; i++) {
            if (values[4][i] == 1 && values[5][i] == 5) { //Obstacle - red mushroom
                obstacle = MediaPlayer.create(Activity_Panel.this, R.raw.obstacle);
                obstacle.start();
                Vibrator();
                removeHeart();
                removeObstacles();
                break;
            }

            if (values[4][i] == 2 && values[5][i] == 5) { //Obstacle - green mushroom
                obstacle = MediaPlayer.create(Activity_Panel.this, R.raw.obstacle);
                obstacle.start();
                Vibrator();
                increaseSpeed();
                removeHeart();
                removeObstacles();
                break;
            }

            if (values[4][i] == 3 && values[5][i] == 5) { //Coin
                updateScore(10);
                collect_coin = MediaPlayer.create(Activity_Panel.this, R.raw.coin);
                collect_coin.start();
                removeObstacles();
                break;
            }
            if (values[4][i] == 4 && values[5][i] == 5) { //Heart
                addHeart();
                removeObstacles();
                break;
            }


        }

    }

    private void resetHearts() {
        for (int i = 0; i < hearts.size(); i++) {
            hearts.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void removeHeart() {
        int currHeart = mario.getHearts();
        hearts.get(mario.getHearts()).setVisibility(View.INVISIBLE);
        mario.setHearts(currHeart - 1);

        if (mario.getHearts() < 0) {
            gameOverScreen();
        }
    }

    private void addHeart() {
        if (mario.getHearts() < 3) {
            collect_coin = MediaPlayer.create(Activity_Panel.this, R.raw.coin);
            collect_coin.start();
            mario.setHearts(mario.getHearts() + 1);
            hearts.get(mario.getHearts()).setVisibility(View.VISIBLE);
        }

    }

    private void increaseSpeed() {
        DELAY -= 70;
        Toast.makeText(Activity_Panel.this, "Speed game increased", Toast.LENGTH_SHORT).show();
    }

    public void Vibrator(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);
    }


    private void removeObstacles() {
        for (int i = 0; i < values[0].length; i++) {
            values[4][i] = 0;
        }
        updateUI();
    }

    private void generateScreen() {
        int index = obstacleLine.nextInt(5);
        if (clockCounter % 10 == 0) {
            values[0][index] = 2;
            updateScore(1);
        } else if (clockCounter % 15 == 0) {
            values[0][index] = 4;
        } else if (clockCounter % 8 == 0) {
            values[0][index] = 3;
        } else {
            if (clockCounter % 3 == 0)
                values[0][index] = 1;
        }
        updateUI();
    }

    private void moveScreen() {
        int[] valuesOnScreen;
        for (int i = values.length - 2; i > 0; i--) {
            valuesOnScreen = values[i];
            values[i] = values[i - 1];
            values[i - 1] = valuesOnScreen;
        }
        for (int i = 0; i < values[0].length; i++) {
            values[0][i] = 0;
        }
        updateUI();
    }

    private void gameOverScreen() {
        Intent i = new Intent(this, GameOver.class);
        startActivity(i);
    }

    private void updateUI() {
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                ImageView im = screen[i][j];
                if (values[i][j] == 0) {
                    im.setVisibility(View.INVISIBLE);
                } else if (values[i][j] == 1) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.mushroom1);
                } else if (values[i][j] == 2) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.mushroom2);
                } else if (values[i][j] == 3) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.coin);
                } else if (values[i][j] == 4) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.heart);
                } else if (values[i][j] == 5) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.mario1);
                }

            }
        }
    }
}