package com.xiaoyuan.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoyuan.Class.My_fall_user;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.FollowUserAdapter;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Axu on 2016/8/menu_mystar.xml.
 */
public class Fragment_Myfollow_user extends Fragment {
    private static List<User> users = new ArrayList<User>();
    public Context context;
    public SwipeRefreshLayout swipeRefreshLayout;
    public static LinearLayoutManager linearLayoutManager_MyfollowUser;
    public static RecyclerView recyclerView_MyfollowUser;
    public static FollowUserAdapter followUserAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myfollow_user, container, false);
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

        linearLayoutManager_MyfollowUser = new LinearLayoutManager(context);
        recyclerView_MyfollowUser = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView_MyfollowUser.setItemAnimator(new DefaultItemAnimator());
        recyclerView_MyfollowUser.setLayoutManager(linearLayoutManager_MyfollowUser);
        getdata();
        return view;
    }

    public void getdata() {
        users.clear();
        //查询数据
        BmobQuery<My_fall_user> bmobQuery = new BmobQuery<My_fall_user>();
        User my = BmobUser.getCurrentUser(context, User.class);
        bmobQuery.addWhereEqualTo("user", new BmobPointer(my));
        bmobQuery.include("my_user");
        bmobQuery.findObjects(context, new FindListener<My_fall_user>() {
            @Override
            public void onSuccess(List<My_fall_user> list) {
                if (list != null) {
                    for (My_fall_user my_fall_user : list) {
                        User user = my_fall_user.getMy_user();
//                        log.d("我关注用户的id:" + user.getObjectId());
                        users.add(user);
                    }
                    followUserAdapter = new FollowUserAdapter(users, context);
                    followUserAdapter.setOnItemclicklister(itemclick);
                    followUserAdapter.setOnDyClickLister(dyclick);
                    recyclerView_MyfollowUser.setAdapter(followUserAdapter);
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.show("获取我的关注失败");
            }
        });
    }

    FollowUserAdapter.OnDyClickLister dyclick = new FollowUserAdapter.OnDyClickLister() {
        @Override
        public void OnDyClick(View view, int position) {
//            User user = users.get(position);
//            Intent intent = new Intent(context, MyInforActivity.class);
//            intent.putExtra("user", user);
//            startActivity(intent);
        }
    };


    FollowUserAdapter.Itemclick itemclick = new FollowUserAdapter.Itemclick() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void OnItemclick(final View view, int position) {
            final TextView tv = (TextView) view;
            String tv_dy = tv.getText().toString();
            if ("订阅".equals(tv_dy)) {
                User my = BmobUser.getCurrentUser(context, User.class);
                My_fall_user user = new My_fall_user();
                user.setMy_user(users.get(position));
                user.setUser(my);
                user.save(context, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        tv.setText("已订阅");
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        tv.setBackground(getActivity().getResources().getDrawable(R.drawable.textview_myfollow_ydy));
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.show("订阅失败");
                        log.d("错误信息:" + s);
                    }
                });
            } else {
                BmobQuery<My_fall_user> bmobQuery = new BmobQuery<My_fall_user>();
                User my = BmobUser.getCurrentUser(context, User.class);
                bmobQuery.addWhereEqualTo("user", new BmobPointer(my));
                bmobQuery.addWhereEqualTo("my_user", new BmobPointer(users.get(position)));
                bmobQuery.findObjects(context, new FindListener<My_fall_user>() {
                    @Override
                    public void onSuccess(List<My_fall_user> list) {
                        if (list != null) {
                            String objectId = list.get(0).getObjectId();
                            My_fall_user my_fall_user = new My_fall_user();
                            my_fall_user.setObjectId(objectId);
                            my_fall_user.delete(context, new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    tv.setText("订阅");
                                    tv.setTextColor(Color.parseColor("#8fc31f"));
                                    tv.setBackground(getActivity().getResources().getDrawable(R.drawable.textview_myfollow_dy));
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.show("取消订阅失败2" + s);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.show("取消订阅失败1" + s);
                    }
                });

            }


        }
    };


}
