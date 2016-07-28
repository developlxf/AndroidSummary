package com.example.androidsummary.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidsummary.bean.NoteItem;

import java.util.ArrayList;
/*
 * 数据库管理帮助类：
 * 数据的增，删，改，查，都在这里实现
 */
public class NoteSqlManager {
	Context context;
	MyOpenHelper myHelper;
	SQLiteDatabase myDatabase;
	/*
	 * 别的类实例化这个类的同时，创建数据库
	 */
	public NoteSqlManager(Context con){
		this.context=con;
		myHelper=new MyOpenHelper(context);
	}
	/*
	 * 得到填充ListView用的array数据，从数据库里查找后解析。第一个界面调用
	 */
	public ArrayList<NoteItem> getArray(){
		ArrayList<NoteItem> array=new ArrayList<NoteItem>();
		ArrayList<NoteItem> array1=new ArrayList<NoteItem>();
		myDatabase=myHelper.getWritableDatabase();
		Cursor cursor=myDatabase.rawQuery("select ids,title,times from mynote" , null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			int id=cursor.getInt(cursor.getColumnIndex("ids"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String times=cursor.getString(cursor.getColumnIndex("times"));
			NoteItem item=new NoteItem(id, title, times);
			array.add(item);
			cursor.moveToNext();
		}
		myDatabase.close();
		for (int i = array.size(); i >0; i--) {
			array1.add(array.get(i-1));
		}
		return array1;
	}

	/*
	 * 返回可能要修改的数据。
	 */
	public NoteItem getTiandCon(int id){
		myDatabase=myHelper.getWritableDatabase();
		Cursor cursor=myDatabase.rawQuery("select title,content from mynote where ids='"+id+"'" , null);
		cursor.moveToFirst();
		String title=cursor.getString(cursor.getColumnIndex("title"));
		String content=cursor.getString(cursor.getColumnIndex("content"));
		NoteItem item=new NoteItem(title,content);
		myDatabase.close();
		return item;
	}
	/*
	 * 用来修改日记
	 */
	public void toUpdate(NoteItem cun){
		myDatabase=myHelper.getWritableDatabase();
		myDatabase.execSQL("update mynote set title='"+ cun.getTitle()+"',times='"+cun.getTimes()+"',content='"+cun.getContent() +"' where ids='"+ cun.getIds()+"'");
		myDatabase.close();
	}
	/*
	 *用来增加新的日记
	 */
	public void toInsert(NoteItem cun){
		myDatabase=myHelper.getWritableDatabase();
		myDatabase.execSQL("insert into mynote(title,content,times)values('"+ cun.getTitle()+"','"+cun.getContent()+"','"+cun.getTimes()+"')");
		myDatabase.close();
	}
	/*
	 * 长按点击后选择删除日记
	 */
	public void toDelete(int ids){
		myDatabase=myHelper.getWritableDatabase();
		myDatabase.execSQL("delete  from mynote where ids="+ids+"");
		myDatabase.close();
	}
}
