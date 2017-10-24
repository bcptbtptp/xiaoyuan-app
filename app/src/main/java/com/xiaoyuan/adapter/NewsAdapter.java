package com.xiaoyuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.news;
import com.xiaoyuan.longer.R;
import com.yancy.imageselector.utils.Utils;

import java.util.List;

/**
 * 主界面的list适配器
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MainCardHolder> {

    public Itemclick mItemOnClickListener;
    public List<news> newses = null;
    private Context context;
    private boolean animateItems = false;//添加数据之后不用显示动画

    public NewsAdapter(List<news> newses, Context context) {
        this.context = context;
        this.newses = newses;
    }

    private void runEnterAnimation(View view, int position) {
        Log.d("position", position + "#");
        if (position > 3 || animateItems) {
            return;
        } else {
            view.setTranslationY(1000);
            view.animate()
                    .translationY(0)
                    .setStartDelay(150 * position)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(500)
                    .start();
        }
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<news> newses) {
        animateItems = true;
        this.newses.addAll(newses);
        super.notifyDataSetChanged();
    }

    public void claerall(){
        this.newses.clear();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (newses == null) ? 0 : newses.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public news getDate(int position) {
        return newses.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        TextView ct;
        ImageView iv;
        TextView time;

        public MainCardHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_news_title);
            ct = (TextView) itemView.findViewById(R.id.tv_news_content);
            time = (TextView) itemView.findViewById(R.id.tv_news_time);
            iv = (ImageView) itemView.findViewById(R.id.iv_news_pic);
            tv.setOnClickListener(this);
        }

        public void setDate() {

            news mnews = getDate(getAdapterPosition());
            tv.setText(mnews.getTitle());
            ct.setText(mnews.getContent());
            time.setText(mnews.getCreatedAt());

            String url = (mnews.getPicture() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/01/8712aa4540e3167580a9eb6bc1e7498b.jpg" : mnews.getPicture().getUrl();
            Picasso.with(context).load(url).fit()
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(iv);

        }

        @Override
        public void onClick(View v) {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.OnItemclick(v, getAdapterPosition());
            }
        }
    }
}
