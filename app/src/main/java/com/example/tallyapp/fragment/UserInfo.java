package com.example.tallyapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tallyapp.R;
import com.example.tallyapp.activity.LoginActivity;
import com.example.tallyapp.activity.UserInformationActivity;
import com.example.tallyapp.dbhelper.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String day, times;
    private String mParam2;
    private ListView listView1, listView2;
    private SharedPreferences sharedPref;
    private String username;
    private TextView nameText, daysText, timesText;

    private DBHelper dbHelper; //定义数据库帮助类对象
    private SQLiteDatabase db;
    private Button exitButton;

    public UserInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInfo newInstance(String param1, String param2) {
        UserInfo fragment = new UserInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //初始化
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        initView(view);
        //退出
        exit();
       //获取信息
        getName_Days_Times();
        //获取列表信息
        getListInfo();

        return view;
    }

    private void initView(View view){
        listView1 = view.findViewById(R.id.setting_list);
        listView2 = view.findViewById(R.id.setting_list1);
        nameText = view.findViewById(R.id.nameTextView);
        daysText = view.findViewById(R.id.daysTextView);
        timesText = view.findViewById(R.id.timesTextView);
        daysText = view.findViewById(R.id.daysTextView);
        timesText = view.findViewById(R.id.timesTextView);
        exitButton = view.findViewById(R.id.exitButton);
        sharedPref = requireContext().getSharedPreferences("user_info", 0);
        username = sharedPref.getString("username", "");
        dbHelper = new DBHelper(UserInfo.this.getActivity());
        db = dbHelper.getWritableDatabase();
        day = String.valueOf(calculateDaysRegistered(username) + 1);
        times = String.valueOf(getTimes());

    }

    private void getListInfo(){
        List<String> data = new ArrayList<>();
        data.add("用户信息");
        data.add("修改密码");
        data.add("修改资料");

        // 创建自定义的适配器
        ArrayAdapter<String> list1_Adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_layout, R.id.text, data) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View rowView = super.getView(position, convertView, parent);

                // 设置图标
                ImageView icView = rowView.findViewById(R.id.icon);
                ImageView iconView = rowView.findViewById(R.id.icon1);
                TextView textView = rowView.findViewById(R.id.text);
                // 根据不同的位置设置不同的图标，这里需要根据实际情况进行修改
                if (position == 0) {
                    icView.setImageResource(R.drawable.ic_user_info);
                    iconView.setImageResource(R.drawable.ic_more_simples);
                } else if (position == 1) {
                    icView.setImageResource(R.drawable.ic_set_password);
                    iconView.setImageResource(R.drawable.ic_more_simples);
                } else if (position == 2) {
                    icView.setImageResource(R.drawable.ic_set_info);
                    iconView.setImageResource(R.drawable.ic_more_simples);
                }

                //listView点击事件
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position){
                            case 0:
                                Intent intent = new Intent(requireContext(), UserInformationActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "修改密码", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getActivity(), "修改资料", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                return rowView;
            }
        };

        ArrayAdapter<String> list2_Adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_layout, R.id.text, data) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View rowView = super.getView(position, convertView, parent);

                // 设置图标
                ImageView icView = rowView.findViewById(R.id.icon);
                ImageView iconView = rowView.findViewById(R.id.icon1);
                TextView textView = rowView.findViewById(R.id.text);
                // 根据不同的位置设置不同的图标，这里需要根据实际情况进行修改
                if (position == 0) {
                    icView.setImageResource(R.drawable.ic_user_info);
                    iconView.setImageResource(R.drawable.ic_more_simples);
                    textView.setText("注销用户");
                } else if (position == 1) {
                    icView.setImageResource(R.drawable.ic_set_password);
                    iconView.setImageResource(R.drawable.ic_more_simples);
                    textView.setText("我的账单");
                } else if (position == 2) {
                    icView.setImageResource(R.drawable.ic_set_info);
                    iconView.setImageResource(R.drawable.ic_more_simples);
                    textView.setText("清除账单");
                }

                //listView点击事件
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position){
                            case 0:
                                Intent intent = new Intent(requireContext(), UserInformationActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "我的账单", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getActivity(), "修改资料", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                return rowView;
            }
        };
        listView1.setAdapter(list1_Adapter);
        listView2.setAdapter(list2_Adapter);
    }

    private String getCurrentBeijingDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置时区为北京时间
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

    //获取注册时间
    @SuppressLint("Range")
    private String getRegistrationDateByUsername(SQLiteDatabase db, String username) {
        String registrationDate = null;
        Cursor cursor = db.rawQuery("SELECT registration_date FROM users WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            registrationDate = cursor.getString(cursor.getColumnIndex("registration_date"));
        }

        //cursor.close();
        return registrationDate;
    }
    //获取天数
    private int calculateDaysRegistered(String username){
        String registrationDateStr = getRegistrationDateByUsername(db, username);
        if (registrationDateStr != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            try {
                // 获取当前北京时间
                String currentBeijingDateTime = getCurrentBeijingDateTime();
                Date currentDate = sdf.parse(currentBeijingDateTime);

                // 将数据库中获取的注册时间转换为日期对象
                Date registrationDate = sdf.parse(registrationDateStr);

                // 计算时间差
                long diffInMillis = currentDate.getTime() - registrationDate.getTime();
                int daysDiff = (int) (diffInMillis / (1000 * 60 * 60 * 24));

                return daysDiff;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return -1; // 表示无法计算天数差
    }

    //退出登录
    private void exit(){
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);}
        });
    }
    //获取昵称,记账天数，记账笔数
    private void getName_Days_Times(){
        Cursor cursor = db.rawQuery("select name from users where username= ?", new String[]{username});
        if(cursor.moveToFirst()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            nameText.setText(name);
            daysText.setText(day);
            timesText.setText(times);
        }
    }
    //这个sql语句也能查询收入和支出总和
    private int getTimes(){
        String sql = "SELECT\n" +
                "TotalIncomes.TotalCount + TotalExpenses.TotalCount AS Count\n" +
                "FROM\n" +
                "(\n" +
                "    SELECT\n" +
                "        COUNT(CASE WHEN income.userID = users.id THEN 1 ELSE NULL END) AS TotalCount\n" +
                "    FROM income\n" +
                "    JOIN users ON income.userID = users.id\n" +
                "    WHERE users.username = ?\n" +
                ") AS TotalIncomes,\n" +
                "(\n" +
                "    SELECT\n" +
                "        COUNT(CASE WHEN expense.userID = users.id THEN 1 ELSE NULL END) AS TotalCount\n" +
                "    FROM expense\n" +
                "    JOIN users ON expense.userID = users.id\n" +
                "    WHERE users.username = ?\n" +
                ") AS TotalExpenses;";

        Cursor cursor = db.rawQuery(sql, new String[]{username, username});

        if(cursor.moveToFirst()){
            @SuppressLint("Range") int count  = cursor.getInt(cursor.getColumnIndex("Count"));
            return count;
        }
        return -1;
    }

}