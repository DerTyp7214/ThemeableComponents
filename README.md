#ThemeableComponents

List of ThemeableComponents:

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

use them like the normal ones.

In your Activity you can get the ThemeManager: `ThemeManager.getInstance(this)`

Functions:

`List<Component> : themeManager.getComponents()` gets all Components

`List<Component> : themeManager.getComponents(this)` gets all Components from this Activity

`List<Component> : themeManager.filterComponents(List<Component>, ThemeManager.Component.TYPE)` filters the Components by Type
