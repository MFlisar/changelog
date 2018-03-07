package com.michaelflisar.changelog.demo;

import android.content.Context;

import com.michaelflisar.changelog.tags.IChangelogTag;

/**
 * Created by flisar on 07.03.2018.
 */

public class MyCustomXMLTag implements IChangelogTag {
    @Override
    public String getXMLTagName() {
        return "customTag";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        String prefix = "<font color=\"#0000FF\"><b>Custom tag prefix: </b></font>";
        return prefix + changeText;
    }
}
