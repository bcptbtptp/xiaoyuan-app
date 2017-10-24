package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 星球类
 * Created by longer on 2016/8/4.
 */
public class Star extends BmobObject {
    private String name;
    private String infor;
    private BmobFile avatar;//星球的头像
    private User father;
    private String galaxy;//属于那个星系
    private BmobFile show;//星球的展示页

    //星球是否开启对应列表,  OFF为不开启，其他则为开始，名字就是列表名  ，注意OFF必须为大写
    private String news;//新闻列表
    private String photo;//图片列表
    private String goods;//商品列表
    private String comment;//评论列表

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BmobFile getShow() {
        return show;
    }

    public void setShow(BmobFile show) {
        this.show = show;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public User getFather() {
        return father;
    }

    public void setFather(User father) {
        this.father = father;
    }

    public String getGalaxy() {
        return galaxy;
    }

    public void setGalaxy(String galaxy) {
        this.galaxy = galaxy;
    }
}
