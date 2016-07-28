package com.example.androidsummary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.androidsummary.R;
import com.example.androidsummary.fragment.Fragment_Welcome;

/**
 * Created by 伦小丹 on 2015/12/25 0025.
 */
public class WelcomePagerAdapter extends FragmentPagerAdapter{

    public WelcomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment_Welcome fragment = null;
        switch (position){
            case 0:
                fragment = Fragment_Welcome.newInstance(R.layout.fragment__wel01);
                break;
            case 1:
                fragment = Fragment_Welcome.newInstance(R.layout.fragment__wel02);
                break;
            case 2:
                fragment = Fragment_Welcome.newInstance(R.layout.fragment__wel03);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
