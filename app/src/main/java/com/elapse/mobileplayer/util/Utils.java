package com.elapse.mobileplayer.util;


import android.content.Context;
import android.net.TrafficStats;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by YF_lala on 2018/12/10.
 */

public class Utils {

    private  StringBuilder mFormatBuilder;
    private  Formatter mFormatter;
    private long lastTotalBytes;
    private long lastTimeStamp;
    public Utils() {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder,Locale.getDefault());
    }

    public String timeToString(int timeMills){
        int totalSeconds = timeMills / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0){
            return mFormatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }else{
            return mFormatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }

    /**
     * 判断是否为网络资源
     * @param uri
     * @return
     */
    public boolean isNetUri(String uri){
        boolean result = false;
        if (uri != null){
            if (uri.toLowerCase().startsWith("http") || uri.toLowerCase().startsWith("rtsp")
                    || uri.toLowerCase().startsWith("mms")){
                result = true;
            }
        }
        return result;
    }

    /**
     * 获取网速
     * @param context
     * @return
     */
    public String getNetWorkSpeed(Context context){
        long nowTotalBytes = TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) ==
                TrafficStats.UNSUPPORTED ? 0 : TrafficStats.getTotalRxBytes()/1024;
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalBytes - lastTotalBytes)*1000 / (nowTimeStamp - lastTimeStamp));
        lastTotalBytes = nowTotalBytes;
        lastTimeStamp = nowTimeStamp;
        return speed + "kb/s";
    }
    public String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        return format.format(new Date());
    }

}
