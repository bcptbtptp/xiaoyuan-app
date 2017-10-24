package com.xiaoyuan.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoyuan.Class.Appinfor;
import com.xiaoyuan.Class.User;
import com.xiaoyuan.Config;
import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.Toast;
import com.xiaoyuan.tools.log;
import com.yolanda.nohttp.Logger;

import java.io.File;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class Fragment_Gallery extends Fragment {

    private View view;
    private TextView version;
    private TextView impor;
    private TextView infor;
    private TextView jd;
    private Button btn_install;
    private Button btn_check;
    public String msavePath;
    public Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, null);

        context = getActivity();
        version = (TextView) view.findViewById(R.id.tv_gall_version);
        impor = (TextView) view.findViewById(R.id.tv_gall_impor);
        infor = (TextView) view.findViewById(R.id.tv_gall_infor);
        jd = (TextView) view.findViewById(R.id.tv_gall_jd);
        btn_install = (Button) view.findViewById(R.id.btn_gall_install);
        btn_check = (Button) view.findViewById(R.id.btn_gall_check);
        btn_install.setOnClickListener(listen);
        btn_check.setOnClickListener(onclick_check);
        String version_now = Config.version;
        version.setText("当前版本：" + version_now);

        return view;
    }

    Button.OnClickListener listen = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            openapk();

        }
    };
    Button.OnClickListener onclick_check = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            init();

        }
    };

    private void init() {
        BmobQuery<Appinfor> bmobQuery = new BmobQuery<Appinfor>();
        bmobQuery.addWhereEqualTo("version", "1.1");

        bmobQuery.findObjects(context, new FindListener<Appinfor>() {
            @Override
            public void onSuccess(List<Appinfor> list) {
                String url = null;
                for (Appinfor appinfor : list) {
                    //显示新版本信息到界面
                    version.setText("新版本：" + appinfor.getVersion());
                    impor.setText("是否必须更新：" + appinfor.getImportant().toString());
                    infor.setText("更新内容：" + appinfor.getInfor());
                    url = appinfor.getApp().getUrl();
                }
                BmobFile bmobfile = new BmobFile("xiaoyuan.apk", "", url);
                downloadFile(bmobfile);
            }

            @Override
            public void onError(int i, String s) {
                Toast.show("查询更新失败");
                Log.d("查询更新失败了", "");
                return;
            }
        });


    }


    private void downloadFile(BmobFile file) {
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(context, saveFile, new DownloadFileListener() {
            @Override
            public void onSuccess(String s) {
                Toast.show("下载成功,保存路径:" + s);
                log.d(s);
                msavePath = s;
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.show("下载失败：" + s);
                log.d(s);
            }

            @Override
            public void onProgress(Integer progress, long total) {
                super.onProgress(progress, total);
                jd.setText("下载进度：" + progress + "%");
            }

            @Override
            public void onStart() {
                super.onStart();
                Toast.show("开始下载...");
            }
        });


    }


    public void openapk() {
        if (msavePath != null) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.fromFile(new File(msavePath)), "application/vnd.android.package-archive");
            startActivity(i);
        }
    }


}




