package ru.zverkov_studio.split;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class BottomAddActivity extends BottomSheetDialogFragment implements View.OnClickListener {

    private static Context mContext;
    private static EditText event_name, create_date;
    private static ImageView run, ski, orient, tour, click, mass, interval;
    private static TextView additional_features;
    private static ImageButton image_continue_button;
    private static Button continue_button;

    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_CREATE_DATE = "create_date";
    public static final String COLUMN_KIND_SPORT = "kind_sport";
    public static final String COLUMN_KIND_START = "kind_start";

    public static final HashMap<String, String> input_data = new HashMap<String, String>();

    ContentValues person_data = new ContentValues();

    public static BottomAddActivity newInstance(Context context) {
        BottomAddActivity fragment = new BottomAddActivity();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View bottomSheetView = inflater.inflate(R.layout.bottom_add_activity, container, true);
        // get the views and attach the listener
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();

        event_name = (EditText) bottomSheetView.findViewById(R.id.event_name);
        event_name.setText(String.format("Тренировка_%s", String.valueOf(dtf.format(now))));

        create_date = (EditText) bottomSheetView.findViewById(R.id.create_date);
        create_date.setText(String.valueOf(dtf.format(now)));

        additional_features = bottomSheetView.findViewById(R.id.activity_additional_features);

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
        additional_features.setOnClickListener(this);
        run.setOnClickListener(this);
        ski.setOnClickListener(this);
        orient.setOnClickListener(this);
        tour.setOnClickListener(this);
        click.setOnClickListener(this);
        mass.setOnClickListener(this);
        interval.setOnClickListener(this);
        image_continue_button.setOnClickListener(this);
        continue_button.setOnClickListener(this);

        input_data.put(COLUMN_KIND_SPORT, "ic_run");
        input_data.put(COLUMN_KIND_START, "click");

        Log.d("myLog", String.valueOf(bottomSheetView));
        return bottomSheetView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_date:
                Log.d("myLog", "birthday_click");

                PickerDate birth = new PickerDate(mContext, create_date);
                birth.show(getFragmentManager(), "Birth");

                break;

            case R.id.run:
                run.setImageResource(R.drawable.ic_active_run);
                ski.setImageResource(R.drawable.ic_ski);
                orient.setImageResource(R.drawable.ic_orient);
                tour.setImageResource(R.drawable.ic_tourism);
                input_data.put(COLUMN_KIND_SPORT, "ic_run");
                break;
            case R.id.ski:
                run.setImageResource(R.drawable.ic_run);
                ski.setImageResource(R.drawable.ic_active_ski);
                orient.setImageResource(R.drawable.ic_orient);
                tour.setImageResource(R.drawable.ic_tourism);
                input_data.put(COLUMN_KIND_SPORT, "ic_ski");
                break;
            case R.id.orient:
                run.setImageResource(R.drawable.ic_run);
                ski.setImageResource(R.drawable.ic_ski);
                orient.setImageResource(R.drawable.ic_active_oreint);
                tour.setImageResource(R.drawable.ic_tourism);
                input_data.put(COLUMN_KIND_SPORT, "ic_orient");
                break;
            case R.id.tour:
                run.setImageResource(R.drawable.ic_run);
                ski.setImageResource(R.drawable.ic_ski);
                orient.setImageResource(R.drawable.ic_orient);
                tour.setImageResource(R.drawable.ic_active_tourism);
                input_data.put(COLUMN_KIND_SPORT, "ic_tourism");
                break;
            case R.id.click:
                click.setImageResource(R.drawable.ic_active_click);
                mass.setImageResource(R.drawable.ic_mass_start);
                interval.setImageResource(R.drawable.ic_interval_start);
                input_data.put(COLUMN_KIND_START, "click");
                break;
            case R.id.mass:
                click.setImageResource(R.drawable.ic_click_start);
                mass.setImageResource(R.drawable.ic_active_mass);
                interval.setImageResource(R.drawable.ic_interval_start);
                input_data.put(COLUMN_KIND_START, "mass");
                break;
            case R.id.interval:
                click.setImageResource(R.drawable.ic_click_start);
                mass.setImageResource(R.drawable.ic_mass_start);
                interval.setImageResource(R.drawable.ic_active_interval);
                input_data.put(COLUMN_KIND_START, "interval");
                break;

            case R.id.continue_button:
            case R.id.image_continue_button:
                input_data.put(COLUMN_EVENT_NAME, String.valueOf(event_name.getText()));
                input_data.put(COLUMN_CREATE_DATE, String.valueOf(create_date.getText()));

                Intent intent = new Intent(mContext, ActivityCreate.class);
                intent.putExtra(COLUMN_EVENT_NAME, input_data.get(COLUMN_EVENT_NAME));
                intent.putExtra(COLUMN_CREATE_DATE, input_data.get(COLUMN_CREATE_DATE));
                intent.putExtra(COLUMN_KIND_SPORT, input_data.get(COLUMN_KIND_SPORT));
                intent.putExtra(COLUMN_KIND_START, input_data.get(COLUMN_KIND_START));
                dismiss();
                startActivity(intent);
                break;
            case R.id.activity_additional_features:
                Toast.makeText(getContext(), "Эта функция пока недоступна", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

