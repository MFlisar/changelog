package com.michaelflisar.changelog;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.michaelflisar.changelog.classes.ChangelogRenderer;
import com.michaelflisar.changelog.classes.DefaultAutoVersionNameFormatter;
import com.michaelflisar.changelog.interfaces.IAutoVersionNameFormatter;
import com.michaelflisar.changelog.interfaces.IChangelogFilter;
import com.michaelflisar.changelog.interfaces.IChangelogRateHandler;
import com.michaelflisar.changelog.interfaces.IChangelogRenderer;
import com.michaelflisar.changelog.interfaces.IChangelogSorter;
import com.michaelflisar.changelog.interfaces.IRecyclerViewItem;
import com.michaelflisar.changelog.internal.ChangelogActivity;
import com.michaelflisar.changelog.internal.ChangelogDialogFragment;
import com.michaelflisar.changelog.internal.ChangelogPreferenceUtil;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;
import com.michaelflisar.changelog.internal.Constants;
import com.michaelflisar.changelog.internal.ParcelUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogBuilder implements Parcelable {

    // ----------------------
    // Setup
    // ----------------------

    private int mMinVersionToShow;
    private boolean mUseBulletList;
    private IChangelogFilter mFilter;
    private IChangelogSorter mSorter;
    private IChangelogRenderer mRenderer;
    private IAutoVersionNameFormatter mAutoVersionNameFormatter;
    private boolean mRateButton;
    private boolean mShowSummary;
    private String mCustomTitle;
    private String mCustomOkLabel;
    private String mCustomRateLabel;

    private int mXmlFileId;

    private boolean mManagedShowOnStart;

    public ChangelogBuilder() {
        initDefaults();
    }

    private void initDefaults() {
        // default values
        mMinVersionToShow = -1; // show all
        mUseBulletList = false; // no bullet list
        // custom interfaces
        mFilter = null;
        mSorter = null;
        mRenderer = new ChangelogRenderer();
        mAutoVersionNameFormatter = new DefaultAutoVersionNameFormatter(DefaultAutoVersionNameFormatter.Type.MajorMinor, "");
        // file id
        mXmlFileId = R.raw.changelog;
        // manage versions to show in preferences
        mManagedShowOnStart = false;
        mRateButton = false;
        // summary
        mShowSummary = false;
        // customisation
        mCustomTitle = null;
        mCustomOkLabel = null;
        mCustomRateLabel = null;
    }

    // ------------------------
    // Parcelable
    // ------------------------

    ChangelogBuilder(Parcel in) {
        mMinVersionToShow = in.readInt();
        mUseBulletList = ParcelUtil.readBoolean(in);
        mFilter = ParcelUtil.readParcelableNullableInterface(in);
        mSorter = ParcelUtil.readParcelableNullableInterface(in);
        mRenderer = ParcelUtil.readParcelableInterface(in);
        mAutoVersionNameFormatter = ParcelUtil.readParcelableInterface(in);
        mXmlFileId = in.readInt();
        mManagedShowOnStart = ParcelUtil.readBoolean(in);
        mRateButton = ParcelUtil.readBoolean(in);
        mShowSummary = ParcelUtil.readBoolean(in);
        mCustomTitle = in.readString();
        mCustomOkLabel = in.readString();
        mCustomRateLabel = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMinVersionToShow);
        ParcelUtil.writeBoolean(dest, mUseBulletList);
        ParcelUtil.writeParcelableNullableInterface(dest, mFilter);
        ParcelUtil.writeParcelableNullableInterface(dest, mSorter);
        ParcelUtil.writeParcelableInterface(dest, mRenderer);
        ParcelUtil.writeParcelableInterface(dest, mAutoVersionNameFormatter);
        dest.writeInt(mXmlFileId);
        ParcelUtil.writeBoolean(dest, mManagedShowOnStart);
        ParcelUtil.writeBoolean(dest, mRateButton);
        ParcelUtil.writeBoolean(dest, mShowSummary);
        dest.writeString(mCustomTitle);
        dest.writeString(mCustomOkLabel);
        dest.writeString(mCustomRateLabel);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ChangelogBuilder createFromParcel(Parcel in) {
            return new ChangelogBuilder(in);
        }

        public ChangelogBuilder[] newArray(int size) {
            return new ChangelogBuilder[size];
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
     * @return sorter object that will do the automatic sorting or null
     */
    public final IChangelogSorter getSorter() {
        return mSorter;
    }

    /**
     * @return renderer object that will do the RecyclerView items rendering
     */
    public final IChangelogRenderer getRenderer() {
        return mRenderer;
    }

    /**
     * @return IAutoVersionNameFormatter object that will do automaitc version name formatting derived from version number
     */
    public final IAutoVersionNameFormatter getAutoDeriveVersionName() {
        return mAutoVersionNameFormatter;
    }

    /**
     * @return the xml file identifier of the changelog file that should be used
     */
    public final int getXmlFileId() {
        return mXmlFileId;
    }

    /*
     * @return true if rate button is shown, false otherwise
     */
    public final boolean isUseRateButton() {
        return mRateButton;
    }

    /*
     * @return true if a summary should be shown, false otherwise
     */
    public final boolean isShowSummary() {
        return mShowSummary;
    }

    /*
     * @return a custom title string or null
     */
    public final String getCustomTitle() {
        return mCustomTitle;
    }

    /*
     * @return a custom ok button string or null
     */
    public final String getCustomOkLabel() {
        return mCustomOkLabel;
    }

    /*
     * @return a custom rate button string or null
     */
    public final String getCustomRateLabel() {
        return mCustomRateLabel;
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
    public ChangelogBuilder withMinVersionToShow(int minVersionToShow) {
        mMinVersionToShow = minVersionToShow;
        return this;
    }

    /**
     * enables or disables bullets before each changelog row
     *
     * @param useBulletList true to enable bullets, false otherwise
     * @return this
     */
    public ChangelogBuilder withUseBulletList(boolean useBulletList) {
        mUseBulletList = useBulletList;
        return this;
    }

    /**
     * provide a custom filter class or @{@link com.michaelflisar.changelog.classes.ChangelogFilter} to filter the changelog
     *
     * @param filter the filter object that will handle filtering if provided
     * @return this
     */
    public ChangelogBuilder withFilter(IChangelogFilter filter) {
        mFilter = filter;
        return this;
    }

    /**
     * provide a custom filter class or @{@link com.michaelflisar.changelog.classes.ImportanceChangelogSorter} to sort the changelog
     *
     * @param sorter the sorter object that will handle sorting if provided
     * @return this
     */
    public ChangelogBuilder withSorter(IChangelogSorter sorter) {
        mSorter = sorter;
        return this;
    }

    /**
     * provide a custom renderer class or @{@link com.michaelflisar.changelog.classes.ChangelogRenderer} to filter the changelog
     *
     * @param renderer the renderer object that will handle rendering RecyclerView items
     * @return this
     */
    public ChangelogBuilder withRenderer(IChangelogRenderer renderer) {
        mRenderer = renderer;
        return this;
    }

    /**
     * provide a custom auto version name formatter class or @{@link IAutoVersionNameFormatter}
     * to format version codes as desired if they are not provided in the xml
     *
     * @param versionNameFormatter the formatter object that will handle deriving version name from version number
     * @return this
     */
    public ChangelogBuilder withVersionNameFormatter(IAutoVersionNameFormatter versionNameFormatter) {
        mAutoVersionNameFormatter = versionNameFormatter;
        return this;
    }

    /**
     * set's a custom changelog xml file id
     *
     * @param xmlFileIdentifier the changelog xml file id
     * @return this
     */
    public ChangelogBuilder withXmlFile(int xmlFileIdentifier) {
        mXmlFileId = xmlFileIdentifier;
        return this;
    }

    /**
     * if enabled, changelog library will save a boolena flag in a preference file that saves the last shown changelog version
     * and will only show a dialog / activity, if new changelogs are found and won't do anything otherwise
     *
     * ATTENTION: this will override the min version filter as it will calculate this version automatically!
     *
     * @param managedShowOnStart true, if changelog library should show only not already shown chnagelogs, false otherwise
     * @return this
     */
    public ChangelogBuilder withManagedShowOnStart(boolean managedShowOnStart) {
        mManagedShowOnStart = managedShowOnStart;
        return this;
    }

    /**
     * if enabled, a rate button is shown in the changelog dialog
     * DEFAULT BEHAVIOUR: clicking the button will open the play store link
     * CUSTOM BEHAVIOUR: the target fragment or the parent activity should implement {@link IChangelogRateHandler}, which will be called if the rate button is clicked
     * ONLY WORKS WITH DIALOG MODE!
     *
     * @param rateButton true, if the rate button should be shown, false otherwise
     * @return this
     */
    public ChangelogBuilder withRateButton(boolean rateButton) {
        mRateButton = rateButton;
        return this;
    }

    /**
     * use this to show a summary and a "show more" button instead of showing all entries
     * ATTENTION: Summary entries will ALWAYS be shown before other entries; sorting is applied to entries and the rest individually!
     *
     * @param showSummary true, if you want to show a summary, false otherwise
     * @return this
     */
    public ChangelogBuilder withSummary(boolean showSummary) {
        mShowSummary = showSummary;
        return this;
    }

    /**
     * provide a custom activity/dialog title
     *
     * @param title the custom activity/dialog title
     * @return this
     */
    public ChangelogBuilder withTitle(String title) {
        mCustomTitle = title;
        return this;
    }

    /**
     * provide a custom ok button text
     *
     * @param text the custom ok button text
     * @return this
     */
    public ChangelogBuilder withOkButtonLabel(String text) {
        mCustomOkLabel = text;
        return this;
    }

    /**
     * provide a custom rate button text
     *
     * @param text the custom rate button text
     * @return this
     */
    public ChangelogBuilder withRateButtonLabel(String text) {
        mCustomRateLabel = text;
        return this;
    }

    // ------------------------
    // build method
    // ------------------------

    /**
     * build the changelog class which will read the xml file
     *
     * @param context the context
     * @return the changelog class
     */
    public Changelog build(Context context) {
        try {
            return ChangelogParserUtil.readChangeLogFile(context, mXmlFileId, mAutoVersionNameFormatter, mSorter);
        } catch (Exception e) {
            // crash the app, something is not workking and should be fixed
            throw new RuntimeException(e);
        }
    }

    /**
     * build the changelog class which will read the xml file
     * and setup the provider RecyclerView and display the items
     *
     * @param recyclerView the RecyclerView which should display the changelog
     */
    public void buildAndSetup(RecyclerView recyclerView) {
        List<IRecyclerViewItem> items = getRecyclerViewItems(recyclerView.getContext());
        ChangelogRecyclerViewAdapter adapter = new ChangelogRecyclerViewAdapter(recyclerView.getContext(), this, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    /**
     * build the changelog class which will read the xml file
     * and show it in a dialog
     *
     * @param activity  the parent activity of the dialog
     * @param darkTheme true, if dialog should use dark theme, false otherwise
     * @return the DialogFragment
     */
    public ChangelogDialogFragment buildAndShowDialog(AppCompatActivity activity, boolean darkTheme) {
        boolean shouldShow = checkShouldShowAndUpdateMinVersion(activity);
        ChangelogDialogFragment dlg = null;
        if (shouldShow) {
            dlg = ChangelogDialogFragment.create(this, darkTheme);
            dlg.show(activity.getSupportFragmentManager(), ChangelogDialogFragment.class.getName());

        } else {
            Log.i(Constants.DEBUG_TAG, "Showing changelog dialog skipped");
        }
        ChangelogPreferenceUtil.updateAlreadyShownChangelogVersion(activity);
        return dlg;
    }

    /**
     * build the changelog class which will read the xml file
     * and show it in an activity
     *
     * @param appThemeHasActionBar true, if the apps theme contains a toolbar, false if not
     * @param context              the context to use to start the activity
     */
    public void buildAndStartActivity(Context context, boolean appThemeHasActionBar) {
        buildAndStartActivity(context, null, appThemeHasActionBar);
    }

    /**
     * build the changelog class which will read the xml file
     * and show it in an activity
     *
     * @param context           the context to use to start the activity
     * @param theme             theme id or null for light default theme
     * @param themeHasActionBar true, if the theme (or the app theme if no theme is provided) contains a toolbar, false if not
     */
    public void buildAndStartActivity(Context context, Integer theme, boolean themeHasActionBar) {
        boolean shouldShow = checkShouldShowAndUpdateMinVersion(context);
        if (shouldShow) {
            Intent intent = ChangelogActivity.createIntent(context, this, theme, themeHasActionBar);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Log.i(Constants.DEBUG_TAG, "Showing changelog activity skipped");
        }
        updateManagedLastShownVersion(context);
    }

    // ------------------------
    // helper method
    // ------------------------

    private final boolean checkShouldShowAndUpdateMinVersion(Context context) {
        if (!mManagedShowOnStart) {
            return true;
        }
        Integer autoMinVersionToShow = ChangelogPreferenceUtil.shouldShowChangelogOnStart(context);
        // only overwrite min version if unseen version is higher than user selected min version
        if (autoMinVersionToShow != null && autoMinVersionToShow > mMinVersionToShow) {
            withMinVersionToShow(autoMinVersionToShow);
        }

        return autoMinVersionToShow != null;
    }

    private final void updateManagedLastShownVersion(Context context) {
        if (mManagedShowOnStart) {
            ChangelogPreferenceUtil.updateAlreadyShownChangelogVersion(context);
        }
    }

    public List<IRecyclerViewItem> getRecyclerViewItems(Context context) {
        Changelog changelog = build(context);
        List<IRecyclerViewItem> items = changelog.getAllRecyclerViewItems();
        items = ChangelogUtil.filterItems(mMinVersionToShow, mFilter, items, mShowSummary);
        return items;
    }

    public ChangelogRecyclerViewAdapter setupEmptyRecyclerView(RecyclerView recyclerView) {
        ChangelogRecyclerViewAdapter adapter = new ChangelogRecyclerViewAdapter(recyclerView.getContext(), this, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return adapter;
    }


}
