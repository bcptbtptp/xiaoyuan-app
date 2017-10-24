package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;

/**
 * Created by longer on 2016/7/30.
 */
public class Person extends BmobObject {

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {     return username;    }
    public String getPassword() {
        return password;
    }
    public Person() {
    }
    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
