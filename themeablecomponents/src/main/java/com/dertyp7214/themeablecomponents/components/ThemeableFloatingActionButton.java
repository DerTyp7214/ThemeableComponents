package com.dertyp7214.themeablecomponents.components;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.core.graphics.ColorUtils;

public class ThemeableFloatingActionButton extends FloatingActionButton {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;

    public ThemeableFloatingActionButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        ThemeManager themeManager = ThemeManager.getInstance(context);
        onThemeChangeListener = new OnThemeChangeListener() {
            @Override
            public void onThemeChanged(Theme theme, boolean animated) {
                applyTheme(theme, animated);
            }

            @Override
            public ThemeManager.Component.TYPE getType() {
                return ThemeManager.Component.TYPE.FAB;
            }

            @Override
            public boolean accent() {
                return true;
            }
        };
        themeManager.register(onThemeChangeListener);
    }

    private void applyTheme(Theme theme, boolean animated) {
        if (animated) {
            ValueAnimator animator = ValueAnimator
                    .ofObject(new ArgbEvaluator(),
                            Objects.requireNonNull(getBackgroundTintList()).getDefaultColor(),
                            theme.getColor());
            animator.setDuration(ThemeManager.getDuration());
            animator.addUpdateListener(animation -> {
                int color = (int) animation.getAnimatedValue();
                setColorFilter(isDark(color) ? Color.WHITE : Color.BLACK);
                setBackgroundTintList(ColorStateList.valueOf(color));
            });
            animator.start();
        } else {
            setColorFilter(isDark(theme.getColor()) ? Color.WHITE : Color.BLACK);
            setBackgroundTintList(ColorStateList.valueOf(theme.getColor()));
        }
    }

    boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }
}
