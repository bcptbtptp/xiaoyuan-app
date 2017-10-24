package com.xiaoyuan.longer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.xiaoyuan.Class.Appinfor;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.Class.UserModel;
import com.xiaoyuan.Config;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class AppSplashActivity extends Activity {
    public Context context;

    private ImageView mSplashImage;
    private static final int ANIMATION_TIME = 2000;

    private static final float SCALE_END = 1.13F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = AppSplashActivity.this;
        mSplashImage = (ImageView) findViewById(R.id.splash_image);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startAnim();

                    }
                });

    }




    private void startAnim() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mSplashImage, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mSplashImage, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                User user = BmobUser.getCurrentUser(AppSplashActivity.this, User.class);
                if (user == null) {
                    log.d("无缓存");
                    startActivity(new Intent(AppSplashActivity.this, LoginActivity.class));
                } else {
                    log.d("有缓存,帐号：" + user.getUsername() + "#");
                    startActivity(new Intent(AppSplashActivity.this, MainActivity.class));
                }
                AppSplashActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
