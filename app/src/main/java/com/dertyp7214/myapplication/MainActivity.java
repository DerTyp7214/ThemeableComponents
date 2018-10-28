package com.dertyp7214.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import androidx.annotation.ColorInt;
import androidx.fragment.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

    @ColorInt
    private int color = Color.rgb(140, 240, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ThemeManager themeManager = ThemeManager.getInstance(this);
        themeManager.enableStatusAndNavBar(this);

        findViewById(R.id.button).setOnClickListener(v ->
                themeManager.openThemeBottomSheet(this));
    }
}
