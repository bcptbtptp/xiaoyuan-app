package com.xiaoyuan.longer;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.fragment.BlankFragment;
import com.xiaoyuan.fragment.Fragment_News;
import com.xiaoyuan.fragment.Fragment_User_infor;
import com.xiaoyuan.fragment.Fragment_Userother_infor;
import com.xiaoyuan.others.ImageFactor;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.WaitDialog;
import com.xiaoyuan.tools.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.BmobUser;

public class User_Activity extends AppCompatActivity {

    private ViewPager viewpage;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    public Context context;
    public ImageView iv_avatar;
    public TextView tv_name;
    public ImageView iv_vip;
    public TextView tv_nick;
    public TextView tv_vip;
    public WaitDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        iv_avatar = (ImageView) findViewById(R.id.iv_user_avatar);
        iv_vip = (ImageView) findViewById(R.id.iv_user_vip);
        tv_name = (TextView) findViewById(R.id.tv_user_name);
        tv_nick = (TextView) findViewById(R.id.tv_user_nick);
        tv_vip = (TextView) findViewById(R.id.tv_star_vip);
        tv_vip.setOnClickListener(onClickListener);
        context = User_Activity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fra = new Fragment_News();
        Fragment fra1 = new Fragment_User_infor();
        fragments.add(fra);
        fragments.add(fra1);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        viewpage = (ViewPager) findViewById(R.id.container);
        viewpage.setAdapter(mSectionsPagerAdapter);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewpage);
        setdata();
        dialog = new WaitDialog(context,"正在查询订单...");
    }

    /**
     * 开通会员的点击事件
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("请选择付款方式");
            //    指定下拉列表的显示数据
            final String[] cities = {"微信付款", "支付宝付款"};
            //    设置一个下拉的列表选择项
            builder.setItems(cities, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean pay_way = which == 0 ? false : true;
                    pay(pay_way);
                }
            });
            builder.show();
        }
    };

    /**\
     *
     * @param pay_way   false是微信支付   true 是支付宝支付
     */
    private void pay(boolean pay_way) {

        showDialog();
        BP.init(context, "afa79ddcef55992f38916b10a3df3006");
        //防止微信支付中断
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        BP.pay("商品名称", "商品描述", 1.00, pay_way, new PListener() {
            @Override
            public void orderId(String s) {
//                log.d("orderId:" + s);
                hideDialog();
                dialog = new WaitDialog(context,"获取订单成功!请等待跳转到支付页面~");
                showDialog();
            }

            @Override
            public void succeed() {
                log.d("succeed");
                Toast.show("支付成功!");
                hideDialog();
            }

            @Override
            public void fail(int i, String s) {
                log.d("fail" + i);
                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (i == -3) {
                    Toast.show("监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付");
                    installBmobPayPlugin();
                } else {
                    Toast.show("支付中断!");
                }
                hideDialog();
            }

            @Override
            public void unknow() {
                Toast.show("支付结果未知,请稍后手动查询");
                hideDialog();
            }
        });
    }

    /**
     * 安装 安全支付控件
     */
    void installBmobPayPlugin() {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.bp);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "pb.db.apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showDialog() {
            if (dialog != null && dialog.isShowing())
            dialog.show();
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }
    /*
    三点菜单，选择内容
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_out) {
            BmobUser.logOut(context);
            startActivity(new Intent(context, LoginActivity.class));
            User_Activity.this.finish();
            MainActivity.instance.finish();
            return true;
        } else if (id == R.id.action_modify) {
            Intent intent = new Intent(context, Modify_myinforActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    设置数据，头像，名字，个性签名
     */
    public void setdata() {
        User user = BmobUser.getCurrentUser(context, User.class);
        //判断是否为会员
        if (user.getVip()) {
            iv_vip.setVisibility(View.VISIBLE);
            tv_vip.setVisibility(View.GONE);

        } else if (!user.getVip() || user.getVip() == null) {
            iv_vip.setVisibility(View.GONE);
            tv_vip.setVisibility(View.VISIBLE);
        }


        tv_nick.setText(user.getNick());
        tv_name.setText(user.getUsername());


        String url = (user.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/08/71935ed3405c04df8052fb759c4206d2.jpg" : user.getAvatar().getUrl();
        Picasso.with(context).load(url).fit()
                .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                .error(com.yancy.imageselector.R.mipmap.imageselector_photo)
                .transform(new ImageFactor())
                .into(iv_avatar);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public List<Fragment> mfragments;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mfragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mfragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "我的动态";
                case 1:
                    return "我的资料";
            }
            return null;
        }
    }
}
