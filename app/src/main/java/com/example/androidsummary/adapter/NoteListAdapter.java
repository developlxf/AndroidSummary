package com.example.androidsummary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidsummary.R;
import com.example.androidsummary.bean.NoteItem;

import java.util.ArrayList;

/**
 * Created by 伦小丹 on 2015/12/22 0022.
 */
public class NoteListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<NoteItem> array;

    public NoteListAdapter(LayoutInflater inf, ArrayList<NoteItem> arry) {
        this.inflater = inf;
        this.array = arry;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.note_list_item, null);
            vh.tv1 = (TextView) convertView.findViewById(R.id.tv_title);
            vh.tv2 = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        vh.tv1.setText(array.get(position).getTitle());
        vh.tv2.setText(array.get(position).getTimes());
        return convertView;
    }

    class ViewHolder {
        TextView tv1, tv2;
    }
}
