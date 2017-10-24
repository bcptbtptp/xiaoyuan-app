package com.xiaoyuan.tools;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 改变部分字段的颜色
 * Created by longer on 2016/7/20.
 */
public class ShuoMClickableSpan extends ClickableSpan {

    public String string;
    public Context context;
    public ShuoMClickableSpan(String str,Context context){
        super();
        this.string = str;
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#696969"));
    }
    @Override
    public void onClick(View widget) {

    }
}

