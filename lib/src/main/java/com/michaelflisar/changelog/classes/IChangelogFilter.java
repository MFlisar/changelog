package com.michaelflisar.changelog.classes;

import android.os.Parcelable;

/**
 * Created by flisar on 05.03.2018.
 */

public interface IChangelogFilter extends Parcelable {
    boolean checkFilter(IRecyclerViewItem item);
}
