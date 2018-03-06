package com.michaelflisar.changelog;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.michaelflisar.changelog.classes.IChangelogFilter;
import com.michaelflisar.changelog.classes.IRecyclerViewItem;
import com.michaelflisar.changelog.internal.ChangelogDialogFragment;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogBuilder implements Parcelable {

    private ChangelogSetup mSetup;

    public ChangelogBuilder() {
        mSetup = new ChangelogSetup();
    }

    public ChangelogBuilder(ChangelogSetup setup) {
        mSetup = setup;
    }

    // ------------------------
    // Parcelable
    // ------------------------

    ChangelogBuilder(Parcel in) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(in.readString());
            mSetup = in.readParcelable(clazz.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not unparcel setup class!");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSetup.getClass().toString());
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
    // adjust methods
    // ------------------------

    public ChangelogBuilder withMinVersionToShow(int minVersionToShow) {
        mSetup.setMinVersionToShow(minVersionToShow);
        return this;
    }

    public ChangelogBuilder withUseBulletList(boolean useBulletList) {
        mSetup.setUseBulletList(useBulletList);
        return this;
    }

    public ChangelogBuilder withFilter(IChangelogFilter filter) {
        mSetup.setFilter(filter);
        return this;
    }

    // ------------------------
    // build method
    // ------------------------

    public Changelog build(Context context) {
        try {
            return ChangelogParserUtil.readChangeLogFile(context, mSetup.getRawFileId());
        } catch (Exception e) {
            // crash the app, something is not workking and should be fixed
            throw new RuntimeException(e);
        }
    }

    public void buildAndSetup(RecyclerView recyclerView) {
        List<IRecyclerViewItem> items = getRecyclerViewItems(recyclerView.getContext());
        ChangelogRecyclerViewAdapter adapter = new ChangelogRecyclerViewAdapter(recyclerView.getContext(), mSetup, items);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    public ChangelogDialogFragment buildAndShowDialog(AppCompatActivity activity, boolean darkTheme) {
        ChangelogDialogFragment dlg = ChangelogDialogFragment.create(this, darkTheme);
        dlg.show(activity.getSupportFragmentManager(), ChangelogDialogFragment.class.getName());
        return dlg;
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
