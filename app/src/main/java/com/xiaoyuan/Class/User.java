package com.xiaoyuan.Class;

import android.content.Context;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by longer on 2016/7/30.
 */
public class User extends BmobUser {

    private String sex;//性别
    private Integer age;//年龄
    private BmobFile avatar;//头像
    private String school;//学校
    private String home;//故乡
    private String nick;//个性签名
    private Boolean vip;//是否是vip
    public Boolean getVip(){
        return vip;
    }
    public void setVip(Boolean vip){
        this.vip=vip;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }
}
