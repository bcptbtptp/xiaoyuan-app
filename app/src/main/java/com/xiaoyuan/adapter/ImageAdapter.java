package com.xiaoyuan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.longer.PinchImageView;
import com.xiaoyuan.longer.R;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Axu on 2016/8/14.
 */
public class ImageAdapter extends PagerAdapter {
    public Context context;
    public ArrayList<String> url = new ArrayList<String>();
    final LinkedList<PinchImageView> viewCache = new LinkedList<PinchImageView>();

    public ImageAdapter(Context context, ArrayList<String> url) {
        this.context = context;
        this.url = url;

    }

    public int getCount() {
        return url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PinchImageView piv;
        if (viewCache.size() > 0) {
            piv = viewCache.remove();
            piv.reset();
        } else {
            piv = new PinchImageView(context);
        }
        String url1 = url.get(position);
        Picasso.with(context).load(url1)
                .placeholder(R.drawable.photo)
                .error(R.drawable.photo)
                .into(piv);
        container.addView(piv);
        return piv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        PinchImageView piv = (PinchImageView) object;
        container.removeView(piv);
        viewCache.add(piv);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        PinchImageView piv = (PinchImageView) object;

        String url1 = url.get(position);
        Picasso.with(context).load(url1)
                .placeholder(R.drawable.photo)
                .error(R.drawable.photo)
                .into(piv);
    }

}
