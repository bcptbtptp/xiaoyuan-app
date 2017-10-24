package com.xiaoyuan.longer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.tools.Snack;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Axu on 2016/8/14.
 */
public class Create_listActivity extends AppCompatActivity {

    public Context context;
    @Bind(R.id.radio_crtlist)
    RadioGroup radioCrtlist;
    @Bind(R.id.edtTxt_crtgoods_title)
    EditText edtTxtCrtgoodsTitle;
    public Star star;
    public String[] name = new String[]{"评论列表", "图片列表", "商品列表", "新闻列表"};
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_crtgoods_up2)
    Button btnCrtgoodsUp2;
    public TextView tv_crtnews;
    public RadioButton rdo_crtnews;
    public TextView tv_crtpl;
    public RadioButton rdo_crtpl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        ButterKnife.bind(this);
        tv_crtnews = (TextView) findViewById(R.id.tv_crtlist_crtnews);
        rdo_crtnews = (RadioButton) findViewById(R.id.radio_crtlist_crtnews);
        tv_crtpl = (TextView) findViewById(R.id.tv_crtlist_crtpl);
        rdo_crtpl = (RadioButton) findViewById(R.id.radio_crtlist_crtpl);
        context = Create_listActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        Intent intent = getIntent();
        star = (Star) intent.getSerializableExtra("star");
        isVIP();
    }

    @OnClick(R.id.btn_crtgoods_up2)
    public void onClick() {
        btnCrtgoodsUp2.setEnabled(false);
        String title = edtTxtCrtgoodsTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Snack.show(edtTxtCrtgoodsTitle, "列名不能为空！");
            btnCrtgoodsUp2.setEnabled(true);
            return;
        }
        int check = radioCrtlist.getCheckedRadioButtonId();
        if (check == -1) {
            Snack.show(edtTxtCrtgoodsTitle, "请选择一个列表类型！");
            btnCrtgoodsUp2.setEnabled(true);
            return;
        }
        // check  为 1：图片列表  2：商品列表   3：新闻列表   0：评论列表
        check = check % 4;
        //检查是否已经创建过该类型的列表，如果有给出提示，没有则将属性修改为列表名；OFF

        if (!checkexic(check)) {
            Snack.show(edtTxtCrtgoodsTitle, "该星球已有" + name[check] + "列表啦~");
            btnCrtgoodsUp2.setEnabled(true);
            btnCrtgoodsUp2.setEnabled(true);
            return;
        }
        //不存在则更新星球信息
        update(check);

    }

    /**
     * 是否为VIP若不是VIP则隐藏不能创建的列表
     */

    public void isVIP() {
        User user = BmobUser.getCurrentUser(context, User.class);
        if (!user.getVip()) {
            rdo_crtnews.setVisibility(View.GONE);
            tv_crtnews.setVisibility(View.GONE);
            rdo_crtpl.setVisibility(View.GONE);
            tv_crtpl.setVisibility(View.GONE);
        }

    }


    private void update(int check) {
        String listname = edtTxtCrtgoodsTitle.getText().toString().trim();
        switch (check) {
            case 1:
                star.setPhoto(listname);
                break;
            case 2:
                star.setGoods(listname);
                break;
            case 3:
                star.setNews(listname);
                break;
            case 0:
                star.setComment(listname);
                break;
        }
        star.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                Snack.show(edtTxtCrtgoodsTitle, "创建成功~");
                btnCrtgoodsUp2.setEnabled(true);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.show("创建失败" + s);
                btnCrtgoodsUp2.setEnabled(true);
            }
        });
    }

    /**
     * 检查列表是否存在  ,如果存在返回false,  不存在返回true
     */
    public boolean checkexic(int check) {
        switch (check) {
            case 1:
                return (star.getPhoto() == null || "OFF".equals(star.getPhoto())) ? true : false;
            case 2:
                return (star.getGoods() == null || "OFF".equals(star.getGoods())) ? true : false;
            case 3:
                return (star.getNews() == null || "OFF".equals(star.getNews())) ? true : false;
            case 0:
                return (star.getComment() == null || "OFF".equals(star.getComment())) ? true : false;
        }
        return true;
    }


}
