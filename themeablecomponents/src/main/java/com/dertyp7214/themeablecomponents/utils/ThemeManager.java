package com.dertyp7214.themeablecomponents.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;

public class ThemeManager {

    private static ThemeManager instance;
    private static int duration = 300;
    private Context context;
    private int color = Color.WHITE;
    private List<OnThemeChangeListener> listeners = new ArrayList<>();

    private ThemeManager(Context context) {
        this.context = context;
    }

    public static ThemeManager getInstance(Context context) {
        if (instance == null) instance = new ThemeManager(context);
        return instance;
    }

    public static int getDuration() {
        return duration;
    }

    public void setDuration(int dur) {
        duration = dur;
    }

    public void changeTheme(@ColorInt int color) {
        this.color = color;
        for (OnThemeChangeListener listener : listeners)
            listener.onThemeChanged(new Theme(color), false);
    }

    public void changeTheme(@ColorInt int color, boolean animated) {
        this.color = color;
        for (OnThemeChangeListener listener : listeners)
            listener.onThemeChanged(new Theme(color), animated);
    }

    public List<Component> getComponents() {
        List<Component> components = new ArrayList<>();

        for (OnThemeChangeListener listener : listeners)
            components.add(new Component(listener));

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
    }

    @ColorInt
    public int getColor() {
        return this.color;
    }

    private boolean isThemeable(View v) {
        return v instanceof ThemeableButton || v instanceof ThemeableCheckBox
                || v instanceof ThemeableEditText || v instanceof ThemeableFloatingActionButton
                || v instanceof ThemeableProgressBar || v instanceof ThemeableSeekBar
                || v instanceof ThemeableRadioButton || v instanceof ThemeableSwitch
                || v instanceof ThemeableToggleButton
                || v instanceof ThemeableToolbar;
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

    public static class Component {
        private TYPE type;
        private OnThemeChangeListener listener;

        Component(OnThemeChangeListener listener) {
            this.type = listener.getType();
            this.listener = listener;
        }

        public TYPE getType() {
            return type;
        }

        public void changeColor(@ColorInt int color) {
            listener.onThemeChanged(new Theme(color), false);
        }

        public void changeColor(@ColorInt int color, boolean animated) {
            listener.onThemeChanged(new Theme(color), animated);
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
            TOOLBAR
        }
    }
}
