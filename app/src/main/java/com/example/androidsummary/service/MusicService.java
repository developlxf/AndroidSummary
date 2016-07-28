package com.example.androidsummary.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.androidsummary.MediaActivity;
import com.example.androidsummary.R;
import com.example.androidsummary.adapter.MusicListAdapter;

/**
 * 类说明：音乐播放服务类
 *
 * @author LiangAn
 * @version 创建时间：2015年3月18日 上午10:24:17
 */
public class MusicService extends Service {

    public static MediaPlayer myMediaPlayer;

    public static int playing_id = 0;

    private boolean isFirst = true;

    Handler handler = new Handler();
    private String playFlag;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myMediaPlayer != null) {
            myMediaPlayer.stop();
            myMediaPlayer.release();
            myMediaPlayer = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playFlag = intent.getExtras().getString("control");

        if (playFlag.equals("click")){
            //初始化要播放的资源
            initMediaSource(initMusicUri(playing_id));
        }

        if (MusicListAdapter.musicInfos != null) {
            if ("play".equals(playFlag) || "click".equals(playFlag)) {
                if (isFirst){
                    isFirst = false;
                    initMediaSource(initMusicUri(playing_id));
                }
                playMusic();
            } else if ("next".equals(playFlag)) {
                playNext();
            } else if ("front".equals(playFlag)) {
                playFront();
            } else if ("listClick".equals(playFlag)) {
                playing_id = intent.getExtras().getInt("musicId_1");
                initMediaSource(initMusicUri(playing_id));
                playMusic();
            } else if ("gridClick".equals(playFlag)) {
                playing_id = intent.getExtras().getInt("musicId_2");
                initMediaSource(initMusicUri(playing_id));
                playMusic();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 初始化媒体对象
     *
     * @param mp3Path mp3路径
     */
    public void initMediaSource(String mp3Path) {
        Uri mp3Uri = Uri.parse(mp3Path);
        if (myMediaPlayer != null) {
            myMediaPlayer.stop();
            myMediaPlayer.reset();
            myMediaPlayer = null;
        }
        // 为myMediaPlayer创建对象
        myMediaPlayer = MediaPlayer.create(this, mp3Uri);
    }

    /**
     * 返回列表第几行的歌曲路径
     *
     * @param _id 表示歌曲序号，从0开始
     */
    public static String initMusicUri(int _id) {
        playing_id = _id;
        String s;
        // 判断列表和列表长度不为空时才获取
        if (MusicListAdapter.musicInfos != null
                && MusicListAdapter.musicInfos.size() != 0) {
            s = MusicListAdapter.musicInfos.get(_id).getMusicPath();
            return s;
        } else {
            // 否则返回空字符串
            return "";
        }
    }

    /**
     * 音乐播放方法，并且带有暂停方法
     */
    private void playMusic() {

        // 判断歌曲不能为空
        if (myMediaPlayer != null && MusicListAdapter.musicInfos.size() != 0) {

            if (myMediaPlayer.isPlaying()) {//在播放  则暂停
                Log.i("music", "playing.........");

                myMediaPlayer.pause();
                // 更换播放按钮背景
                MediaActivity.play_music
                        .setBackgroundResource(R.drawable.bar_play);
            } else {//没播放  则开始播放
                try {
                    myMediaPlayer.start();
                    // 更换按钮背景-
                    MediaActivity.play_music
                            .setBackgroundResource(R.drawable.bar_pause);
                } catch (Exception e) {

                    e.printStackTrace();
                }


                // 启动线程更新SeekBar
                startSeekBarUpdate();

                // 更新歌曲播放第几首
                int x = playing_id + 1;
                MediaActivity.music_number.setText("" + x + "/"
                        + MusicListAdapter.musicInfos.size());

                String a = MusicListAdapter.musicInfos.get(playing_id)
                        .getMusicName();
                // 切换带动画更新歌曲名
                MediaActivity.music_Name.setText(a);
                MediaActivity.music_Name.setAnimation(AnimationUtils
                        .loadAnimation(MusicService.this, R.anim.translate_z));

                // 带动画更新专辑名
                MediaActivity.music_Album
                        .setText(MusicListAdapter.musicInfos.get(playing_id)
                                .getMusicAlbum());
                MediaActivity.music_Album.setAnimation(AnimationUtils
                        .loadAnimation(MusicService.this, R.anim.alpha_y));

                // 更新歌曲时间
                MediaActivity.time_right.setText(MusicListAdapter
                        .toTime(MusicListAdapter.musicInfos.get(playing_id)
                                .getMusicTime()));

            }

            /**
             * 监听播放是否完成
             */
            myMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    // 播放完当前歌曲，自动播放下一首
                    if (MusicListAdapter.musicInfos != null)
                        playNext();
                }
            });

        } else {
            Toast.makeText(MusicService.this, "木有在手机里找到歌曲啊...",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放下一首
     */
    private void playNext() {

        // 判断歌曲不能为空
        if (MusicListAdapter.musicInfos.size() != 0) {
            // 如果到了最后一首则一直播放最后一首
            if (playing_id == MusicListAdapter.musicInfos.size() - 1) {
                playing_id = MusicListAdapter.musicInfos.size() - 1;
                Toast.makeText(MusicService.this, "已经是最后一首啦！",
                        Toast.LENGTH_SHORT).show();

                MediaActivity.play_music
                        .setBackgroundResource(R.drawable.bar_pause);

            } else {
                initMediaSource(initMusicUri(++playing_id));
                playMusic();
            }
        } else {
            Toast.makeText(MusicService.this, "木有找到歌曲啊！", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 播放上一首
     */
    private void playFront() {

        // 判断歌曲不能为空
        if (MusicListAdapter.musicInfos.size() != 0) {
            // 如果到了第一首则一直播放第一首
            if (playing_id == 0) {
                playing_id = 0;
                Toast.makeText(MusicService.this, "现在就是第一首哦！",
                        Toast.LENGTH_SHORT).show();
            } else {
                initMediaSource(initMusicUri(--playing_id));
                playMusic();
            }
        } else {
            Toast.makeText(MusicService.this, "木有找到歌曲啊！", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void startSeekBarUpdate() {
        handler.post(updatesb);
    }


    Runnable updatesb = new Runnable() {

        @Override
        public void run() {
            // 获取SeekBar走动到那的时间
            MediaActivity.play_time = myMediaPlayer
                    .getCurrentPosition();

            // 设置填充当前获取的进度
            MediaActivity.seekbar
                    .setProgress(MediaActivity.play_time);
            // SeekBar的最大值填充歌曲时间
            MediaActivity.seekbar.setMax(MusicListAdapter.musicInfos
                    .get(playing_id).getMusicTime());

            // 线程延迟1000毫秒启动
            handler.postDelayed(updatesb, 1000);
        }
    };

}
