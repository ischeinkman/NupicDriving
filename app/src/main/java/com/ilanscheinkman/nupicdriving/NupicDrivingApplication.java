package com.ilanscheinkman.nupicdriving;

import android.app.Application;

/**
 * Created by ilan on 3/12/16.
 */
public class NupicDrivingApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ContextManager.initialize(getApplicationContext());
    }
}
