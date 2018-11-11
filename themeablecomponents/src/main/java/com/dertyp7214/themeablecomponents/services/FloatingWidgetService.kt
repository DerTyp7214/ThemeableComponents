@file:Suppress("DEPRECATION")

package com.dertyp7214.themeablecomponents.services

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import androidx.fragment.app.FragmentActivity
import com.dertyp7214.themeablecomponents.R
import com.dertyp7214.themeablecomponents.components.ThemeableFloatingActionButton
import com.dertyp7214.themeablecomponents.utils.ThemeManager
import com.pawegio.kandroid.displayHeight
import com.pawegio.kandroid.displayWidth

class FloatingWidgetService : Service() {

    private lateinit var mWindowManager: WindowManager
    private lateinit var mOverlayView: View

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("RtlHardcoded", "ClickableViewAccessibility", "InflateParams")
    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.LightTheme)

        val activity: Activity? = getActivity()

        if (activity != null && activity is FragmentActivity) {

            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)

            val params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    else WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT)

            params.gravity = Gravity.TOP or Gravity.LEFT
            params.x = 0
            params.y = 250

            mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            mWindowManager.addView(mOverlayView, params)

            val openBottomSheet = mOverlayView.findViewById<ThemeableFloatingActionButton>(R.id.fabHead)
            ThemeManager.getInstance(activity).changeColor(openBottomSheet, resources.getColor(R.color.design_default_color_primary))

            openBottomSheet.setOnTouchListener(object : View.OnTouchListener {
                private var move = false
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0.toFloat()
                private var initialTouchY = 0.toFloat()

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {

                            initialX = params.x
                            initialY = params.y

                            initialTouchX = event.rawX
                            initialTouchY = event.rawY

                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            if (move) {
                                move = false
                                if (params.x - 10 < displayWidth / 2
                                        && params.x + 10 + mOverlayView.width > displayWidth / 2
                                        && params.y > displayHeight - 10
                                        - (mOverlayView.height * 1.5)) {
                                    this@FloatingWidgetService.stopSelf()
                                } else if (params.x != 0 || params.x != displayWidth) {
                                    val animator = ValueAnimator.ofInt(params.x, if (params.x > displayWidth / 2) displayWidth - mOverlayView.width else 0)
                                    animator.duration = 300
                                    animator.start()
                                    animator.addUpdateListener {
                                        params.x = it.animatedValue as Int
                                        mWindowManager.updateViewLayout(mOverlayView, params)
                                    }
                                }
                            } else {
                                val themeManager = ThemeManager.getInstance(activity)
                                if (themeManager.isInBackground) {
                                    /* val intent = Intent(this@FloatingWidgetService, activity.javaClass)
                                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent) */
                                } else {
                                    try {
                                        val fragmentActivity: Activity? = themeManager.activeActivity
                                        if (fragmentActivity is FragmentActivity) {
                                            ThemeManager.getInstance(applicationContext)
                                                    .openThemeBottomSheet(fragmentActivity)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {

                            val xDiff = Math.round(event.rawX - initialTouchX).toFloat()
                            val yDiff = Math.round(event.rawY - initialTouchY).toFloat()

                            if ((xDiff > 1 || xDiff < -1) && (yDiff > 1 || yDiff < -1)) {
                                move = true

                                params.x = initialX + xDiff.toInt()
                                params.y = initialY + yDiff.toInt()

                                if (params.x < 0) params.x = 0
                                if (params.y < 0) params.y = 0
                                if (params.x > displayWidth - mOverlayView.width) params.x = displayWidth - mOverlayView.width
                                if (params.y > displayHeight - mOverlayView.height - (mOverlayView.height / 2))
                                    params.y = displayHeight - mOverlayView.height - (mOverlayView.height / 2)

                                if (params.x - 10 < displayWidth / 2
                                        && params.x + 10 + mOverlayView.width > displayWidth / 2
                                        && params.y > displayHeight - 10
                                        - (mOverlayView.height * 1.5))
                                    ThemeManager.getInstance(this@FloatingWidgetService)
                                            .changeColor(openBottomSheet, Color.RED)
                                else
                                    ThemeManager.getInstance(this@FloatingWidgetService)
                                            .changeColor(openBottomSheet, resources.getColor(R.color.design_default_color_primary))

                                mWindowManager.updateViewLayout(mOverlayView, params)
                            }

                            return true
                        }
                    }
                    return false
                }
            })
        }
    }

    private fun navigationBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    private fun statusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    @SuppressLint("PrivateApi")
    fun getActivity(): Activity? {
        val activityThreadClass = Class.forName("android.app.ActivityThread")
        val activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
        val activitiesField = activityThreadClass.getDeclaredField("mActivities")
        activitiesField.isAccessible = true

        val activities = activitiesField.get(activityThread) as Map<Any, Any>

        for (activityRecord in activities.values) {
            val activityRecordClass = activityRecord.javaClass
            val pausedField = activityRecordClass.getDeclaredField("paused")
            pausedField.isAccessible = true
            if (!pausedField.getBoolean(activityRecord)) {
                val activityField = activityRecordClass.getDeclaredField("activity")
                activityField.isAccessible = true
                return activityField.get(activityRecord) as Activity
            }
            return null
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mWindowManager.removeView(mOverlayView)
        } catch (e: Exception) {
        }
    }
}