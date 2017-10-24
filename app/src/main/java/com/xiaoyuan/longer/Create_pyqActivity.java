package com.xiaoyuan.longer;

import android.app.Activity;
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
import com.xiaoyuan.Class.Image_pyq;
import com.xiaoyuan.Class.Pyq;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.Image_9Adapter;
import com.xiaoyuan.adapter.Image_crtnewsAdapter;
import com.xiaoyuan.tools.Snack;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**创建朋友圈的
 * Created by Axu on 2016/8/6.
 */
public class Create_pyqActivity extends AppCompatActivity {
    private EditText et_content;
    private Button btn_up;
    private TextView tv_jd;
    private TextView tv_addpic;
    public Context context;
    private ProgressBar pg;

    public static BmobFile bmobFile = new BmobFile();
    private Image_9Adapter imagecrtnewsAdapter;
    public static Pyq pyq = new Pyq();
    private ArrayList<String> path = new ArrayList<>();
    private static String pic_path = null;
    public static final int REQUEST_CODE = 1000;
    public User my = null;
    public int num;

    protected void onCreate(Bundle savedInstanceState) {
        bmobFile = null;
        pyq = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pyq);
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
        context = Create_pyqActivity.this;
        my = BmobUser.getCurrentUser(context, User.class);
        et_content = (EditText) findViewById(R.id.edtTxt_crtpyq_content);
        btn_up = (Button) findViewById(R.id.btn_crtpyq_up);
        tv_addpic = (TextView) findViewById(R.id.tv_crtpyq_addpic);
        tv_jd = (TextView) findViewById(R.id.tv_crtpyq_jd);
        pg = (ProgressBar) findViewById(R.id.progress_crtpyq_progressBar);
        btn_up.setOnClickListener(listener);
        tv_addpic.setOnClickListener(onclick_addpic);
    }

    View.OnClickListener onclick_addpic = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView recycler = (RecyclerView) findViewById(R.id.recy_crtpyq);

            ImageConfig imageConfig
                    = new ImageConfig.Builder(
                    new GlideLoader())
                    .steepToolBarColor(getResources().getColor(R.color.colorPrimaryDark))
                    .titleBgColor(getResources().getColor(R.color.colorPrimary))
                    .titleSubmitTextColor(getResources().getColor(R.color.white))
                    .titleTextColor(getResources().getColor(R.color.white))
                    .mutiSelectMaxSize(9)
                    .pathList(path)
                    .filePath("/ImageSelector/Pictures")
                    .showCamera()
                    .requestCode(REQUEST_CODE)
                    .build();

            ImageSelector.open((Activity) context, imageConfig);   // 开启图片选择器

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            recycler.setLayoutManager(gridLayoutManager);
            imagecrtnewsAdapter = new Image_9Adapter(context, path);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }

            path.clear();
            path.addAll(pathList);
            imagecrtnewsAdapter.notifyDataSetChanged();
        }
    }

    Button.OnClickListener listener = new Button.OnClickListener() {
        public void onClick(View v) {
            if (TextUtils.isEmpty(et_content.getText())) {
                Toast.show("动态内容不能为空");
                return;
            }
            //先上传动态，和图片，如果都成功了，给他们之间添加关系
            num = path.size();
            final String[] paths = (String[]) path.toArray(new String[num]);
//            log.d("长度："+paths.length);
            BmobFile.uploadBatch(context, paths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (list1.size() == paths.length) {//如果数量相等，则代表文件全部上传完成
                        //成功上传图片后，上传动态，以及批量添加数据到image_pyq
                        tv_jd.setText("上传完成");
                        uploadcontent(list);
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {
                    //1、curIndex--表示当前第几个文件正在上传
                    //2、curPercent--表示当前上传文件的进度值（百分比）
                    //3、total--表示总的上传文件数
                    //4、totalPercent--表示总的上传进度（百分比）
                    tv_jd.setText("正在上传第" + i + "张");
                    pg.setVisibility(View.VISIBLE);
                    pg.setProgress(i3);
                }

                @Override
                public void onError(int i, String s) {
                    Toast.show("上传图片失败" + s);
                }
            });


        }

    };

    /**
     * 添加动态，并得到动态的id
     */
    void uploadcontent(final List<BmobFile> pic) {

        final String content = et_content.getText().toString().trim();
        final Pyq pyq = new Pyq();
        pyq.setContent(content);
        pyq.setPl(0);
        pyq.setZf(0);
        pyq.setAuthor(my);
        pyq.setSc(0);
        pyq.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                //3.4.7  版本问题，发布消息之后无法得到该消息的objectid
                //先查询 出objectid
                BmobQuery<Pyq> query = new BmobQuery<Pyq>();
                query.order("-updatedAt");
                query.addWhereEqualTo("author", my);
                query.findObjects(context, new FindListener<Pyq>() {
                    @Override
                    public void onSuccess(List<Pyq> list) {
                        //批量将动态，当前用户，以及图片绑定在一起
                        uploadpic(list.get(0), pic);
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            ;

            @Override
            public void onFailure(int i, String s) {
                Snack.show(tv_jd,"发布动态失败");
                Log.d("发布动态失败", s);
            }
        });
    }

    /**
     * 批量将动态，当前用户，以及图片绑定在一起
     */
    void uploadpic(Pyq pyq, List<BmobFile> pic) {
        List<BmobObject> image_pyqes = new ArrayList<BmobObject>();

        for (int i = 0; i < num; i++) {
            Image_pyq image_pyq = new Image_pyq();
            image_pyq.setPicture(pic.get(i));
            image_pyq.setDt(pyq);
            image_pyqes.add(image_pyq);
        }
        new BmobObject().insertBatch(context, image_pyqes, new SaveListener() {
            @Override
            public void onSuccess() {
                Snack.show(tv_jd,"发布成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.show("发布失败" + s);
            }
        });

    }


}
