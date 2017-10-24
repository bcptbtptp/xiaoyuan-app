package com.xiaoyuan.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 帮助类，往shareprefrences里存放，读取数据
 * 往文件夹中存放数据
 */
public class FileTools {

    /**
     * 保存数据到文件
     *
     * @param context  上下文
     * @param filename 文件名称
     * @param content  文件数据
     * @return 成功返回true  , 失败返回 false
     */
    public static boolean saveFile(Context context, String filename, String content) {
        try {
            File file = new File(context.getFilesDir(), filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从文件读取数据
     *
     * @param context  上下文
     * @param filename 文件名
     * @return 存在返回文件中的内容，不存在返回""
     */
    public static String getFile(Context context, String filename) {
        try {
            String result = "";
            File file = new File(context.getFilesDir(), filename);
            if (!file.exists()) {
                // 文件不存在
                return "";
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            // 分行读取
            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 保存数据到SharedPreferences(String)
     *
     * @param context 上下文
     * @param name    数据名称
     * @param date    数据内容
     */
    public static void saveshareString(Context context, String name, String date) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(name, date);
        editor.commit();
        System.out.println("保存到share 成功:" + name + "->" + date);
    }

    /**
     * 保存数据到SharedPreferences(Int)
     *
     * @param context 上下文
     * @param name    数据名称
     * @param date    数据内容
     */
    public static void saveshareInt(Context context, String name, int date) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(name, date);
        editor.commit();
        System.out.println("保存到share 成功:" + name + "->" + date);
    }

    /**
     * 从SharedPreferences读取数据 (Int)
     * @param context 上下文
     * @param name    数据名
     * @return 存在返回内容，不存在返回""
     */
    public static int getshareInt(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        int result = sp.getInt(name, 100);
        System.out.println("得到share 数据成功:" + result);
        return result;
    }

    /**
     * 从SharedPreferences读取数据 String
     * @param context 上下文
     * @param name    数据名
     * @return 存在返回内容，不存在返回""
     */
    public static String getshareString(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String result = sp.getString(name, "");
        System.out.println("得到share 数据成功:" + result);
        return result;
    }

}
