package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by longer on 2016/8/4.
 */
public class Appinfor extends BmobObject {
    private String version;
    private String infor;
    private Boolean important;
    private BmobFile app;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }

    public BmobFile getApp() {
        return app;
    }

    public void setApp(BmobFile app) {
        this.app = app;
    }
}
