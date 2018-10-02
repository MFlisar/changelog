package com.michaelflisar.changelog.items;

import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.interfaces.IHeader;
import com.michaelflisar.changelog.interfaces.IRecyclerViewItem;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */
public class ItemRelease implements IHeader {
    private final String mVersionName;
    private final int mVersionCode;
    private final String mDate;
    private final String mFilter;

    public final String getVersionName() {
        return mVersionName;
    }

    @Override
    public final int getVersionCode() {
        return mVersionCode;
    }

    public final String getDate() {
        return mDate;
    }

    @Override
    public final String getFilter() {
        return mFilter;
    }

    public final List<ItemRow> getRows() {
        return mRows;
    }

    private final List<ItemRow> mRows;

    public ItemRelease(String versionName, int versionCode, String date, String filter) {

        mRows = new ArrayList<>();

        mVersionName = versionName;
        mVersionCode = versionCode;
        mDate = date;
        mFilter = filter;
    }

    public void add(ItemRow row) {
        mRows.add(row);
    }

    @Override
    public final ChangelogRecyclerViewAdapter.Type getRecyclerViewType() {
        return ChangelogRecyclerViewAdapter.Type.Header;
    }

    @Override
    public final int getLayoutId() {
        return R.layout.changelog_header;
    }
}
