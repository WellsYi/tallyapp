package com.example.tallyapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.tallyapp.R;

public class ShowDialog {
    private Context context;

    public ShowDialog(Context context) {
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
            }
        });

        // 4. 设置是否可以通过点击背景取消对话框
        builder.setCancelable(true); // 如果想要允许取消，则设置为 true

        // 5. 显示 AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
