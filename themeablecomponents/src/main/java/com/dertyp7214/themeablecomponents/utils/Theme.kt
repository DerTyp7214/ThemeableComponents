package com.dertyp7214.themeablecomponents.utils

import android.graphics.Color

import androidx.annotation.ColorInt

class Theme(@param:ColorInt @field:ColorInt
            @get:ColorInt
            val color: Int) {

    val darkColor: Int
        @ColorInt
        get() = manipulateColor(this.color, 0.7f)

    val lightColor: Int
        @ColorInt
        get() = manipulateColor(this.color, 1.3f)

    @ColorInt
    private fun manipulateColor(@ColorInt color: Int, factor: Float): Int {
        val a = Color.alpha(color)
        val r = Math.round(Color.red(color) * factor)
        val g = Math.round(Color.green(color) * factor)
        val b = Math.round(Color.blue(color) * factor)
        return Color.argb(
                a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255)
        )
    }
}
