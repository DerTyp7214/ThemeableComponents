package com.dertyp7214.themeablecomponents.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.graphics.ColorUtils
import com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

open class ThemeableFloatingActionButton : FloatingActionButton {

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
                get() = ThemeManager.Component.TYPE.FAB
            override val id: String
                get() = getIdFromView(this@ThemeableFloatingActionButton)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme, animated)
            }

            override fun accent(): Boolean {
                return true
            }
        }
        themeManager.register(onThemeChangeListener)
    }

    fun applyTheme(theme: Theme, animated: Boolean) {
        if (animated) {
            val animator = ValueAnimator
                    .ofObject(ArgbEvaluator(),
                            Objects.requireNonNull<ColorStateList>(backgroundTintList).defaultColor,
                            theme.color)
            animator.duration = ThemeManager.duration.toLong()
            animator.addUpdateListener { animation ->
                val color = animation.animatedValue as Int
                setColorFilter(if (isDark(color)) Color.WHITE else Color.BLACK)
                backgroundTintList = ColorStateList.valueOf(color)
            }
            animator.start()
        } else {
            setColorFilter(if (isDark(theme.color)) Color.WHITE else Color.BLACK)
            backgroundTintList = ColorStateList.valueOf(theme.color)
        }
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        setColorFilter(if (isDark(backgroundTintList!!.defaultColor)) Color.WHITE else Color.BLACK)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        try {
            setColorFilter(if (isDark(backgroundTintList!!.defaultColor)) Color.WHITE else Color.BLACK)
        } catch (e: Exception) {
        }
    }

    private fun isDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }
}
