package com.example.androidsummary.sql;

import android.content.Context;

import com.example.androidsummary.bean.BlogHtml;
import com.example.androidsummary.utils.CacheManager;
import com.example.androidsummary.utils.MD5Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

/**
 * 博客内容-数据库实现
 *
 * @author tangqi
 * @data 2015年8月7日下午11:24:06
 */

public class BlogContentDaoImpl implements BlogContentDao {

    private DbUtils db;

    public BlogContentDaoImpl(Context context, String url) {
        String urlMD5 = "url-md5";
        try {
            urlMD5 = MD5Utils.md5(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db = DbUtils.create(context,
                CacheManager.getBlogContentDbPath(context), urlMD5);
    }

    public void insert(BlogHtml blogHtml) {
        BlogHtml findItem;
        try {
            findItem = db.findFirst(Selector.from(BlogHtml.class).where("url",
                    "=", blogHtml.getUrl()));

            if (findItem != null) {
                db.update(blogHtml,
                        WhereBuilder.b("url", "=", blogHtml.getUrl()));
            } else {
                db.save(blogHtml);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        return;
    }

    public BlogHtml query(String url) {
        BlogHtml blogHtml = null;
        try {
            blogHtml = db.findFirst(Selector.from(BlogHtml.class).where("url",
                    "=", url));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return blogHtml;
    }
}