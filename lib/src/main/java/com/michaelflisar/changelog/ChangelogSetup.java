package com.michaelflisar.changelog;

import android.os.Parcel;
import android.os.Parcelable;

import com.michaelflisar.changelog.classes.ChangelogRenderer;
import com.michaelflisar.changelog.classes.IChangelogFilter;
import com.michaelflisar.changelog.classes.IChangelogRenderer;
import com.michaelflisar.changelog.internal.ParcelUtil;

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
    private IChangelogRenderer mRenderer;

    private int mXmlFileId;

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
        mRenderer = new ChangelogRenderer();
        // layout id
        mXmlFileId = R.xml.changelog;
        // default ids
        mLayoutHeaderId = R.layout.changelog_header;
        mLayoutRowId = R.layout.changelog_row;
        mLayoutItemBulletId = R.id.tvBullet;
        mLayoutItemVersionId = R.id.tvHeaderVersion;
        mLayoutItemDateId = R.id.tvHeaderDate;
        mLayoutItemTextId = R.id.tvText;
    }

    // ------------------------
    // Parcelable
    // ------------------------

    ChangelogSetup(Parcel in) {
        mMinVersionToShow = in.readInt();
        mUseBulletList = ParcelUtil.readBoolean(in);
        boolean hasFilter = ParcelUtil.readBoolean(in);
        if (hasFilter) {
            mFilter = ParcelUtil.readParcelableInterface(in);
        } else {
            mFilter = null;
        }
        mRenderer = ParcelUtil.readParcelableInterface(in);
        mXmlFileId = in.readInt();
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
        ParcelUtil.writeBoolean(dest, mUseBulletList);
        ParcelUtil.writeBoolean(dest, mFilter != null);
        if (mFilter != null) {
           ParcelUtil.writeParcelableInterface(dest, mFilter);
        }
        ParcelUtil.writeParcelableInterface(dest, mRenderer);
        dest.writeInt(mXmlFileId);
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

    /**
     * @return the min version to show (> 0 means filter is active)
     */
    public final int getMinVersionToShow() {
        return mMinVersionToShow;
    }

    /**
     * @return true, if bullets should be shown before each changelog row, false otherwise
     */
    public final boolean isUseBulletList() {
        return mUseBulletList;
    }

    /**
     * @return filter object that will do the filter logic or null
     */
    public final IChangelogFilter getFilter() {
        return mFilter;
    }

    /**
     * @return renderer object that will do the RecyclerView items rendering
     */
    public final IChangelogRenderer getRenderer() {
        return mRenderer;
    }

    /**
     * @return the xml file identifier of the changelog file that should be used
     */
    public final int getXmlFileId() {
        return mXmlFileId;
    }

    /**
     * @return the layout id of the header layout
     */
    public final int getLayoutHeaderId() {
        return mLayoutHeaderId;
    }

    /**
     * @return the layout id of the row layout
     */
    public final int getLayoutRowId() {
        return mLayoutRowId;
    }

    /**
     * @return the layout id of the bullet view
     */
    public final int getLayoutItemBulletId() {
        return mLayoutItemBulletId;
    }

    /**
     * @return the layout id of the version view
     */
    public final int getLayoutItemVersionId() {
        return mLayoutItemVersionId;
    }

    /**
     * @return the layout id of the date view
     */
    public final int getLayoutItemDateId() {
        return mLayoutItemDateId;
    }

    /**
     * @return the layout id of the text view
     */
    public final int getLayoutItemTextId() {
        return mLayoutItemTextId;
    }

    // -----------------
    // Setter
    // -----------------

    /**
     * sets the minimum app version that should be shown, use value <= 0 to disable it
     *
     * @param minVersionToShow the minumum version that should be shown
     * @return this
     */
    public ChangelogSetup withMinVersionToShow(int minVersionToShow) {
        mMinVersionToShow = minVersionToShow;
        return this;
    }

    /**
     * enables or disables bullets before each changelog row
     *
     * @param useBulletList true to enable bullets, false otherwise
     * @return this
     */
    public ChangelogSetup withUseBulletList(boolean useBulletList) {
        mUseBulletList = useBulletList;
        return this;
    }

    /**
     * provide a custom filter class or @{@link com.michaelflisar.changelog.classes.ChangelogFilter} to filter the changelog
     *
     * @param filter the filter object that will handle filtering if provided
     * @return this
     */
    public ChangelogSetup withFilter(IChangelogFilter filter) {
        mFilter = filter;
        return this;
    }

    /**
     * provide a custom renderer class or @{@link com.michaelflisar.changelog.classes.ChangelogRenderer} to filter the changelog
     *
     * @param renderer the renderer object that will handle rendering RecyclerView items
     * @return this
     */
    public ChangelogSetup withRenderer(IChangelogRenderer renderer) {
        mRenderer = renderer;
        return this;
    }

    /**
     * set's a custom changelog xml file id
     *
     * @param xmlFileIdentifier the changelog xml file id
     * @return this
     */
    public ChangelogSetup withXmlFile(int xmlFileIdentifier) {
        mXmlFileId = xmlFileIdentifier;
        return this;
    }

    /**
     * set's a custom layout header id
     *
     * @param layoutHeaderId the header id
     * @return this
     */
    public ChangelogSetup withLayoutHeaderId(int layoutHeaderId) {
        mLayoutHeaderId = layoutHeaderId;
        return this;
    }

    /**
     * set's a custom layout row id
     *
     * @param layoutRowId the row id
     * @return this
     */
    public ChangelogSetup withLayoutRowId(int layoutRowId) {
        mLayoutRowId = layoutRowId;
        return this;
    }

    /**
     * set's a custom bullet view id
     *
     * @param layoutItemBulletId the view id
     * @return this
     */
    public ChangelogSetup withLayoutItemBulletId(int layoutItemBulletId) {
        mLayoutItemBulletId = layoutItemBulletId;
        return this;
    }

    /**
     * set's a custom version view id
     *
     * @param layoutItemVersionId the view id
     * @return this
     */
    public ChangelogSetup withLayoutItemVersionId(int layoutItemVersionId) {
        mLayoutItemVersionId = layoutItemVersionId;
        return this;
    }

    /**
     * set's a custom date view id
     *
     * @param layoutItemDateId the view id
     * @return this
     */
    public ChangelogSetup withLayoutItemDateId(int layoutItemDateId) {
        mLayoutItemDateId = layoutItemDateId;
        return this;
    }

    /**
     * set's a custom text view id
     *
     * @param layoutItemTextId the view id
     * @return this
     */
    public ChangelogSetup withLayoutItemTextId(int layoutItemTextId) {
        mLayoutItemTextId = layoutItemTextId;
        return this;
    }
}
