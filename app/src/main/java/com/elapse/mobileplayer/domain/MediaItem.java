package com.elapse.mobileplayer.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YF_lala on 2018/12/8.
 * represent a video or audio
 */

public class MediaItem implements Parcelable/*Serializable*/{

    private String name;
    private long size;
    private long duration;
    private String data;
    private  String artist;
    private String imgUrl;
//    private String videoUrl;
//    private String highQUrl;
    private String desc;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

//    public String getVideoUrl() {
//        return videoUrl;
//    }
//
//    public void setVideoUrl(String videoUrl) {
//        this.videoUrl = videoUrl;
//    }
//
//    public String getHighQUrl() {
//        return highQUrl;
//    }
//
//    public void setHighQUrl(String highQUrl) {
//        this.highQUrl = highQUrl;
//    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public MediaItem() {
    }

    private MediaItem(Parcel source) {
         name = source.readString();
         size = source.readLong();
         duration = source.readLong();
         data = source.readString();
         artist = source.readString();
         imgUrl = source.readString();
//         videoUrl = source.readString();
//         highQUrl = source.readString();
         desc = source.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(size);
        dest.writeLong(duration);
        dest.writeString(data);
        dest.writeString(artist);
        dest.writeString(imgUrl);
//        dest.writeString(videoUrl);
//        dest.writeString(highQUrl);
        dest.writeString(desc);
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public MediaItem createFromParcel(Parcel source) {
            return new MediaItem(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new MediaItem[size];
        }
    };
}
