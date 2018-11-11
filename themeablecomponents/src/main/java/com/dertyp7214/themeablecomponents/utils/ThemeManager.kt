package com.dertyp7214.themeablecomponents.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AnimRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentActivity
import com.costular.kotlin_utils.sharedprefs.edit
import com.dertyp7214.themeablecomponents.BuildConfig
import com.dertyp7214.themeablecomponents.R
import com.dertyp7214.themeablecomponents.components.*
import com.dertyp7214.themeablecomponents.helpers.Utils
import com.dertyp7214.themeablecomponents.screens.ThemeableActivity
import com.dertyp7214.themeablecomponents.services.FloatingWidgetService


class ThemeManager private constructor(context: Context) {
    private var colorPrimary = Color.WHITE
    @get:ColorInt
    var colorAccent = Color.BLACK
        private set
    private val listeners = ArrayList<OnThemeChangeListener>()
    private val customViews = ArrayList<Component>()
    private var defAccent = Color.GRAY
    private var defPrimary = Color.GRAY
    private var registered: Boolean = false
    private val activityMap: HashMap<Activity, Int> = HashMap()

    var darkMode: Boolean
        get() {
            return sharedPreferences.getBoolean("darkMode", false)
        }
        set(value) {
            sharedPreferences.edit().putBoolean("darkMode", value).apply()
        }

    val components: List<Component>
        get() {
            val components = ArrayList<Component>()
            for (listener in listeners)
                components.add(Component(listener))

            components.addAll(customViews)
            return components
        }

    var activeActivity: Activity? = null
        private set(activity) {
            field = activity
        }

    private var activityCounter: Int = 0
    var isInBackground: Boolean = false
        get() {
            return activityCounter == 0
        }

    init {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + "_theme",
                Context.MODE_PRIVATE)
        saveColors = sharedPreferences.getBoolean("saveColors", true)
    }

    @JvmOverloads
    fun changeAccentColor(@ColorInt color: Int, animated: Boolean = false) {
        this.colorAccent = color
        for (listener in listeners)
            if (listener.accent())
                listener.onThemeChanged(Theme(color), animated)
        for (component in customViews)
            if (component.isAccent)
                component.changeColor(color, animated)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun startThemeService(activity: Activity) {
        askForSystemOverlayPermission(activity)
        if (Settings.canDrawOverlays(activity)) {
            val intent = Intent(activity, FloatingWidgetService::class.java)
            activity.startService(intent)
        }
    }

    fun stopThemeService(activity: Activity) {
        val intent = Intent(activity, FloatingWidgetService::class.java)
        activity.stopService(intent)
    }

    @JvmOverloads
    fun changePrimaryColor(@ColorInt color: Int, animated: Boolean = false) {
        this.colorPrimary = color
        for (listener in listeners)
            if (!listener.accent())
                listener.onThemeChanged(Theme(color), animated)
        for (component in customViews)
            if (!component.isAccent)
                component.changeColor(color, animated)
    }

    @JvmOverloads
    fun changePrimaryColor(activity: Activity, @ColorInt color: Int, statusBar: Boolean, navigationBar: Boolean, animated: Boolean = false) {
        changePrimaryColor(color, animated)
        if (animated) {
            val sAnimator = ValueAnimator
                    .ofObject(ArgbEvaluator(), activity.window.statusBarColor,
                            Theme(color).darkColor)
            sAnimator.duration = duration.toLong()
            sAnimator.addUpdateListener { animation ->
                val c = animation.animatedValue as Int
                if (statusBar) {
                    activity.window.statusBarColor = c
                    if (!isDark(c) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
            sAnimator.start()
            val nAnimator = ValueAnimator
                    .ofObject(ArgbEvaluator(), activity.window.statusBarColor, color)
            nAnimator.duration = duration.toLong()
            nAnimator.addUpdateListener { animation ->
                val c = animation.animatedValue as Int
                if (navigationBar) {
                    activity.window.navigationBarColor = c
                    if (!isDark(c) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
            }
            nAnimator.start()
        } else {
            if (statusBar) {
                activity.window.statusBarColor = Theme(color).darkColor
                if (!isDark(Theme(color).darkColor) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            if (navigationBar) {
                activity.window.navigationBarColor = color
                if (!isDark(color) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }
    }

    private fun isDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }

    fun getComponents(activity: Activity): List<Component> {

        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)

        return getAllChildren(rootView)
                .asSequence()
                .filter { isThemeable(it) && parseComponent(it) != null }
                .map { parseComponent(it)!! }
                .toList()
    }

    fun filterComponents(c: List<Component>, type: Component.TYPE): List<Component> {
        return c.filter { it.type == type }
    }

    fun register(listener: OnThemeChangeListener) {
        listeners.add(listener)
        listener.onThemeChanged(getDefaultTheme(listener), false)
    }

    private fun getDefaultTheme(listener: OnThemeChangeListener): Theme {
        return if (saveColors) {
            Theme(sharedPreferences
                    .getInt(listener.id, if (listener.accent()) defAccent else defPrimary))
        } else Theme(if (listener.accent()) colorAccent else colorPrimary)
    }

    private fun askForSystemOverlayPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.packageName))
            activity.startActivityForResult(intent, 123)
        }
    }

    fun register(view: View, accent: Boolean) {
        customViews.add(Component(view, accent))
    }

    fun setDefaultAccent(@ColorInt color: Int) {
        defAccent = color
    }

    fun setDefaultPrimary(@ColorInt color: Int) {
        defPrimary = color
    }

    fun enableStatusAndNavBar(activity: Activity) {
        val status = object : OnThemeChangeListenerStatusNav {
            override val type: Component.TYPE
                get() = Component.TYPE.VIEW
            override val id: String
                get() = "statusBar"
            override val idTag: Activity
                get() = activity

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                changeStatusColor(activity, theme)
            }

            override fun accent(): Boolean {
                return false
            }
        }
        val nav = object : OnThemeChangeListenerStatusNav {
            override val type: Component.TYPE
                get() = Component.TYPE.VIEW
            override val id: String
                get() = "navigationBar"
            override val idTag: Activity
                get() = activity

            override fun onThemeChanged(theme: Theme, animated: Boolean) {
                changeNavColor(activity, theme)
            }

            override fun accent(): Boolean {
                return false
            }
        }
        if (!listeners.contains(status))
            listeners.add(status)
        if (!listeners.contains(nav))
            listeners.add(nav)
        changeNavColor(activity, getDefaultTheme(nav))
        changeStatusColor(activity, getDefaultTheme(status))
    }

    private fun changeStatusColor(activity: Activity, theme: Theme) {
        activity.window.statusBarColor = theme.color
        var tmp = activity.window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tmp = if (!isDark(theme.color))
                tmp or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            else
                tmp and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        activity.window.decorView.systemUiVisibility = tmp
    }

    private fun changeNavColor(activity: Activity, theme: Theme) {
        activity.window.navigationBarColor = theme.color
        var tmp = activity.window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tmp = if (!isDark(theme.color))
                tmp or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            else
                tmp and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
        activity.window.decorView.systemUiVisibility = tmp
    }

    fun openThemeBottomSheet(activity: FragmentActivity) {
        val l = ArrayList<OnThemeChangeListener>()
        l.clear()
        for (listener in listeners) {
            if (listener is OnThemeChangeListenerStatusNav && listener.idTag == activity) l.add(listener)
            else
                for (component in getComponents(activity)) {
                    if (listener.id == component.getId() && component.hasListener() && !includesId(l, listener.id))
                        l.add(component.listener!!)
                }
        }
        val bottomSheet = ThemeBottomSheet(sharedPreferences, l)
        bottomSheet.show(activity.supportFragmentManager, "TAG")
    }

    private fun includesId(list: List<OnThemeChangeListener>, id: String): Boolean {
        for (listener in list) if (listener.id == id) return true
        return false
    }

    private fun isThemeable(v: View): Boolean {
        return (v is ThemeableButton || v is ThemeableCheckBox
                || v is ThemeableEditText || v is ThemeableFloatingActionButton
                || v is ThemeableProgressBar || v is ThemeableSeekBar
                || v is ThemeableRadioButton || v is ThemeableSwitch
                || v is ThemeableToggleButton || v is ThemeableToolbar
                || v is ThemeableView)
    }

    private fun parseComponent(v: View): Component? {
        return when (v) {
            is ThemeableButton -> Component(v.onThemeChangeListener)
            is ThemeableFloatingActionButton -> Component(v.onThemeChangeListener)
            is ThemeableProgressBar -> Component(v.onThemeChangeListener)
            is ThemeableSeekBar -> Component(v.onThemeChangeListener)
            is ThemeableEditText -> Component(v.onThemeChangeListener)
            is ThemeableRadioButton -> Component(v.onThemeChangeListener)
            is ThemeableCheckBox -> Component(v.onThemeChangeListener)
            is ThemeableSwitch -> Component(v.onThemeChangeListener)
            is ThemeableToggleButton -> Component(v.onThemeChangeListener)
            is ThemeableToolbar -> Component(v.onThemeChangeListener)
            is ThemeableView -> Component(v.onThemeChangeListener)
            else -> null
        }
    }

    private fun getAllChildren(v: View): List<View> {
        if (v !is ViewGroup || v is ThemeableToolbar) {
            val viewArrayList = ArrayList<View>()
            viewArrayList.add(v)
            return viewArrayList
        }

        val result = ArrayList<View>()

        for (i in 0 until v.childCount) {
            val child = v.getChildAt(i)
            result.addAll(getAllChildren(child))
        }
        return result
    }

    @StyleRes
    fun getTheme(): Int {
        return when {
            sharedPreferences.contains("customTheme") -> sharedPreferences.getInt("customTheme", R.style.LightTheme)
            darkMode -> R.style.DarkTheme
            else -> R.style.LightTheme
        }
    }

    fun setCustomTheme(@StyleRes styleId: Int?) {
        if (styleId == null) {
            sharedPreferences.edit {
                remove("customTheme")
            }
        } else {
            sharedPreferences.edit {
                putInt("customTheme", styleId)
            }
        }
    }

    fun setCustomTransitions(@AnimRes enterAnim: Int?, @AnimRes exitAnim: Int?) {
        sharedPreferences.edit {
            if (enterAnim == null && sharedPreferences.contains("enterAnim"))
                remove("enterAnim")
            else
                putInt("enterAnim", enterAnim!!)
            if (enterAnim == null && sharedPreferences.contains("exitAnim"))
                remove("exitAnim")
            else
                putInt("exitAnim", exitAnim!!)
        }
    }

    fun getTransitions(): Pair<Int, Int> {
        return Pair(sharedPreferences.getInt("enterAnim", R.anim.fly_in), sharedPreferences.getInt("exitAnim", R.anim.fly_out))
    }

    fun reload(activity: Activity) {
        if (activity is ThemeableActivity)
            activity.recreate()
        else {
            val intent = activity.intent
            activity.finish()
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            activity.startActivity(intent)
        }
    }

    fun isRegistered(): Boolean {
        return registered
    }

    fun registerApplication(application: Application) {
        registered = true
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
                activeActivity = null
            }

            override fun onActivityResumed(activity: Activity?) {
                activeActivity = activity
                var styleId = getTheme()
                if (activity is ThemeableActivity) {
                    styleId = when {
                        getTheme() == R.style.DarkTheme -> R.style.DarkTheme_Animated
                        getTheme() == R.style.LightTheme -> R.style.LightTheme_Animated
                        else -> getTheme()
                    }
                }
                if (activityMap.containsKey(activity) && activityMap[activity] != styleId) {
                    reload(activity!!)
                    activityMap[activity] = styleId
                }
            }

            override fun onActivityStarted(activity: Activity?) {
                activityCounter++
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
                activityCounter--
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activeActivity = activity
                var styleId = getTheme()
                if (activity is ThemeableActivity) {
                    styleId = when {
                        getTheme() == R.style.DarkTheme -> R.style.DarkTheme_Animated
                        getTheme() == R.style.LightTheme -> R.style.LightTheme_Animated
                        else -> getTheme()
                    }
                }
                activity!!.setTheme(styleId)
                activityMap[activity] = styleId
            }
        })
    }

    fun changeColor(v: View, color: Int) {
        if (isThemeable(v)) {
            parseComponent(v)!!.changeColor(color, false)
        }
    }

    interface Callback {
        fun run(themeManager: ThemeManager)
    }

    class Component {
        private var v: View? = null
        var type: TYPE? = null
            private set
        var isAccent: Boolean = false
            private set
        internal var listener: OnThemeChangeListener? = null

        internal constructor(listener: OnThemeChangeListener) {
            this.type = listener.type
            this.isAccent = listener.accent()
            this.listener = listener
        }

        internal constructor(v: View, isAccent: Boolean) {
            this.v = v
            this.type = TYPE.VIEW
            this.isAccent = isAccent
        }

        fun hasListener(): Boolean {
            return listener != null
        }

        fun getId(): String {
            if (v != null) return Utils.getIdFromView(v!!)
            return listener!!.id
        }

        @JvmOverloads
        fun changeColor(@ColorInt color: Int, animated: Boolean = false) {
            if (saveColors) sharedPreferences.edit().putInt(listener!!.id, color).apply()
            if (v == null)
                listener!!.onThemeChanged(Theme(color), animated)
            else
                v!!.backgroundTintList = ColorStateList.valueOf(color)
        }

        enum class TYPE {
            BUTTON,
            CHECKBOX,
            EDITTEXT,
            FAB,
            PROGRESSBAR,
            RADIOBUTTON,
            SEEKBAR,
            SWITCH,
            TOGGLEBUTTON,
            TOOLBAR,
            VIEW
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: ThemeManager? = null
        @JvmStatic
        var duration = 300
        private lateinit var sharedPreferences: SharedPreferences
        private var saveColors: Boolean = false

        @JvmStatic
        fun getInstance(context: Context): ThemeManager {
            if (instance == null) instance = ThemeManager(context)
            return instance as ThemeManager
        }

        fun attach(activity: Activity, callback: Callback) {
            activity.findViewById<View>(android.R.id.content).post { callback.run(getInstance(activity)) }
        }
    }
}
