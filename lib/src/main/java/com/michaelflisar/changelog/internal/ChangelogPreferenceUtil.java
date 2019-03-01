package com.michaelflisar.changelog.internal;

import android.content.Context;
import android.content.SharedPreferences;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogUtil;

/**
 * Created by flisar on 06.03.2018.
 */

public class ChangelogPreferenceUtil {

    private static final String PREF_FILE = "com.michaelflisar.changelog";
    private static final String PREF_KEY = "changelogVersion";

    public static int getAlreadyShownChangelogVersion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return prefs.getInt(PREF_KEY, -1);
    }

    public static void updateAlreadyShownChangelogVersion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        int currentVersion = ChangelogUtil.getAppVersionCode(context);
        prefs.edit().putInt(PREF_KEY, currentVersion).apply();
    }

    public static Integer shouldShowChangelogOnStart(Context context) {
        int lastChangelog = getAlreadyShownChangelogVersion(context);
        int currentVersion = ChangelogUtil.getAppVersionCode(context);
        // return last shown version if this is not the first app install AND if last version is less than current
        if (lastChangelog != -1 && lastChangelog < currentVersion) {
            return lastChangelog + 1;
        }
        return null;
    }
}
