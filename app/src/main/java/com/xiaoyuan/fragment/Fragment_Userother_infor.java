package com.xiaoyuan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoyuan.Class.User;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.User_other_Activity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Axu on 2016/8/18.
 */
public class Fragment_Userother_infor extends Fragment {
    @Bind(R.id.tv_userother_sex)
    TextView tvUserotherSex;
    @Bind(R.id.tv_userother_age)
    TextView tvUserotherAge;
    @Bind(R.id.tv_userother_school)
    TextView tvUserotherSchool;
    @Bind(R.id.tv_userother_home)
    TextView tvUserotherHome;

    public Fragment_Userother_infor() {


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userother_infor, container, false);
        ButterKnife.bind(this, view);
        setdata();
        return view;
    }

    /*
    设置数据
     */
    public void setdata(){
        User user=User_other_Activity.user_other;
        tvUserotherSex.setText(user.getSex());
        tvUserotherAge.setText(Integer.toString(user.getAge()));
        tvUserotherHome.setText(user.getHome());
        tvUserotherSchool.setText(user.getSchool());

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
