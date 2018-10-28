package com.dertyp7214.themeablecomponents.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.dertyp7214.themeablecomponents.BuildConfig;
import com.dertyp7214.themeablecomponents.components.ThemeBottomSheet;
import com.dertyp7214.themeablecomponents.components.ThemeableButton;
import com.dertyp7214.themeablecomponents.components.ThemeableCheckBox;
import com.dertyp7214.themeablecomponents.components.ThemeableEditText;
import com.dertyp7214.themeablecomponents.components.ThemeableFloatingActionButton;
import com.dertyp7214.themeablecomponents.components.ThemeableProgressBar;
import com.dertyp7214.themeablecomponents.components.ThemeableRadioButton;
import com.dertyp7214.themeablecomponents.components.ThemeableSeekBar;
import com.dertyp7214.themeablecomponents.components.ThemeableSwitch;
import com.dertyp7214.themeablecomponents.components.ThemeableToggleButton;
import com.dertyp7214.themeablecomponents.components.ThemeableToolbar;
import com.dertyp7214.themeablecomponents.components.ThemeableView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.FragmentActivity;

public class ThemeManager {

    private static ThemeManager instance;
    private static int duration = 300;
    private static SharedPreferences sharedPreferences;
    private static boolean saveColors;
    private Context context;
    private int colorPrimary = Color.WHITE;
    private int colorAccent = Color.BLACK;
    private List<OnThemeChangeListener> listeners = new ArrayList<>();
    private List<Component> customViews = new ArrayList<>();
    private int defAccent = Color.GRAY, defPrimary = Color.GRAY;

    private ThemeManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + "_theme",
                Context.MODE_PRIVATE);
        saveColors = sharedPreferences.getBoolean("saveColors", true);
    }

    public static ThemeManager getInstance(Context context) {
        if (instance == null) instance = new ThemeManager(context);
        return instance;
    }

    public static void attach(Activity activity, Callback callback) {
        activity.findViewById(android.R.id.content).post(() -> callback.run(getInstance(activity)));
    }

    public static int getDuration() {
        return duration;
    }

    public void setDuration(int dur) {
        duration = dur;
    }

    public void changeAccentColor(@ColorInt int color) {
        changeAccentColor(color, false);
    }

    public void changeAccentColor(@ColorInt int color, boolean animated) {
        this.colorAccent = color;
        for (OnThemeChangeListener listener : listeners)
            if (listener.accent())
                listener.onThemeChanged(new Theme(color), animated);
        for (Component component : customViews)
            if (component.isAccent())
                component.changeColor(color, animated);
    }

    public void changePrimaryColor(@ColorInt int color) {
        changePrimaryColor(color, false);
    }

    public void changePrimaryColor(@ColorInt int color, boolean animated) {
        this.colorPrimary = color;
        for (OnThemeChangeListener listener : listeners)
            if (! listener.accent())
                listener.onThemeChanged(new Theme(color), animated);
        for (Component component : customViews)
            if (! component.isAccent())
                component.changeColor(color, animated);
    }

    public void changePrimaryColor(Activity activity, @ColorInt int color, boolean statusBar, boolean navigationBar) {
        changePrimaryColor(activity, color, statusBar, navigationBar, false);
    }

    public void changePrimaryColor(Activity activity, @ColorInt int color, boolean statusBar, boolean navigationBar, boolean animated) {
        changePrimaryColor(color, animated);
        if (animated) {
            ValueAnimator sAnimator = ValueAnimator
                    .ofObject(new ArgbEvaluator(), activity.getWindow().getStatusBarColor(),
                            new Theme(color).getDarkColor());
            sAnimator.setDuration(duration);
            sAnimator.addUpdateListener(animation -> {
                int c = (Integer) animation.getAnimatedValue();
                if (statusBar) {
                    activity.getWindow().setStatusBarColor(c);
                    if (! isDark(c)
                            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        activity.getWindow().getDecorView()
                                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            });
            sAnimator.start();
            ValueAnimator nAnimator = ValueAnimator
                    .ofObject(new ArgbEvaluator(), activity.getWindow().getStatusBarColor(), color);
            nAnimator.setDuration(duration);
            nAnimator.addUpdateListener(animation -> {
                int c = (Integer) animation.getAnimatedValue();
                if (navigationBar) {
                    activity.getWindow().setNavigationBarColor(c);
                    if (! isDark(c) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        activity.getWindow().getDecorView()
                                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                }
            });
            nAnimator.start();
        } else {
            if (statusBar) {
                activity.getWindow().setStatusBarColor(new Theme(color).getDarkColor());
                if (! isDark(new Theme(color).getDarkColor())
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    activity.getWindow().getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            if (navigationBar) {
                activity.getWindow().setNavigationBarColor(color);
                if (! isDark(color) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    activity.getWindow().getDecorView()
                            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }
    }

    private boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }

    public List<Component> getComponents() {
        List<Component> components = new ArrayList<>();
        for (OnThemeChangeListener listener : listeners)
            components.add(new Component(listener));

        components.addAll(customViews);
        return components;
    }

    public List<Component> getComponents(Activity activity) {
        List<Component> components = new ArrayList<>();

        ViewGroup rootView = activity.findViewById(android.R.id.content);
        for (View v : getAllChildren(rootView)) {
            if (isThemeable(v))
                if (parseComponent(v) != null)
                    components.add(parseComponent(v));
        }

        return components;
    }

    public List<Component> filterComponents(List<Component> c, Component.TYPE type) {
        List<Component> components = new ArrayList<>();

        for (Component component : c)
            if (component != null)
                if (component.getType().equals(type))
                    components.add(component);

        return components;
    }

    public void register(OnThemeChangeListener listener) {
        listeners.add(listener);
        listener.onThemeChanged(getDefaultTheme(listener), false);
    }

    private Theme getDefaultTheme(OnThemeChangeListener listener) {
        if (saveColors) {
            return new Theme(sharedPreferences
                    .getInt(listener.getId(), listener.accent() ? defAccent : defPrimary));
        }
        return new Theme(listener.accent() ? colorAccent : colorPrimary);
    }

    public void register(View view, boolean accent) {
        customViews.add(new Component(view, accent));
    }

    public void setDefaultAccent(@ColorInt int color) {
        defAccent = color;
    }

    public void setDefaultPrimary(@ColorInt int color) {
        defPrimary = color;
    }

    public void enableStatusAndNavBar(Activity activity) {
        OnThemeChangeListener status = new OnThemeChangeListener() {
            @Override
            public void onThemeChanged(Theme theme, boolean animated) {
                changeStatusColor(activity, theme);
            }

            @Override
            public Component.TYPE getType() {
                return Component.TYPE.VIEW;
            }

            @Override
            public boolean accent() {
                return false;
            }

            @Override
            public String getId() {
                return "statusBar";
            }
        };
        OnThemeChangeListener nav = new OnThemeChangeListener() {
            @Override
            public void onThemeChanged(Theme theme, boolean animated) {
                changeNavColor(activity, theme);
            }

            @Override
            public Component.TYPE getType() {
                return Component.TYPE.VIEW;
            }

            @Override
            public boolean accent() {
                return false;
            }

            @Override
            public String getId() {
                return "navigationBar";
            }
        };
        if (! listeners.contains(status))
            listeners.add(status);
        if (! listeners.contains(nav))
            listeners.add(nav);
        changeNavColor(activity, getDefaultTheme(nav));
        changeStatusColor(activity, getDefaultTheme(status));
    }

    private void changeStatusColor(Activity activity, Theme theme) {
        activity.getWindow().setStatusBarColor(theme.getColor());
        int tmp = activity.getWindow().getDecorView().getSystemUiVisibility();
        if (! isDark(theme.getColor()))
            tmp |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        else tmp &= ~ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        activity.getWindow().getDecorView().setSystemUiVisibility(tmp);
    }

    private void changeNavColor(Activity activity, Theme theme) {
        activity.getWindow().setNavigationBarColor(theme.getColor());
        int tmp = activity.getWindow().getDecorView().getSystemUiVisibility();
        if (! isDark(theme.getColor()))
            tmp |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        else tmp &= ~ View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        activity.getWindow().getDecorView().setSystemUiVisibility(tmp);
    }

    public void openThemeBottomSheet(FragmentActivity activity) {
        ThemeBottomSheet bottomSheet = new ThemeBottomSheet(sharedPreferences, listeners);
        assert bottomSheet.getFragmentManager() != null;
        bottomSheet.show(activity.getSupportFragmentManager(), "TAG");
    }

    @ColorInt
    public int getColorAccent() {
        return this.colorAccent;
    }

    public int getColorPrimary() {
        return this.colorPrimary;
    }

    private boolean isThemeable(View v) {
        return v instanceof ThemeableButton || v instanceof ThemeableCheckBox
                || v instanceof ThemeableEditText || v instanceof ThemeableFloatingActionButton
                || v instanceof ThemeableProgressBar || v instanceof ThemeableSeekBar
                || v instanceof ThemeableRadioButton || v instanceof ThemeableSwitch
                || v instanceof ThemeableToggleButton || v instanceof ThemeableToolbar
                || v instanceof ThemeableView;
    }

    private Component parseComponent(View v) {
        if (v instanceof ThemeableButton)
            return new Component(((ThemeableButton) v).onThemeChangeListener);
        if (v instanceof ThemeableFloatingActionButton)
            return new Component(((ThemeableFloatingActionButton) v).onThemeChangeListener);
        if (v instanceof ThemeableProgressBar)
            return new Component(((ThemeableProgressBar) v).onThemeChangeListener);
        if (v instanceof ThemeableSeekBar)
            return new Component(((ThemeableSeekBar) v).onThemeChangeListener);
        if (v instanceof ThemeableEditText)
            return new Component(((ThemeableEditText) v).onThemeChangeListener);
        if (v instanceof ThemeableRadioButton)
            return new Component(((ThemeableRadioButton) v).onThemeChangeListener);
        if (v instanceof ThemeableSwitch)
            return new Component(((ThemeableSwitch) v).onThemeChangeListener);
        if (v instanceof ThemeableToggleButton)
            return new Component(((ThemeableToggleButton) v).onThemeChangeListener);
        if (v instanceof ThemeableToolbar)
            return new Component(((ThemeableToolbar) v).onThemeChangeListener);
        if (v instanceof ThemeableView)
            return new Component(((ThemeableView) v).onThemeChangeListener);
        return null;
    }

    private List<View> getAllChildren(View v) {
        if (! (v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            result.addAll(getAllChildren(child));
        }
        return result;
    }

    public interface Callback {
        void run(ThemeManager themeManager);
    }

    public static class Component {
        private View v;
        private TYPE type;
        private boolean isAccent;
        private OnThemeChangeListener listener;

        Component(OnThemeChangeListener listener) {
            this.type = listener.getType();
            this.isAccent = listener.accent();
            this.listener = listener;
        }

        Component(View v, boolean isAccent) {
            this.v = v;
            this.type = TYPE.VIEW;
            this.isAccent = isAccent;
        }

        public TYPE getType() {
            return type;
        }

        public boolean isAccent() {
            return this.isAccent;
        }

        public void changeColor(@ColorInt int color) {
            changeColor(color, false);
        }

        public void changeColor(@ColorInt int color, boolean animated) {
            if (saveColors) sharedPreferences.edit().putInt(listener.getId(), color).apply();
            if (v == null) listener.onThemeChanged(new Theme(color), animated);
            else v.setBackgroundTintList(ColorStateList.valueOf(color));
        }

        public enum TYPE {
            BUTTON,
            CHECKBOX,
            EDITTEXT,
            FAB,
            PROGRESSBAR,
            RADIOBUTTON,
            SEEKBAR,
            SWITCH,
            TOGGLEBUTTON,
            TOOLBAR,
            VIEW
        }
    }
}
