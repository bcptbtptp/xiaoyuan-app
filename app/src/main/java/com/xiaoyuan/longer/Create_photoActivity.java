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
import com.xiaoyuan.Class.Photo;
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
 * Created by Axu on 2016/8/9.
 */
public class Create_photoActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edtTxt_crtphoto_title)
    EditText edtTxtCrtphotoTitle;
    @Bind(R.id.edtTxt_crtphoto_info)
    EditText edtTxtCrtphotoInfo;
    @Bind(R.id.progress_crtphoto_progressBar)
    ProgressBar progressCrtphotoProgressBar;
    @Bind(R.id.tv_crtphoto_jd)
    TextView tvCrtphotoJd;
    @Bind(R.id.recy_crtphoto)
    RecyclerView recyCrtphoto;
    public Context context;
    @Bind(R.id.edtTxt_crtphoto_type)
    EditText edtTxtCrtphotoType;
    private Image_crtnewsAdapter imagecrtnewsAdapter;
    private ArrayList<String> path = new ArrayList<>();
    public static final int REQUEST_CODE = 1000;
    private static String pic_path = null;
    public Star star;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_photo);
        context = Create_photoActivity.this;
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        star = (Star) intent.getSerializableExtra("star");
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @OnClick({R.id.tv_crtphoto_addpic, R.id.btn_crtphoto_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_crtphoto_addpic:
                choosepic();
                break;

            case R.id.btn_crtphoto_up:
                Picup();
                break;
        }


    }

    public void choosepic() {

        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.colorPrimaryDark))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.colorPrimary))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                .singleSelect()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(1)
                // 已选择的图片路径
                .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();

        ImageSelector.open((Activity) context, imageConfig);   // 开启图片选择器

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        recyCrtphoto.setLayoutManager(gridLayoutManager);
        imagecrtnewsAdapter = new Image_crtnewsAdapter(context, path);
        recyCrtphoto.setAdapter(imagecrtnewsAdapter);


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
        }
    }


    public void Picup() {
        if (!TextUtils.isEmpty(edtTxtCrtphotoInfo.getText()) && !TextUtils.isEmpty(edtTxtCrtphotoTitle.getText())&&!TextUtils.isEmpty(edtTxtCrtphotoType.getText())) {

            if (pic_path == null) {
                return;
            } else {
                final BmobFile bmobFile = new BmobFile(new File(pic_path));
                bmobFile.uploadblock(context, new UploadFileListener() {
                    public void onSuccess() {
                        String title = edtTxtCrtphotoTitle.getText().toString().trim();
                        String info = edtTxtCrtphotoInfo.getText().toString().trim();
                        String type=edtTxtCrtphotoType.getText().toString().trim();
                        User user = BmobUser.getCurrentUser(context, User.class);
                        Photo photo = new Photo();
                        photo.setTitle(title);
                        photo.setInfor(info);
                        photo.setLike(0);
                        photo.setStar(star);
                        photo.setType(type);
                        photo.setAuthor(user);
                        photo.setPicture(bmobFile);
                        photo.save(context, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                Snack.show(toolbar,"发布图片成功");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Snack.show(toolbar,"发布图片失败");
                                Log.d("发布新闻失败", s);
                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        progressCrtphotoProgressBar.setVisibility(View.GONE);
                        Snack.show(toolbar,"上传文件失败");
                        Log.d("上传文件失败", s);
                    }


                    public void onProgress(Integer value) {
                        super.onProgress(value);
                        // 返回的上传进度（百分比）
                        tvCrtphotoJd.setText(Integer.toString(value) + " %");
                        progressCrtphotoProgressBar.setVisibility(View.VISIBLE);
                        progressCrtphotoProgressBar.setProgress(value);
                    }
                });


            }
        } else {
            Toast.show("不能为空");
        }

    }

}
