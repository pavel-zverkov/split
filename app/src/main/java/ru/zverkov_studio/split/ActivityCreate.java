package ru.zverkov_studio.split;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ActivityCreate extends AppCompatActivity implements View.OnClickListener {

    int float_button_mode;
    DataBasePersons persons;
    DataBaseEvents activities;
    ProxyList activity_data = new ProxyList();
    BottomNavigationView bottomNavigationView;
    BottomAppBar bottomAppBar;

    boolean start = false;

    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_CREATE_DATE = "create_date";
    public static final String COLUMN_KIND_SPORT = "kind_sport";
    public static final String COLUMN_KIND_START = "kind_start";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        open_DB();

        FloatingActionButton float_button = (FloatingActionButton) findViewById(R.id.float_button_create);
        float_button.setOnClickListener(this);

        bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_bar_create);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_create);
        bottomNavigationView.setSelectedItemId(R.id.play_list);
        float_button.setImageResource(R.drawable.ic_play);
        float_button_mode = R.id.play_list;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create,
                new FragmentPlayList(ActivityCreate.this, activity_data, start)).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.filters_create:
                    case R.id.settings_create:
                        Toast.makeText(ActivityCreate.this, "Эта функция пока недоступна", Toast.LENGTH_SHORT);
                        break;
                    case R.id.club_create:
                        selectedFragment = new FragmentClubCreate(ActivityCreate.this);
                        float_button_mode = R.id.club_create;
                        float_button.setImageResource(R.drawable.ic_big_plus);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create,
                                selectedFragment).commit();
                        break;
                    case R.id.play_list:
                        selectedFragment = new FragmentPlayList(ActivityCreate.this, activity_data, start);
                        float_button_mode = R.id.play_list;
                        float_button.setImageResource(R.drawable.ic_play);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create,
                                selectedFragment).commit();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_button_create:
                switch (float_button_mode){
                    case R.id.club_create:
                        break;
                    case R.id.play_list:
                        Log.d("CreateActivity", "Click on play");
                        bottomNavigationView.setVisibility(View.GONE);
                        bottomAppBar.setVisibility(View.GONE);
                        start = true;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create,
                                new FragmentPlayList(ActivityCreate.this, activity_data, start)).commit();
                        break;
                }

        }
    }

    public void open_DB(){
        persons = new DataBasePersons(ActivityCreate.this);
        persons.open();
        persons.create_additional_tables();
    }
}
