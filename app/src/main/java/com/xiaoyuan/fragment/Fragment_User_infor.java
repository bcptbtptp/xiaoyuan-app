package com.xiaoyuan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoyuan.Class.User;
import com.xiaoyuan.longer.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by Axu on 2016/8/19.
 */
public class Fragment_User_infor extends Fragment {
    @Bind(R.id.tv_user_sex)
    TextView tvUserSex;
    @Bind(R.id.tv_user_age)
    TextView tvUserAge;
    @Bind(R.id.tv_user_school)
    TextView tvUserSchool;
    @Bind(R.id.tv_user_home)
    TextView tvUserHome;
public Context context;
    public Fragment_User_infor() {


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_infor, container, false);
        context=getContext();
        ButterKnife.bind(this, view);
        setdata();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    public void setdata(){
        User user= BmobUser.getCurrentUser(context,User.class);
        tvUserSex.setText(user.getSex());
        tvUserAge.setText(Integer.toString(user.getAge()));
        tvUserSchool.setText(user.getSchool());
        tvUserHome.setText(user.getHome());
    }
}
