package ru.zverkov_studio.split;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ClubActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "myLog";
    DataBase club;
    ClubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_activity);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        open_DB();
        create_recyclerview();

        FloatingActionButton plus_button = (FloatingActionButton) findViewById(R.id.plus_button);
        plus_button.setOnClickListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.filters:
                        break;
                    case R.id.club:
                        break;
                    case R.id.calendar:
                        Intent calendar_intent = new Intent(ClubActivity.this, CalendarActivity.class);
                        startActivity(calendar_intent);
                        break;
                    case R.id.settings:
                        break;
                }

                return false;
            }
        });

    }

    public void open_DB(){
        club = new DataBase(this);
        club.open();
    }

    public void create_recyclerview() {
        RecyclerView club_list = findViewById(R.id.club_list);
        club_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClubAdapter(this, club);
        club_list.setAdapter(adapter);

        ClubRemoveItem simpleItemTouchCallback = new ClubRemoveItem(this, club_list, 0, ItemTouchHelper.LEFT);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(club_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_button:
                add_person();
                break;
        }
    }

    public void add_person() {
        AddPerson bottomSheetDialog = AddPerson.newInstance(this, club, adapter);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    public void onDestroy() {
        super.onDestroy();
        club.close();
    }
}