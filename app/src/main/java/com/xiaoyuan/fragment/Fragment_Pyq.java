package com.xiaoyuan.fragment;


import android.app.Fragment;
import android.content.Context;
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

import com.xiaoyuan.Class.Image_pyq;
import com.xiaoyuan.Class.Pyq;
import com.xiaoyuan.adapter.PyqAdapter;
import com.xiaoyuan.longer.MainActivity;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Fragment_Pyq extends Fragment {

    private View view;
    public SwipeRefreshLayout swipeRefreshLayout;
    public static int i;
    public Context context;
    public static List<Pyq> pyqs = null;
    public boolean atbottom = true;
    public static PyqAdapter pyqAdapter;
    public static LinearLayoutManager linearLayoutManager_pyq;
    public static RecyclerView recyclerView_pyq;
    public List<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();//所有动态配图链表
    public HashMap<Integer, ArrayList<String>> map = new LinkedHashMap<Integer, ArrayList<String>>();
    public boolean newdata = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pyq, null);
        i = 8;
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipeRefresh));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        getdatd();
                    }
                }, 1000);
            }
        });
        context = getActivity();

        linearLayoutManager_pyq = new LinearLayoutManager(context);
        recyclerView_pyq = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView_pyq.setItemAnimator(new DefaultItemAnimator());
        recyclerView_pyq.setLayoutManager(linearLayoutManager_pyq);

        recyclerView_pyq.addOnScrollListener(onScrollListener);
        getdatd();
        //更改底部颜色
        MainActivity.setbuttom(false);
        return view;
    }

    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView_pyq, int dx, int dy) {
            super.onScrolled(recyclerView_pyq, dx, dy);
            int visible = linearLayoutManager_pyq.getChildCount();
            int total = linearLayoutManager_pyq.getItemCount();
            int past = linearLayoutManager_pyq.findFirstCompletelyVisibleItemPosition();
//            log.d("visible" + visible + "  past:" + past + "  total:" + total);
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
     * 查询到每条动态
     */
    public void getdatd() {
        newdata = false;
        arrayLists.clear();
        BmobQuery<Pyq> pyq = new BmobQuery<Pyq>();
        pyq.setLimit(8);
        i = 8;
        pyq.include("author");
        pyq.order("-updatedAt");
        pyq.findObjects(context, new FindListener<Pyq>() {
            @Override
            public void onSuccess(List<Pyq> list) {

                if (list == null) {
                    log.d("动态为空");
                    return;
                }
                log.d("获取动态成功");
                pyqs = list;
                //查询每条动态的图片地址，保存到链表
                getpic(list);
            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                Log.d("查询失败", "###");
                Toast.show("查询失败");
            }
        });
    }

    /**
     * 查询到每个动态的配图，添加到链表
     *
     * @param list
     */
    private void getpic(List<Pyq> list) {
        map.clear();
        for (int i = 0; i < list.size(); i++) {
            final Pyq pyq = list.get(i);
            final int j = i;
            final int dtnum = list.size();
//            log.d("dtnum"+dtnum);
            BmobQuery<Image_pyq> query = new BmobQuery<Image_pyq>();
            query.addWhereEqualTo("dt", new BmobPointer(pyq));
            query.findObjects(context, new FindListener<Image_pyq>() {
                @Override
                public void onSuccess(List<Image_pyq> list) {

                    //如果没有配图也要加进去
                    ArrayList<String> pic_list = new ArrayList<String>();
//                    log.d("这是条" + j + "动态有多少张图：" + list.size() + "  动态名：" + pyq.getContent());
                    if (list.size() != 0) {
                        String pa = null;
                        for (Image_pyq image_pyq : list) {
                            pa = image_pyq.getPicture().getUrl();
                            pic_list.add(pa);
                        }
                        map.put(j, pic_list);
                    } else {//如果没有配图也要加进去
                        map.put(j, null);
                    }
//                    log.d("map长度：" + map.size());
                    if (map.size() == dtnum) {//如果已经加载完毕，对map进行排序，添加进入链表,可以设置适配器了
                        for (int k = 0; k < map.size(); k++) {
                            arrayLists.add(map.get(k));
                        }
                        if (!newdata) {
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            log.d("开始设置适配器");
                            pyqAdapter = new PyqAdapter(pyqs, context, arrayLists);
                            recyclerView_pyq.setAdapter(pyqAdapter);
                        } else {
                            log.d("开始通知更新数据"+arrayLists.size());
                            pyqAdapter.notifyDataSetChanged(pyqs, arrayLists);
                            atbottom = true;
                        }
                    }
                }

                @Override
                public void onError(int i, String s) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    Log.d("", "查询多图失败！！");
                }
            });
        }
    }


    public void getnewdata() {
        log.d("更新了数据");
        BmobQuery<Pyq> bmobQuery = new BmobQuery<Pyq>();
        bmobQuery.setLimit(8);
        Log.d("num", i + "#");
        bmobQuery.setSkip(i); // 忽略前10条数据（即第一页数据结果）
        bmobQuery.order("-updatedAt");
        bmobQuery.include("author");
        bmobQuery.findObjects(context, new FindListener<Pyq>() {
            @Override
            public void onSuccess(List<Pyq> list) {
                Log.d("长度", list.size() + "#");
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                if (list.size() == 0) {
                    com.xiaoyuan.tools.Toast.show("没有更多的内容啦~");
                    return;
                }
                pyqs = list;
                //查询每条动态的图片地址，保存到链表
                newdata = true;
                getpic(list);
                i = i + 8;
            }

            @Override
            public void onError(int i, String s) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);//关闭刷新动画
                }
                pyqs = null;
                Log.e("出错了_新闻图片", s);
                com.xiaoyuan.tools.Toast.show("加载失败");
            }
        });

    }


}
