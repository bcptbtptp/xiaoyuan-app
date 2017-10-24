package com.xiaoyuan.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by longer on 2016/7/29.
 */
public class WaitDialog extends ProgressDialog {

    public WaitDialog(Context context,String str) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage(str);
    }
}
