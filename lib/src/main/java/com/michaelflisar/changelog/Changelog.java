package com.michaelflisar.changelog;

import com.michaelflisar.changelog.classes.IChangelogSorter;
import com.michaelflisar.changelog.classes.IRecyclerViewItem;
import com.michaelflisar.changelog.classes.Release;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class Changelog {

    private final List<Release> mReleases;

    Changelog() {
        mReleases = new ArrayList<>();
    }

    public void sort(IChangelogSorter sorter) {
        if (sorter != null) {
            for (Release r : mReleases) {
                Collections.sort(r.getRows(), sorter);
            }
        }
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
