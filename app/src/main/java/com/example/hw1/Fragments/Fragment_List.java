package com.example.hw1.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hw1.Callbacks.Callback_List;
import com.example.hw1.R;
import com.example.hw1.objects.Record;

import java.util.ArrayList;


public class Fragment_List extends Fragment {

    private AppCompatActivity activity;
    private Callback_List callbackList;
    private ListView top10scores;
    private ArrayList<Record> scores;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }
    public void setCallbackList(Callback_List callbackList) {
        this.callbackList = callbackList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__list, container, false);

        findViews(view);
        scores = callbackList.getRecords();

        ArrayAdapter<Record> listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, scores);

        top10scores.setAdapter(listViewAdapter);

        top10scores.setOnItemClickListener((parent, view1, position, id) ->
        {
            Record r = (Record) parent.getItemAtPosition(position);
            callbackList.ZoomOnMap(r.getLat(), r.getLon());
        });
        return view;
    }

    private void findViews(View view) {
        top10scores = (ListView) view.findViewById(R.id.score_top10_list);
    }

}