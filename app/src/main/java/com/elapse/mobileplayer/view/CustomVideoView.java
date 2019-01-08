package com.elapse.mobileplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * 自定义videoView , 可以设置窗口大小，用于全屏切换
 * Created by YF_lala on 2018/12/11.
 */

public class CustomVideoView extends android.widget.VideoView{

    public CustomVideoView(Context context) {
        this(context,null);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }

    /**
     * 设置视频的宽和高
     * @param videoWidth 指定视频宽
     * @param videoHeight 指定视频的高
     */
    public void setVideoSize(int videoWidth,int videoHeight){
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = videoWidth;
        params.height = videoHeight;
        setLayoutParams(params);
//        requestLayout(); 和setLayoutParams()作用相同
    }
}
