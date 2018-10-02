package com.michaelflisar.changelog.items;

import android.content.Context;

import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.interfaces.IRecyclerViewItem;
import com.michaelflisar.changelog.interfaces.IRow;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;
import com.michaelflisar.changelog.tags.IChangelogTag;

/**
 * Created by flisar on 05.03.2018.
 */
public class ItemRow implements IRow {
    private final ItemRelease mRelease;
    private final IChangelogTag mTag;

    private String mTitle;
    private String mText;
    private String mFilter;
    private final boolean mIsSummary;

    public final ItemRelease getRelease() {
        return mRelease;
    }

    public final IChangelogTag getTag() {
        return mTag;
    }

    public final String getTitle() {
        return mTitle;
    }

    public final String getText(Context context) {
        if (context == null)
            return mText;
        return mTag.formatChangelogRow(context, mText);
    }

    @Override
    public final String getFilter() {
        return mFilter;
    }

    @Override
    public boolean isSummary() {
        return mIsSummary;
    }

    @Override
    public final int getVersionCode() {
        return mRelease.getVersionCode();
    }

    public ItemRow(ItemRelease release, IChangelogTag tag, String title, String text, String filter, boolean isSummary) {
        mRelease = release;
        mTag = tag;
        mTitle = title;
        if (text != null) {
            text = text.replaceAll("\\[", "<").replaceAll("\\]", ">");
        }
        mText = text;
        mFilter = filter;
        mIsSummary = isSummary;
    }

    @Override
    public final ChangelogRecyclerViewAdapter.Type getRecyclerViewType() {
        return ChangelogRecyclerViewAdapter.Type.Row;
    }

    @Override
    public final int getLayoutId() {
        return R.layout.changelog_row;
    }
}
