package com.example.androidsummary.bean;

/**
 * 1、歌曲名 2、歌手 3、歌曲时间 4、专辑(专辑图片，专辑名称，专辑ID[用来获取图片]) 5、歌曲大小
 * Created by 伦小丹 on 2015/12/23 0023.
 */
public class MusicInfo {
    private int _id;
    private String musicName;
    private String musicSinger;
    private int musicTime;
    private String musicAlbum;
    private int musicSize;
    private String musicPath;

    public int get_id() {
        return _id;
    }

    public String getMusicName() {
        return musicName;
    }

    public String getMusicSinger() {
        return musicSinger;
    }

    public int getMusicTime() {
        return musicTime;
    }

    public String getMusicAlbum() {
        return musicAlbum;
    }

    public int getMusicSize() {
        return musicSize;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public void setMusicSinger(String musicSinger) {
        this.musicSinger = musicSinger;
    }

    public void setMusicTime(int musicTime) {
        this.musicTime = musicTime;
    }

    public void setMusicAlbum(String musicAlbum) {
        this.musicAlbum = musicAlbum;
    }

    public void setMusicSize(int musicSize) {
        this.musicSize = musicSize;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }
}
