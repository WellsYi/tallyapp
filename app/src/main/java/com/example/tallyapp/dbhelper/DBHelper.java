package com.example.tallyapp.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context){
        super(context, "user_info", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table users(" +
                "id Integer primary key autoincrement, " +
                "name varchar(20) not null," +
                "username varchar(20) not null," +
                "password varchar(20) not null," +
                "remenber Integer default 0 not null)" ;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == 1){
            try {
                db.execSQL("ALTER TABLE users ADD COLUMN latestLogin INTEGER DEFAULT 0");
                db.execSQL("UPDATE users SET latestLogin = 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
