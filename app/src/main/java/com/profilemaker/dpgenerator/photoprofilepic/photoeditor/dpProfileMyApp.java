package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;


import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import androidx.multidex.MultiDex;



public class dpProfileMyApp extends Application {
    private static dpProfileMyApp mInstance;
    private static dpProfileMyApp myApplication;
    public static  String FEEDBACK_EMAIL = "mangroliyasureshbhai.app@gmail.com";
    public static  String PRIVACY_POLICY_URL = "https://profilmaker.blogspot.com/2024/05/privacy-policy.html";
    public static  String DEVELOPER_ACCOUNT_URL = "https://play.google.com/store/apps/dev?id=8533694176296979137";
    public static  String BACKEND_RESOURCES_URL = "https://i.postimg.cc";
    public static  String FORMAT = ".png";

    public static void setApplication(dpProfileMyApp application) {
        myApplication = application;
    }

    public static synchronized dpProfileMyApp getInstance() {

        synchronized (dpProfileMyApp.class) {
            synchronized (dpProfileMyApp.class) {
                myApplication = mInstance;
            }
        }
        return myApplication;
    }


    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        setApplication(this);
        mInstance = this;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    public static Context getContext() {
        return mInstance.getContext();
    }


    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

}