package com.xiaoyuan.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.My_fall_user;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

/**
 * 搜索界面用户适配器
 * Created by Axu on 2016/8/10.
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MainCardHolder> {
    public List<User> users = null;
    public Itemclick mItemOnClickListener;
    public OnDyClickLister mOnItemclicklister;
    private Context context;
    private boolean animateItems = false;//添加数据之后不用显示动画
    public List<User> mlist = null;

    public PeopleAdapter(List<User> users, Context context, List<User> list) {
        this.context = context;
        this.users = users;
        this.mlist = list;
    }


    private void runEnterAnimation(View view, int position) {
        Log.d("position", position + "");
        if (animateItems || position > 8) {
            return;
        } else {
            view.setTranslationY(2000);
            view.animate()
                    .translationY(0)
                    .setStartDelay(170 * position)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(600)
                    .start();
        }
    }

    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }

    public void setOnDyClickLister(OnDyClickLister onDyClickLister) {
        mOnItemclicklister = onDyClickLister;
    }

    public void notifyDataSetChanged(List<User> users) {
        animateItems = true;
        this.users.addAll(users);
        super.notifyDataSetChanged();
    }

    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_people, parent, false));
    }

    public void onBindViewHolder(MainCardHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.setDate();
    }

    public int getItemCount() {
        return (users == null) ? 0 : users.size();
    }

    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public interface OnDyClickLister {
        void OnDyClick(View view, int position);
    }

    public User getDate(int position) {
        return users.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_info;
        ImageView iv_tx;
        TextView btn_dy;
        LinearLayout ll_people;


        public MainCardHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_people_name);
            tv_info = (TextView) itemView.findViewById(R.id.tv_people_info);
            iv_tx = (ImageView) itemView.findViewById(R.id.iv_people_tx);
            btn_dy = (TextView) itemView.findViewById(R.id.btn_people_dy);
            ll_people = (LinearLayout) itemView.findViewById(R.id.ll_people);

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void setDate() {
            User user = getDate(getAdapterPosition());
            tv_name.setText(user.getUsername());
            tv_info.setText(user.getNick());
            btn_dy.setOnClickListener(this);
            ll_people.setOnClickListener(this);
            String id = user.getObjectId();

            if (mlist != null) {
                for (User listuser : mlist) {
                    if (id.equals(listuser.getObjectId())) {
                        btn_dy.setText("已订阅");
                        btn_dy.setTextColor(Color.parseColor("#ffffff"));
                        btn_dy.setBackground(context.getResources().getDrawable(R.drawable.textview_myfollow_ydy));
                        break;
                    }
                }
            }
            String url = (user.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/01/8712aa4540e3167580a9eb6bc1e7498b.jpg" : user.getAvatar().getUrl();
            Picasso.with(context).load(url).fit()
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(iv_tx);

        }

        public void onClick(View v) {
            if (v == btn_dy) {
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.OnItemclick(v, getAdapterPosition());
                }
            } else if (v == ll_people) {
                mOnItemclicklister.OnDyClick(v, getAdapterPosition());
            }
        }
    }
}
