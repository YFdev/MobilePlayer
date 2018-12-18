package com.elapse.mobileplayer.app;

import android.app.Application;

import org.xutils.*;
import org.xutils.BuildConfig;

/**
 * 自定义application，初始化xUtils
 * Created by YF_lala on 2018/12/14.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
