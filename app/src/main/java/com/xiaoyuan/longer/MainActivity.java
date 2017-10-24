package com.xiaoyuan.longer;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.xiaoyuan.Class.Appinfor;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.Config;
import com.xiaoyuan.fragment.Fragment_Gallery;
import com.xiaoyuan.fragment.Fragment_Main;
import com.xiaoyuan.fragment.Fragment_Pyq;
import com.xiaoyuan.others.ImageFactor;
import com.xiaoyuan.others.ScaleDownShowBehavior;
import com.xiaoyuan.tools.FileTools;
import com.xiaoyuan.tools.Snack;
import com.xiaoyuan.tools.log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Appinfor maxAppinfor;
    public ProgressDialog downloadDialog;
    private BottomSheetBehavior mBottomSheetBehavior;
    public FloatingActionButton fab;
    public String msavePath;
    private Context context;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private CoordinatorLayout root;
    private long exitTime;
    private ImageView iv_xx;
    private ImageView iv_pyq;
    private TextView tv_updata;
    private TextView tv_school;
    private ImageView iv_head_avatar;
    public TextView tv_name;
    public static MainActivity instance = null;//暴露给其他位置关闭主界面
    public DrawerLayout drawer;//用来关闭侧滑菜单
    public static LinearLayout ll_buttom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        context = MainActivity.this;
        instance = MainActivity.this;
        isupdata();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        iv_pyq = (ImageView) findViewById(R.id.iv_content_pyq);
        iv_xx = (ImageView) findViewById(R.id.iv_content_xx);
        tv_updata = (TextView) findViewById(R.id.tv_main_updata);
        ll_buttom = (LinearLayout) findViewById(R.id.main_buttom);
        tv_updata.setOnClickListener(updatalisten);


        //实时通讯
//        User user = BmobUser.getCurrentUser(this,User.class);
//        if(user == null){
//            log.d("id:");
//        }else {
//        log.d("id:"+user.getObjectId());}
//        BmobIM.connect(user.getObjectId(), new ConnectListener() {
//            @Override
//            public void done(String uid, BmobException e) {
//                if (e == null) {
//                    Logger.i("connect success");
//                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
////                    EventBus.getDefault().post(new RefreshEvent());
//                } else {
//                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
//                }
//            }
//        });


        //默认进入主界面
        fragmentchange(new Fragment_Main(), false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        root = (CoordinatorLayout) findViewById(R.id.root);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snack = Snackbar.make(root, "Replace with your own action", Snackbar.LENGTH_LONG);
                snack.show();
                snack.setAction("朕已阅", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });


        ScaleDownShowBehavior scaleDownShowFab = ScaleDownShowBehavior.from(fab);
        scaleDownShowFab.setOnStateChangedListener(onStateChangedListener);

        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.main_buttom));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        iv_head_avatar = (ImageView) headerView.findViewById(R.id.iv_head_avatar);
        iv_head_avatar.setOnClickListener(ivlisten);
        tv_name = (TextView) headerView.findViewById(R.id.tv_head_name);
        tv_school = (TextView) headerView.findViewById(R.id.tv_head_school);
        setdata();

    }


    /**
     * 有可用更新
     */
    public void updata() {
        if (maxAppinfor.getImportant()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("发现新版本");
            builder.setCancelable(false);
            builder.setMessage("必须更新\r\n" + maxAppinfor.getInfor());
            builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    download();
                }
            });

            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            builder.show();
        }
        if (!maxAppinfor.getImportant()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("发现新版本");
            builder.setMessage("更新" + maxAppinfor.getInfor());
            builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    download();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("忽略此次更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FileTools.saveshareString(context, "ignoreVersion", maxAppinfor.getVersion());

                }
            });
            builder.show();
        }
    }

    /**
     * 下载最新文件
     */
    public void download() {
        downloadDialog = new ProgressDialog(context);
        downloadDialog.setTitle("下载中请等待");
        downloadDialog.setMax(100);//设置最大值
        downloadDialog.setProgress(0);//进度归零
        downloadDialog.setCancelable(false);//按返回键取消
        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置风格为长方形
        downloadDialog.setCanceledOnTouchOutside(false);//点击外部取消
        downloadDialog.setIndeterminate(false);//设置进度是否明确
        downloadDialog.show();
        File path=new File(Environment.getExternalStorageDirectory()+"/"+maxAppinfor.getApp().getFilename());
        BmobFile bmobfile = new BmobFile("xiaoyuan.apk", "", maxAppinfor.getApp().getUrl());
        File saveFile = new File(path, bmobfile.getFilename());
        bmobfile.download(context, path, new DownloadFileListener() {
            @Override
            public void onSuccess(String s) {
                com.xiaoyuan.tools.Toast.show("下载成功,保存路径:" + s);
                Log.d("下载成功,保存路径:", s);
                msavePath = s;
                if (msavePath != null) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setDataAndType(Uri.fromFile(new File(msavePath)), "application/vnd.android.package-archive");
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                com.xiaoyuan.tools.Toast.show("下载失败：" + s);
            }

            @Override
            public void onProgress(Integer progress, long total) {
                super.onProgress(progress, total);
                downloadDialog.setProgress(progress);
//                Log.d(Integer.toString(progress), "#######");
            }

            @Override
            public void onStart() {
                super.onStart();
                com.xiaoyuan.tools.Toast.show("开始下载...");
            }
        });


    }

    /**
     * 监测更新
     * 记得每次发布新版本要在config 以及 清单文件中，以及Bmob中修改版本信息
     */

    public void isupdata() {
        BmobQuery<Appinfor> bmobQuery = new BmobQuery<Appinfor>();
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(context, new FindListener<Appinfor>() {
            @Override
            public void onSuccess(List<Appinfor> list) {
                if (list != null) {
                    maxAppinfor = list.get(0);
                } else {
                    log.d("没有发现新版本");
                    return;
                }
                log.d("得到版本号：" + maxAppinfor.getVersion());
                String ignoreVersion = FileTools.getshareString(context, "ignoreVersion");
                ignoreVersion = ignoreVersion == null ? "1.0" : ignoreVersion;//防止用户第一次使用没有数据
                //比较版本数据库里新版本与本地版本
                if (!Config.version.equals(maxAppinfor.getVersion())) {//用户没有忽略此次更新
                    if (!ignoreVersion.equals(maxAppinfor.getVersion()))
                        updata();
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d("查询更新失败了", "");
            }
        });
    }


    /*
    设置数据，用户名，头像，学校
     */
    public void setdata() {
        User user = BmobUser.getCurrentUser(context, User.class);
        if (user != null) {
            tv_name.setText(user.getUsername());
            tv_school.setText(user.getSchool());

            String url = (user.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/08/71935ed3405c04df8052fb759c4206d2.jpg" : user.getAvatar().getUrl();
            Picasso.with(context).load(url).fit()
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .error(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .transform(new ImageFactor())
                    .into(iv_head_avatar);
        }
    }


    /*
    点击更新按钮
     */
    public TextView.OnClickListener updatalisten = new TextView.OnClickListener() {
        public void onClick(View view) {
            fragmentchange(new Fragment_Gallery(), true);
            drawer.closeDrawer(GravityCompat.START);
        }
    };

    /*
      点击头像监听事件
     */
    public ImageView.OnClickListener ivlisten = new ImageView.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    };


    /*
    设置下滑上滑，影藏主页下面的按钮
     */
    private ScaleDownShowBehavior.OnStateChangedListener onStateChangedListener = new ScaleDownShowBehavior.OnStateChangedListener() {
        @Override
        public void onChanged(boolean isShow) {
            mBottomSheetBehavior.setState(isShow ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
        }
    };

    private boolean initialize = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!initialize) {
            initialize = true;
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_chat) {
            Intent intent = new Intent(this, Chat_Activityh.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_create_pyq) {
            Intent intent = new Intent(this, Create_pyqActivity.class);
            startActivity(intent);
        }else if (id == R.id.action_search) {
            Intent intent = new Intent(context, Ts_Activity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "该功能还在建设中", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //左侧菜单按键
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_vip) {
            Snack.show(tv_name, "该功能还未开放！！");
        } else if (id == R.id.nav_myfollow) {
            Intent intent = new Intent(this, Myfollow_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_user) {
            Intent intent = new Intent(this, User_Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mystar) {
            Intent intent = new Intent(this, Mystar_Activity.class);
            startActivity(intent);
        }
        //隐藏菜单栏
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Fragment 窗口的切换 back == true 放入任务栈 ，false 为不放入
    public void fragmentchange(Fragment frag, boolean back) {
        fm = getFragmentManager();
        int back_num = fm.getBackStackEntryCount();
        if (back_num > 0) {
            fm.popBackStack();
        }
        ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragment_main, frag);
        if (back) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onclick_xinxi(View view) {
        iv_xx.setBackground(ContextCompat.getDrawable(context, R.drawable.xx_checked));
        iv_pyq.setBackground(ContextCompat.getDrawable(context, R.drawable.pyq));
        fragmentchange(new Fragment_Main(), false);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onclick_friend(View view) {
        iv_pyq.setBackground(ContextCompat.getDrawable(context, R.drawable.pyq_checkes));
        iv_xx.setBackground(ContextCompat.getDrawable(context, R.drawable.xx));
        fragmentchange(new Fragment_Pyq(), false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Snackbar.make(root, "再按一次推出校源", Snackbar.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }
    /**
     * 设置底部菜单颜色，（用来区分星系和朋友圈的颜色）
     * 星系为true  朋友圈为false
     */
    public static void setbuttom(boolean pd) {
        if (pd) {
            ll_buttom.setBackgroundColor(Color.parseColor("#1e2934"));
        } else {
            ll_buttom.setBackgroundColor(Color.parseColor("#ffffff"));
        }

    }
}
