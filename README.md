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
* supports raw and xml resources, default resource name is `changelog.xml` in raw folder
* supports an optional rate app button
* supports summaries with a "show more" button

**Examples - activity, dialog, automatically sorted activity, custom layout**

![Changelog activity](https://github.com/MFlisar/changelog/blob/master/images/activity.png)
![Changelog dialog](https://github.com/MFlisar/changelog/blob/master/images/dialog.png)
![Changelog dialog-sorted](https://github.com/MFlisar/changelog/blob/master/images/activity-sorted.png)
![Changelog custom](https://github.com/MFlisar/changelog/blob/master/images/custom.png)
![Changelog custom](https://github.com/MFlisar/changelog/blob/master/images/summary.png)

### Compose version

You can find a **compose version** here: [ComposeChangelog](https://github.com/MFlisar/ComposeChangelog)

It does have some improvements like it won't show empty changelogs, it does use coroutines to load data, its even more modular and code was cleaned.
 
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
    compile 'com.github.MFlisar:changelog:NEWEST-VERSION'
}
```

NEWEST-VERSION: [![Release](https://jitpack.io/v/MFlisar/changelog.svg)](https://jitpack.io/#MFlisar/changelog)

### Demo

Just check out the [demo activity](https://github.com/MFlisar/changelog/blob/master/demo/src/main/java/com/michaelflisar/changelog/demo/MainActivity.java), it will show the base usage of the builder and it's settings.

### Simple usage example

You must add a `changelog.xml` to your raw resource files. Then you can use it like following:

```java
ChangelogBuilder builder = new ChangelogBuilder()
	.withUseBulletList(bulletList) // true if you want to show bullets before each changelog row, false otherwise
	.withMinVersionToShow(110)     // provide a number and the log will only show changelog rows for versions equal or higher than this number
	.withFilter(new ChangelogFilter(ChangelogFilter.Mode.Exact, "somefilterstring", true)) // this will filter out all tags, that do not have the provided filter attribute
	.withManagedShowOnStart(true)  // library will take care to show activity/dialog only if the changelog has new infos and will only show this new infos
	.withRateButton(true) // enable this to show a "rate app" button in the dialog => clicking it will open the play store; the parent activity or target fragment can also implement IChangelogRateHandler to handle the button click
	.withSummary(true, true) // enable this to show a summary and a "show more" button, the second paramter describes if releases without summary items should be shown expanded or not
	.withVersionNameFormatter(new DefaultAutoVersionNameFormatter(DefaultAutoVersionNameFormatter.Type.MajorMinor, "b")) // Will format a version 100 like "1.0b", default is without the b
	.withTitle("Some custom title") // provide a custom title if desired, default one is "Changelog <VERSION>"
	.withOkButtonLabel("Back") // provide a custom ok button text if desired, default one is "OK"
	.withRateButtonLabel("Wanna rate?") // provide a custom rate button text if desired, default one is "Rate"
	.buildAndShowDialog(activity, false); // second parameter defines, if the dialog has a dark or light theme
	
	// Check advanced usage section for more
```

### Example `changelog.xml`

```xml
<changelog>

	<!-- simple example - no version name => will be generated based on verionCode: 100 => v1.00 -->
	<release versionCode="120" versionName="v1.2" date="2018-03-04">
		<info>Some info</info>
		<new type="summary">Some improvement</new>
		<bugfix>Some bugfix</bugfix>
	</release>
	
	<!-- simple example - no filter -->
	<release versionCode="120" versionName="v1.2" date="2018-03-04">
		<info>Some info</info>
		<new type="summary">Some improvement</new>
		<bugfix>Some bugfix</bugfix>
	</release>
	
	<!-- example with custom filter in release tag -->
	<release versionCode="110" versionName="v1.1" date="2018-03-03" filter="dogs">
		<info>Some dogs info - filter only set in release tag</info>
		<new type="summary">Some dogs improvement - filter only set in release tag</new>
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
* optionally unregister all 3 default tags before adding custom tags if you don't want to use them: `ChangelogSetup.get().clearTags()`

#### Custom sorting

* create a custom tag class that implements [IChangelogSorter.java](https://github.com/MFlisar/changelog/blob/master/lib/src/main/java/com/michaelflisar/changelog/interfaces/IChangelogSorter.java) or use the integrated sorter that sorts by importance (new > info > bugfix > custom)
* add it to the builder like following:

```java
ChangelogBuilder builder = new ChangelogBuilder()
	.withSorter(new ImportanceChangelogSorter()); // or provide a custom sorter
```

## RAW vs XML resource

If you do not use apostrophes you can use xml resources, otherwise you should use the raw resources. XML resources are faster (~10x), but they are precompiled and offer limited functionality. RAW resources are slower, but work better. Decide yourself. Use the RAW resource (which is used by default) if you don't know what you should use.

### TODO

Some features are probably nice for some people, I will add them if I need them. Feel free to contribute, I already made some issues for main missing features:	
* support online source for xml - https://github.com/MFlisar/changelog/issues/1
* add some setup features to the default `ChangelogRenderer` (colors, text size, ...)

### Credits

This library is inspired by https://github.com/gabrielemariotti/changeloglib and the xml parser and the basic idea is heavily based on Gabriele Mariotti code. Thanks for this
