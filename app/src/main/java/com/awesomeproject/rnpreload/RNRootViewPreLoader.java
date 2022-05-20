package com.awesomeproject.rnpreload;

import android.app.Activity;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import com.awesomeproject.BuildConfig;
import com.awesomeproject.MainApplication;
import com.awesomeproject.utils.AppContext;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class RNRootViewPreLoader {

    private static final Map<String, ReactRootView> CACHE = new WeakHashMap<>();

    /**
     * 初始化ReactRootView，并添加到缓存
     *
     * @param context   上下文对象
     * @param componentName 加载的组件名
     */
    public static void preLoad(Context context, String componentName) {

        if (CACHE.get(componentName) != null) {
            return;
        }
        ReactRootView rootView = new ReactRootView(new MutableContextWrapper(context.getApplicationContext()));
        rootView.startReactApplication(
                ((MainApplication) context.getApplicationContext()).getReactNativeHost().getReactInstanceManager(),
                componentName,
                null);

        CACHE.put(componentName, rootView);
    }

    public static void preLoad(Context context, String bundlePath,String modulePath,String moduleName) {
        if (CACHE.get(moduleName) != null) {
            return;
        }
        ReactRootView mReactRootView = new ReactRootView(new MutableContextWrapper(context.getApplicationContext()));
        List<ReactPackage> packages = new PackageList(AppContext.getAppContext()).getPackages();
        // 有一些第三方可能不能自动链接，对于这些包我们可以用下面的方式手动添加进来：
        // packages.add(new MyReactNativePackage());
        // 同时需要手动把他们添加到`settings.gradle`和 `app/build.gradle`配置文件中。
        ReactInstanceManager mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(AppContext.getAppContext())
                .setBundleAssetName(bundlePath)
                .setJSMainModulePath(modulePath)
                .addPackages(packages)
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.BEFORE_RESUME)
                .build();
        // 注意这里的MyReactNativeApp 必须对应"index.js"中的
        // "AppRegistry.registerComponent()"的第一个参数，注意不是直接填appName
        mReactRootView.startReactApplication(mReactInstanceManager, moduleName, null);
        CACHE.put(moduleName, mReactRootView);
    }

    /**
     * 获取ReactRootView
     *
     * @param componentName 加载的组件名
     * @return ReactRootView
     */
    public static ReactRootView getReactRootView(Activity activity, String componentName) {
        ReactRootView rootView = CACHE.get(componentName);
        if (rootView != null && rootView.getContext() instanceof MutableContextWrapper) {
            ((MutableContextWrapper) rootView.getContext()).setBaseContext(
                    activity
            );
        }
        return rootView;
    }



    /**
     * 从当前界面移除 ReactRootView
     *
     * @param componentName 加载的组件名
     */
    public static void detachView(String componentName) {
        try {
            ReactRootView rootView = CACHE.get(componentName);
            if (rootView == null)
                return;
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
            if (rootView.getContext() instanceof MutableContextWrapper) {
                ((MutableContextWrapper) rootView.getContext()).setBaseContext(
                        rootView.getContext().getApplicationContext()
                );
            }
        } catch (Throwable e) {
            Log.e("RNRootViewPreLoader", e.getMessage());
        }
    }


    public static ReactRootView startReactApplication(Activity plainActivity, ReactInstanceManager reactInstanceManager, String componentName, Bundle launchOptions) {
        ReactRootView rootView = new ReactRootView(plainActivity);
        rootView.startReactApplication(
                reactInstanceManager,
                componentName,
                launchOptions);
        CACHE.put(componentName, rootView);
        return rootView;
    }

    public static void refreshRootView(String componentName) {
        ReactRootView rootView = CACHE.get(componentName);
        if (rootView == null)
            return;
        // 如果不为空,重新生成ReactContext
        rootView.getReactInstanceManager().recreateReactContextInBackground();
    }
}
