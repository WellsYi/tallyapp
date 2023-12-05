package com.example.tallyapp.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context){
        super(context, "user_info", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table users(" +
                "id Integer primary key autoincrement, " +
                "name varchar(20) not null," +
                "username varchar(20) not null," +
                "password varchar(20) not null," +
                "remenber Integer default 0 not null, " +
                "registration_date DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 3) {
            try {
                // 删除旧的users表
                db.execSQL("DROP TABLE IF EXISTS users");

                // 创建新的users表
                String sql = "create table users(" +
                        "id Integer primary key autoincrement, " +
                        "name varchar(20) not null," +
                        "username varchar(20) not null," +
                        "password varchar(20) not null," +
                        "remenber Integer default 0 not null, " +
                        "registration_date DATETIME DEFAULT CURRENT_TIMESTAMP)";
                db.execSQL(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
