package com.xiaoyuan.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.Goods;
import com.xiaoyuan.Class.Image_pyq;
import com.xiaoyuan.Class.Pyq;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.TimeTools;
import com.xiaoyuan.tools.log;
import com.yancy.imageselector.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Axu on 2016/8/4.
 */
public class PyqAdapter extends RecyclerView.Adapter<PyqAdapter.MainCardHolder> {

    public Itemclick mItemOnClickListener;
    public List<Pyq> pyq = null;
    public List<ArrayList<String>> marrayLists = new ArrayList<ArrayList<String>>();//动态对应的配图
    private Context context;
    public ArrayList<Integer> recy = new ArrayList<Integer>();//保存有配图的item

    public PyqAdapter(List<Pyq> pyq, Context context, List<ArrayList<String>> arrayLists) {
        this.context = context;
        this.marrayLists = arrayLists;
        this.pyq = pyq;
        recy.add(1000);
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Pyq> pyq, List<ArrayList<String>> arrayLists) {
        this.pyq.addAll(pyq);
        this.marrayLists = arrayLists;
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pyq, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (pyq == null) ? 0 : pyq.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Pyq getDate(int position) {
        return pyq.get(position);
    }

    public ArrayList<String> getDate_pic(int position) {
        return marrayLists.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_title;
        TextView tv_time;
        TextView tv_info;
        TextView tv_zf;
        TextView tv_pl;
        TextView tv_like;
        ImageView img_tx;
        RecyclerView recycler;
        ArrayList<String> path = new ArrayList<>();
        Image_9Adapter_pyq imagecrtnewsAdapter;
        GridLayoutManager gridLayoutManager;

        public MainCardHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_pyq_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_pyq_time);
            tv_info = (TextView) itemView.findViewById(R.id.tv_pyq_info);
            tv_zf = (TextView) itemView.findViewById(R.id.tv_pyq_zf);
            tv_pl = (TextView) itemView.findViewById(R.id.tv_pyq_pl);
            tv_like = (TextView) itemView.findViewById(R.id.tv_pyq_like);
            img_tx = (ImageView) itemView.findViewById(R.id.img_pyq_tx);
            recycler = (RecyclerView) itemView.findViewById(R.id.recycle_pyq);
        }

        public void setDate() {
            int position = getAdapterPosition();
            Pyq pyq = getDate(position);
            User user = pyq.getAuthor();
            tv_title.setText(user.getUsername());
            tv_time.setText(TimeTools.getday(pyq.getUpdatedAt()));
            tv_info.setText(pyq.getContent());
            tv_zf.setText(Integer.toString(pyq.getZf()));
            tv_pl.setText(Integer.toString(pyq.getPl()));
            tv_like.setText(Integer.toString(pyq.getSc()));

            String urlTx = (user.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/01/8712aa4540e3167580a9eb6bc1e7498b.jpg" : user.getAvatar().getUrl();
            Picasso.with(context).load(urlTx).fit()
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(img_tx);

            gridLayoutManager = new GridLayoutManager(context, 3);
            recycler.setLayoutManager(gridLayoutManager);

            if (recy.contains(position)) {
//                log.d("设置了图1:" + position);
                path = getDate_pic(position);
                if (path != null) {
                    imagecrtnewsAdapter = new Image_9Adapter_pyq(context, path);
                    recycler.setAdapter(imagecrtnewsAdapter);
                    recycler.setVisibility(View.VISIBLE);
                }
            } else {
                path = getDate_pic(position);
                if (path != null) {
//                    log.d("设置了图2:" + position);
                    recy.add(position);
                    imagecrtnewsAdapter = new Image_9Adapter_pyq(context, path);
                    recycler.setAdapter(imagecrtnewsAdapter);
                } else {
                    recycler.setVisibility(View.GONE);
                }
            }
        }


        @Override
        public void onClick(View v) {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.OnItemclick(v, getAdapterPosition());
            }
        }
    }
}

