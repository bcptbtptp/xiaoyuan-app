package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;

/**对应用户的关注用户
 * Created by longer on 2016/8/11.
 */
public class My_fall_user extends BmobObject {

    User user;//用户
    User my_user;//我的关注的人

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getMy_user() {
        return my_user;
    }

    public void setMy_user(User my_user) {
        this.my_user = my_user;
    }
}
