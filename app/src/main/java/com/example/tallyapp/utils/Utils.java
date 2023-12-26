package com.example.tallyapp.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.tallyapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    private Context context;
    private DialogCallback dialogCallback;

    public Utils(Context context) {
        this.context = context;
    }

    public void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
        TextView textViewMessage = dialogView.findViewById(R.id.textViewMessage);
        textViewMessage.setText(msg);
        builder.setView(dialogView);

        // 2. 设置对话框相关信息
        builder.setTitle("提示");
        //builder.setMessage(msg);

        /* 3. 设置确认按钮 */
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 在这里添加处理“确认”按钮点击事件的代码
            }
        });

        // 4. 设置是否可以通过点击背景取消对话框
        builder.setCancelable(false); // 如果想要允许取消，则设置为 true

        // 5. 显示 AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showAboutDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
        TextView textViewMessage = dialogView.findViewById(R.id.textViewMessage);
        textViewMessage.setText(msg);
        builder.setView(dialogView);

        // 2. 设置对话框相关信息
        builder.setTitle("关于");
        //builder.setMessage(msg);

        /* 3. 设置确认按钮 */
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 在这里添加处理“确认”按钮点击事件的代码
                if (dialogCallback != null) {
                    dialogCallback.onConfirm(); // 触发接口方法
                }
            }
        });

        // 4. 设置是否可以通过点击背景取消对话框
        builder.setCancelable(true); // 如果想要允许取消，则设置为 true

        // 5. 显示 AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setDialogCallback(DialogCallback dialogCallback) {
        this.dialogCallback = dialogCallback;
    }

    public interface DialogCallback {
        void onConfirm();
    }
    public void showDialog(String msg, DialogCallback confirmCallback, DialogCallback cancelCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_layout, null);
        TextView textViewMessage = dialogView.findViewById(R.id.textViewMessage);
        textViewMessage.setText(msg);
        builder.setView(dialogView);

        // 2. 设置对话框相关信息
        builder.setTitle("提示");

        /* 3. 设置确认按钮 */
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (confirmCallback != null) {
                    confirmCallback.onConfirm(); // 触发确认操作回调
                }
            }
        });

        /* 4. 设置取消按钮 */
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (cancelCallback != null) {
                    cancelCallback.onConfirm(); // 触发取消操作回调
                }
            }
        });

        // 5. 设置是否可以通过点击背景取消对话框
        builder.setCancelable(false); // 如果想要允许取消，则设置为 true

        // 6. 显示 AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //获取收入类型
    public ArrayList<String> getIncomeTypes(SQLiteDatabase db) {
        ArrayList<String> IncomeTypesList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT typename FROM income_type", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String typeName = cursor.getString(cursor.getColumnIndex("typename"));
                IncomeTypesList.add(typeName);
            } while (cursor.moveToNext());
        }

        return IncomeTypesList;
    }

}
