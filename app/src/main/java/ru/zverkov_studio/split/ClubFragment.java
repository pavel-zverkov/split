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

public class ClubFragment extends Fragment {

    final String TAG = "myLog";
    DataBase club;
    ClubAdapter adapter;
    Context mContext;
    View club_fragment;

    public ClubFragment(Context context, DataBase dataBase, ClubAdapter clubAdapter){
        club = dataBase;
        mContext = context;
        adapter = clubAdapter;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        club_fragment = inflater.inflate(R.layout.club_fragment, container, false);
        Log.d("myLog", "Inflate club_fragment");

        create_recyclerview();
        return club_fragment;
    }

    public void create_recyclerview() {
        Log.d("myLog", "Creating recyclerview");
        RecyclerView club_list = club_fragment.findViewById(R.id.club_list);
        club_list.setLayoutManager(new LinearLayoutManager(club_fragment.getContext()));
        club_list.setAdapter(adapter);

        ClubRemoveItem simpleItemTouchCallback = new ClubRemoveItem(club_fragment.getContext(), club_list, 0, ItemTouchHelper.LEFT);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(club_list);
    }
}