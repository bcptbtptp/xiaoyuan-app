package com.xiaoyuan.longer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xiaoyuan.Class.Star;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Axu on 2016/8/17.
 */
public class Add_contentActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.tv_addcontent_news)
    TextView tvAddcontentNews;
    @Bind(R.id.tv_addcontent_newsshow)
    TextView tvAddcontentNewsshow;
    @Bind(R.id.tv_addcontent_goods)
    TextView tvAddcontentGoods;
    @Bind(R.id.tv_addcontent_goodsshow)
    TextView tvAddcontentGoodsshow;
    @Bind(R.id.tv_addcontent_photos)
    TextView tvAddcontentPhotos;
    @Bind(R.id.tv_addcontent_photosshow)
    TextView tvAddcontentPhotosshow;
    @Bind(R.id.tv_addcontent_comments)
    TextView tvAddcontentComments;
    @Bind(R.id.tv_addcontent_commentsshow)
    TextView tvAddcontentCommentsshow;
    public Star star;

    public Context context;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        ButterKnife.bind(this);
        context=Add_contentActivity.this;
        toolbar.setTitle("添加内容");
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }


        getIntentDate();
    }


    @OnClick({R.id.tv_addcontent_news, R.id.tv_addcontent_goods, R.id.tv_addcontent_photos, R.id.tv_addcontent_comments})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addcontent_news:
                Intent intent_news = new Intent(context, Create_newsActivity.class);
                intent_news.putExtra("star", star);
                startActivity(intent_news);
                break;
            case R.id.tv_addcontent_goods:
                Intent intent_goods = new Intent(context, Create_goodsActivity.class);
                intent_goods.putExtra("star", star);
                startActivity(intent_goods);

                break;
            case R.id.tv_addcontent_photos:
                Intent intent_photos = new Intent(context, Create_photoActivity.class);
                intent_photos.putExtra("star", star);
                startActivity(intent_photos);

                break;
            case R.id.tv_addcontent_comments:
                Intent intent_comments = new Intent(context, Create_newsActivity.class);
                intent_comments.putExtra("star", star);
                startActivity(intent_comments);

                break;
        }
    }

    public void getIntentDate() {
        Intent intent = getIntent();
        star = (Star) intent.getSerializableExtra("star");
        if (star != null) {//如果是通过传值过来的
            String list_news = star.getNews();
            String list_goods = star.getGoods();
            String list_photo = star.getPhoto();
            String list_comment = star.getComment();

            if (!"OFF".equals(list_news)) {
                tvAddcontentNewsshow.setText(list_news);
            } else {

                tvAddcontentNews.setVisibility(View.GONE);
            }
            if (!"OFF".equals(list_goods)) {
                tvAddcontentGoodsshow.setText(list_goods);
            } else {

                tvAddcontentGoods.setVisibility(View.GONE);
            }
            if (!"OFF".equals(list_photo)) {
                tvAddcontentPhotosshow.setText(list_photo);
            } else {

                tvAddcontentPhotos.setVisibility(View.GONE);
            }
            if (!"OFF".equals(list_comment)) {
                tvAddcontentCommentsshow.setText(list_comment);
            } else {

                tvAddcontentComments.setVisibility(View.GONE);
            }
        }
    }


}
