package com.elapse.mobileplayer.app;

import android.app.Application;
import android.util.Log;

import com.elapse.mobileplayer.R;
import com.iflytek.cloud.SpeechUtility;

import org.xutils.BuildConfig;

import org.xutils.x;

/**
 * 自定义application，初始化xUtils
 * Created by YF_lala on 2018/12/14.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        //出现bug的原因是初始化失败
        SpeechUtility utility = SpeechUtility.createUtility(MyApplication.this,
                "appid=" + getString(R.string.app_id));//组件未安装.(错误码:21002)
        super.onCreate();
        if (utility != null){
            Log.d("utility", "onCreate: succeed");
        }else {
            Log.d("utility", "onCreate: failed");
        }
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
