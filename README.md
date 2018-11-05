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

### 2. Add the dependency:
```gralde
dependencies {
    implementation 'com.github.DerTyp7214:ThemeableComponents:<LATEST_VERSION>'
}
```

## Usage

### Register ThemeManager in Application Class (Optional)

Add to your `Application.class` in `onCreate`: `ThemeManager.getInstance(this).registerApplication(this)`.<br/>
It will automatically apply your selected theme (dark / light) and apply all colors to your (Themeable)components.

### List of ThemeableComponents:

ThemeableComponents | Original
------------------------------------------------------------- | --
`com.dertyp7214.themeablecomponents.components.ThemeableButton` | MaterialButton
`com.dertyp7214.themeablecomponents.components.ThemeableCheckBox` | AppCompatCheckBox 
`com.dertyp7214.themeablecomponents.components.ThemeableEditText` | TextInputEditText 
`com.dertyp7214.themeablecomponents.components.ThemeableFloatingActionButton` | FloatingActionButton 
`com.dertyp7214.themeablecomponents.components.ThemeableProgressBar` | ProgressBar 
`com.dertyp7214.themeablecomponents.components.ThemeableRadioButton` | AppCompatRadioButton 
`com.dertyp7214.themeablecomponents.components.ThemeableSeekBar` | AppCompatSeekBar 
`com.dertyp7214.themeablecomponents.components.ThemeableSwitch` | Switch 
`com.dertyp7214.themeablecomponents.components.ThemeableToggleButton` | ToggleButton
`com.dertyp7214.themeablecomponents.components.ThemeableToolbar` | Toolbar 
`com.dertyp7214.themeablecomponents.components.ThemeableView` | View 

Screens | Description
-- | --
`com.dertyp7214.themeablecomponents.screens.ThemeableActivity` | extend from this instead of Activity. It activates default theme (dark/light)

use them like the normal ones.

In your Activity you can get the ThemeManager: `ThemeManager.getInstance(this)`

### Functions:

Function | Description
-- | --
`ThemeManager.getInstance(activity: Activity): ThemeManager` | returns instance of ThemeManager
`themeManager.getTransitions(): Pair<Int, Int>` | returns enter and exit Transtiotions
`themeManager.getComponents(): List<Component>` | returns all Components
`themeManager.getComponents(this: Activity): List<Component>` | returns all Components from this Activity
`themeManager.filterComponents(components: List<Component>, type: ThemeManager.Component.TYPE): List<Component>` | filters the Components by Type


### ChangeStyles:

Function | Description
-- | --
`themeManager.darkMode: Boolean` | set / get darkMode
`themeManager.setCustomTransitions(enterAnim: Int, exitAnim: Int)` | set custom transtitions
`themeManager.enableStatusAndNavBar(activity: Activity)` | enable coloring of status and navbar
`themeManager.openThemeBottomSheet(activity: Activity)` | opens bottomsheet with themable components
`themeManager.setDefaultAccent(color: Int)` | changes the default color of all accent components
`themeManager.setDefaultPrimary(color: Int)` | changes the default color of all primary components
`themeManager.changeAccentColor(color: Int)` | changes the color of all accent components
`themeManager.changeAccentColor(color: Int, animated: Boolean)` | changes the color animated of all accent components
`themeManager.changePrimaryColor(color: Int)` | changes the color of all primary components
`themeManager.changePrimaryColor(color: Int, animated: Boolean)` | changes the color animated of all primary components
`themeManager.changePrimaryColor(activity: Activity, color: Int, statusBar: Boolean, navigationBar: Boolean)` | changes the color of all primary components and the navigation, status-bar
`themeManager.changePrimaryColor(activity: Activity, color: Int, statusBar: Boolean, navigationBar: Boolean, animated: Boolean)` | changes the color animated of all primary components and the navigation, status-bar

### Download Example-APP

[Download](https://github.com/DerTyp7214/ThemeableComponents/raw/master/app/release/app-release.apk)

### ColorPicker

```Java
ColorPicker colorPicker = new ColorPicker(activity);
colorPicker.setColor(color);
colorPicker.setAnimationTime(duration);
colorPicker.setDarkMode(darkmode);
colorPicker.setListener(new ColorPicker.Listener() {
    @Override
    public void color(int color) {

    }

    @Override
    public void update(int color) {

    }

    @Override
    public void cancel() {

    }
});
colorPicker.show();
```
