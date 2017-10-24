package com.xiaoyuan.longer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.xiaoyuan.Class.Goods;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.Class.news;
import com.xiaoyuan.adapter.Image_crtnewsAdapter;
import com.xiaoyuan.tools.Snack;
import com.xiaoyuan.tools.Toast;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class Create_newsActivity extends AppCompatActivity {

    private EditText ed_title;
    private EditText ed_info;
    private EditText ed_type;
    private TextView tv_addpic;
    private TextView tv_jd;
    private ProgressBar pg;
    public Star star;
    private Button bu_up;
    public Context context;
    private Image_crtnewsAdapter imagecrtnewsAdapter;
    private ArrayList<String> path = new ArrayList<>();
    private static String pic_path = null;//允许用户选择一张图片，所以只保存一个图片地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        Intent intent = getIntent();
        star=(Star) intent.getSerializableExtra("star");
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
        init();

    }


    public void init() {
        context = Create_newsActivity.this;
        ed_info = (EditText) findViewById(R.id.edtTxt_crtnews_info);
        ed_title = (EditText) findViewById(R.id.edtTxt_crtnews_title);
        ed_type=(EditText)findViewById(R.id.edtTxt_crtnews_type);
        bu_up = (Button) findViewById(R.id.btn_crtnews_up);
        tv_addpic = (TextView) findViewById(R.id.tv_crtnews_addpic);
        tv_jd = (TextView) findViewById(R.id.tv_crtnews_jd);
        pg = (ProgressBar) findViewById(R.id.progress_crtnews_progressBar);
        bu_up.setOnClickListener(listener);
        tv_addpic.setOnClickListener(onclick_addpic);
    }

    View.OnClickListener onclick_addpic = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView recycler = (RecyclerView) findViewById(R.id.recy_crtnews);

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
                    .crop()
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
            recycler.setLayoutManager(gridLayoutManager);
            imagecrtnewsAdapter = new Image_crtnewsAdapter(context, path);
            recycler.setAdapter(imagecrtnewsAdapter);
        }
    };

    public class GlideLoader implements com.yancy.imageselector.ImageLoader {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .centerCrop()
                    .into(imageView);
        }
    }


    public static final int REQUEST_CODE = 1000;

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
        }
    }


    Button.OnClickListener listener = new Button.OnClickListener() {
        public void onClick(View v) {
            if (!TextUtils.isEmpty(ed_info.getText()) && !TextUtils.isEmpty(ed_title.getText())&&!TextUtils.isEmpty(ed_type.getText())) {

                if (pic_path == null) {
                    Toast.show("请先选择图片");
                    return;
                } else {
                    final BmobFile bmobFile = new BmobFile(new File(pic_path));
                    bmobFile.uploadblock(context, new UploadFileListener() {
                        @Override
                        public void onSuccess() {
                            String title = ed_title.getText().toString().trim();
                            String content = ed_info.getText().toString().trim();
                            String type=ed_type.getText().toString().trim();
                            User user = BmobUser.getCurrentUser(context, User.class);
                            news news = new news();
                            news.setTitle(title);
                            news.setAuthor(user);
                            news.setStar(star);
                            news.setType(type);
                            news.setContent(content);
                            news.setPicture(bmobFile);
                            news.save(context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Snack.show(pg,"发布新闻成功");
                                    Log.d("成功",star.getName());
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Snack.show(pg,"发布新闻失败");
                                    Log.d("发布新闻失败", s);
                                }
                            });
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            pg.setVisibility(View.GONE);
                            Snack.show(pg,"上传文件失败");
                            Log.d("上传文件失败", s);
                        }

                        @Override
                        public void onProgress(Integer value) {
                            super.onProgress(value);
                            // 返回的上传进度（百分比）
                            tv_jd.setText(Integer.toString(value) + " %");
                            pg.setVisibility(View.VISIBLE);
                            pg.setProgress(value);
                        }
                    });
                }
            } else {
                Toast.show("不能有空值");
            }
        }
    };
}