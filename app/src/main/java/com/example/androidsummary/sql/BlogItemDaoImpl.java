package com.example.androidsummary.sql;

import android.content.Context;

import com.example.androidsummary.bean.AppConstants;
import com.example.androidsummary.bean.BlogCategory;
import com.example.androidsummary.bean.BlogItem;
import com.example.androidsummary.utils.CacheManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class BlogItemDaoImpl implements BlogItemDao{
    private DbUtils db;

    public BlogItemDaoImpl(Context context, String userId) {
        db = DbUtils.create(context, CacheManager.getBlogListDbPath(context), userId + "_blog");
    }

    public void insert(String category, List<BlogItem> list) {
        try {
            for (int i = 0; i < list.size(); i++) {
                BlogItem blogItem = list.get(i);
                BlogItem findItem = db.findFirst(Selector.from(BlogItem.class).where("category", "=", category).and("link", "=", blogItem.getLink()));
                if (findItem != null) {
                    db.update(blogItem, WhereBuilder.b("category", "=", category).and("link", "=", blogItem.getLink()));
                } else {
                    db.save(blogItem);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<BlogItem> query(String category, int page) {
        List<BlogItem> list = null;
        try {
            if (AppConstants.BLOG_CATEGORY_ALL.equals(category)) {
                // 全部分类
                list = db.findAll(Selector.from(BlogItem.class).where("category", "=", category).and("isTop", "=", 1));
                // 加上这句，可把置顶的文章在后面的地方不显示
                List<BlogItem> normalList = db.findAll(
                        Selector.from(BlogItem.class).where("category", "=", category).and("isTop", "!=", 1).orderBy("date", true).limit(page * 20));
                if (list != null) {
                    list.addAll(normalList);
                } else {
                    list = normalList;
                }
            } else {
                // 其他分类
                list = db.findAll(Selector.from(BlogItem.class).where("category", "=", category).orderBy("date", true).limit(page * 20));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<BlogItem> queryAll() {
        List<BlogItem> list = null;
        try {
            list = db.findAll(BlogItem.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insertCategory(List<BlogCategory> blogCategoryList) {
        try {
            for (int i = 0; i < blogCategoryList.size(); i++) {
                BlogCategory blogCategory = blogCategoryList.get(i);
                BlogCategory findItem = db.findFirst(Selector.from(BlogCategory.class).where("name", "=", blogCategory.getName()));
                if (findItem != null) {
                    db.update(blogCategory, WhereBuilder.b("name", "=", blogCategory.getName()));
                } else {
                    db.save(blogCategory);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BlogCategory> queryCategory() {
        List<BlogCategory> list = null;
        try {
            list = db.findAll(Selector.from(BlogCategory.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void deleteAll() {
        try {
            db.deleteAll(BlogItem.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
