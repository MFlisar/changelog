package com.michaelflisar.changelog.classes;

import android.content.Context;

import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;
import com.michaelflisar.changelog.tags.IChangelogTag;

/**
 * Created by flisar on 05.03.2018.
 */
public class Row implements IRecyclerViewItem {
    private final Release mRelease;
    private final IChangelogTag mTag;

    private String mTitle;
    private String mText;
    private String mFilter;

    public final Release getRelease() {
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
    public final int getVersionCode() {
        return mRelease.getVersionCode();
    }

    public Row(Release release, IChangelogTag tag, String title, String text, String filter) {
        mRelease = release;
        mTag = tag;
        mTitle = title;
        if (text != null) {
            text = text.replaceAll("\\[", "<").replaceAll("\\]", ">");
        }
        mText = text;
        mFilter = filter;
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
