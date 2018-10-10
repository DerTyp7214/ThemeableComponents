package com.dertyp7214.themeablecomponents.components;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import java.util.Objects;

import androidx.appcompat.widget.AppCompatSeekBar;

public class ThemeableSeekBar extends AppCompatSeekBar {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;

    public ThemeableSeekBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeableSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
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
                return ThemeManager.Component.TYPE.SEEKBAR;
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
                            Objects.requireNonNull(getProgressTintList()).getDefaultColor(),
                            theme.getColor());
            animator.setDuration(ThemeManager.getDuration());
            animator.addUpdateListener(animation -> {
                int color = (int) animation.getAnimatedValue();
                setProgressTintList(ColorStateList.valueOf(color));
                setIndeterminateTintList(ColorStateList.valueOf(color));
            });
            animator.start();
        } else {
            setProgressTintList(ColorStateList.valueOf(theme.getColor()));
            setThumbTintList(ColorStateList.valueOf(theme.getColor()));
        }
    }
}
