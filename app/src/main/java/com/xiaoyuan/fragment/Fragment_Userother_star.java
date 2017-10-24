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
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.FollowStartAdapter;
import com.xiaoyuan.adapter.StarsAdapter;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Star_Activity;
import com.xiaoyuan.longer.User_other_Activity;
import com.xiaoyuan.tools.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Axu on 2016/8/18.
 */
public class Fragment_Userother_star  extends Fragment {

    public Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private static List<Star> stars = null;
    public static StarsAdapter starsAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userother_star, container, false);
recyclerView=(RecyclerView)view.findViewById(R.id.rv_main) ;
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


        gridLayoutManager = new GridLayoutManager(context,2);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(gridLayoutManager);

        getdata();
        return view;
    }

    public void getdata() {
        User other=User_other_Activity.user_other;
        BmobQuery<Star> bmobQuery = new BmobQuery<Star>();
        bmobQuery.addWhereEqualTo("father", new BmobPointer(other));
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(context, new FindListener<Star>() {
            @Override
            public void onSuccess(List<Star> list) {
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                stars = list;
                starsAdapter = new StarsAdapter(stars, context);
                starsAdapter.setOnItemclicklister(itemclick);
                recyclerView.setAdapter(starsAdapter);
            }

            @Override
            public void onError(int i, String s) {
                if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
//                newses = null;
                Log.e("出错了_新闻图片", s);
                Toast.show("查询失败");
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
