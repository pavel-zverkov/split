package ru.zverkov_studio.split;

import android.content.Intent;
import android.os.Bundle;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();


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
                        menuItem = menu.getItem(1);
                        menuItem.setChecked(true);
                        selectedFragment = new ClubFragment();
                        break;
                    case R.id.calendar:
                        menuItem = menu.getItem(2);
                        menuItem.setChecked(true);
                        selectedFragment = new CalendarFragment();
                        float_button.setImageIcon();
                        break;
                    case R.id.settings:
                        break;
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
