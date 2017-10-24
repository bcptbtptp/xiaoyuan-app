package com.xiaoyuan.longer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.Image_crtnewsAdapter;
import com.xiaoyuan.adapter.Image_editAdapter;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Axu on 2016/8/15.
 */
public class Modify_myinforActivity extends AppCompatActivity {
    @Bind(R.id.tv_infor_ok)
    TextView tvInforOk;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_infor_avatar)
    ImageView ivInforAvatar;
    @Bind(R.id.edtTxt_myinfor_name)
    EditText edtTxtMyinforName;
    @Bind(R.id.rdoBtn_infor_sex)
    RadioGroup rdoBtnInforSex;
    @Bind(R.id.edtTxt_myinfo_school)
    EditText edtTxtMyinfoSchool;
    @Bind(R.id.edtTxt_myinfor_age)
    EditText edtTxtMyinforAge;
    @Bind(R.id.edtTxt_infor_home)
    EditText edtTxtInforHome;
    @Bind(R.id.edtTxt_myinfor_nike)
    EditText edtTxtMyinforNike;
    public Context context;
    public User user;
    public String sex;
    private static String pic_path = null;
    public static final int REQUEST_CODE = 1000;
    public RecyclerView recyCrtphoto;
    private Image_editAdapter imagecrtnewsAdapter;
    private ArrayList<String> path = new ArrayList<>();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_myinfor);
        recyCrtphoto = (RecyclerView) findViewById(R.id.recy_crtphoto);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        context = Modify_myinforActivity.this;
        setdata();

    }

    @OnClick({R.id.tv_infor_ok, R.id.toolbar, R.id.iv_info_changetx, R.id.rdoBtn_infor_man, R.id.rdoBtn_infor_woman, R.id.rdoBtn_infor_sex})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_infor_ok:
                tvInforOk.setEnabled(false);
                Picup();
                log.d("点击了一下");
                break;
            case R.id.toolbar:
                break;
            case R.id.iv_info_changetx:
                choosepic();
                break;
            case R.id.rdoBtn_infor_man:
                sex = "男";
                break;
            case R.id.rdoBtn_infor_woman:
                sex = "女";
                break;
            case R.id.rdoBtn_infor_sex:
                break;
        }
    }


    private void setdata() {
        user = BmobUser.getCurrentUser(context, User.class);
        if (user == null) {
            Toast.show("获取数据失败咯");
            return;
        }
        sex = user.getSex().toString().trim();
        if (sex.equals("女")) {
            rdoBtnInforSex.check(R.id.rdoBtn_infor_woman);
        } else if (sex.equals("男")) {
            rdoBtnInforSex.check(R.id.rdoBtn_infor_man);
        }
        edtTxtMyinforName.setText(user.getUsername());
        edtTxtMyinforAge.setText(user.getAge() + "");
        edtTxtMyinforNike.setText(user.getNick());
        edtTxtInforHome.setText(user.getHome());
        edtTxtMyinfoSchool.setText(user.getSchool());


        String url = (user.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/08/71935ed3405c04df8052fb759c4206d2.jpg" : user.getAvatar().getUrl();
        Picasso.with(context).load(url).fit()
                .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                .error(com.yancy.imageselector.R.mipmap.imageselector_photo)
                .into(ivInforAvatar);
    }

    public void updata() {
        user.setSchool(edtTxtMyinfoSchool.getText().toString());
        user.setAge(Integer.parseInt(edtTxtMyinforAge.getText().toString()));
        user.setNick(edtTxtMyinforNike.getText().toString());
        user.setHome(edtTxtInforHome.getText().toString());
        user.setSex(sex);
        user.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.show("更新用户信息成功");
                Modify_myinforActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.show("更新用户信息失败:" + s);
                tvInforOk.setEnabled(true);
            }
        });
    }


    public void choosepic() {
        ImageConfig imageConfig
                = new ImageConfig.Builder(
                new GlideLoader())
                .steepToolBarColor(getResources().getColor(R.color.colorPrimaryDark))
                .titleBgColor(getResources().getColor(R.color.colorPrimary))
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                .singleSelect()
                .crop()
                .mutiSelectMaxSize(1)
                .pathList(path)
                .filePath("/ImageSelector/Pictures")
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();

        ImageSelector.open((Activity) context, imageConfig);   // 开启图片选择器

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyCrtphoto.setLayoutManager(gridLayoutManager);
        imagecrtnewsAdapter = new Image_editAdapter(context, path);
        recyCrtphoto.setAdapter(imagecrtnewsAdapter);
    }


    public class GlideLoader implements com.yancy.imageselector.ImageLoader {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .override(80, 80)
                    .centerCrop()
                    .into(imageView);

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }
            path.clear();
            pic_path = pathList.get(pathList.size() - 1);
            path.add(pic_path);
            if (pic_path != null) {
                imagecrtnewsAdapter.notifyDataSetChanged();
                ivInforAvatar.setVisibility(View.GONE);
            } else {
                ivInforAvatar.setVisibility(View.VISIBLE);
            }

        }
    }

    public void Picup() {
        if (pic_path == null) {
            updata();
            return;
        } else {
            final BmobFile bmobFile = new BmobFile(new File(pic_path));
            bmobFile.uploadblock(context, new UploadFileListener() {
                public void onSuccess() {                    ;
                    user.setAvatar(bmobFile);
                    user.update(context, new UpdateListener() {
                        @Override
                        public void onSuccess() {
//                            Toast.show("更换头像成功");
                            updata();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.show("更换头像失败");
                            Log.d("更换头像失败", s);
                        }
                    });
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.show("上传头像失败");
                    Log.d("上传头像失败", s);
                    tvInforOk.setEnabled(true);
                }
            });
        }
    }


}
