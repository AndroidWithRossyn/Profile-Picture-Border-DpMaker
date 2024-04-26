package com.profilemaker.dpgenerator.photoprofilepic.photoeditor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adapters.AdjustAdapter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.AdjustConfig;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.ContrastFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.EmbossFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.ExposureFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.HighlightShadowFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.HueFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.ParentFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.RGBFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.SaturationFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.SharpnessFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.VibranceFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.VignetteFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.adjust.WhiteBalanceFilter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.listener.AdjustListener;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.model.AdjustModel;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.utils.SaveFileUtils;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.views.SliderController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;

public class AdjustActivity extends BaseActivity {

    GPUImageFilterGroup group = new GPUImageFilterGroup(null);
    GPUImageView ivMain;
    GPUImageBrightnessFilter empty = new GPUImageBrightnessFilter();
    public static Bitmap iBtm = null;

    RecyclerView rvTool;
    AdjustAdapter adjust;

    FragmentContainerView iSlide;
    SliderController controller;

    ArrayList<ParentFilter> ivList;

    ArrayList<Integer> ivFilter = new ArrayList<>();
    ArrayList<GPUImageFilter> filters = new ArrayList<>();


    public static void setMainBtm(Bitmap bitmap) {
        iBtm = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adjust);


        rvTool = findViewById(R.id.rvTool);
        ivMain = findViewById(R.id.ivMain);
        findViewById(R.id.ivCompare).setOnTouchListener(onTouchListener);
        iSlide = findViewById(R.id.iSlide);

        ivMain.setRatio((float) iBtm.getWidth() / iBtm.getHeight());

        ivMain.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
        ivMain.setImage(iBtm);


        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.ivDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new saveImage().execute();
            }
        });
        empty.setBrightness(0f);

        ivList = new ArrayList<>(Arrays.asList(
                new ExposureFilter(new ArrayList<>(Collections.singletonList(new AdjustConfig(-0.6f, 0f, 0.6f, 0f, -50f, 50f))), this),
                new ContrastFilter(new ArrayList<>(Collections.singletonList(
                        new AdjustConfig(0.5f, 1f, 1.7f, 0f, -50f, 50f))), this),
                new SaturationFilter(new ArrayList<>(Collections.singletonList(
                        new AdjustConfig(0f, 1f, 1.7f, 0f, -50f, 50f))), this),
                new WhiteBalanceFilter(new ArrayList<>(Arrays.asList(
                        new AdjustConfig(3600f, 5550f, 12000f, 0f, -50f, 50f),
                        new AdjustConfig(-100f, 0f, 100f, 0f, -50f, 50f))), this),
                new VibranceFilter(new ArrayList<>(Collections.singletonList(
                        new AdjustConfig(-0.5f, 0f, 1f, 0f, -50f, 50f))), this),
                new EmbossFilter(new ArrayList<>(Collections.singletonList(new AdjustConfig(0f, 0f, 4.0f, 0f, 0f, 100f))),this),
                new HighlightShadowFilter(new ArrayList<>(Arrays.asList(
                        new AdjustConfig(1f, 1f, -0.5f, 0f, 0f, 100f),
                        new AdjustConfig(0f, 0f, 1.5f, 0f, 0f, 100f))), this),
                new HueFilter(new ArrayList<>(Collections.singletonList(
                        new AdjustConfig(0f, 0f, 355f, 0f, 0f, 100f))), this),
                new RGBFilter(new ArrayList<>(Arrays.asList(
                        new AdjustConfig(0.1f, 1f, 2f, 0f, -50f, 50f),
                        new AdjustConfig(0.1f, 1f, 2f, 0f, -50f, 50f),
                        new AdjustConfig(0.1f, 1f, 2f, 0f, -50f, 50f))), this),
                new SharpnessFilter(new ArrayList<>(Collections.singletonList(
                        new AdjustConfig(0f, 0f, 1.5f, 0f, 0f, 100f))), this),
                new VignetteFilter(new ArrayList<>(Arrays.asList(
                        new AdjustConfig(0.5f, 0.5f, 0f, 0f, 0f, 100f),
                        new AdjustConfig(1.7f, 1.7f, 0.55f, 0f, 0f, 100f))), this)
                ));

        ArrayList<AdjustModel> listAdjust = new ArrayList<AdjustModel>(Arrays.asList(
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_brightness), getResources().getString(R.string.brightness)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_contrast), getResources().getString(R.string.contrast)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_saturation), getResources().getString(R.string.saturation)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_wb), getResources().getString(R.string.wb)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_vibrance), getResources().getString(R.string.vibrance)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_sharpen), getResources().getString(R.string.emboss)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_temperature), getResources().getString(R.string.shadow)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_hue), getResources().getString(R.string.hue)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_vignette), getResources().getString(R.string.rgb)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_sharpene), getResources().getString(R.string.sharpen)),
                new AdjustModel(AppCompatResources.getDrawable(this, R.drawable.ic_vignettes), getResources().getString(R.string.vignette))
        ));

        adjust = new AdjustAdapter(listAdjust, R.layout.item_adjust);

        AdjustListener listener1 = new AdjustListener(this, rvTool, new AdjustListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                controller = SliderController.newInstance(position, listAdjust.get(position).getText(), ivList.get(position));
                getSupportFragmentManager().beginTransaction().replace(R.id.iSlide, controller).commit();
                rvTool.setVisibility(View.GONE);
            }
            @Override  public void onLongClick(View view, int position) { }
        });

        rvTool.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTool.setAdapter(adjust);
        rvTool.addOnItemTouchListener(listener1);

    }

    @SuppressLint("ClickableViewAccessibility")
    View.OnTouchListener onTouchListener = (view, motionEvent) ->{
        switch (motionEvent.getAction()) {
            case 0:
                if (!group.getFilters().isEmpty()) ivMain.setFilter(empty);
                return true;
            case 1:
                if (!group.getFilters().isEmpty()) ivMain.setFilter(group);
                return false;
            default:
                return true;
        }
    };

    class saveImage extends AsyncTask<Void, String, String> {
        Bitmap exported = ivMain.getGPUImage().getBitmapWithFilterApplied();
        public void onPreExecute() {
        }

        public String doInBackground(Void... voids) {
            try {
                return SaveFileUtils.saveBitmap(AdjustActivity.this, exported, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()), null).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
            if (string == null) {
                Toast.makeText(getApplicationContext(), R.string.txt_select_picture, Toast.LENGTH_LONG).show();
                return;
            }
            Intent i = new Intent(AdjustActivity.this, ShareActivity.class);
            i.putExtra("img", string);
            startActivity(i);

        }

    }

    @Override
    public void onBackPressed() {
        if (!group.getFilters().isEmpty()) {
            rvTool.setAdapter(null);
            AdjustActivity.super.onBackPressed();
        } else {
            finish();

        }
    }



    @Override
    public void onResume() {
        super.onResume();

    }


    public void onCloseSimpleFragment(ParentFilter currentFilter) {
        updateFilterGroup(currentFilter);
        rvTool.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().remove(controller).commit();
    }

    public void updateFilterGroup(ParentFilter currentFilter) {
        for (int i = 0; i< ivFilter.size(); ++i) {
            if (ivFilter.get(i) == currentFilter.getFilterIndex()) {
                filters.set(i, currentFilter.getFilter());
                group = new GPUImageFilterGroup(filters);
                ivMain.setFilter(group);
                return;
            }
        }
        ivFilter.add(currentFilter.getFilterIndex());
        filters.add(currentFilter.getFilter());
        group = new GPUImageFilterGroup(filters);
        ivMain.setFilter(group);
    }

}
