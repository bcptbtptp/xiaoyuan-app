package com.xiaoyuan.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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

import com.xiaoyuan.Class.Star;
import com.xiaoyuan.adapter.StarsAdapter;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Star_Activity;
import com.xiaoyuan.longer.Ts_Activity;
import com.xiaoyuan.tools.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**搜索星球
 * A simple {@link Fragment} subclass.
 */
public class Fragment_select_star extends Fragment {


    @Bind(R.id.rv_main)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private GridLayoutManager gridLayoutManager;
    private static List<Star> stars = null;
    public static StarsAdapter starsAdapter;
    public String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ts_Activity ts_activity = (Ts_Activity) getActivity();
        ts_activity.setButtonClickedListener0(new Ts_Activity.OnButtonClickedListener0() {
            @Override
            public void onclicked(String s) {
                swipeRefreshLayout.setRefreshing(true);
                name = s;
                getdata();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_star, container, false);
        context = getContext();
        ButterKnife.bind(context, view);


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

        return view;
    }


    public void getdata() {
        BmobQuery<Star> bmobQuery = new BmobQuery<Star>();
        bmobQuery.addWhereContains("name",name);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
