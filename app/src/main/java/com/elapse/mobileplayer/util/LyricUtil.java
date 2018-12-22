package com.elapse.mobileplayer.util;

import com.elapse.mobileplayer.domain.Lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * 解析歌词
 * Created by YF_lala on 2018/12/20.
 */

@Deprecated
public class LyricUtil {

    private ArrayList<Lyric> mLyrics;
    /**
     * 读取歌词文件
     * @param file /mnt/sdcard/audio/beijing.lrc
     */
    public void readLyricFile(File file){
        if (file == null || ! file.exists()){
            //文件不存在
            mLyrics = null;
        }else {
            //解析歌词
            //一行一行读取并解析
            mLyrics = new ArrayList<>();
            BufferedReader reader;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file)));
                String line = "";
                while ((line = reader.readLine()) != null){
                    parseLyric(line);
                }

                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //排序

            //计算每句显示时间
        }
    }

    /**
     * 解析一句歌词
     * @param line [02:04.2][03:37.32][00:59.73]我在这儿
     */
    private String parseLyric(String line) {
        int pos1 = line.indexOf("["); // 0 ,如果没有返回-1
        int pos2 = line.indexOf("]"); // 9

        if (pos1 == 0 && pos2 != -1){//代表有一句歌词
            long[] times = new long[getCountTag(line)];
            String strTime = line.substring(pos1 + 1,pos2);//02:04.2
            times[0] = strTime2longTime(strTime);
            String content = line;
            int i = 1;
            while(pos1 == 0 && pos2 != -1 ){
                content = content.substring(pos2 + 1);
                pos1 = content.indexOf("\\[");
                pos2 = content.indexOf("\\]");

               if (pos2 != -1){
                   strTime = content.substring(pos1 + 1,pos2);
                   times[i] = strTime2longTime(strTime);

                   if (times[i] == -1){
                       return "";
                   }
                   i ++;
               }
            }

            //把时间数组和文本关联起来，并加入到集合中
            for (int j = 0;j < times.length; j++){
                Lyric lyric = new Lyric();
                if (times[j] != 0){
                    lyric.setContent(content);
                    lyric.setTimeStamp(times[j]);
                    mLyrics.add(lyric);
                }
            }
            return content;
        }
        return null;
    }

    /**
     * 把string类型时间转换成long类型
     * @param strTime 02:04.2
     * @return
     */
    private long strTime2longTime(String strTime) {
        int minute = 0;
        int second = 0;
        int mill = 0;
        try {
            //1、把02:04.2按照：切割
            String[] s1 = strTime.split(":");
            //2、把04.2按.切割
            String[] s2 = s1[1].split("\\.");
            //分钟
            minute = Integer.valueOf(s1[0]);
            //秒
            second = Integer.parseInt(s2[0]);
            //毫秒
            mill = Integer.valueOf(s2[1]);
            return minute * 60 * 1000 + second * 1000 + mill * 10;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return  -1;
        }
    }

    /**
     * 判断有多少句歌词
     * @param line
     * @return
     */
    private int getCountTag(String line) {
        int result = -1;
        String[] left = line.split("\\[");
        String[] right = line.split("\\]");

        if (left.length == 0 && right.length == 0){
            result = 1;
        }else if (left.length > right.length){
            result = left.length;
        }else {
            result = right.length;
        }
        return result;
    }

}
