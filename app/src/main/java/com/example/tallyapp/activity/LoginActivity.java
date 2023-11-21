package com.example.tallyapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.tallyapp.MainActivity;
import com.example.tallyapp.R;

import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.entity.Users;
import com.example.tallyapp.utils.ShowDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    private ShowDialog showDialog;
    private EditText usernameET, passwordET;
    private Button loginButton, signupButton;

    private CheckBox remenberBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化
        initView();

        //是否记住密码？

        checkRight();
        //注册
        signup();

        //登录
        login();
    }

    private void initView(){
        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.signinButton);
        signupButton = findViewById(R.id.signupButton);
        remenberBox = findViewById(R.id.remenber_box);
        dbHelper = new DBHelper(LoginActivity.this);
        db = dbHelper.getWritableDatabase();
        showDialog = new ShowDialog(LoginActivity.this);


    }

    private void signup(){
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();
                if(!InformationJudgment(username, password)){
                    Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateTable(username);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginActivity.this, "欢迎登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //验证信息是否合法
    private boolean InformationJudgment(String username, String password){
        //判断是否为空
        if(username == null || password == null || username.equals("") || username.equals("")){
            showDialog.showDialog("密码、用户名不能为空！");
//            Toast.makeText(LoginActivity.this, "密码、用户名不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        //判断用户是否存在
        if(isexist(username)){
            //如果存在判断密码是否正确
            if(isPasswordMatch(username, password)){
                return true;
            }else {
                showDialog.showDialog("密码不正确！");
//                Toast.makeText(LoginActivity.this, "密码不正确！", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            showDialog.showDialog("用户不存在！");
//            Toast.makeText(LoginActivity.this, "用户不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isexist(String userName){
        Cursor cursor = db.rawQuery("select * from users where username= ?", new String[]{userName});
        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private boolean isPasswordMatch(String userName, String password){
        Cursor cursor = db.rawQuery("SELECT password FROM users WHERE username = ?", new String[]{userName});

        if(cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String passwordFromDB = cursor.getString(cursor.getColumnIndex("password"));
            cursor.close();

            // 检查输入的密码与数据库中密码是否匹配
            return password.equals(passwordFromDB);
        }

        return false;
    }


    private void updateTable(String username){
        ContentValues values = new ContentValues();
        if(remenberBox.isChecked()) {
            values.put("remenber", 1);
        }
        else {
            values.put("remenber", 0);
        }

        // 更新特定用户的信息
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update("users", values, "username = ?", new String[]{username});
        db.close();
    }



    @SuppressLint({"Range", "ClickableViewAccessibility"})
    private void checkRight() {
        usernameET.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (usernameET.getRight() - usernameET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    ListPopupWindow listPopupWindow = new ListPopupWindow(LoginActivity.this);

                    // 获取带有 remenber 标记的用户列表
                    ArrayList<Users> userList = getUsersWithRemenberFlag();

                    // 创建适配器
                    ArrayAdapter<Users> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_list_item_1, userList);
                    listPopupWindow.setAdapter(adapter);

                    // 设置列表项点击事件
                    listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
                        Users selectedUser = userList.get(position);
                        String selectedUsername = selectedUser.getUsername();

                        // 设置选定的用户名到 usernameET
                        usernameET.setText(selectedUsername);
                        // 设置对应的密码到 passwordET
                        passwordET.setText(selectedUser.getPassword());

                        listPopupWindow.dismiss();
                    });

                    // 设置ListPopupWindow的锚点及显示位置
                    listPopupWindow.setAnchorView(usernameET);
                    listPopupWindow.setDropDownGravity(Gravity.END);

                    // 显示ListPopupWindow
                    listPopupWindow.show();
                    return true;
                }
            }
            return false;
        });
    }



    @SuppressLint("Range")
    private ArrayList<Users> getUsersWithRemenberFlag() {
        ArrayList<Users> userList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"username", "password"}, "remenber = ?", new String[]{"1"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                Users user = new Users(username, password);
                userList.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return userList;
    }


}