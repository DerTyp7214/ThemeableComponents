package com.dertyp7214.themeablecomponents.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import androidx.core.graphics.ColorUtils;

import static com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView;

public class ThemeableView extends View {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;

    public ThemeableView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                return ThemeManager.Component.TYPE.VIEW;
            }

            @Override
            public boolean accent() {
                return false;
            }

            @Override
            public String getId() {
                return getIdFromView(ThemeableView.this);
            }
        };
        themeManager.register(onThemeChangeListener);
    }

    private void applyTheme(Theme theme, boolean animated) {
        setBackgroundColor(theme.getColor());
    }

    boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }
}
