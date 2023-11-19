package com.example.tallyapp.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Dialog {
    private Context context;

    public Dialog(Context context) {
        this.context = context;
    }

    public void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 2. Set dialog-related information
        builder.setTitle("提示");
        builder.setMessage(msg);

        /* 3. Set positive button */
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Add your code to handle the "确认" button click event here
            }
        });

        // 4. Set whether the dialog can be dismissed by clicking on the background
        builder.setCancelable(false); // If you want it to be dismissible, set it to true

        // 5. Show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

