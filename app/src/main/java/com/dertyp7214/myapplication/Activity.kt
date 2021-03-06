package com.dertyp7214.myapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.dertyp7214.themeablecomponents.components.ThemeableToggleButton

class Activity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)

        val themeManager = Application.getInstance().getManager()
        themeManager.enableStatusAndNavBar(this)

        findViewById<View>(R.id.themeableFloatingActionButton).setOnClickListener { themeManager.openThemeBottomSheet(this) }
        findViewById<View>(R.id.themeableToggleButton).setOnClickListener {
            themeManager.darkMode = !themeManager.darkMode
            themeManager.reload(this)
        }
        findViewById<ThemeableToggleButton>(R.id.themeableToggleButton).isChecked = themeManager.darkMode
    }
}
