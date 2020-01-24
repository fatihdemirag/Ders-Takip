package fatihdemirag.net.dersprogram.ui.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import fatihdemirag.net.dersprogram.R;
import fatihdemirag.net.dersprogram.helpers.TabsPagerAdapter;

public class Syllabus extends Fragment {


    public Syllabus() {
    }

    private ViewPager vpPager;
    private FragmentPagerAdapter adapterViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_syllabus, container, false);
        vpPager = view.findViewById(R.id.viewPage);

        adapterViewPager = new TabsPagerAdapter(getActivity(),getChildFragmentManager());

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vpPager);


        vpPager.setAdapter(adapterViewPager);


        return view;
    }
}
