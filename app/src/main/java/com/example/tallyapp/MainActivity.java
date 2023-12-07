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
    private void checkButtom() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            //根据当前页码点击时判断左右滑动过渡动画
            if (item.getItemId() == R.id.menu_button_1 && getCurrentPage() > 1) {
                replaceFragment(detailFragment, false);
                return true;
            } else if (item.getItemId() == R.id.menu_button_2 && getCurrentPage() == 1) {
                replaceFragment(statisticsFragment, true);
                return true;
            } else if (item.getItemId() == R.id.menu_button_2 && getCurrentPage() > 2) {
                replaceFragment(statisticsFragment, false);
                return true;
            } else if (item.getItemId() == R.id.menu_button_3 && getCurrentPage() > 3) {
                replaceFragment(userFragment, false);
                return true;
            }else if (item.getItemId() == R.id.menu_button_3 && getCurrentPage() < 3) {
                replaceFragment(userFragment, true);
                return true;
            } else if (item.getItemId() == R.id.menu_button_4) {
                replaceFragment(accountingFragment, true); // 向右滑动
                return true;
            }
            return false;
        });
    }


    //添加左右滑动的过渡动画
    private void replaceFragment(Fragment fragment, boolean isRightDirection) {
        int enterAnim, exitAnim, popEnterAnim, popExitAnim;

        if (isRightDirection) {
            enterAnim = R.anim.slide_in_right;
            exitAnim = R.anim.slide_out_left;
            popEnterAnim = R.anim.slide_in_left;
            popExitAnim = R.anim.slide_out_right;
        } else {
            enterAnim = R.anim.slide_in_left;
            exitAnim = R.anim.slide_out_right;
            popEnterAnim = R.anim.slide_in_right;
            popExitAnim = R.anim.slide_out_left;
        }

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
                .replace(R.id.content, fragment)
                .addToBackStack(null) // 如果需要添加到返回栈中
                .commit();
    }


    // 获取当前页码
    private int getCurrentPage() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (currentFragment == detailFragment) {
            return 1;
        } else if (currentFragment == statisticsFragment) {
            return 2;
        } else if (currentFragment == userFragment) {
            return 3;
        } else if (currentFragment == accountingFragment) {
            return 4;
        }
        return -1; // 如果当前 Fragment 不是预期的页面，返回 -1 或者其他值作为错误标识
    }


}
