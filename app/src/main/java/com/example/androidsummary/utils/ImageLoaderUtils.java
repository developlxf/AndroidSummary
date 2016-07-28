package com.example.androidsummary.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.androidsummary.R;

/**图片加载工具类
 * 使用Glide进行加载图片
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class ImageLoaderUtils {
    /**
     * 加载图片
     * @param url 图片路径
     * @param container 图片要放置的ImageView
     * @param defaultImgid 图片加载失败时显示的默认图片
     */
    public static void displayImg(String url,ImageView container,int defaultImgid){
        Glide.with(container.getContext()).load(url)//通过url加载图片
                .centerCrop()//图片等比缩放 居中显示
                .crossFade()//淡入淡出动画  可以添加参数设置动画时间
                .error(defaultImgid)//加载失败就显示默认图片
                .into(container);//加载成功就将图片显示到指定的ImageView
    }

    //加载sd卡图片  缓存
    public static void displaySDImg(String url,ImageView container){
        if (TextUtils.isEmpty(url)){
            return;
        }

        String fileUrl = "";
        if (url.contains("file:/")){
            fileUrl = url;
        }else{
            fileUrl = "file:/"+url;
        }

        displayImg(fileUrl,container, R.drawable.ic_default);
    }
}
