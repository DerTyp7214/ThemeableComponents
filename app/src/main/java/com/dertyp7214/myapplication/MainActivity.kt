package com.dertyp7214.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.dertyp7214.themeablecomponents.screens.ThemeableActivity

class MainActivity : ThemeableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        themeManager.enableStatusAndNavBar(this)

        findViewById<CheckBox>(R.id.checkBox).setOnClickListener {
            if (it is CheckBox && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!it.isChecked)
                    themeManager.stopThemeService(this)
                else
                    themeManager.startThemeService(this)
            }
        }
        findViewById<View>(R.id.button).setOnClickListener { themeManager.openThemeBottomSheet(this) }
        findViewById<View>(R.id.themeableView).setOnClickListener {
            themeManager.darkMode = !themeManager.darkMode
            themeManager.reload(this)
        }
        findViewById<View>(R.id.themeableFloatingActionButton).setOnClickListener { startActivity(Intent(this, Activity::class.java)) }
    }
}
