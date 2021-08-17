package com.prakash.ghimire.project.musicplayerapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.prakash.ghimire.project.musicplayerapp.R;
import com.prakash.ghimire.project.musicplayerapp.adapters.MusicAdapter;
import com.prakash.ghimire.project.musicplayerapp.model.Music;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MusicAdapter.ViewHolder.OnItemClickListener {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 1;

    Toolbar toolbar;
    private RecyclerView musicListRecyclerView;

    List<Music> musicList;
    private MusicAdapter adapter;

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


    @SuppressLint("NewApi")
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            } else {
                setupRecyclerAdapter();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Do something...
                setupRecyclerAdapter();

            } else {
                dialogBuilder();
            }
        }
    }

    private void dialogBuilder(){
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setTitle("Requires Permission");
        dialogBuilder.setMessage("To read and write external storage this application requires the permissions needed to"+
                " function properly. Please restart the app and grant access to the storage permission.Or, go to app"+
                " settings and enable storage permission.");

        dialogBuilder.setNegativeButton("Close", (dialog, which) -> finish());

        dialogBuilder.create().show();
    }

    private ArrayList<Music> getAllMusicFiles(Context context){
        ArrayList<Music> musicLists = new ArrayList<>();

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("InlinedApi") String[] projection = {
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
                        "album: "+album+"\n"+
                        "title: "+title+"\n"+
                        "artist: "+artist+"\n"+
                        "duration: "+duration);
                musicLists.add(musicModel);
            }

            cursor.close();
        }

        return musicLists;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setupRecyclerAdapter(){
        musicListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicListRecyclerView.setHasFixedSize(true);


        musicList = getAllMusicFiles(this);

        adapter = new MusicAdapter(this, musicList, this);
        musicListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void OnItemClick(int position) {
        String artistName = musicList.get(position).getArtist();
        String albumArt = musicList.get(position).getAlbum();
        String songTitle = musicList.get(position).getTitle();
        String path = musicList.get(position).getPath();

        Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
        intent.putExtra("artist", artistName);
        intent.putExtra("album", albumArt);
        intent.putExtra("title", songTitle);
        intent.putExtra("path", path);
        intent.putExtra("position", position);

        startActivity(intent);
    }
}