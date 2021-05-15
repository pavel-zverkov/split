package ru.zverkov_studio.split;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    int float_button_mode;
    DataBase club;
    ClubAdapter adapter;
    ProxyList activity_data = new ProxyList();
    BottomNavigationView bottomNavigationView;
    BottomAppBar bottomAppBar;
    boolean start = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);

        open_DB();

        FloatingActionButton float_button = (FloatingActionButton) findViewById(R.id.float_button_create);
        float_button.setOnClickListener(this);

        bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_bar_create);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_create);
        bottomNavigationView.setSelectedItemId(R.id.play_list);
        float_button.setImageResource(R.drawable.ic_play);
        float_button_mode = R.id.play_list;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create,
                new PlayListFragment(CreateActivity.this, activity_data, start)).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.filters_create:
                        break;
                    case R.id.club_create:
                        selectedFragment = new ClubCreateFragment(CreateActivity.this, activity_data);
                        float_button_mode = R.id.club_create;
                        float_button.setImageResource(R.drawable.ic_big_plus);
                        break;
                    case R.id.play_list:
                        selectedFragment = new PlayListFragment(CreateActivity.this, activity_data, start);
                        float_button_mode = R.id.play_list;
                        float_button.setImageResource(R.drawable.ic_play);
                        break;
                    case R.id.settings_create:
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create,
                        selectedFragment).commit();
                Log.d("myLog", "Replace");
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
                                new PlayListFragment(CreateActivity.this, activity_data, start)).commit();
                        break;
                }

        }
    }

    public void open_DB(){
        club = new DataBase(CreateActivity.this);
        club.open();
    }
}
