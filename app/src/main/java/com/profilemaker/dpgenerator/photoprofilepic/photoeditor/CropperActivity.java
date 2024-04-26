package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.flipzeus.FlipDirection;
import com.github.flipzeus.ImageFlipper;
import com.isseiaoki.simplecropview.CropImageView;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.utils.Constants;

public class CropperActivity extends BaseActivity{
    static Bitmap btm;
    public CropImageView iCrop;
    String iFrom;

    public static void setMainBtm(Bitmap bitmap) {
        btm = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cropper);

        iFrom = getIntent().getStringExtra(Constants.OPEN);

        this.iCrop = findViewById(R.id.crop_image_view);
        iCrop.setImageBitmap(btm);
        iCrop.setCropMode(CropImageView.CropMode.SQUARE);
        findViewById(R.id.iLeft).setOnClickListener(view -> iCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D));

        findViewById(R.id.ivClose).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.ivDone).setOnClickListener(view -> new saveCrop().execute());
        findViewById(R.id.iRight).setOnClickListener(view -> iCrop.rotateImage(CropImageView.RotateDegrees.ROTATE_90D));
        findViewById(R.id.iVertical).setOnClickListener(view -> ImageFlipper.flip(iCrop, FlipDirection.VERTICAL));

        findViewById(R.id.iHorizontal).setOnClickListener(view -> ImageFlipper.flip(iCrop, FlipDirection.HORIZONTAL));

    }


    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public void onResume() {
        super.onResume();

    }



    class saveCrop extends AsyncTask<Void, Bitmap, Bitmap> {

        public Bitmap doInBackground(Void... voidArr) {
            return iCrop.getCroppedBitmap();
        }

        public void onPostExecute(Bitmap bitmap) {
            Intent intent = null;
            if (iFrom.equalsIgnoreCase(Constants.OPEN_PROFILE)){
                ProfileActivity.setMainBtm(bitmap);
                intent = new Intent(CropperActivity.this, ProfileActivity.class);
            } else if (iFrom.equalsIgnoreCase(Constants.OPEN_DRIP)){
                DripActivity.setMainBtm(bitmap);
                intent = new Intent(CropperActivity.this, DripActivity.class);
            } else if (iFrom.equalsIgnoreCase(Constants.OPEN_PATTERN)){
                PatternActivity.setMainBtm(bitmap);
                intent = new Intent(CropperActivity.this, PatternActivity.class);
            }
            startActivityForResult(intent, 900);
            finish();

        }
    }
}
