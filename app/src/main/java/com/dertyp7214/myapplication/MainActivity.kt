package com.dertyp7214.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.dertyp7214.themeablecomponents.components.ThemeableFloatingActionButtonProgressBar
import com.dertyp7214.themeablecomponents.screens.ThemeableActivity
import kotlin.random.Random

class MainActivity : ThemeableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        themeManager.enableStatusAndNavBar(this)
        val fab = findViewById<ThemeableFloatingActionButtonProgressBar>(R.id.themeableFloatingActionButton)

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

        fab.setOnClickListener { startActivity(Intent(this, Activity::class.java)) }
        fab.isLoading = true
        fab.isIndeterminate = false

        var counter = 0

        Thread {
            while (true) {
                runOnUiThread {
                    if (counter == 0) fab.isLoading = true else if (counter == 100) fab.isLoading = false
                    fab.progress = if (counter == 150) 0 else counter + 1
                    fab.secondaryProgress = if (counter == 150) 0 else fab.secondaryProgress + Random.nextInt(1, 3)
                    if (counter == 150) counter = 0 else counter++
                }
                Thread.sleep(Random.nextLong(10, 100))
            }
        }.start()
    }
}
