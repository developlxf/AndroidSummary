package com.example.androidsummary.bean;

/**
 * Created by 伦小丹 on 2015/12/22 0022.
 */
public class NoteItem {
    private String title;//标题
    private String content;//内容
    private String times;//时间
    private int ids;//编号
    public NoteItem(String ti, int id, String con , String time){
        this.ids=id;
        this.title=ti;
        this.content=con;
        this.times=time;
    }
    public NoteItem(String ti, String con, String time){
        this.title=ti;
        this.content=con;
        this.times=time;
    }
    public NoteItem(int i, String ti, String time){
        this.ids=i;
        this.title=ti;
        this.times=time;
    }
    public NoteItem(String ti, String con){
        this.title=ti;
        this.content=con;
    }
    public int getIds() {
        return ids;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getTimes() {
        return times;
    }
}
