package com.dertyp7214.themeablecomponents.utils;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class Theme {

    @ColorInt
    private final int color;

    public Theme(@ColorInt int color){
        this.color=color;
    }

    @ColorInt
    public int getColor() {
        return this.color;
    }

    @ColorInt
    public int getDarkColor() {
        return manipulateColor(this.color, 0.7F);
    }

    @ColorInt
    public int getLightColor() {
        return manipulateColor(this.color, 1.3F);
    }

    @ColorInt
    private int manipulateColor(@ColorInt int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(
                a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255)
        );
    }
}
