package com.dertyp7214.themeablecomponents.screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class WallpaperScreen extends AppCompatActivity {

    private final int EXTERNAL_STORAGE = 20;

    private boolean permissions = false;
    private boolean blured = false;
    private int fps = 30;
    private Thread blurProcess;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE);
            } else permissions = true;
        }
        while (!permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if (requestCode == EXTERNAL_STORAGE) this.permissions = true;
        }
    }

    protected void setFPS(int fps) {
        this.fps = fps;
    }

    protected void enableBlur(boolean enable) {
        blured = enable;
    }

    protected void startBlur() {
        if (blurProcess == null) {
            blurProcess = new Thread(() -> {
                runOnUiThread(() -> {

                });
                sleep(1000 / fps);
            });
        }
    }

    protected void blur(int count) {

    }

    protected void sleep(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
