package ru.zverkov_studio.split;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int float_button_mode;
    DataBase club;
    ClubAdapter adapter;
    ArrayList list = new ArrayList();
    String[] row = new String[3];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        row[0] = "hello";
        row[1] = "world";
        row[2] = "!";
        list.add(row);
        Log.d("myLog", "list" + String.valueOf(list.get(0)));

        open_DB();

        FloatingActionButton float_button = (FloatingActionButton) findViewById(R.id.float_button);
        float_button.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.calendar);
        float_button_mode = R.id.calendar;
        float_button.setImageResource(R.drawable.ic_watch);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new CalendarFragment()).commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.filters:
                        break;
                    case R.id.club:
                        selectedFragment = new ClubFragment(MainActivity.this, club, adapter);
                        float_button_mode = R.id.club;
                        float_button.setImageResource(R.drawable.ic_big_plus);
                        break;
                    case R.id.calendar:
                        selectedFragment = new CalendarFragment();
                        float_button_mode = R.id.calendar;
                        float_button.setImageResource(R.drawable.ic_watch);
                        break;
                    case R.id.settings:
                        break;
                }
                
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                Log.d("myLog", "Replace");
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_button:
                switch (float_button_mode){
                    case R.id.club:
                        add_person();
                        break;
                    case R.id.calendar:
                        add_activity();
                        break;
                }

        }
    }

    public void add_person() {
        Log.d("myLog", String.valueOf(this) + " " + String.valueOf(MainActivity.this));
        AddPerson bottomSheetDialog = AddPerson.newInstance(this, club, adapter);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    public void add_activity() {
        Log.d("myLog", String.valueOf(this) + " " + String.valueOf(MainActivity.this));
        AddActivity bottomSheetDialog = AddActivity.newInstance(this);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    public void open_DB(){
        club = new DataBase(this);
        club.open();
        adapter = new ClubAdapter(this, club);
    }
}
