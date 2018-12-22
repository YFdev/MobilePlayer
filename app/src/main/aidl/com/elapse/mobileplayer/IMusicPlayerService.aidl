// IMusicPlayerService.aidl
package com.elapse.mobileplayer;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
        // 根据位置打开对应的音乐
        void openAudio(int position);
        // 开始播放
        void start();
        //暂停音乐
        void pause();
        // 停止音乐
        void stop();
        // 得到当前播放进度
        int getCurrentPosition();
        //获取时长
        int getDuration();
        // 获取歌手
        String getArtist();
        // 获取歌曲名
        String getName();
        //获取歌曲路径
        String getAudioPath();
         // 播放下一个
        void next();
        // 播放上一个
        void previous();
        // 设置播放模式
        void setPlayMode(int mode);
        // 得到播放模式
        int getPlayMode();
        //是否在播放
        boolean isPlaying();
        //视频拖动
        void seekTo(int position);
        //获取sessionID
        int getAudioSessionId();
}
