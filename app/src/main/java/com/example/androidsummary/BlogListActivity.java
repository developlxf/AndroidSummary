package com.example.androidsummary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidsummary.adapter.BlogCategoryAdapter;
import com.example.androidsummary.adapter.BlogListAdapter;
import com.example.androidsummary.bean.AppConstants;
import com.example.androidsummary.bean.BlogCategory;
import com.example.androidsummary.bean.BlogItem;
import com.example.androidsummary.bean.Blogger;
import com.example.androidsummary.sql.BlogItemDao;
import com.example.androidsummary.sql.DaoFactory;
import com.example.androidsummary.utils.HttpAsyncTask;
import com.example.androidsummary.utils.JsoupUtil;
import com.example.androidsummary.utils.URLUtil;
import com.example.androidsummary.widget.XListView;

import java.util.ArrayList;
import java.util.List;

public class BlogListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, XListView.IXListViewLoadMore, View.OnClickListener {
    private Blogger blogger;
    private XListView listView;
    private BlogListAdapter adapter;
    private HttpAsyncTask httpAsyncTask;
    private ImageView mReLoadImageView;
    private ProgressBar mPbLoading;

    //博主对应的博客数据库
    private BlogItemDao blogItemDao;
    //博客类别列表
    private List<BlogCategory> blogCategoryList;


    private int mPage = 1;
    private String mCategory = AppConstants.BLOG_CATEGORY_ALL;
    private String mBaseUrl = "";

    private ImageView iv_back;
    private TextView tv_title;
    private ImageView ib_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);

        initData();

        initView();

        if (blogger!=null)
        initToolbar();
    }

    private void initView() {
        blogCategoryList = new ArrayList<BlogCategory>();
        Log.i("list",blogCategoryList+"初始化");

        listView = (XListView) findViewById(R.id.listView_blog);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);

        //网络加载失败时  点击屏幕重新加载的按钮
        mReLoadImageView = (ImageView) findViewById(R.id.reLoadImage);
        mReLoadImageView.setOnClickListener(this);

        adapter = new BlogListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        // 先预加载数据，再请求最新数据
        mHandler.sendEmptyMessage(AppConstants.MSG_PRELOAD_DATA);

        ib_menu = (ImageView) findViewById(R.id.btn_menu);
        ib_menu.setImageResource(R.drawable.ic_add);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(ib_menu);
            }
        });



    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConstants.MSG_PRELOAD_DATA:
                    //按当前类别查询第一页
                    List<BlogItem> list = blogItemDao.query(mCategory, mPage);

                    if (list != null && list.size() != 0) {
                        adapter.setList(list);
                        listView.setPullLoadEnable(BlogListActivity.this);// 设置可上拉加载
                        listView.stopLoadMore();
                        mPbLoading.setVisibility(View.GONE);
                    } else {
                        // 不请求最新数据，让用户自己刷新或者加载
                        mPbLoading.setVisibility(View.VISIBLE);
                        getData(mPage);
                    }

                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void getData(int page) {
        if (httpAsyncTask != null) {
            httpAsyncTask.cancel(true);
        }

        httpAsyncTask = new HttpAsyncTask(this);
        String url = URLUtil.getBlogListURL(mBaseUrl, page);
        httpAsyncTask.execute(url);
        httpAsyncTask.setOnResponseListener(mOnResponseListener);
    }

    //判断网络是否可用
    private boolean isNetworkAvailable(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    //按页请求数据
    private void requestData() {
        if (isNetworkAvailable(this)) {
            getData(mPage);
        } else {
            mHandler.sendEmptyMessage(AppConstants.MSG_PRELOAD_DATA);
        }
    }

    private HttpAsyncTask.OnResponseListener mOnResponseListener = new HttpAsyncTask.OnResponseListener() {

        @Override
        public void onResponse(String resultString) {
            // 解析html页面获取列表
            if (resultString != null) {
                //返回博文列表  并填充博客类别列表

                Log.i("list",blogCategoryList + " 异步任务---");
                Log.i("list",blogCategoryList.toString());

                List<BlogItem> list = JsoupUtil.getBlogItemList(mCategory, resultString, blogCategoryList);

                Log.i("list",blogCategoryList + " 异步任务");

                if (list != null && list.size() > 0) {
                    if (mPage == 1) {
                        adapter.setList(list);
                    } else {
                        adapter.addList(list);
                    }
                    listView.setPullLoadEnable(BlogListActivity.this);// 设置可上拉加载

                    saveDB(list);
                    mReLoadImageView.setVisibility(View.GONE);
                } else {
                    if (adapter.getCount() == 0) {
                        mReLoadImageView.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(BlogListActivity.this,"暂无最新数据",Toast.LENGTH_SHORT).show();
                }
            } else {
                if (adapter.getCount() == 0) {
                    mReLoadImageView.setVisibility(View.VISIBLE);
                }
                Toast.makeText(BlogListActivity.this,"网络已断开",Toast.LENGTH_SHORT).show();
            }

            mPbLoading.setVisibility(View.GONE);
            listView.stopLoadMore();
        }
    };

    /**
     * 保存数据库
     *
     * @param list
     */
    private void saveDB(final List<BlogItem> list) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                blogItemDao.insert(mCategory, list);

                if (blogCategoryList != null) {
                    blogItemDao.insertCategory(blogCategoryList);
                }
            }
        }).start();

    }
    private void initData() {
        blogger = (Blogger) getIntent().getSerializableExtra("blogger");

        //通过博主的id来创建博客数据库
        blogItemDao = DaoFactory.getInstance().getBlogItemDao(this,blogger.getUserId());

        mBaseUrl = URLUtil.getBlogDefaultUrl(blogger.getUserId());

        //从数据库中查询博客所有分类列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                //必须判断 否则第一次加载时数据库为空  会把blogCategoryList变为空
                if (blogItemDao.queryCategory()!=null)
                blogCategoryList = blogItemDao.queryCategory();
                Log.i("list",blogCategoryList + "  数据库");
            }
        }).start();
    }

    private void initToolbar() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.btn_back);
        tv_title.setText(blogger.getTitle());
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlogListActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (httpAsyncTask!=null){
            httpAsyncTask.cancel(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BlogItem blogItem = (BlogItem) parent.getAdapter().getItem(position);
        Intent intent = new Intent(this,BlogContentActivity.class);
        intent.putExtra("blogitem",blogItem);
        startActivity(intent);
        //Activity切换动画
        overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        requestData();
    }

    private void refresh() {
        mReLoadImageView.setVisibility(View.GONE);
        listView.disablePullLoad();
        mPage = 1;
        requestData();
    }



    private PopupWindow mPopupWindow;
    /**
     * 显示PopWindow
     */
    private void showMenu(View view) {
        if (mPopupWindow == null) {
            getPopupWindow(view);
        }

//        int xOffset = (int) getResources().getDimension(R.dimen.popwindow_bloglist_width) - DisplayUtil.dp2px(this, 40);
//        mPopupWindow.showAsDropDown(view, (-1) * xOffset, 0);
        mPopupWindow.showAsDropDown(view, 0, 0);
    }

    /**
     * 初始化PopupWindow
     *
     * @param view
     */
    private void getPopupWindow(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popwindow_bloglist, null);

        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));

        ListView listView = (ListView) contentView.findViewById(R.id.lv_blog_type);
        Log.i("list",blogCategoryList+"  ");
        final BlogCategoryAdapter adapter_popup = new BlogCategoryAdapter(this, blogCategoryList);
        listView.setAdapter(adapter_popup);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mPopupWindow.dismiss();
                if (position == 0) {
                    tv_title.setText(blogger.getTitle());
                    mCategory = AppConstants.BLOG_CATEGORY_ALL;
                    mBaseUrl = URLUtil.getBlogDefaultUrl(blogger.getUserId());

                    adapter.clearList();
                    mPbLoading.setVisibility(View.VISIBLE);
                    refresh();
                } else {
                    BlogCategory blogCategory = ((BlogCategoryAdapter) parent.getAdapter()).getItem(position);
                    tv_title.setText(blogCategory.getName());
                    mCategory = blogCategory.getName();
                    mBaseUrl = URLUtil.getBlogCategoryUrl(blogCategory.getLink());

                    adapter.clearList();
                    mPbLoading.setVisibility(View.VISIBLE);
                    refresh();
                }
            }
        });

        contentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reLoadImage:
                refresh();
                break;
        }
    }
}
