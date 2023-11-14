package com.example.tallyapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tallyapp.MainActivity;
import com.example.tallyapp.R;
import com.example.tallyapp.dao.UserDao;
import com.example.tallyapp.entity.User;
import com.example.tallyapp.utils.Dialog;

import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    private MyDBOpenHelper mhelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.signinButton);
        Button signupButton = findViewById(R.id.signupButton);
        CheckBox remenberBox = findViewById(R.id.remenber_box);
        CheckBox autologin = findViewById(R.id.autologin_box);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                //判断是否为注册数据中的用户
                if(username == null || "".equals(username)){
                    dialog.showDialog("用户名不能为空！");
                    return;
                }
                if(password == null || "".equals(password)){
                    dialog.showDialog("密码不能为空");
                    return;
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            User user = UserDao.getUser(username);
                            if(user.getRemember() == 1){
                                usernameEditText.setText(username);
                                passwordEditText.setText(password);
                                remenberBox.setChecked(true);
                            }
                            else {
                                usernameEditText.setText("");
                                passwordEditText.setText("");
                                remenberBox.setChecked(false);
                            }
                            if(user != null && user.getPassword().equals(password) && remenberBox.isChecked()){
                                UserDao.updateUser(user);
                                //登录成功
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }).start();

                //验证是否为已注册用户

//                    showDialog("登录成功！");
                //保存信息

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


}