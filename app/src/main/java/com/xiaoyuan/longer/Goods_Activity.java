package com.xiaoyuan.longer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoyuan.fragment.Fragment_Goods;
import com.xiaoyuan.fragment.Fragment_message_main;
import com.xiaoyuan.fragment.Fragment_message_my;

import java.util.ArrayList;
import java.util.List;

public class Goods_Activity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private PopupWindow mPopWindows2;// 课程详细
    private Context context;
    private static boolean bianji = false;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

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

        Fragment fragment = new Fragment_Goods();
        Fragment fragment2 = new Fragment_message_main();
        Fragment fragment3 = new Fragment_message_my();

        fragments.add(fragment);
        fragments.add(fragment2);
        fragments.add(fragment3);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        context = Goods_Activity.this;

    }



//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_goods, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_edit) {
//            if(bianji) {
//
//            }else{
//                bianji = true;
//                Toast.makeText(Goods_Activity.this,"选择一条商品信息",Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        } else if (id == R.id.action_create) {
//            Intent intent = new Intent(this, Create_tableActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }



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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "列表1";
                case 1:
                    return "列表2";
                case 2:
                    return "列表3";
            }
            return null;
        }
    }

    public void show_pop() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_goods, null);
        mPopWindows2 = new PopupWindow(contentView);
        mPopWindows2.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindows2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindows2.setFocusable(true);
        mPopWindows2.setBackgroundDrawable(new BitmapDrawable());

        TextView tv_name = (TextView) contentView.findViewById(R.id.popup_name);
        TextView tv_locationg = (TextView) contentView.findViewById(R.id.popup_location);
        TextView tv_teacher = (TextView) contentView.findViewById(R.id.popup_teacher);
        TextView tv_day = (TextView) contentView.findViewById(R.id.popup_day);
        Button but_delete = (Button) contentView.findViewById(R.id.course_delete);

        tv_name.setText("11");
        tv_locationg.setText("11");
        tv_teacher.setText("1122");
        tv_day.setText("1123");
//        but_delete.setOnClickListener(this);

        mPopWindows2.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }

    public void onclick_scroll(View view){
        onclick_edit();
    }

    //点击条目
    public  void onclick_edit(){
        if(bianji) {
            Toast.makeText(Goods_Activity.this,"选择一条商品信息",Toast.LENGTH_SHORT).show();
            show_pop();
        }else{
            Toast.makeText(Goods_Activity.this,"您不能编辑本界面",Toast.LENGTH_SHORT).show();
        }
    }
}
