package com.xiaoyuan.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

public class TimeTools {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 计算2个时间之差，返回天数
     *
     * @return
     */
    public static String getday(String date_old) {
        try {
            Date date_return = df.parse(date_old);
            // 获取当前时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            Date date_now = df.parse(str);

            long diff = date_now.getTime() - date_return.getTime();// 这样得到的差值是微秒级别
            long days = diff / 1000;

            String time = "";
            long d;

//            System.out.println("时间：" + days + "秒" + (days / 60) + "分" + (days / 3600) + "小时");
            if (days >= (60 * 60 * 24)) {//时间已经大于一天
                d = days / (60 * 60 * 24);
                time = d + "天前";
            } else if (days >= (60 * 60) && days < (60 * 60 * 24)) {//时间已经大于一小时小于一天
                d = days / (60 * 60);
                time = d + "小时前";
            } else if (days >= 60 && days < (60 * 60)) {//时间已经大于一分钟小于一小时
                d = days / 60;
                time = d + "分钟前";
            } else if (days >= 0 && days < 60) {//时间已经大于一秒小于一分钟
                d = days;
                time = d + "秒钟前";
            }

//            System.out.println("相差天数：" + time);
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 记录登录错误的次数,异常返回100
     */
    public static int login_erroenum(Context context) {
        try {
            //如果为空直接返回0
            String login_errortime = FileTools.getshareString(context, "login_errortime");
            log.d("login_errortime" + login_errortime);
            if ("".equals(login_errortime)) {
                //保存登录次数，并保存一个时间 2016-08-18 00:00:00
                FileTools.saveshareString(context, "login_errortime", "2016-08-18 00:00:00");
                FileTools.saveshareInt(context, "login_errornum", 0);
                return 0;
            }
            //如果不为空，当前时间对比上次登录错误的时间和次数
            int errornum = FileTools.getshareInt(context, "login_errornum");
            if (errornum == 100) {
                return 100;
            }
            if (errornum < 5) {//小于5次直接返回，大于5次判断错误时间
                return errornum;
            } else {

                Date date_return = df.parse(login_errortime);
                // 获取当前时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                Date date_now = df.parse(str);
                //转化为min
                long diff = (date_now.getTime() - date_return.getTime()) / (1000 * 60);// 这样得到的差值是微秒级别
                log.d("多少分钟了" + diff);
                if(diff >= 10){
                    //重置错误次数
                    FileTools.saveshareInt(context, "login_errornum", 0);
                    return 0;
                }else{
                    return 5;
                }
//                return diff >= 10 ? 0 : 5;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.d("得到次数出问题了");
            return 100;
        }
    }

//    // 将日期转化为对应的星期几
//    public static String xinqi(String day) {
//        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
//        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        Date date = new Date();
//        try {
//            date = sdfInput.parse(day);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        calendar.setTime(date);
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//        if (dayOfWeek < 0)
//            dayOfWeek = 0;
////		System.out.println(dayNames[dayOfWeek]);
//        return dayNames[dayOfWeek];
//    }
}
