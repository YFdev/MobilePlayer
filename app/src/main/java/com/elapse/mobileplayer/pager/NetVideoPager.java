package com.elapse.mobileplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.elapse.mobileplayer.R;
import com.elapse.mobileplayer.base.BasePager;

/**
 * Created by YF_lala on 2018/12/7.
 * local video
 */

public class NetVideoPager extends BasePager {


    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.video_pager,null);
        return view;
    }

    @Override
    public void initData() {

    }
}
