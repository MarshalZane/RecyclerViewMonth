package com.michaelzane.recyclerviewmonth;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;


public class BaseApplication extends Application {

    final static String MY_TAG = "LoggerTest";

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(MY_TAG)//自定义日志TAG
                .logLevel(LogLevel.FULL);//测试阶段设置日志输出
        //初始化全局异常捕获处理器
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
