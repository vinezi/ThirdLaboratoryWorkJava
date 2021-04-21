package com.politov.third_laboratory_work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout cameraIntentBtn, videoIntentBtn, audioIntentBtn, textIntentBtn, title;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;
    private boolean RECORD_AUDIO_GRANTED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        // если устройство до API 23, устанавливаем разрешение
        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            RECORD_AUDIO_GRANTED = true;
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        init();
        cameraIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
                finish();
            }
        });
        videoIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
                finish();
            }
        });
        audioIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RECORD_AUDIO_GRANTED){
                    startActivity(new Intent(MainActivity.this, AudioActivity.class));
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, R.string.permission_denied_main, Toast.LENGTH_LONG).show();
                }

            }
        });
        textIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TextActivity.class));
                finish();
            }
        });
        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, R.string.egg, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void init() {
        cameraIntentBtn = findViewById(R.id.leftTop);
        videoIntentBtn = findViewById(R.id.rightTop);
        audioIntentBtn = findViewById(R.id.leftBottom);
        textIntentBtn = findViewById(R.id.rightBottom);
        title = findViewById(R.id.title);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                RECORD_AUDIO_GRANTED = true;
        }
        if(RECORD_AUDIO_GRANTED)
            Toast.makeText(this, R.string.permission_garanted_main, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, R.string.permission_denied_main, Toast.LENGTH_LONG).show();
    }
}