package com.michaelflisar.changelog.items;

import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.interfaces.IMore;
import com.michaelflisar.changelog.interfaces.IRecyclerViewItem;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */
public class ItemMore implements IRecyclerViewItem, IMore {

    private final List<IRecyclerViewItem> mItems;

    public ItemMore(List<IRecyclerViewItem> items) {
        mItems = items;
    }

    public void addItem(IRecyclerViewItem item) {
        mItems.add(item);
    }

    public void addItem(int index, IRecyclerViewItem item) {
        mItems.add(index, item);
    }

    @Override
    public final ChangelogRecyclerViewAdapter.Type getRecyclerViewType() {
        return ChangelogRecyclerViewAdapter.Type.More;
    }

    @Override
    public final int getLayoutId() {
        return R.layout.changelog_more;
    }

    @Override
    public List<IRecyclerViewItem> getItems() {
        return mItems;
    }
}
