package com.example.androidsummary.bean;

import com.lidroid.xutils.db.annotation.Column;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class BlogHtml {
    private static final long serialVersionUID = -590113455366277508L;

    @Column(column = "url")
    private String url;// 地址

    @Column(column = "html")
    private String html; // 内容

    @Column(column = "title")
    private String title; // 标题

    @Column(column = "updateTime")
    private long updateTime; // 更新时间

    @Column(column = "")
    private String reserve; // 保留

    @Column(column = "id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

}
