package com.dertyp7214.themeablecomponents.components;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Switch;

import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import androidx.annotation.ColorInt;

public class ThemeableSwitch extends Switch {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;
    private int color = Color.WHITE;

    public ThemeableSwitch(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeableSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
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
                return ThemeManager.Component.TYPE.SWITCH;
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
                    .ofObject(new ArgbEvaluator(), color,
                            theme.getColor());
            animator.setDuration(ThemeManager.getDuration());
            animator.addUpdateListener(animation -> {
                int color = (int) animation.getAnimatedValue();
                setSwitchColor(color);
            });
            animator.start();
        } else {
            setSwitchColor(theme.getColor());
        }
        color = theme.getColor();
    }

    private void setSwitchColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            getThumbDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            getTrackDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            ColorStateList buttonStates = new ColorStateList(
                    new int[][]{
                            new int[]{- android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Color.LTGRAY,
                            color,
                            Color.GRAY
                    }
            );
            setButtonTintList(buttonStates);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ColorStateList thumbStates = new ColorStateList(
                    new int[][]{
                            new int[]{- android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Color.LTGRAY,
                            color,
                            Color.GRAY
                    }
            );
            setThumbTintList(thumbStates);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ColorStateList trackStates = new ColorStateList(
                        new int[][]{
                                new int[]{- android.R.attr.state_enabled},
                                new int[]{}
                        },
                        new int[]{
                                Color.GRAY,
                                Color.LTGRAY
                        }
                );
                setTrackTintList(trackStates);
                setTrackTintMode(PorterDuff.Mode.OVERLAY);
            }
        }
    }
}
