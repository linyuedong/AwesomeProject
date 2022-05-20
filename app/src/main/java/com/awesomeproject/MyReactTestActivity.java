package com.awesomeproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;

public class MyReactTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_react_test);
        findViewById(R.id.bt1).setOnClickListener(v -> {
            Intent intent = new Intent(MyReactTestActivity.this,MyReactActivity.class);
            intent.putExtra("bundlePath","awesome/index.android.bundle");
            intent.putExtra("modulePath","index");
            intent.putExtra("moduleName","AwesomeProject");
            startActivity(intent);
        });

        findViewById(R.id.bt2).setOnClickListener(v -> {
            Intent intent = new Intent(MyReactTestActivity.this,MyReactActivity.class);
            intent.putExtra("bundlePath","Demo59/index.android.bundle");
            intent.putExtra("modulePath","index");
            intent.putExtra("moduleName","Demo59");
            startActivity(intent);
        });

    }
}