package com.xiaoyuan.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xiaoyuan.Class.User;
import com.xiaoyuan.Class.UserModel;
import com.xiaoyuan.longer.MainActivity;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.FileTools;
import com.xiaoyuan.tools.Snack;
import com.xiaoyuan.tools.TimeTools;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.WaitDialog;
import com.xiaoyuan.tools.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class Fragment_Login extends Fragment implements View.OnClickListener {
    public static int logintime = 0;
    private View view;
    public EditText username;
    public EditText password;
    public Button login;
    private Context context;
    private WaitDialog mDialog;
    public int errornum;//错误次数


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, null);

        username = (EditText) view.findViewById(R.id.login_ed_username);
        password = (EditText) view.findViewById(R.id.login_ed_password);
        login = (Button) view.findViewById(R.id.login_bu_login);
        context = getActivity();
        login.setOnClickListener(this);
        //检查是否是已经登录过的用户，如果是设置用户名，更改登录为退出当前帐号
        checkuser();
        return view;
    }

    private void checkuser() {
        User user = BmobUser.getCurrentUser(context, User.class);
        if (user != null) {
            username.setText(user.getUsername());
            login.setText("退出当前帐号");
            password.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        String but = login.getText().toString();
        if ("退出当前帐号".equals(but)) {
            BmobUser.logOut(context);
            MainActivity.instance.finish();
            Snack.show(login, "退出当前用户成功");
            password.setVisibility(View.VISIBLE);
            username.setText("");
            login.setText("登录");
        } else if ("登录".equals(but)) {
            //1：先判断是否为空:   2：再判断登录次数  3：在判断密码是否正确（不正确累加登录次数）
            String str_username = username.getText().toString().trim();
            String str_password = password.getText().toString().trim();
            if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password)) {
                Snackbar.make(view, "不能为空！", Snackbar.LENGTH_SHORT).show();
                return;
            }

            //登录次数的判断，1：需要记录上次登录错误的时间和次数（如果为空或者已经大于10min则重置为0次，如果在10min之内，继续累加次数）
            errornum = TimeTools.login_erroenum(context);
            //判断是否小于5次
            if (errornum < 5) {
                if (errornum > 1) {
                    Snack.show(login, "您还有" + (4 - errornum) + "次机会");
                }
                mDialog = new WaitDialog(context, "正在登录中....");
                mDialog.setCancelable(true);
                mDialog.show();
                getDate(str_username, str_password);
            } else {
                Snack.show(login, "你都登录错误好多次了~");
            }
        }
    }


    /**
     * 用户登录
     *
     * @param str_username 帐号
     * @param str_password 密码
     */
    public void getDate(String str_username, String str_password) {
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(str_username);
        bu2.setPassword(str_password);
        bu2.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                setdate(true, "");
            }

            @Override
            public void onFailure(int i, String s) {
                setdate(false, s);
            }
        });
    }

    /**
     * 接受响应，根据情况，适配数据
     *
     * @param pd true表示成功  false 表示失败
     */
    private void setdate(boolean pd, String mes) {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (pd) {
            Toast.show("登录成功");
            //重置登录错误的次数为0
            FileTools.saveshareInt(context, "login_errornum", 0);
            startActivity(new Intent(context, MainActivity.class));
            getActivity().finish();
        } else {
            Toast.show("登录失败:" + mes);
            //登录失败，将错误次数累加，之后保存到share
            FileTools.saveshareInt(context, "login_errornum", errornum + 1);

            //并且保存当前时间
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                FileTools.saveshareString(context, "login_errortime", str);
            } catch (Exception e) {
                e.printStackTrace();
                log.d("保存时间出问题了");
            }

            Log.d("登录失败", mes);
        }
    }

}
