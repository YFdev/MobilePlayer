package com.elapse.mobileplayer.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.elapse.mobileplayer.IMusicPlayerService;
import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.service.MusicPlayerService;

/**
 * 音乐播放器-->系统
 * Created by YF_lala on 2018/12/16.
 */

public class SystemAudioPlayer extends Activity {

    private ImageView img_icon;
    private int position;
    private IMusicPlayerService mService;
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
                    mService.openAudio(position);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player_with_controller);
        initView();
        getData();
    }

    private void initView() {
        //启动帧动画
        img_icon = findViewById(R.id.img_bg_music_player);

    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.elapse.mobileplayer_OPENAUDIO");
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
        startService(intent);//不会多次调用startService（）
    }

    public void getData() {
        //得到播放位置
        position = getIntent().getIntExtra("position",0);
        bindAndStartService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
