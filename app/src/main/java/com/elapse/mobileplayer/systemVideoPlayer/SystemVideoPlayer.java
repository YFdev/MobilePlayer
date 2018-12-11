package com.elapse.mobileplayer.systemVideoPlayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by YF_lala on 2018/12/10.
 */

public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    private static final int PROGRESS = 0 ;
    private Utils mUtils;
    private VideoView mVideoView;
    private Uri mUri;
    //顶部标题栏
    private LinearLayout ll_top_bar;
    private LinearLayout ll_name_time;//影片名称和当前时间
    private TextView current_time;//当前时间
    private TextView video_name;//影片名称
    private SeekBar current_volume;//音量
    private Button btn_voice;//音量控制按钮
    private Button btn_info;//info信息
    private ImageView img_battery_state;//电量信息
    //底部控制栏
    private LinearLayout ll_bottom_bar;
    private LinearLayout ll_duration;//
    private TextView tv_current_duration;//已播放时长
    private SeekBar seek_bar_video;//当前进度
    private TextView tv_duration;//总时长
    //控制按钮栏
    private LinearLayout ll_controller;
    //退出、前一个、暂停、后一个、全屏
    private Button btn_video_exit,btn_previous,btn_pause,btn_forward,btn_full_screen;
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
            }
            return true;
        }
    });
    private BroadcastReceiver mBatteryChangeReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_controller);

        initView();
        initData();
        mUri = getIntent().getData();
        mVideoView.setVideoURI(mUri);
        setListener();
    }

    private void initData() {
        mUtils = new Utils();
        //注册广播，监听电量
        mBatteryChangeReceiver = new BatteryChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryChangeReceiver,intentFilter);

    }

    private void setListener() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //total duration and seekBar.setMax()
                int duration = mVideoView.getDuration();
                seek_bar_video.setMax(duration);
                tv_duration.setText(mUtils.timeToString(duration));
                //send message to update
                mHandler.sendEmptyMessage(PROGRESS);
                mVideoView.start();
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
                Toast.makeText(SystemVideoPlayer.this,"finished",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
//         set control panel
//        mVideoView.setMediaController(new MediaController(this));

        seek_bar_video.setOnSeekBarChangeListener(new MyVideoSeekBarChangeListener());
    }

    public String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return format.format(new Date());
    }

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

        }
        //当手指离开seekBar时回调
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

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
        current_volume = findViewById(R.id.current_volume);
        video_name = findViewById(R.id.video_name);
        img_battery_state = findViewById(R.id.img_battery_state);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pause:
                if (mVideoView.isPlaying()){
                    mVideoView.pause();
                    btn_pause.setPressed(true);
                } else{
                    mVideoView.start();
                    btn_pause.setPressed(false);
                }
                break;
            case R.id.btn_video_exit:
                btn_video_exit.setPressed(true);
                finish();
                break;
            case R.id.btn_previous:
                break;
            case R.id.btn_forward:
                break;
            case R.id.btn_full_screen:
                break;
        }
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

}
