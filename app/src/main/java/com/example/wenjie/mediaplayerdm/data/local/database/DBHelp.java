package com.example.wenjie.mediaplayerdm.data.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelp extends SQLiteOpenHelper {


    /**
     * DBHelp构造方法
     *
     * @param context
     */
    public DBHelp(Context context) {
        super(context, DatabaseDef.NAME, null, DatabaseDef.VERSION);
    }


    /**
     * 当第一调用getWritableDatabase() or getReadableDatabase()方法的时候会执行
     * onCreate的方法，主要作用就是用来创建表结构
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table media" +
                "(_id integer primary key autoincrement," +
                "name varchar(20) not null," +
                "data varchar(50)," +
                "size double," +
                "durate double," +
                "recode double)");
    }


    /**
     * 当安装的时候发现数据库版本号不一致，那么这个方法会被调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
