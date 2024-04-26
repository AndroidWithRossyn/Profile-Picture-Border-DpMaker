package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;

import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.utils.Constants;


public class ShareActivity extends BaseActivity {
    private static final String TAG = "ShareActivity";
    private File files;
    ImageView mMainImage;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_share);


        findViewById(R.id.ivClose).setOnClickListener(v -> onBackPressed());


        findViewById(R.id.tvContinue).setOnClickListener(v -> {
            startActivity(new Intent(ShareActivity.this, MainActivity.class));
            finishAffinity();
        });
        String path = getIntent().getExtras().getString("img");
        this.files = new File(path);
        mMainImage = (ImageView) findViewById(R.id.iView);

        Glide.with(getApplicationContext()).load(files).into((ImageView) mMainImage);

        findViewById(R.id.ivFb).setOnClickListener(view -> shareToFacebook(files.getPath()));

        findViewById(R.id.llShare).setVisibility(View.VISIBLE);

        findViewById(R.id.ivInsta).setOnClickListener(view -> shareToInstagram(files.getPath()));
        findViewById(R.id.ivOther).setOnClickListener(view -> shareImage(files.getPath()));
        findViewById(R.id.ivTwitter).setOnClickListener(view -> shareToTwitter(files.getPath()));
        findViewById(R.id.ivWhats).setOnClickListener(view -> sendToWhatsaApp(files.getPath()));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void sendToWhatsaApp(String str) {
        String str2 = Constants.WHATSAPP;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.setPackage(str2);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void shareToFacebook(String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        String str2 = Constants.FACEBOOK;
        intent.setPackage(str2);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this, sb.toString(), new File(str)));
            intent.setType("image/*");
            intent.addFlags(1);
            startActivity(Intent.createChooser(intent, "Share Photo."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareToInstagram(String str) {
        String str2 = Constants.INSTAGRAM;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.setPackage(str2);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareToTwitter(String str) {
        String str2 = Constants.TWITTER;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            intent.setPackage(str2);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void shareImage(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uriForFile = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.addFlags(524288);
            intent.setType("image/*");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getResources().getString(R.string.download_this));
            sb2.append("\nhttps://play.google.com/store/apps/details?id=");
            sb2.append(getPackageName());
            intent.putExtra("android.intent.extra.TEXT", sb2.toString());
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("shareImage: ");
            sb3.append(e);
            Log.e(TAG, sb3.toString());
        }
    }
}