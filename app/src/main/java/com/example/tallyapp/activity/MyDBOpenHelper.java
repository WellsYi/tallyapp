package com.example.tallyapp.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDBOpenHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "student.db";
    private static final int VERSION = 1;

    public MyDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table sign_up_info(id INTEGER primary key autoincrement, username varchar(20), password varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
