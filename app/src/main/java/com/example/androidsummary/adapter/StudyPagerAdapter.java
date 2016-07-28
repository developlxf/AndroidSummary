package com.example.androidsummary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.androidsummary.fragment.WebFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伦小丹 on 2015/12/26 0026.
 */
public class StudyPagerAdapter extends FragmentPagerAdapter {
    private final String[] titles = new String[]{"概览","主布局","欢迎界面","CDSN","照片墙","音乐播放","百度地图","记事本","简介界面"};
    private final String[] urls = new String[]{"file:///android_asset/Overview.html",
            "file:///android_asset/MainUI.html",
            "file:///android_asset/Welcome.html",
            "file:///android_asset/CDSN.html",
            "file:///android_asset/Photo.html",
            "file:///android_asset/Music.html",
            "file:///android_asset/Map.html",
            "file:///android_asset/Note.html",
            "file:///android_asset/Description.html"};
    private List<WebFragment> list;
    private WebFragment fragment;

    public StudyPagerAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
        for (String s:urls){
            fragment = WebFragment.newInstance(s);
            list.add(fragment);
        }
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
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
