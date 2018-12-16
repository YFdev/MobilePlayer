package com.elapse.mobileplayer.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by YF_lala on 2018/12/15.
 */

public class CacheUtils {
    /**
     * 保存数据
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences("netCache",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }

    /**
     * 得到缓存数据
     * @param context
     * @param key
     * @return
     */
    public static String getValue(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences("netCache",Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }
}
