package com.dallmeier.evidencer.base;

import androidx.multidex.MultiDexApplication;

import com.dallmeier.evidencer.utils.SharedPrefUtil;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Created by Mr Son on 2016-07-27.
 */
@HiltAndroidApp
public final class BaseApplication extends MultiDexApplication {
    private static BaseApplication mInstance;

    public BaseApplication() {
        super();
        mInstance = this;
    }

    /**
     * Get Singleton Instance
     *
     * @return BaseApplication
     */
    public static BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefUtil.init(getApplicationContext());
    }
}
