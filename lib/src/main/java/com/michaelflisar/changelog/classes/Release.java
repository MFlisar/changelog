package com.michaelflisar.changelog.classes;

import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */
public class Release implements IRecyclerViewItem, IHeader {
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

    public final List<Row> getRows() {
        return mRows;
    }

    private final List<Row> mRows;

    public Release(String versionName, int versionCode, String date, String filter) {

        mRows = new ArrayList<>();

        mVersionName = versionName;
        mVersionCode = versionCode;
        mDate = date;
        mFilter = filter;
    }

    public void add(Row row) {
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
