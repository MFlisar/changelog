package com.michaelflisar.changelog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.michaelflisar.changelog.interfaces.IChangelogEntry;
import com.michaelflisar.changelog.interfaces.IChangelogFilter;
import com.michaelflisar.changelog.interfaces.IHeader;
import com.michaelflisar.changelog.interfaces.IRecyclerViewItem;
import com.michaelflisar.changelog.interfaces.IRow;
import com.michaelflisar.changelog.items.ItemMore;
import com.michaelflisar.changelog.items.ItemRelease;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogUtil {

    /**
     * returns the app version name
     *
     * @param context context to use to retrieve the app version name
     * @return the app version name
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * returns the app version name
     *
     * @param context context to use to retrieve the app version code
     * @return the app version code
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * @param minimumVersionToShow if >0, filters release notes that are meant for version <= this number
     * @param filter               a custom filter
     * @param rows                 list of all items of the changelog that needs to be filtered
     * @param showSummary          true, if summary items should be shown in combination with a "show more" button
     * @return the filtered list of items
     */
    public static List<IRecyclerViewItem> filterItems(int minimumVersionToShow, IChangelogFilter filter, List<IRecyclerViewItem> rows, boolean showSummary) {

        List<IRecyclerViewItem> rowsToAdd = new ArrayList<>();
        // 1) add all rows that fulfill the min version filter
        if (minimumVersionToShow > 0) {
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i) instanceof IChangelogEntry && ((IChangelogEntry) rows.get(i)).getVersionCode() >= minimumVersionToShow) {
                    rowsToAdd.add(rows.get(i));
                }
            }
        } else {
            rowsToAdd.addAll(rows);
        }
        // 2) check filter
        if (filter != null) {
            for (int i = rowsToAdd.size() - 1; i >= 0; i--) {
                if (!filter.checkFilter(rowsToAdd.get(i))) {
                    rowsToAdd.remove(i);
                }
            }
        }
        // 3) create summary
        if (showSummary) {
            ItemMore currentMoreItem = new ItemMore(new ArrayList<>());
            List<ItemMore> moreItems = new ArrayList<>();
            moreItems.add(currentMoreItem);

            for (int i = rowsToAdd.size() - 1; i >= 0; i--) {
                if (rowsToAdd.get(i) instanceof IHeader && i > 0) {
                    currentMoreItem = new ItemMore(new ArrayList<>());
                    moreItems.add(currentMoreItem);
                } else if (rowsToAdd.get(i) instanceof IRow && !((IRow) rowsToAdd.get(i)).isSummary()) {
                    IRecyclerViewItem item = rowsToAdd.remove(i);
                    currentMoreItem.addItem(0, item);
                }
            }

            // add show more button(s)
            int index = 0;
            if (rowsToAdd.size() > 0) {
                rowsToAdd.add(moreItems.get(index++));
            }
            for (int i = rowsToAdd.size() - 2; i >= 1; i--) {
                if (rowsToAdd.get(i + 1) instanceof IHeader) {
                    currentMoreItem = moreItems.get(index++);
                    if (currentMoreItem.getItems().size() > 0) {
                        rowsToAdd.add(i + 1, currentMoreItem);
                    }
                }
            }
        }

        // 4) remove empty headers
        for (int i = rowsToAdd.size() - 2; i >= 1; i--) {
            if (rowsToAdd.get(i) instanceof IHeader && rowsToAdd.get(i + 1) instanceof IHeader) {
                rowsToAdd.remove(i);
            }
        }

        return rowsToAdd;
    }

    /**
     * convert the releases to a list of RecyclerView items
     *
     * @param releases the list of all releases parsed from the xml for example
     * @return a list of RecyclerView items
     */
    public static List<IRecyclerViewItem> getRecyclerViewItems(List<ItemRelease> releases) {
        ArrayList<IRecyclerViewItem> items = new ArrayList<>();
        for (ItemRelease release : releases) {
            items.add(release);
            items.addAll(release.getRows());
        }
        return items;
    }
}
