package com.example.androidsummary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.androidsummary.adapter.PhotoWallRecyclerAdapter;
import com.example.androidsummary.bean.Images;
import com.example.androidsummary.widget.PhotoShowActivity;

import java.io.ByteArrayOutputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 1.通过三级缓存加载网络图片
 * 2.RecyclerView的使用
 * 3.Activity之间传递图片
 */
public class PhotoWallActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;//Goolge官方的下拉刷新控件
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private boolean isGrid = true;

    //当前显示的图片路径 数据
    private String[] imagePaths = new String[30];
    private PhotoWallRecyclerAdapter adapter;

    private ProgressBar progressBar;
    private ImageView iv_menu;//右上角的图标
    private TextView tv_title;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_wall);

        imagePaths = Images.randomImages();//初始化数据

        initViews();//初始化各个控件
    }

    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar_photowall);
        iv_menu = (ImageView) findViewById(R.id.btn_menu);
        iv_menu.setImageResource(R.drawable.grids);
        iv_menu.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("照片墙");
        iv_back = (ImageView) findViewById(R.id.btn_back);
        iv_back.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //设置刷新时的颜色  可以设置多个
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.parseColor("#9876AA"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        /**
                         * 执行完操作后执行此代码，停止刷新动画
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                                //刷新数据
                                imagePaths = Images.randomImages();
                                adapter.setImagePaths(imagePaths);
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                }, 1000);
            }
        });

        /**
         * 设置RecyclerView的布局管理器和适配器
         */
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new PhotoWallRecyclerAdapter(this, imagePaths);

        progressBar.setVisibility(View.GONE);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new PhotoWallRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NetworkImageView networkImageView) {
                /**
                 * 将获得的Drawable转换成Bitmap  再通过Intent传递过去
                 * 因为Drawable没有实现序列化接口，而Bitmap实现了
                 */
                Drawable drawable = networkImageView.getDrawable();
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();

                //bitmap 对象太大    转换成字节数组再传
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] photo = bos.toByteArray();

                Intent intent = new Intent(PhotoWallActivity.this, PhotoShowActivity.class);
                intent.putExtra("photo", photo);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_menu:
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                if (isGrid) {
                    isGrid = false;
                    iv_menu.setImageResource(R.drawable.list);
                    manager.setSpanCount(1);

                } else {
                    isGrid = true;
                    manager.setSpanCount(2);
                    iv_menu.setImageResource(R.drawable.grids);
                }

                recyclerView.setLayoutManager(manager);
                break;
            case R.id.btn_back:
                PhotoWallActivity.this.finish();
                break;
        }
    }
}
