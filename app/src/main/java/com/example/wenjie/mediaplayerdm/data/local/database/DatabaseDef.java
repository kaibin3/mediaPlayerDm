package com.example.wenjie.mediaplayerdm.data.local.database;

/**
 * Created by wen.jie on 2018/1/3.
 */

public class DatabaseDef {

    public static final String NAME = "media.db";
    public static final int VERSION = 1;


    public class MediaInfoColumn {
        public String ID = "id";
        public String NAME = "media_name";
        public String DATA = "media_data";
        public String SIZE = "media_size";
        public String DURATE = "media_durate";
        public String RECORD = "media_recode";
    }

}
