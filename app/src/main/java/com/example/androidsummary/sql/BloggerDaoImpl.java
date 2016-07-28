package com.example.androidsummary.sql;

import android.content.Context;
import android.util.Log;

import com.example.androidsummary.bean.Blogger;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**DbUtils：
 * android中的orm框架，一行代码就可以进行增删改查；
 支持事务，默认关闭；
 可通过注解自定义表名，列名，外键，唯一性约束，NOT NULL约束，CHECK约束等（需要混淆的时候请注解表名和列名）；
 支持绑定外键，保存实体时外键关联实体自动保存或更新；
 自动加载外键关联实体，支持延时加载；
 支持链式表达查询，更直观的查询语义，参考下面的介绍或sample中的例子。
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class BloggerDaoImpl implements BloggerDao{
    private DbUtils db;//xUtils包下的
    private Context context;

    public BloggerDaoImpl(Context context,String type) {
        this.context = context;
        init(type);
    }

    @Override
    public void init(String type) {
        //1.context 2.dbName
        this.db = DbUtils.create(context,"blogger_"+type);
    }

    @Override
    public void insert(Blogger blogger) {
        try {
            //查询单个  通过哪个表来查 条件是什么
            Blogger b = db.findFirst(Selector.from(Blogger.class).where("userId","=",blogger.getUserId()));
            if (b!=null){
                //如果数据库中已有 就更新
                Log.i("upda","置顶更新");
                db.update(blogger, WhereBuilder.b("userId", "=", blogger.getUserId()));
            }else{
                //没有 就保存  第一次保存会自动创建对象对应的表
                db.save(blogger);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(List<Blogger> list) {
        try {
            db.saveOrUpdateAll(list);//保存或更新
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Blogger query(String userId) {
        try {
            return db.findFirst(Selector.from(Blogger.class).where("userId", "=", userId));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Blogger> queryAll() {
        try {
            // // 最新的排最前面
            List<Blogger> list = new ArrayList<Blogger>();
            List<Blogger> toplist = db.findAll(Selector.from(Blogger.class).where("isTop", "=", 1).orderBy("updateTime", true));
            List<Blogger> newlist = db.findAll(Selector.from(Blogger.class).where("isTop", "=", 0).and("isNew", "=", 1).orderBy("updateTime", true));
            List<Blogger> oldlist = db.findAll(Selector.from(Blogger.class).where("isTop", "=", 0).and("isNew", "=", 0));

            if (toplist != null) {
                list.addAll(toplist);
            }

            if (newlist != null) {
                list.addAll(newlist);
            }

            if (oldlist != null) {
                list.addAll(oldlist);
            }
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    //分页查询
    @Override
    public List<Blogger> query(int pageIndex, int pageSize) {//第几页  每页多少个
        List<Blogger> list = null;
        try {
            list = db.findAll(Selector.from(Blogger.class).orderBy("isNew", true).limit(pageSize).offset(pageIndex * pageSize));
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        }

        return list;
    }

    //删除单个对象
    @Override
    public void delete(Blogger blogger) {
        try {
            db.delete(blogger);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //删除集合
    @Override
    public void deleteAll(List<Blogger> list) {
        try {
            db.deleteAll(list);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    //删除表
    @Override
    public void deleteAll() {
        try {
            db.deleteAll(Blogger.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
