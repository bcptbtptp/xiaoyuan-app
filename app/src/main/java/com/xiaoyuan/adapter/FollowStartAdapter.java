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
import com.xiaoyuan.longer.R;

import java.util.List;

/**
 * Created by Axu on 2016/8/menu_mystar.xml.
 */
public class FollowStartAdapter extends RecyclerView.Adapter<FollowStartAdapter.MainCardHolder>{

    public Itemclick mItemOnClickListener;
    public List<Star> stars = null;

    private Context context;
    private boolean animateItems = false;//添加数据之后不用显示动画

    public FollowStartAdapter(List<Star> stars, Context context) {
        this.context = context;
        this.stars = stars;
    }

    private void runEnterAnimation(View view, int position) {
        Log.d("position", position + "#");
        if (animateItems || position > 5) {
            return;
        } else {
            view.setTranslationY(150);
            view.animate()
                    .translationY(0)
                    .setStartDelay(200 * position)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(500)
                    .start();
        }
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Star> stars) {
        animateItems = true;
        this.stars.addAll(stars);
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_star, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (stars == null) ? 0 : stars.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Star getDate(int position) {
        return stars.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_infor;
        ImageView iv;
        CardView card_star;

        public MainCardHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_star_name);
            tv_infor = (TextView) itemView.findViewById(R.id.tv_star_infor);
            iv = (ImageView) itemView.findViewById(R.id.iv_star_avatar);
            card_star = (CardView) itemView.findViewById(R.id.card_selestar);
        }

        public void setDate() {
            Star star = getDate(getAdapterPosition());
            tv_name.setText(star.getName());
            tv_infor.setText(star.getInfor());
            card_star.setOnClickListener(this);

            String url = (star.getShow() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/01/8712aa4540e3167580a9eb6bc1e7498b.jpg" : star.getAvatar().getUrl();
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
