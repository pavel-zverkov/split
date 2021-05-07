package ru.zverkov_studio.split;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.DatabaseMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import java.util.Locale;

public class AddPerson extends BottomSheetDialogFragment implements View.OnClickListener {

    private static Context mContext;
    private static EditText full_name, birthday;
    private static TextView male, female;
    private static RecyclerView recyclerView;
    private static ImageButton image_add_button;
    private static Button add_button;
    private static FilterAdapter adapter;
    private static ClubAdapter m_club_adapter;
    private static DataBase mDB;


    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";

    ContentValues person_data = new ContentValues();

    public static AddPerson newInstance(Context context, DataBase database, ClubAdapter club_adapter) {
        AddPerson fragment = new AddPerson();
        m_club_adapter = club_adapter;
        mDB = database;
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
        View bottomSheetView = inflater.inflate(R.layout.add_person, container, true);
        // get the views and attach the listener
        full_name = (EditText) bottomSheetView.findViewById(R.id.full_name);
        birthday= (EditText) bottomSheetView.findViewById(R.id.birthday);

        male = (TextView) bottomSheetView.findViewById(R.id.male);
        female = (TextView) bottomSheetView.findViewById(R.id.female);

        recyclerView = bottomSheetView.findViewById(R.id.qualification);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        adapter = new FilterAdapter(mContext, new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.qualifications))));
        recyclerView.setAdapter(adapter);

        image_add_button = (ImageButton) bottomSheetView.findViewById(R.id.image_add_button);
        add_button = (Button) bottomSheetView.findViewById(R.id.add_button);

        male.setOnClickListener(this);
        female.setOnClickListener(this);
        image_add_button.setOnClickListener(this);
        add_button.setOnClickListener(this);
        birthday.setOnClickListener(this);

        person_data.put(COLUMN_NAME, "");
        person_data.put(COLUMN_BIRTHDAY, "");
        person_data.put(COLUMN_GENDER, male.getText().toString());
        person_data.put(COLUMN_QUALIFY, "");
        Log.d("myLog", String.valueOf(bottomSheetView));
        return bottomSheetView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday:
                Log.d("myLog", "birthday_click");
                
                BirthdayPicker birth = new BirthdayPicker(mContext, birthday);
                birth.show(getFragmentManager(), "Birth");
                break;
                
            case R.id.male:
                Log.d("myLog", "male click");
                male.setBackground(mContext.getResources().getDrawable(R.drawable.gender_item_background));
                female.setBackground(null);
                person_data.put(COLUMN_GENDER, male.getText().toString());
                break;
            case R.id.female:
                Log.d("myLog", "female click");
                male.setBackground(null);
                female.setBackground(mContext.getResources().getDrawable(R.drawable.gender_item_background));
                person_data.put(COLUMN_GENDER, female.getText().toString());
                break;
            case R.id.add_button:
            case R.id.image_add_button:
                person_data.put(COLUMN_NAME, full_name.getText().toString());
                person_data.put(COLUMN_BIRTHDAY, birthday.getText().toString());
                person_data.put(COLUMN_QUALIFY, adapter.getChoice());
                Log.d("myLog", String.valueOf(person_data));
                mDB.addRec(person_data);
                m_club_adapter.addItem(mDB.getAllData());
                dismiss();
                break;
        }
    }
}

