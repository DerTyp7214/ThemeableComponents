package com.dertyp7214.themeablecomponents.components

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import com.dertyp7214.themeablecomponents.R
import com.dertyp7214.themeablecomponents.helpers.Utils
import com.dertyp7214.themeablecomponents.utils.OnThemeChangeListener
import com.dertyp7214.themeablecomponents.utils.Theme
import com.dertyp7214.themeablecomponents.utils.ThemeManager


open class ThemeableFloatingActionButtonProgressBar : RelativeLayout {

    private lateinit var progressBar: ThemeableProgressBar
    private lateinit var floatingActionButton: ThemeableFloatingActionButton
    lateinit var onThemeChangeListener: OnThemeChangeListener
    internal val context: Context

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

    fun init() {
        inflate(context, R.layout.fab_progress_bar, this)
        progressBar = findViewById(R.id.themeableProgressBar)
        floatingActionButton = findViewById(R.id.fab)

        progressBar.max = 100

        val themeManager = ThemeManager.getInstance(context)
        onThemeChangeListener = object : OnThemeChangeListener {
            override val type: ThemeManager.Component.TYPE
                get() = ThemeManager.Component.TYPE.FAB
            override val id: String
                get() = Utils.getIdFromView(this@ThemeableFloatingActionButtonProgressBar)

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                applyTheme(theme, animated)
            }

            override fun accent(): Boolean {
                return true
            }
        }
        themeManager.register(onThemeChangeListener)
    }

    var progress: Int
        get() {
            return progressBar.progress
        }
        set(value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                progressBar.setProgress(value, true)
            else progressBar.progress = value
        }

    var secondaryProgress: Int
        get() {
            return progressBar.secondaryProgress
        }
        set(value) {
            progressBar.secondaryProgress = value
        }

    var isIndeterminate: Boolean
        get() {
            return progressBar.isIndeterminate
        }
        set(value) {
            progressBar.isIndeterminate = value
        }

    var isLoading: Boolean
        get() {
            return if (isFinished) isFinished else progressBar.visibility == View.VISIBLE
        }
        set(value) {
            if (!isFinished) {
                progressBar.visibility = if (value) View.VISIBLE else View.INVISIBLE
                val downloadToClear: AnimatedVectorDrawable = resources.getDrawable(R.drawable.download_to_clear, null) as AnimatedVectorDrawable
                val clearToDownload: AnimatedVectorDrawable = resources.getDrawable(R.drawable.clear_to_download, null) as AnimatedVectorDrawable
                floatingActionButton.setImageDrawable(if (value) downloadToClear else clearToDownload)
                if (value) {
                    downloadToClear.start()
                    progressBar.progress = 0
                    progressBar.secondaryProgress = 0
                } else
                    clearToDownload.start()
            }
        }

    var isFinished: Boolean = false
        set(value) {
            field = value
            val clearToTick: AnimatedVectorDrawable = resources.getDrawable(R.drawable.clear_to_tick, null) as AnimatedVectorDrawable
            val tickToDownload: AnimatedVectorDrawable = resources.getDrawable(R.drawable.tick_to_download, null) as AnimatedVectorDrawable
            floatingActionButton.setImageDrawable(if (value) clearToTick else tickToDownload)
            if (value) {
                progressBar.visibility = if (value) View.VISIBLE else View.INVISIBLE
                clearToTick.start()
            } else
                floatingActionButton.start()
        }

    var max: Int
        get() {
            return progressBar.max
        }
        set(value) {
            progressBar.max = value
        }

    var min: Int
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            return progressBar.min
        }
        @RequiresApi(Build.VERSION_CODES.O)
        set(value) {
            progressBar.min = value
        }

    override fun setOnClickListener(l: OnClickListener?) {
        floatingActionButton.setOnClickListener(l)
    }

    private fun applyTheme(theme: Theme, animated: Boolean) {
        progressBar.applyTheme(if (isDark(theme.color)) Theme(Color.WHITE) else Theme(Color.BLACK), animated)
        floatingActionButton.applyTheme(theme, animated)
    }

    private fun isDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }
}