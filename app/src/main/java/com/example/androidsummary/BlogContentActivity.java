package com.example.androidsummary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.androidsummary.bean.BlogHtml;
import com.example.androidsummary.bean.BlogItem;
import com.example.androidsummary.sql.BlogCollectDao;
import com.example.androidsummary.sql.BlogCollectDaoImpl;
import com.example.androidsummary.sql.BlogContentDao;
import com.example.androidsummary.sql.BlogContentDaoImpl;
import com.example.androidsummary.utils.HttpAsyncTask;
import com.example.androidsummary.utils.JsoupUtil;

public class BlogContentActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, HttpAsyncTask.OnResponseListener {

    private WebView mWebView = null;
    private ProgressBar mProgressBar; // 进度条
    private ImageView mReLoadImageView; // 重新加载的图片
    private ImageView mShareBtn;
    private ToggleButton mCollectBtn;

    private BlogCollectDao mBlogCollectDao;
    private BlogItem mBlogItem;
    public String mTitle;
    private String mUrl;
    private String mFileName;
    private static final int MSG_RELOAD_DATA = 1000;
    private boolean isFirstCheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_content);

        init();

        //预加载数据
        mHandler.sendEmptyMessage(MSG_RELOAD_DATA);
    }
    private void getData(String url) {
        BlogContentDao blogContentDb = new BlogContentDaoImpl(this, url);
        BlogHtml blogHtml = blogContentDb.query(url);
        if (blogHtml != null) {
            mTitle = blogHtml.getTitle();
            loadHtml(blogHtml.getHtml());
        } else {
            requestData(url);
        }
    }
    /**
     * 加载数据
     */
    private void requestData(String url) {
        mProgressBar.setVisibility(View.VISIBLE);
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(this);
        httpAsyncTask.execute(url);
        httpAsyncTask.setOnResponseListener(this);
    }

    /**
     * 加载页面
     *
     */
    private void loadHtml(String html) {
        if (!TextUtils.isEmpty(html)) {
            mWebView.loadDataWithBaseURL("http://blog.csdn.net", html, "text/html", "utf-8", null);
            mReLoadImageView.setVisibility(View.GONE);
        } else {
            Toast.makeText(this,"网络已断开",Toast.LENGTH_SHORT).show();
        }

        mProgressBar.setVisibility(View.GONE);
    }
    // 预加载数据
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RELOAD_DATA:
                    Log.i("url","+++++"+mUrl);
                    getData(mUrl);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void init() {

        mBlogCollectDao = new BlogCollectDaoImpl(this);

        //从上个Activity传过来的数据
        mBlogItem = (BlogItem) getIntent().getSerializableExtra("blogitem");
        Log.i("url","mBlogItem   "+mBlogItem);
        if (mBlogItem != null) {
            mUrl = mBlogItem.getLink();
            Log.i("url","-----"+mUrl);

            mTitle = mBlogItem.getTitle();
            mFileName = mUrl.substring(mUrl.lastIndexOf("/") + 1);
        }

        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_blogcontent);
        toolbar.setTitle(mTitle);
        setSupportActionBar(toolbar);

        //初始化控件
        mProgressBar = (ProgressBar) findViewById(R.id.blogContentPro);
        mReLoadImageView = (ImageView) findViewById(R.id.reLoadImage);
        mShareBtn = (ImageView) findViewById(R.id.iv_share);
        mCollectBtn = (ToggleButton) findViewById(R.id.tb_collect);

        mReLoadImageView.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
        mCollectBtn.setOnCheckedChangeListener(this);
        if (isCollect()) {
            isFirstCheck = true;
            mCollectBtn.setChecked(true);
        }

        //
        /**
         * 初始化web控件
         */
        mWebView = (WebView) findViewById(R.id.webview);
        //为了让WebView能够响应超链接功能
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);//设置webview支持JavaScript 能够执行Javascript脚本
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");//设置默认编码格式
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);


        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局  使内容只有一列  不能左右滑动  4.4以下有效

        /*
         * 自适应屏幕  可双击放大
         */
//        mWebView.getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小
//        mWebView.getSettings().setLoadWithOverviewMode(true);//缩放至屏幕大小

        // LOAD_CACHE_ELSE_NETWORK，优先使用缓存。
        // LOAD_NO_CACHE: 不适用缓存。
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }
    /**
     * 判断是否收藏
     * @return
     */
    private boolean isCollect() {
        if (mBlogCollectDao!=null && null != mBlogCollectDao.query(mBlogItem.getLink())) {
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_collect://收藏按钮
                if (isCollect()){
                    mCollectBtn.setChecked(false);
                }else{
                    mCollectBtn.setChecked(true);
                }
                break;
            case R.id.iv_share://分享按钮
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,mTitle+":\n"+mUrl);
                startActivity(Intent.createChooser(intent,"CSDN博客分享"));
                break;
        }
    }

    //收藏按钮状态改变时  从数据库中添加或者删除
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 屏蔽第一次也会进来的问题
        if (isFirstCheck) {
            isFirstCheck = false;
            return;
        }

        if (isChecked){
            mBlogCollectDao.insert(mBlogItem);
            Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
        }else{
            mBlogCollectDao.delete(mBlogItem);
            Toast.makeText(this,"取消收藏成功",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(String resultString) {
        mTitle = JsoupUtil.getTitle(resultString);
        String content = JsoupUtil.getContent(resultString);
        loadHtml(content);
        saveDb(content);
    }


    /**
     * 保存数据库
     *
     * @param html
     */
    private void saveDb(String html) {
        if (TextUtils.isEmpty(html)) {
            return;
        }
        BlogHtml blogHtml = new BlogHtml();
        blogHtml.setUrl(mUrl);
        blogHtml.setHtml(html);
        blogHtml.setTitle(mTitle);
        blogHtml.setUpdateTime(System.currentTimeMillis());
        blogHtml.setReserve("");

        BlogContentDao blogContentDb = new BlogContentDaoImpl(this, mUrl);
        blogContentDb.insert(blogHtml);
    }

}
