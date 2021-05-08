package ru.zverkov_studio.split;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    int float_button_mode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_create);

        FloatingActionButton float_button = (FloatingActionButton) findViewById(R.id.float_button_create);
        float_button.setOnClickListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.filters_create:
                        break;
                    case R.id.club_create:
                        //selectedFragment = new ClubFragment(MainActivity.this, club, adapter);
                        float_button_mode = R.id.club_create;
                        float_button.setImageResource(R.drawable.ic_big_plus);
                        break;
                    case R.id.play_list:
                        selectedFragment = new PlayListFragment(CreateActivity.this);
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

    }
}
