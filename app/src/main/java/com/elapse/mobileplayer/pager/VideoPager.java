package com.elapse.mobileplayer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.elapse.mobileplayer.base.BasePager;

/**
 * Created by YF_lala on 2018/12/7.
 * local video
 */

public class VideoPager extends BasePager {

    private TextView mTextView;
    private Context mContext;
    public VideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        mTextView = new TextView(mContext);
        mTextView.setTextColor(Color.BLACK);

        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(30);
        return mTextView;
    }

    @Override
    public void initData() {
        mTextView.setText("Local Video");
    }
}
