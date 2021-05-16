package ru.zverkov_studio.split;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class ProxyList {

    ArrayList activity_list = new ArrayList();

    public void add_to_activity(String[] row){
        Log.d("PROXY", "ADD " + row[0]);
        activity_list.add(row);
    }

    public void remove_from_activity(String[] row){
        Log.d("PROXY", "REMOVE " + row[0]);
        activity_list.remove(row);
    }

    public ArrayList get_data(){
        return activity_list;
    }
    public int getCount() { return activity_list.size(); }
}
