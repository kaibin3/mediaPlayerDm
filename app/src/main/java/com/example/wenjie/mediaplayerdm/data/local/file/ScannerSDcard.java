package com.example.wenjie.mediaplayerdm.data.local.file;


import android.annotation.TargetApi;
import android.os.Build;

import com.example.wenjie.mediaplayerdm.data.model.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ScannerSDcard {

    private static List<Files> fileList = new ArrayList<Files>();

    /**
     * 扫描sdcard来获取文件和文件夹
     *
     * @param file
     * @return
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static List<Files> findFiles(File file) {

        if (file != null) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {

                    if (f.isFile()) {    // 假设是文件
                        String f_name = "文件 " + f.getName();
                        String f_path = f.getAbsolutePath();
                        fileList.add(new Files(f_name, f_path));

                    } else {             // 是文件夹
                        String fs_name = "文件夹" + f.getName();
                        String fs_path = f.getAbsolutePath();
                        fileList.add(new Files(fs_name, fs_path));

                    }
                }
            }
        }
        return fileList;

    }


}
