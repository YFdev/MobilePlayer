package com.elapse.mobileplayer.base;

import android.content.Context;
import android.view.View;

/**
 * Created by YF_lala on 2018/12/7.
 * Basic class -->common layout
 */

public abstract class BasePager {
    public Context mContext;
    public View rootView;
    public boolean hasInit = false;
    public BasePager(Context context) {
        mContext = context;
        rootView = initView();
        hasInit = false;
    }

    public abstract View initView();

    //initial data,to asking/display data
    public void initData(){

    }

}
