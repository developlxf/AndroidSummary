package com.example.androidsummary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.androidsummary.widget.JazzyViewPager;

import java.util.List;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class JazzyAdapter extends FragmentPagerAdapter{
    private JazzyViewPager jazzyViewPager;
    private List<Fragment> list;
    private List<String> titles;

    public JazzyAdapter(FragmentManager fm,JazzyViewPager jazzyViewPager,List<Fragment> list,List<String> titles) {
        super(fm);
        this.jazzyViewPager = jazzyViewPager;
        this.list = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        jazzyViewPager.setObjectForPosition(list.get(position),position);
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
