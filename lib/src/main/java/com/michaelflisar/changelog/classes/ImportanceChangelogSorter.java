package com.michaelflisar.changelog.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.michaelflisar.changelog.interfaces.IChangelogSorter;
import com.michaelflisar.changelog.items.ItemRow;
import com.michaelflisar.changelog.tags.ChangelogTagBugfix;
import com.michaelflisar.changelog.tags.ChangelogTagInfo;
import com.michaelflisar.changelog.tags.ChangelogTagNew;
import com.michaelflisar.changelog.tags.IChangelogTag;

/**
 * Created by flisar on 08.03.2018.
 */

public class ImportanceChangelogSorter implements IChangelogSorter {

    public ImportanceChangelogSorter() {
    }

    @Override
    public int compare(ItemRow o1, ItemRow o2) {
        return ((Integer) getTagOrderNumber(o1.getTag())).compareTo(getTagOrderNumber(o2.getTag()));
    }

    private int getTagOrderNumber(IChangelogTag tag) {
        if (tag instanceof ChangelogTagNew) {
            return 0;
        } else if (tag instanceof ChangelogTagInfo) {
            return 1;
        } else if (tag instanceof ChangelogTagBugfix) {
            return 2;
        } else {
            return 3;
        }
    }

    // ------------------------
    // Parcelable
    // ------------------------

    ImportanceChangelogSorter(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ImportanceChangelogSorter createFromParcel(Parcel in) {
            return new ImportanceChangelogSorter(in);
        }

        public ImportanceChangelogSorter[] newArray(int size) {
            return new ImportanceChangelogSorter[size];
        }
    };
}
