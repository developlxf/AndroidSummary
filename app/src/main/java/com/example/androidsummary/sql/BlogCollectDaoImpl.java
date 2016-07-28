package com.example.androidsummary.sql;

import android.content.Context;

import com.example.androidsummary.bean.BlogItem;
import com.example.androidsummary.utils.CacheManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**博客收藏数据库的实现
 * 因为每篇文章的地址是唯一的   本数据库根据文章地址作为关键字 进行增删查改
 * Created by 伦小丹 on 2015/12/25 0025.
 */
public class BlogCollectDaoImpl implements BlogCollectDao{
    private DbUtils db;

    public BlogCollectDaoImpl(Context context) {
        //1.上下文 2.数据库路径（此方法为获得外部缓存目录） 3.数据库名字
        db = DbUtils.create(context, CacheManager.getBloggerCollectDbPath(context),"collect_blog");
    }

    @Override
    public void insert(List<BlogItem> list) {
        for (int i=0;i<list.size();i++){
            BlogItem b = list.get(i);
            try {
                BlogItem find = db.findFirst(Selector.from(BlogItem.class).where("link","=",b.getLink()));
                if (find!=null){
                    db.update(b,WhereBuilder.b("link","=",b.getLink()));
                }else{
                    db.save(b);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void insert(BlogItem blogItem) {
        try {
            BlogItem b = db.findFirst(Selector.from(BlogItem.class).where("link", "=",blogItem.getLink() ));
            if (b!=null){
                //更新\
                db.update(blogItem, WhereBuilder.b("link","=",blogItem.getLink()));
            }else{
                //插入
                db.save(blogItem);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(BlogItem blogItem) {
        try {
            BlogItem find = db.findFirst(Selector.from(BlogItem.class).where("link","=",blogItem.getLink()));
            if (find!=null)
            db.delete(blogItem);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BlogItem query(String link) {
        try {
            return db.findFirst(Selector.from(BlogItem.class).where("link","=",link));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BlogItem> queryAll(){
        try {
            return db.findAll(Selector.from(BlogItem.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BlogItem> query(int page, int pageSize) {
        try {
            return db.findAll(Selector.from(BlogItem.class).orderBy("updateTime",true).limit(page*pageSize));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
