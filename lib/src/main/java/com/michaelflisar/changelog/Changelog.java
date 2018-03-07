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
}
