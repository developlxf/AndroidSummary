package com.example.androidsummary.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class CacheManager {

    public static String getExternalCacheDir(Context context) {
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                path = file.getAbsolutePath();
            } else {
                path = context.getCacheDir().getAbsolutePath();
            }
        } else {
            path = context.getCacheDir().getAbsolutePath();
        }
        return path;
    }
    /**
     * 获取外部缓存目录
     *
     * @param context
     * @return
     */
    public static String getExternalCachePath(Context context) {
        return getExternalCacheDir(context);
    }

    /**
     * 获取博客收藏数据库目录
     *
     * @param context
     * @return
     */
    public static String getBloggerCollectDbPath(Context context) {
        return getExternalCachePath(context) + File.separator + "BlogCollect";
    }

    /**
     * 获取博主数据库目录
     *
     * @param context
     * @return
     */
    public static String getBloggerDbPath(Context context) {
        return getExternalCachePath(context) + File.separator + "Blogger";
    }

    /**
     * 获取博客列表数据库目录
     *
     * @param context
     * @return
     */
    public static String getBlogListDbPath(Context context) {
        return getExternalCachePath(context) + File.separator + "BlogList";
    }

    /**
     * 获取博客内容数据库目录
     *
     * @param context
     * @return
     */
    public static String getBlogContentDbPath(Context context) {
        return getExternalCachePath(context) + File.separator + "BlogContent";
    }
}
