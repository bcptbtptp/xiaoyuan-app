package com.xiaoyuan.longer;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xiaoyuan.fragment.Fragment_Myfollow_user;
import com.xiaoyuan.fragment.Fragment_Myfollow_star;

import java.util.ArrayList;
import java.util.List;

public class Myfollow_Activity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    public ViewPager mViewPager;
    public List<Fragment> fragments = new ArrayList<>();
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfollow);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        context = Myfollow_Activity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        Fragment fragment = new Fragment_Myfollow_star();
        Fragment fragment2 = new Fragment_Myfollow_user();

        fragments.add(fragment);
        fragments.add(fragment2);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Message_Activity.class);
                startActivity(intent);
            }
        });

    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mfragments;
        public SectionsPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            mfragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mfragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "星球";
                case 1:
                    return "好友";
            }
            return null;
        }
    }
}
