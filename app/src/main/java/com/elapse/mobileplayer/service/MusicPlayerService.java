package com.elapse.mobileplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureUtils;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.elapse.mobileplayer.IMusicPlayerService;
import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.activity.SystemAudioPlayerActivity;
import com.elapse.mobileplayer.domain.MediaItem;
import com.elapse.mobileplayer.util.CacheUtils;
import com.elapse.mobileplayer.util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MusicPlayerService extends Service {

    private static final String GET_INFO = "com.elapse.mobileplayer_GET_INFO";
    private ArrayList<MediaItem> mMediaItems;
    private int position;
    private MediaItem mediaItem;
    //用于播放音乐
    private MediaPlayer mMediaPlayer;
    private int mPlayMode;
    private NotificationManager manager;

    public MusicPlayerService() {
    }


    private IMusicPlayerService.Stub mStub = new IMusicPlayerService.Stub() {
        MusicPlayerService service = MusicPlayerService.this;
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
        }


    };

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayMode = CacheUtils.getInt(this,"mode");
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
//                mMediaPlayer.release();
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

                if (mPlayMode == Constants.PLAY_MODE_CYCLE){
                    //不会触发播放完成
                    mMediaPlayer.setLooping(true);
                }else {
                    mMediaPlayer.setLooping(false);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(this,"还没有数据",Toast.LENGTH_SHORT).show();
        }
    }

    class mpOnPrepareListener implements MediaPlayer.OnPreparedListener{

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onPrepared(MediaPlayer mp) {
            //通知Activity获取信息
//            notifyChange(GET_INFO);
            EventBus.getDefault().post(mediaItem);
            start();
        }
    }

    /**
     * 发送准备完成的广播,用EventBus代替
     * @param action
     */
//    private void notifyChange(String action) {
//        Intent intent = new Intent(action);
//        sendBroadcast(intent);
//    }

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
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void start(){
        mMediaPlayer.start();
        //前台通知,点击后进入播放器
        //注意***从前台启动activity会生成多个activity实例，因此activity必须使用SingleTask模式
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, SystemAudioPlayerActivity.class);
        intent.putExtra("Notification",true);//标识：来自状态栏
        PendingIntent pi = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.icon_music)
                .setContentText("橙子音乐")
                .setContentText("正在播放..."+getName())
                .setContentIntent(pi)
                .build();
        manager.notify(1,notification);
    }

    /**
     * 暂停音乐
     */
    private void pause(){
        mMediaPlayer.pause();
        //取消前台通知
        manager.cancel(1);
    }

    /**
     * 停止音乐
     */
    private void stop(){
        mMediaPlayer.stop();
    }

    /**
     * 得到当前播放进度
     * @return 当前进度
     */
    private int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * 获取时长
     * @return 时长
     */
    private int getDuration(){
        return mMediaPlayer.getDuration();
    }

    /**
     * 获取歌手
     * @return 歌手
     */
    private String getArtist(){
        return mediaItem.getArtist();
    }

    /**
     * 获取歌曲名
     * @return 歌曲名
     */
    private String getName(){
        return mediaItem.getName();
    }

    /**
     * 获取歌曲路径
     * @return 歌曲路径
     */
    private String getAudioPath(){
        return mediaItem.getData();
    }

    /**
     * 播放下一个
     */
    private void next(){
        //1、根据当前播放模式设置下一个位置
        setNextPosition();
        //2、根据当前播放模式和位置播放音乐
        openNextAudio();
    }

    private void openNextAudio() {
//        int playMode = getPlayMode();
//        if (playMode == Constants.PLAY_MODE_CYCLE){
//            openAudio(position);
//        }else if (playMode == Constants.PLAY_MODE_ORDERED){
//            if (position < mMediaItems.size()){
//                openAudio(position);
//            }else {
//                position = mMediaItems.size() - 1;
//            }
//        }else if (playMode == Constants.PLAY_MODE_RANDOM){
//           openAudio(position);
//        }
        openAudio(position);
    }

    private void setNextPosition() {
        int playMode = getPlayMode();
        if (playMode == Constants.PLAY_MODE_CYCLE){
            position ++;
            if (position >= mMediaItems.size()){
                position = 0;
            }
        }else if (playMode == Constants.PLAY_MODE_ORDERED){
            position ++;
            if (position >= mMediaItems.size()){
                position = 0;
            }
        }else if (playMode == Constants.PLAY_MODE_RANDOM){
            position = new Random().nextInt(mMediaItems.size() + 1);
        }
    }

    /**
     * 播放上一个
     */
    private void previous(){
        //根据当前播放模式，设置上一个位置
        setPrePosition();
        //根据当前播放模式和位置播放音频
        openPreAudio();
    }

    private void openPreAudio() {
//        int playMode = getPlayMode();
//        if (playMode == Constants.PLAY_MODE_ORDERED){
//            if (position >= 0){
//                openAudio(position);
//            }else {
//                position = 0 ;
//            }
//        }else if (playMode == Constants.PLAY_MODE_CYCLE){
//            openAudio(position);
//        }else if (playMode == Constants.PLAY_MODE_RANDOM){
//            openAudio(position);
//        }
        openAudio(position);
    }

    private void setPrePosition() {
        int playMode = getPlayMode();
        if (playMode == Constants.PLAY_MODE_CYCLE){
            position -- ;
            if (position < 0){
                position = mMediaItems.size() - 1;
            }
        }else if (playMode == Constants.PLAY_MODE_ORDERED){
            position -- ;
            if (position < 0){
                position = mMediaItems.size() - 1;
            }
        }else if (playMode == Constants.PLAY_MODE_RANDOM){
            position = new Random().nextInt(mMediaItems.size() + 1);
        }
    }

    /**
     * 设置播放模式
     * @param mode 播放模式
     */
    private void setPlayMode(int mode){
        mPlayMode = mode;
        CacheUtils.putInt(this,"mode",mPlayMode);
        //播放模式设置
        if (mPlayMode == Constants.PLAY_MODE_CYCLE){
            //不会触发播放完成
            mMediaPlayer.setLooping(true);
        }else {
            mMediaPlayer.setLooping(false);
        }
    }

    /**
     * 得到播放模式
     * @return 播放模式
     */
    private int getPlayMode(){
        return mPlayMode;
    }

    /**
     *
     */
    private boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    //拖动
    private void seekTo(int position){
        mMediaPlayer.seekTo(position);
    }

// 不使用aidl的实现方式
// private MyBinder mBinder = new MyBinder();
//
//    class MyBinder extends Binder{
//        private void start(){
//            mMediaPlayer.start();
//            //前台通知,点击后进入播放器
//            //注意***从前台启动activity会生成多个activity实例，因此activity必须使用SingleTask模式
//            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            Intent intent = new Intent(MusicPlayerService.this, SystemAudioPlayerActivity.class);
//            intent.putExtra("Notification",true);//标识：来自状态栏
//            PendingIntent pi = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//            Notification notification = new Notification.Builder(this)
//                    .setSmallIcon(R.drawable.icon_music)
//                    .setContentText("橙子音乐")
//                    .setContentText("正在播放..."+getName())
//                    .setContentIntent(pi)
//                    .build();
//            manager.notify(1,notification);
//        }
//
//        /**
//         * 暂停音乐
//         */
//        private void pause(){
//            mMediaPlayer.pause();
//            //取消前台通知
//            manager.cancel(1);
//        }
//
//        /**
//         * 停止音乐
//         */
//        private void stop(){
//            mMediaPlayer.stop();
//        }
//
//        /**
//         * 得到当前播放进度
//         * @return 当前进度
//         */
//        private int getCurrentPosition(){
//            return mMediaPlayer.getCurrentPosition();
//        }
//
//        /**
//         * 获取时长
//         * @return 时长
//         */
//        private int getDuration(){
//            return mMediaPlayer.getDuration();
//        }
//
//        /**
//         * 获取歌手
//         * @return 歌手
//         */
//        private String getArtist(){
//            return mediaItem.getArtist();
//        }
//
//        /**
//         * 获取歌曲名
//         * @return 歌曲名
//         */
//        private String getName(){
//            return mediaItem.getName();
//        }
//
//        /**
//         * 获取歌曲路径
//         * @return 歌曲路径
//         */
//        private String getAudioPath(){
//            return mediaItem.getData();
//        }
//
//        /**
//         * 播放下一个
//         */
//        private void next(){
//
//        }
//
//        /**
//         * 播放上一个
//         */
//        private void previous(){
//
//        }
//
//        /**
//         * 设置播放模式
//         * @param mode 播放模式
//         */
//        private void setPlayMode(int mode){
//            mPlayMode = mode;
//            CacheUtils.putInt(this,"mode",mPlayMode);
//        }
//
//        /**
//         * 得到播放模式
//         * @return 播放模式
//         */
//        private int getPlayMode(){
//            return mPlayMode;
//        }
//
//        /**
//         *
//         */
//        private boolean isPlaying(){
//            return mMediaPlayer.isPlaying();
//        }
//
//        //拖动
//        private void seekTo(int position){
//            mMediaPlayer.seekTo(position);
//        }
//    }
}

