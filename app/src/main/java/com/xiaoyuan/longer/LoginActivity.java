package com.xiaoyuan.longer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.xiaoyuan.Class.User;
import com.xiaoyuan.fragment.Fragment_Login_Main;
import com.xiaoyuan.fragment.Fragment_Login;
import com.xiaoyuan.fragment.Fragment_Login_register;

import cn.bmob.v3.BmobUser;

public class LoginActivity extends AppCompatActivity {

    public User user;
    private Context context;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;


        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragment, new Fragment_Login_Main());

        ft.commit();


    }

    // Fragment 窗口的切换
    public void fragmentchange(Fragment frag) {
        fm = getFragmentManager();
        int back_num = fm.getBackStackEntryCount();
        if (back_num > 0) {
            fm.popBackStack();
        }
        ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragment, frag);
        ft.addToBackStack(null);
        ft.commit();
    }

    // 登录按键
    public void onclick_login(View view) {
        fragmentchange(new Fragment_Login());
    }


    // 注册按键
    public void onclick_zhuce(View view) {
        fragmentchange(new Fragment_Login_register());
    }

}
