@file:Suppress("DEPRECATION")

package com.dertyp7214.themeablecomponents.components

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.TextView
import com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager

open class ThemeableEditText : com.google.android.material.textfield.TextInputEditText {

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
                get() = ThemeManager.Component.TYPE.EDITTEXT
            override val id: String
                get() = getIdFromView(this@ThemeableEditText)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme)
            }

            override fun accent(): Boolean {
                return true
            }
        }
        themeManager.register(onThemeChangeListener)
    }

    private fun applyTheme(theme: Theme) {
        clearCursor()
        setTextHandleColor(theme)
        background.mutate().setColorFilter(theme.color, PorterDuff.Mode.SRC_ATOP)
    }

    private fun setTextHandleColor(theme: Theme) {
        try {
            val editorField = TextView::class.java.getDeclaredField("mEditor")
            if (!editorField.isAccessible) {
                editorField.isAccessible = true
            }

            val editor = editorField.get(this)
            val editorClass = editor.javaClass

            val handleNames = arrayOf("mSelectHandleLeft", "mSelectHandleRight", "mSelectHandleCenter")
            val resNames = arrayOf("mTextSelectHandleLeftRes", "mTextSelectHandleRightRes", "mTextSelectHandleRes")

            for (i in handleNames.indices) {
                val handleField = editorClass.getDeclaredField(handleNames[i])
                if (!handleField.isAccessible) {
                    handleField.isAccessible = true
                }

                var handleDrawable: Drawable? = handleField.get(editor) as Drawable

                if (handleDrawable == null) {
                    val resField = TextView::class.java.getDeclaredField(resNames[i])
                    if (!resField.isAccessible) {
                        resField.isAccessible = true
                    }
                    val resId = resField.getInt(this)
                    handleDrawable = resources.getDrawable(resId)
                }

                if (handleDrawable != null) {
                    val drawable = handleDrawable.mutate()
                    drawable.setColorFilter(theme.color, PorterDuff.Mode.SRC_IN)
                    handleField.set(editor, drawable)
                }
            }
            highlightColor = theme.lightColor
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun clearCursor() {
        try {
            val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            f.isAccessible = true
            f.set(this, 0)
        } catch (ignored: Exception) {
        }
    }
}
