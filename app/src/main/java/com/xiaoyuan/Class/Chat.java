package com.xiaoyuan.Class;

import cn.bmob.v3.BmobObject;

/**
 * Created by Axu on 2016/8/5.
 */
public class Chat extends BmobObject {
    public static final int TYPE_RECEVED=0;
    public static final int TYPE_SEND=1;
    private String content;
    private int type;
    public Chat(String content,int type){
        this.content=content;
        this.type=type;
    }
    public String getContent(){
        return  content;
    }
    public void setContent(String content){
        this.content=content;
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type=type;
    }
}
