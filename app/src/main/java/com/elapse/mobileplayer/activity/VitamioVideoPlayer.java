package com.elapse.mobileplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.domain.MediaItem;
import com.elapse.mobileplayer.util.Utils;
import com.elapse.mobileplayer.view.VitamioVideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * vitamio 播放器
 * Created by YF_lala on 2018/12/10.
 */

public class VitamioVideoPlayer extends Activity implements View.OnClickListener {

    private static final int PROGRESS = 0 ;
    private static final int HIDE_MEDIA_CONTROLLER = 1;
    /**
     * 全屏
     */
    private static final int FULL_SCREEN = 1;
    private static final int DEFAULT_SCREEN = 2;
    private static final int GET_NET_WORK_SPEED = 3;
    private Utils mUtils;
    private VitamioVideoView mVideoView;
    //接收intent传递的URI
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
    //底部控制栏-->布局
    private LinearLayout ll_bottom_bar;
    private LinearLayout ll_duration;//-->布局
    private TextView tv_current_duration;//已播放时长
    private SeekBar seek_bar_video;//当前进度
    private TextView tv_duration;//总时长
    //控制按钮栏_布局
    private LinearLayout ll_controller;//-->布局
    //退出、前一个、暂停、后一个、全屏
    private Button btn_video_exit,btn_previous,btn_pause,btn_forward,btn_full_screen;
    //播放卡布局
    private LinearLayout buffer_layout;
    //网速信息
    private TextView net_work_info;
    //遮罩层布局
    private LinearLayout ll_loading;
    private TextView wifi_speed;
    //是否使用系统监听卡(直播使用)
    private boolean usingSysListener = false;
    //接收电量广播
    private BroadcastReceiver mBatteryChangeReceiver;
    //接收intent传递的播放列表
    private ArrayList<MediaItem> mMediaItems;
    //接收intent传递的列表位置
    private int position;
    //手势识别
    private GestureDetector mDetector;
    //是否显示控制面板
    private boolean isControllerShow;
    //保存上一次播放进度
    private int pre_position;
    //发消息更新seekbar进度
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
    //是否静音
    private boolean isMute = false;
    //判断是否为网络资源
    private boolean isNetUri;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case PROGRESS:
                    //得到当前播放进度
                    int currentPosition = (int) mVideoView.getCurrentPosition();
                    seek_bar_video.setProgress(currentPosition);
                    tv_current_duration.setText(mUtils.timeToString(currentPosition));

                    //更新系统时间
                    current_time.setText(getSystemTime());

                    //缓冲进度
                    if (isNetUri){
                        //只有网络资源才需要缓冲
                        int buffer = mVideoView.getBufferPercentage();//0--100
                        int total_buffer = buffer * seek_bar_video.getMax();
                        int secondProgress = total_buffer / 100;
                        seek_bar_video.setSecondaryProgress(secondProgress);
                    }else {
                        //本地视频不缓冲
                        seek_bar_video.setSecondaryProgress(0);
                    }

                    //监听卡
                    if (!usingSysListener){
                        if (mVideoView.isPlaying()){
                            int buffer = currentPosition - pre_position;
                            if (buffer < 500){
                                //视频卡
                                buffer_layout.setVisibility(View.VISIBLE);
                            }else {
                                buffer_layout.setVisibility(View.GONE);
                            }
                        }else{
                            //视频不卡
                            buffer_layout.setVisibility(View.GONE);
                        }
                    }
                    pre_position = currentPosition;
                    //每秒更新一次
                    // mHandler.removeMessages(PROGRESS);
                    mHandler.sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
                case HIDE_MEDIA_CONTROLLER:
                    hideMediaController();
                    break;
                case GET_NET_WORK_SPEED:
                    String wifiSpeed = mUtils.getNetWorkSpeed(VitamioVideoPlayer.this);
                    net_work_info.setText("缓冲中..."+wifiSpeed);
                    wifi_speed.setText("正在加载..."+wifiSpeed);
                    // 2秒更新一次
                    mHandler.removeMessages(GET_NET_WORK_SPEED);
                    mHandler.sendEmptyMessageDelayed(GET_NET_WORK_SPEED,2000);
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.vitamio_player_with_controller);

        initView();
        initData();
        getData();
        setData();
        setListener();
    }

    private void setData() {
        if (mMediaItems != null && mMediaItems.size() >0){
            MediaItem mediaItem = mMediaItems.get(position);
            isNetUri = mUtils.isNetUri(mediaItem.getData());
            mVideoView.setVideoPath(mediaItem.getData());
            video_name.setText(mediaItem.getName());
        }else if (mUri != null){
            mVideoView.setVideoURI(mUri);
            isNetUri = mUtils.isNetUri(mUri.toString());
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
        //开始更新网速
        mHandler.sendEmptyMessage(GET_NET_WORK_SPEED);
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
                int duration = (int) mVideoView.getDuration();
                seek_bar_video.setMax(duration);
                tv_duration.setText(mUtils.timeToString(duration));
                //默认隐藏控制面板
                hideMediaController();
                //发消息更新
                mHandler.sendEmptyMessage(PROGRESS);
//                mVideoView.setVideoSize(mp.getVideoWidth(),mp.getVideoHeight());
                //设置视频默认播放大小
                setVideoType(DEFAULT_SCREEN);

                ll_loading.setVisibility(View.GONE);

            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Toast.makeText(VitamioVideoPlayer.this,"Unknown error",Toast.LENGTH_SHORT).show();
               showErrorDialog();
                return true;
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

        if (usingSysListener){
            //监听视频卡顿--系统api
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mVideoView.setOnInfoListener(new MyOnInfoListener());
            }else{
                usingSysListener = false;
            }
        }
    }

    /**
     * 切换到系统播放器
     */
    private void showSwitchPlayer(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当视频花屏时，可尝试切换到系统播放器");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSystemPlayer();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    /**
     * 切换到系统播放器
     */
    private void startSystemPlayer(){
        if (mVideoView != null){
            mVideoView.stopPlayback();
        }
        Intent intent = new Intent(this,SystemVideoPlayer.class);
        if (mMediaItems != null && mMediaItems.size() > 0){
            Bundle b = new Bundle();
            b.putSerializable("video_list",mMediaItems);
            intent.putExtras(b);
            intent.putExtra("position",position);
        }else if (mUri != null){
            intent.setData(mUri);
        }
        startActivity(intent);
        finish();
    }
    /**
     * 播放出错处理
     */
    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("抱歉，无法播放该视频");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    public String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * 监听卡
     */
    private class MyOnInfoListener implements MediaPlayer.OnInfoListener{

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what){
                case MediaPlayer.MEDIA_INFO_BUFFERING_START://视频卡、拖动卡
//                    Toast.makeText(SystemVideoPlayer.this,"卡顿",Toast.LENGTH_SHORT).show();
                    buffer_layout.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END://视频拖动卡结束
                    buffer_layout.setVisibility(View.GONE);
                    break;
            }
            return true;
        }
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
        buffer_layout = findViewById(R.id.buffer_layout);
        net_work_info = findViewById(R.id.net_work_info);
        ll_loading = findViewById(R.id.ll_loading);
        wifi_speed = findViewById(R.id.Wifi_speed);

        btn_pause.setOnClickListener(this);
        btn_video_exit.setOnClickListener(this);
        btn_full_screen.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        btn_voice.setOnClickListener(this);
        btn_info.setOnClickListener(this);
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
            case R.id.btn_info:
                showSwitchPlayer();
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
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mMediaItems.get(position);
                isNetUri = mUtils.isNetUri(mediaItem.getData());
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
                //显示遮罩层
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mMediaItems.get(position);
                video_name.setText(mediaItem.getName());
                isNetUri = mUtils.isNetUri(mediaItem.getData());
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

    /**
     * 电量广播
     */
    class BatteryChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);//范围0--100
            //主线程
            setBattery(level);
        }

        /**
         * 设置电池状态图片
         * @param level
         */
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
