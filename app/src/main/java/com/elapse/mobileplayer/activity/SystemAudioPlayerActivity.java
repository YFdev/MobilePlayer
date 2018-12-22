package com.elapse.mobileplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elapse.mobileplayer.IMusicPlayerService;
import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.domain.MediaItem;
import com.elapse.mobileplayer.service.MusicPlayerService;
import com.elapse.mobileplayer.util.Constants;
import com.elapse.mobileplayer.util.LyricParser;
import com.elapse.mobileplayer.util.Utils;
import com.elapse.mobileplayer.view.BaseVisualizerView;
import com.elapse.mobileplayer.view.ShowLyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * 音乐播放器-->系统
 * Created by YF_lala on 2018/12/16.
 */

public class SystemAudioPlayerActivity extends Activity implements View.OnClickListener{

    //进度更新
    private static final int GET_DURATION = 1;
    //显示歌词
    private static final int SHOW_LYRIC = 2;
    //播放器页面帧动画
//    private ImageView img_anim;
    //音乐频谱
    private BaseVisualizerView mBaseVisualizerView;
    private Visualizer mVisualizer;
//    private MediaPlayer mMediaPlayer;
    //点击播放位置
    private int position;
    //aidl
    private IMusicPlayerService mService;
    //歌曲名
    private TextView tv_music_name;
    //歌唱者
    private TextView tv_artist;
    //播放时长/总时长
    private TextView tv_music_duration;
    private String duration;
    //播放进度
    private SeekBar sk_music;
    //控制栏布局
    private LinearLayout ll_controller;
    //歌词控件
    private ShowLyricView mShowLyricView;
    //循环模式
    private Button btn_play_mode;
    private Button btn_previous;//上一曲
    private Button btn_pauseAndStart;//启停
    private Button btn_next;//下一曲
    private Button btn_lyric;//显示歌词
    private Utils mUtils;
    //注册广播，用于更新歌曲名以及演唱者
//    private onPrepareBroadcastReceiver receiver;
    /**
     * true : 从状态栏进入的
     * false : 不是从状态栏进入的
     */
    private boolean isFromNotification;

    //conn
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 绑定成功回调
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMusicPlayerService.Stub.asInterface(service);
            if (mService != null){
                try {
                    if (!isFromNotification){
                        //不是从状态栏启动的
                        mService.openAudio(position);
                    }else {
                        //从状态栏启动的,如果不调用Activity的UI不会更新，必须在主线程执行
                        showViewData();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 异常断开回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (mService != null){
                    mService.stop();
                    mService = null;
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case GET_DURATION:
                    //得到当前进度
                    try {
                        int currentPosition = mService.getCurrentPosition();
                        //设置seekBar和时间进度
                        sk_music.setProgress(currentPosition);
                        tv_music_duration.setText(mUtils.timeToString(currentPosition)+"/"+duration);
                        Log.d("utils", "handleMessage: "+mUtils.timeToString(currentPosition)+"/"+duration);
                        //每秒更新一次
                        mHandler.removeMessages(GET_DURATION);
                        mHandler.sendEmptyMessageDelayed(GET_DURATION,1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case SHOW_LYRIC:
                    try {
                        //得到当前进度
                        int cur_position = mService.getCurrentPosition();
                        //把进度传到showLyricView控件，计算该高亮的歌词
                        mShowLyricView.setCurrentIndex(cur_position);
                        //实时发消息
                        mHandler.removeMessages(SHOW_LYRIC);
                        mHandler.sendEmptyMessage(SHOW_LYRIC);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
            }
            return true;
        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_audio_player_with_controller);
        initData();
        initView();
        getData();
    }

    private void initData() {
        //注册广播
//        receiver = new onPrepareBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("com.elapse.mobileplayer_GET_INFO");
//        registerReceiver(receiver,filter);
        mUtils = new Utils();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        //启动帧动画
//        img_anim = findViewById(R.id.img_bg_music_player);
//        img_anim.setImageResource(R.drawable.rock);
//        AnimationDrawable drawable = (AnimationDrawable) img_anim.getDrawable();
//        drawable.start();
        //音乐频谱
        mBaseVisualizerView = findViewById(R.id.visualizerview);
        //歌曲名
        tv_music_name = findViewById(R.id.tv_music_name);
        //歌唱者
        tv_artist = findViewById(R.id.tv_artist);
        //播放时长/总时长
        tv_music_duration = findViewById(R.id.tv_music_duration);
        //播放进度
        sk_music = findViewById(R.id.sk_music);
        //控制栏布局
        ll_controller = findViewById(R.id.ll_controller);
        //循环模式
        btn_play_mode = findViewById(R.id.btn_audio_play_mode);
        btn_previous = findViewById(R.id.btn_previous);//上一曲
        btn_pauseAndStart = findViewById(R.id.btn_pause);//启停
        btn_next = findViewById(R.id.btn_next);//下一曲
        btn_lyric = findViewById(R.id.btn_show_lyric);//显示歌词
        mShowLyricView = findViewById(R.id.show_lyric_view);//歌词显示控件
        //设置视频拖动
        sk_music.setOnSeekBarChangeListener(new MusicSeekBarChangeListener());
        btn_lyric.setOnClickListener(this);
        btn_play_mode.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        btn_pauseAndStart.setOnClickListener(this);
        btn_next.setOnClickListener(this);

    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.elapse.mobileplayer_OPENAUDIO");
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
        startService(intent);//不会多次调用startService（）
    }

    public void getData() {

        isFromNotification = getIntent().getBooleanExtra("Notification",false);
        if (! isFromNotification){
            //得到播放位置
            position = getIntent().getIntExtra("position",0);
        }
        bindAndStartService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_audio_play_mode://播放模式
                setPlayMode();
                break;
            case R.id.btn_previous://上一曲
                try {
                    mService.previous();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_pause:{//播放和暂停
                if (mService != null){
                    try {
                        //设置歌曲名和歌唱者
//                        tv_music_name.setText(mService.getName());
//                        tv_artist.setText(mService.getArtist());
                        if (mService.isPlaying()){
                            //暂停
                            mService.pause();
//                            //停止seekbar和走时
//                            mHandler.removeMessages(GET_DURATION);
                            //按钮设置为播放图标
                            btn_pauseAndStart.setBackgroundResource(R.drawable.btn_play_selector);
                        }else {
                            //播放
                            mService.start();
//                            //开始seekbar和走时
//                            mHandler.sendEmptyMessage(GET_DURATION);
                            //按钮设置为暂停图标
                            btn_pauseAndStart.setBackgroundResource(R.drawable.btn_pause_selector);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
                break;
            case R.id.btn_next://下一曲
                try {
                    mService.next();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_show_lyric://显示歌词

                break;
        }
    }

    private void setPlayMode() {
        try {
            if (mService.getPlayMode() == Constants.PLAY_MODE_ORDERED){
                mService.setPlayMode(Constants.PLAY_MODE_RANDOM);
            }else if (mService.getPlayMode() == Constants.PLAY_MODE_RANDOM){
                mService.setPlayMode(Constants.PLAY_MODE_CYCLE);
            }else {
                mService.setPlayMode(Constants.PLAY_MODE_ORDERED);
            }
            //设置图片以及toast
            showPlayMode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //设置不同模式的btn样式
    private void showPlayMode() {
        try {
            int mode = mService.getPlayMode();
            if (mode == Constants.PLAY_MODE_ORDERED){
                btn_play_mode.setBackgroundResource(R.drawable.btn_play_mode_selector);
                Toast.makeText(this,"顺序播放",Toast.LENGTH_SHORT).show();
            }else if (mode == Constants.PLAY_MODE_RANDOM){
                btn_play_mode.setBackgroundResource(R.drawable.btn_play_mode_random_selector);
                Toast.makeText(this,"随机播放",Toast.LENGTH_SHORT).show();
            }else if (mode == Constants.PLAY_MODE_CYCLE){
                btn_play_mode.setBackgroundResource(R.drawable.btn_play_mode_cycle_selector);
                Toast.makeText(this,"单曲循环",Toast.LENGTH_SHORT).show();
            }else {
                btn_play_mode.setBackgroundResource(R.drawable.btn_play_mode_selector);
                Toast.makeText(this,"顺序播放",Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    //校验播放状态
    private void checkPlayMode() {
        try {
            int mode = mService.getPlayMode();
            if (mode == Constants.PLAY_MODE_ORDERED){
                btn_play_mode.setBackgroundResource(R.drawable.btn_play_mode_selector);
            }else if (mode == Constants.PLAY_MODE_RANDOM){
                btn_play_mode.setBackgroundResource(R.drawable.btn_play_mode_random_selector);
            }else if (mode == Constants.PLAY_MODE_CYCLE){
                btn_play_mode.setBackgroundResource(R.drawable.btn_play_mode_cycle_selector);
            }else {
                btn_play_mode.setBackgroundResource(R.drawable.btn_play_mode_selector);
            }

            //校验播放和暂停
            if (mService.isPlaying()){
                btn_pauseAndStart.setBackgroundResource(R.drawable.btn_pause_selector);
            }else {
                btn_pauseAndStart.setBackgroundResource(R.drawable.btn_play_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //拖动监听
    private class MusicSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){//重要，又忘了
                //拖动进度
                try {
                    mService.seekTo(progress);
                    sk_music.setProgress(progress);
                    tv_music_duration.setText(mUtils.timeToString(progress)+"/"+duration);
                    Log.d("utils", "onProgressChanged: "+mUtils.timeToString(progress)+"/"+duration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //开始拖动
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeMessages(GET_DURATION);

        }
        //结束拖动
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.sendEmptyMessageDelayed(GET_DURATION,1000);

        }
    }
    /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizerFxAndUi()
    {
        try {
            int audioSessionid = mService.getAudioSessionId();
            mVisualizer = new Visualizer(audioSessionid);
            // 参数内必须是2的位数
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            // 设置允许波形表示，并且捕获它
            mBaseVisualizerView.setVisualizer(mVisualizer);
            mVisualizer.setEnabled(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mVisualizer != null)
            mVisualizer.release();
    }

    @Override
    protected void onDestroy() {
//        if (receiver != null){
//            unregisterReceiver(receiver);
//            receiver = null;
//        }
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);

        //解绑服务
        if (conn != null){
            unbindService(conn);
            conn = null;
        }
        super.onDestroy();
    }

    /**
     * 音乐播放器准备好后广播
     * 用EventBus替代
     */
//    private class onPrepareBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            showViewData();
//            checkPlayMode();
//        }
//    }

    //EventBus订阅方法
    @Subscribe(threadMode =  ThreadMode.MAIN ,sticky = false ,priority = 0)
    public void  showData(MediaItem mediaItem){
        //发消息，歌词同步
        showLyric();
        showViewData();
        checkPlayMode();
        setupVisualizerFxAndUi();
    }

    private void showLyric() {
        //用于解析歌词
        LyricParser parser = new LyricParser();
        try {
            String path = mService.getAudioPath();
            //传入file
            //mnt/sdcard/audio/beijing.mp3
            //mnt/sdcard/audio/beijing.lrc
            path = path.substring(0,path.lastIndexOf("."));
            File file = new File(path + ".lrc");
            if (!file.exists()){
                file = new File(path + ".txt");
            }
            if (!file.exists()){
                Toast.makeText(this,"没有发现歌词文件",Toast.LENGTH_SHORT).show();
            }
            parser.readFile(file);//解析歌词
            mShowLyricView.setLyrics(parser.getLyrics());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (parser.isExist()){
            mHandler.sendEmptyMessage(SHOW_LYRIC);
        }
    }

    private void showViewData() {
        try {
            tv_music_name.setText(mService.getName());
            tv_artist.setText(mService.getArtist());
            sk_music.setMax(mService.getDuration());
            duration = mUtils.timeToString(mService.getDuration());
            mHandler.sendEmptyMessage(GET_DURATION);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
