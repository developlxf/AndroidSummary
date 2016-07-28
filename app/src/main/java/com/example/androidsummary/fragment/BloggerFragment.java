package com.example.androidsummary.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidsummary.BlogListActivity;
import com.example.androidsummary.R;
import com.example.androidsummary.adapter.BloggerListAdapter;
import com.example.androidsummary.bean.AppConstants;
import com.example.androidsummary.bean.Blogger;
import com.example.androidsummary.sql.BloggerDao;
import com.example.androidsummary.sql.DaoFactory;
import com.example.androidsummary.utils.BloogerManager;
import com.example.androidsummary.utils.CategoryManager;
import com.example.androidsummary.utils.ExtraString;
import com.example.androidsummary.utils.HttpAsyncTask;
import com.example.androidsummary.utils.JsoupUtil;
import com.example.androidsummary.utils.SpfUtils;
import com.example.androidsummary.widget.BloggerAddDialog;

import java.util.HashMap;
import java.util.List;

public class BloggerFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private View rootView;//fragment的视图
    private ListView xListView;
    /**
     * 主页的博主列表
     */
    private List<Blogger> bloggerList;
    private BloggerListAdapter adapter;

    /**
     * 博主数据库
     */
    public BloggerDao bloggerDao;
    private String newUserId;
    private String type = CategoryManager.CategoryName.ANDROID;//android

    //添加博主返回的状态常量
    private static final int MSG_ADD_SUCCESS = 1000;
    private static final int MSG_ADD_FAILURE = 1001;
    private static final int MSG_ADD_REPEAT = 1002;
    private static final int MSG_ADD_EMPTY = 1003;
    private static final int MSG_ADD_BLOG = 1004;
    private Blogger blogger_curr;
    private HashMap<String, String> mAddBloggerItem;

    private ProgressDialog progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initData();

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_blogger, container, false);
            xListView = (ListView) rootView.findViewById(R.id.listView);
            adapter = new BloggerListAdapter(getActivity(), bloggerList);
            xListView.setAdapter(adapter);
            xListView.setOnItemClickListener(this);
            xListView.setOnItemLongClickListener(this);


            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BloggerAddDialog dialog = new BloggerAddDialog(getActivity(), new BloggerAddDialog.OnConfirmListener() {

                        @Override
                        public void onConfirm(String result) {


                            if (TextUtils.isEmpty(result)) {
                                mHandler.sendEmptyMessage(MSG_ADD_EMPTY);
                                return;
                            }

                            if (bloggerDao.query(result) != null) {
                                mHandler.sendEmptyMessage(MSG_ADD_REPEAT);
                                return;
                            }

                            newUserId = result;

                            //显示正在添加的对话框
                            progressBar = new ProgressDialog(getActivity());
                            progressBar.show();
                            progressBar.setContentView(R.layout.dialog_loading);//必须放在show后面。。。。否则异常，因为show里面会掉requestFeature方法

                            mHandler.sendEmptyMessageDelayed(MSG_ADD_BLOG, 1000);
                        }

                    });

                    dialog.show();
                }
            });
        }
        return rootView;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what != MSG_ADD_BLOG && progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();
            }

            switch (msg.what) {
                case MSG_ADD_SUCCESS:
                    Toast.makeText(getActivity(),"博客ID添加成功",Toast.LENGTH_SHORT).show();
                    addBlogger();
                    break;

                case MSG_ADD_FAILURE:
                    Toast.makeText(getActivity(),"博客ID不存在，添加失败",Toast.LENGTH_SHORT).show();
                    break;

                case MSG_ADD_EMPTY:
                    Toast.makeText(getActivity(),"博客ID为空",Toast.LENGTH_SHORT).show();
                    break;

                case MSG_ADD_REPEAT:
                    Toast.makeText(getActivity(),"博客ID重复添加",Toast.LENGTH_SHORT).show();
                    break;

                case MSG_ADD_BLOG:
                    requestData(newUserId);
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }

    };

    /**
     * 请求博主数据
     *
     * @param result
     */
    private void requestData(String result) {
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(getActivity());
        httpAsyncTask.execute(AppConstants.CSDN_BASE_URL + result);
        httpAsyncTask.setOnResponseListener(new HttpAsyncTask.OnResponseListener() {
            @Override
            public void onResponse(String resultString) {
                if (TextUtils.isEmpty(resultString)) {
                    mHandler.sendEmptyMessage(MSG_ADD_FAILURE);
                } else {
                    //解析html数据
                    mAddBloggerItem = JsoupUtil.getBloggerItem(resultString);
                    mHandler.sendEmptyMessage(MSG_ADD_SUCCESS);
                }
            }
        });
    }


    private void addBlogger() {
        Blogger blogger = new Blogger();
        blogger.setUserId(newUserId);
        blogger.setTitle(mAddBloggerItem.get("title"));
        blogger.setDescription(mAddBloggerItem.get("description"));
        blogger.setImgUrl(mAddBloggerItem.get("imgUrl"));
        blogger.setLink(AppConstants.CSDN_BASE_URL + newUserId);
        blogger.setType(CategoryManager.CategoryName.ANDROID);
        blogger.setIsTop(0);
        blogger.setIsNew(1);
        blogger.setUpdateTime(System.currentTimeMillis());
        bloggerDao.insert(blogger);

        bloggerList = bloggerDao.queryAll();
        adapter.setList(bloggerList);
    }

    private void initData() {
        //通过本地保存的SharedPreferences得到我们保存的数据的具体类型  参数2.key  3.取不到时的默认值
        type = (String) SpfUtils.get(getActivity(), ExtraString.BLOG_TYPE, CategoryManager.CategoryName.ANDROID);
        //获得博主数据库  通过DbUtils创建  第二个参数为数据库名
        bloggerDao = DaoFactory.getInstance().getBloggerDao(getActivity(), type);
        //在初始化的最后将判断是不是第一次创建的boolean值改为false，将type对应BLOG_TYPE键保存到sharedPreferences
        new BloogerManager().init(getActivity(), bloggerDao, type);

        //查询所有博主
        bloggerList = bloggerDao.queryAll();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        blogger_curr = (Blogger) parent.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(),BlogListActivity.class);
        intent.putExtra("blogger",blogger_curr);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //获得当前的点击对象
        blogger_curr = (Blogger) parent.getAdapter().getItem(position);
        //操作
        String str = blogger_curr.getIsTop() == 0 ? "置顶博主" : "取消置顶";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(new String[]{str, "删除博主", "取消操作"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (blogger_curr.getIsTop() == 1) {
                            blogger_curr.setIsTop(0);
                            Toast.makeText(getActivity(), "取消置顶成功", Toast.LENGTH_SHORT).show();
                        } else {
                            blogger_curr.setIsTop(1);
                            Toast.makeText(getActivity(), "置顶成功", Toast.LENGTH_SHORT).show();
                        }

                        blogger_curr.setUpdateTime(System.currentTimeMillis());
                        bloggerDao.insert(blogger_curr);//更新数据库中的数据
                        //更新界面
                        bloggerList = bloggerDao.queryAll();
                        adapter.setList(bloggerList);
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        bloggerDao.delete(blogger_curr);//从博主数据库中删除此博主
                        bloggerList = bloggerDao.queryAll();
                        adapter.setList(bloggerList);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        break;
                }
            }
        }).show();

        return true;
    }

    public BloggerListAdapter getAdapter() {
        return adapter;
    }

}
