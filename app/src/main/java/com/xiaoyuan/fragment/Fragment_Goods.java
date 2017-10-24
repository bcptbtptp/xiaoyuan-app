package com.xiaoyuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyuan.Class.Goods;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.news;
import com.xiaoyuan.adapter.GoodsAdapter;
import com.xiaoyuan.adapter.NewsAdapter;
import com.xiaoyuan.longer.Message_Activity;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Star_Activity;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

/**
 * Created by Axu on 2016/8/4.
 */

public class Fragment_Goods extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    public View view;
    private Context context;
    public static int i = 8;
    public static RecyclerView recyclerView_goods;
    public boolean atbottom = true;
    public static GoodsAdapter goodsAdapter;
    public static List<Goods> goodses = null;
    public static LinearLayoutManager linearLayoutManager_goods;
    public Star star = null;
    private LinearLayout sqlLl;
    private TextView[] tvTag = new TextView[8];
    private CardView cardView;
    private boolean limit = false;//判断是否有类型的限制，true 为有
    public String type;

    private void assignViews() {
        sqlLl = (LinearLayout) view.findViewById(R.id.sql_ll);
        cardView = (CardView) view.findViewById(R.id.card_tag);
        tvTag[0] = (TextView) view.findViewById(R.id.tv_tag1);
        tvTag[1] = (TextView) view.findViewById(R.id.tv_tag2);
        tvTag[2] = (TextView) view.findViewById(R.id.tv_tag3);
        tvTag[3] = (TextView) view.findViewById(R.id.tv_tag4);
        tvTag[4] = (TextView) view.findViewById(R.id.tv_tag5);
        tvTag[5] = (TextView) view.findViewById(R.id.tv_tag6);
        tvTag[6] = (TextView) view.findViewById(R.id.tv_tag7);
        tvTag[7] = (TextView) view.findViewById(R.id.tv_tag8);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods, null);
        i = 8;
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
        context = getContext();
        star = Star_Activity.star;
        assignViews();
        linearLayoutManager_goods = new LinearLayoutManager(context);
        recyclerView_goods = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView_goods.setItemAnimator(new DefaultItemAnimator());
        recyclerView_goods.setLayoutManager(linearLayoutManager_goods);

        recyclerView_goods.addOnScrollListener(onScrollListener);
        getdatd_cache();
        gettag();
        return view;
    }

    /**
     * 得到新闻的所有分类并且显示到ui
     * 会用到分组查询，
     */
    public void gettag() {
        BmobQuery<Goods> bmobQuery = new BmobQuery<Goods>();
        bmobQuery.groupby(new String[]{"type"});
        bmobQuery.addWhereEqualTo("star", star);
        bmobQuery.order("-updatedAt");
        bmobQuery.findStatistics(context, Goods.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                JSONArray ary = (JSONArray) o;
                if (ary != null) {
                    int length = ary.length();
                    log.d(length+"cd");
                    if (length == 1) {//小于一个不用显示
                        return;
                    }
                    if (length > 4) {
                        sqlLl.setVisibility(View.VISIBLE);
                    }
                    ;
                    try {
                        cardView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = ary.getJSONObject(i);
                            String type = obj.getString("type");
                            tvTag[i].setText(type);
                            tvTag[i].setVisibility(View.VISIBLE);
                            tvTag[i].setOnClickListener(onClickListener);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    log.d("没有查询到分类");
                }
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //得到标签的名字，执行查询
            TextView tv = (TextView) v;
            type = tv.getText().toString();
            i = 0;
            limit = true;
            goodsAdapter.claerall();//清楚之前的数据
            getnewdata();
        }
    };

    /**
     * 滑动监听事件
     */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView_goods, int dx, int dy) {
            super.onScrolled(recyclerView_goods, dx, dy);
            int visible = linearLayoutManager_goods.getChildCount();
            int total = linearLayoutManager_goods.getItemCount();
            int past = linearLayoutManager_goods.findFirstCompletelyVisibleItemPosition();
            if ((visible + past) >= total) {
                if (atbottom) {
                    getnewdata();
                    atbottom = false;
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        }
    };


    public void getdatd() {
        BmobQuery<Goods> goods = new BmobQuery<Goods>();
        goods.setLimit(8);
        i = 8;
        goods.addWhereEqualTo("star", star);
        goods.order("-updatedAt");
        goods.findObjects(context, new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                goodses = list;
                goodsAdapter = new GoodsAdapter(goodses, context);
                recyclerView_goods.setAdapter(goodsAdapter);
                atbottom = true;
            }

            @Override
            public void onError(int i, String s) {
                Toast.show("查询失败");
            }
        });

    }

    public void getdatd_cache() {
        BmobQuery<Goods> goods = new BmobQuery<Goods>();
        goods.setLimit(8);
        goods.addWhereEqualTo("star", star);
        goods.order("-updatedAt");
        boolean isCache = goods.hasCachedResult(context, Goods.class);
        if (isCache) {
            goods.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        } else {
            goods.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        goods.findObjects(context, new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                goodses = list;
                goodsAdapter = new GoodsAdapter(goodses, context);
                recyclerView_goods.setAdapter(goodsAdapter);
                atbottom = true;
            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                Toast.show("查询失败");
            }
        });


    }


    public void getnewdata() {
        BmobQuery<Goods> bmobQuery = new BmobQuery<Goods>();
        bmobQuery.setLimit(10);
//        Log.d("num", i + "#");
        if(limit){//如果有类型限制
            bmobQuery.addWhereEqualTo("type",type);
        }
        bmobQuery.addWhereEqualTo("star", star);
        bmobQuery.setSkip(i); // 忽略前10条数据（即第一页数据结果）
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(context, new FindListener<Goods>() {
            @Override
            public void onSuccess(List<Goods> list) {
                Log.d("长度", list.size() + "#");
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                if (list.size() == 0) {
                    Toast.show("没有更多的内容啦~");
                    return;
                }
                goodses=list;
                goodsAdapter.notifyDataSetChanged(goodses);
                atbottom = true;
                i = i + 10;
            }

            @Override
            public void onError(int i, String s) {
                goodses = null;
                Log.e("出错了_新闻图片", s);
                Toast.show("加载失败");
                goodsAdapter.notifyDataSetChanged(goodses);
                atbottom = true;
                i = i + 10;
            }
        });


    }

}



