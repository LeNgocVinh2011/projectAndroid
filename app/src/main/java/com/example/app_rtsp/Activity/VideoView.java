package com.example.app_rtsp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_rtsp.R;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

public class VideoView extends AppCompatActivity{
    private LibVLC libVLC;
    private MediaPlayer mediaPlayer;
    private VLCVideoLayout videoLayout;
    private ProgressBar loading;

    private String camLink = "";

    private SharedPreferences sharedPreferences;
    private float keyOrientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_view);
        References();
        sharedPreferences = getSharedPreferences("pref", 0);
        keyOrientation = sharedPreferences.getFloat("keyOrientation", -1);

        setActivityOrientation((int) keyOrientation);

        libVLC = new LibVLC(this);
        mediaPlayer = new MediaPlayer(libVLC);
        videoLayout.requestFocus();
        mediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                if(event.type == 267 || event.type == 268){
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mediaPlayer.attachViews(videoLayout, null, false, false);

        Media media = new Media(libVLC, Uri.parse(RecieveLink()));
        media.setHWDecoderEnabled(true, false);
        media.addOption(":network-caching=600");

        mediaPlayer.setMedia(media);
        media.release();
        mediaPlayer.play();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mediaPlayer.stop();
        mediaPlayer.detachViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.release();
        libVLC.release();
    }

    private void References(){
        videoLayout = findViewById(R.id.camera_view);
        loading = findViewById(R.id.loadingPanel);
    }

    private String RecieveLink(){
        Intent intent = getIntent();
        camLink = intent.getStringExtra("cam_link");
        return camLink;
    }

    public void setActivityOrientation(int orientation)
    {
        Activity activity = VideoView.this;
        if(activity != null){
            activity.setRequestedOrientation(orientation);
        }
    }
}
