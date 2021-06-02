package ru.zverkov_studio.split;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ActivityEvent extends AppCompatActivity {

    public static DataBasePersons persons;
    public static Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        mContext = ActivityEvent.this;

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(
                new SampleFragmentPagerAdapterEvent(getSupportFragmentManager(), this));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        open_DB();
    }

    public class SampleFragmentPagerAdapterEvent extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "УЧАСТНИКИ", "ДИСТАНЦИЯ" };
        private Context context;
        boolean mode;

        public SampleFragmentPagerAdapterEvent(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override public int getCount() {
            return PAGE_COUNT;
        }

        @Override public Fragment getItem(int position) {
            return new PageFragmentEvent(position + 1);
        }

        @Override public CharSequence getPageTitle(int position) {
            // генерируем заголовок в зависимости от позиции
            return tabTitles[position];
        }
    }

    public static class PageFragmentEvent extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";
        public static final String CNT_PAGE = "PAGE_CONTENT";

        private int mPage;
        boolean mode;
        private ArrayList<String> mContent;

        public PageFragmentEvent (int page) {
            mPage = page;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mPage = getArguments().getInt(ARG_PAGE);
            }
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState) {

            View view = null;
            switch (mPage){
                case 1:
                    view = inflater.inflate(R.layout.page_persons, container, false);
                    RecyclerView persons_list = view.findViewById(R.id.persons_list);
                    persons_list.setLayoutManager(new LinearLayoutManager(getContext()));
                    AdapterPersonsEvent adapterPersonsEvent = new AdapterPersonsEvent(getContext());
                    Log.d("track", "activity_event");
                    persons_list.setAdapter(adapterPersonsEvent);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.page_track, container, false);
                    RecyclerView track_list = view.findViewById(R.id.track_list);
                    track_list.setLayoutManager(new LinearLayoutManager(getContext()));
                    AdapterTrack adapter = new AdapterTrack(getContext());
                    track_list.setAdapter(adapter);

                    FloatingActionButton addButton = view.findViewById(R.id.add_track_point);
                    addButton.setVisibility(View.GONE);

                    FloatingActionButton stop_button = view.findViewById(R.id.stop_button);
                    stop_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (persons.get_times(adapter.getItemCount() - 1).size() != persons.getAllData(DataBasePersons.TABLE_DECLARED, null).getCount()){
                                Toast.makeText(mContext, "Не все участники финишировали", Toast.LENGTH_LONG).show();
                            }
                            else {
                                getActivity().finish();
                                ActivityCreate.fa.finish();
                            }
                        }
                    });
                    break;
            }

            return view;
        }
    }

    public void open_DB(){
        persons = new DataBasePersons(this);
        persons.open();
    }
}
