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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class PlayListFragment extends Fragment {

    final String TAG = "myLog";
    Context mContext;
    View play_list_fragment;

    public PlayListFragment(Context context){
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        play_list_fragment = inflater.inflate(R.layout.play_list_fragment, container, false);
        Log.d("myLog", "Inflate club_fragment");

        ViewPager viewPager = play_list_fragment.findViewById(R.id.view_pager);
        viewPager.setAdapter(
                new SampleFragmentPagerAdapter(getFragmentManager(), mContext));

        // Передаём ViewPager в TabLayout
        TabLayout tabLayout = play_list_fragment.findViewById(R.id.sliding_tabs);
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.setupWithViewPager(viewPager);

        return play_list_fragment;
    }

    public static class PageFragment extends Fragment {
        public static final String ARG_PAGE = "ARG_PAGE";

        private int mPage;

        public static PageFragment newInstance(int page) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mPage = getArguments().getInt(ARG_PAGE);
            }
        }

        @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.persons_list, container, false);
            TextView textView = (TextView) view;
            textView.setText("Fragment #" + mPage);
            return view;
        }
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "УЧАСТНИКИ", "ДИСТАНЦИЯ" };
        private Context context;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override public int getCount() {
            return PAGE_COUNT;
        }

        @Override public Fragment getItem(int position) {
            return PageFragment.newInstance(position + 1);
        }

        @Override public CharSequence getPageTitle(int position) {
            // генерируем заголовок в зависимости от позиции
            return tabTitles[position];
        }


    }
}
