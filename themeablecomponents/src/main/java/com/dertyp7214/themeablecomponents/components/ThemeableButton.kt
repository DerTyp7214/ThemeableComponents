package com.dertyp7214.themeablecomponents.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import androidx.core.graphics.ColorUtils
import com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager
import java.util.*

class ThemeableButton : com.google.android.material.button.MaterialButton {

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
                get() = ThemeManager.Component.TYPE.BUTTON
            override val id: String
                get() = getIdFromView(this@ThemeableButton)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme, animated)
            }

            override fun accent(): Boolean {
                return true
            }
        }
        themeManager.register(onThemeChangeListener)
    }

    private fun applyTheme(theme: Theme, animated: Boolean) {
        if (animated) {
            val animator = ValueAnimator
                    .ofObject(ArgbEvaluator(), Objects.requireNonNull<ColorStateList>(backgroundTintList).defaultColor,
                            theme.color)
            animator.duration = ThemeManager.duration.toLong()
            animator.addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                backgroundTintList = ColorStateList.valueOf(color)
                setTextColor(if (isDark(color)) Color.WHITE else Color.BLACK)
            }
            animator.start()
        } else {
            backgroundTintList = ColorStateList.valueOf(theme.color)
            setTextColor(if (isDark(theme.color)) Color.WHITE else Color.BLACK)
            Log.d("DARK", isDark(theme.color).toString() + "")
        }
    }

    private fun isDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }
}
