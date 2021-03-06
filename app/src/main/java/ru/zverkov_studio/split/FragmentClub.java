package ru.zverkov_studio.split;


import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

public class FragmentClub extends Fragment {

    final String TAG = "myLog";
    DataBasePersons club;
    AdapterClub adapter;
    Context mContext;
    View club_fragment;


    public FragmentClub(Context context, DataBasePersons dataBasePersons, AdapterClub adapterClub){
        club = dataBasePersons;
        mContext = context;
        adapter = adapterClub;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        club_fragment = inflater.inflate(R.layout.fragment_club, container, false);
        Log.d("myLog", "Inflate club_fragment");

        create_recyclerview();
        return club_fragment;
    }

    public void create_recyclerview() {
        Log.d("myLog", "Creating recyclerview");
        RecyclerView club_list = club_fragment.findViewById(R.id.club_list);
        club_list.setLayoutManager(new LinearLayoutManager(mContext));
        club_list.setAdapter(adapter);
        Log.d("myLog", String.valueOf(mContext) + " " + String.valueOf(club_fragment.getContext()));


        ItemTouchHelperClub simpleItemTouchCallback = new ItemTouchHelperClub(club_fragment.getContext(), club_list, 0, ItemTouchHelper.LEFT);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(club_list);
    }
}