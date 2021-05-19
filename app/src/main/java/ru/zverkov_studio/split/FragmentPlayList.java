package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class FragmentPlayList extends Fragment {

    final String TAG = "myLog";
    Context mContext;
    View play_list_fragment;
    private ArrayList<String> content = new ArrayList<String>();

    public FragmentPlayList(Context context){
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        play_list_fragment = inflater.inflate(R.layout.fragment_play_list, container, false);
        Log.d("myLog", "Inflate club_fragment");

        content.add("СТАРТ");
        content.add("ФИНИШ");

        ViewPager viewPager = play_list_fragment.findViewById(R.id.view_pager);
        viewPager.setAdapter(
                new SampleFragmentPagerAdapter(getChildFragmentManager(), mContext));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = play_list_fragment.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return play_list_fragment;
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "УЧАСТНИКИ", "ДИСТАНЦИЯ" };
        private Context context;
        boolean mode;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override public int getCount() {
            return PAGE_COUNT;
        }

        @Override public Fragment getItem(int position) {
            return new PageFragment(position + 1);
        }

        @Override public CharSequence getPageTitle(int position) {
            // генерируем заголовок в зависимости от позиции
            return tabTitles[position];
        }
    }

    public static class PageFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";
        public static final String CNT_PAGE = "PAGE_CONTENT";

        private int mPage;
        boolean mode;
        private ArrayList<String> mContent;

        public PageFragment (int page) {
            mPage = page;
        }

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mPage = getArguments().getInt(ARG_PAGE);
            }
        }

        @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState) {
            View view = null;
            switch (mPage){
                case 1:
                    view = inflater.inflate(R.layout.page_persons, container, false);
                    LinearLayout attention = view.findViewById(R.id.persons_attention);
                    RecyclerView persons_list = view.findViewById(R.id.persons_list);
                    persons_list.setLayoutManager(new LinearLayoutManager(getContext()));

                    AdapterPersons adapterPersons = new AdapterPersons(getContext());
                    persons_list.setAdapter(adapterPersons);

                    ItemTouchHelperPersons itemTouchHelperPersons = new ItemTouchHelperPersons(getContext(), persons_list, 0, ItemTouchHelper.LEFT);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperPersons);
                    itemTouchHelper.attachToRecyclerView(persons_list);

                    if (persons_list.getAdapter().getItemCount() != 0){
                        attention.setVisibility(View.GONE);
                    }
                    else{
                        attention.setVisibility(View.VISIBLE);
                    }

                    break;
                case 2:
                    view = inflater.inflate(R.layout.page_track, container, false);
                    RecyclerView track_list = view.findViewById(R.id.track_list);
                    track_list.setLayoutManager(new LinearLayoutManager(getContext()));
                    AdapterTrack adapter = new AdapterTrack(getContext());
                    track_list.setAdapter(adapter);

                    ItemTouchHelperTrack itemTouchHelperTrack = new ItemTouchHelperTrack(getContext(), track_list, 0, ItemTouchHelper.LEFT);
                    ItemTouchHelper itemTouchHelper_ = new ItemTouchHelper(itemTouchHelperTrack);
                    itemTouchHelper_.attachToRecyclerView(track_list);

                    FloatingActionButton addButton = view.findViewById(R.id.add_track_point);
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.add("ПРОМЕЖУТОК");
                        }
                    });
                    break;
            }

            return view;
        }
    }

}
