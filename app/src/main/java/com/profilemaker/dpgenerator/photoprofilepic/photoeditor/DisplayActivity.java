package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.utils.Constants;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.views.CornerImage;

public class DisplayActivity extends BaseActivity {
    CornerImage mMainImage;
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);


        mMainImage = (CornerImage) findViewById(R.id.iView);
        imgUri = Uri.parse(getIntent().getStringExtra(Constants.KEY_URI_IMAGE));
        mMainImage.setImageURI(imgUri);

        findViewById(R.id.ivClose).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.ivShare).setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("image/png");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            startActivity(Intent.createChooser(sharingIntent, "Share image using"));
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
