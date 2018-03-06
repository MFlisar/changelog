package com.michaelflisar.changelog.classes;

import android.content.Context;
import android.os.Parcelable;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

/**
 * Created by flisar on 06.03.2018.
 */

public interface IChangelogRenderer extends Parcelable {

    /**
     * is called from the RecyclerView adapter if it wants to bind a release item
     *
     * @param context    the context of the view
     * @param viewHolder the viewHolder
     * @param release    the release that should be bound to the viewHolder
     * @param builder    the builder
     */
    void bindHeader(Context context, ChangelogRecyclerViewAdapter.ViewHolderHeader viewHolder, Release release, ChangelogBuilder builder);

    /**
     * is called from the RecyclerView adapter if it wants to bind a row item
     *
     * @param context    the context of the view
     * @param viewHolder the viewHolder
     * @param row        the row that should be bound to the viewHolder
     * @param builder    the builder
     */
    void bindRow(Context context, ChangelogRecyclerViewAdapter.ViewHolderRow viewHolder, Row row,  ChangelogBuilder builder);
}
