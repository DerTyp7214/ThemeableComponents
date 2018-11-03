package com.dertyp7214.themeablecomponents.screens

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.dertyp7214.themeablecomponents.utils.ThemeManager

open class ThemeableActivity : FragmentActivity() {

    protected lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeManager = ThemeManager.getInstance(this)
        if (!themeManager.isRegistered())
            setTheme(themeManager.getTheme())
    }
}
