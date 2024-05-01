package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adapters.YourImageAdapter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.dialog.DetailsDialog;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.activities.AlbumSelectActivity;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.helpers.ConstantsCustomGallery;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.models.Image;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.utils.Constants;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.utils.PreferenceUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements DefaultLifecycleObserver {
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 123;

    PreferenceUtil sharedPref;


    TOOL iTool = TOOL.NONE;

    public enum TOOL {
        NONE,
        PROFILE,
        DRIP,
        PATTERN
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new PreferenceUtil(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.iProfile).setOnClickListener(v -> {
            iTool = TOOL.PROFILE;
            ePic();
        });
        findViewById(R.id.iPattern).setOnClickListener(v -> {
            iTool = TOOL.PATTERN;
            ePic();
        });
        findViewById(R.id.iDrip).setOnClickListener(v -> {
            iTool = TOOL.DRIP;
            ePic();
        });
        findViewById(R.id.iWorks).setOnClickListener(v -> {
            Intent privacy = new Intent(MainActivity.this, WorkActivity.class);
            startActivity(privacy);
        });

        findViewById(R.id.iShare).setOnClickListener(view -> {
            Intent myapp = new Intent(Intent.ACTION_SEND);
            myapp.setType("text/plain");
            myapp.putExtra(Intent.EXTRA_TEXT, getString(R.string.download_this) + "\n https://play.google.com/store/apps/details?id=" + getPackageName() + " \n");
            startActivity(myapp);
        });

        findViewById(R.id.iRate).setOnClickListener(view -> {
            if (!BuildConfig.DEBUG) {
                inAppReview();
            }
        });

        findViewById(R.id.iPrivacy).setOnClickListener(view -> {
            Log.d("qq", "moreApp");
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dpProfileMyApp.PRIVACY_POLICY_URL)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dpProfileMyApp.PRIVACY_POLICY_URL)));
            }
        });

        findViewById(R.id.iFeedback).setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{dpProfileMyApp.FEEDBACK_EMAIL});
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            startActivity(emailIntent);
        });

        findViewById(R.id.iMore).setOnClickListener(view -> {
            Log.d("qq", "moreApp");
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dpProfileMyApp.DEVELOPER_ACCOUNT_URL)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dpProfileMyApp.DEVELOPER_ACCOUNT_URL)));
            }
        });


        // List of Drawable resource IDs
        List<Integer> drawableList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            drawableList.add(R.drawable.main_acitvity_images);
        }


        RecyclerView recyclerView = findViewById(R.id.recyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(layoutManager);


        YourImageAdapter adapter = new YourImageAdapter(drawableList, MainActivity.this);
        recyclerView.setAdapter(adapter);


        final int itemCount = adapter.getItemCount();
        final int scrollDuration = 2000;
        final int scrollOffset = 5;

        final Runnable scrollRunnable = new Runnable() {
            int firstVisibleItem = 0;
            int lastVisibleItem = 0;
            boolean reverseScroll = false;

            @Override
            public void run() {
                if (!reverseScroll) {
                    recyclerView.smoothScrollBy(scrollOffset, 0);
                } else {
                    recyclerView.smoothScrollBy(-scrollOffset, 0);
                }
                if (reverseScroll && firstVisibleItem == 0) {
                    reverseScroll = false;
                } else if (!reverseScroll && lastVisibleItem == itemCount - 1) {
                    reverseScroll = true;
                }
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                recyclerView.postDelayed(this, scrollDuration / (lastVisibleItem - firstVisibleItem + 1));
            }
        };


        recyclerView.postDelayed(scrollRunnable, scrollDuration / (layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition() + 1));

    }


    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
    }

    @Override
    public void onDestroy() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        exitApp();
    }


    public void exitApp() {
        finish();

    }

    private void ePic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] arrPermissionsGrid = {"android.permission.READ_MEDIA_IMAGES"};
            Dexter.withContext(MainActivity.this).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
                        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1);
                        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(MainActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> Toast.makeText(MainActivity.this, "Error occurred! ", LENGTH_SHORT).show()).onSameThread().check();

        } else {
            String[] arrPermissionsGrid = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (Build.VERSION.SDK_INT >= 29)
                arrPermissionsGrid = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
            Dexter.withContext(MainActivity.this).withPermissions(arrPermissionsGrid).withListener(new MultiplePermissionsListener() {
                public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
                        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1);
                        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                    }
                    if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                        DetailsDialog.showDetailsDialog(MainActivity.this);
                    }
                }

                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).withErrorListener(dexterError -> Toast.makeText(MainActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
        }
    }

    private void inAppReview() {

        Log.d(TAG, "in app review token : " + sharedPref.getInAppReviewToken());
    }


    public void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
    }

    public void showSnackBar(String msg) {
        Toast.makeText(this, msg, LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            for (int i = 0; i < images.size(); i++) {
                Uri uri = Uri.fromFile(new File(images.get(i).path));
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(uri);
                    final Bitmap btm = BitmapFactory.decodeStream(imageStream);
                    send(btm);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    private void send(Bitmap btm) {
        CropperActivity.setMainBtm(btm);
        Intent intent = new Intent(this, CropperActivity.class);
        if (iTool == TOOL.PROFILE) {
            intent.putExtra(Constants.OPEN, Constants.OPEN_PROFILE);
        } else if (iTool == TOOL.DRIP) {
            intent.putExtra(Constants.OPEN, Constants.OPEN_DRIP);
        } else if (iTool == TOOL.PATTERN) {
            intent.putExtra(Constants.OPEN, Constants.OPEN_PATTERN);
        }
        startActivityForResult(intent, 900);

    }


}
