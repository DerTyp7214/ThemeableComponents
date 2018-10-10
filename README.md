# ThemeableComponents

### List of ThemeableComponents:

`com.dertyp7214.themeablecomponents.components.ThemeableButton`
`com.dertyp7214.themeablecomponents.components.ThemeableCheckBox`
`com.dertyp7214.themeablecomponents.components.ThemeableEditText`
`com.dertyp7214.themeablecomponents.components.ThemeableFloatingActionButton`
`com.dertyp7214.themeablecomponents.components.ThemeableProgressBar`
`com.dertyp7214.themeablecomponents.components.ThemeableRadioButton`
`com.dertyp7214.themeablecomponents.components.ThemeableSeekBar`
`com.dertyp7214.themeablecomponents.components.ThemeableSwitch`
`com.dertyp7214.themeablecomponents.components.ThemeableToggleButton`
`com.dertyp7214.themeablecomponents.components.ThemeableToolbar`
`com.dertyp7214.themeablecomponents.components.ThemeableView`

use them like the normal ones.

In your Activity you can get the ThemeManager: `ThemeManager.getInstance(this)`

### Functions:

`List<Component> : themeManager.getComponents()` gets all Components

`List<Component> : themeManager.getComponents(this)` gets all Components from this Activity

`List<Component> : themeManager.filterComponents(List<Component>, ThemeManager.Component.TYPE)` filters the Components by Type


### ChangeColors:

`themeManager.changeAccentColor(color)` changes the color of all accent components

`themeManager.changeAccentColor(color, animated)` changes the color animated of all accent components

`themeManager.changePrimaryColor(color)` changes the color of all primary components

`themeManager.changePrimaryColor(color, animated)` changes the color animated of all primary components

`themeManager.changePrimaryColor(activity, color, statusBar, navigationBar)` changes the color of all primary components and the navigation, status-bar

`themeManager.changePrimaryColor(activity, color, statusBar, navigationBar, animated)` changes the color animated of all primary components and the navigation, status-bar


### ColorPicker

```Java
ColorPicker colorPicker = new ColorPicker(activity);
colorPicker.setColor(color);
colorPicker.setAnimationTime(duration);
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
colorPicker.setDarkMode(darkmode);
```