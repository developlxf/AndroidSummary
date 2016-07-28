package com.example.androidsummary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidsummary.bean.NoteItem;
import com.example.androidsummary.sql.NoteSqlManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteEditActivity extends AppCompatActivity {

    private EditText ed_title, ed_content;
    private Button btn_save;
    private NoteSqlManager myDatabase;
    private NoteItem item;
    int ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_note_edit);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.drawable.icon_note);
        setSupportActionBar(toolbar);

        ed_title = (EditText) findViewById(R.id.et_title);
        ed_content = (EditText) findViewById(R.id.et_content);
        btn_save = (Button) findViewById(R.id.btn_save);
        myDatabase = new NoteSqlManager(this);

        Intent intent = this.getIntent();
        ids = intent.getIntExtra("ids", 0);
        //默认为0，不为0,则为修改数据时跳转过来的
        if (ids != 0) {
            item = myDatabase.getTiandCon(ids);
            ed_title.setText(item.getTitle());
            ed_content.setText(item.getContent());
        }
        //保存按钮的点击事件，他和返回按钮是一样的功能，所以都调用isSave()方法；
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isSave();
            }
        });
    }

    /*
     * 返回按钮调用的方法。
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String times = formatter.format(curDate);
        String title = ed_title.getText().toString();
        String content = ed_content.getText().toString();
        //是要修改数据
        if (ids != 0) {
            item = new NoteItem(title, ids, content, times);
            myDatabase.toUpdate(item);
            NoteEditActivity.this.finish();
        }
        //新建日记
        else {
            if (title.equals("") && content.equals("")) {
                NoteEditActivity.this.finish();
            } else {
                item = new NoteItem(title, content, times);
                myDatabase.toInsert(item);
                NoteEditActivity.this.finish();
            }

        }
    }

    private void isSave() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String times = formatter.format(curDate);
        String title = ed_title.getText().toString();
        String content = ed_content.getText().toString();
        //是要修改数据
        if (ids != 0) {
            item = new NoteItem(title, ids, content, times);
            myDatabase.toUpdate(item);
            NoteEditActivity.this.finish();
        }
        //新建日记
        else {
            item = new NoteItem(title, content, times);
            myDatabase.toInsert(item);
            NoteEditActivity.this.finish();
        }
    }


}
