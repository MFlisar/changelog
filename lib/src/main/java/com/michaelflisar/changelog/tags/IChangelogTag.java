package com.michaelflisar.changelog.tags;

import android.content.Context;

/**
 * Created by flisar on 07.03.2018.
 */

public interface IChangelogTag {

    String getXMLTagName();

    String formatChangelogRow(Context context, String changeText);
}
