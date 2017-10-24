package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;

/**对应用户的关注星球
 * Created by longer on 2016/8/11.
 */
public class My_fall_star extends BmobObject {

    User user;//用户
    Star star;//我的关注的星球

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Star getStar() {
        return star;
    }

    public void setStar(Star star) {
        this.star = star;
    }
}
