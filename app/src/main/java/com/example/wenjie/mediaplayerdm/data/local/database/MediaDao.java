package com.example.wenjie.mediaplayerdm.data.local.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.wenjie.mediaplayerdm.data.model.Media;

import java.util.ArrayList;
import java.util.List;

public class MediaDao {
    private DBHelp dbhelp = null;
    private SQLiteDatabase database = null;
    private ContentResolver receiver = null;
    private Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private List<Media> mList = null;

    public MediaDao(Context context) {
        dbhelp = new DBHelp(context);
        receiver = context.getContentResolver();
    }

    /**
     * 获取系统的数据库的数据，并把它放到自建的数据库中
     */
    public void getSerivesMedia() {
        database = dbhelp.getWritableDatabase();
        String[] pro = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = receiver.query(uri, pro, null, null, null);
        while (cursor.moveToNext()) {
            Integer id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
            String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            long durate = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
            if (isExit(id)) {

            } else {
                ContentValues values = new ContentValues();
                values.put("_id", id);
                values.put("name", name);
                values.put("data", date);
                values.put("durate", durate);
                values.put("size", size);
                database.insert("media", null, values);
            }
        }
        cursor.close();
        database.close();
    }


    /**
     * 根据id删除 数据库的文件
     *
     * @param id
     */
    public void delete(int id) {
        database = dbhelp.getWritableDatabase();
        database.delete("media", "_id=?", new String[]{id + ""});
        database.close();
    }


    /**
     * 从自建的数据库获取播放文件
     *
     * @return List<Media>
     */
    public List<Media> getAllMedia() {
        database = dbhelp.getReadableDatabase();
        mList = new ArrayList<Media>();
        Cursor cursor = database.query("media", new String[]{"_id", "name",
                "data", "size", "durate", "recode"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String path = cursor.getString(cursor.getColumnIndex("data"));
            long size = cursor.getLong(cursor.getColumnIndex("size"));
            long durate = cursor.getLong(cursor.getColumnIndex("durate"));
            long record = cursor.getLong(cursor.getColumnIndex("recode"));
            mList.add(new Media(name, durate, size, record, path, id));
        }
        cursor.close();
        database.close();
        return mList;

    }


    /**
     * 更行数据库播放记录
     *
     * @param id
     * @param CurrentPosition
     */
    public void updateRecode(int id, int CurrentPosition) {
        database = dbhelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        double recode = CurrentPosition;
        values.put("recode", recode);
        database.update("media", values, "_id=?",
                new String[]{id + ""});
        database.close();

    }


    /**
     * 更具id获取 Media
     *
     * @param id
     * @return
     */
    public Media getMedia(int id) {
        Media media = null;
        database = dbhelp.getReadableDatabase();

        Cursor cursor = database.query("media", new String[]{"_id", "name",
                        "data", "size", "durate", "recode"}, "_id=?", new String[]{id + ""}, null,
                null, null);
        if (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            String _name = cursor.getString(cursor.getColumnIndex("name"));
            String path = cursor.getString(cursor.getColumnIndex("data"));
            long size = cursor.getLong(cursor.getColumnIndex("size"));
            long durate = cursor.getLong(cursor.getColumnIndex("durate"));
            long record = cursor.getLong(cursor.getColumnIndex("recode"));
            media = new Media(_name, durate, size, record, path, _id);

        }
        cursor.close();
        database.close();
        return media;
    }


    /**
     * 更具id获取判断 数据库中是否存在该文件
     *
     * @param id
     * @return
     */
    public boolean isExit(int id) {
        database = dbhelp.getWritableDatabase();

        Cursor cursor = database.query("media", new String[]{"_id", "name",
                        "data", "size", "durate", "recode"}, "_id=?", new String[]{id + ""}, null,
                null, null);

        if (cursor.moveToNext()) {
            return true;
        } else {
            return false;
        }


    }
}
