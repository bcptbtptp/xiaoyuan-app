package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * 星球推送的消息
 * Created by longer on 2016/8/4.
 */
public class Ts extends BmobObject {
    private String content;//推送的内容
    private Star star;//属于那个星球推送的
    private BmobFile picture;

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }
}
