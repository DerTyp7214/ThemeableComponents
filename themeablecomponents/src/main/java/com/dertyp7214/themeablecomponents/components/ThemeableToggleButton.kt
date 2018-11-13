package com.dertyp7214.themeablecomponents.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ToggleButton
import androidx.core.graphics.ColorUtils
import com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager

open class ThemeableToggleButton : ToggleButton {

    internal val context: Context
    lateinit var onThemeChangeListener: OnThemeChangeListener

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.context = context
        init()
    }

    constructor(context: Context) : super(context) {
        this.context = context
        init()
    }

    private fun init() {
        val themeManager = ThemeManager.getInstance(context)
        onThemeChangeListener = object : OnThemeChangeListener {
            override val type: ThemeManager.Component.TYPE
                get() = ThemeManager.Component.TYPE.TOGGLEBUTTON
            override val id: String
                get() = getIdFromView(this@ThemeableToggleButton)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme, animated)
            }

            override fun accent(): Boolean {
                return true
            }
        }
        themeManager.register(onThemeChangeListener)
    }

    private fun applyTheme(theme: Theme, animated: Boolean) = if (animated) {
        val animator = ValueAnimator
                .ofObject(ArgbEvaluator(),
                        backgroundTintList!!.defaultColor,
                        theme.color)
        animator.duration = ThemeManager.duration.toLong()
        animator.addUpdateListener {
            val color = it.animatedValue as Int
            setTextColor(if (isDark(color) && isChecked) Color.WHITE else Color.BLACK)
            val colorStateList = ColorStateList(
                    arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked), intArrayOf()),
                    intArrayOf(Color.LTGRAY, color, Color.LTGRAY)
            )
            backgroundTintList = colorStateList
        }
        animator.start()
    } else {
        setTextColor(if (isDark(theme.color) && isChecked) Color.WHITE else Color.BLACK)
        val colorStateList = ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked), intArrayOf()),
                intArrayOf(Color.LTGRAY, theme.color, Color.LTGRAY)
        )
        backgroundTintList = colorStateList
    }

    private fun isDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }
}
