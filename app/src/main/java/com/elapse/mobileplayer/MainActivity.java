package com.elapse.mobileplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.elapse.mobileplayer.base.BasePager;
import com.elapse.mobileplayer.pager.AudioPager;
import com.elapse.mobileplayer.pager.NetAudioPager;
import com.elapse.mobileplayer.pager.NetVideoPager;
import com.elapse.mobileplayer.pager.Pager_frag;
import com.elapse.mobileplayer.pager.VideoPager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private RadioGroup rg_main;
    private ArrayList<BasePager> basePagers;
    private int position;//record page position
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rg_main = findViewById(R.id.rg_main);

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
}
