package com.xiaoyuan.longer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.Photo;
import com.xiaoyuan.adapter.ImageAdapter;
import com.yancy.imageselector.ImageLoader;
import com.yancy.imageselector.bean.Image;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by Axu on 2016/8/13.
 */
public class Image_Activity extends Activity {
    public Photo photo;
    public Context context;
    public int position;
    public ImageAdapter imageAdapter;

    public ArrayList<String> url = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        context = Image_Activity.this;
        Intent intent = getIntent();
        url = (ArrayList<String>) intent.getSerializableExtra("url");
//        photo = (Photo) intent.getSerializableExtra("photo");
        position = (int) intent.getSerializableExtra("position");
        final PinchImageViewPager pager = (PinchImageViewPager) findViewById(R.id.pic);
        imageAdapter=new ImageAdapter(context,url);
        pager.setAdapter(imageAdapter);
        pager.setCurrentItem(position);
    }


}

