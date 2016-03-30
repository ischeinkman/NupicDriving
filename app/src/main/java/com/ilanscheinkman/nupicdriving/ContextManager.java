package com.ilanscheinkman.nupicdriving;

import android.content.Context;

/**
 * A class for the storing and loading of contexts statically,
 * so that presenters will be able to access context without having
 * to rely on their views to pass them in.
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
