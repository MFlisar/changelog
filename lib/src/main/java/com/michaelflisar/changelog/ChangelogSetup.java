package com.michaelflisar.changelog;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.michaelflisar.changelog.classes.IChangelogFilter;
import com.michaelflisar.changelog.classes.Release;
import com.michaelflisar.changelog.classes.Row;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogSetup implements Parcelable {

    // ----------------------
    // Class
    // ----------------------

    private int mMinVersionToShow;
    private boolean mUseBulletList;
    private IChangelogFilter mFilter;

    private int mRawFileId;

    private int mLayoutHeaderId;
    private int mLayoutRowId;
    private int mLayoutItemBulletId;
    private int mLayoutItemVersionId;
    private int mLayoutItemDateId;
    private int mLayoutItemTextId;

    public ChangelogSetup() {
        // default values
        mMinVersionToShow = -1; // show all
        mUseBulletList = false; // no bullet list
        mFilter = null;
        // layout id
        mRawFileId = R.raw.changelog;
        // default ids
        mLayoutHeaderId = R.layout.changelog_header;
        mLayoutRowId = R.layout.changelog_row;
        mLayoutItemBulletId = R.id.tvBullet;
        mLayoutItemVersionId = R.id.tvHeaderVersion;
        mLayoutItemDateId = R.id.tvHeaderDate;
        mLayoutItemTextId = R.id.tvText;
    }

    public void bindHeader(Context context, ChangelogRecyclerViewAdapter.ViewHolderHeader viewHolder, Release release, ChangelogSetup setup) {
        if (release != null) {
            // 1) update version
            String version = release.getVersionName() != null ? release.getVersionName() : "";
            version = context.getString(R.string.changelog_version_title, version);
            // default layout has a TextView
            ((TextView) viewHolder.viewVersion).setText(version);

            // 2) Update date
            String date = release.getDate() != null ? release.getDate() : "";
            // default layout has a TextView
            ((TextView) viewHolder.viewDate).setText(date);
            viewHolder.viewDate.setVisibility(date.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    public void bindRow(Context context, ChangelogRecyclerViewAdapter.ViewHolderRow viewHolder, Row row, ChangelogSetup setup) {
        if (row != null) {
            // 1) update text
            String text = row.getText(context);
            ((TextView) viewHolder.viewText).setText(Html.fromHtml(text));
            ((TextView) viewHolder.viewText).setMovementMethod(LinkMovementMethod.getInstance());

            // 2) update bullet list item
            viewHolder.viewBullet.setVisibility(setup.isUseBulletList() ? View.VISIBLE : View.GONE);
        }
    }

    // ------------------------
    // Parcelable
    // ------------------------

    ChangelogSetup(Parcel in) {
        mMinVersionToShow = in.readInt();
        mUseBulletList = in.readInt() == 1;
        String clazz = in.readString();
        if (clazz != null) {
            try {
                mFilter = in.readParcelable(Class.forName(clazz).getClassLoader());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            mFilter = null;
        }
        mRawFileId = in.readInt();
        mLayoutHeaderId = in.readInt();
        mLayoutRowId = in.readInt();
        mLayoutItemBulletId = in.readInt();
        mLayoutItemVersionId = in.readInt();
        mLayoutItemDateId = in.readInt();
        mLayoutItemTextId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMinVersionToShow);
        dest.writeInt(mUseBulletList ? 1 : 0);
        if (mFilter != null) {
            dest.writeString(mFilter.getClass().getName());
            dest.writeParcelable(mFilter, 0);
        }
        dest.writeInt(mRawFileId);
        dest.writeInt(mLayoutHeaderId);
        dest.writeInt(mLayoutRowId);
        dest.writeInt(mLayoutItemBulletId);
        dest.writeInt(mLayoutItemVersionId);
        dest.writeInt(mLayoutItemDateId);
        dest.writeInt(mLayoutItemTextId);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ChangelogSetup createFromParcel(Parcel in) {
            return new ChangelogSetup(in);
        }

        public ChangelogSetup[] newArray(int size) {
            return new ChangelogSetup[size];
        }
    };

    // -----------------
    // Getter
    // -----------------

    public final int getMinVersionToShow() {
        return mMinVersionToShow;
    }

    public final boolean isUseBulletList() {
        return mUseBulletList;
    }

    public final IChangelogFilter getFilter() {
        return mFilter;
    }

    public final int getRawFileId() {
        return mRawFileId;
    }

    public final int getLayoutHeaderId() {
        return mLayoutHeaderId;
    }

    public final int getLayoutRowId() {
        return mLayoutRowId;
    }

    public final int getLayoutItemBulletId() {
        return mLayoutItemBulletId;
    }

    public final int getLayoutItemVersionId() {
        return mLayoutItemVersionId;
    }

    public final int getLayoutItemDateId() {
        return mLayoutItemDateId;
    }

    public final int getLayoutItemTextId() {
        return mLayoutItemTextId;
    }

    // -----------------
    // Setter
    // -----------------

    void setMinVersionToShow(int minVersionToShow) {
        mMinVersionToShow = minVersionToShow;
    }

    void setUseBulletList(boolean useBulletList) {
        mUseBulletList = useBulletList;
    }

    void setFilter(IChangelogFilter filter) {
        mFilter = filter;
    }

    void setLayoutHeaderId(int layoutHeaderId) {
        mLayoutHeaderId = layoutHeaderId;
    }

    void setLayoutRowId(int layoutRowId) {
        mLayoutRowId = layoutRowId;
    }

    void setLayoutItemBulletId(int layoutItemBulletId) {
        mLayoutItemBulletId = layoutItemBulletId;
    }

    void setLayoutItemVersionId(int layoutItemVersionId) {
        mLayoutItemVersionId = layoutItemVersionId;
    }

    void setLayoutItemDateId(int layoutItemDateId) {
        mLayoutItemDateId = layoutItemDateId;
    }

    void setLayoutItemTextId(int layoutItemTextId) {
        mLayoutItemTextId = layoutItemTextId;
    }
}
