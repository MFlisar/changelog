package com.michaelflisar.changelog.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogFilter implements IChangelogFilter {

    public enum Mode {
        Exact,
        Contains,
        NotContains
    }

    private Mode mMode;
    private String mStringToCheck;
    private boolean mEmptyFiltersAreValid;
    private boolean mInheritReleaseFilterToRows;

    public ChangelogFilter(Mode mode, String stringToCheck, boolean emptyFiltersAreValid) {
        this(mode, stringToCheck, emptyFiltersAreValid, true);
    }

    public ChangelogFilter(Mode mode, String stringToCheck, boolean emptyFiltersAreValid, boolean inheritReleaseFilterToRows) {
        mMode = mode;
        mStringToCheck = stringToCheck;
        mEmptyFiltersAreValid = emptyFiltersAreValid;
        mInheritReleaseFilterToRows = inheritReleaseFilterToRows;
    }

    @Override
    public boolean checkFilter(IRecyclerViewItem item) {

        ChangelogRecyclerViewAdapter.Type type = item.getRecyclerViewType();
        String filter = item.getFilter();
        if (filter == null && mInheritReleaseFilterToRows && type == ChangelogRecyclerViewAdapter.Type.Row) {
            filter = ((Row) item).getRelease().getFilter();
        }
        if (filter == null) {
            filter = "";
        }
        if (filter.length() == 0 && mEmptyFiltersAreValid) {
            return true;
        }
        switch (mMode) {
            case Exact:
                return mStringToCheck.equals(filter);
            case Contains:
                return filter != null && filter.contains(mStringToCheck);
            case NotContains:
                return filter == null || !filter.contains(mStringToCheck);
            default:
                return false;
        }
    }

    // ------------------------
    // Parcelable
    // ------------------------

    ChangelogFilter(Parcel in) {
        mMode = Mode.values()[in.readInt()];
        mInheritReleaseFilterToRows = in.readByte() != 0;
        mEmptyFiltersAreValid = in.readByte() != 0;
        mStringToCheck = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMode.ordinal());
        dest.writeByte((byte) (mInheritReleaseFilterToRows ? 1 : 0));
        dest.writeByte((byte) (mEmptyFiltersAreValid ? 1 : 0));
        dest.writeString(mStringToCheck);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ChangelogFilter createFromParcel(Parcel in) {
            return new ChangelogFilter(in);
        }

        public ChangelogFilter[] newArray(int size) {
            return new ChangelogFilter[size];
        }
    };
}
