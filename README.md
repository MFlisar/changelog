### Changelog [![Release](https://jitpack.io/v/MFlisar/changelog.svg)](https://jitpack.io/#MFlisar/changelog)

This is a simple (and lightweight) builder based changelog library that will show a changelog defined as xml in a `RecyclerView` or a `RecyclerView` dialog. It supports custom filters, to easily only show parts of your changelog (for example based on a build flavour or to only show new changelog entries on app start).
 
### Gradle (via [JitPack.io](https://jitpack.io/))

1. add jitpack to your project's `build.gradle`:
```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```
2. add the compile statement to your module's `build.gradle`:
```groovy
dependencies {
    compile 'com.github.MFlisar:changelog:0.1'
}
```

##### Demo

Just check out the [DemoActivity](https://github.com/MFlisar/changelog/blob/master/demo/src/main/java/com/michaelflisar/changelog/demo/MainActivity.java), it will show the base usage of the builder and it's settings.

##### Simple usage example

Use the `RxBusBuilder` to create `Subscriptions` or simple `Flowables`. Just like following:
```java
ChangelogBuilder builder = new ChangelogBuilder()
	.withUseBulletList(bulletList) // true if you want to show bullets before each changelog row, false otherwise
	.withMinVersionToShow(110)     // provide a number and the log will only show changelog rows for versions equal or higher than this number
	.withFilter(new ChangelogFilter(ChangelogFilter.Mode.Exact, "somefilterstring", true)) // this will filter out all tags, that do not have the provided filter attribute
	.buildAndShowDialog(activity, false); // second parameter defines, if the dialog has a dark or light theme
```

##### Credits

This library is inspired by https://github.com/gabrielemariotti/changeloglib and the xml parser and the basic idea is heavily based on Gabriele Mariotti code. Thanks for this