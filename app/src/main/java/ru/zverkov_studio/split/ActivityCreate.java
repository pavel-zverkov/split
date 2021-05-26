package ru.zverkov_studio.split;

import android.content.Context;
import android.content.Intent;
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

    public static final String TAG = "ActivityCreate";
    public static Context CONTEXT;

    int float_button_mode;
    DataBasePersons persons;
    DataBaseEvents events;
    BottomNavigationView bottomNavigationView;
    BottomAppBar bottomAppBar;
    FloatingActionButton float_button;

    boolean start = false;

    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_CREATE_DATE = "create_date";
    public static final String COLUMN_KIND_SPORT = "kind_sport";
    public static final String COLUMN_KIND_START = "kind_start";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        CONTEXT = ActivityCreate.this;

        open_DB();

        float_button = (FloatingActionButton) findViewById(R.id.float_button_create);
        float_button.setOnClickListener(this);

        bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_bar_create);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_create);
        bottomNavigationView.setSelectedItemId(R.id.play_list);
        float_button.setImageResource(R.drawable.ic_play);
        float_button_mode = R.id.play_list;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create,
                new FragmentPlayList(ActivityCreate.this)).commit();

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
                        selectedFragment = new FragmentPlayList(ActivityCreate.this);
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
                        bottomNavigationView.setSelectedItemId(R.id.play_list);
                        float_button.setImageResource(R.drawable.ic_play);
                        float_button_mode = R.id.play_list;
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create,
                                new FragmentPlayList(ActivityCreate.this)).commit();

                        persons.all_declared();
                        break;
                    case R.id.play_list:
                        Intent intent = new Intent(ActivityCreate.this, ActivityEvent.class);
                        startActivity(intent);
                        break;
                }

        }
    }

    public void open_DB(){
        persons = new DataBasePersons(ActivityCreate.this);
        persons.open();
        persons.create_additional_tables();

        events = new DataBaseEvents(ActivityCreate.this);
        events.open();
        events.drop_track_table();
        events.clear_event_data();
        events.create_track_table();

        Intent intent = getIntent();
        events.fill_event_data(intent.getStringExtra(COLUMN_EVENT_NAME),
                               intent.getStringExtra(COLUMN_CREATE_DATE),
                               intent.getStringExtra(COLUMN_KIND_SPORT),
                               intent.getStringExtra(COLUMN_KIND_START));
        for (int i = 0; i < 4; i++){
            Log.d("events_data", events.get_event_data()[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        persons.drop_additional_tables();
        persons.close();

        events.close();
    }
}
