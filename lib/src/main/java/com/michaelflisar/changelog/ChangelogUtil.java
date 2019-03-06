package com.michaelflisar.changelog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Pair;

import com.michaelflisar.changelog.interfaces.IChangelogEntry;
import com.michaelflisar.changelog.interfaces.IChangelogFilter;
import com.michaelflisar.changelog.interfaces.IHeader;
import com.michaelflisar.changelog.interfaces.IRecyclerViewItem;
import com.michaelflisar.changelog.interfaces.IRow;
import com.michaelflisar.changelog.items.ItemMore;
import com.michaelflisar.changelog.items.ItemRelease;

import java.util.ArrayList;
import java.util.HashMap;
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
     * @param expandIfNoSummary    true, if a release without a summary entry should expand it's items instead of showing a "show more" button, false otherwise
     * @return the filtered list of items
     */
    public static List<IRecyclerViewItem> filterItems(int minimumVersionToShow, IChangelogFilter filter, List<IRecyclerViewItem> rows, boolean showSummary, boolean expandIfNoSummary) {

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

            ArrayList<Pair<IHeader, ArrayList<IRecyclerViewItem>>> headers = new ArrayList<>();

            ItemMore currentMoreItem;// = new ItemMore(new ArrayList<>());
//            List<ItemMore> moreItems = new ArrayList<>();
//            moreItems.add(currentMoreItem);

            // 1) create list of header - list of items pairs
            IHeader currentHeader;// = null;
            ArrayList<IRecyclerViewItem> currentHeaderList = null;
            for (int i = 0; i < rowsToAdd.size(); i++) {
                // if we find a header, we put it into the header map
                if (rowsToAdd.get(i) instanceof IHeader) {
                    currentHeader = (IHeader) rowsToAdd.get(i);
                    currentHeaderList = new ArrayList<>();
                    headers.add(new Pair<>(currentHeader, currentHeaderList));
                }
                // else add the item to the current header
                else {
                    currentHeaderList.add(rowsToAdd.get(i));
                }
            }

            // 2) create new list and add header + more items (or items) to it
            List<IRecyclerViewItem> adjustedRowsToAdd = new ArrayList<>();
            for (int i = 0; i < headers.size(); i++) {
                currentHeader = headers.get(i).first;
                currentHeaderList = headers.get(i).second;

                adjustedRowsToAdd.add(currentHeader);
                // either add items of header
                if (expandIfNoSummary && !containsSummaryItem(currentHeaderList)) {
                    adjustedRowsToAdd.addAll(currentHeaderList);
                }
                // or add summary items only and add a show more button
                else {
                    adjustedRowsToAdd.addAll(getSummaryItems(currentHeaderList, true));
                    if (containsNonSummaryItem(currentHeaderList))
                        adjustedRowsToAdd.add(new ItemMore(getSummaryItems(currentHeaderList, false)));
                }
            }

            rowsToAdd = adjustedRowsToAdd;
        }

        // 4) remove empty headers
        for (int i = rowsToAdd.size() - 2; i >= 0; i--) {
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

    // -------------------------
    // internal helper functions
    // -------------------------

    private static boolean containsSummaryItem(List<IRecyclerViewItem> items) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof IRow && ((IRow) items.get(i)).isSummary()) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsNonSummaryItem(List<IRecyclerViewItem> items) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof IRow && !((IRow) items.get(i)).isSummary()) {
                return true;
            }
        }
        return false;
    }

    private static List<IRecyclerViewItem> getSummaryItems(List<IRecyclerViewItem> items, boolean isSummary) {
        ArrayList<IRecyclerViewItem> result = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof IRow && ((IRow) items.get(i)).isSummary() == isSummary) {
                result.add(items.get(i));
            }
        }
        return result;
    }
}
