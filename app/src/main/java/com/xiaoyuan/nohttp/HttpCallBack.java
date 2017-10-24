package com.xiaoyuan.nohttp;

import com.yolanda.nohttp.rest.Response;

/**
 * Created in Mar 6, 2016 9:05:28 PM.
 *
 * @author YOLANDA;
 */
public interface HttpCallBack<T> {

    /**
     * Server correct response to callback when an HTTP request.
     *
     * @param what the credit of the incoming request is used to distinguish between multiple requests.
     * @param response in response to the results.
     */
    void onSucceed(int what, Response<T> response);

    /**
     * When there was an error correction.
     *
     * @param what the credit of the incoming request is used to distinguish between multiple requests.
     * @param url url.
     * @param tag tag of request callback.
     * @param exception error message for request.
     * @param responseCode server response code.
     * @param networkMillis request process consumption time.
     */
    void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis);

}
