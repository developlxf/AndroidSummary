package com.example.androidsummary.sql;

import com.example.androidsummary.bean.BlogCategory;
import com.example.androidsummary.bean.BlogItem;

import java.util.List;

/**每个博主的博客列表数据库
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public interface BlogItemDao {
    /**
     * 保存博客列表
     *
     */
    public void insert(String category, List<BlogItem> blogItemList);

    /**
     * 查找博客列表
     *
     * @param page
     * @return
     */
    public List<BlogItem> query(String category, int page);

    /**
     * 查询所有
     * @return
     */
    public List<BlogItem> queryAll();

    /**
     * 插入博客分类
     *
     */
    public void insertCategory(List<BlogCategory> blogCategoryList);

    /**
     * 查询博客分类
     *
     */
    public List<BlogCategory> queryCategory();

    /**
     * 删除所有
     */
    public void deleteAll();
}
