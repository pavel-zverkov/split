package ru.zverkov_studio.split;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalendarFragment extends Fragment {
    View calendar_fragment;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calendar_fragment = inflater.inflate(R.layout.calendar_fragment, container, false);
        Log.d("myLog", "Inflate calendar_fragment");
        return calendar_fragment;
    }
}
