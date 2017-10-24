package com.xiaoyuan.longer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.fragment.Fragment_News;
import com.xiaoyuan.fragment.Fragment_Userother_infor;
import com.xiaoyuan.fragment.Fragment_Userother_star;

import java.util.ArrayList;
import java.util.List;

public class User_other_Activity extends AppCompatActivity {

    private ViewPager viewpage;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private Context context;
    public ImageView iv_other_avatar;
    public TextView tv_other_name;
    public TextView tv_other_nick;
    public static User user_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_other);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iv_other_avatar = (ImageView) findViewById(R.id.iv_userother_avatar);
        tv_other_name = (TextView) findViewById(R.id.tv_userother_name);
        tv_other_nick = (TextView) findViewById(R.id.tv_userother_nick);

        Intent intent = getIntent();
        user_other = (User) intent.getSerializableExtra("user");

        context = User_other_Activity.this;
        Fragment fra = new Fragment_News();
        Fragment fra1 = new Fragment_Userother_star();
        Fragment fra2 = new Fragment_Userother_infor();

        fragments.add(fra);
        fragments.add(fra1);
        fragments.add(fra2);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        viewpage = (ViewPager) findViewById(R.id.container);
        viewpage.setAdapter(mSectionsPagerAdapter);

        TabLayout tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(viewpage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Message_Activity.class);
                startActivity(intent);
            }
        });
        setdatda();
    }
/*
设置上面的头像，名字及个性签名
 */

    public void setdatda() {
        tv_other_name.setText(user_other.getUsername());
        tv_other_nick.setText(user_other.getNick());

        String url = (user_other.getAvatar() == null) ? "http://bmob-cdn-5205.b0.upaiyun.com/2016/08/08/71935ed3405c04df8052fb759c4206d2.jpg" : user_other.getAvatar().getUrl();
        Picasso.with(context).load(url).fit()
                .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                .error(com.yancy.imageselector.R.mipmap.imageselector_photo)
                .into(iv_other_avatar);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public List<Fragment> mfragments;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mfragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mfragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ta的动态";
                case 1:
                    return "Ta的星球";
                case 2:
                    return "Ta的资料";
            }
            return null;
        }
    }
}
