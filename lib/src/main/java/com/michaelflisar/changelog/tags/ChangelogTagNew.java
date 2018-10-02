package com.michaelflisar.changelog.tags;

import android.content.Context;

import com.michaelflisar.changelog.R;

/**
 * Created by flisar on 07.03.2018.
 */

public class ChangelogTagNew implements IChangelogTag {

    public static final String TAG = "new";

    @Override
    public String getXMLTagName() {
        return TAG;
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String  prefix = context.getResources().getString(R.string.changelog_bug_improvement);
        prefix = prefix.replaceAll("\\[", "<").replaceAll("\\]", ">");
        return prefix + changeText;
    }
}
