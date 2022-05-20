package com.awesomeproject;

import android.app.Application;
import android.content.Context;

import com.awesomeproject.rnpreload.BundleUtil;
import com.awesomeproject.rnpreload.RNRootViewPreLoader;
import com.awesomeproject.utils.AppContext;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.config.ReactFeatureFlags;
import com.facebook.soloader.SoLoader;
import com.awesomeproject.newarchitecture.MainApplicationReactNativeHost;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

    public static Application app;

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }

      };

  private final ReactNativeHost mNewArchitectureNativeHost =
      new MainApplicationReactNativeHost(this);

  @Override
  public ReactNativeHost getReactNativeHost() {
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      return mNewArchitectureNativeHost;
    } else {
      return mReactNativeHost;
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
    app = this;
    // If you opted-in for the New Architecture, we enable the TurboModule system
    ReactFeatureFlags.useTurboModules = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;
    SoLoader.init(this, /* native exopackage */ false);
    AppContext.injectContext(this);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
//      AppContext.getHandler().post(new Runnable() {
//          @Override
//          public void run() {
//              ReactInstanceManager reactInstanceManager = ((ReactApplication) AppContext.getAppContext()).getReactNativeHost().getReactInstanceManager();//getReactInstanceManager的时候getJSBundleFile
//              //hasStartedCreatingInitialContext()值是在子线程设置的，而getCurrentReactContext是在UI线程设置的，中间有个时间差。
//              if (!reactInstanceManager.hasStartedCreatingInitialContext() || reactInstanceManager.getCurrentReactContext() == null) {
//                  try {
//                      reactInstanceManager.createReactContextInBackground();
//                  } catch (Exception e) {
//                      reactInstanceManager.recreateReactContextInBackground();
//                      e.printStackTrace();
//                  }
//                  reactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
//                      @Override
//                      public void onReactContextInitialized(ReactContext reactContext) {
//                          loadBundle();
//                          reactInstanceManager.removeReactInstanceEventListener(this);
//                      }
//                  });
//              } else {
//                  loadBundle();
//              }
//          }
//      });
      //RNRootViewPreLoader.preLoad(this,"awesome/index.android.bundle","index","AwesomeProject");
  }

    private void loadBundle() {
        BundleUtil.loadScriptFromAsset("awesome/index.android.bundle");
        //BundleUtil.loadScriptFromAsset("awesome/index.android.bundle");
    }


    /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.awesomeproject.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
