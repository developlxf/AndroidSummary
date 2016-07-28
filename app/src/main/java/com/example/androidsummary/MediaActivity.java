package com.example.androidsummary;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidsummary.adapter.MusicListAdapter;
import com.example.androidsummary.service.MusicService;

/**
 *
 */
public class MediaActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private ImageButton left_music;//上一曲
    private ImageButton right_music;//下一曲
    public static ImageButton play_music;//播放按钮

    private ListView musicListView;//歌曲列表
    private MediaPlayer mp;

    //各种控件
    public static SeekBar seekbar;
    public static TextView time_left;
    public static TextView time_right;
    public static TextView music_Name;
    public static TextView music_Album;
    public static TextView music_number;
    private MusicListAdapter adapter_music;

    // 为歌曲时间和播放时间定义静态变量
    public static int play_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        initViews();

        showMisicList();

        if (MusicListAdapter.musicInfos != null)
            updataUI();
    }

    private void updataUI() {
        // 判断歌曲不能为空并且后缀为.mp3
        if (MusicListAdapter.musicInfos.size() > 0
                ) {
            // 显示获取的歌曲时间
            time_right.setText(MusicListAdapter
                    .toTime(MusicListAdapter.musicInfos.get(
                            MusicService.playing_id).getMusicTime()));
            // 截取.mp3字符串
            String a = MusicListAdapter.musicInfos.get(MusicService.playing_id)
                    .getMusicName();
//            int b = a.indexOf(".mp3");
//            String c = a.substring(0, b);
            // 显示获取的歌曲名
            music_Name.setText(a);
            music_Name.setAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.translate_z));

            // 显示播放当前第几首和歌曲总数
            int x = MusicService.playing_id + 1;
//            music_number.setText("" + x + "/"+ MusicListAdapter.musicInfos.size());
        } else {
            System.out.println("请点击列表播放歌曲！*********");
        }

    }

    private void initViews() {
        time_left = (TextView) findViewById(R.id.time_tv1);
        time_right = (TextView) findViewById(R.id.time_tv2);
        music_Name = (TextView) findViewById(R.id.music_name);
        music_Album = (TextView) findViewById(R.id.music_album);
        music_number = (TextView) findViewById(R.id.music_number);
        musicListView = (ListView) findViewById(R.id.media_listview);

        //监听上一首
        left_music = (ImageButton) findViewById(R.id.ib1);
        left_music.setOnClickListener(this);

        //监听下一首
        right_music = (ImageButton) findViewById(R.id.ib3);
        right_music.setOnClickListener(this);

        //播放按钮
        play_music = (ImageButton) findViewById(R.id.ib2);
        play_music.setOnClickListener(this);

        //进度条的拖动
        seekbar = (SeekBar) findViewById(R.id.player_seekbar);
        seekbar.setOnSeekBarChangeListener(this);
    }

    /**
     * 通过内容提供者  查询手机内的所有MP3
     */
    private void showMisicList() {
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE,// 标题，游标从0读取
                        MediaStore.Audio.Media.DURATION,// 持续时间,1
                        MediaStore.Audio.Media.ARTIST,// 艺术家,2
                        MediaStore.Audio.Media._ID,// id,3
                        MediaStore.Audio.Media.DISPLAY_NAME,// 显示名称,4
                        MediaStore.Audio.Media.DATA,// 数据，5
                        MediaStore.Audio.Media.ALBUM_ID,// 专辑名称ID,6
                        MediaStore.Audio.Media.ALBUM,// 专辑,7
                        MediaStore.Audio.Media.SIZE},
                null, null, MediaStore.Audio.Media.ARTIST);// 选择条件  排序

        Log.i("music", cursor.getCount() + "");

        if (cursor != null && cursor.getCount() == 0) {
            Toast.makeText(this, "你的手机未找到音乐,请添加音乐", Toast.LENGTH_LONG)
                    .show();
            return;
        }


        adapter_music = new MusicListAdapter(cursor, MediaActivity.this);
        musicListView.setAdapter(adapter_music);

        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MusicService.myMediaPlayer != null) {
                    Intent play_center = new Intent(MediaActivity.this, MusicService.class);
                    play_center.putExtra("control", "click");
                    MusicService.playing_id = position;
                    startService(play_center);
                } else {
                    Intent play_center = new Intent(MediaActivity.this, MusicService.class);
                    play_center.putExtra("control", "click");
                    MusicService.playing_id = position;
                    startService(play_center);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib1://上一曲
                Intent play_left = new Intent(this, MusicService.class);
                play_left.putExtra("control", "front");
                startService(play_left);
                break;
            case R.id.ib2://播放
                Intent play_center = new Intent(this, MusicService.class);
                play_center.putExtra("control", "play");
                startService(play_center);
                break;
            case R.id.ib3://下一曲
                Intent play_right = new Intent(this, MusicService.class);
                play_right.putExtra("control", "next");
                startService(play_right);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // 判断用户是否触拖SeekBar并且不为空才执行
        if (fromUser && MusicService.myMediaPlayer != null) {
            MusicService.myMediaPlayer.seekTo(progress);
        }
        time_left.setText(MusicListAdapter.toTime(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
