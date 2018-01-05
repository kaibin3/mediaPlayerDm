package com.example.wenjie.mediaplayerdm.PhiFind;

/**
 * Created by wen.jie on 2018/1/5.
 */

public class VideoPlayHelper {

    public static String formatDuration(int duration) {
        duration = duration / 1000;
        int min = duration / 60;
        String minStr = getTimeStr(min);
        int second = duration % 60;
        String secondStr = getTimeStr(second);
        return minStr + ":" + secondStr;
    }

    private static String getTimeStr(int time) {
        if (time < 10) {
            return "0" + time;
        }
        return "" + time;
    }

}
