package com.dertyp7214.themeablecomponents.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.dertyp7214.themeablecomponents.R
import com.dertyp7214.themeablecomponents.utils.ThemeManager

open class ThemeableActivity : FragmentActivity() {

    protected lateinit var themeManager: ThemeManager
    private lateinit var transitions: Pair<Int, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeManager = ThemeManager.getInstance(this)
        if (!themeManager.isRegistered())
            setTheme(themeManager.getTheme())

        transitions = themeManager.getTransitions()
    }

    override fun startActivity(intent: Intent?) {
        startActivity(intent, null)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
        overridePendingTransition(transitions.first, R.anim.fade_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, transitions.second)
    }
}
