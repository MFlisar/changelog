package com.michaelflisar.changelog.interfaces;

import android.os.Parcelable;

import com.michaelflisar.changelog.items.ItemRow;

import java.util.Comparator;

/**
 * Created by flisar on 08.03.2018.
 */

public interface IChangelogSorter extends Comparator<ItemRow>, Parcelable {
}
