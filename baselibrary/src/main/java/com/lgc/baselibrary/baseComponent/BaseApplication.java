package com.lgc.baselibrary.baseComponent;

import android.app.Application;
import android.util.Log;

/**
 * <pre>
 *      author : liuguicen
 *      time : 2018/05/19
 *      version : 1.0
 * <pre>
 */

public class BaseApplication extends Application{
    final static String TAG = "MyApplication";

    public static BaseApplication appContext ;

    public BaseApplication() {
        Log.e(TAG, "MyApplication: 应用创建了");
        appContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static BaseApplication getAppContext() {
        return appContext;
    }
}
