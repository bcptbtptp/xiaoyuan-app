package com.xiaoyuan.nohttp;

import android.content.Context;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

/**
 * Created by longer on 2016/7/29.
 */

public class CallServer {

    private static CallServer instance;

    private RequestQueue queue;

    public synchronized static CallServer getInstance() {
        if (instance == null) {
            instance = new CallServer();
        }
        return instance;
    }

    private CallServer() {
        queue = NoHttp.newRequestQueue();
    }

    /**
     * 添加一个请求到请求队列
     *
     * @param context 上下文
     * @param request 请求对象
     * @param callBack 接受回调结果
     * @param what what，当多个请求用同一个responseListener接受结果时，用来区分请求
     * @param isShowDialog 是否显示dialog
     * @param isCanCancel 请求是否能被用户取消
     * @param isShowError 是否提示用户错误信息
     */
    public <T> void add(Context context, Request<T> request, HttpCallBack<T> callBack, int what, boolean isShowDialog, boolean isCanCancel, boolean isShowError) {
        queue.add(what, request, new ResponseListener<T>(request, context, callBack, isShowDialog, isCanCancel, isShowError));
    }

}