package com.example.hw1.Callbacks;

import com.example.hw1.objects.Record;
import java.util.ArrayList;

public interface Callback_List {

    ArrayList<Record> getRecords();
    void ZoomOnMap(double lat, double lon);
}
