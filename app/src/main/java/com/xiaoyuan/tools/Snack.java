package com.xiaoyuan.tools;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by longer on 2016/8/5.
 */
public class Snack {
    public static void show(View view, String text)
    {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }
}
