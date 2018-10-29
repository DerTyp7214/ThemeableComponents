package com.dertyp7214.themeablecomponents.utils

interface OnThemeChangeListener {
    val type: ThemeManager.Component.TYPE
    val id: String
    fun onThemeChanged(theme: Theme, animated: Boolean)
    fun accent(): Boolean
}
