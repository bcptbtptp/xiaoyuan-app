package com.xiaoyuan.longer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.My_fall_star;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.Ts;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.fragment.Fragment_Comment;
import com.xiaoyuan.fragment.Fragment_Goods;
import com.xiaoyuan.fragment.Fragment_News;
import com.xiaoyuan.fragment.Fragment_Picture;
import com.xiaoyuan.tools.Snack;
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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Axu on 2016/8/10.
 */
public class Star_Activity extends AppCompatActivity {

    @Bind(R.id.iv_starinfor)
    ImageView ivStarinfor;
    @Bind(R.id.tv_starinfor_name)
    TextView tvStarinforName;
    @Bind(R.id.tv_starinfor_infor)
    TextView tvStarinforInfor;
    @Bind(R.id.tv_star_gz)
    TextView tvStarGz;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.container)
    ViewPager container;
    @Bind(R.id.fab_star_edit)
    FloatingActionButton fabStarEdit;
    @Bind(R.id.fab_star_ts)
    FloatingActionButton fabStarTs;
    @Bind(R.id.fab_star_add)
    FloatingActionButton fabStarAdd;
    private ViewPager viewpage;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> list_text = new ArrayList<>();
    private Context context;
    public static Star star;
    public TextView tv_galaxy;
    private User my = null;
    public boolean first = false;//是不是刚进入界面（原来刷新，添加，删除后星球内容显示的问题）
    private PopupWindow mPopWindows2;// 课程详细
    public EditText ed_ts;//推送窗口的内容
    private ArrayList<String> path = new ArrayList<>();
    private static String pic_path = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        ButterKnife.bind(this);
        star = null;
        tv_galaxy = (TextView) findViewById(R.id.iv_star_galaxy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    finish();
                }
            });
        }
        context = Star_Activity.this;
        my = BmobUser.getCurrentUser(context, User.class);
        Intent intent = getIntent();
        star = (Star) intent.getSerializableExtra("star");
        getIntentDate();
    }

    @Override
    protected void onStart() {
        if (first) {
            //重新得到星球的信息
            BmobQuery<Star> query = new BmobQuery<Star>();
            query.getObject(context, star.getObjectId(), new GetListener<Star>() {
                @Override
                public void onSuccess(Star mstar) {
                    star = mstar;
                    getIntentDate();
                    first = false;
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.show("查询星球出错啦");
                }
            });
        }

        super.onStart();
    }

    /**
     * 得到activity 传过来的star值
     */
    public void getIntentDate() {
        if (star != null) {//如果是通过传值过来的
            tvStarinforName.setText(star.getName());
            tv_galaxy.setText(star.getGalaxy());
            tvStarinforInfor.setText(star.getInfor());
            String url = (star.getShow() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/11/2d673b7140b144a9801cd607ae3f1811.jpg" : star.getShow().getUrl();
            Picasso.with(context).load(url)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(ivStarinfor);
            //检查是否为星球的创建者，如果是，隐藏关注，显示右上角子菜单
            check();

            //遍历星球的列表
            getfragment();
        }
    }

    /***
     * 遍历星球的列表,设置适配器
     */
    private void getfragment() {
        String list_news = star.getNews();
        String list_goods = star.getGoods();
        String list_photo = star.getPhoto();
        String list_comment = star.getComment();

        fragments.clear();
        list_text.clear();
        Fragment fra_news = new Fragment_News();
        Fragment fra_goods = new Fragment_Goods();
        Fragment fra_photo = new Fragment_Picture();
        Fragment fra_comment = new Fragment_Comment();

        if (!"OFF".equals(list_news)) {
            fragments.add(fra_news);
            list_text.add(list_news);
        }
        if (!"OFF".equals(list_goods)) {
            fragments.add(fra_goods);
            list_text.add(list_goods);
        }
        if (!"OFF".equals(list_photo)) {
            fragments.add(fra_photo);
            list_text.add(list_photo);
        }
        if (!"OFF".equals(list_comment)) {
            fragments.add(fra_comment);
            list_text.add(list_comment);
        }
        if (list_text.size() == 0) {
            Snack.show(tvStarGz, "ta不懒，就是不想创建一点内容~");
            return;
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments, list_text);
        viewpage = (ViewPager) findViewById(R.id.container);
        viewpage.setAdapter(mSectionsPagerAdapter);

        tabs.setupWithViewPager(viewpage);
    }


    /**
     * 检查当前星球是否存在以关注的列表
     */
    public void check() {
        BmobQuery<My_fall_star> bmobQuery = new BmobQuery<My_fall_star>();
        bmobQuery.addWhereEqualTo("user", new BmobPointer(my));
        bmobQuery.addWhereEqualTo("star", new BmobPointer(star));
        bmobQuery.findObjects(context, new FindListener<My_fall_star>() {
            @Override
            public void onSuccess(List<My_fall_star> list) {
                if (list != null && list.size() != 0) {
                    tvStarGz.setText("取消关注");
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.show("获取关注失败");
            }
        });

    }

    @OnClick(R.id.tv_star_gz)
    public void onClick() {
        String gz = tvStarGz.getText().toString();
        if ("关注".equals(gz)) {//添加关注
            User my = BmobUser.getCurrentUser(context, User.class);
            My_fall_star my_fall_star = new My_fall_star();
            my_fall_star.setStar(star);
            my_fall_star.setUser(my);
            my_fall_star.save(context, new SaveListener() {
                @Override
                public void onSuccess() {
                    tvStarGz.setText("取消关注");
                    Toast.show("关注成功");
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.show("关注失败" + s);
                }
            });
        } else {//取消关注
            BmobQuery<My_fall_star> bmobQuery = new BmobQuery<My_fall_star>();
            bmobQuery.addWhereEqualTo("user", new BmobPointer(my));
            bmobQuery.addWhereEqualTo("star", new BmobPointer(star));
            bmobQuery.findObjects(context, new FindListener<My_fall_star>() {
                @Override
                public void onSuccess(List<My_fall_star> list) {
                    if (list != null) {
                        String objectId = list.get(0).getObjectId();
                        My_fall_star my_fall_star = new My_fall_star();
                        my_fall_star.setObjectId(objectId);
                        my_fall_star.delete(context, new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                tvStarGz.setText("关注");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.show("取消关注失败" + s);
                            }
                        });
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Toast.show("取消关注失败" + s);
                }
            });
        }
    }


    /**
     * 三点菜单
     *
     * @param menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_star, menu);
        //判断是否为创建者，如果不是这隐藏菜单
        checkfather(menu);
        return true;
    }

    /**
     * 判断是否为创建者，如果不是这隐藏菜单
     */
    private void checkfather(Menu menu) {
        if (star.getFather() != null) {
            String id = star.getFather().getObjectId();
            if (!my.getObjectId().equals(id)) {
                if (null != menu) {
                    for (int i = 0; i < menu.size(); i++) {
                        menu.getItem(i).setVisible(false);
                        menu.getItem(i).setEnabled(false);
                        fabStarAdd.setAlpha(0);
                        fabStarAdd.getBackground().setAlpha(0);
                        fabStarAdd.setEnabled(false);
                        fabStarEdit.setAlpha(0);
                        fabStarEdit.getBackground().setAlpha(0);
                        fabStarEdit.setEnabled(false);
                        fabStarTs.setAlpha(0);
                        fabStarTs.getBackground().setAlpha(0);
                        fabStarTs.setEnabled(false);
                    }
                }
            } else {//如果是创建者
                //隐藏关注按钮，显示下面的3个fab
                tvStarGz.setVisibility(View.GONE);
                fabStarAdd.setRotation(0);
                fabStarAdd.setVisibility(View.VISIBLE);
                fabStarEdit.setVisibility(View.VISIBLE);
                fabStarTs.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_createlist) {
            Intent intent = new Intent(context, Create_listActivity.class);
            intent.putExtra("star", star);
            startActivity(intent);
        }
        first = true;
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.fab_star_edit, R.id.fab_star_ts, R.id.fab_star_add})
    public void onClick(View view) {
        Intent intent = null;
        first = true;
        switch (view.getId()) {
            case R.id.fab_star_edit:
                intent = new Intent(context, Modify_starActivity.class);
                intent.putExtra("star", star);
                startActivity(intent);
                break;
            case R.id.fab_star_ts:
                show_pop();
                break;
            case R.id.fab_star_add:
                intent = new Intent(context, Add_contentActivity.class);
                intent.putExtra("star", star);
                startActivity(intent);
                break;
        }
    }

    /**
     * 显示出推送消息的窗口
     */
    public void show_pop() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_star_ts, null);
        mPopWindows2 = new PopupWindow(contentView);
        mPopWindows2.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindows2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindows2.setFocusable(true);

        TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_star_cancel);
        TextView tv_ts = (TextView) contentView.findViewById(R.id.tv_star_ts);
        TextView tv_pic = (TextView) contentView.findViewById(R.id.tv_star_pic);
        ed_ts = (EditText) contentView.findViewById(R.id.ed_star_popup);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindows2.dismiss();
            }
        });
        tv_ts.setOnClickListener(onClickListener);
        tv_pic.setOnClickListener(clickListener);

        mPopWindows2.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }

    //popuwindows 的添加图片按钮
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageConfig imageConfig
                    = new ImageConfig.Builder(
                    new GlideLoader())
                    .steepToolBarColor(getResources().getColor(R.color.colorPrimaryDark))
                    .titleBgColor(getResources().getColor(R.color.colorPrimary))
                    .titleSubmitTextColor(getResources().getColor(R.color.white))
                    .titleTextColor(getResources().getColor(R.color.white))
                    .singleSelect()
                    // 多选时的最大数量   （默认 9 张）
//                    .mutiSelectMaxSize(1)
                    // 已选择的图片路径
                    .pathList(path)
                    // 拍照后存放的图片路径（默认 /temp/picture）
                    .filePath("/ImageSelector/Pictures")
                    // 开启拍照功能 （默认开启）
                    .showCamera()
                    .requestCode(REQUEST_CODE)
                    .build();

            ImageSelector.open((Activity) context, imageConfig);   // 开启图片选择器
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }

            path.clear();
            pic_path = pathList.get(pathList.size() - 1);
//            path.add(pic_path);
            log.d("选择的图片：" + pic_path);
        }
    }

    //点击推送后的事件
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String content = ed_ts.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.show("推送内容不能为空哦");
                return;
            }
            if (pic_path != null) {
                log.d("选择了图片");
                //////先上传图片
                final BmobFile bmobFile = new BmobFile(new File(pic_path));
                bmobFile.uploadblock(context, new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        Ts ts = new Ts();
                        ts.setContent(content);
                        ts.setStar(star);
                        ts.setPicture(bmobFile);
                        ts.save(context, new SaveListener() {
                            @Override
                            public void onSuccess() {
                                Toast.show("推送成功");
                                pic_path = null;
                                path.clear();
                                mPopWindows2.dismiss();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                pic_path = null;
                                path.clear();
                                Toast.show("推送失败");
                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.show("推送失败，上传图片：" + s);
                    }
                });
            }else{//如果没有选择图片，直接是文字推送
                log.d("没有选择图片");
                Ts ts = new Ts();
                ts.setContent(content);
                ts.setStar(star);
                ts.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.show("推送成功");
                        mPopWindows2.dismiss();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.show("推送失败");
                    }
                });
            }

        }
    };

    /**
     * tablayout 菜单
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public List<Fragment> mfragments;
        public List<String> mlist_text;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> list_text) {
            super(fm);
            mfragments = fragments;
            mlist_text = list_text;
        }

        @Override
        public Fragment getItem(int position) {
            return mfragments.get(position);
        }

        @Override
        public int getCount() {
            return mlist_text.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mlist_text.get(0);
                case 1:
                    return mlist_text.get(1);
                case 2:
                    return mlist_text.get(2);
                case 3:
                    return mlist_text.get(3);
            }
            return null;
        }
    }
}


