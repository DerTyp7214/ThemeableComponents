package com.dertyp7214.themeablecomponents.components;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener;
import com.dertyp7214.themeablecomponents.utils.Theme;
import com.dertyp7214.themeablecomponents.utils.ThemeManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;

public class ThemeableToolbar extends Toolbar {

    private final Context context;
    public OnThemeChangeListener onThemeChangeListener;

    public ThemeableToolbar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ThemeableToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ThemeableToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                return ThemeManager.Component.TYPE.TOOLBAR;
            }

            @Override
            public boolean accent() {
                return false;
            }
        };
        themeManager.register(onThemeChangeListener);
    }

    private void applyTheme(Theme theme, boolean animated) {
        if (animated) {
            try {
                ValueAnimator animator = ValueAnimator
                        .ofObject(new ArgbEvaluator(),
                                Objects.requireNonNull(getBackgroundTintList()).getDefaultColor(),
                                theme.getColor());
                animator.setDuration(ThemeManager.getDuration());
                animator.addUpdateListener(animation -> {
                    int color = (int) animation.getAnimatedValue();
                    setToolbarColor(color);
                });
                animator.start();
            } catch (Exception ignored) {
                setToolbarColor(theme.getColor());
            }
        } else {
            setToolbarColor(theme.getColor());
        }
    }

    private void setToolbarColor(@ColorInt int toolbarColor) {
        int tintColor = isDark(toolbarColor) ? Color.WHITE : Color.BLACK;
        setBackgroundTintList(ColorStateList.valueOf(toolbarColor));
        for (ImageView imageButton : findChildrenByClass(ImageView.class, this)) {
            Drawable drawable = imageButton.getDrawable();
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP);
            imageButton.setImageDrawable(drawable);
        }
        for (TextView textView : findChildrenByClass(TextView.class, this)) {
            textView.setTextColor(tintColor);
            textView.setHintTextColor(tintColor);
        }
    }

    private <V extends View> Collection<V> findChildrenByClass(Class<V> clazz, ViewGroup... viewGroups) {
        Collection<V> collection = new ArrayList<>();
        for (ViewGroup viewGroup : viewGroups)
            collection.addAll(gatherChildrenByClass(viewGroup, clazz, new ArrayList<V>()));
        return collection;
    }

    private <V extends View> Collection<V> gatherChildrenByClass(ViewGroup viewGroup, Class<V> clazz, Collection<V> childrenFound) {

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            final View child = viewGroup.getChildAt(i);
            if (clazz.isAssignableFrom(child.getClass())) {
                childrenFound.add((V) child);
            }
            if (child instanceof ViewGroup) {
                gatherChildrenByClass((ViewGroup) child, clazz, childrenFound);
            }
        }

        return childrenFound;
    }

    boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }
}
