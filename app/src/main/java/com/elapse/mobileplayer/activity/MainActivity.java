package com.elapse.mobileplayer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.base.BasePager;
import com.elapse.mobileplayer.pager.AudioPager;
import com.elapse.mobileplayer.pager.NetAudioPager;
import com.elapse.mobileplayer.pager.NetVideoPager;
import com.elapse.mobileplayer.pager.Pager_frag;
import com.elapse.mobileplayer.pager.VideoPager;
import com.elapse.mobileplayer.view.TitleBar;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    //底部布局
    private RadioGroup rg_main;
    //页面集合
    private ArrayList<BasePager> basePagers;
    //页面位置
    private int position;//record page position
    //标记是否退出，用于实现再按一次退出
    private boolean isExit;

//    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_main = findViewById(R.id.rg_main);
//        mTitleBar = findViewById(R.id.title_bar);
        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));//local video
        basePagers.add(new AudioPager(this));//local audio
        basePagers.add(new NetVideoPager(this));//net video
        basePagers.add(new NetAudioPager(this));//net audio
        rg_main.setOnCheckedChangeListener(new MyOnCheckChangeListener());
        rg_main.check(R.id.rb_video);
    }


    class MyOnCheckChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_audio:
                    position = 3;
                    break;
            }
            setFragment();
        }
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Pager_frag frag = new Pager_frag();
        frag.setBasePager(getBasePager(position));
        transaction.replace(R.id.fl_main,frag);
        transaction.commit();
    }

    private BasePager getBasePager(int position) {
        BasePager basePager = basePagers.get(position);
        if (basePager != null && !basePager.hasInit){
            basePager.hasInit = true;
            basePager.initData();
        }
        return basePager;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (position != 0){
                position = 0;
                rg_main.check(R.id.rb_video);//首页
                return true;//返回true 事件不再继续传递
            }else if (! isExit){
                isExit = true;
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                },2000);
                return true;
            }
        }
        //默认退出
        return super.onKeyDown(keyCode, event);
    }
}
