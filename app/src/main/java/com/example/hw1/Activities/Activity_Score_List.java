package com.example.hw1.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.hw1.Callbacks.Callback_List;
import com.example.hw1.Callbacks.Callback_Map;
import com.example.hw1.Fragments.Fragment_List;
import com.example.hw1.Fragments.Fragment_Map;
import com.example.hw1.objects.MSP;
import com.example.hw1.R;
import com.example.hw1.objects.MyDB;
import com.example.hw1.objects.Record;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Activity_Score_List extends AppCompatActivity {

    private ImageButton back_button;
    private Fragment_List list_fragment;
    private Fragment_Map map_fragment;
    private String score;
    private MyDB myDB;
    double[] myCoordinates = new double[2];
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);
        findViews();
        initFragments();
        loadTop10ScoresFromDB();
        if (myDB == null || myDB.getRecords().size() == 0) {
            myDB = new MyDB();
            for (int i = 0; i < 10; i++) {
                myDB.getRecords().add(new Record()
                        .setScore(0)
                        .setLat(0.0)
                        .setLon(0.0)
                );
            }
        }
        Log.d("myDB is", "myDB is: " + myDB.toString());



        Bundle data = getIntent().getExtras();
        if (data != null) {
            score = data.getString("finalScore");
            myCoordinates[0] = data.getDouble("lat");
            myCoordinates[1] = data.getDouble("lon");
            myDB.getRecords().add(createNewRecord());
        }

        saveToSP();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToGame();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Record createNewRecord() {
        Record r = new Record();
        if (!score.equals(null)) {
            r.setScore(Integer.parseInt(score)).setLat(myCoordinates[0]).setLon(myCoordinates[1]).setTime(String.valueOf(LocalTime.now().format(dtf)));
            return r;
        } else
            return r.setScore(0).setLat(0).setLon(0).setTime(String.valueOf(LocalTime.now().format(dtf)));


    }

    private void returnToGame() {
        Intent i = new Intent(this, OpeningScreen.class);
        finish();
        startActivity(i);
    }

    private void findViews() {
        back_button = this.findViewById(R.id.back_button);
    }

    private void initFragments() {
        map_fragment = new Fragment_Map();
        map_fragment.setActivity(this);
        map_fragment.setCallBack_map(callback_map);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_map, map_fragment).commit();

        list_fragment = new Fragment_List();
        list_fragment.setActivity(this);
        list_fragment.setCallbackList(callback_list);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_list, list_fragment).commit();
    }

    /**
     * SP
     */
    private void saveToSP() {
        myDB.getRecords().sort(new Comparator<Record>() {
            @Override
            public int compare(Record r1, Record r2) {
                if (r1.getScore() == r2.getScore())
                    return 0;
                else if (r1.getScore() > r2.getScore())
                    return -1;
                else
                    return 1;
            }
        });

        myDB = addTop10ScoresFromDB();
        MSP.getMe().putString("MY_DB", new Gson().toJson(myDB));
        Log.d("saveToSP", "saveToSP: " + new Gson().toJson(myDB));
    }

    private void loadTop10ScoresFromDB() {
        myDB = new Gson().fromJson(MSP.getMe().getString("MY_DB", ""), MyDB.class);
        Log.d("loadToSP", "loadToSP: " + myDB.toString());
    }

    private MyDB addTop10ScoresFromDB() {
        ArrayList<Record> res = new ArrayList<>();
        int counter = 0;
        while (res.size() < 10) {
            res.add(myDB.getRecords().get(counter));
            counter++;
        }
        myDB.setRecords(res);
        return myDB;
    }

    /**
     * Callback functions
     */

    Callback_Map callback_map = (lat, lon) -> {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(myMap -> {
            LatLng coordinates = new LatLng(lat, lon);
            myMap.clear();
            myMap.addMarker(new MarkerOptions().position(coordinates).title("Player Location"));
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15), 5000, null);
        });
    };

    Callback_List callback_list = new Callback_List() {
        @Override
        public ArrayList<Record> getRecords() {
            return myDB.getRecords();
        }

        @Override
        public void ZoomOnMap(double lat, double lon) {
            callback_map.mapClicked(lat, lon);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }
}