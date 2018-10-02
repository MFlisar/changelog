package com.michaelflisar.changelog;

import com.michaelflisar.changelog.interfaces.IChangelogSorter;
import com.michaelflisar.changelog.interfaces.IRecyclerViewItem;
import com.michaelflisar.changelog.items.ItemRelease;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class Changelog {

    private final List<ItemRelease> mReleases;

    Changelog() {
        mReleases = new ArrayList<>();
    }

    public final void sort(IChangelogSorter sorter) {
        if (sorter != null) {
            for (ItemRelease r : mReleases) {
                Collections.sort(r.getRows(), sorter);
            }
        }
    }

    final void add(ItemRelease release) {
        mReleases.add(release);
    }

    public final List<ItemRelease> getReleases() {
        return mReleases;
    }

    public final List<IRecyclerViewItem> getAllRecyclerViewItems() {
        return ChangelogUtil.getRecyclerViewItems(mReleases);
    }
}
