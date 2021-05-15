package ru.zverkov_studio.split;


import androidx.annotation.Nullable;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
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

import java.util.ArrayList;

public class ClubCreateFragment extends Fragment {

    final String TAG = "myLog";

    Context mContext;
    View club_create_fragment;
    ProxyList activity_data;


    public ClubCreateFragment(Context context, ProxyList data){
        activity_data = data;
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        club_create_fragment = inflater.inflate(R.layout.club_create_fragment, container, false);
        Log.d("myLog", "Inflate club_fragment");
        create_recyclerview();
        return club_create_fragment;
    }

    public void create_recyclerview() {
        Log.d("myLog", "Creating recyclerview");
        RecyclerView club_list = club_create_fragment.findViewById(R.id.club_create_list);
        club_list.setLayoutManager(new LinearLayoutManager(mContext));
        ClubCreateAdapter adapter = new ClubCreateAdapter(mContext, new DBtoList(mContext).get_data(), activity_data);
        club_list.setAdapter(adapter);
        Log.d("myLog", String.valueOf(mContext) + " " + String.valueOf(club_create_fragment.getContext()));

        ClubCreateItemTouchHelper simpleItemTouchCallback = new ClubCreateItemTouchHelper(club_create_fragment.getContext(), club_list, 0, ItemTouchHelper.LEFT);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(club_list);
    }
}