package com.elapse.mobileplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.elapse.mobileplayer.domain.Lyric;

import java.util.ArrayList;

/**
 * 自定义歌词显示控件
 * Created by YF_lala on 2018/12/20.
 */

public class ShowLyricView extends android.support.v7.widget.AppCompatTextView {
    //歌词列表
    private ArrayList<Lyric> mLyrics;
    private Paint mPaint;
    private Paint mFBPaint;
    private int width;
    private int height;
    private int index;//当前歌词在列表中的索引
    private int textHeight;//行高
    private int textSize;//文字大小
    private int cur_position;
    private long sleepTime;//高亮显示时间
    private long timeStamp;//高亮显示的时刻

    public ShowLyricView(Context context) {
        this(context,null);
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        textHeight = sp2px(context,20);
        textSize = sp2px(context,16);
    }

    /**
     * dp 转 px
     * @param context getResources()
     * @param sp dp
     * @return px
     */
    private int sp2px(Context context, int sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,sp,context.getResources().getDisplayMetrics()
        );
    }

    private void initView() {
        mLyrics = new ArrayList<>();
//        for (int i = 0;i < 500;i++){
//            Lyric lyric = new Lyric();
//            lyric.setTimeStamp(1000 * i);
//            lyric.setSleepTime(1000);
//            lyric.setContent(lyric.getSleepTime() + "beijing beijing" +lyric.getTimeStamp());
//            mLyrics.add(lyric);
//        }

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        //设置居中对齐
        mPaint.setTextAlign(Paint.Align.CENTER);

        mFBPaint = new Paint();
        mFBPaint.setColor(Color.WHITE);
        mFBPaint.setAntiAlias(true);
        mFBPaint.setTextSize(textSize);
        //设置居中对齐
        mFBPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLyrics != null && mLyrics.size() > 0){
            //往上移动
            float push_distance = 0;
            if (sleepTime == 0){
                push_distance = 0;
            }else {
                //平移
                //本句所花时间/休眠时间 = 移动的距离/总距离（行高）
                //注意数据类型转换陷阱
                float delta = (float) textHeight * ((float) cur_position - (float) timeStamp) / (float) sleepTime;

                push_distance = textHeight + delta;
            }
            canvas.translate(0,-push_distance);
            //绘制歌词
            //绘制当前
            String cur_lyric = mLyrics.get(index).getContent();
            canvas.drawText(cur_lyric,width/2,height/2,mPaint);

            //绘制前半部分
            int tempY = height; // 视图中间坐标
            for (int i = index - 1;i >= 0;i--){
               tempY = tempY - textHeight;
                if (tempY < 0){
                    break;
                }
                canvas.drawText(mLyrics.get(i).getContent(),width / 2,tempY,mFBPaint);
            }

            //绘制后半部分
            for (int i = index + 1;i < mLyrics.size();i++){
                tempY = tempY + textHeight;
                if (tempY > height){
                    break;
                }
                canvas.drawText(mLyrics.get(i).getContent(),width/2,tempY,mFBPaint);
            }

        }else {
            canvas.drawText("没有歌词",width / 2,height / 2,mPaint);
        }

    }

    public void setLyrics(ArrayList<Lyric> lyrics) {
        mLyrics = lyrics;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    //根据当前播放位置，找出高亮显示歌词
    public void setCurrentIndex(int cur_position) {
        this.cur_position = cur_position;
        if (mLyrics == null || mLyrics.size() == 0){
            return;
        }
        for (int i = 1;i<mLyrics.size();i++){
            if (cur_position < mLyrics.get(i).getTimeStamp()){
                int tempIndex = i - 1;
                if (cur_position >= mLyrics.get(tempIndex).getTimeStamp()){
                    //当前正在播放的歌词
                    index = tempIndex;
                    sleepTime = mLyrics.get(index).getSleepTime();
                    timeStamp = mLyrics.get(index).getTimeStamp();
                }
            }
        }
        //重新绘制
        invalidate();//主线程执行
        //子线程执行用postInvalidate();
    }
}
