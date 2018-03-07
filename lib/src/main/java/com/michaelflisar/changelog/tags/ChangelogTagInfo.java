package com.michaelflisar.changelog.tags;

import android.content.Context;

/**
 * Created by flisar on 07.03.2018.
 */

public class ChangelogTagInfo implements IChangelogTag {

    @Override
    public String getXMLTagName() {
        return "info";
    }

    @Override
    public String formatChangelogRow(Context context, String changeText) {
        return changeText;
    }
}
