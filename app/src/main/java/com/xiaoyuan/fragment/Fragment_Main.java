package com.xiaoyuan.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuan.Class.Goods;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.Ts;
import com.xiaoyuan.adapter.GoodsAdapter;
import com.xiaoyuan.adapter.MainAdapter;
import com.xiaoyuan.longer.MainActivity;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Star_Activity;
import com.xiaoyuan.tools.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class Fragment_Main extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    public Context context;
    public static int i = 8;
    public static List<Ts> tss = new ArrayList<Ts>();
    public boolean atbottom = true;
    public static RecyclerView recyclerView_main;
    public static MainAdapter mainAdapter;
    public static LinearLayoutManager linearLayoutManager_main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, null);
        context = getActivity();

        linearLayoutManager_main = new LinearLayoutManager(context);
        recyclerView_main = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView_main.setItemAnimator(new DefaultItemAnimator());
        recyclerView_main.setLayoutManager(linearLayoutManager_main);

        recyclerView_main.addOnScrollListener(onScrollListener);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipeRefresh));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getdatd();

                    }
                }, 500);
            }
        });
        getdatd();
        MainActivity.setbuttom(true);
        return view;
    }

    /**
     * 滑动监听事件
     */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView_goods, int dx, int dy) {
            super.onScrolled(recyclerView_goods, dx, dy);
            int visible = linearLayoutManager_main.getChildCount();
            int total = linearLayoutManager_main.getItemCount();
            int past = linearLayoutManager_main.findFirstCompletelyVisibleItemPosition();
            if ((visible + past) >= total) {
                if (atbottom) {
                    getnewdata();
                    atbottom = false;
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        }
    };

    /**
     * 查询数据
     */

    public void getdatd() {
        tss.clear();
        BmobQuery<Ts> ts = new BmobQuery<Ts>();
        ts.setLimit(8);
        i = 8;
        ts.include("star");
        ts.order("-updatedAt");
        ts.findObjects(context, new FindListener<Ts>() {
            @Override
            public void onSuccess(List<Ts> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                tss = list;
                mainAdapter = new MainAdapter(tss, context);
                mainAdapter.setOnItemclicklister(itemclick);
                recyclerView_main.setAdapter(mainAdapter);
                atbottom = true;
            }

            @Override
            public void onError(int i, String s) {
                Toast.show("查询失败" + s);
            }
        });

    }

    /**
     * 推送点击事件
     */
    MainAdapter.Itemclick itemclick = new MainAdapter.Itemclick() {
        @Override
        public void OnItemclick(View view, int position) {
            Star star = tss.get(position).getStar();
            Intent intent = new Intent(context, Star_Activity.class);
            intent.putExtra("star", star);
            startActivity(intent);
        }
    };

    /**
     * 刷新得到新数据
     */
    public void getnewdata() {
        BmobQuery<Ts> bmobQuery = new BmobQuery<Ts>();
        bmobQuery.setLimit(10);
//        Log.d("num", i + "#");
        bmobQuery.include("star");
        bmobQuery.setSkip(i); // 忽略前10条数据（即第一页数据结果）
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(context, new FindListener<Ts>() {
            @Override
            public void onSuccess(List<Ts> list) {
                Log.d("长度", list.size() + "#");
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                if (list.size() == 0) {
                    Toast.show("没有更多的内容啦~");
                    return;
                }
                tss.addAll(list);
                mainAdapter.notifyDataSetChanged(tss);
                atbottom = true;
                i = i + 10;
            }

            @Override
            public void onError(int i, String s) {
                tss = null;
                Log.e("出错了", s);
                Toast.show("加载失败" + s);
                atbottom = true;
            }
        });


    }
}
