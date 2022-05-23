package com.awesomeproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.awesomeproject.rnpreload.BundleUtil;
import com.awesomeproject.rnpreload.ScriptUtil;
import com.awesomeproject.utils.AppContext;
import com.example.songlcy.rnandroid.MainActivity;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.soloader.SoLoader;

public class MyReactTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_react_test);


        //RNRootViewPreLoader.preLoad(this,"awesome/index.android.bundle","index","AwesomeProject");
        findViewById(R.id.bt1).setOnClickListener(v -> {
            Intent intent = new Intent(MyReactTestActivity.this,MyReactActivity.class);
            intent.putExtra("bundlePath","awesome/index.android.bundle");
            intent.putExtra("modulePath","index");
            intent.putExtra("moduleName","AwesomeProject");
            startActivity(intent);
        });

        findViewById(R.id.bt2).setOnClickListener(v -> {
            Intent intent = new Intent(MyReactTestActivity.this,SingeInstanceReactActivity.class);
            //intent.putExtra("moduleName","FirstModule");
            intent.putExtra("moduleName","AwesomeProject");
            startActivity(intent);
        });

//        startActivity(new Intent(this, MainActivity.class));

        findViewById(R.id.initContext).setOnClickListener(v -> {
            initReactContext1();
        });
    }

    private void initReactContext() {
        ReactInstanceManager rim = ((ReactApplication)getApplication()).getReactNativeHost().getReactInstanceManager();
        Log.e("loadReactContext:", "RN C++ 层开始加载");
        SoLoader.init(getApplication(),false);
        Log.e("loadReactContext:", "RN C++ 层加载完成");
        Log.e("loadReactContext:", "基础 bundle 开始加载");
        if(!rim.hasStartedCreatingInitialContext()) {
            rim.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                // 初始化react上下文时调用（所有模块都已注册）。 总是调用UI线程。
                @Override
                public void onReactContextInitialized(ReactContext context) {
                    Log.e("onReactContextInit:", "基础 bundle 加载完成");
                    Toast.makeText(MyReactTestActivity.this, "基础 bundle 加载完成", Toast.LENGTH_SHORT).show();
                    loadBundle();
                    Log.e(this.getClass().getName(), "子模块 bundle 开始加载");
                    Log.e("onReactContextInit:", "子模块 bundle 加载完成");
                    Toast.makeText(MyReactTestActivity.this, "子模块 bundle 加载完成", Toast.LENGTH_SHORT).show();
                    rim.removeReactInstanceEventListener(this);
                }
            });
        }
        rim.createReactContextInBackground();
    }

    private void initReactContext1() {
        AppContext.getHandler().post(new Runnable() {
            @Override
            public void run() {
                ReactInstanceManager reactInstanceManager = ((ReactApplication) getApplication()).getReactNativeHost().getReactInstanceManager();//getReactInstanceManager的时候getJSBundleFile
                //hasStartedCreatingInitialContext()值是在子线程设置的，而getCurrentReactContext是在UI线程设置的，中间有个时间差。
                if (!reactInstanceManager.hasStartedCreatingInitialContext() || reactInstanceManager.getCurrentReactContext() == null) {
                    reactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                        @Override
                        public void onReactContextInitialized(ReactContext reactContext) {
                            reactInstanceManager.removeReactInstanceEventListener(this);
                            loadBundle();
                        }
                    });
                    try {
                        reactInstanceManager.createReactContextInBackground();
                    } catch (Exception e) {
                        reactInstanceManager.recreateReactContextInBackground();
                        e.printStackTrace();
                    }

                } else {
                    loadBundle();
                }
            }
        });
    }

    private void loadBundle() {
        //react-native 版本必须一致
        BundleUtil.loadScriptFromAsset("awesome1.bundle");
        //BundleUtil.loadScriptFromAsset("first.bundle");
        //ScriptUtil.loadScriptFromAsset(this,((ReactApplication)getApplication()).getReactNativeHost().getReactInstanceManager().getCurrentReactContext().getCatalystInstance(), "first.bundle", false);

        //BundleUtil.loadScriptFromAsset("awesome/index.android.bundle");
    }
}