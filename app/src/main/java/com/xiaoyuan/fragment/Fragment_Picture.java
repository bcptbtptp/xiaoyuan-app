package com.xiaoyuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyuan.Class.Photo;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.news;
import com.xiaoyuan.adapter.PictureAdapter;
import com.xiaoyuan.longer.Image_Activity;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Star_Activity;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

/**
 * Created by Axu on 2016/8/9.
 */
public class Fragment_Picture extends Fragment {
    public View view;
    RecyclerView recyclerView;
    public Context context;
    public boolean atbottom = true;
    public static int i = 8;
    SwipeRefreshLayout swipeRefreshLayout;
    public ArrayList<String> url = new ArrayList<String>();
    public static PictureAdapter pictureAdapter;
    public static List<Photo> photos = null;
    public static GridLayoutManager gridLayoutManager;
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
        view = inflater.inflate(R.layout.fragment_picture, null);
        context = getContext();
        star = Star_Activity.star;
        assignViews();
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

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        gridLayoutManager = new GridLayoutManager(context, 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(onScrollListener);
//        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
//        recyclerView.addItemDecoration(decoration);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            int lastPosition = -1;
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                int[] lastPositions = new int[((GridLayoutManager) staggeredGridLayoutManager).getSpanCount()];
//                ((GridLayoutManager) staggeredGridLayoutManager).findLastVisibleItemPositions(lastPositions);
//                lastPosition = findMax(lastPositions);
//                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
//
//                    if (atbottom) {
//                        getnewdata();
//                        atbottom = false;
//                        swipeRefreshLayout.setRefreshing(true);
//                    }
//
//                }
//            }
//        });

        getdatd();
        gettag();
        return view;
    }

    /**
     * 得到新闻的所有分类并且显示到ui
     * 会用到分组查询，
     */
    public void gettag() {
        BmobQuery<Photo> bmobQuery = new BmobQuery<Photo>();
        bmobQuery.groupby(new String[]{"type"});
        bmobQuery.addWhereEqualTo("star", star);
        bmobQuery.order("-updatedAt");
        bmobQuery.findStatistics(context, Photo.class, new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                JSONArray ary = (JSONArray) o;
                if (ary != null) {
                    int length = ary.length();
                    if(length == 1){//小于一个不用显示
                        return;
                    }
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
            int visible = gridLayoutManager.getChildCount();
            int total = gridLayoutManager.getItemCount();
            int past = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            if ((visible + past) >= total) {
                if (atbottom) {
                    atbottom = false;
                    swipeRefreshLayout.setRefreshing(true);
                    Log.d("位置", "visible=" + visible + "  past=" + past + "  total" + total);
                    getnewdata();
                }
            }
        }
    };

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }


    public void getdatd() {
        url.clear();
        BmobQuery<Photo> bmobQuery = new BmobQuery<Photo>();
        bmobQuery.setLimit(8);
        i = 8;
        bmobQuery.addWhereEqualTo("star",star);
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(context, new FindListener<Photo>() {
            public void onSuccess(List<Photo> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                photos = list;
                recyclerView.clearAnimation();
                pictureAdapter = new PictureAdapter(photos, context);
                pictureAdapter.setOnItemclicklister(itemclick);
                recyclerView.setAdapter(pictureAdapter);
                atbottom = true;
                for (int n = 0; n < list.size(); n++) {
                    Photo p = null;
                    p = list.get(n);
                    url.add(p.getPicture().getUrl());
//                    log.d("第" + n + "个：" + p.getObjectId());
                }
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
        BmobQuery<Photo> bmobQuery = new BmobQuery<Photo>();
        bmobQuery.setLimit(8);
        Log.d("num", i + "#");
        if(limit){//如果有类型限制
            bmobQuery.addWhereEqualTo("type",type);
        }
        bmobQuery.addWhereEqualTo("star",star);
        bmobQuery.setSkip(i); // 忽略前8条数据（即第一页数据结果）
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(context, new FindListener<Photo>() {
            @Override
            public void onSuccess(List<Photo> list) {
                Log.d("长度", list.size() + "#");
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                if (list.size() == 0) {
                    Toast.show("没有更多的内容啦~");
                    return;
                } else {
                    for (int n = 0; n < list.size(); n++) {
                        Photo p = null;
                        p = list.get(n);
                        url.add(p.getPicture().getUrl());
                        log.d("第" + n + "个：" + p.getObjectId());
                    }
                }
                photos = list;
                pictureAdapter.notifyDataSetChanged(photos);
                atbottom = true;
                i = i + 8;
            }

            @Override
            public void onError(int i, String s) {
                photos = null;
                Log.e("出错了_图片", s);
                Toast.show("加载失败");
                pictureAdapter.notifyDataSetChanged(photos);
                atbottom = true;
                i = i + 8;
            }
        });


    }

    PictureAdapter.Itemclick itemclick = new PictureAdapter.Itemclick() {

        public void OnItemclick(View v, int position) {
//            Photo pho = photos.get(position);
            Intent intent = new Intent(context, Image_Activity.class);
//            intent.putExtra("photo", pho);
            intent.putExtra("url", url);
            intent.putExtra("position", position);
            startActivity(intent);


        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //得到标签的名字，执行查询
            TextView tv = (TextView) v;
            type = tv.getText().toString();
            i = 0;
            limit = true;
            pictureAdapter.claerall();//清楚之前的数据
            getnewdata();
        }
    };


}
