package com.example.androidsummary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidsummary.R;
import com.example.androidsummary.bean.Blogger;
import com.example.androidsummary.utils.ImageLoaderUtils;
import com.example.androidsummary.widget.CircleImageView;

import java.util.List;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class BloggerListAdapter extends BaseAdapter {

    private Context context;
    private List<Blogger> list;

    public BloggerListAdapter(Context context, List<Blogger> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<Blogger> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public Blogger getItem(int position) {
        Log.i("size",position+" "+list.size());
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.blogger_list_item, null);
            holder = new ViewHolder();
            holder.imageView = (CircleImageView) convertView.findViewById(R.id.imv_blogger);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_blog_title);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_blog_desc);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //设置图片资源
        if (!TextUtils.isEmpty(list.get(position).getImgUrl())){
            ImageLoaderUtils.displayImg(list.get(position).getImgUrl(),holder.imageView,R.drawable.ic_default);
        }else{
            holder.imageView.setImageResource(R.drawable.ic_default);
        }

        //设置文本资源
        if (list.get(position).getIsTop() == 1) {
            //  \b空一位
            holder.tv_title.setText(list.get(position).getTitle() + "\b[顶]");
            holder.tv_title.setTextColor(Color.BLUE);
        } else {
            holder.tv_title.setText(list.get(position).getTitle());
            holder.tv_title.setTextColor(Color.BLACK);
        }

        if (!TextUtils.isEmpty(list.get(position).getDescription())) {
            holder.tv_desc.setVisibility(View.VISIBLE);
            holder.tv_desc.setText(list.get(position).getDescription());
        } else {
            holder.tv_desc.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder{
        CircleImageView imageView;
        TextView tv_title,tv_desc;
    }
}
