package com.politov.third_laboratory_work;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import me.bogerchan.niervisualizer.NierVisualizerManager;
import me.bogerchan.niervisualizer.renderer.IRenderer;
import me.bogerchan.niervisualizer.renderer.circle.CircleBarRenderer;
import me.bogerchan.niervisualizer.renderer.line.LineRenderer;

public class AudioActivity extends AppCompatActivity {

    final NierVisualizerManager visualizerManager = new NierVisualizerManager();
    SurfaceView sv_wave;

    ImageButton btnAudio, btnStartPause, btnStop;
    public static int AUDIO_CAPTURED = 1;
    MediaPlayer mPlayer;

    TextView textCurrentTime, textTotalDuration;
    SeekBar playerSeekBar;
    Handler handler = new Handler();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AudioActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        init();
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null)
                {
                    stopPlay();
                    recreate();
                }
                openAudio();
            }
        });

        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPause();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayer != null)
                    stopPlay();
            }
        });

        playerSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (mPlayer != null){
                    SeekBar seekBar = (SeekBar) view;
                    int playPosition = (mPlayer.getDuration() / 100 ) * seekBar.getProgress();
                    mPlayer.seekTo(playPosition);
                    textCurrentTime.setText(milliSecondsToTimer(mPlayer.getCurrentPosition()));
                }
                return false;
            }
        });
    }

    private void init() {
        /////BUTTONS/////
        btnAudio = findViewById(R.id.btnAudioIntent);
        btnStartPause = findViewById(R.id.btnStartPauseAudio);
        btnStop = findViewById(R.id.btnStopAudio);
        /////PROGRESS BAR/////
        textCurrentTime = findViewById(R.id.textCurrentTime);
        textTotalDuration = findViewById(R.id.textTotalDuration);
        playerSeekBar = findViewById(R.id.playerSeekBar);
        playerSeekBar.setMax(100);
        /////VISUALIZATION/////
        sv_wave = findViewById(R.id.sv_wave);
    }

    ////////////////////////////PROGRESS BAR////////////////////////////
    private final Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mPlayer.getCurrentPosition();
            textCurrentTime.setText(milliSecondsToTimer(currentDuration));
        }
    };
    private  void  updateSeekBar(){
        if (mPlayer.isPlaying()){
            playerSeekBar.setProgress((int) (((float) mPlayer.getCurrentPosition() / mPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }
    private String milliSecondsToTimer(long milliSeconds) {
        String timerString = "";
        String secondsString;
        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0)
            timerString = hours + ":";
        if (seconds < 10){
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondsString;
        return timerString;
    }

    ////////////////////////////PLAYER BUTTONS FUNCTION////////////////////////////
    private void openAudio() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, AUDIO_CAPTURED);
    }
    private void startPause() {
        if (mPlayer != null)
        {
            if (mPlayer.isPlaying())
            {
                handler.removeCallbacks(updater);
                mPlayer.pause();
                btnStartPause.setImageResource(R.drawable.ic_audio_play_btn);
                visualizerManager.pause();
            }
            else
            {
                mPlayer.start();
                btnStartPause.setImageResource(R.drawable.ic_audio_pause_btn);
                updateSeekBar();
                visualizerManager.resume();
            }
        }
    }
    private void stopPlay(){
        btnStartPause.setImageResource(R.drawable.ic_audio_play_btn);
        playerSeekBar.setProgress(0);
        textTotalDuration.setText(R.string.zeroTime);
        textCurrentTime.setText(R.string.zeroTime);
        if (mPlayer != null){
            mPlayer.stop();
            visualizerManager.pause();
        }
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
        } catch (Throwable t) {
            Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    ////////////////////////////ACTIVITY RESULT////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AUDIO_CAPTURED) {
            mPlayer= MediaPlayer.create(this, data.getData());
            final int state = visualizerManager.init(mPlayer.getAudioSessionId());
            if (NierVisualizerManager.SUCCESS != state) {
                visualizerManager.release();
            }
            textTotalDuration.setText(milliSecondsToTimer(mPlayer.getDuration()));
            visualizerManager.start(sv_wave, new IRenderer[]{new CircleBarRenderer(), new LineRenderer(true)});
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlay();
                    visualizerManager.stop();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            stopPlay();
        }
    }
}