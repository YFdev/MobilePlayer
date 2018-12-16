package com.elapse.mobileplayer.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.Toast;

import com.elapse.mobileplayer.IMusicPlayerService;
import com.elapse.mobileplayer.domain.MediaItem;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {

    private ArrayList<MediaItem> mMediaItems;
    private int position;
    private MediaItem mediaItem;
    //用于播放音乐
    private MediaPlayer mMediaPlayer;

    public MusicPlayerService() {
    }


    private IMusicPlayerService.Stub mStub = new IMusicPlayerService.Stub() {
        MusicPlayerService service = MusicPlayerService.this;
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void previous() throws RemoteException {
            service.previous();
        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            service.setPlayMode(mode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        //加载音乐列表
        getDataFromLocal();
    }

    private void getDataFromLocal() {
        mMediaItems = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objects = {MediaStore.Audio.Media.DISPLAY_NAME,//name in SDCard
                        MediaStore.Audio.Media.DURATION,//the duration of video
                        MediaStore.Audio.Media.SIZE,//
                        MediaStore.Audio.Media.DATA};// the abs path of video
                Cursor cursor = contentResolver.query(uri, objects, null, null, null);
                if (cursor != null){
                    while (cursor.moveToNext()){
                        MediaItem mediaItem = new MediaItem();
                        String name = cursor.getString(0);
                        long duration = cursor.getLong(1);
                        long size = cursor.getLong(2);
                        String data = cursor.getString(3);
                        mediaItem.setName(name);
                        mediaItem.setData(data);
                        mediaItem.setDuration(duration);
                        mediaItem.setSize(size);
                        mMediaItems.add(mediaItem);
                    }
                    cursor.close();
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    /**
     * 根据位置打开对应的音乐，并且播放
     * @param position
     */
    private void openAudio(int position){
        this.position = position;
        if (mMediaItems != null && mMediaItems.size() > 0){
            mediaItem = mMediaItems.get(position);//当前播放的音频文件对象
            if (mMediaPlayer != null){
                mMediaPlayer.release();
                mMediaPlayer.reset();
            }
            try {
                mMediaPlayer = new MediaPlayer();
                //设置监听
                mMediaPlayer.setOnPreparedListener(new mpOnPrepareListener());
                mMediaPlayer.setOnCompletionListener(new mpOnCompletionListener());
                mMediaPlayer.setOnErrorListener(new mpOnErrorListener());
                mMediaPlayer.setDataSource(mediaItem.getData());
                mMediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(this,"还没有数据",Toast.LENGTH_SHORT).show();
        }
    }

    class mpOnPrepareListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            start();
        }
    }

    /**
     * 监听播放完成
     */
    class mpOnCompletionListener implements MediaPlayer.OnCompletionListener{
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    /**
     * 监听播放出错
     */
    class mpOnErrorListener implements MediaPlayer.OnErrorListener{
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }
    /**
     * 开始播放
     */
    private void start(){
        mMediaPlayer.start();
    }

    /**
     * 暂停音乐
     */
    private void pause(){

    }

    /**
     * 停止音乐
     */
    private void stop(){
        mMediaPlayer.stop();
    }

    /**
     * 得到当前播放进度
     * @return
     */
    private int getCurrentPosition(){

        return 0;
    }

    /**
     * 获取时长
     * @return
     */
    private int getDuration(){

        return 0;
    }

    /**
     * 获取歌手
     * @return
     */
    private String getArtist(){

        return null;
    }

    /**
     * 获取歌曲名
     * @return
     */
    private String getName(){

        return null;
    }

    /**
     * 获取歌曲路径
     * @return
     */
    private String getAudioPath(){

        return null;
    }

    /**
     * 播放下一个
     */
    private void next(){

    }

    /**
     * 播放上一个
     */
    private void previous(){

    }

    /**
     * 设置播放模式
     * @param mode
     */
    private void setPlayMode(int mode){

    }

    /**
     * 得到播放模式
     * @return
     */
    private int getPlayMode(){

        return 0;
    }
}

