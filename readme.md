# color-morph-drawable

[![Download](https://api.bintray.com/packages/jackpocket/maven/color-morph-drawable/images/download.svg) ](https://bintray.com/jackpocket/maven/color-morph-drawable/_latestVersion)

Easily-extendable color-morphing of a background Drawable.

![color-morph-drawable Sample](https://github.com/jackpocket/android-color-morph-drawable/raw/master/demo.gif)

# Installation

```
    repositories {
        jcenter()
    }

    dependencies {
        compile('com.jackpocket:color-morph-drawable:1.0.0')
    }
```

# Usage

#### ColorMorphDrawable
Any View can have its background set as a `ColorMorphDrawable`; the base component for this View. Morphing between colors can be done by calling `ColorMorphDrawable.morphWith(Morphable)`, or the supplied helper methods, on your drawable instance;

```java
ColorMorphDrawable drawable = new ColorMorphDrawable(0xFF3498DB)
    .setCornerRadiusPx(4)
    .setDefaultDurationMs(425)
    .setDefaultInterpolator(new AccelerateDecelerateInterpolator());

findViewById(R.id.some_view)
    .setBackground(drawable);

drawable.morphRippledTo(0xFF9B59B6);
```

#### ColorMorphController
The `ColorMorphController` helper class is for managing the state of the drawable based on touch events, as well as helping with the delegation of gestures. Calling `ColorMorphController.attach(View)` will handle the creation and attaching of a `ColorMorphDrawable` based on the supplied attributes or resource defaults.

```java
ColorMorphController controller = new ColorMorphController(getContext())
    .setColorNormal(0xFF3498DB)
    .setColorTouched(0xFF9B59B6)
    .setOnClickListener(v -> doSomething())
    .setSecondaryGestureListener(new SimpleOnGestureListener())
    .attach(findViewById(R.id.some_view));
```

#### ColorMorphLayout
You can't use custom drawables in XML layout files, but you can wrap your layouts in another custom layout that directly implements the required controllers. That's where the `ColorMorphLayout` comes into play.

```xml
<com.jackpocket.colormorph.ColorMorphLayout
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:borderCornerRadius="10dip"
    app:colorNormal="#FF3498DB"
    app:colorTouched="#FF9B59B6"
    app:touchEffectsEnabled="true">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dip"
        android:text="Test Morph"
        android:textColor="#FFFFFF"/>

</com.jackpocket.colormorph.ColorMorphLayout>
```

If you look at the `ColorMorphLayout` class, you can see how to implement the `ColorMorphController` directly into your own custom classes. Just be sure to pass the click/gesture listeners to the controller correctly.

#### Overriding defaults
Overriding the default behavior / resources is fairly straightforward:

```xml
<color name="cmd__default_color_normal">#FFFFFFFF</color>
<color name="cmd__default_color_touched">#FFCECEC5</color>

<dimen name="cmd__default_corner_radius">0dip</dimen>

<bool name="cmd__touch_effects_enabled_by_default">true</bool>
```



