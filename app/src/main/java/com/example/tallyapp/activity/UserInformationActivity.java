package com.example.tallyapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tallyapp.R;
import com.example.tallyapp.dbhelper.DBHelper;
import com.example.tallyapp.fragment.Accounting;
import com.example.tallyapp.fragment.Statistics;
import com.example.tallyapp.fragment.UserInfo;
import com.example.tallyapp.utils.Utils;

import java.util.zip.Inflater;

public class UserInformationActivity extends AppCompatActivity {
    private Button backButton, logoutButton;
    private TextView idTextView, registdateTextView, usernameTextView, genderTextView, nameTextView, phonenumberTextView;
    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String username;
    private String newName, newGender, newPhoneNumber;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        initView();
        setInitUserInformation();
        setInformation();
        setLogoutButton();
        back();

    }

    private void initView(){
        backButton = findViewById(R.id.backButton);
        idTextView = findViewById(R.id.idTextView);
        registdateTextView = findViewById(R.id.registdateTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        genderTextView = findViewById(R.id.genderTextView);
        nameTextView = findViewById(R.id.nameTextView);
        phonenumberTextView = findViewById(R.id.phonenumberTextView);
        dbHelper = new DBHelper(UserInformationActivity.this);
        db = dbHelper.getWritableDatabase();
        sharedPref = getSharedPreferences("user_info", 0);
        editor = sharedPref.edit();
        username = sharedPref.getString("username", "");
        utils = new Utils(UserInformationActivity.this);
        logoutButton = findViewById(R.id.logoutButton);
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
    private void setInitUserInformation() {
        // 根据用户名查询数据库
        String query = "SELECT * FROM users WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            // 将查询到的信息设置到视图组件中
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String gender = cursor.getString(cursor.getColumnIndex("gender"));
            String phonenumber = cursor.getString(cursor.getColumnIndex("phonenumber"));
            String registration_date = cursor.getString(cursor.getColumnIndex("registration_date"));

            idTextView.setText("id:  " + String.valueOf(id));
            nameTextView.setText("昵称:  " + name);
            usernameTextView.setText("用户名：  " + username);
            // 不显示密码
            genderTextView.setText("性别:  " + gender);
            phonenumberTextView.setText("手机号:  " + phonenumber);
            registdateTextView.setText("注册时间:  " + registration_date);
        }

        // 关闭游标
        cursor.close();
    }

    //修改信息并更新到数据库，只有昵称、性别和手机号可以修改
    @SuppressLint("MissingInflatedId")
    private void setInformation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInformationActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_setname, null);
        View view = inflater.inflate(R.layout.dialog_setphonenumber, null);
        TextView reWriteInEditText = dialogView.findViewById(R.id.rewriteEditText);
        TextView reWriteInNumberText = view.findViewById(R.id.rewriteEditText);

        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogView.getParent() != null) {
                    ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                }

                builder.setView(dialogView)
                        .setTitle("昵称")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 处理保存逻辑
                                newName = reWriteInEditText.getText().toString();
                                if (newName.length() >= 2 && newName.length() <= 11) {
                                    // 如果是11位数字，执行保存逻辑
                                    nameTextView.setText("昵称：  " + newName);
                                    ContentValues values = new ContentValues();
                                    values.put("name", newName);
                                    // 执行更新操作
                                    db.update("users", values, "username = ?", new String[] { username});
                                } else {
                                    // 如果不是11位数字，提示用户输入正确的格式
                                    // 可以使用Toast.makeText()来显示提示信息
                                    utils.showDialog("昵称长度必须在2到11个字符之间");
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });

        AlertDialog.Builder genderBuilder = new AlertDialog.Builder(UserInformationActivity.this);
        genderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderBuilder.setTitle("性别");
                String[] genders = {"男", "女"};
                genderBuilder.setItems(genders, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String newGender = genders[which];
                        genderTextView.setText("性别：  " + newGender);

                        // 更新到数据库
                        ContentValues values = new ContentValues();
                        values.put("gender", newGender);
                        int rowsAffected = db.update("users", values, "username = ?", new String[] { username});

                        if (rowsAffected > 0) {
                            // 更新成功
                            Toast.makeText(UserInformationActivity.this, "性别更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            // 更新失败
                            Toast.makeText(UserInformationActivity.this, "性别更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                genderBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = genderBuilder.create();
                dialog.show();
            }
        });

        AlertDialog.Builder numberbuilder = new AlertDialog.Builder(UserInformationActivity.this);
        phonenumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getParent() != null) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
                numberbuilder.setView(view)
                        .setTitle("手机号")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newPhoneNumber = reWriteInNumberText.getText().toString();
                                //逻辑判断
                                //是否是11位纯数字号码

                                if (newPhoneNumber.matches("\\d{11}")) {
                                    // 如果是11位数字，更新TextView
                                    phonenumberTextView.setText("手机号：  " + newPhoneNumber);
                                    ContentValues values = new ContentValues();
                                    values.put("phonenumber", newPhoneNumber);
                                    // 执行更新操作
                                    db.update("users", values, "username = ?", new String[] { username});
                                } else {
                                    // 如果不是11位数字，提示用户输入正确的格式
                                    // 可以使用Toast.makeText()来显示提示信息
                                    utils.showDialog("请输入11位数字的手机号");
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = numberbuilder.create();
                        dialog.show();
            }
        });

    }

    private void setLogoutButton(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用 showDialog 方法显示一个带有自定义布局的对话框
                utils.showDialog("注销账户后数据将无法恢复！",
                        new Utils.DialogCallback() {
                            @Override
                            public void onConfirm() {
                                //清除数据
                                String deleteUserName = username;
                                String deleteUserQuery = "DELETE FROM users WHERE username = ?";
                                String deleteExpensesQuery = "DELETE FROM expense WHERE userID IN (SELECT id FROM users WHERE username = ?)";
                                String deleteIncomesQuery = "DELETE FROM income WHERE userID IN (SELECT id FROM users WHERE username = ?)";
                                //删除用户支出信息
                                db.execSQL(deleteExpensesQuery, new String[]{deleteUserName});
                                //删除用户收入信息
                                db.execSQL(deleteIncomesQuery, new String[]{deleteUserName});
                                //删除用户信息
                                db.execSQL(deleteUserQuery, new String[]{deleteUserName});
                                utils.showDialog("注销成功！");
                                //退出登录
                                Intent intent = new Intent(UserInformationActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        },
                        new Utils.DialogCallback() {
                            @Override
                            public void onConfirm() {
                                // 取消操作的逻辑，可以为空或者添加相应的处理
                            }
                        }
                );
            }
        });
    }

}