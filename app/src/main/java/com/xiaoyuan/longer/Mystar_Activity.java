package com.xiaoyuan.longer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.StarsAdapter;
import com.xiaoyuan.tools.Snack;
import com.xiaoyuan.tools.Toast;

import java.lang.reflect.Field;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Axu on 2016/8/14.
 */
public class Mystar_Activity extends AppCompatActivity {
    public Context context;
    private GridLayoutManager gridLayoutManager;
    private static List<Star> stars = null;
    public static StarsAdapter starsAdapter;
    public RecyclerView recyclerView;
    public CardView cardView_add;
    SwipeRefreshLayout swipeRefreshLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeActionOverflowMenuShown();
        setContentView(R.layout.activity_mystar);
        context = Mystar_Activity.this;


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipeRefresh));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getdata();
                    }
                }, 500);
            }
        });


        gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(gridLayoutManager);
        cardView_add = (CardView) findViewById(R.id.card_mystar);
        cardView_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Create_starActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("我的星球");
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        getdata();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mystar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_createstar) {
            Intent intent = new Intent(context, Create_starActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeActionOverflowMenuShown() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {

        }
    }


    public void getdata() {
        BmobQuery<Star> bmobQuery = new BmobQuery<Star>();
        bmobQuery.setLimit(8);
        User my = BmobUser.getCurrentUser(context, User.class);
        bmobQuery.addWhereEqualTo("father", my);
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(context, new FindListener<Star>() {
            @Override
            public void onSuccess(List<Star> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (list.size() == 0) {
                    Snackbar snack = Snackbar.make(swipeRefreshLayout, "创建我的星球吧！", Snackbar.LENGTH_LONG);
                    snack.show();
                    snack.setAction("right now", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(context, Create_starActivity.class));
                        }
                    });
                    return;
                }
                stars = list;
                starsAdapter = new StarsAdapter(stars, context);
                starsAdapter.setOnItemclicklister(itemclick);
                recyclerView.setAdapter(starsAdapter);
            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
//                newses = null;
                Log.e("出错了_新闻图片", s);
                Toast.show("查询失败" + s);
            }
        });
    }

    StarsAdapter.Itemclick itemclick = new StarsAdapter.Itemclick() {
        @Override
        public void OnItemclick(View view, int position) {
            Star star = stars.get(position);
            Intent intent = new Intent(context, Star_Activity.class);
            intent.putExtra("star", star);
            startActivity(intent);
        }
    };


}
