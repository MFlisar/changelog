package com.michaelflisar.changelog;

import com.michaelflisar.changelog.classes.IRecyclerViewItem;
import com.michaelflisar.changelog.classes.Release;
import com.michaelflisar.changelog.internal.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class Changelog {

    private final List<Release> mReleases;

    Changelog() {
        mReleases = new ArrayList<>();
    }

    void add(Release release) {
        mReleases.add(release);
    }

    public final List<Release> getReleases() {
        return mReleases;
    }

    public final List<IRecyclerViewItem> getAllRecyclerViewItems() {
        return ChangelogUtil.getRecyclerViewItems(mReleases);
    }

    // ----------------------
    // Enum
    // ----------------------

    public enum Type {
        Info,
        Bug,
        Improvement;

        public static Type parseFromString(String type) {
            if (type != null) {
                if (type.equals(Constants.XML_TAG_BUGFIX)) {
                    return Bug;
                }
                if (type.equals(Constants.XML_TAG_IMPROVEMENT)) {
                    return Improvement;
                }
                if (type.equals(Constants.XML_TAG_INFO)) {
                    return Info;
                }
            }
            throw new RuntimeException(String.format("Could not parse changelog type: %s", type));
        }
    }
}
