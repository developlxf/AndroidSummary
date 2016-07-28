package com.example.androidsummary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**自定义ImageCache 实现（LruCache和DiskLruCache双缓存）
 *
 * Created by 伦小丹 on 2015/12/22 0022.
 */
public  class BitmapCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> lruCache;//android 提供的图片缓存类
    private DiskLruCache diskLruCache;//磁盘缓存类
    private final int DISK_MAX = 10*1024*1024;//磁盘最大缓存  10M

    private Context context;

    public BitmapCache(Context context) {
        this.context = context;

        //获得应用程序的最大内存
        int maxSize = (int) Runtime.getRuntime().maxMemory();
        //用最大内存的1/8来作为图片缓存
        this.lruCache = new LruCache<String, Bitmap>(maxSize / 8) {
            // 必须重写此方法  获得图片大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        //获取DiskLruCache对象 参数：1.缓存到的文件 2.应用的版本号 3.一个key可以对应多少个缓存文件 4.最多缓存多少字节的数据
        try {
            diskLruCache = DiskLruCache.open(getDiskCacheDir(context,"mycache"),getAppVersion(context),1,DISK_MAX);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *   从缓存中根据key取得相应图片  先判断内存缓存，没有再去磁盘缓存找，都没有再访问网络下载
     */
    @Override
    public Bitmap getBitmap(String url) {
        if (lruCache.get(url)!=null){
            Log.i("tag","从LruCahce获取");
            return lruCache.get(url);
        }

        String key = MD5Utils.md5(url);
        try {
            if(diskLruCache.get(key)!=null){
                DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
                Bitmap bitmap = null;
                if (snapshot!=null){
                    bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                    lruCache.put(url,bitmap);
                    Log.i("tag","从DiskLruCache获取");
                }

                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //将图片和对应得key放入缓存
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
        // 判断是否存在DiskLruCache缓存，若没有存入
        String key = MD5Utils.md5(url);//因为url可能存在特殊字符，为防止编码错误，进行md5加密，保证key为全字符或数字
        try {
            if (diskLruCache.get(key) == null) {
                //通过editor打开一个输出流
                DiskLruCache.Editor editor = diskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);//因定义的一个key只能对应一个文件 所以这里的参数index填0
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        editor.commit();//提交操作
                    } else {
                        editor.abort();//放弃操作
                    }
                }

                //用于将内存中的操作记录同步到日志文件（也就是journal文件）当中。这个方法非常重要，因为DiskLruCache
                // 能够正常工作的前提就是要依赖于journal文件中的内容。前面在讲解写入缓存操作的时候我有调用过一次这个
                // 方法，但其实并不是每次写入缓存都要调用一次flush()方法的，频繁地调用并不会带来任何好处，只会额外增
                // 加同步journal文件的时间。比较标准的做法就是在Activity的onPause()方法中去调用一次flush()方法就可以了。
                diskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法会判断当前sd卡是否存在，然后选择缓存地址
     *
     * @param context  上下文
     * @param uniqueName  对应的文件夹名字  拼接上去的
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalCacheDir().getPath();
            } else {
                cachePath = context.getCacheDir().getPath();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}