package com.awesomeproject.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.ArrayRes;
import android.text.TextUtils;


public final class AppContext {
    private AppContext() {
    }

    public static final String RN_SOCKET_CLOSE_KEY = "isRnSocketClose";
    private static Application sApp;
    private final static Handler handler = new Handler(Looper.getMainLooper());
    private static String sVersionName = "";
    private static int sVersionCode = 0;
    private static String homeActivity = "";

    public static void injectContext(Application application) {
        sApp = application;
    }

    public static final Application getAppContext() {
        return sApp;
    }

    public static Handler getMainHandler() {
        return handler;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static String getAppVersionName() {
        if (!TextUtils.isEmpty(sVersionName)) {
            return sVersionName;
        }
        try {
            sVersionName = getAppContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sVersionName;
    }

    public static int getAppVersionCode() {
        if (sVersionCode > 0) {
            return sVersionCode;
        }
        try {
            sVersionCode = getAppContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sVersionCode;
    }

    public static String getPackageName() {
        return getAppContext().getPackageName();
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return getAppContext().getResources().getStringArray(id);
    }

    public static void injectHomeActivity(String homePage) {
        homeActivity = homePage;
    }

    public static boolean isHomeActivity(Activity activity) {
        return activity != null && !TextUtils.isEmpty(homeActivity) && homeActivity.equals(activity.getClass().getName());
    }
}

