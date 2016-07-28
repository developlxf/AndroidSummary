package com.example.androidsummary;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.androidsummary.adapter.WelcomePagerAdapter;
import com.nineoldandroids.view.ViewHelper;

public class WelcomeActivity extends BaseActivity {
    private ViewPager viewPager;
    private TextView tv_skip;
    private static final int PAGER_NUM = 3;

    private int startX ;

    private boolean isFirst = true;//判断是不是第一次跳转

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tv_skip = (TextView) findViewById(R.id.btn_skip);
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toClass(MainActivity.class);
                finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager_wel);
        viewPager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager()));
        //设置viewpager自定义的切换动画
        viewPager.setPageTransformer(true, new MyPageTransformer());
        //监听滑动到最后一页时，再向左滑动则进入主界面
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("slip",viewPager.getCurrentItem()+"");
                        if (viewPager.getCurrentItem() == PAGER_NUM-1){
                            if (startX-event.getX() >80 ){
                                if (isFirst){
                                    isFirst = false;
                                    toClass(MainActivity.class);
                                    finish();
                                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                                }
                            }
                        }
                        break;
                }

                return false;
            }
        });
    }


    class MyPageTransformer implements ViewPager.PageTransformer {

        //1. 控件（当前的页面）  2.变换状态区间 （-1，0】 【0，1）
        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();

            //当前页面的背景和文字
            View backgroundView = page.findViewById(R.id.welcome_fragment);
            View text_head = page.findViewById(R.id.heading);
            View text_content = page.findViewById(R.id.content);

            //第一个fragment的3个图片
            View object1 = page.findViewById(R.id.a000);
            View object2 = page.findViewById(R.id.a001);
            View object3 = page.findViewById(R.id.a002);

            //第二个fragment的4个图片
            View object4 = page.findViewById(R.id.a003);
            View object5 = page.findViewById(R.id.a004);
            View object6 = page.findViewById(R.id.a005);
            View object7 = page.findViewById(R.id.a006);

            //第三个fragment的3个图片
            View object10 = page.findViewById(R.id.a010);
            View object12 = page.findViewById(R.id.a012);
            View object13 = page.findViewById(R.id.a013);

            //当前页面的总体效果
            ViewHelper.setTranslationX(page, pageWidth * -position);


            if (position <= -1.0f || position >= 1.0f) {

            } else if (position == 0.0f) {

            } else {//如果position在（-1，1），不包括0，0表示没有滑动

                if (backgroundView != null) {//背景透明度变化
                    ViewHelper.setAlpha(backgroundView, 1.0f - Math.abs(position));
                }

                //文字  平移加透明度变化
                if (text_head != null) {
                    ViewHelper.setTranslationX(text_head, pageWidth * position);
                    ViewHelper.setAlpha(text_head, 1.0f - Math.abs(position));
                }

                if (text_content != null) {
                    ViewHelper.setTranslationX(text_content, pageWidth * position);
                    ViewHelper.setAlpha(text_content, 1.0f - Math.abs(position));
                }

                //第一个fragment三个图片的平移变换  其中第三个图片速度稍慢
                if (object1 != null) {
                    ViewHelper.setTranslationX(object1, pageWidth * position);
                }
                if (object2 != null) {
                    ViewHelper.setTranslationX(object2, pageWidth * position);
                }
                if (object3 != null) {
                    ViewHelper.setTranslationX(object3, (float) (pageWidth / 1.2 * position));
                }

                //第二个fragment四个图片的平移变换  速度相同
                if (object4 != null) {
                    ViewHelper.setTranslationX(object4, pageWidth / 2 * position);
                }
                if (object5 != null) {
                    ViewHelper.setTranslationX(object5, pageWidth / 2 * position);
                }
                if (object6 != null) {
                    ViewHelper.setTranslationX(object6, pageWidth / 2 * position);
                }
                if (object7 != null) {
                    ViewHelper.setTranslationX(object7, pageWidth / 2 * position);
                }

                //第三个fragment的三个图片的平移变换
                if(object10 != null){
                    ViewHelper.setTranslationX(object10,pageWidth/2 * position);
                }
                if (object12 != null) {
                    ViewHelper.setTranslationX(object12, (float) (pageWidth / 1.3 * position));
                }
                if (object13 != null) {
                    ViewHelper.setTranslationX(object13, (float) (pageWidth / 1.8 * position));
                }

            }
        }
    }
}
