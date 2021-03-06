package com.gnayils.obiew;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.gnayils.obiew.weibo.EmotionDB;

/**
 * Created by Administrator on 8/13/2016.
 */
public class Obiew extends Application implements Application.ActivityLifecycleCallbacks {

    public static Obiew obiew;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        obiew = this;
        getTheme().applyStyle(Preferences.getThemeResource(), true);
        registerActivityLifecycleCallbacks(this);
        EmotionDB.initialize(this);
    }

    public static Activity getCurrentActivity() {
        return obiew == null ? null : obiew.currentActivity;
    }

    public static Context getAppContext() {
        return obiew == null ? null : obiew.getApplicationContext();
    }

    public static Resources getAppResources() {
        return obiew == null ? null : obiew.getResources();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(this);
        EmotionDB.destroy();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        currentActivity = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
