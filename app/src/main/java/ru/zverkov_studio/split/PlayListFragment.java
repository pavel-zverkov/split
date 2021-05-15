package ru.zverkov_studio.split;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PlayListFragment extends Fragment {

    final String TAG = "myLog";
    Context mContext;
    View play_list_fragment;
    ProxyList activity_data;
    private ArrayList<String> content = new ArrayList<String>();

    public PlayListFragment(Context context, ProxyList data){
        activity_data = data;
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        play_list_fragment = inflater.inflate(R.layout.play_list_fragment, container, false);
        Log.d("myLog", "Inflate club_fragment");

        content.add("Start");
        content.add("Finish");

        ViewPager viewPager = play_list_fragment.findViewById(R.id.view_pager);
        viewPager.setAdapter(
                new SampleFragmentPagerAdapter(getChildFragmentManager(), mContext, activity_data));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = play_list_fragment.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return play_list_fragment;
    }

    public static class PageFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";
        public static final String CNT_PAGE = "PAGE_CONTENT";

        private int mPage;
        private ArrayList<String> mContent;
        private ProxyList activity_data;

        public PageFragment (int page, ArrayList<String> content, ProxyList data) {
            mPage = page;
            mContent = content;
            activity_data = data;
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
                    view = inflater.inflate(R.layout.persons_page, container, false);
                    RecyclerView persons_list = view.findViewById(R.id.persons_list);
                    persons_list.setLayoutManager(new LinearLayoutManager(getContext()));
                    PersonsAdapter personsAdapter = new PersonsAdapter(getContext(), activity_data);
                    persons_list.setAdapter(personsAdapter);

                    PersonsItemTouchHelper personsItemTouchHelper = new PersonsItemTouchHelper(getContext(), persons_list, 0, ItemTouchHelper.LEFT);
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(personsItemTouchHelper);
                    itemTouchHelper.attachToRecyclerView(persons_list);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.track_page, container, false);
                    RecyclerView track_list = view.findViewById(R.id.track_list);
                    track_list.setLayoutManager(new LinearLayoutManager(getContext()));
                    track_list.setAdapter(new TrackAdapter(getContext(), mContent, R.drawable.ic_run));
                    break;
            }

            return view;
        }
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "УЧАСТНИКИ", "ДИСТАНЦИЯ" };
        private Context context;
        ProxyList activity_data;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context, ProxyList data) {
            super(fm);
            this.context = context;
            activity_data = data;
        }

        @Override public int getCount() {
            return PAGE_COUNT;
        }

        @Override public Fragment getItem(int position) {
            return new PageFragment(position + 1, content, activity_data);
        }

        @Override public CharSequence getPageTitle(int position) {
            // генерируем заголовок в зависимости от позиции
            return tabTitles[position];
        }


    }
}
