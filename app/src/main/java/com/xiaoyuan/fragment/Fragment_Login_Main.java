package com.xiaoyuan.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xiaoyuan.Class.User;
import com.xiaoyuan.longer.R;

import cn.bmob.v3.BmobUser;

public class Fragment_Login_Main extends Fragment {
    public User user;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_main, null);
        return view;
    }

}
