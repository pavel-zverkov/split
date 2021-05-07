package ru.zverkov_studio.split;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        FloatingActionButton plus_button = (FloatingActionButton) findViewById(R.id.watch);
        plus_button.setOnClickListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.filters:
                        break;
                    case R.id.club:
                        Intent club_intent = new Intent(CalendarActivity.this, ClubActivity.class);
                        startActivity(club_intent);
                        break;
                    case R.id.calendar:
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
        switch (v.getId()){
            case R.id.watch:
                break;
        }
    }
}
