package com.michaelflisar.changelog.internal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.classes.IRecyclerViewItem;
import com.michaelflisar.changelog.classes.Release;
import com.michaelflisar.changelog.classes.Row;

import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum Type {
        Row,
        Header
    }

    private final Context mContext;
    private final ChangelogBuilder mBuilder;
    private List<IRecyclerViewItem> mItems;
    private LayoutInflater mInflater;

    // ----------------
    // Constructors
    // ----------------

    public ChangelogRecyclerViewAdapter(Context context, ChangelogBuilder builder, List<IRecyclerViewItem> items) {
        mContext = context;
        mBuilder = builder;
        mItems = items;
        mInflater = LayoutInflater.from(context);
    }

    public void setItems(List<IRecyclerViewItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Type.Header.ordinal()) {
            return mBuilder.getRenderer().createHeaderViewHolder(mInflater, parent, mBuilder);
        } else {
            return mBuilder.getRenderer().createRowViewHolder(mInflater, parent, mBuilder);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (isHeader(position)) {
            mBuilder.getRenderer().bindHeader(mContext, viewHolder, (Release) getItem(position), mBuilder);
        } else {
            mBuilder.getRenderer().bindRow(mContext, viewHolder, (Row) getItem(position), mBuilder);
        }
    }

    private final boolean isHeader(int position) {
        return getItem(position).getRecyclerViewType() == Type.Header;
    }

    private final IRecyclerViewItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public final int getItemViewType(int position) {
        return getItem(position).getRecyclerViewType().ordinal();
    }

    @Override
    public final int getItemCount() {
        return mItems.size();
    }
}
