package com.dertyp7214.themeablecomponents.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

open class WallpaperScreen : AppCompatActivity() {

    private val EXTERNAL_STORAGE = 20

    private var permissions = false
    private var blured = false
    private var fps = 30
    private var blurProcess: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), EXTERNAL_STORAGE)
            } else
                permissions = true
        }
        while (!permissions);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == EXTERNAL_STORAGE) this.permissions = true
        }
    }

    protected fun setFPS(fps: Int) {
        this.fps = fps
    }

    protected fun enableBlur(enable: Boolean) {
        blured = enable
    }

    protected fun startBlur() {
        if (blurProcess == null) {
            blurProcess = Thread {
                runOnUiThread {

                }
                sleep((1000 / fps).toLong())
            }
        }
    }

    protected fun blur(count: Int) {

    }

    private fun sleep(time: Long) {
        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
