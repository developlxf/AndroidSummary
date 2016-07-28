package com.example.androidsummary.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库
 */
public class MyOpenHelper extends SQLiteOpenHelper{

	public MyOpenHelper(Context context) {
		super(context, "notes", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table mynote(ids integer PRIMARY KEY autoincrement,title text,content text,times text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	
}
