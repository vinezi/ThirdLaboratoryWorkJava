package com.politov.third_laboratory_work;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    public static int VIDEO_CAPTURED = 1;
    VideoView videoView;
    ImageButton btnVideo, btnStartPause, btnStop;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VideoActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //btnStartPause.setImageResource(R.drawable.ic_video_play_btn);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, VIDEO_CAPTURED);
            }
        });
        /*btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPause();
            }
        });*/

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
            }
        });

    }
    private void init() {
        videoView = findViewById(R.id.videoView);
        btnVideo = findViewById(R.id.btnVideoIntent);
        //btnStartPause = findViewById(R.id.btnStartPauseVideo);
        btnStop = findViewById(R.id.btnStopVideo);
    }

    /*private void startPause() {
        if (videoView.isPlaying())
        {
            videoView.pause();
            btnStartPause.setImageResource(R.drawable.ic_audio_play_btn);
        }
        else
        {
            videoView.start();
            btnStartPause.setImageResource(R.drawable.ic_audio_pause_btn);
        }
    }*/

    private void stopPlay(){
        videoView.stopPlayback();
        videoView.resume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) {
            videoView.setVideoURI(data.getData());
            videoView.start();
            videoView.setOnCompletionListener ( new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                        stopPlay();
                }
            });
        }
    }
}