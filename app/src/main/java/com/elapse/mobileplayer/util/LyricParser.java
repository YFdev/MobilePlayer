package com.elapse.mobileplayer.util;

import android.text.TextUtils;

import com.elapse.mobileplayer.domain.Lyric;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 自定义解析歌词方法
 * Created by YF_lala on 2018/12/21.
 */

public class LyricParser {

    private ArrayList<Lyric> mLyrics;

    private boolean isExist = false;//是否存在歌词

    public boolean isExist() {
        return isExist;
    }

    public ArrayList<Lyric> getLyrics() {
        return mLyrics;
    }

    public void readFile(File file) {
        if (file == null || !file.exists()) {
            mLyrics = null;
            isExist = false;
        } else {
            //一行一行读取
            isExist = true;
            mLyrics = new ArrayList<>();
            BufferedReader reader;
            try {
                reader = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), getCharset(file)));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    parseLyric(line);
                }
                reader.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //排序
            Collections.sort(mLyrics, new Comparator<Lyric>() {
                @Override
                public int compare(Lyric o1, Lyric o2) {
                    if (o1.getTimeStamp() < o2.getTimeStamp()) {
                        return -1;
                    } else if (o1.getTimeStamp() > o2.getTimeStamp()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            //计算显示时间
            for (int i = 0; i < mLyrics.size(); i++) {
                long sleepTime = mLyrics.get(i + 1).getTimeStamp() - mLyrics.get(i).getTimeStamp();
                if (sleepTime > 0) {
                    mLyrics.get(i).setSleepTime(sleepTime);
                }
            }
        }
    }

    /**
     * 判断歌词编码
     */
    public String getCharset(File file) {
        String charset = "GBK";
        byte[] firstBytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            bis.mark(0);
            int read = bis.read(firstBytes, 0, 3);
            if (read == -1)
                return charset;
            if (firstBytes[0] == (byte) 0xFF && firstBytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (firstBytes[0] == (byte) 0xFE && firstBytes[1] == (byte) 0xFF) {
                charset = "utf-16be";
                checked = true;
            } else if (firstBytes[0] == (byte) 0xEF &&
                    firstBytes[1] == (byte) 0xBB &&
                    firstBytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read > 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF)
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return charset;
    }

    /**
     * 解析一句歌词
     *
     * @param line [02:04.2][03:37.32][00:59.73]我在这儿
     */
    private void parseLyric(String line) {
        String lyricLine = line;
        //替换所有“[]”
        lyricLine = lyricLine.replaceAll("\\[", "$");
        lyricLine = lyricLine.replaceAll("\\]", "$");
        //按'$'分割
        String[] items = lyricLine.split("$");
        for (int i = 0; i < items.length - 1; i++) {
            Lyric lyric = new Lyric();
            if (!TextUtils.isEmpty(items[i].trim())) {
                lyric.setTimeStamp(str2long(items[i].trim()));
                if (!TextUtils.isEmpty(items[items.length - 1].trim())) {
                    lyric.setContent(items[items.length - 1].trim());
                } else {
                    lyric.setContent(" ");
                }
                mLyrics.add(lyric);
            }
        }
    }

    private long str2long(String item) {
        int minute = 0;
        int second = 0;
        int mill = 0;
        try {
            //1、把02:04.2按照：切割
            String[] s1 = item.split(":");
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
            return -1;
        }
    }
}
