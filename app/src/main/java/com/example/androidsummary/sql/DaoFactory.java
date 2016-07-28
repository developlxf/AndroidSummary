package com.example.androidsummary.sql;

import android.content.Context;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class DaoFactory {
    private static DaoFactory mInstance = null;

    /**
     * 获取DaoFactory的实例
     *
     * @return
     */
    public static DaoFactory getInstance() {
        if (mInstance == null) {
            synchronized (DaoFactory.class) {
                if (mInstance == null) {
                    mInstance = new DaoFactory();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取博主数据库
     *
     * @param context
     * @return
     */
    public BloggerDao getBloggerDao(Context context, String type) {
        return new BloggerDaoImpl(context, type);
    }

    /**
     * 获取博客列表数据库
     *
     * @param context
     * @return
     */
    public BlogItemDao getBlogItemDao(Context context, String userId) {
        return new BlogItemDaoImpl(context, userId);
    }

    /**
     * 获取博客内容数据库
     *
     * @param context
     * @return
     */
    public BlogContentDao getBlogContentDao(Context context, String url) {
        return new BlogContentDaoImpl(context, url);
    }

    /**
     * 获取博客收藏数据库
     *
     * @param context
     * @return
     */
    public BlogCollectDao getBlogCollectDao(Context context) {
        return new BlogCollectDaoImpl(context);
    }
//
//    /**
//     * 获取博客评论数据库
//     *
//     * @param context
//     * @return
//     */
//    public BlogCommentDao getBlogCommentDao(Context context, String filename) {
//        return new BlogCommentDaoImpl(context, filename);
//    }
//
//    /**
//     * 获取某频道-博客专家数据库
//     *
//     * @param context
//     * @param channel
//     * @return
//     */
//    public ChannelBloggerDao getChannelBloggerDao(Context context, Channel channel) {
//        return new ChannelBloggerDaoImpl(context, channel);
//    }
}
