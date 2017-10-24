package com.xiaoyuan;

import com.xiaoyuan.bmob.ContantStr;
import com.xiaoyuan.longer.MainActivity;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by longer on 2016/7/28.
 */
public class Application extends android.app.Application {

    private static Application INSTANCE;
    public static Application getINSTANCE(){
        return INSTANCE;
    }
    private void setInstance(Application app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(Application a) {
        Application.INSTANCE = a;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        // 初始化NoHttp
//        NoHttp.init(INSTANCE);
        // 打开NoHttp的调试模式
//        Logger.setDebug(true);
        // 设置NoHttp的日志的tag
//        Logger.setTag("NoHttpTest");

        com.orhanobut.logger.Logger.init("smile");
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(INSTANCE)
                //设置appkey
                .setApplicationId(ContantStr.ApplicationKey)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(15)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(5000)
                .build();
        Bmob.initialize(config);



    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
