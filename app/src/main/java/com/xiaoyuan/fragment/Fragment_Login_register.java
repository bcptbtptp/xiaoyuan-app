package com.xiaoyuan.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xiaoyuan.Class.User;
import com.xiaoyuan.bmob.ContantStr;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Fragment_Login_register extends Fragment implements View.OnClickListener {

    private View view;
    public EditText username;
    public EditText password;
    public Button login;
    public EditText password2;
    boolean nameRight = false;
    private Context context;
    public TextInputLayout txtInput_username;
    public TextInputLayout txtInput_password;
    public TextInputLayout txtInput_password2;
    public String str_username;
    public String str_password;
    public String str_password2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_register, null);
        login = (Button) view.findViewById(R.id.login_re_bu_register);
        context = getActivity();
        txtInput_username = (TextInputLayout) view.findViewById(R.id.login_txtInput_username);
        txtInput_password = (TextInputLayout) view.findViewById(R.id.login_txtInput_password);
        txtInput_password2 = (TextInputLayout) view.findViewById(R.id.login_txtInput_password2);


        username = txtInput_username.getEditText();
        username.addTextChangedListener(textWatcher_username);

        password = txtInput_password.getEditText();
        password.addTextChangedListener(textWatcher_pass1);

        password2 = txtInput_password2.getEditText();
        password2.addTextChangedListener(textWatcher_pass2);
        login.setOnClickListener(this);
        return view;
    }

    /**
     * 用户名输入时
     */
    public TextWatcher textWatcher_username = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            str_username = username.getText().toString().trim();
            String regEx = "^[A-Za-z0-9]+";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(str_username);
            boolean b = matcher.matches();
            if (!b) {
                txtInput_username.setError("只能为数字或字母符号");
                txtInput_username.setErrorEnabled(true);
                nameRight = false;
            } else {
                nameRight = true;
                txtInput_username.setErrorEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    /**
     * 密码第一次输入时
     */
    public TextWatcher textWatcher_pass1 = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            str_password = password.getText().toString().trim();
//            log.d(s.toString()+"afterTextChanged");
            if (s.length() < 6) {
                txtInput_password.setErrorEnabled(true);
                txtInput_password.setError("密码至少为六位");
            } else {
                txtInput_password.setErrorEnabled(false);
            }
        }
    };

    /**
     * 密码第二次输入时
     */
    public TextWatcher textWatcher_pass2 = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            str_password2 = password2.getText().toString().trim();
            if (s.length() >= 6) {
                if (str_password.equals(str_password2)) {
                    txtInput_password2.setErrorEnabled(false);
                } else {
                    txtInput_password2.setError("两次密码不一致");
                    txtInput_password2.setErrorEnabled(true);
                }
            }
        }
    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_re_bu_register) {
            //他们说的暂时不要注册防刷
//            if (!checkTime()) {
//                Toast.show("一天只能注册一次哦");
//                return;
//            }
            if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password) || TextUtils.isEmpty(str_password2)) {
                Toast.show("不能为空");
                return;
            }
            if (str_password.length() < 6) {
                txtInput_password.setErrorEnabled(true);
                txtInput_password.setError("密码至少为六位");
//                txtInput_password2.setErrorEnabled(true);
//                txtInput_password2.setError("密码至少为六位");
                return;
            }
            if (!str_password.equals(str_password2)) {
//                Toast.show("两次密码不一致");
                txtInput_password.setErrorEnabled(true);
//                txtInput_password2.setErrorEnabled(true);
                txtInput_password.setError("两次密码不一致");
//                txtInput_password2.setError("两次密码不一致");
                return;
            }

            if (nameRight) {
                register();
            } else {
                txtInput_username.setErrorEnabled(true);
                txtInput_username.setError("用户名只能为数字或字母符号");
                Toast.show("用户名必须是数字和字母的组合");
            }

        }

    }

    /**
     * 登录是调用的函数
     */
    public void register() {
        User user = new User();
        user.setSex("男");
        user.setAge(0);
        user.setSchool("");
        user.setNick("一句话介绍自己");
        user.setHome("地球");
        user.setAvatar(new BmobFile("ali", "", "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/08/71935ed3405c04df8052fb759c4206d2.jpg"));//默认头像阿狸
        user.setUsername(str_username);
        user.setPassword(str_password);
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                fm.popBackStack();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.fragment, new Fragment_Login_Main());
                ft.commit();
                Toast.show("注册成功");
                savetime();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.show("注册失败:" + s);
            }
        });


    }

    /**
     * 登录时读取时间对比,大于一天返回true
     */
    public boolean checkTime() {
        boolean re = false;
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int all = year * 10000 + month * 100 + day;
        Log.d(Integer.toString(year) + Integer.toString(month) + Integer.toString(day), "当前时间");
        SharedPreferences sharedPreferences = context.getSharedPreferences("regist_date", context.MODE_PRIVATE);
        Log.d(Integer.toString(all), "all1");
        int reyear = sharedPreferences.getInt("year", 0);
        int remonth = sharedPreferences.getInt("month", 0);
        int reday = sharedPreferences.getInt("day", 0);
        int all2 = reyear * 10000 + remonth * 100 + reday;
        Log.d(Integer.toString(reyear) + Integer.toString(remonth) + Integer.toString(reday), "保存的时间");
//        Log.d(Integer.toString(all2), "all2");
        if (reyear == 0 && remonth == 0 && reday == 0) {
            re = true;
        } else {
            if (all2 < all) {
                re = true;
            } else {
                re = false;
            }
        }
        return re;
    }

    public void savetime() {
        boolean re = false;
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        SharedPreferences.Editor editor = context.getSharedPreferences("regist_date", context.MODE_PRIVATE).edit();
        editor.putInt("year", year);
        editor.putInt("month", month);
        editor.putInt("day", day);

        editor.commit();
    }

}

