package com.michaelflisar.changelog.classes;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

/**
 * Created by flisar on 06.03.2018.
 */

public class ChangelogRenderer implements IChangelogRenderer {

    public ChangelogRenderer() {
    }

    @Override
    public void bindHeader(Context context, ChangelogRecyclerViewAdapter.ViewHolderHeader viewHolder, Release release, ChangelogBuilder builder) {
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

    @Override
    public void bindRow(Context context, ChangelogRecyclerViewAdapter.ViewHolderRow viewHolder, Row row, ChangelogBuilder builder) {
        if (row != null) {
            // 1) update text
            String text = row.getText(context);
            ((TextView) viewHolder.viewText).setText(Html.fromHtml(text));
            ((TextView) viewHolder.viewText).setMovementMethod(LinkMovementMethod.getInstance());

            // 2) update bullet list item
            viewHolder.viewBullet.setVisibility(builder.isUseBulletList() ? View.VISIBLE : View.GONE);
        }
    }

    // ------------------------
    // Parcelable
    // ------------------------

    ChangelogRenderer(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ChangelogRenderer createFromParcel(Parcel in) {
            return new ChangelogRenderer(in);
        }

        public ChangelogRenderer[] newArray(int size) {
            return new ChangelogRenderer[size];
        }
    };
}
