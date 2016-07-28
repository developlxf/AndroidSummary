package com.example.androidsummary;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidsummary.adapter.StudyPagerAdapter;

public class StudyActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageView iv_back;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_study);
        viewPager = (ViewPager) findViewById(R.id.viewpager_study);
        viewPager.setAdapter(new StudyPagerAdapter(getSupportFragmentManager()));

        tabLayout.setTabTextColors(Color.GRAY, Color.RED);
        tabLayout.setSelectedTabIndicatorColor(Color.RED);
        tabLayout.setSelectedTabIndicatorHeight(6);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//可滑动

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);


        iv_back = (ImageView) findViewById(R.id.btn_back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("项目实现简介");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
        }
    }
}
