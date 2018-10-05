package com.dertyp7214.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static boolean darkTheme = false;

    @ColorInt
    private int color = Color.rgb(140, 240, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(darkTheme ? R.style.AppTheme_Dark : R.style.AppTheme);
        setContentView(R.layout.activity_main);

        ToggleButton toggleButton = findViewById(R.id.toggleButton);

        toggleButton.setChecked(darkTheme);
        toggleButton.setOnClickListener(v -> {
            darkTheme = toggleButton.isChecked();
            finish();
            startActivity(getIntent());
        });

        ThemeManager themeManager = ThemeManager.getInstance(this);
        themeManager.changeTheme(color);
    }
}
