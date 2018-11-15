[![](https://jitpack.io/v/DerTyp7214/ThemeableComponents.svg)](https://jitpack.io/#DerTyp7214/ThemeableComponents)

# ThemeableComponents

## To import the lib

### 1. Add it in your root build.gradle at the end of repositories
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. Add the dependency
```gradle
dependencies {
    implementation 'com.github.DerTyp7214:ThemeableComponents:<LATEST_VERSION>'
}
```


## Usage

### Register ThemeManager in Application Class (Optional)

Add to your `Application.class` in `onCreate`: `ThemeManager.getInstance(this).registerApplication(this)`.<br/>
It will automatically apply your selected theme (dark / light) and apply all colors to your (Themeable)components.


### ThemeableComponents

ThemeableComponents | Original
-- | --
`com.dertyp7214.themeablecomponents.components.ThemeableButton` | [MaterialButton](https://developer.android.com/reference/com/google/android/material/button/MaterialButton)
`com.dertyp7214.themeablecomponents.components.ThemeableCheckBox` | [AppCompatCheckBox](https://developer.android.com/reference/androidx/appcompat/widget/AppCompatCheckBox)
`com.dertyp7214.themeablecomponents.components.ThemeableEditText` | [TextInputEditText](https://developer.android.com/reference/com/google/android/material/textfield/TextInputEditText)
`com.dertyp7214.themeablecomponents.components.ThemeableFloatingActionButton` | [FloatingActionButton](https://developer.android.com/reference/com/google/android/material/floatingactionbutton/FloatingActionButton)
`com.dertyp7214.themeablecomponents.components.ThemeableFloatingActionButtonProgressBar` | [FloatingActionButton](https://developer.android.com/reference/com/google/android/material/floatingactionbutton/FloatingActionButton)
`com.dertyp7214.themeablecomponents.components.ThemeableProgressBar` | [ProgressBar](https://developer.android.com/reference/android/widget/ProgressBar)
`com.dertyp7214.themeablecomponents.components.ThemeableRadioButton` | [AppCompatRadioButton](https://developer.android.com/reference/androidx/appcompat/widget/AppCompatRadioButton)
`com.dertyp7214.themeablecomponents.components.ThemeableSeekBar` | [AppCompatSeekBar](https://developer.android.com/reference/androidx/appcompat/widget/AppCompatSeekBar)
`com.dertyp7214.themeablecomponents.components.ThemeableSwitch` | [Switch](https://developer.android.com/reference/android/widget/Switch)
`com.dertyp7214.themeablecomponents.components.ThemeableToggleButton` | [ToggleButton](https://developer.android.com/reference/android/widget/ToggleButton)
`com.dertyp7214.themeablecomponents.components.ThemeableToolbar` | [Toolbar](https://developer.android.com/reference/androidx/appcompat/widget/Toolbar)
`com.dertyp7214.themeablecomponents.components.ThemeableView` | [View](https://developer.android.com/reference/android/view/View)

Screens | Description
-- | --
`com.dertyp7214.themeablecomponents.screens.ThemeableActivity` | extend from this instead of Activity. It activates default theme (dark/light)

use them like the normal ones.

In your Activity you can get the ThemeManager: `ThemeManager.getInstance(this)`


### Functions

Function | Description
-- | --
`ThemeManager.getInstance(activity: Activity): ThemeManager` | returns instance of [ThemeManager](#thememanager)
`themeManager.getTransitions(): Pair<Int, Int>` | returns enter and exit Transitions
`themeManager.getComponents(): List<Component>` | returns all Components
`themeManager.getComponents(this: Activity): List<Component>` | returns all Components from this Activity
`themeManager.filterComponents(components: List<Component>, type: ThemeManager.Component.TYPE): List<Component>` | filters the Components by Type


### ThemeManager

Function | Description
-- | --
`themeManager.darkMode: Boolean` | set / get darkMode
`themeManager.startThemeService(activity: Activity)` | starts the theme floating button service
`themeManager.stopThemeService(activity: Activity)` | stops the theme floating button service
`themeManager.setCustomTransitions(enterAnim: Int, exitAnim: Int)` | set custom transitions
`themeManager.enableStatusAndNavBar(activity: Activity)` | enable coloring of status and navigationbar
`themeManager.openThemeBottomSheet(activity: Activity)` | opens Bottomsheet with themeable components
`themeManager.setDefaultAccent(color: Int)` | changes the default color of all accent components
`themeManager.setDefaultPrimary(color: Int)` | changes the default color of all primary components
`themeManager.changeAccentColor(color: Int)` | changes the color of all accent components
`themeManager.changeAccentColor(color: Int, animated: Boolean)` | changes the color animated of all accent components
`themeManager.changePrimaryColor(color: Int)` | changes the color of all primary components
`themeManager.changePrimaryColor(color: Int, animated: Boolean)` | changes the color animated of all primary components
`themeManager.changePrimaryColor(activity: Activity, color: Int, statusBar: Boolean, navigationBar: Boolean)` | changes the color of all primary components and the navigation, status-bar
`themeManager.changePrimaryColor(activity: Activity, color: Int, statusBar: Boolean, navigationBar: Boolean, animated: Boolean)` | changes the color animated of all primary components and the navigation, status-bar


### ColorPicker

Function | Description
-- | --
`ColorPicker(context: Context)` | constructor
`colorPicker.setColor(color: Int)` | set start color
`colorPicker.setAnimationTime(time: Int)` | set startup animation time
`colorPicker.setCancelable(cancelable: Boolean)` | if true you can not exit ColoPicker with touching next to it
`colorPicker.setDarkMode(darkMode: Boolean)` | set Darkmode
`colorPicker.onTouchListener(touchListener: ColorPicker.TouchListener)` | set [ColorPicker.TouchListener](#touch-listener)
`colorPicker.setListener(listener: ColorPicker.Listener)` | set [ColorPicker.Listener](#listener)
`colorPicker.toast(toast: Boolean)` | displays on colorchanging a bottom toast with the value
`colorPicker.setAlpha(alpha: Float)` | set alpha for the popup
`colorPicker.disableInput()` | disable input in the TextField
`colorPicker.enableInput()` | enable input in the TextField
`colorPicker.show()` | displays the ColorPicker


#### Touch Listener
```Kotlin
ColorPicker.TouchListener {
    override fun startTouch() {
    }

    override fun stopTouch() {
    }
}
```

#### Listener
```Kotlin
ColorPicker.Listener {
    override fun color(color: Int) {
    }

    override fun update(color: Int) {
    }

    override fun cancel() {
    }
}
```

### Download Example-APP

[Download](https://github.com/DerTyp7214/ThemeableComponents/raw/master/app/release/app-release.apk)
