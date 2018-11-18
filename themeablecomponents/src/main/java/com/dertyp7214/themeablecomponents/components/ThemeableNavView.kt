package com.dertyp7214.themeablecomponents.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import androidx.core.graphics.drawable.DrawableCompat
import com.dertyp7214.themeablecomponents.helpers.Utils
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class ThemeableNavView : BottomNavigationView {

    internal val context: Context
    lateinit var onThemeChangeListener: OnThemeChangeListener
    lateinit var theme: Theme

    constructor(context: Context) : super(context) {
        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.context = context
        init()
    }

    private fun init() {
        setBackgroundColor(Color.TRANSPARENT)
        val themeManager = ThemeManager.getInstance(context)
        onThemeChangeListener = object : OnThemeChangeListener {
            override val type: ThemeManager.Component.TYPE
                get() = ThemeManager.Component.TYPE.NAVVIEW
            override val id: String
                get() = Utils.getIdFromView(this@ThemeableNavView)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme, animated)
            }

            override fun accent(): Boolean {
                return true
            }
        }
        themeManager.register(onThemeChangeListener)
        setOnNavigationItemSelectedListener {
            DrawableCompat.setTint(it.icon, theme.color)
            true
        }
    }

    private fun applyTheme(theme: Theme, animated: Boolean) {
        this.theme = theme
        DrawableCompat.setTint(getSelectedItem()?.icon!!, theme.color)
        Log.d("COLOR", String.format("#%06X", 0xFFFFFF and theme.color))
    }

    private fun getSelectedItem(): MenuItem? {
        val menu = menu
        for (i in 0 until menu.size()) {
            if (menu.getItem(i).isChecked) {
                return menu.getItem(i)
            }
        }
        return null
    }
}
