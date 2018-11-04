package com.dertyp7214.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val themeManager = Application.getInstance().getThemeManager()
        themeManager.enableStatusAndNavBar(this)

        findViewById<View>(R.id.button).setOnClickListener { themeManager.openThemeBottomSheet(this) }
        findViewById<View>(R.id.themeableView).setOnClickListener {
            themeManager.darkMode = !themeManager.darkMode
            themeManager.reload(this)
        }
        findViewById<View>(R.id.themeableFloatingActionButton).setOnClickListener { startActivity(Intent(this, Activity::class.java)) }
    }
}