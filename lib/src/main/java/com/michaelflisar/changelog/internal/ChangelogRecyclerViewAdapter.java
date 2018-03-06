package com.michaelflisar.changelog.internal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.changelog.ChangelogSetup;
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
    private final ChangelogSetup mSetup;
    private List<IRecyclerViewItem> mItems;

    // ----------------
    // Constructors
    // ----------------

    public ChangelogRecyclerViewAdapter(Context context, ChangelogSetup setup, List<IRecyclerViewItem> items) {
        mContext = context;
        mSetup = setup;
        mItems = items;
    }

    public void setItems(List<IRecyclerViewItem> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Type.Header.ordinal()) {
            return new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(mSetup.getLayoutHeaderId(), parent, false), mSetup);
        } else {
            return new ViewHolderRow(LayoutInflater.from(parent.getContext()).inflate(mSetup.getLayoutRowId(), parent, false), mSetup);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (isHeader(position)) {
            mSetup.bindHeader(mContext, (ViewHolderHeader) viewHolder, (Release) getItem(position), mSetup);
        } else {
            mSetup.bindRow(mContext, (ViewHolderRow) viewHolder, (Row) getItem(position), mSetup);
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

    // -------------------------------------------------------------
    // ViewHolder
    // -------------------------------------------------------------

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {
        public View viewVersion;
        public View viewDate;

        public ViewHolderHeader(View itemView, ChangelogSetup setup) {
            super(itemView);
            viewVersion = itemView.findViewById(setup.getLayoutItemVersionId());
            viewDate = itemView.findViewById(setup.getLayoutItemDateId());
        }
    }

    public static class ViewHolderRow extends RecyclerView.ViewHolder {
        public View viewText;
        public View viewBullet;

        public ViewHolderRow(View itemView, ChangelogSetup setup) {
            super(itemView);
            viewText = itemView.findViewById(setup.getLayoutItemTextId());
            viewBullet = itemView.findViewById(setup.getLayoutItemBulletId());
        }
    }
}
