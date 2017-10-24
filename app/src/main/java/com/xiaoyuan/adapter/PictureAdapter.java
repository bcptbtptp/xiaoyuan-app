package com.xiaoyuan.adapter;

import android.content.Context;
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
import com.xiaoyuan.Class.Photo;
import com.xiaoyuan.Class.Star;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Axu on 2016/8/9.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MainCardHolder> {
    public Itemclick mItemOnClickListener;
    public List<Photo> photos = null;

    private Context context;
    private boolean animateItems = false;//添加数据之后不用显示动画

    public PictureAdapter(List<Photo> photos, Context context) {
        this.context = context;
        this.photos = photos;
    }

    private void runEnterAnimation(View view, int position) {
        log.d(position + "");
        if (animateItems || position > 5) {
            return;
        } else {
            view.setTranslationY(120);
            view.animate()
                    .translationY(0)
                    .setStartDelay(200 * position)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(400)
                    .start();
        }
    }

    public void claerall() {
        this.photos.clear();
    }

    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Photo> photos) {
        animateItems = true;
        this.photos.addAll(photos);
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_picture, parent, false));
    }


    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (photos == null) ? 0 : photos.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Photo getDate(int position) {
        return photos.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_sc;
        TextView tv_name;
        ImageView iv;

        public MainCardHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_pic_name);
            tv_sc = (TextView) itemView.findViewById(R.id.tv_pic_sc);
            iv = (ImageView) itemView.findViewById(R.id.img_pic_pic);
        }

        public void setDate() {
            iv.setOnClickListener(this);
            Photo photo = getDate(getAdapterPosition());

            tv_name.setText(photo.getTitle());
            if (photo.getLike() != 0 && photo.getLike() != null) {
                tv_sc.setText(photo.getLike() + "");
            } else {
                tv_sc.setText("0");
            }


            String url = (photo.getPicture() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/01/8712aa4540e3167580a9eb6bc1e7498b.jpg" : photo.getPicture().getUrl();
            Picasso.with(context).load(url)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(iv);
        }

        @Override
        public void onClick(View v) {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.OnItemclick(v, getAdapterPosition());
                Log.d("Click", "###");
            }
        }

    }

}
