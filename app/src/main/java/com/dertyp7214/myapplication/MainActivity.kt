package com.dertyp7214.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dertyp7214.themeablecomponents.screens.ThemeableActivity

class MainActivity : ThemeableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        themeManager.enableStatusAndNavBar(this)

        findViewById<View>(R.id.button).setOnClickListener { themeManager.openThemeBottomSheet(this) }
        findViewById<View>(R.id.themeableView).setOnClickListener {
            themeManager.darkMode = !themeManager.darkMode
            themeManager.reload(this)
        }
        findViewById<View>(R.id.themeableFloatingActionButton).setOnClickListener { startActivity(Intent(this, Activity::class.java)) }
    }
}
