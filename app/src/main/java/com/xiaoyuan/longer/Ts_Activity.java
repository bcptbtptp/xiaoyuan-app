package com.xiaoyuan.longer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoyuan.adapter.Image_crtnewsAdapter;
import com.xiaoyuan.fragment.BlankFragment;
import com.xiaoyuan.fragment.Fragment_select_user;
import com.xiaoyuan.fragment.Fragment_select_star;
import com.xiaoyuan.tools.log;

import java.util.ArrayList;
import java.util.List;

public class Ts_Activity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private Context context;
    private ViewPager mViewPager;
    public List<Fragment> fragments = new ArrayList<>();
    private TextView tv_ss;
    private EditText ed_username;
    private OnButtonClickedListener0 buttonClickedListener0;
    private OnButtonClickedListener1 buttonClickedListener1;
    private OnButtonClickedListener2 buttonClickedListener2;


    private Image_crtnewsAdapter adapter;

    private ArrayList<String> path = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ts);

        context = Ts_Activity.this;
        Fragment fra1 = new Fragment_select_star();
        Fragment fra2 = new Fragment_select_user();
        Fragment fra3 = new BlankFragment();
        fragments.add(fra1);
        fragments.add(fra2);
        fragments.add(fra3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Message_Activity.class);
                startActivity(intent);
            }
        });
        init();
    }

    /**
     * tablayou滑动时事件
     */
    public TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if(tab.getPosition() == 0){
                tv_ss.setOnClickListener(onclick0);
            }else if(tab.getPosition() == 1){
                tv_ss.setOnClickListener(onclick1);
            }else if(tab.getPosition() == 2){
                tv_ss.setOnClickListener(onclick2);
            }
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    public void init() {
        tv_ss = (TextView) findViewById(R.id.tv_ts_select);
        ed_username = (EditText) findViewById(R.id.ed_tx_username);
        //默认为第一个界面
        tv_ss.setOnClickListener(onclick0);
    }


    View.OnClickListener onclick0 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonClickedListener0.onclicked(ed_username.getText().toString().trim());
        }
    };
    View.OnClickListener onclick1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonClickedListener1.onclicked(ed_username.getText().toString().trim());
        }
    };
    View.OnClickListener onclick2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            buttonClickedListener2.onclicked(ed_username.getText().toString().trim());
        }
    };


    public void setButtonClickedListener0(OnButtonClickedListener0 buttonClickedListener) {
        this.buttonClickedListener0 = buttonClickedListener;
    }
    public void setButtonClickedListener1(OnButtonClickedListener1 buttonClickedListener) {
        this.buttonClickedListener1 = buttonClickedListener;
    }
    public void setButtonClickedListener2(OnButtonClickedListener2 buttonClickedListener) {
        this.buttonClickedListener2 = buttonClickedListener;
    }


    public interface OnButtonClickedListener0 {
        void onclicked(String s);
    }
    public interface OnButtonClickedListener1 {
        void onclicked(String s);
    }
    public interface OnButtonClickedListener2 {
        void onclicked(String s);
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mfragments;

        public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mfragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mfragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "星球";
                case 1:
                    return "用户";
                case 2:
                    return "内容";
            }
            return null;
        }
    }
}
