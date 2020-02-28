package com.m8.m8;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.lang.reflect.Method;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // the following line is important
        Fresco.initialize(getApplicationContext());
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
