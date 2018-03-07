package com.michaelflisar.changelog.classes;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.michaelflisar.changelog.ChangelogBuilder;

/**
 * Created by flisar on 06.03.2018.
 */

public interface IChangelogRenderer<VHHeader extends RecyclerView.ViewHolder, VHRow extends RecyclerView.ViewHolder> extends Parcelable {

    VHHeader createHeaderViewHolder(LayoutInflater inflater, ViewGroup parent, ChangelogBuilder builder);

    VHRow createRowViewHolder(LayoutInflater inflater, ViewGroup parent, ChangelogBuilder builder);

    /**
     * is called from the RecyclerView adapter if it wants to bind a release item
     *
     * @param context    the context of the view
     * @param viewHolder the viewHolder
     * @param release    the release that should be bound to the viewHolder
     * @param builder    the builder
     */
    void bindHeader(Context context, VHHeader viewHolder, Release release, ChangelogBuilder builder);

    /**
     * is called from the RecyclerView adapter if it wants to bind a row item
     *
     * @param context    the context of the view
     * @param viewHolder the viewHolder
     * @param row        the row that should be bound to the viewHolder
     * @param builder    the builder
     */
    void bindRow(Context context, VHRow viewHolder, Row row, ChangelogBuilder builder);
}
