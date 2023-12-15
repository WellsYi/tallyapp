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
        super(context, "user_info", null, 5);
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
        if (oldVersion == 4) {
            try {
                // 删除旧的users表
                db.execSQL("DROP TABLE IF EXISTS users");

                String sql = "create table users(" +
                        "id Integer primary key autoincrement, " +
                        "name varchar(20) not null," +
                        "username varchar(20) not null," +
                        "password varchar(20) not null," +
                        "remenber Integer default 0 not null, " +
                        "registration_date DATETIME DEFAULT CURRENT_TIMESTAMP)";
                db.execSQL(sql);

                // 创建新的花销类型表
                String sql_expense_types = "create table expense_type(" +
                        "id Integer primary key autoincrement, " +
                        "typename varchar(20) not null)";
                db.execSQL(sql_expense_types);

                //创建花销表
                String sql_expense =  "create table expense(" +
                        "id Integer primary key autoincrement, " +
                        "userID Integer, " +
                        "expensetypeID Integer, " +
                        "Amount DECIMAL(10, 2) not null," +
                        "Date DATETIME not null," +
                        "foreign key (expensetypeID) references expense_type(id)," +
                        "foreign key (userID) references users(id))";
                db.execSQL(sql_expense);

                //创建收入类型表
                String sql_income_types = "create table income_type(" +
                        "id Integer primary key autoincrement, " +
                        "typename varchar(20) not null)";
                db.execSQL(sql_income_types);

                //创建收入表
                String sql_income =  "create table income(" +
                        "id Integer primary key autoincrement, " +
                        "userID Integer, " +
                        "incometypeID Integer, " +
                        "Amount DECIMAL(10, 2) not null," +
                        "Date DATETIME not null," +
                        "foreign key (incometypeID) references income_type(id)," +
                        "foreign key (userID) references users(id))";
                db.execSQL(sql_income);

                //传入花销类型数据
                String sql_insert_expense_types = "INSERT INTO expense_type(typename) VALUES " +
                        "('餐饮'), ('购物'), ('日用'), ('交通'), ('水果'), ('零食'), ('运动'), ('娱乐'), " +
                        "('服装'), ('住房'), ('居家'), ('旅行'), ('烟酒'), ('数码'), ('汽车'), ('学习'), " +
                        "('宠物'), ('礼物')";
                db.execSQL(sql_insert_expense_types);

                //传入收入类型数据
                String sql_insert_income_types = "INSERT INTO income_type(typename) VALUES " +
                        "('工资'), ('兼职'), ('理财'), ('礼金'), ('其他')";
                db.execSQL(sql_insert_income_types);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
