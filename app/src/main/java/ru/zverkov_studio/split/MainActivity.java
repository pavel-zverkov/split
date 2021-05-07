package ru.zverkov_studio.split;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MenuItem menuItem;
    DataBase club;
    ClubAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        open_DB();
        adapter = new ClubAdapter(this, club);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //Menu menu = bottomNavigationView.getMenu();

        FloatingActionButton float_button = (FloatingActionButton) findViewById(R.id.float_button);
        float_button.setOnClickListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.filters:
                        break;
                    case R.id.club:
                        selectedFragment = new ClubFragment(MainActivity.this, club, adapter);
                        break;
                    case R.id.calendar:
                        selectedFragment = new CalendarFragment();
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
                add_person();
                break;
        }
    }

    public void add_person() {
        AddPerson bottomSheetDialog = AddPerson.newInstance(this, club, adapter);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    public void open_DB(){
        club = new DataBase(this);
        club.open();
    }
}
