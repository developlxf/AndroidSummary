package com.example.androidsummary.bean;

import com.lidroid.xutils.db.annotation.Column;

import java.io.Serializable;

/**
 * xUtils包
 * @ Table注解用来标识实体类与数据表的对应关系类似
 * @ Column注解来标识实体类中属性与数据表中字段的对应关系
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class Blogger implements Serializable{
    private static final long serialVersionUID = 6569781303855823679L;
    /**
     *
     */
    @Column(column = "id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 博主ID
     */
    @Column(column = "userId")
    private String userId;

    /**
     * 博主名字
     */
    @Column(column = "title")
    private String title;

    /**
     * 博主描述
     */
    @Column(column = "description")
    private String description;

    /**
     * 博主头像地址
     */
    @Column(column = "imgUrl")
    private String imgUrl;

    /**
     * 博主博客链接
     */
    @Column(column = "link")
    private String link;

    /**
     * 博主类型（小分类）
     */
    @Column(column = "type")
    private String type;

    /**
     * 博主类别（大分类）
     */
    @Column(column = "category")
    private String category;

    /**
     * 是否最新添加
     */
    @Column(column = "isNew")
    private int isNew;

    /**
     * 是否置顶
     */
    @Column(column = "isTop")
    private int isTop;

    /**
     * 博主更新时间
     */
    @Column(column = "updateTime")
    private long updateTime;

    /**
     * 保留字段
     */
    @Column(column = "reserve")
    private String reserve;


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public int getIsNew() {
        return isNew;
    }

    public int getIsTop() {
        return isTop;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public String getReserve() {
        return reserve;
    }

    @Override
    public String toString() {
        return title;
    }
}
