package com.example.wenjie.mediaplayerdm.util;


public class MediaUtil {


    /**
     * 格式化时间
     *
     * @param mesc
     * @return
     */
    public static String formatTime(long mesc) {
        long hour;
        long minute;
        long second;
        String hours = "00";
        String minStr = "";
        String secStr = "";


        hour = mesc / 1000 / 60 / 60;// 得到时
        minute = mesc / 1000 / 60;// 得到分钟
        second = mesc / 1000 % 60;// 得到秒

        if (minute < 10) {
            minStr = "0" + minute;
        } else {
            minStr = minute + "";
        }

        if (second < 10) {
            secStr = "0" + second;
        } else {
            secStr = second + "";
        }

        if (hour > 0 && hour < 10) {
            hours = "0" + hour;
        } else if (hour >= 10) {
            hours = "" + hours;
        } else {
            hours = "00";
        }
        return hours + ":" + minStr + ":" + secStr;

    }


    /**
     * 格式化大小
     *
     * @param mesc
     * @return
     */
    public static String formatSize(long mesc) {

        long start;
        double end;
        String msize = "";
        start = (mesc / 1024 / 1024);
        end = (double) (mesc / 1024 % 1024) / 1024;
        msize = end + "";
        msize = msize.substring(2, 4);

        return start + "." + msize;

    }


}
