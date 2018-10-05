package com.dertyp7214.themeablecomponents.components;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import java.util.Objects;

import androidx.core.graphics.ColorUtils;

public class ThemeableToggleButton extends ToggleButton {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;

    public ThemeableToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public ThemeableToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableToggleButton(Context context) {
        super(context);
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
                return ThemeManager.Component.TYPE.TOGGLEBUTTON;
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
                setTextColor(isDark(color) && isChecked() ? Color.WHITE : Color.BLACK);
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{- android.R.attr.state_checked},
                                new int[]{android.R.attr.state_checked},
                                new int[]{}
                        },
                        new int[]{
                                Color.LTGRAY,
                                color,
                                Color.LTGRAY
                        }
                );
                setBackgroundTintList(colorStateList);
            });
            animator.start();
        } else {
            setTextColor(isDark(theme.getColor()) && isChecked() ? Color.WHITE : Color.BLACK);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{- android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Color.LTGRAY,
                            theme.getColor(),
                            Color.LTGRAY
                    }
            );
            setBackgroundTintList(colorStateList);
        }
    }

    boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }
}
