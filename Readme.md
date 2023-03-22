## Android Color Picker
A android color picker library
Easy to use and it have a features to save last some colors.

### How to
#### add to repositories 
```
maven { url 'https://jitpack.io' }
```
#### add  dependencies build.gradle(module)
```
implementation 'com.github.ab44gl:ColorPicker-Android:2.0'
```
#### to show dialog
```kotlin
val dialog = ColorPickerDialog()
dialog.setOnOkCancelListener { isOk, color ->
  //do with color
}
dialog.show(supportFragmentManager)
```

#### Here is an example images:

| Example 1  | Example 2 |
| ---- | ---- |
|![IMG_20230315_144204](https://user-images.githubusercontent.com/112514266/225262411-175c94ef-50ae-477e-aeb6-69c15b1d3a95.jpg) | ![IMG_20230315_144222](https://user-images.githubusercontent.com/112514266/225262425-3c9c0188-8cd4-4671-b084-3d41789df69c.jpg)|
