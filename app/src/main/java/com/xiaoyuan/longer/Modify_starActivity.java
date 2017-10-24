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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.Image_editAdapter;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;
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
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Longer on 2016/8/16.
 */
public class Modify_starActivity extends AppCompatActivity {

    public Context context;
    public Star star;
    @Bind(R.id.tv_editlist_news)
    TextView tvEditlistNews;
    @Bind(R.id.ed_editlist_news)
    EditText edEditlistNews;
    @Bind(R.id.tv_editlist_goods)
    TextView tvEditlistGoods;
    @Bind(R.id.ed_editlist_goods)
    EditText edEditlistGoods;
    @Bind(R.id.tv_editlist_photos)
    TextView tvEditlistPhotos;
    @Bind(R.id.ed_editlist_photos)
    EditText edEditlistPhotos;
    @Bind(R.id.tv_editlist_comments)
    TextView tvEditlistComments;
    @Bind(R.id.ed_editlist_comments)
    EditText edEditlistComments;
    @Bind(R.id.btn_editlist)
    Button btnEditlist;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_modify_avatar)
    ImageView ivModifyAvatar;
    @Bind(R.id.recy_crtphoto)
    RecyclerView recyCrtphoto;
    @Bind(R.id.iv_modify_changetx)
    TextView ivModifyChangetx;
    @Bind(R.id.ed_modify_glaxy)
    EditText edModifyGlaxy;
    @Bind(R.id.ed_modify_name)
    EditText edModifyName;
    @Bind(R.id.ed_modify_info)
    EditText edModifyInfo;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    @Bind(R.id.iv_modify_show)
    ImageView ivModifyShow;
    @Bind(R.id.recy_crtphoto2)
    RecyclerView recyCrtphoto2;
    @Bind(R.id.iv_modify_changeshow)
    TextView ivModifyChangeshow;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    private User my = null;
    private static String pic_path = null;
    private static String pic_path2 = null;
    public static final int REQUEST_CODE = 1000;
    public static final int REQUEST_CODE2 = 2000;
    private Image_editAdapter imagecrtnewsAdapter;
    private Image_editAdapter imagecrtnewsAdapter2;
    private ArrayList<String> path = new ArrayList<>();
    private ArrayList<String> path2 = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_star);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("修改星球内容");
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }
        context = Modify_starActivity.this;
//        my = BmobUser.getCurrentUser(context, User.class);
        getIntentDate();
    }

    /**
     * 得到activity 传过来的star值
     */
    public void getIntentDate() {
        Intent intent = getIntent();
        star = (Star) intent.getSerializableExtra("star");
        if (star != null) {//如果是通过传值过来的
            String list_news = star.getNews();
            String list_goods = star.getGoods();
            String list_photo = star.getPhoto();
            String list_comment = star.getComment();
            edModifyGlaxy.setText(star.getGalaxy().toString().trim());
            edModifyGlaxy.setEnabled(false);
            edModifyName.setText(star.getName().toString().trim());
            edModifyInfo.setText(star.getInfor().toString().trim());


            String url = (star.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/08/71935ed3405c04df8052fb759c4206d2.jpg" : star.getAvatar().getUrl();
            Picasso.with(context).load(url).fit()
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .error(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .into(ivModifyAvatar);

            String url2 = (star.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/08/71935ed3405c04df8052fb759c4206d2.jpg" : star.getShow().getUrl();
            Picasso.with(context).load(url2).fit()
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .error(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .into(ivModifyShow);


            if (!"OFF".equals(list_news)) {
                edEditlistNews.setText(list_news);
            } else {
                edEditlistNews.setText("还没有创建该类型列表");
                tvEditlistNews.setVisibility(View.GONE);
                edEditlistNews.setEnabled(false);
            }
            if (!"OFF".equals(list_goods)) {
                edEditlistGoods.setText(list_goods);
            } else {
                edEditlistGoods.setText("还没有创建该类型列表");
                edEditlistGoods.setEnabled(false);
                tvEditlistGoods.setVisibility(View.GONE);
            }
            if (!"OFF".equals(list_photo)) {
                edEditlistPhotos.setText(list_photo);
            } else {
                edEditlistPhotos.setText("还没有创建该类型列表");
                edEditlistPhotos.setEnabled(false);
                tvEditlistPhotos.setVisibility(View.GONE);
            }
            if (!"OFF".equals(list_comment)) {
                edEditlistComments.setText(list_comment);
            } else {
                edEditlistComments.setText("还没有创建该类型列表");
                edEditlistComments.setEnabled(false);
                tvEditlistComments.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 更新列表数据
     *
     * @param
     */
    public void uploadlist() {
        String news = edEditlistNews.getText().toString().trim();
        String goods = edEditlistGoods.getText().toString().trim();
        String photos = edEditlistPhotos.getText().toString().trim();
        String comment = edEditlistComments.getText().toString().trim();

        if (edEditlistNews.isEnabled()) {
            star.setNews(news);
            log.d("111");
        }
        if (edEditlistComments.isEnabled()) {
            star.setComment(comment);
            log.d("222");
        }
        if (edEditlistPhotos.isEnabled()) {
            star.setPhoto(photos);
        }
        if (edEditlistGoods.isEnabled()) {
            star.setGoods(goods);
        }
        star.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.show("修改成功");
                btnEditlist.setEnabled(true);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.show("修改失败" + s);
                btnEditlist.setEnabled(true);
            }
        });
    }

    /*
    更新星球信息
     */
    public void updatastar() {
        String galaxy = edModifyGlaxy.getText().toString().trim();
        String info = edModifyInfo.getText().toString().trim();
        String name = edModifyName.getText().toString().trim();

        if (!TextUtils.isEmpty(galaxy) && !TextUtils.isEmpty(info) && !TextUtils.isEmpty(name)) {
            star.setGalaxy(galaxy);
            star.setInfor(info);
            star.setName(name);
            star.update(context, new UpdateListener() {
                @Override
                public void onSuccess() {
                    Toast.show("更新星球信息成功");
                    Log.d("更新星球信息成功！", "000");
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.show("更新星球信息失败:" + s);
                    Log.d("更新星球信息失败！", "000");

                }
            });

        } else {
            Toast.show("更新星球信息失败，不能有空值！");
        }


    }

    @OnClick({R.id.tv_editlist_news, R.id.tv_editlist_goods, R.id.tv_editlist_photos, R.id.tv_editlist_comments, R.id.btn_editlist, R.id.iv_modify_changetx, R.id.iv_modify_changeshow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_editlist_news:
                delete_news("news");
                break;
            case R.id.tv_editlist_goods:
                delete_news("goods");
                break;
            case R.id.tv_editlist_photos:
                delete_news("photos");
                break;
            case R.id.tv_editlist_comments:
                delete_news("comments");
                break;
            case R.id.btn_editlist:
                if (pic_path != null) {
                    uppicavator();
                }
                if (pic_path2 != null) {
                    uppicshow();
                }
                uploadlist();
                updatastar();
                btnEditlist.setEnabled(false);
                break;
            case R.id.iv_modify_changetx:
                addpic_avatar();
                break;
            case R.id.iv_modify_changeshow:
                addpic_show();
                break;
        }
    }

    private void delete_news(String name) {
        tvEditlistNews.setEnabled(false);
        tvEditlistComments.setEnabled(false);
        tvEditlistPhotos.setEnabled(false);
        tvEditlistGoods.setEnabled(false);

        if ("news".equals(name)) {
            star.setNews("OFF");
        } else if ("goods".equals(name)) {
            star.setGoods("OFF");
        } else if ("photos".equals(name)) {
            star.setPhoto("OFF");
        } else if ("comments".equals(name)) {
            star.setComment("OFF");
        }

        star.update(context, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.show("删除列表成功");
                tvEditlistNews.setEnabled(true);
                tvEditlistComments.setEnabled(true);
                tvEditlistPhotos.setEnabled(true);
                tvEditlistGoods.setEnabled(true);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.show("删除列表失败");
                tvEditlistNews.setEnabled(true);
                tvEditlistComments.setEnabled(true);
                tvEditlistPhotos.setEnabled(true);
                tvEditlistGoods.setEnabled(true);
            }
        });
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
        recyCrtphoto.setLayoutManager(gridLayoutManager);
        imagecrtnewsAdapter = new Image_editAdapter(context, path);
        recyCrtphoto.setAdapter(imagecrtnewsAdapter);
        ivModifyAvatar.setVisibility(View.GONE);
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
        recyCrtphoto2.setLayoutManager(gridLayoutManager);
        imagecrtnewsAdapter2 = new Image_editAdapter(context, path2);
        recyCrtphoto2.setAdapter(imagecrtnewsAdapter2);
        ivModifyShow.setVisibility(View.GONE);
    }


    public class GlideLoader implements ImageLoader {
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

    /*
    更新头像
     */
    public void uppicavator() {
        final BmobFile bmobFile = new BmobFile(new File(pic_path));
        bmobFile.uploadblock(context, new UploadFileListener() {
            public void onSuccess() {
                ;
                star.setAvatar(bmobFile);
                star.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("更换头像成功", "");
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

            }
        });
    }

    /*
       更新头像
        */
    public void uppicshow() {
        final BmobFile bmobFile = new BmobFile(new File(pic_path2));
        bmobFile.uploadblock(context, new UploadFileListener() {
            public void onSuccess() {
                ;
                star.setShow(bmobFile);
                star.update(context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("更换头像成功", "");
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

            }
        });
    }

}


