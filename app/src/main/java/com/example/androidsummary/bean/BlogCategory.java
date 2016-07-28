package com.example.androidsummary.bean;

import com.lidroid.xutils.db.annotation.Column;

import java.io.Serializable;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class BlogCategory implements Serializable{
    private static final long serialVersionUID = -8366599113596257949L;

    @Column(column = "id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /**
     * 分类名称
     */
    @Column(column = "name")
    private String name;

    /**
     * 分类连接
     */
    @Column(column = "link")
    private String link;

    /**
     * 分类图片
     */
    @Column(column = "image")
    private String image;

    /**
     * 分类详情
     */
    @Column(column = "content")
    private String content;

    /**
     * 分类更新时间
     */
    @Column(column = "updateTime")
    private String updateTime;

    /**
     * 保留字段
     */
    @Column(column = "reserve")
    private String reserve;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
