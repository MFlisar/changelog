package com.michaelflisar.changelog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.michaelflisar.changelog.classes.IChangelogFilter;
import com.michaelflisar.changelog.classes.IRecyclerViewItem;
import com.michaelflisar.changelog.classes.Release;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogUtil {

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static List<IRecyclerViewItem> filterItems(int minimumVersionToShow, IChangelogFilter filter, List<IRecyclerViewItem> rows) {
        if (minimumVersionToShow <= 0 && filter == null) {
            return rows;
        }
        List<IRecyclerViewItem> rowsToAdd = new ArrayList<>();
        // 1) add all rows that fulfill the min version filter
        if (minimumVersionToShow > 0) {
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).getVersionCode() >= minimumVersionToShow) {
                    rowsToAdd.add(rows.get(i));
                }
            }
        } else {
            rowsToAdd.addAll(rows);
        }
        // 2) check filter
        if (filter != null) {
            for (int i = rowsToAdd.size() - 1; i >= 0; i--) {
                if (!filter.checkFilter(rowsToAdd.get(i)))
                    rowsToAdd.remove(i);
            }
        }
        return rowsToAdd;
    }

    public static List<IRecyclerViewItem> getRecyclerViewItems(List<Release> releases) {
        ArrayList<IRecyclerViewItem> items = new ArrayList<>();
        for (Release release : releases) {
            items.add(release);
            items.addAll(release.getRows());
        }
        return items;
    }
}
