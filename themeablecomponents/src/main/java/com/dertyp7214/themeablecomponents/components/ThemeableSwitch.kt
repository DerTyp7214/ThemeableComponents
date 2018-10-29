package com.dertyp7214.themeablecomponents.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.widget.Switch
import androidx.annotation.ColorInt
import com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager

class ThemeableSwitch : Switch {

    internal val context: Context
    lateinit var onThemeChangeListener: OnThemeChangeListener
    private var color = Color.WHITE

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
                get() = ThemeManager.Component.TYPE.SWITCH
            override val id: String
                get() = getIdFromView(this@ThemeableSwitch)

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
                    .ofObject(ArgbEvaluator(), color,
                            theme.color)
            animator.duration = ThemeManager.duration.toLong()
            animator.addUpdateListener {
                val color = it.animatedValue as Int
                setSwitchColor(color)
            }
            animator.start()
        } else {
            setSwitchColor(theme.color)
        }
        color = theme.color
    }

    private fun setSwitchColor(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            thumbDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            trackDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            val buttonStates = ColorStateList(
                    arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_checked), intArrayOf()),
                    intArrayOf(Color.LTGRAY, color, Color.GRAY)
            )
            buttonTintList = buttonStates
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val thumbStates = ColorStateList(
                    arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_checked), intArrayOf()),
                    intArrayOf(Color.LTGRAY, color, Color.GRAY)
            )
            thumbTintList = thumbStates
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val trackStates = ColorStateList(
                        arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf()),
                        intArrayOf(Color.GRAY, Color.LTGRAY)
                )
                trackTintList = trackStates
                trackTintMode = PorterDuff.Mode.OVERLAY
            }
        }
    }
}
