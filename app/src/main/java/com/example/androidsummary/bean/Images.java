package com.example.androidsummary.bean;

import android.util.Log;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by 伦小丹 on 2015/12/22 0022.
 */
public class Images {
    public static String[] imagePaths = new String[]{
            "http://img.ivsky.com/img/tupian/pre/201509/02/weimei_qiutian_fengjing.jpg",
            "http://img.ivsky.com/img/tupian/pre/201509/02/weimei_qiutian_fengjing-001.jpg",
            "http://img.ivsky.com/img/tupian/pre/201509/02/weimei_qiutian_fengjing-003.jpg",
            "http://img.ivsky.com/img/tupian/pre/201509/02/weimei_qiutian_fengjing-004.jpg",
            "http://img.ivsky.com/img/tupian/pre/201509/02/weimei_qiutian_fengjing-005.jpg",
            "http://img.ivsky.com/img/tupian/pre/201509/02/weimei_qiutian_fengjing-006.jpg",
            "http://img.hb.aicdn.com/06e5ee6af814657a3371f0f514c29e891fadb6db1d76c-3m7C5d_fw658",
            "http://img.hb.aicdn.com/425c82c62a49749203b26e7afef61a7453938ec81ee86-OSrbnk_fw658",
            "http://img.hb.aicdn.com/e83935b9b92df8c7f8401943cfc1148c8be4fb5b13571-psM0Jm_fw658",
            "http://huaban.com/go/?pin_id=254930922",
            "http://img.hb.aicdn.com/f9b62ab75be550929e3b9405e5ef7ccfff71a6441ce84-FB2yHh_fw658",
            "http://img.hb.aicdn.com/e2cce2fba3fc4e2b2ab18c21fa66a44fdace305e5e312-6ewGZ5_fw658",
            "http://img.hb.aicdn.com/fd27cc5c68511bd16efe2595a03b7eed2180ced8722ef-V6rVt7_fw658",
            "http://img.hb.aicdn.com/fca032623b5371f00f6ed4e862675d385cb581a21c9bf-rvZSju_fw658",
            "http://img.hb.aicdn.com/a14033f716c4bd48af1a8d13499a5cb321f19f121d119-EyRGYe_fw658",
            "http://img.hb.aicdn.com/e810d0495b493ee631c7e5618d11b43afed250e319666-vlOhkE_fw658",
            "http://img.hb.aicdn.com/47ad0e1fd529da77a6c8c6cf7c869e93f1bdcd3817c20-n2VRau_fw658",
            "http://img.hb.aicdn.com/c5981c728059d50b2ab1f9f1145b8b94ab57d4b715158-fjKM0y_fw658",
            "http://img.hb.aicdn.com/593ac9a284ff8b12fb3dbe8d2cd4a548c688a60feb0b-aSQB68_fw658",
            "http://img.hb.aicdn.com/cb54d719dae8ecb100332544ad66f30bf4c25c8713efc-BcTi6f_fw658",
            "http://img.hb.aicdn.com/d1a51434128168238395006ef8e294c00973c44713437f-uCHtNx_fw658",
            "http://img.hb.aicdn.com/96522248e829c94bfac65c103edb22f3e9414e80675f3-myqJHU_fw658",
            "http://img.hb.aicdn.com/a0287332335eb896532afe7d21b053fd9dc685c71164b-Kq0Ktr_fw658",
            "http://img.hb.aicdn.com/c2e1d4bc4956b61b027e8fc0b56d501691e4367925462-kbYYj8_fw658",
            "http://img.hb.aicdn.com/c0a941e3866f9aa512e34beeac9f4f92c6eb1e5113212-I7zEu6_fw658",
            "http://huaban.com/go/?pin_id=254919980",
            "http://img.hb.aicdn.com/a3aa19926e7f59e7927f8de55b8288309e0571fd3df6-jMin8c_fw658",
            "http://pic2015.5442.com/2015/1214/17/1.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/2.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/3.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/4.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/5.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/6.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/7.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/8.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/9.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/10.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/11.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/12.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/13.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/14.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/15.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/16.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/17.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/18.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/19.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/20.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/21.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/22.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/23.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/24.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/25.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1214/17/26.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/1.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/2.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/3.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/4.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/5.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/6.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/7.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/8.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/9.jpg!960.jpg",
            "http://pic2015.5442.com/2015/1208/7/10.jpg!960.jpg"
    };

    public static String[] randomImages(){
        String[] images = new String[30];

        //使用Set集合装载随机生成的30个数字  防止重复
        Set<Integer> set = new HashSet<Integer>();
        set.clear();
        Random rand = new Random();
        while (set.size() < 30){
            int i = rand.nextInt(imagePaths.length);
            Log.i("images",set.size()+"");
            set.add(i);
        }

        int index = 0;
        //赋值
        for (int m : set){
            images[index] = imagePaths[m];
            index++;
        }

        Log.i("images",images[0].toString());
        Log.i("images",images[1].toString());
        return images;
    }
}
