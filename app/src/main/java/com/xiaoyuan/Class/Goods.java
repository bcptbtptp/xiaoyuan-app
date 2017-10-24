package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**商品列表类
 * Created by longer on 2016/8/4.
 */
public class Goods extends BmobObject{

    private String name;//商品名称
    private String infor;//商品介绍
    private String price;//商品价格
    private BmobFile picture;//商品图片
    private String type;//类型
    private User author;//作者
    private Star star;//属于那个星球的

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
