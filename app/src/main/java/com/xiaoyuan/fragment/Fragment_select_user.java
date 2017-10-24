package com.xiaoyuan.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoyuan.Class.My_fall_user;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.adapter.PeopleAdapter;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Ts_Activity;
import com.xiaoyuan.longer.User_other_Activity;
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
 * A simple {@link Fragment} subclass.
 */
public class Fragment_select_user extends Fragment {
    public Context context;
    private String name = "";
    public static RecyclerView recyclerView_people;
    public boolean atbottom = true;
    public static LinearLayoutManager linearLayoutManager_people;
    private static List<User> users = null;
    public SwipeRefreshLayout swipeRefreshLayout;
    public static PeopleAdapter peopleAdapter;
    public static List<User> mlist = new ArrayList<User>();

    public Fragment_select_user() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ts_Activity ts = (Ts_Activity) getActivity();
        ts.setButtonClickedListener1(new Ts_Activity.OnButtonClickedListener1() {
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

        View view = inflater.inflate(R.layout.fragment_select_user, container, false);
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

        linearLayoutManager_people = new LinearLayoutManager(context);
        recyclerView_people = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView_people.setItemAnimator(new DefaultItemAnimator());
        recyclerView_people.setLayoutManager(linearLayoutManager_people);
        return view;
    }

    public void getdata() {
        final User my = BmobUser.getCurrentUser(context, User.class);
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.order("-updatedAt");
        bmobQuery.addWhereContains("username", name);
        bmobQuery.addWhereNotEqualTo("username",my.getUsername());
        bmobQuery.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                users = list;
                //得到我关注的用户
                mlist.clear();
                BmobQuery<My_fall_user> bmobQuery = new BmobQuery<My_fall_user>();

                bmobQuery.addWhereEqualTo("user", new BmobPointer(my));
                bmobQuery.include("my_user");
                bmobQuery.findObjects(context, new FindListener<My_fall_user>() {
                    @Override
                    public void onSuccess(List<My_fall_user> list) {
                        if (list != null) {
                            for (My_fall_user my_fall_user : list) {
                                mlist.add(my_fall_user.getMy_user());
                                log.d("user:" + my_fall_user.getMy_user().getUsername());
                            }
                        }
                        peopleAdapter = new PeopleAdapter(users, context, mlist);
                        peopleAdapter.setOnItemclicklister(itemclick);
                        peopleAdapter.setOnDyClickLister(onDyClickLister);
                        recyclerView_people.setAdapter(peopleAdapter);
                        atbottom = true;
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.show("获取我的关注失败");
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Toast.show("查询用户");
            }
        });
    }

    PeopleAdapter.OnDyClickLister onDyClickLister = new PeopleAdapter.OnDyClickLister() {
        @Override
        public void OnDyClick(View view, int position) {
            User user = users.get(position);
            Intent intent = new Intent(context, User_other_Activity.class);
            intent.putExtra("user", user);
            Log.d(user.getNick(),user.getUsername());
            startActivity(intent);
        }
    };

    PeopleAdapter.Itemclick itemclick = new PeopleAdapter.Itemclick() {
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
                        Toast.show("订阅成功");
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
