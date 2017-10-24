package com.xiaoyuan.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.Class.Ts;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.others.ImageFactor;
import com.xiaoyuan.tools.log;

import java.util.List;

/**
 * Created by Axu on 2016/8/22.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainCardHolder> {
    public Itemclick mItemOnClickListener;
    public List<Ts> tss = null;
    private Context context;

    public MainAdapter(List<Ts> tss, Context context) {
        this.context = context;
        this.tss = tss;
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Ts> tss) {
        this.tss = tss;
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (tss == null) ? 0 : tss.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Ts getDate(int position) {
        return tss.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_content;
        ImageView iv_avatar;
        ImageView iv_pic;
        CardView cardView;

        public MainCardHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_main_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_main_content);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_main_avatar);
            iv_pic = (ImageView) itemView.findViewById(R.id.iv_main_pic);
            cardView = (CardView) itemView.findViewById(R.id.card_main);
        }

        public void setDate() {
            Ts ts = getDate(getAdapterPosition());
            Star star = ts.getStar();
            tv_name.setText(star.getName());
            tv_content.setText(ts.getContent());
            iv_pic.setVisibility(View.GONE);
            cardView.setOnClickListener(this);


            String url = (star.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/01/8712aa4540e3167580a9eb6bc1e7498b.jpg" : star.getAvatar().getUrl();
            Picasso.with(context).load(url).fit()
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .transform(new ImageFactor())
                    .into(iv_avatar);


            if (ts.getPicture() != null) {
                iv_pic.setVisibility(View.VISIBLE);
                Picasso.with(context).load(ts.getPicture().getUrl())
                        .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                        .error(com.yancy.imageselector.R.mipmap.imageselector_photo)
                        .into(iv_pic);
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
