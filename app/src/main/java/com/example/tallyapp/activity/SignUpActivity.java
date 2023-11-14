package com.example.tallyapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.example.tallyapp.R;
import com.example.tallyapp.dao.UserDao;
import com.example.tallyapp.entity.User;

public class SignUpActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private MyDBOpenHelper mhelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button backButton = findViewById(R.id.back);
        Button signupButton = findViewById(R.id.signinButton);
        EditText accountET = findViewById(R.id.userNEditText);
        EditText nameET = findViewById(R.id.nameEditText);
        EditText pwdET = findViewById(R.id.userPEditText);
        EditText repwdET = findViewById(R.id.userPAgainEditText);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = accountET.getText().toString();
                String password = pwdET.getText().toString();
                String repwd = repwdET.getText().toString();
                String name = nameET.getText().toString();
                //检查信息是否合法
//                if (!InformationJudgment(username, password, repwd)) {
//                    return;
//                }

                //存储用户信息
                User user = new User(name, username,password);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UserDao.insertUser(user);
//                            Toast.makeText()
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
                //跳转至登录界面


            }
        });

    }
    public void showDialog(String msg){
        //1、创建AlertDialog.Builder对象
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        //2、设置提示窗口相关信息
        builder.setTitle("提示");
        builder.setMessage(msg);//提示信息
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setCancelable(false);//点击空白区域不能被关掉  true能被关掉
        builder.show();//显示提示框
    }

    public static boolean containsAlphanumericCombination(String inputString) {
        // 正则表达式模式
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";

        // 使用Pattern和Matcher来进行匹配
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(inputString);

        return matcher.find();
    }

    public boolean InformationJudgment(String account, String pwd, String repwd){
        //判断条件
        //判断用户名是否为空
        if(account != null && !"".equals(account)){
            if(account.equals(sp.getString("account", ""))){
                showDialog("用户已存在");
                return false;
            }
        }else {
            //用户名为空
            showDialog("用户名不能为空");
            return false;
        }

        if(pwd == null || pwd.equals("")){
            showDialog("密码不能为空");
            return false;
        }
        if(pwd.length() < 8 || pwd.length() > 16){
            showDialog("密码长度应是8-16");
            return false;
        }
        if(!containsAlphanumericCombination(pwd)) {
            showDialog("密码应包含大小写字母与数字");
            return false;
        }

        if (!pwd.equals(repwd)){
            //Toast.makeText(MainActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
            showDialog("两次密码不一致");
            return false;
        }

        return true;
    }

}