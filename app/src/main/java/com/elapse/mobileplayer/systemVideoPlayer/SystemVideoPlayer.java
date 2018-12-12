package com.elapse.mobileplayer.systemVideoPlayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.elapse.mobileplayer.view.CustomVideoView;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.domain.MediaItem;
import com.elapse.mobileplayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by YF_lala on 2018/12/10.
 */

public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    private static final int PROGRESS = 0 ;
    private static final int HIDE_MEDIA_CONTROLLER = 1;
    /**
     * 全屏
     */
    private static final int FULL_SCREEN = 1;
    private static final int DEFAULT_SCREEN = 2;
    private Utils mUtils;
    private CustomVideoView mVideoView;
    private Uri mUri;
    //顶部标题栏
    private LinearLayout ll_top_bar;
    private LinearLayout ll_name_time;//影片名称和当前时间-->布局
    private TextView current_time;//当前时间
    private TextView video_name;//影片名称
    private SeekBar seek_current_volume;//音量
    private Button btn_voice;//音量控制按钮
    private Button btn_info;//info信息
    private ImageView img_battery_state;//电量信息
    //底部控制栏
    private LinearLayout ll_bottom_bar;
    private LinearLayout ll_duration;//-->布局
    private TextView tv_current_duration;//已播放时长
    private SeekBar seek_bar_video;//当前进度
    private TextView tv_duration;//总时长
    //控制按钮栏_布局
    private LinearLayout ll_controller;//-->布局
    //退出、前一个、暂停、后一个、全屏
    private Button btn_video_exit,btn_previous,btn_pause,btn_forward,btn_full_screen;

    private BroadcastReceiver mBatteryChangeReceiver;
    private ArrayList<MediaItem> mMediaItems;
    private int position;
    //手势识别
    private GestureDetector mDetector;
    //是否显示控制面板
    private boolean isControllerShow;

    //发消息更新seekbar进度
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case PROGRESS:
                    //得到当前播放进度
                    int currentPosition = mVideoView.getCurrentPosition();
                    seek_bar_video.setProgress(currentPosition);
                    tv_current_duration.setText(mUtils.timeToString(currentPosition));

                    //更新系统时间
                    current_time.setText(getSystemTime());

                    //每秒更新一次
                    // mHandler.removeMessages(PROGRESS);
                    mHandler.sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
                case HIDE_MEDIA_CONTROLLER:
                    hideMediaController();
                    break;
            }
            return true;
        }
    });
    //设置是否全屏
    private boolean isFullScreen;
    //获取屏幕参数
    private int screenWidth = 0;
    private int screenHeight = 0;
    //视频真实宽高
    private int videoHeight;
    private int videoWidth;

    //实例化AudioManager
    private AudioManager am;
    //当前音量
    private int currentVolume;
    /**
     * 最大音量
     * 0--15
     */

    private int maxVolume;
    private boolean isMute = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_controller);

        initView();
        initData();
        getData();
        setData();
        setListener();
    }

    private void setData() {
        if (mMediaItems != null && mMediaItems.size() >0){
            MediaItem mediaItem = mMediaItems.get(position);
            mVideoView.setVideoPath(mediaItem.getData());
            video_name.setText(mediaItem.getName());
        }else if (mUri != null){
            mVideoView.setVideoURI(mUri);
            video_name.setText(mUri.toString());
        }else {
            Toast.makeText(this,"没有视频",Toast.LENGTH_SHORT).show();
        }
        setButtonState();
    }

    private void getData() {
        mMediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("video_list");
        position = getIntent().getIntExtra("position",0);
        mUri = getIntent().getData();//获取一个地址:文件、浏览器、相册等
    }

    private void initData() {
        mUtils = new Utils();
        //注册广播，监听电量
        mBatteryChangeReceiver = new BatteryChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryChangeReceiver,intentFilter);
        //获取屏幕参数
//        screenWidth = getWindowManager().getDefaultDisplay().getWidth();//过时方法
//        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        //得到音量
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        seek_current_volume.setMax(maxVolume);
        //设置当前音量
        seek_current_volume.setProgress(currentVolume);

        //实例化手势识别器，处理双击、长按、单击事件
        mDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                startAndPauseVideo();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                setFullScreenAndDefaultMode();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isControllerShow){
                    hideMediaController();
                    mHandler.removeMessages(HIDE_MEDIA_CONTROLLER);
                }else {
                    showMediaController();
                    mHandler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,3500);
                }
                return super.onSingleTapConfirmed(e);
            }
        });
    }

    private void setFullScreenAndDefaultMode() {
        if (!isFullScreen){
            //设置默认
            setVideoType(FULL_SCREEN);
        }else {
            //设置全屏
            setVideoType(DEFAULT_SCREEN);
        }
    }

    private void setVideoType(int type) {
        switch (type){
            case FULL_SCREEN:
                //1、设置视频画面大小-->设置与屏幕相同大小
                mVideoView.setVideoSize(screenWidth,screenHeight);
                //2、设置全屏按钮状态
                btn_full_screen.setBackgroundResource(R.drawable.btn_full_screen_pressed);
                isFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                isFullScreen = false;
                //设置视频画面大小
                //视频真实宽高
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;
                //屏幕宽高
                int width = screenWidth;
                int height = screenHeight;
                if (mVideoWidth * height < width * mVideoHeight){
                    width = height * mVideoWidth / mVideoHeight;
                }else if (mVideoWidth * height > width * mVideoHeight){
                    height = width * mVideoHeight / mVideoWidth;
                }
                mVideoView.setVideoSize(width,height);
                //设置全屏按钮状态
                btn_full_screen.setBackgroundResource(R.drawable.btn_full_screen_normal);
                break;
        }
    }
    /**
     * 隐藏控制面板
     */
    private void hideMediaController() {
        isControllerShow = false;
        ll_top_bar.setVisibility(View.GONE);
        ll_bottom_bar.setVisibility(View.GONE);
    }

    /**
     * 显示控制面板
     */
    private void showMediaController() {
        isControllerShow = true;
        ll_top_bar.setVisibility(View.VISIBLE);
        ll_bottom_bar.setVisibility(View.VISIBLE);
    }

    private void setListener() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoWidth = mp.getVideoWidth();
                videoHeight = mp.getVideoHeight();
                mVideoView.start();
                //获得视频总长，关联seekBar
                int duration = mVideoView.getDuration();
                seek_bar_video.setMax(duration);
                tv_duration.setText(mUtils.timeToString(duration));
                //默认隐藏控制面板
                hideMediaController();
                //发消息更新
                mHandler.sendEmptyMessage(PROGRESS);
//                mVideoView.setVideoSize(mp.getVideoWidth(),mp.getVideoHeight());
                //设置视频默认播放大小
                setVideoType(DEFAULT_SCREEN);

            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayer.this,"Unknown error",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                Toast.makeText(SystemVideoPlayer.this,"finished",Toast.LENGTH_SHORT).show();
//                finish();
                playNextVideo();
            }
        });
//         set control panel
//        mVideoView.setMediaController(new MediaController(this));

        seek_bar_video.setOnSeekBarChangeListener(new MyVideoSeekBarChangeListener());

        seek_current_volume.setOnSeekBarChangeListener(new VolumeSeekBarChangeListener());
    }

    public String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return format.format(new Date());
    }

    //音量控制条
    private class VolumeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                if (progress > 0){
                    isMute = false;
                }else {
                    isMute = true;
                }
                updateVolume(progress,isMute);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeMessages(HIDE_MEDIA_CONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,3500);
        }
    }

    /**
     * 设置音量大小
     * @param progress
     * flags 0:表示不调用系统音量；1：调用系统音量
     */
    private void updateVolume(int progress,boolean isMute) {
        if (isMute){
            am.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
            seek_current_volume.setProgress(0);
        }else {
            am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            seek_current_volume.setProgress(progress);
            currentVolume = progress;
        }

    }

    //播放进度控制条
    class MyVideoSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{
        //进度改变时回调

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                mVideoView.seekTo(progress);
            }
        }
        //当触碰seekBar时回调
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeMessages(HIDE_MEDIA_CONTROLLER);
        }
        //当手指离开seekBar时回调
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,3500);
        }
    }

    private void initView() {
        mVideoView = findViewById(R.id.video_view);
        ll_top_bar = findViewById(R.id.ll_top_bar);
        ll_bottom_bar = findViewById(R.id.ll_bottom_bar);
        ll_controller = findViewById(R.id.ll_controller);
        ll_duration = findViewById(R.id.ll_duration);
        ll_name_time = findViewById(R.id.ll_name_time);
        seek_bar_video = findViewById(R.id.seek_bar_video);
        btn_forward = findViewById(R.id.btn_forward);
        btn_full_screen = findViewById(R.id.btn_full_screen);
        btn_info = findViewById(R.id.btn_info);
        btn_pause = findViewById(R.id.btn_pause);
        btn_previous = findViewById(R.id.btn_previous);
        btn_video_exit = findViewById(R.id.btn_video_exit);
        btn_voice = findViewById(R.id.btn_voice);
        tv_current_duration = findViewById(R.id.tv_current_time);
        tv_duration = findViewById(R.id.tv_duration);
        current_time = findViewById(R.id.current_time);
        seek_current_volume = findViewById(R.id.current_volume);
        video_name = findViewById(R.id.video_name);
        img_battery_state = findViewById(R.id.img_battery_state);

        btn_pause.setOnClickListener(this);
        btn_video_exit.setOnClickListener(this);
        btn_full_screen.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        btn_voice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pause:
                startAndPauseVideo();
                break;
            case R.id.btn_video_exit:
                btn_video_exit.setPressed(true);
                finish();
                break;
            case R.id.btn_previous:
                playPreviousVideo();
                break;
            case R.id.btn_forward:
                playNextVideo();
                break;
            case R.id.btn_full_screen:
                setFullScreenAndDefaultMode();
                break;
            case R.id.btn_voice:
                //通过AudioManager调节音量，1、实例化AudioManager;2、获取当前音量、最大音量；3、与seekBar关联；4、设置SeekBar状态变化
                isMute = ! isMute;
                updateVolume(currentVolume,isMute);
                break;
        }
        mHandler.removeMessages(HIDE_MEDIA_CONTROLLER);
        mHandler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,3500);
    }

    /**
     * 视频启停
     */
    private void startAndPauseVideo() {
        if (mVideoView.isPlaying()){
            btn_pause.setPressed(true);
            mVideoView.pause();
        } else{
            btn_pause.setPressed(false);
            mVideoView.start();
        }
    }

    /**
     * 播放前一个视频
     */
    private void playPreviousVideo() {
        if (mMediaItems != null && mMediaItems.size() > 0){
            position --;
            if (position >= 0){
                MediaItem mediaItem = mMediaItems.get(position);
                video_name.setText(mediaItem.getName());
                mVideoView.setVideoPath(mediaItem.getData());
                setButtonState();
            }
        }else if (mUri != null){
            setButtonState();
        }
    }

    /**
     * 播放下一个视频
     */
    private void playNextVideo() {
        if(mMediaItems != null && mMediaItems.size() >0){
            //播放下一个
            position++;
            if (position < mMediaItems.size()){
                MediaItem mediaItem = mMediaItems.get(position);
                video_name.setText(mediaItem.getName());
                mVideoView.setVideoPath(mediaItem.getData());

                //设置按钮状态
                setButtonState();
            }
        }else if (mUri != null){
            //设置上一个/下一个按钮为灰色切不可点击
            setButtonState();
        }
    }

    private void setButtonState() {
        if (mMediaItems != null && mMediaItems.size() > 0){
            if (mMediaItems.size() == 1){
               setEnabledFalse();
            }else if (mMediaItems.size() == 2){
                if (position == 0){
                    btn_previous.setBackgroundResource(R.drawable.video_pre_gray);
                    btn_previous.setEnabled(false);
                    btn_forward.setBackgroundResource(R.drawable.btn_forward_selector);
                    btn_forward.setEnabled(true);
                }else {
                    btn_previous.setBackgroundResource(R.drawable.btn_back_selector);
                    btn_previous.setEnabled(true);
                    btn_forward.setBackgroundResource(R.drawable.video_next_btn_bg);
                    btn_forward.setEnabled(false);
                }
            }
            else {
                if (position == 0){
                    btn_previous.setBackgroundResource(R.drawable.video_pre_gray);
                    btn_previous.setEnabled(false);
                }else if (position == mMediaItems.size()-1){
                    btn_forward.setBackgroundResource(R.drawable.video_next_btn_bg);
                    btn_forward.setEnabled(false);
                }else {
                    btn_forward.setBackgroundResource(R.drawable.btn_forward_selector);
                    btn_forward.setEnabled(true);

                    btn_previous.setBackgroundResource(R.drawable.btn_back_selector);
                    btn_previous.setEnabled(true);
                }
            }
        }else if (mUri != null){
            //两个按钮均设置灰色
            setEnabledFalse();
        }
    }

    /**
     * 设置前进后退键
     */
    private void setEnabledFalse() {
        btn_forward.setBackgroundResource(R.drawable.video_next_btn_bg);
        btn_forward.setEnabled(false);
        btn_previous.setBackgroundResource(R.drawable.video_pre_gray);
        btn_previous.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (mBatteryChangeReceiver != null){
            unregisterReceiver(mBatteryChangeReceiver);
            mBatteryChangeReceiver = null;
        }
        super.onDestroy();
    }

    class BatteryChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);//范围0--100
            //主线程
            setBattery(level);
        }

        private void setBattery(int level) {
            if (level <= 0){
                img_battery_state.setImageResource(R.drawable.ic_battery_0);
            }else if(level <= 10){
                img_battery_state.setImageResource(R.drawable.ic_battery_10);
            }else if(level <= 20){
                img_battery_state.setImageResource(R.drawable.ic_battery_20);
            }else if (level <= 40){
                img_battery_state.setImageResource(R.drawable.ic_battery_40);
            }else if (level <= 60){
                img_battery_state.setImageResource(R.drawable.ic_battery_60);
            }else if (level <= 80){
                img_battery_state.setImageResource(R.drawable.ic_battery_80);
            }else if (level <= 100){
                img_battery_state.setImageResource(R.drawable.ic_battery_100);
            }
        }
    }

//    private float startX;
    private float startY;
    private float distanceY;
    private int mVolume;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //接管onTouchEvent
        mDetector.onTouchEvent(event);
        //滑动改变视频音量
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                startX = event.getX();
                startY = event.getY();
                mVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                distanceY = Math.min(screenWidth,screenHeight);
                mHandler.removeMessages(HIDE_MEDIA_CONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float distance = startY - endY;
                float deltaVolume  = (distance/distanceY) * maxVolume;
                int volume = (int) Math.min(Math.max(0,mVolume + deltaVolume),maxVolume);
                if (deltaVolume != 0){
                    updateVolume(volume,isMute);
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,3500);
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 监听物理键，调节音量大小
     * @param keyCode
     * @param event
     * @return true 表示不同时设置系统音量，false表示同时设置系统音量
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVolume -- ;
            updateVolume(currentVolume,isMute);
            mHandler.removeMessages(HIDE_MEDIA_CONTROLLER);
            mHandler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,3500);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            currentVolume ++;
            updateVolume(currentVolume,isMute);
            mHandler.removeMessages(HIDE_MEDIA_CONTROLLER);
            mHandler.sendEmptyMessageDelayed(HIDE_MEDIA_CONTROLLER,3500);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
