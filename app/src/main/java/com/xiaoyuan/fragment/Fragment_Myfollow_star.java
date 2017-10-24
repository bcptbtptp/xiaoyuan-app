package com.xiaoyuan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuan.Class.My_fall_star;
import com.xiaoyuan.Class.My_fall_user;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.FollowStartAdapter;
import com.xiaoyuan.adapter.FollowUserAdapter;
import com.xiaoyuan.adapter.StarsAdapter;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Star_Activity;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Axu on 2016/8/menu_mystar.xml.
 */
public class Fragment_Myfollow_star extends Fragment {
    public Context context;
    private GridLayoutManager gridLayoutManager;
    public SwipeRefreshLayout swipeRefreshLayout;
    public static FollowStartAdapter followStartAdapter;
    public static RecyclerView recyclerView_followstar;
    private static List<Star> stars = new ArrayList<Star>();


    public Fragment_Myfollow_star() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfollow_star, container, false);
        context = getContext();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
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
        recyclerView_followstar = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView_followstar.setItemAnimator(new DefaultItemAnimator());
        recyclerView_followstar.setLayoutManager(gridLayoutManager);
        getdata();
        return view;
    }

    public void getdata() {
        stars.clear();
        BmobQuery<My_fall_star> query = new BmobQuery<My_fall_star>();
        User my = BmobUser.getCurrentUser(context, User.class);
        query.addWhereEqualTo("user", my);
        query.include("star");
        query.findObjects(context, new FindListener<My_fall_star>() {
            @Override
            public void onSuccess(List<My_fall_star> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                for(My_fall_star my_fall_star:list){
                    stars.add(my_fall_star.getStar());
//                    log.d(my_fall_star.getStar().getName());
                }
                followStartAdapter = new FollowStartAdapter(stars, context);
                followStartAdapter.setOnItemclicklister(itemclick);
                recyclerView_followstar.setAdapter(followStartAdapter);
            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.e("出错了_新闻图片", s);
                Toast.show("查询失败");
            }
        });
    }


    FollowStartAdapter.Itemclick itemclick = new FollowStartAdapter.Itemclick() {
        @Override
        public void OnItemclick(View view, int position) {
            Star star = stars.get(position);
            Intent intent = new Intent(context, Star_Activity.class);
            intent.putExtra("star", star);
            startActivity(intent);
        }
    };
}
