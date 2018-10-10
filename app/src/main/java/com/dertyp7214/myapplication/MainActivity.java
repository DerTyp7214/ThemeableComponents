package com.dertyp7214.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import com.dertyp7214.themeablecomponents.colorpicker.ColorPicker;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @ColorInt
    private int color = Color.rgb(140, 240, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_main);

        ThemeManager themeManager = ThemeManager.getInstance(this);
        themeManager.changeAccentColor(color);
        themeManager.changePrimaryColor(this, Color.WHITE, true, true, true);

        findViewById(R.id.button).setOnClickListener(v -> {
            ColorPicker colorPicker = new ColorPicker(this);
            colorPicker.setColor(themeManager.getColorAccent());
            colorPicker.setAnimationTime(0);
            colorPicker.setListener(new ColorPicker.Listener() {
                @Override
                public void color(int color) {

                }

                @Override
                public void update(int color) {
                    themeManager.changeAccentColor(color);
                }

                @Override
                public void cancel() {

                }
            });
            colorPicker.show();
            colorPicker.setDarkMode(true);
        });

        findViewById(R.id.toolbar).setOnClickListener(v -> {
            ColorPicker colorPicker = new ColorPicker(this);
            colorPicker.setColor(themeManager.getColorPrimary());
            colorPicker.setAnimationTime(0);
            colorPicker.setListener(new ColorPicker.Listener() {
                @Override
                public void color(int color) {

                }

                @Override
                public void update(int color) {
                    themeManager
                            .changePrimaryColor(MainActivity.this, color, true, true, false);
                }

                @Override
                public void cancel() {

                }
            });
            colorPicker.show();
            colorPicker.setDarkMode(true);
        });
    }

    @ColorInt
    private int changeHue(@ColorInt int color, int degree) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[0] += degree;
        while (hsv[0] > 360) hsv[0] -= 360;
        return Color.HSVToColor(hsv);
    }
}
