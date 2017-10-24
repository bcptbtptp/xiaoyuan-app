package com.xiaoyuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.Goods;
import com.xiaoyuan.longer.R;
import com.yancy.imageselector.utils.Utils;

import java.util.List;

/**
 * Created by Axu on 2016/8/4.
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.MainCardHolder> {

    public Itemclick mItemOnClickListener;
    public List<Goods> goods = null;
    private Context context;
    private boolean animateItems = false;//添加数据之后不用显示动画

    public GoodsAdapter(List<Goods> goods, Context context) {
        this.context = context;
        this.goods = goods;
    }
    private void runEnterAnimation(View view, int position) {
        if (position > 3 || animateItems) {
            return;
        } else {
            view.setTranslationY(2000);
            view.animate()
                    .translationY(0)
                    .setStartDelay(180 * position)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(600)
                    .start();
        }
    }

    public void claerall(){
        this.goods.clear();
    }
    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Goods> goods) {
        animateItems = true;
        this.goods.addAll(goods);
        super.notifyDataSetChanged();
    }
    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (goods == null) ? 0 : goods.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Goods getDate(int position) {
        return goods.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;
        TextView ct;
        ImageView iv;
        TextView price;

        public MainCardHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_goods_title);
            ct = (TextView) itemView.findViewById(R.id.tv_goods_content);
            price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            iv=(ImageView)itemView.findViewById(R.id.iv_goods_pic) ;
            tv.setOnClickListener(this);
        }

        public void setDate() {

            Goods mnews = getDate(getAdapterPosition());
            tv.setText(mnews.getName());
            ct.setText(mnews.getInfor());
            price.setText(mnews.getPrice());

            String url = (mnews.getPicture() == null ) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/01/8712aa4540e3167580a9eb6bc1e7498b.jpg" : mnews.getPicture().getUrl();
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

