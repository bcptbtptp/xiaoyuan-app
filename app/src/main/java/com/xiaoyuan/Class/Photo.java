package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**相册列表
 * Created by longer on 2016/8/9.
 */
public class Photo extends BmobObject {
    private String title;//标题
    private String infor;//介绍
    private Integer like;//点赞
    private BmobFile picture;//图片
    private User author;//作者
    private Star star;//属于那个星球的
    private String type;//类型

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
