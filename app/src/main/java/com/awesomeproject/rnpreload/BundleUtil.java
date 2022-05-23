package com.awesomeproject.rnpreload;

import android.content.Context;
import android.util.Log;

import com.awesomeproject.MainApplication;
import com.awesomeproject.utils.AppContext;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.CatalystInstanceImpl;
import com.facebook.react.bridge.ReactContext;

public class BundleUtil {

    private static final String TAG = "BundleUtil";
    //创建ReactInstanceManager的时候调用
    public static void loadScriptFromAsset(String assetName) {
        String source = assetName;
        if(!assetName.startsWith("assets://")) {
            source = "assets://" + assetName;
        }
        getCatalystInstance().loadScriptFromAssets(AppContext.getAppContext().getAssets(), source,false);
    }


    public static void loadScriptFromFile(String fileName) {
       getCatalystInstance().loadScriptFromFile(fileName, fileName,false);
    }


    public static CatalystInstance getCatalystInstance() {
        ReactInstanceManager manager = getReactNativeHost().getReactInstanceManager();
        if (manager == null) {
            Log.e(TAG,"manager is null!!");
            return null;
        }

        ReactContext context = manager.getCurrentReactContext();
        if (context == null) {
            Log.e(TAG,"context is null!!");
            return null;
        }
        return context.getCatalystInstance();
    }


    public static ReactNativeHost getReactNativeHost(){
        return ((ReactApplication)AppContext.getAppContext()).getReactNativeHost();
    }

}
