package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash);


        startMainActivity();
    }


    public void onDestroy() {
        // startMainActivity();
        super.onDestroy();
    }

    private void startMainActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }

}

