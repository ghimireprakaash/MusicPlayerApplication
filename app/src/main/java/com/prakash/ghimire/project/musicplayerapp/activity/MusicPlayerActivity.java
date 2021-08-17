package com.prakash.ghimire.project.musicplayerapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.prakash.ghimire.project.musicplayerapp.R;

public class MusicPlayerActivity extends AppCompatActivity {
    int position = -1;

    RelativeLayout playPauseBtnLayout;

    //buttons like shuffle, previous, next, play, pause, loop...
    ImageView backIcon, album, defaultAlbum, shuffle, previous, playPauseBtn, next, loop;
    TextView artistName, songTitle, durationPlayed, durationTotal, autoScrollSongTitle;

    //SeekBar
    SeekBar musicSeekBar;


    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        init();
        backIcon.setOnClickListener(v -> finish());

        //retrieving music info's...
        getMusicInfo();
        //retrieving music path from the storage
        String path = getIntent().getStringExtra("path");
        //passing path to playMusic method
        playMusic(path);
//        musicSeekBar.setMax(songDuration);

        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int songProgress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                songProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(songProgress);
            }
        });

//        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mediaPlayer != null){
//                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
//                    musicSeekBar.setProgress(currentPosition);
//                    durationPlayed.setText(formattedTime(currentPosition));
//                }
//
//                handler.postDelayed(this, 1000);
//            }
//        });
    }

    private void init(){
        artistName = findViewById(R.id.artistName);
        backIcon = findViewById(R.id.backIcon);

        album = findViewById(R.id.album_art);
        defaultAlbum = findViewById(R.id.default_album_art);

        songTitle = findViewById(R.id.songTitle);

        musicSeekBar = findViewById(R.id.musicSeekBar);
        durationPlayed = findViewById(R.id.durationPlayed);
        durationTotal = findViewById(R.id.durationTotal);

        playPauseBtnLayout = findViewById(R.id.play_pause_button_layout);
        shuffle = findViewById(R.id.shuffle);
        previous = findViewById(R.id.play_previous);
        playPauseBtn = findViewById(R.id.play_pause_button);
        next = findViewById(R.id.play_next);
        loop = findViewById(R.id.loop);

        autoScrollSongTitle = findViewById(R.id.autoHorizontalScrollSongTitle);
    }

    private void getMusicInfo(){
        position = getIntent().getIntExtra("position", -1);

        String artist = getIntent().getStringExtra("artist");
        artistName.setText(artist);

//        String albumArt = getIntent().getStringExtra("Album");

        String title = getIntent().getStringExtra("title");
        songTitle.setText(title);

        autoScrollSongTitle.setText(title);
        autoScrollSongTitle.setMovementMethod(new ScrollingMovementMethod());
        autoScrollSongTitle.setSelected(true);
    }

    private void playMusic(String path){
        mediaPlayer = new MediaPlayer();
        if (mediaPlayer!=null){
            mediaPlayer.release();
            playPauseBtn.setImageResource(R.drawable.ic_pause);
        }

        try {
            mediaPlayer = MediaPlayer.create(this, Uri.parse(path));
            mediaPlayer.start();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private String formattedTime(int currentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1){
            return totalNew;
        } else {
            return totalOut;
        }
    }
}