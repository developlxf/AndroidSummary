package com.example.androidsummary.sql;

import com.example.androidsummary.bean.Blogger;

import java.util.List;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public interface BloggerDao {
    /**
     * 插入博主
     *
     * @param blogger
     */
    public void insert(Blogger blogger);

    /**
     * 插入博主列表
     *
     * @param list
     */
    public void insert(List<Blogger> list);

    /**
     * 查询某个博主是否存在
     *
     * @param userId
     * @return
     */
    public Blogger query(String userId);

    /**
     * 查询所有博主
     *
     * @return
     */
    public List<Blogger> queryAll();

    /**
     * 查询博主（分页）
     *
     * @return
     */
    List<Blogger> query( int pageIndex, int pageSize);

    /**
     * 删除博主
     *
     * @param blogger
     */
    public void delete(Blogger blogger);

    /**
     * 删除博主列表
     *
     * @param list
     */
    public void deleteAll(List<Blogger> list);

    /***
     * 删除所有博主
     *
     */
    public void deleteAll();

    /**
     * 初始化博客数据库
     * @param type
     */
    public void init(String type);
}
