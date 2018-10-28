package com.dertyp7214.themeablecomponents.components;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import java.util.Objects;

import androidx.core.graphics.ColorUtils;

import static com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView;

public class ThemeableButton extends com.google.android.material.button.MaterialButton {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;

    public ThemeableButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
                return ThemeManager.Component.TYPE.BUTTON;
            }

            @Override
            public boolean accent() {
                return true;
            }

            @Override
            public String getId() {
                return getIdFromView(ThemeableButton.this);
            }
        };
        themeManager.register(onThemeChangeListener);
    }

    private void applyTheme(Theme theme, boolean animated) {
        if (animated) {
            ValueAnimator animator = ValueAnimator
                    .ofObject(new ArgbEvaluator(), Objects.requireNonNull(getBackgroundTintList()).getDefaultColor(),
                            theme.getColor());
            animator.setDuration(ThemeManager.getDuration());
            animator.addUpdateListener(animation -> {
                int color = (int) animation.getAnimatedValue();
                setBackgroundTintList(ColorStateList.valueOf(color));
                setTextColor(isDark(color) ? Color.WHITE : Color.BLACK);
            });
            animator.start();
        } else {
            setBackgroundTintList(ColorStateList.valueOf(theme.getColor()));
            setTextColor(isDark(theme.getColor()) ? Color.WHITE : Color.BLACK);
            Log.d("DARK", isDark(theme.getColor())+"");
        }
    }

    boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }
}
