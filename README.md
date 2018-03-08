### Changelog [![Release](https://jitpack.io/v/MFlisar/changelog.svg)](https://jitpack.io/#MFlisar/changelog)

This is a simple builder based changelog library that shows a changelog in a `RecyclerView`, a `RecyclerView` dialog or a `RecyclerView` activity with following features:

**Features**
* filtering
  * based on a min version (useful for app start to only show new changelog entries)
  * based on a custom filter string (useful for filtering changelog based on build flavour)
* builder supports any in layout `RecyclerView` or provides a ready to use `Dialog` or `Activity`
* also supports automatic handling of showing changelogs on app start (uses preference to save last seen changelog version and handles everything for you automatically to only show **new changelogs** and only show those once)
* shows loading progress in `Dialog` or `Activity` while parsing changelog
* customise look
  * optional bullet lists
  * custom and automatic version names (e.g. version 100 will be formatted as "v1.00" by default if no custom version name is provided)
  * fully **customised layouts** via a custom renderer
  * **custom xml tags** + **custom rendering** of them
  * automatic and **custom sorting**

**Examples - activity, dialog, automatically sorted activity, custom layout**

![Changelog activity](https://github.com/MFlisar/changelog/blob/master/images/activity.png)
![Changelog dialog](https://github.com/MFlisar/changelog/blob/master/images/dialog.png)
![Changelog dialog-sorted](https://github.com/MFlisar/changelog/blob/master/images/activity-sorted.png)
![Changelog custom](https://github.com/MFlisar/changelog/blob/master/images/custom.png)
 
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
    compile 'com.github.MFlisar:changelog:1.0.1'
}
```

### Demo

Just check out the [demo activity](https://github.com/MFlisar/changelog/blob/master/demo/src/main/java/com/michaelflisar/changelog/demo/MainActivity.java), it will show the base usage of the builder and it's settings.

### Simple usage example

You must add a `changelog.xml` to your xml resource file. Then you can use it like following:

```java
ChangelogBuilder builder = new ChangelogBuilder()
	.withUseBulletList(bulletList) // true if you want to show bullets before each changelog row, false otherwise
	.withMinVersionToShow(110)     // provide a number and the log will only show changelog rows for versions equal or higher than this number
	.withFilter(new ChangelogFilter(ChangelogFilter.Mode.Exact, "somefilterstring", true)) // this will filter out all tags, that do not have the provided filter attribute
	.withManagedShowOnStart(true)  // library will take care to show activity/dialog only if the changelog has new infos and will only show this new infos
	.buildAndShowDialog(activity, false); // second parameter defines, if the dialog has a dark or light theme
```

### Example `changelog.xml`

```xml
<changelog>

	<!-- simple example - no version name => will be generated based on verionCode: 100 => v1.00 -->
	<release versionCode="120" versionName="v1.2" date="2018-03-04">
		<info>Some info</info>
		<new>Some improvement</new>
		<bugfix>Some bugfix</bugfix>
	</release>
	
	<!-- simple example - no filter -->
	<release versionCode="120" versionName="v1.2" date="2018-03-04">
		<info>Some info</info>
		<new>Some improvement</new>
		<bugfix>Some bugfix</bugfix>
	</release>
	
	<!-- example with custom filter in release tag -->
	<release versionCode="110" versionName="v1.1" date="2018-03-03" filter="dogs">
		<info>Some dogs info - filter only set in release tag</info>
		<new>Some dogs improvement - filter only set in release tag</new>
		<bugfix>Some dogs bugfix - filter only set in release tag</bugfix>
	</release>
	
	<!-- example with filters in rows -->
	<release versionCode="100" versionName="v1.0" date="2018-03-01">
		<info filter="cats">New cats added - this info has filter text 'cats'</info>
		<info filter="dogs">New dogs added - this info has filter text 'dogs'</info>
		<new filter="cats">Some cats improvement - this info has filter text 'cats'</new>
		<new filter="dogs">Some dogs improvement - this info has filter text 'dogs'</new>
		<bugfix filter="cats">Some cats bugfix - this info has filter text 'cats'</bugfix>
		<bugfix filter="dogs">Some dogs bugfix - this info has filter text 'dogs'</bugfix>
	</release>
</changelog>
```

### Advanced usage

#### Custom layouts

Simply provide custom a custom renderer (very simply interface), derive it from the default `ChangelogRenderer` to only adjust small things.

```java
ChangelogBuilder builder = new ChangelogBuilder()
	.withRenderer(...); // provide a custom item renderer
```	

Have a look at following classes to see how this works:

* default renderer: [ChangelogRenderer.java](https://github.com/MFlisar/changelog/blob/master/lib/src/main/java/com/michaelflisar/changelog/classes/ChangelogRenderer.java)
* example custom renderer: [ExampleCustomRenderer.java](https://github.com/MFlisar/changelog/blob/master/demo/src/main/java/com/michaelflisar/changelog/demo/ExampleCustomRenderer.java)

#### Custom tags

* create a custom tag class that implements [IChangelogTag.java](https://github.com/MFlisar/changelog/blob/master/lib/src/main/java/com/michaelflisar/changelog/tags/IChangelogTag.java)
* register this class like `ChangelogSetup.get().registerTag(...)`
* optionally unregister all 3 default tags before adding custom tags if don't want to use them:  `ChangelogSetup.get().clearTags()`

#### Custom sorting

* create a custom tag class that implements [IChangelogSorter.java](https://github.com/MFlisar/changelog/blob/master/lib/src/main/java/com/michaelflisar/changelog/classes/IChangelogSorter.java) or use the integrated sorter that sorts by importance (new > info > bugfix > custom)
* add it to the builder like following:

```java
ChangelogBuilder builder = new ChangelogBuilder()
	.withSorter(new ImportanceChangelogSorter()); // or provide a custom sorter
```

### TODO

Some features are probably nice for some people, I will add them if I need them. Feel free to contribute, I already made some issues for main missing features:	
* support online source for xml - https://github.com/MFlisar/changelog/issues/1
* support show changelog summaries - https://github.com/MFlisar/changelog/issues/3
* add some setup features to the default `ChangelogRenderer` (colors, text size, ...)

### Credits

This library is inspired by https://github.com/gabrielemariotti/changeloglib and the xml parser and the basic idea is heavily based on Gabriele Mariotti code. Thanks for this