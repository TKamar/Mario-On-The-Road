package com.example.hw1.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hw1.Callbacks.Callback_Map;
import com.example.hw1.R;


public class Fragment_Map extends Fragment {



    public Fragment_Map() {
        // Required empty public constructor
    }

    private AppCompatActivity activity;
    private Callback_Map callBack_map;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBack_map(Callback_Map callBack_map) {
        this.callBack_map = callBack_map;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__map, container, false);
        return view;
    }
}