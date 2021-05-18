package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomAddPerson extends BottomSheetDialogFragment implements View.OnClickListener {

    private static Context mContext;
    private static EditText full_name, birthday;
    private static TextView male, female, additional_features;
    private static RecyclerView recyclerView;
    private static ImageButton image_add_button;
    private static Button add_button;
    private static AdapterFilter adapter;
    private static AdapterClub m_club_adapter;
    private static DataBasePersons mDB;

    private static final String TABLE_CLUB = "club_table";
    
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    ContentValues person_data = new ContentValues();

    public static BottomAddPerson newInstance(Context context, DataBasePersons database, AdapterClub club_adapter) {
        BottomAddPerson fragment = new BottomAddPerson();
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
        View bottomSheetView = inflater.inflate(R.layout.bottom_add_person, container, true);
        // get the views and attach the listener
        full_name = (EditText) bottomSheetView.findViewById(R.id.full_name);
        birthday= (EditText) bottomSheetView.findViewById(R.id.birthday);

        male = (TextView) bottomSheetView.findViewById(R.id.male);
        female = (TextView) bottomSheetView.findViewById(R.id.female);
        additional_features = (TextView) bottomSheetView.findViewById(R.id.additional_features);

        recyclerView = bottomSheetView.findViewById(R.id.qualification);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterFilter(mContext, new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.qualifications))));
        recyclerView.setAdapter(adapter);

        image_add_button = (ImageButton) bottomSheetView.findViewById(R.id.image_add_button);
        add_button = (Button) bottomSheetView.findViewById(R.id.add_button);

        male.setOnClickListener(this);
        female.setOnClickListener(this);
        additional_features.setOnClickListener(this);
        image_add_button.setOnClickListener(this);
        add_button.setOnClickListener(this);
        birthday.setOnClickListener(this);

        person_data.put(COLUMN_NAME, "");
        person_data.put(COLUMN_BIRTHDAY, "");
        person_data.put(COLUMN_GENDER, male.getText().toString());
        person_data.put(COLUMN_QUALIFY, "");
        person_data.put(COLUMN_PHONE, "");
        person_data.put(COLUMN_EMAIL, "");
        Log.d("myLog", String.valueOf(bottomSheetView));
        return bottomSheetView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday:
                Log.d("myLog", "birthday_click");
                
                PickerDate birth = new PickerDate(mContext, birthday);
                birth.show(getFragmentManager(), "Birth");
                break;
                
            case R.id.male:
                Log.d("myLog", "male click");
                male.setBackground(mContext.getResources().getDrawable(R.drawable.background_gender_item));
                female.setBackground(null);
                person_data.put(COLUMN_GENDER, male.getText().toString());
                break;
            case R.id.female:
                Log.d("myLog", "female click");
                male.setBackground(null);
                female.setBackground(mContext.getResources().getDrawable(R.drawable.background_gender_item));
                person_data.put(COLUMN_GENDER, female.getText().toString());
                break;
            case R.id.additional_features:
                Toast.makeText(mContext, "Oпция пока недоступна", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_button:
            case R.id.image_add_button:
                person_data.put(COLUMN_NAME, full_name.getText().toString());
                person_data.put(COLUMN_BIRTHDAY, birthday.getText().toString());
                person_data.put(COLUMN_QUALIFY, adapter.getChoice());
                Log.d("myLog", String.valueOf(person_data));
                mDB.addRec(TABLE_CLUB, person_data);
                m_club_adapter.addItem(mDB.getAllData(TABLE_CLUB));
                dismiss();
                break;
        }
    }
}