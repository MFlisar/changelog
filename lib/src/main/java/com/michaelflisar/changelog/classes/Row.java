package com.michaelflisar.changelog.classes;

import android.content.Context;

import com.michaelflisar.changelog.Changelog;
import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

/**
 * Created by flisar on 05.03.2018.
 */
public class Row implements IRecyclerViewItem {
    private final Release mRelease;
    private final Changelog.Type mType;

    private String mTitle;
    private String mText;
    private String mFilter;

    public final Release getRelease() {
        return mRelease;
    }

    public final Changelog.Type getType() {
        return mType;
    }

    public final String getTitle() {
        return mTitle;
    }

    public final String getText(Context context) {
        if (context == null)
            return mText;
        return Formatter.getFormatter().formatChangelogRow(context, mType, mText);
    }

    @Override
    public final String getFilter() {
        return mFilter;
    }

    @Override
    public final int getVersionCode() {
        return mRelease.getVersionCode();
    }

    public Row(Release release, String tag, String title, String text, String filter) {
        mRelease = release;
        mType = Changelog.Type.parseFromString(tag);
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
