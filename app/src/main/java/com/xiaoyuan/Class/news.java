package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**新闻列表类
 * Created by longer on 2016/7/31.
 */
public class news extends BmobObject {
    private String title;
    private String content;
    private BmobFile picture;
    private Star star;//属于那个星球的
    private User author;//作者
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }
}
