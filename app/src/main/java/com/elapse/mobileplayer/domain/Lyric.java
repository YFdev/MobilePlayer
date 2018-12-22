package com.elapse.mobileplayer.domain;

/**
 * Created by YF_lala on 2018/12/20.
 */

public class Lyric {
    //歌词格式[00:04:05]北京 北京
    private String content;//歌词内容
    private long timeStamp;//时间戳
    private long sleepTime;//高亮时间

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "content='" + content + '\'' +
                ", timeStamp=" + timeStamp +
                ", sleepTime=" + sleepTime +
                '}';
    }
}
