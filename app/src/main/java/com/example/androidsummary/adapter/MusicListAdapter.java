package com.example.androidsummary.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidsummary.R;
import com.example.androidsummary.bean.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伦小丹 on 2015/12/23 0023.
 */
public class MusicListAdapter extends BaseAdapter{
    private Cursor cursor;
    public static List<MusicInfo> musicInfos;
    private MusicInfo mi;

    private Context context;

    public MusicListAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;

        //初始化数据
        initData();

    }

    private void initData() {
        musicInfos = new ArrayList<MusicInfo>();
        cursor.moveToFirst();
        // 获得歌曲的各种属性
        for (int i = 0; i < cursor.getCount(); i++) {
            // 过滤mp3文件
            if (cursor.getString(5).endsWith(".mp3")) {
                mi = new MusicInfo();
                mi.setMusicName(cursor.getString(0));//歌曲名称
                mi.setMusicTime(cursor.getInt(1));//歌曲时间长度
                mi.setMusicAlbum(cursor.getString(2));//专辑
                mi.setMusicSinger(cursor.getString(3));//歌手
                mi.setMusicSize(cursor.getInt(4));//大小
                mi.setMusicPath(cursor.getString(5));//路径
                mi.set_id(cursor.getInt(6));//歌曲id

                Log.i("music",mi.toString()+"-----------");
                musicInfos.add(mi);
            }
            // 移动到下一个
            cursor.moveToNext();
        }

    }

    @Override
    public int getCount() {
        return musicInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return musicInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.music_list_item,null);
            holder = new ViewHolder();
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
            holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        holder.tv1.setText(musicInfos.get(position).getMusicName());
        holder.tv2.setText(toTime(musicInfos.get(position).getMusicTime()));
        holder.tv3.setText(musicInfos.get(position).getMusicAlbum());
        holder.tv4.setText(toMB(musicInfos.get(position).getMusicSize()) + "MB");

        return convertView;
    }

    /**
     * 时间转化处理
     */
    public static String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format(" %02d:%02d ", minute, second);
    }

    /**
     * 文件大小转换，将B转换为MB
     */
    public String toMB(int size) {
        float a = (float) size / (float) (1024 * 1024);
        String b = Float.toString(a);
        int c = b.indexOf(".");
        String fileSize = "";
        fileSize += b.substring(0, c + 2);
        return fileSize;
    }


    class ViewHolder {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;

    }
}
