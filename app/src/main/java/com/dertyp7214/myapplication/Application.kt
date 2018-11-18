package com.dertyp7214.myapplication

import com.dertyp7214.themeablecomponents.controller.AppController
import com.dertyp7214.themeablecomponents.utils.ThemeManager

class Application : AppController(){

    companion object {
        private var instance: Application? = null

        fun getInstance(): Application {
            if (instance == null)
                instance = Application()
            return instance!!
        }
    }

    fun getManager(): ThemeManager {
        return ThemeManager.getInstance(this)
    }
}
