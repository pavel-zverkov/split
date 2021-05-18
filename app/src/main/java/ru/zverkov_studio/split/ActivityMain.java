package ru.zverkov_studio.split;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {

    int float_button_mode;
    DataBasePersons persons;
    AdapterClub adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        open_DB();

        FloatingActionButton float_button = (FloatingActionButton) findViewById(R.id.float_button);
        float_button.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        set_start_fragment(bottomNavigationView, float_button);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.filters:
                    case R.id.settings:
                        Toast.makeText(ActivityMain.this, "Эта функция пока недоступна", Toast.LENGTH_SHORT);
                        break;
                    case R.id.club:
                        selectedFragment = new FragmentClub(ActivityMain.this, persons, adapter);
                        float_button_mode = R.id.club;
                        float_button.setImageResource(R.drawable.ic_big_plus);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                        break;
                    case R.id.calendar:
                        selectedFragment = new FragmentCalendar();
                        float_button_mode = R.id.calendar;
                        float_button.setImageResource(R.drawable.ic_watch);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
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
        Log.d("myLog", String.valueOf(this) + " " + String.valueOf(ActivityMain.this));
        BottomAddPerson bottomSheetDialog = BottomAddPerson.newInstance(this, persons, adapter);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    public void add_activity() {
        Log.d("myLog", String.valueOf(this) + " " + String.valueOf(ActivityMain.this));
        BottomAddActivity bottomSheetDialog = BottomAddActivity.newInstance(this);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    public void open_DB(){
        persons = new DataBasePersons(this);
        persons.open();
        adapter = new AdapterClub(this, persons);
    }

    public void set_start_fragment(BottomNavigationView bottomNavigationView, FloatingActionButton float_button){
        bottomNavigationView.setSelectedItemId(R.id.calendar);
        float_button_mode = R.id.calendar;
        float_button.setImageResource(R.drawable.ic_watch);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentCalendar()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        persons.close();
    }
}
