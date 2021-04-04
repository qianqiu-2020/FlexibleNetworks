package com.example.flexiblenetworks.database;
import android.content.Context;
import android.database.sqlite.*;

import androidx.annotation.Nullable;

/*
* DBHelper 数据库帮助类，用于连接和创建数据库，同时定义了若干个SQL语句
* */

public class DBHelper extends SQLiteOpenHelper {

    /*public static final String CREATE_TABLE_User =
            "create table user (" +
                    "user_id integer primary key, " +
                    "user_name varchar(45), " +
                    "user_imageId integer); ";*/

    public static final String CREATE_TABLE_Friends =
            "create table friends ( " +
                    "friend_id integer, " +
                    "friend_name varchar(45), " +
                    "friend_imageId integer, " +
                    //"friend_type integer default 0, " +/*0代表该id为好友id,1代表该id为群聊id,默认为0*/
                    "friend_group integer default 0, " +
                    "primary key(friend_id)); " ;

    public static final String CREATE_TABLE_ChatFile(long friend_id){
        String sql= "create table "+"chatFile_"+friend_id+" (" +
                    "message_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sender_id integer, " +
                    "receiver_id integer, " +
                    "send_time timestamp(6) DEFAULT(datetime('now','localtime')), " +
                    "message_type integer, " +
                    "message nvarchar(300))";
                    //"primary key(sender_id, receiver_id, send_time)); ";
        return sql;
    }

    public static final String CREATE_TABLE_Group =
            "create table groups ( " +
                    "group_id integer auto increment, " +
                    "group_name varchar(45), " +
                    "group_imageId integer, " +
                    "primary key(group_id)); ";

    private Context mContext;
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    /*创建数据库默认创建好友表*/
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Friends);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
    public void add_friend(SQLiteDatabase db, int friend_id, String friend_name,int friend_imageId)
    {
        try {
            ContentValues values = new ContentValues();
            values.put("friend_id", friend_id);
            values.put("friend_name", friend_name);
            values.put("friend_imageId", friend_imageId);
            db.insert("friends", null, values);
            db.execSQL(CREATE_TABLE_ChatFile(friend_id));//自动新建一个聊天记录表
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_chatFile(SQLiteDatabase db, int sender_id, int receiver_id, String message)
    {
        try {
            ContentValues values = new ContentValues();
            values.put("sender_id", sender_id);
            values.put("receiver_id", receiver_id);
            values.put("message", message);
            int friend_id;
            if (sender_id == BaseActivity.user_id)
                friend_id = receiver_id;
            else {
                friend_id = sender_id;
            }
            db.insert("chatFile_" + friend_id, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete_friend(SQLiteDatabase db, int friend_id, boolean deleteChatFile)
    {
        db.delete("friends","friend_id=?",new String[]{friend_id+""});
        if(deleteChatFile)
        {
            db.execSQL("drop table "+"chatFile_"+friend_id);
        }
    }
    public void delete_friend(SQLiteDatabase db, int friend_id)
    {
        boolean deleteChatFile=false;
        db.delete("friends","friend_id=?",new String[]{friend_id+""});
        if(deleteChatFile)
        {
            db.execSQL("drop table "+"chatFile_"+friend_id);
        }
    }

    public void search_chatFile(SQLiteDatabase db,int friend_id)
    {
        Cursor cursor = db.query("chatFile"+friend_id,null,null,null,null,null,"send_time desc");
        if(cursor.moveToFirst())
        {
            do{
                int sender_id = cursor.getInt(cursor.getColumnIndex("sender_id"));
                int receiver_id = cursor.getInt(cursor.getColumnIndex("receiver_id"));
                String send_time = cursor.getString(cursor.getColumnIndex("send_time"));
                int message_type = cursor.getInt(cursor.getColumnIndex("message_type"));
                String message = cursor.getString(cursor.getColumnIndex("message"));
            }while(cursor.moveToNext());
        }
        cursor.close();
    }
    public void search_friends(SQLiteDatabase db)
    {
        Cursor cursor = db.query("friends",null,null,null,null,null,null);
        if(cursor.moveToFirst())
        {
            do{
                int friend_id = cursor.getInt(cursor.getColumnIndex("friend_id"));
                String friend_name = cursor.getString(cursor.getColumnIndex("friend_name"));
                int friend_imageId = cursor.getInt(cursor.getColumnIndex("friend_imageId"));
                int friend_group = cursor.getInt(cursor.getColumnIndex("friend_group"));
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    public void update_friends_name(SQLiteDatabase db, int friend_id, String new_friend_name)
    {
        ContentValues values = new ContentValues();
        values.put("friend_name",new_friend_name);
        db.update("friends",values,"friend_id = ?",null);
    }
    public void update_friend_imageId(SQLiteDatabase db)
    {

    }*/
}
