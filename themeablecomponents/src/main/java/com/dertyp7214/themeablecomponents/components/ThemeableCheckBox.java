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

import java.util.Objects;

import androidx.appcompat.widget.AppCompatCheckBox;

public class ThemeableCheckBox extends AppCompatCheckBox {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;

    public ThemeableCheckBox(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeableCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
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
                return ThemeManager.Component.TYPE.CHECKBOX;
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
                            Objects.requireNonNull(getButtonTintList()).getDefaultColor(),
                            theme.getColor());
            animator.setDuration(ThemeManager.getDuration());
            animator.addUpdateListener(animation -> {
                int color = (int) animation.getAnimatedValue();
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{- android.R.attr.state_checked},
                                new int[]{android.R.attr.state_checked},
                                new int[]{}
                        },
                        new int[]{
                                Color.GRAY,
                                color,
                                Color.LTGRAY
                        }
                );
                setButtonTintList(colorStateList);
            });
            animator.start();
        } else {
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{- android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Color.GRAY,
                            theme.getColor(),
                            Color.LTGRAY
                    }
            );
            setButtonTintList(colorStateList);
        }
    }
}
