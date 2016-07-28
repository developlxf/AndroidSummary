package com.example.androidsummary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidsummary.R;
import com.example.androidsummary.bean.AppConstants;
import com.example.androidsummary.bean.BlogItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class BlogListAdapter extends BaseAdapter{
    private ViewHolder holder; // 视图容器
    private LayoutInflater layoutInflater; // 布局加载器
    private Context context; // 上下文对象
    private List<BlogItem> list; // 博客列表

    public BlogListAdapter(Context context) {
        super();
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        list = new ArrayList<BlogItem>();

    }

    public void setList(List<BlogItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<BlogItem> list) {
        this.list.addAll(list);
    }

    public void clearList() {
        this.list.clear();
    }

    public List<BlogItem> getList() {
        return list;
    }

    public void removeItem(int position) {
        if (list.size() > 0) {
            list.remove(position);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            // 装载布局文件blog_list_item.xml
            convertView = layoutInflater.inflate(R.layout.listitem_blog, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.img = (ImageView) convertView.findViewById(R.id.blogImg);
            convertView.setTag(holder); // 表示给View添加一个格外的数据，
        } else {
            holder = (ViewHolder) convertView.getTag();// 通过getTag的方法将数据取出来
        }

        BlogItem item = null;
        if (list!=null){
            item  = list.get(position); // 获取当前数据
        }
        if (null != item) {
            // 显示标题内容
            holder.title.setText(item.getTitle());
            holder.content.setText("\b\b\b\b\b\b\b" + item.getContent());
            holder.date.setText(item.getDate());
            holder.img.setVisibility(View.VISIBLE);

            String icoType = item.getIcoType();
            if (AppConstants.BLOG_ICO_TYPE.BLOG_TYPE_ORIGINAL.equals(icoType)) {
                holder.img.setImageResource(R.drawable.ic_original);
            } else if (AppConstants.BLOG_ICO_TYPE.BLOG_TYPE_REPOST.equals(icoType)) {
                holder.img.setImageResource(R.drawable.ic_repost);
            } else if (AppConstants.BLOG_ICO_TYPE.BLOG_TYPE_TRANSLATED.equals(icoType)) {
                holder.img.setImageResource(R.drawable.ic_translate);
            } else {
                holder.img.setImageResource(R.drawable.ic_original);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView date;
        TextView title;
        ImageView img;
        TextView content;
    }
}
