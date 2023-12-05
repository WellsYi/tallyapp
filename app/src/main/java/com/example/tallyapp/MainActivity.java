package com.example.tallyapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tallyapp.fragment.AccountDetail;
import com.example.tallyapp.fragment.Accounting;
import com.example.tallyapp.fragment.Statistics;
import com.example.tallyapp.fragment.UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private Fragment detailFragment;
    private Fragment statisticsFragment;
    private Fragment userFragment;
    private Fragment accountingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        initView();
        //默认显示界面
        getSupportFragmentManager().beginTransaction().replace(R.id.content, detailFragment).commit();
        //导航栏点击事件
        checkButtom();

    }

    private void initView(){
        bottomNavigationView = findViewById(R.id.mian_BottomNavigationView);
        detailFragment = new AccountDetail();
        statisticsFragment = new Statistics();
        userFragment = new UserInfo();
        accountingFragment = new Accounting();
    }

    //点击事件方法
    private void checkButtom(){
      bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
          if (item.getItemId() == R.id.menu_button_1){
              replaceFragment(detailFragment);
              return true;
          } else if (item.getItemId() == R.id.menu_button_2) {
              replaceFragment(statisticsFragment);
              return true;
          }else if (item.getItemId() == R.id.menu_button_3){
              replaceFragment(userFragment);
              return true;
          }else if (item.getItemId() == R.id.menu_button_4){
              replaceFragment(accountingFragment);
              return true;
          }
          return false;
      });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }
}
