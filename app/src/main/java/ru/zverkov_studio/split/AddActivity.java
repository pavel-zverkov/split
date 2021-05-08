package ru.zverkov_studio.split;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class AddActivity extends BottomSheetDialogFragment implements View.OnClickListener {

    private static Context mContext;
    private static EditText event_name, create_date;
    private static ImageView run, ski, orient, tour, click, mass, interval;
    private static ImageButton image_continue_button;
    private static Button continue_button;

    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_CREATE_DATE = "create_date";
    public static final String COLUMN_KIND_SPORT = "kind_sport";
    public static final String COLUMN_KIND_START = "kind_start";

    ContentValues person_data = new ContentValues();

    public static AddActivity newInstance(Context context) {
        AddActivity fragment = new AddActivity();
        mContext = context;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View bottomSheetView = inflater.inflate(R.layout.add_activity, container, true);
        // get the views and attach the listener
        event_name = (EditText) bottomSheetView.findViewById(R.id.event_name);
        create_date = (EditText) bottomSheetView.findViewById(R.id.create_date);

        run = (ImageView) bottomSheetView.findViewById(R.id.run);
        ski = (ImageView) bottomSheetView.findViewById(R.id.ski);
        orient = (ImageView) bottomSheetView.findViewById(R.id.orient);
        tour = (ImageView) bottomSheetView.findViewById(R.id.tour);
        click = (ImageView) bottomSheetView.findViewById(R.id.click);
        mass = (ImageView) bottomSheetView.findViewById(R.id.mass);
        interval = (ImageView) bottomSheetView.findViewById(R.id.interval);

        image_continue_button = (ImageButton) bottomSheetView.findViewById(R.id.image_continue_button);
        continue_button = (Button) bottomSheetView.findViewById(R.id.continue_button);

        create_date.setOnClickListener(this);
        run.setOnClickListener(this);
        ski.setOnClickListener(this);
        orient.setOnClickListener(this);
        tour.setOnClickListener(this);
        click.setOnClickListener(this);
        mass.setOnClickListener(this);
        interval.setOnClickListener(this);
        image_continue_button.setOnClickListener(this);
        continue_button.setOnClickListener(this);

        Log.d("myLog", String.valueOf(bottomSheetView));
        return bottomSheetView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_date:
                Log.d("myLog", "birthday_click");

                BirthdayPicker birth = new BirthdayPicker(mContext, create_date);
                birth.show(getFragmentManager(), "Birth");
                break;
            case R.id.run:
                run.setImageResource(R.drawable.ic_active_run);
                ski.setImageResource(R.drawable.ic_ski);
                orient.setImageResource(R.drawable.ic_orient);
                tour.setImageResource(R.drawable.ic_tourism);
                break;
            case R.id.ski:
                run.setImageResource(R.drawable.ic_run);
                ski.setImageResource(R.drawable.ic_active_ski);
                orient.setImageResource(R.drawable.ic_orient);
                tour.setImageResource(R.drawable.ic_tourism);
                break;
            case R.id.orient:
                run.setImageResource(R.drawable.ic_run);
                ski.setImageResource(R.drawable.ic_ski);
                orient.setImageResource(R.drawable.ic_active_oreint);
                tour.setImageResource(R.drawable.ic_tourism);
                break;
            case R.id.tour:
                run.setImageResource(R.drawable.ic_run);
                ski.setImageResource(R.drawable.ic_ski);
                orient.setImageResource(R.drawable.ic_orient);
                tour.setImageResource(R.drawable.ic_active_tourism);
                break;
            case R.id.click:
                click.setImageResource(R.drawable.ic_active_click);
                mass.setImageResource(R.drawable.ic_mass_start);
                interval.setImageResource(R.drawable.ic_interval_start);
                break;
            case R.id.mass:
                click.setImageResource(R.drawable.ic_click_start);
                mass.setImageResource(R.drawable.ic_active_mass);
                interval.setImageResource(R.drawable.ic_interval_start);
                break;
            case R.id.interval:
                click.setImageResource(R.drawable.ic_click_start);
                mass.setImageResource(R.drawable.ic_mass_start);
                interval.setImageResource(R.drawable.ic_active_interval);
                break;
            case R.id.continue_button:
            case R.id.image_continue_button:
                Intent intent = new Intent(mContext, CreateActivity.class);
                dismiss();
                startActivity(intent);
                break;
        }
    }
}

