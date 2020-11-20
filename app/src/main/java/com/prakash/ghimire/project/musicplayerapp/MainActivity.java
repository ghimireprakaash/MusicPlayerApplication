package com.prakash.ghimire.project.musicplayerapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.prakash.ghimire.project.musicplayerapp.model.Music;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 1;
    Toolbar toolbar;
    RecyclerView musicListRecyclerView;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        checkPermissions();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Music Player");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        musicListRecyclerView = findViewById(R.id.musicRecyclerView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            } else {
                getAllMusicFiles(this);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Do something...
                getAllMusicFiles(this);

            } else {
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
                dialogBuilder.setTitle("Requires Permission");
                dialogBuilder.setMessage("To read and write external storage this application requires the permissions needed to" +
                        " function properly.");
                dialogBuilder.setPositiveButton("Allow", (dialog, which) -> {

                });

                dialogBuilder.setNegativeButton("Deny", (dialog, which) -> dialogBuilder.setOnDismissListener(DialogInterface::dismiss));

                dialogBuilder.create().show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private ArrayList<Music> getAllMusicFiles(Context context){
        ArrayList<Music> musicList = new ArrayList<>();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA                 //For media path
        };

        Cursor cursor = context.getContentResolver().query(musicUri, projection,
                null,
                null,
                null);
        if (cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                String duration = cursor.getString(3);
                String path = cursor.getString(4);

                Music musicModel = new Music(album, title, artist, duration, path);
                Log.d(TAG, "path: "+path+"\n"+
                        "title: "+title+"\n"+
                        "artist: "+artist+"\n"+
                        "duration: "+duration);
                musicList.add(musicModel);
            }

            cursor.close();
        }

        return musicList;
    }
}