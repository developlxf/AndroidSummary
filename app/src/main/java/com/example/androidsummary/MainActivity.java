package com.example.androidsummary;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androidsummary.adapter.JazzyAdapter;
import com.example.androidsummary.bean.BlogItem;
import com.example.androidsummary.bean.Blogger;
import com.example.androidsummary.fragment.BloggerFragment;
import com.example.androidsummary.fragment.CollectFragment;
import com.example.androidsummary.widget.JazzyViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager fm;
    private JazzyViewPager jazzyViewPager;
    private LinearLayout linear_tab;
    private DrawerLayout drawer;
    private BloggerFragment fragment_blogger;
    private CollectFragment fragment_blogg;
    //搜索博主时用的集合
    private List<Blogger> search_list_blogger = new ArrayList<Blogger>();
    //搜索收藏博文时用的集合
    private List<BlogItem> search_list_blogg = new ArrayList<BlogItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        setSupportActionBar(toolbar);

        /**
         * 关联DrawerLayout和ActionBarDrawerToggle
         */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //设置侧滑菜单的点击事件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initTabAndViewPager();
    }

    private void initTabAndViewPager() {
        //初始化数据
        List<Fragment> list = new ArrayList<Fragment>();
        List<String> titles = new ArrayList<>();
        fragment_blogger = new BloggerFragment();
        fragment_blogg = new CollectFragment();
        list.add(fragment_blogger);
        list.add(fragment_blogg);
        titles.add("博主");
        titles.add("收藏");

        //初始化TabLayout
        final TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout_main);
        linear_tab = (LinearLayout) findViewById(R.id.linear_main);
        tablayout.addTab(tablayout.newTab().setText(titles.get(0)));
        tablayout.addTab(tablayout.newTab().setText(titles.get(1)));
        tablayout.setTabTextColors(Color.GRAY, Color.RED);
        linear_tab.setBackgroundColor(Color.parseColor("#79FFCC"));
        tablayout.setSelectedTabIndicatorColor(Color.parseColor("#FF3233"));
        tablayout.setSelectedTabIndicatorHeight(6);

        //初始化ViewPager
        fm = getSupportFragmentManager();
        jazzyViewPager = (JazzyViewPager) findViewById(R.id.jazzyviewpager);
        jazzyViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        JazzyAdapter adapter = new JazzyAdapter(fm,jazzyViewPager,list,titles);
        jazzyViewPager.setAdapter(adapter);

        //关联ViewPager和TabLayout
        tablayout.setupWithViewPager(jazzyViewPager);
        tablayout.setTabsFromPagerAdapter(adapter);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                jazzyViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_main);//在菜单中找到对应控件的item

        /**
         * SearchView进行内容的检索
         */
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("查询...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (jazzyViewPager.getCurrentItem()) {
                    case 0://博主
                        search_list_blogger = fragment_blogger.bloggerDao.queryAll();
                        List<Blogger> list = new ArrayList<Blogger>();
                        list.clear();
                        for (int i = 0; i < search_list_blogger.size(); i++) {
                            if (search_list_blogger.get(i).getTitle().contains(newText)) {
                                Log.i("title",newText+"/"+search_list_blogger.get(i).getTitle());
                                list.add(search_list_blogger.get(i));
                            }
                        }

                        fragment_blogger.getAdapter().setList(list);
                        fragment_blogger.getAdapter().notifyDataSetChanged();

                        break;
                    case 1://博文
                        search_list_blogg = fragment_blogg.blogCollectDao.queryAll();
                        List<BlogItem> list_collect = new ArrayList<BlogItem>();
                        list_collect.clear();
                        for (int i=0;i<search_list_blogg.size();i++){
                            if (search_list_blogg.get(i).getTitle().contains(newText)){
                                list_collect.add(search_list_blogg.get(i));
                            }
                        }

                        fragment_blogg.getAdapter().setList(list_collect);
                        fragment_blogg.getAdapter().notifyDataSetChanged();
                        break;
                }
                return true;

            }
        });
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_csdn) {

        } else if (id == R.id.nav_gallery) {
            toClass(PhotoWallActivity.class);
        } else if (id == R.id.nav_slideshow) {
            toClass(MediaActivity.class);
        } else if (id == R.id.nav_manage) {
            toClass(NoteActivity.class);
        } else if (id == R.id.nav_share) {
            toClass(StudyActivity.class);
        } else if (id == R.id.nav_send) {
            toClass(BaiduMapActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private long firstTime ;
    private boolean isFirst = true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (drawer.isDrawerOpen(Gravity.LEFT)){
                drawer.closeDrawer(Gravity.LEFT);
                return true;
            }

            if (isFirst == false && System.currentTimeMillis() - firstTime<=2000){
                isFirst = true;
                return super.onKeyDown(keyCode, event);
            }else{
                firstTime = System.currentTimeMillis();
                isFirst = false;
                Toast.makeText(this,"2s内再按一次退出",Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
