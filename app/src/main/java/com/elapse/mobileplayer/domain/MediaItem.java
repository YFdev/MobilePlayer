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

    public MediaItem() {
    }

    private MediaItem(Parcel source) {
         name = source.readString();
         size = source.readLong();
         duration = source.readLong();

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

    private String data;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(size);
        dest.writeLong(duration);
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
