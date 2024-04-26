package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;

import static com.profilemaker.dpgenerator.photoprofilepic.photoeditor.utils.Constants.iBitmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.views.CornerImage;

public class PreviewActivity extends BaseActivity {
    CornerImage ivFb, ivInst, ivWhat;
    public static Bitmap btm;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_preview);

        findViewById(R.id.ivClose).setOnClickListener(v -> onBackPressed());

        if (iBitmap != null) {
            btm = iBitmap;
        }
        iViews();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void iViews() {
        ivFb = findViewById(R.id.ivFacebook);
        ivInst = findViewById(R.id.ivInstagram);
        ivWhat = findViewById(R.id.ivWhatsapp);
        ivFb.setImageBitmap(btm);
        ivInst.setImageBitmap(btm);
        ivWhat.setImageBitmap(btm);
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public void onResume() {
        super.onResume();

    }




}
