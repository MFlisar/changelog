package com.michaelflisar.changelog.interfaces;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;
import com.michaelflisar.changelog.items.ItemMore;
import com.michaelflisar.changelog.items.ItemRelease;
import com.michaelflisar.changelog.items.ItemRow;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 06.03.2018.
 */

public interface IChangelogRenderer<VHHeader extends RecyclerView.ViewHolder, VHRow extends RecyclerView.ViewHolder, VHMore extends RecyclerView.ViewHolder> extends Parcelable {

    /**
     * create a Viewholder for the recycler for a header item
     *
     * @param inflater the inflator to inflate a layout
     * @param parent   the parent view group
     * @param builder  the builder
     * @return the new view holder instance
     */
    VHHeader createHeaderViewHolder(LayoutInflater inflater, ViewGroup parent, ChangelogBuilder builder);

    /**
     * create a Viewholder for the recycler for a header item
     *
     * @param inflater the inflator to inflate a layout
     * @param parent   the parent view group
     * @param builder  the builder
     * @return the new view holder instance
     */
    VHRow createRowViewHolder(LayoutInflater inflater, ViewGroup parent, ChangelogBuilder builder);

    /**
     * create a Viewholder for the recycler for a more item
     *
     * @param inflater the inflator to inflate a layout
     * @param parent   the parent view group
     * @param builder  the builder
     * @return the new view holder instance
     */
    VHMore createMoreViewHolder(LayoutInflater inflater, ViewGroup parent, ChangelogBuilder builder);

    /**
     * is called from the RecyclerView adapter if it wants to bind a release item
     *
     * @param adapter    the adapter
     * @param context    the context of the view
     * @param viewHolder the viewHolder
     * @param release    the release that should be bound to the viewHolder
     * @param builder    the builder
     */
    void bindHeader(ChangelogRecyclerViewAdapter adapter, Context context, VHHeader viewHolder, ItemRelease release, ChangelogBuilder builder);

    /**
     * is called from the RecyclerView adapter if it wants to bind a row item
     *
     * @param adapter    the adapter
     * @param context    the context of the view
     * @param viewHolder the viewHolder
     * @param row        the row that should be bound to the viewHolder
     * @param builder    the builder
     */
    void bindRow(ChangelogRecyclerViewAdapter adapter, Context context, VHRow viewHolder, ItemRow row, ChangelogBuilder builder);

    /**
     * is called from the RecyclerView adapter if it wants to bind a more item
     *
     * @param adapter    the adapter
     * @param context    the context of the view
     * @param viewHolder the viewHolder
     * @param more       the more item that should be bound to the viewHolder
     * @param builder    the builder
     */
    void bindMore(ChangelogRecyclerViewAdapter adapter, Context context, VHMore viewHolder, ItemMore more, ChangelogBuilder builder);
}
