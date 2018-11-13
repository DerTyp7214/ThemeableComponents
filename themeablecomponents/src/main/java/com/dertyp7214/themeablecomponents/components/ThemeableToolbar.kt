@file:Suppress("UNCHECKED_CAST")

package com.dertyp7214.themeablecomponents.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.ColorUtils
import com.dertyp7214.themeablecomponents.helpers.Utils.getIdFromView
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager
import java.util.*

open class ThemeableToolbar : Toolbar {

    internal val context: Context
    lateinit var onThemeChangeListener: OnThemeChangeListener

    constructor(context: Context) : super(context) {
        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.context = context
        init()
    }

    private fun init() {
        val themeManager = ThemeManager.getInstance(context)
        onThemeChangeListener = object : OnThemeChangeListener {
            override val type: ThemeManager.Component.TYPE
                get() = ThemeManager.Component.TYPE.TOOLBAR
            override val id: String
                get() = getIdFromView(this@ThemeableToolbar)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme, animated)
            }

            override fun accent(): Boolean {
                return false
            }
        }
        themeManager.register(onThemeChangeListener)
    }

    private fun applyTheme(theme: Theme, animated: Boolean) = if (animated) {
        try {
            val animator = ValueAnimator
                    .ofObject(ArgbEvaluator(),
                            backgroundTintList!!.defaultColor,
                            theme.color)
            animator.duration = ThemeManager.duration.toLong()
            animator.addUpdateListener {
                val color = it.animatedValue as Int
                setToolbarColor(color)
            }
            animator.start()
        } catch (ignored: Exception) {
            setToolbarColor(theme.color)
        }

    } else {
        setToolbarColor(theme.color)
    }

    private fun setToolbarColor(@ColorInt toolbarColor: Int) {
        val tintColor = if (isDark(toolbarColor)) Color.WHITE else Color.BLACK
        backgroundTintList = ColorStateList.valueOf(toolbarColor)
        for (imageButton in findChildrenByClass(ImageView::class.java, this)) {
            val drawable = imageButton.drawable
            drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
            imageButton.setImageDrawable(drawable)
        }
        for (textView in findChildrenByClass(TextView::class.java, this)) {
            textView.setTextColor(tintColor)
            textView.setHintTextColor(tintColor)
        }
    }

    private fun <V : View> findChildrenByClass(clazz: Class<V>, vararg viewGroups: ViewGroup): Collection<V> {
        val collection = ArrayList<V>()
        for (viewGroup in viewGroups)
            collection.addAll(gatherChildrenByClass(viewGroup, clazz, ArrayList()))
        return collection
    }

    private fun <V : View> gatherChildrenByClass(viewGroup: ViewGroup, clazz: Class<V>, childrenFound: MutableCollection<V>): Collection<V> {

        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (clazz.isAssignableFrom(child.javaClass)) {
                childrenFound.add(child as V)
            }
            if (child is ViewGroup) {
                gatherChildrenByClass(child, clazz, childrenFound)
            }
        }

        return childrenFound
    }

    private fun isDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }
}
