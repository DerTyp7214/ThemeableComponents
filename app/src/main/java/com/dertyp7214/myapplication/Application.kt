package com.dertyp7214.myapplication

import com.dertyp7214.themeablecomponents.utils.ThemeManager

class Application : android.app.Application(){

    private lateinit var themeManager: ThemeManager

    fun getThemeManager(): ThemeManager {
        return themeManager
    }

    companion object {
        private var instance: Application? = null

        fun getInstance(): Application {
            if (instance == null)
                instance = Application()
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        themeManager = ThemeManager.getInstance(this)
        themeManager.registerApplication(this)
    }
}
