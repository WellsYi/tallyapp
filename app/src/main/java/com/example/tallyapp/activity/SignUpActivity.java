package com.example.tallyapp.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tallyapp.R;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.utils.ShowDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {
    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    private Button backButton, signupButton;
    private EditText userName, name, password, repeatPassword;
    private ShowDialog showDialog;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //初始化
        initView();

        //注册用户
        signup();

        //返回键返回登录界面
        backToLogin();

    }

    private void initView(){
        backButton = findViewById(R.id.back);
        signupButton = findViewById(R.id.signinButton);
        userName = findViewById(R.id.userNEditText);
        name = findViewById(R.id.nameEditText);
        password = findViewById(R.id.userPEditText);
        repeatPassword = findViewById(R.id.repeatPasswordEditText);
        dbHelper = new DBHelper(SignUpActivity.this);
        db = dbHelper.getWritableDatabase();
        showDialog = new ShowDialog(SignUpActivity.this);
    }

    private void backToLogin(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signup(){
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InformationJudgment(name.getText().toString(), userName.getText().toString(), password.getText().toString(), repeatPassword.getText().toString())){
                    return;
                }
                else{
                    ContentValues values = new ContentValues();
                    values.put("name", name.getText().toString());
                    values.put("username", userName.getText().toString());
                    values.put("password", password.getText().toString());
                    values.put("registration_date", getCurrentBeijingDateTime()); //获取时间
                    long newRowID = db.insert("users", null, values);
                    if(newRowID != -1){
                        Toast.makeText(SignUpActivity.this, "注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(SignUpActivity.this, "注册失败",Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        });

    }
    //获取时间
    private String getCurrentBeijingDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }


    public static boolean containsAlphanumericCombination(String inputString) {
        // 正则表达式模式
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";

        // 使用Pattern和Matcher来进行匹配
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(inputString);

        return matcher.find();
    }

    private boolean InformationJudgment(String name, String userName, String password, String repeatPassword){
        //判断条件
        //判断用户是否已经注册
        if(isexist(userName)){
            showDialog.showDialog("用户已存在！");
            return false;
        }

        //填写是否规范
        if(password == null || password.equals("") || name == null || name.equals("") || userName == null || userName.equals("")){
            showDialog.showDialog("昵称、用户名、密码不能为空");
            return false;
        }
        if(password.length() < 8 || password.length() > 16){
            showDialog.showDialog("密码长度应是8-16");
            return false;
        }
        if(!containsAlphanumericCombination(password)) {
            showDialog.showDialog("密码应包含大小写字母与数字");
            return false;
        }

        if (!password.equals(repeatPassword)){
            //Toast.makeText(MainActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
            showDialog.showDialog("两次密码不一致");
            return false;
        }
        return true;
    }

    private boolean isexist(String userName){
        Cursor cursor = db.rawQuery("select * from users where username= ?", new String[]{userName});
        if(cursor.getCount() == 0){
            return false;
        }
        return true;
    }

}