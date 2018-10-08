package com.michaelflisar.changelog.internal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.interfaces.IRecyclerViewItem;
import com.michaelflisar.changelog.items.ItemMore;
import com.michaelflisar.changelog.items.ItemRelease;
import com.michaelflisar.changelog.items.ItemRow;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum Type {
        Row,
        Header,
        More
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

    public void replaceMoreItem(int moreIndex, List<IRecyclerViewItem> items) {
        mItems.remove(moreIndex);
        if (items.size() == 0) {
            notifyItemRemoved(moreIndex);
        } else {
            mItems.addAll(moreIndex, items);
            notifyItemChanged(moreIndex);
            notifyItemRangeInserted(moreIndex + 1, items.size() - 1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Type.Header.ordinal()) {
            return mBuilder.getRenderer().createHeaderViewHolder(mInflater, parent, mBuilder);
        } else if (viewType == Type.Row.ordinal()) {
            return mBuilder.getRenderer().createRowViewHolder(mInflater, parent, mBuilder);
        } else if (viewType == Type.More.ordinal()) {
            return mBuilder.getRenderer().createMoreViewHolder(mInflater, parent, mBuilder);
        } else {
            throw new RuntimeException(String.format("Type not handled: %s", viewType));
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItem(position).getRecyclerViewType() == Type.Header) {
            mBuilder.getRenderer().bindHeader(this, mContext, viewHolder, (ItemRelease) getItem(position), mBuilder);
        } else if (getItem(position).getRecyclerViewType() == Type.Row) {
            mBuilder.getRenderer().bindRow(this, mContext, viewHolder, (ItemRow) getItem(position), mBuilder);
        } else if (getItem(position).getRecyclerViewType() == Type.More) {
            mBuilder.getRenderer().bindMore(this, mContext, viewHolder, (ItemMore) getItem(position), mBuilder);
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
