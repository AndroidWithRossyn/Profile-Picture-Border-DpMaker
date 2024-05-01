package com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.R;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.adapter.CustomAlbumSelectAdapter;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.helpers.ConstantsCustomGallery;
import com.profilemaker.dpgenerator.photoprofilepic.photoeditor.picker.models.Album;


public class AlbumSelectActivity extends HelperActivity {
    private ArrayList<Album> albums;

    private TextView errorDisplay, tvProfile;

    private ProgressBar loader;
    private GridView gridView;
    private CustomAlbumSelectAdapter adapter;

    private ContentObserver observer;
    private Handler handler;
    private Thread thread;

    private final String[] projection = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_select);

        setView(findViewById(R.id.layout_album_select));

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        ConstantsCustomGallery.limit = intent.getIntExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, ConstantsCustomGallery.DEFAULT_LIMIT);

        errorDisplay = (TextView) findViewById(R.id.text_view_error);
        errorDisplay.setVisibility(View.INVISIBLE);

        tvProfile = (TextView) findViewById(R.id.tvProfile);
        tvProfile.setText(R.string.album_view);

        loader = (ProgressBar) findViewById(R.id.loader);
        gridView = (GridView) findViewById(R.id.grid_view_album_select);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ImageSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_ALBUM, albums.get(position).name);
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
            }
        });

        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onStart() {
        super.onStart();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ConstantsCustomGallery.PERMISSION_GRANTED: {
                        loadAlbums();
                        break;
                    }

                    case ConstantsCustomGallery.FETCH_STARTED: {
                        loader.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.INVISIBLE);
                        break;
                    }

                    case ConstantsCustomGallery.FETCH_COMPLETED: {
                        if (adapter == null) {
                            adapter = new CustomAlbumSelectAdapter(AlbumSelectActivity.this, getApplicationContext(), albums);
                            gridView.setAdapter(adapter);

                            loader.setVisibility(View.GONE);
                            gridView.setVisibility(View.VISIBLE);
                            orientationBasedUI(getResources().getConfiguration().orientation);

                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    }

                    case ConstantsCustomGallery.ERROR: {
                        loader.setVisibility(View.GONE);
                        errorDisplay.setVisibility(View.VISIBLE);
                        break;
                    }

                    default: {
                        super.handleMessage(msg);
                    }
                }
            }
        };
        observer = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                loadAlbums();
            }
        };
        getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, observer);

        checkPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopThread();

        getContentResolver().unregisterContentObserver(observer);
        observer = null;

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        albums = null;
        if (adapter != null) {
            adapter.releaseResources();
        }
        gridView.setOnItemClickListener(null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientationBasedUI(newConfig.orientation);
    }

    private void orientationBasedUI(int orientation) {
        final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        if (adapter != null) {
            int size = orientation == Configuration.ORIENTATION_PORTRAIT ? metrics.widthPixels / 2 : metrics.widthPixels / 4;
            adapter.setLayoutParams(size);
        }
        gridView.setNumColumns(orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
    private void loadAlbums() {
        startThread(new AlbumLoaderRunnable());
    }

    private class AlbumLoaderRunnable implements Runnable {
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            if (adapter == null) {
                sendMessage(ConstantsCustomGallery.FETCH_STARTED);
            }

            Cursor cursor = getApplicationContext().getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                            null, null, MediaStore.Images.Media.DATE_MODIFIED);
            if (cursor == null) {
                sendMessage(ConstantsCustomGallery.ERROR);
                return;
            }

            ArrayList<Album> temp = new ArrayList<>(cursor.getCount());
            HashSet<Long> albumSet = new HashSet<>();
            File file;
            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return;
                    }

                    @SuppressLint("Range") long albumId = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    @SuppressLint("Range") String album = cursor.getString(cursor.getColumnIndex(projection[1]));
                    @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(projection[2]));

                    if (!albumSet.contains(albumId)) {
                        file = new File(image);
                        if (file.exists()) {

                            temp.add(new Album(album, image));
                            albumSet.add(albumId);
                        }
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();

            if (albums == null) {
                albums = new ArrayList<>();
            }
            albums.clear();
            albums.addAll(temp);

            sendMessage(ConstantsCustomGallery.FETCH_COMPLETED);
        }
    }

    private void startThread(Runnable runnable) {
        stopThread();
        thread = new Thread(runnable);
        thread.start();
    }

    private void stopThread() {
        if (thread == null || !thread.isAlive()) {
            return;
        }

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int what) {
        if (handler == null) {
            return;
        }

        Message message = handler.obtainMessage();
        message.what = what;
        message.sendToTarget();
    }

    @Override
    protected void permissionGranted() {
        Message message = handler.obtainMessage();
        message.what = ConstantsCustomGallery.PERMISSION_GRANTED;
        message.sendToTarget();
    }

    @Override
    protected void hideViews() {
        loader.setVisibility(View.GONE);
        gridView.setVisibility(View.INVISIBLE);
    }
}
