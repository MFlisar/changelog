package com.michaelflisar.changelog;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.michaelflisar.changelog.classes.IRecyclerViewItem;
import com.michaelflisar.changelog.internal.ChangelogActivity;
import com.michaelflisar.changelog.internal.ChangelogDialogFragment;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogBuilder implements Parcelable {

    private ChangelogSetup mSetup;

    public ChangelogBuilder(ChangelogSetup setup) {
        mSetup = setup;
    }

    // ------------------------
    // Parcelable
    // ------------------------

    ChangelogBuilder(Parcel in) {
        mSetup = in.readParcelable(ChangelogSetup.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mSetup, 0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ChangelogBuilder createFromParcel(Parcel in) {
            return new ChangelogBuilder(in);
        }

        public ChangelogBuilder[] newArray(int size) {
            return new ChangelogBuilder[size];
        }
    };

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
            return ChangelogParserUtil.readChangeLogFile(context, mSetup.getXmlFileId());
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
        ChangelogRecyclerViewAdapter adapter = new ChangelogRecyclerViewAdapter(recyclerView.getContext(), mSetup, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
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
        ChangelogDialogFragment dlg = ChangelogDialogFragment.create(this, darkTheme);
        dlg.show(activity.getSupportFragmentManager(), ChangelogDialogFragment.class.getName());
        return dlg;
    }

    /**
     * build the changelog class which will read the xml file
     * and show it in an activity
     *
     * @param context the context to use to start the activity
     */
    public void buildAndStartActivity(Context context) {
        buildAndStartActivity(context, null);
    }

    /**
     * build the changelog class which will read the xml file
     * and show it in an activity
     *
     * @param context the context to use to start the activity
     * @param theme   theme id or null for light default theme
     */
    public void buildAndStartActivity(Context context, Integer theme) {
        Intent intent = ChangelogActivity.createIntent(context, this, theme);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // ------------------------
    // helper method
    // ------------------------

    public List<IRecyclerViewItem> getRecyclerViewItems(Context context) {
        Changelog changelog = build(context);
        List<IRecyclerViewItem> items = changelog.getAllRecyclerViewItems();
        items = ChangelogUtil.filterItems(mSetup.getMinVersionToShow(), mSetup.getFilter(), items);
        return items;
    }

    public ChangelogRecyclerViewAdapter setupEmptyRecyclerView(RecyclerView recyclerView) {
        ChangelogRecyclerViewAdapter adapter = new ChangelogRecyclerViewAdapter(recyclerView.getContext(), mSetup, new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return adapter;
    }
}
