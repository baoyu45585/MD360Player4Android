package com.asha.md360player4android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Uri uri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_me);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        try {
            String url= "/storage/emulated/0/DCIM/vr_4k_1.mp4";
            uri = Uri.parse(url);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void vr_ijk_play(View v){
        Intent intent=new Intent(this,IjkVideoPlayerActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    public void ijk_play(View v){
        Intent intent=new Intent(this,IjkPlayerDemoActivity .class);
        intent.setData(uri);
        startActivity(intent);
    }

    public void ijk_vr_play(View v){
        Intent intent=new Intent(this,VideoPlayerActivity .class);
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }






}
