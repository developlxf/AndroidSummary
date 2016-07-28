package com.example.androidsummary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 伦小丹 on 2015/12/22 0022.
 */
public class ImageCompress {
    public static Bitmap getBitmap(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只加载图片尺寸参数  不加载内容
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);

        //原图尺寸
        int width = options.outWidth;
        int height = options.outHeight;

        int sx =width/100;
        int sy =height/120;

        int s = sx>sy?sx:sy;

        options.inSampleSize = s;//设置压缩比率
        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeFile(path,options);

        Log.i("tag",bitmap.toString());
        return bitmap;
    }

    public static void storeImage(Bitmap bitmap){
        OutputStream os = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(),"lxfIcon.jpg");
            if (file.exists()==false){
                file.createNewFile();
            }
            os = new FileOutputStream(new File(file.getAbsolutePath()));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
