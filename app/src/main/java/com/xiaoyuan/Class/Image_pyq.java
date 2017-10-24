package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**对应用户的关注用户
 * Created by longer on 2016/8/11.
 */
public class Image_pyq extends BmobObject {

    Pyq dt;//朋友圈对应的动态
    BmobFile picture;//图

    public Pyq getDt() {
        return dt;
    }

    public void setDt(Pyq dt) {
        this.dt = dt;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }
}
