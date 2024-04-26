package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;


import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import androidx.multidex.MultiDex;



public class xProfile extends Application {
    private static xProfile mInstance;
    private static xProfile myApplication;

    public static  String FEEDBACK_EMAIL = "";
    public static  String NATIVE_STYLE = "radio";
    public static  String PRIVACY_POLICY_URL = "";
    public static  String DEVELOPER_ACCOUNT_URL = "";
    public static  String BACKEND_RESOURCES_URL = "https://i.postimg.cc";
    public static  String FORMAT = ".png";

    public static void setApplication(xProfile application) {
        myApplication = application;
    }

    public static synchronized xProfile getInstance() {

        synchronized (xProfile.class) {
            synchronized (xProfile.class) {
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