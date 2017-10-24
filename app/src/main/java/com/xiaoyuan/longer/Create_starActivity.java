package com.xiaoyuan.longer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.Image_crtnewsAdapter;
import com.xiaoyuan.tools.Snack;
import com.xiaoyuan.tools.Toast;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageLoader;
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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Axu on 2016/8/8.
 */
public class Create_starActivity extends AppCompatActivity {


    @Bind(R.id.progress_crtstar_progressBar2)
    ProgressBar progressCrtstarProgressBar2;
    @Bind(R.id.tv_crtstar_jd2)
    TextView tvCrtstarJd2;
    @Bind(R.id.recy_crtstar2)
    RecyclerView recyCrtstar2;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edtTxt_crtstar_name)
    EditText edtTxtCrtstarName;
    @Bind(R.id.edtTxt_crtstar_info)
    EditText edtTxtCrtstarInfo;
    @Bind(R.id.progress_crtstar_progressBar)
    ProgressBar progressCrtstarProgressBar;
    @Bind(R.id.tv_crtstar_jd)
    TextView tvCrtstarJd;
    @Bind(R.id.recy_crtstar)
    RecyclerView recyCrtstar;
    @Bind(R.id.btn_crtstar_up)
    Button btnCrtstarUp;

    public static final int REQUEST_CODE = 1000;
    public static final int REQUEST_CODE2 = 2000;
    public Context context;
    private Image_crtnewsAdapter imagecrtnewsAdapter;
    private Image_crtnewsAdapter imagecrtnewsAdapter2;
    private ArrayList<String> path = new ArrayList<>();
    private ArrayList<String> path2 = new ArrayList<>();
    private static String pic_path = null;
    private static String pic_path2 = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_star);
        context = Create_starActivity.this;
        ButterKnife.bind(this);
        btnCrtstarUp.setOnClickListener(listen);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    @OnClick({R.id.tv_crtstar_addpic, R.id.tv_crtstar_addpic2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_crtstar_addpic:
                addpic_avatar();
                break;
            case R.id.tv_crtstar_addpic2:
                addpic_show();
                break;
        }
    }

    /**
     * 头像选择器
     */
    public void addpic_avatar() {
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
        recyCrtstar.setLayoutManager(gridLayoutManager);
        imagecrtnewsAdapter = new Image_crtnewsAdapter(context, path);
        recyCrtstar.setAdapter(imagecrtnewsAdapter);
    }

    /**
     * 主界面展示图选择器
     */
    public void addpic_show() {
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
                .pathList(path2)
                .filePath("/ImageSelector/Pictures")
                .showCamera()
                .requestCode(REQUEST_CODE2)
                .build();

        ImageSelector.open((Activity) context, imageConfig);   // 开启图片选择器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyCrtstar2.setLayoutManager(gridLayoutManager);
        imagecrtnewsAdapter2 = new Image_crtnewsAdapter(context, path2);
        recyCrtstar2.setAdapter(imagecrtnewsAdapter2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }

            path.clear();
//            path.addAll(pathList);
            pic_path = pathList.get(pathList.size() - 1);
            path.add(pic_path);
            imagecrtnewsAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE2 && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }

            path.clear();
//            path.addAll(pathList);
            pic_path2 = pathList.get(pathList.size() - 1);
            path.add(pic_path2);
            imagecrtnewsAdapter2.notifyDataSetChanged();
        }

    }


    Button.OnClickListener listen = new Button.OnClickListener() {
        public void onClick(View v) {
            if (TextUtils.isEmpty(edtTxtCrtstarInfo.getText()) || TextUtils.isEmpty(edtTxtCrtstarName.getText())) {
                Toast.show("不能有空");
                return;
            }
            if (pic_path == null || pic_path2 == null) {
                Toast.show("请先选择图片");
                return;
            }
            //先上传头像
            final BmobFile bmobFile = new BmobFile(new File(pic_path));
            bmobFile.uploadblock(context, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    //在上传主页展示图
                    uoloda_show(bmobFile);
                }

                @Override
                public void onFailure(int i, String s) {
                    progressCrtstarProgressBar.setVisibility(View.GONE);
                    Toast.show("上传头像失败");
                    Log.d("上传文件失败", s);
                }

                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);
                    tvCrtstarJd.setText(Integer.toString(value) + " %");
                    progressCrtstarProgressBar.setVisibility(View.VISIBLE);
                    progressCrtstarProgressBar.setProgress(value);
                }
            });
        }
    };
    public void uoloda_show(final BmobFile bmobFile_avatar){
        final BmobFile bmobFile = new BmobFile(new File(pic_path2));
        bmobFile.uploadblock(context, new UploadFileListener() {
            @Override
            public void onSuccess() {
                String name = edtTxtCrtstarName.getText().toString().trim();
                String info = edtTxtCrtstarInfo.getText().toString().trim();
                Star star = new Star();
                User user = BmobUser.getCurrentUser(context, User.class);
                star.setName(name);
                star.setFather(user);
                star.setInfor(info);
                star.setGalaxy("成都工业学院");
                star.setGoods("OFF");
                star.setNews("OFF");
                star.setComment("OFF");
                star.setPhoto("OFF");
                star.setShow(bmobFile);
                star.setAvatar(bmobFile_avatar);
                star.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Snack.show(btnCrtstarUp,"发布星球成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Snack.show(btnCrtstarUp,"发布星球失败");
                        Log.d("发布星球失败", s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                progressCrtstarProgressBar2.setVisibility(View.GONE);
                Snack.show(btnCrtstarUp,"上传主页展示图失败");
                Log.d("上传文件失败", s);
            }

            @Override
            public void onProgress(Integer value) {
                super.onProgress(value);
                tvCrtstarJd2.setText(Integer.toString(value) + " %");
                progressCrtstarProgressBar2.setVisibility(View.VISIBLE);
                progressCrtstarProgressBar2.setProgress(value);
            }
        });

    }


    public class GlideLoader implements ImageLoader {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .centerCrop()
                    .into(imageView);
        }
    }


}
