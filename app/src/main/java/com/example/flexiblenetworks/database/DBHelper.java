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

}
