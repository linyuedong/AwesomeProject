package com.example.songlcy.rnandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.awesomeproject.R;
import com.awesomeproject.SingeInstanceReactActivity;
import com.awesomeproject.rnpreload.ScriptUtil;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ReactContext;
import com.facebook.soloader.SoLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLoad;
    private Button btnUnLoad;
    private Button btnLoadA;
    private Button btnLoadB;
    private ReactInstanceManager rim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getReactInstanceManager();
        initView();
        initListener();

    }

    private void getReactInstanceManager() {
        rim = ((ReactApplication)getApplication()).getReactNativeHost().getReactInstanceManager();
    }

    private void initView() {
        btnLoad = findViewById(R.id.btn_load);
        btnUnLoad = findViewById(R.id.btn_unload);
        btnLoadA = findViewById(R.id.btn_loadA);
        btnLoadB = findViewById(R.id.btn_loadB);
    }

    private void initListener() {
        btnLoad.setOnClickListener(this);
        btnUnLoad.setOnClickListener(this);
        btnLoadA.setOnClickListener(this);
        btnLoadB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                loadReactContext();
                break;
            case R.id.btn_unload:
                unLoadReactContext();
                break;
            case R.id.btn_loadA:
                ReactNativeHost reactNativeHost = ((ReactApplication)getApplication()).getReactNativeHost();
//                if(ScriptUtil.hasStartedCreatingInitialContext(reactNativeHost)) {
//                    startRNActivity("A");
//                } else {
//                    Toast.makeText(this, "?????????RN??????????????????????????????ReactContext???RN????????????", Toast.LENGTH_SHORT).show();
//                }
                Intent intent = new Intent(this, SingeInstanceReactActivity.class);
                intent.putExtra("bundlePath","awesome/index.android.bundle");
                intent.putExtra("modulePath","index");
                intent.putExtra("moduleName","FirstModule");
                startActivity(intent);
                break;

            case R.id.btn_loadB:
                startRNActivity("B");
                break;
            default:
                break;
        }
    }

    /**
     * ?????? ReactContext
     */
    private void loadReactContext() {
        Log.e("loadReactContext:", "RN C++ ???????????????");
        SoLoader.init(getApplication(),false);
        Log.e("loadReactContext:", "RN C++ ???????????????");
        Log.e("loadReactContext:", "?????? bundle ????????????");
        if(!rim.hasStartedCreatingInitialContext()) {
            rim.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {

                // ?????????react??????????????????????????????????????????????????? ????????????UI?????????
                @Override
                public void onReactContextInitialized(ReactContext context) {
                    Log.e("onReactContextInit:", "?????? bundle ????????????");
                    Toast.makeText(MainActivity.this, "?????? bundle ????????????", Toast.LENGTH_SHORT).show();
                    Log.e(this.getClass().getName(), "????????? bundle ????????????");
                    loadSubModule();
                    Log.e("onReactContextInit:", "????????? bundle ????????????");
                    Toast.makeText(MainActivity.this, "????????? bundle ????????????", Toast.LENGTH_SHORT).show();
                    rim.removeReactInstanceEventListener(this);
                }
            });
        }

        rim.createReactContextInBackground();
    }

    /**
     * ?????? ReactContext
     */
    private void unLoadReactContext() {
        rim.destroy();
    }

    /**
     * ?????????????????? bundle
     */
    private void loadSubModule() {
        ScriptUtil.loadScriptFromAsset(this,rim.getCurrentReactContext().getCatalystInstance(), "first.bundle", false);
        //ScriptUtil.loadScriptFromAsset(this,rim.getCurrentReactContext().getCatalystInstance(),"second.bundle", false);
    }

    /**
     * ??????????????? RN ??????
     * @param module
     */
    private void startRNActivity(String module) {
        Intent intent = new Intent();
        String className = module == "A" ? FirstModuleRNActivity.class.getName() : SecondModuleRNActivity.class.getName();
        intent.setClassName(this, className);
        startActivity(intent);
    }

}
