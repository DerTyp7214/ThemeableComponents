package com.dertyp7214.themeablecomponents.utils;

public interface OnThemeChangeListener {
    void onThemeChanged(Theme theme, boolean animated);
    ThemeManager.Component.TYPE getType();
}
