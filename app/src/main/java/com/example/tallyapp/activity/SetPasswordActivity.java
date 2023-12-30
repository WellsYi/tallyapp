package com.example.tallyapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tallyapp.R;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPasswordActivity extends AppCompatActivity {
    private Button backButton, confirmButtonh;
    private TextView usernameTextView;
    private EditText passwordEditText, newpasswordEditText, repeatnewpasswordEditText;
    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String username;
    private Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        initView();
        getAndConfirmInformation();
        back();
    }

    private void initView(){
        backButton = findViewById(R.id.backButton);
        confirmButtonh = findViewById(R.id.confirmButton);
        usernameTextView = findViewById(R.id.usernameTextView);
        passwordEditText = findViewById(R.id.passwordEditText);
        newpasswordEditText = findViewById(R.id.newpasswordEditText);
        repeatnewpasswordEditText = findViewById(R.id.repeatnewpasswordEditText);
        dbHelper = new DBHelper(SetPasswordActivity.this);
        db = dbHelper.getWritableDatabase();
        sharedPref = getSharedPreferences("user_info", 0);
        editor = sharedPref.edit();
        username = sharedPref.getString("username", "");
        utils = new Utils(SetPasswordActivity.this);
        usernameTextView.setText(username);
    }

    private void back(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("Range")
    private void getAndConfirmInformation(){
        confirmButtonh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = passwordEditText.getText().toString();
                String newPassword = newpasswordEditText.getText().toString();
                String repeatNewPassword = repeatnewpasswordEditText.getText().toString();
                String password = null;
                //从数据库中获取密码并确认
                String sql = "SELECT password FROM users WHERE username = ?";
                Cursor cursor = db.rawQuery(sql, new String[]{username});
                if (cursor.moveToFirst()) {
                    password = cursor.getString(cursor.getColumnIndex("password"));
                    if (oldPassword.equals("") || oldPassword == null || !oldPassword.equals(password)){
                        utils.showDialog("原密码不正确");
                    } else if (newPassword.length() < 8 || newPassword.length() > 16 || !containsAlphanumericCombination(newPassword)) {
                        utils.showDialog("密码必须是8-16位的大小写英文、数字结合(不能是纯数字)");
                    } else if (!newPassword.equals(repeatNewPassword)) {
                        utils.showDialog("两次填写的密码不一致");
                    }else {
                        ContentValues values = new ContentValues();
                        values.put("password", newPassword);
                        int rowsAffected = db.update("users", values, "username = ?", new String[] { username});
                        if (rowsAffected > 0) {
                            // 更新成功
                            Toast.makeText(SetPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 更新失败
                            Toast.makeText(SetPasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
                
            }
        });
        
    }
    public static boolean containsAlphanumericCombination(String inputString) {
        // 正则表达式模式
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";

        // 使用Pattern和Matcher来进行匹配
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(inputString);

        return matcher.find();
    }

}