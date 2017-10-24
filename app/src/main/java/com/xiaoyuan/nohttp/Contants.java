package com.xiaoyuan.nohttp;

/**
 * Created by longer on 2016/7/28.
 */
public class Contants {
    public static final String SERVER = "http://www.lovelysquirrel.cn/XiaoYuanSystem/login.jsp";
    public static final String SERVER2 = "http://www.lovelysquirrel.cn/XiaoYuanSystem/login/userlogin";

    public static final String YOUDAO = "http://fanyi.youdao.com/";



    //NOhttp的封装使用
//    public void init() {
//        Request<String> response = NoHttp.createStringRequest(Contants.SERVER2, RequestMethod.POST);
//        response.add("username",username.getText().toString());
//        response.add("password",password.getText().toString());
//        CallServer.getInstance().add(context, response, callback, LOGIN, true, true, false);
//    }
//
//    public HttpCallBack<String> callback = new HttpCallBack<String>() {
//        @Override
//        public void onSucceed(int what, Response response) {
//            Headers header = response.getHeaders();
//            code = header.getResponseCode();
//            Log.d("iiii",code+"#");
//            if (code == 200){
//                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//        @Override
//        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
//            code = responseCode;
//            Log.d("code",code+"#");
//            if (code == 302) {
//                getActivity().finish();
//                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
}
