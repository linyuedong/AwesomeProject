package com.awesomeproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.awesomeproject.rnpreload.RNRootViewPreLoader;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceEventListener;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;

import java.util.List;

public class SingeInstanceReactActivity extends AppCompatActivity {

    private static final String TAG = "MyReactActivity";
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    private String bundlePath;
    private String modulePath;
    private String moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        long l1 = System.currentTimeMillis();
        initView();
        long l2 = System.currentTimeMillis();
        Log.i(TAG, "l1 = " + l1);
        Log.i(TAG, "l2 = " + l2);
        Log.i(TAG, "l = " + (l2-l1));
        //setContentView(R.layout.activity_my_react);
        setContentView(mReactRootView);
    }

    private void initData() {
        Intent intent = getIntent();
        bundlePath = intent.getStringExtra("bundlePath");
        modulePath = intent.getStringExtra("modulePath");
        moduleName = intent.getStringExtra("moduleName");
    }

    private void initView() {
        long l3 = System.currentTimeMillis();
        ReactRootView reactRootView = RNRootViewPreLoader.getReactRootView(this, moduleName);
        if(reactRootView != null){
            mReactRootView = reactRootView;
            Log.i(TAG, "initView: != null");
            return;
        }
        mReactRootView = new ReactRootView(this);
        List<ReactPackage> packages = new PackageList(getApplication()).getPackages();
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // packages.add(new MyReactNativePackage());
        // ????????????????????????????????????`settings.gradle`??? `app/build.gradle`??????????????????
        mReactInstanceManager = ((ReactApplication)getApplication()).getReactNativeHost().getReactInstanceManager();
        // ???????????????MyReactNativeApp ????????????"index.js"??????
        // "AppRegistry.registerComponent()"??????????????????????????????????????????appName
        mReactInstanceManager.addReactInstanceEventListener(new ReactInstanceEventListener() {
            @Override
            public void onReactContextInitialized(ReactContext context) {
                long l4 = System.currentTimeMillis();
                Log.i(TAG, "l3 = " + l3);
                Log.i(TAG, "l4 = " + l4);
                Log.i(TAG, "l34 = " + (l4-l3));
            }
        });

        mReactRootView.startReactApplication(mReactInstanceManager, moduleName, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RNRootViewPreLoader.detachView(moduleName);
    }
}