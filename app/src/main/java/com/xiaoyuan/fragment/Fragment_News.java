package com.xiaoyuan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.news;
import com.xiaoyuan.adapter.NewsAdapter;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Star_Activity;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

public class Fragment_News extends Fragment  {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private static List<news> newses = null;
    public static int i = 8;
    public boolean atbottom = true;//到达底部，没有加载数据之前不会重复执行
    public View view;
    public static NewsAdapter newsAdapter;
    public static RecyclerView recyclerView;
    public static LinearLayoutManager linearLayoutManager;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_user, null);
        i = 8;
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
//        swipeRefreshLayout.setRefreshing(true);//进入时刷新的动画
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipeRefresh));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
//                Toast.show("在刷新");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getdata();
                    }
                }, 500);
            }
        });
        context = getContext();
        assignViews();
        star = Star_Activity.star;
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(onScrollListener);
        //默认查询所有的数据，同时查询分类
        getdata_cache();
        gettag();
        return view;
    }

    /**
     * 得到新闻的所有分类并且显示到ui
     * 会用到分组查询，
     */
    public void gettag() {
        BmobQuery<news> bmobQuery = new BmobQuery<news>();
        bmobQuery.groupby(new String[]{"type"});
        bmobQuery.addWhereEqualTo("star", star);
        bmobQuery.order("-updatedAt");
        bmobQuery.findStatistics(context, news.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                JSONArray ary = (JSONArray) o;
                if (ary != null) {
                    int length = ary.length();
                    if (length > 4) {
                        sqlLl.setVisibility(View.VISIBLE);
                    }                    ;
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

    /**
     * recycleview 的滑倒底部监听事件
     */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visible = linearLayoutManager.getChildCount();
            int total = linearLayoutManager.getItemCount();
            int past = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if ((visible + past) >= total) {
                if (atbottom) {
                    atbottom = false;
                    swipeRefreshLayout.setRefreshing(true);
//                    Log.d("text", "visible=" + visible + "  past=" + past + "  total" + total);
                    getnewdatd();
                }
            }
        }
    };


    public void getdata() {
        BmobQuery<news> bmobQuery = new BmobQuery<news>();
        bmobQuery.setLimit(8);
        i = 8;
        bmobQuery.order("-updatedAt");
        bmobQuery.addWhereEqualTo("star", star);
        bmobQuery.findObjects(context, new FindListener<news>() {
            @Override
            public void onSuccess(List<news> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                newses = list;
                newsAdapter = new NewsAdapter(newses, context);
                recyclerView.setAdapter(newsAdapter);
                atbottom = true;
            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                newses = null;
                Log.e("出错了_新闻图片", s);
                Toast.show("查询失败");
                newsAdapter = new NewsAdapter(newses, context);
                recyclerView.setAdapter(newsAdapter);
                atbottom = true;
            }
        });
    }

    public void getdata_cache() {
        BmobQuery<news> bmobQuery = new BmobQuery<news>();
        bmobQuery.setLimit(8);
        bmobQuery.addWhereEqualTo("star", star);
        bmobQuery.order("-updatedAt");
//        boolean isCache = bmobQuery.hasCachedResult(context, news.class);
//        if (isCache) {
//            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
//        } else {
//            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//        }
        bmobQuery.findObjects(context, new FindListener<news>() {
            @Override
            public void onSuccess(List<news> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                newses = list;
                newsAdapter = new NewsAdapter(newses, context);
                recyclerView.setAdapter(newsAdapter);
                atbottom = true;
            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                newses = null;
                Log.e("出错了_新闻图片", s);
                Toast.show("查询失败");
                newsAdapter = new NewsAdapter(newses, context);
                recyclerView.setAdapter(newsAdapter);
                atbottom = true;
            }
        });
    }

    public void getnewdatd() {
        BmobQuery<news> bmobQuery = new BmobQuery<news>();
        bmobQuery.setLimit(10);
        Log.d("num", i + "#");
        if(limit){//如果有类型限制
            bmobQuery.addWhereEqualTo("type",type);
        }
        bmobQuery.addWhereEqualTo("star", star);
        bmobQuery.setSkip(i); // 忽略前10条数据（即第一页数据结果）
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(context, new FindListener<news>() {
            @Override
            public void onSuccess(List<news> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                if (list.size() == 0) {
                    Toast.show("没有更多的内容啦~");
                    return;
                }
                newses = list;
                newsAdapter.notifyDataSetChanged(newses);
                atbottom = true;
                i = i + 10;

            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                newses = null;
                Log.e("出错了_新闻图片", s);
                Toast.show("加载失败");
                newsAdapter.notifyDataSetChanged(newses);
                atbottom = true;
                i = i + 10;
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
            newsAdapter.claerall();//清楚之前的数据
            getnewdatd();
        }
    };

}
