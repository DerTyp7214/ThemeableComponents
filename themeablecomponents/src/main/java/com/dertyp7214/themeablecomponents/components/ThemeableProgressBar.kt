package com.dertyp7214.themeablecomponents.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.ProgressBar
import com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager

open class ThemeableProgressBar : ProgressBar {

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
                get() = ThemeManager.Component.TYPE.PROGRESSBAR
            override val id: String
                get() = getIdFromView(this@ThemeableProgressBar)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme, animated)
            }

            override fun accent(): Boolean {
                return true
            }
        }
        themeManager.register(onThemeChangeListener)
    }

    fun applyTheme(theme: Theme, animated: Boolean) = if (animated) {
        val animator = ValueAnimator
                .ofObject(ArgbEvaluator(),
                        progressTintList!!.defaultColor,
                        theme.color)
        animator.duration = ThemeManager.duration.toLong()
        animator.addUpdateListener {
            val color = it.animatedValue as Int
            progressTintList = ColorStateList.valueOf(color)
            secondaryProgressTintList = ColorStateList.valueOf(color)
            indeterminateTintList = ColorStateList.valueOf(color)
        }
        animator.start()
    } else {
        progressTintList = ColorStateList.valueOf(theme.color)
        secondaryProgressTintList = ColorStateList.valueOf(theme.color)
        indeterminateTintList = ColorStateList.valueOf(theme.color)
    }
}
