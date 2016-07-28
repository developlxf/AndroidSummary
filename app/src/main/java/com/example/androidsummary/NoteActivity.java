package com.example.androidsummary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.androidsummary.adapter.NoteListAdapter;
import com.example.androidsummary.bean.NoteItem;
import com.example.androidsummary.sql.NoteSqlManager;
import com.example.androidsummary.widget.SlidingListView;

import java.util.ArrayList;

/**
 * 知识点：
 * 1.数据库基本操作
 * 2.Toolbar的使用及actionview的添加
 * 3.SearchView的使用
 * 4.自定义控件（ListView）
 * 5.TextInputLayout的使用（嵌套EditText）
 */
public class NoteActivity extends BaseActivity {

    private Button bt_addnote;
    private SlidingListView lv;
    private LayoutInflater inflater;
    private ArrayList<NoteItem> data;
    private ArrayList<NoteItem> data_search;
    private NoteSqlManager mdb;
    private SearchView searchView;
    private NoteListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_note);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.drawable.icon_note);
        setSupportActionBar(toolbar);

        lv=(SlidingListView) findViewById(R.id.listview_note);
        bt_addnote=(Button) findViewById(R.id.btn_note);
        inflater=getLayoutInflater();
        data_search = new ArrayList<NoteItem>();

        mdb=new NoteSqlManager(this);

		/*
		 * 修改
		 */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent=new Intent(getApplicationContext(),NoteEditActivity.class);
                intent.putExtra("ids",data.get(position).getIds() );
                startActivity(intent);
            }
        });
        /*
         *  删除
         */
        lv.setDelButtonClickListener(new SlidingListView.DelButtonClickListener() {
            @Override
            public void clickHappend(int position) {
                mdb.toDelete(data.get(position).getIds());
                data=mdb.getArray();
                NoteListAdapter adapter=new NoteListAdapter(inflater,data);
                lv.setAdapter(adapter);
            }
        });



		/*
		 * 按钮点击事件，用来新建日记
		 */
        bt_addnote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NoteEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        data=mdb.getArray();//读取数据库中的数据
        adapter=new NoteListAdapter(inflater,data);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item

        /**
         * SearchView进行内容的检索
         */
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("查询...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                data_search.clear();
                for (NoteItem item : data){
                    if (item.getTitle().contains(newText)){
                        data_search.add(item);
                    }
                }
                //更新UI
                adapter=new NoteListAdapter(inflater,data_search);
                lv.setAdapter(adapter);
                return true;

            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
