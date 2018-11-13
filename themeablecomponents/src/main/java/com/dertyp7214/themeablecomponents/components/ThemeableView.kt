package com.dertyp7214.themeablecomponents.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager

open class ThemeableView : View {

    internal val context: Context
    lateinit var onThemeChangeListener: OnThemeChangeListener

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
        val themeManager = ThemeManager.getInstance(context)
        onThemeChangeListener = object : OnThemeChangeListener {
            override val type: ThemeManager.Component.TYPE
                get() = ThemeManager.Component.TYPE.VIEW
            override val id: String
                get() = getIdFromView(this@ThemeableView)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme)
            }

            override fun accent(): Boolean {
                return false
            }
        }
        themeManager.register(onThemeChangeListener)
    }

    private fun applyTheme(theme: Theme) {
        setBackgroundColor(theme.color)
    }
}
