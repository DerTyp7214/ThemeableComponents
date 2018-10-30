package com.dertyp7214.myapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.dertyp7214.themeablecomponents.utils.ThemeManager

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val themeManager = ThemeManager.getInstance(this)
        themeManager.enableStatusAndNavBar(this)

        findViewById<View>(R.id.button).setOnClickListener { themeManager.openThemeBottomSheet(this) }
    }
}
