package com.dertyp7214.themeablecomponents.controller

import android.annotation.SuppressLint
import android.app.Application

import com.dertyp7214.themeablecomponents.utils.ThemeManager

open class AppController : Application() {

    protected lateinit var themeManager: ThemeManager

    @SuppressLint("MissingSuperCall")
    override fun onCreate() {
        super.onCreate()
        onCreate(true)
    }

    fun onCreate(style: Boolean = true) {
        themeManager = ThemeManager.getInstance(this)
        themeManager.registerApplication(this, style)
    }
}
