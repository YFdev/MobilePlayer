package com.elapse.mobileplayer.util;


import java.util.Formatter;

/**
 * Created by YF_lala on 2018/12/10.
 */

public class Utils {

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public Utils() {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter();
    }

    public String timeToString(int timeMills){
        int totalSeconds = timeMills / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0){
            return mFormatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }else
            return mFormatter.format("%02d:%02d",minutes,seconds).toString();
    }
}
