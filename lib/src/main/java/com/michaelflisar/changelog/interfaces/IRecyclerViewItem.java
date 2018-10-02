package com.michaelflisar.changelog.interfaces;

import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

/**
 * Created by flisar on 05.03.2018.
 */

public interface IRecyclerViewItem {
    // RecyclerView
    ChangelogRecyclerViewAdapter.Type getRecyclerViewType();
    int getLayoutId();
}
