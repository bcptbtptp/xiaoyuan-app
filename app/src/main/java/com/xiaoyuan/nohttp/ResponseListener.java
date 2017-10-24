package com.xiaoyuan.nohttp;

import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.WaitDialog;

import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;


import android.content.Context;
import android.content.DialogInterface;

/**
 * Created in longer in 2016-7-29
 *
 * @author Longer
 */
public class ResponseListener<T> implements OnResponseListener<T> {

    private Request<T> mRequest;

    private WaitDialog mDialog;

    private HttpCallBack<T> callBack;

    private boolean isShowError;

    public ResponseListener(Request<T> request, Context context, HttpCallBack<T> callBack, boolean isShowDialog, boolean isCanCancel, boolean isShowError) {
        this.mRequest = request;
        this.callBack = callBack;
        this.isShowError = isShowError;
        if (context != null && isShowDialog) {
            mDialog = new WaitDialog(context,"正在连接中....");
            mDialog.setCancelable(isCanCancel);
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel(true);
                }
            });
        }
    }

    @Override
    public void onStart(int what) {
        if (mDialog != null && !mDialog.isShowing())
            mDialog.show();
    }

    @Override
    public void onSucceed(int what, Response<T> response) {
        if (callBack != null)
            callBack.onSucceed(what, response);
    }

    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        if (isShowError) {
            if (exception instanceof ServerError) {// 服务器错误
                Toast.show("服务器发生错误");
            } else if (exception instanceof NetworkError) {// 网络不好
                Toast.show("请检查网络");
            } else if (exception instanceof TimeoutError) {// 请求超时
                Toast.show("请求超时，网络不好或者服务器不稳定");
            } else if (exception instanceof UnKnownHostError) {// 找不到服务器
                Toast.show("未发现指定服务器");
            } else if (exception instanceof URLError) {// URL是错的
                Toast.show("URL错误");
            } else if (exception instanceof NotFoundCacheError) {
                Toast.show("没有发现缓存");
            } else {
                Toast.show("未知错误");
            }
        }
        if (callBack != null)
            callBack.onFailed(what, url, tag, exception, responseCode, networkMillis);
    }

    @Override
    public void onFinish(int what) {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

}

