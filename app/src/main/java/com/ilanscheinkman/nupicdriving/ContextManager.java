package com.ilanscheinkman.nupicdriving;

import android.content.Context;

/**
 * Created by ilan on 3/24/16.
 */
public class ContextManager {
    private static Context appContext;

    public static Context getAppContext(){
        return appContext;
    }

    public static void initialize(Context context){
        if(appContext == null) appContext = context.getApplicationContext();
    }
}
