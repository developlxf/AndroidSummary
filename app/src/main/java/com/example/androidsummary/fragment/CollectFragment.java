package com.example.androidsummary.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.androidsummary.BlogContentActivity;
import com.example.androidsummary.R;
import com.example.androidsummary.adapter.BlogListAdapter;
import com.example.androidsummary.bean.AppConstants;
import com.example.androidsummary.bean.BlogItem;
import com.example.androidsummary.sql.BlogCollectDaoImpl;
import com.example.androidsummary.widget.XListView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectFragment extends Fragment implements XListView.IXListViewLoadMore, XListView.IXListViewRefreshListener, AdapterView.OnItemClickListener {
    private XListView xListView;
    private List<BlogItem> list_blogitem;
    public BlogCollectDaoImpl blogCollectDao;
    private BlogListAdapter adapter;

    private int page = 1;
    private int pageSize = 10;

    public CollectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect, container, false);
        xListView = (XListView) view.findViewById(R.id.xlistview_collectblog);
        blogCollectDao = new BlogCollectDaoImpl(getActivity());
        adapter = new BlogListAdapter(getActivity());
        Log.i("collect", adapter.getCount() + "");
        xListView.setAdapter(adapter);
        xListView.setOnItemClickListener(this);
        //设置下拉刷新
        xListView.setPullRefreshEnable(CollectFragment.this);
        //设置xListView可上拉加载更多
        xListView.setPullLoadEnable(CollectFragment.this);
        //预加载数据
        handler.sendEmptyMessage(AppConstants.MSG_PRELOAD_DATA);
        return view;
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AppConstants.MSG_PRELOAD_DATA:
                    //首先加载前10条数据  然后每次收到消息就多加载一页
                    list_blogitem = blogCollectDao.query(page, pageSize);
                    if (list_blogitem!=null){
                        adapter.setList(list_blogitem);
                        adapter.notifyDataSetChanged();
                    }

                    //收到消息后停止刷新和加载动画
                    xListView.stopRefresh();
                    xListView.stopLoadMore();
                    break;
            }
        }
    };

    //开始加载
    @Override
    public void onLoadMore() {
        page++;
        //1000毫秒后发送空消息
        handler.sendEmptyMessageDelayed(AppConstants.MSG_PRELOAD_DATA, 1000);
    }

    //开始刷新
    @Override
    public void onRefresh() {
        page = 1;
        handler.sendEmptyMessageDelayed(AppConstants.MSG_PRELOAD_DATA, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BlogItem item = (BlogItem) adapter.getItem(position - 1);//因为有个下拉刷新头  此处减一
        Intent i = new Intent();
        i.setClass(getActivity(), BlogContentActivity.class);
        i.putExtra("blogitem", item);
        startActivity(i);

        // 动画过渡
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
    }

    public BlogListAdapter getAdapter() {
        return adapter;
    }
}
