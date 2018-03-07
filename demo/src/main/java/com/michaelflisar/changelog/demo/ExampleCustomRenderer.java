package com.michaelflisar.changelog.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.TypedValue;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.classes.ChangelogRenderer;
import com.michaelflisar.changelog.classes.Release;
import com.michaelflisar.changelog.classes.Row;

/**
 * Created by flisar on 07.03.2018.
 */

// implement IChangelogRenderer for a complete custom renderer!
// here we just extent the default renderer and change some small things for demonstration purposes
public class ExampleCustomRenderer extends ChangelogRenderer {

    @Override
    public void bindHeader(Context context, ViewHolderHeader viewHolder, Release release, ChangelogBuilder builder) {
        //--------------
        // default rendering
        //--------------

        super.bindHeader(context, viewHolder, release, builder);

        //--------------
        // Customising
        //--------------

        // change text sizes, style and colors
        viewHolder.getTvVersion().setTextColor(Color.RED);
        viewHolder.getTvVersion().setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        viewHolder.getTvVersion().setAllCaps(true);
        viewHolder.getTvVersion().setTypeface(null, Typeface.BOLD_ITALIC);

        // append the version code to the version name
        viewHolder.getTvVersion().setText(String.format("%s (%d)", viewHolder.getTvVersion().getText(), release.getVersionCode()));

        viewHolder.getTvDate().setTextColor(Color.GREEN);
    }

    @Override
    public void bindRow(Context context, ViewHolderRow viewHolder, Row row, ChangelogBuilder builder) {
        //--------------
        // default rendering
        //--------------
        super.bindRow(context, viewHolder, row, builder);

        //--------------
        // Customising
        //--------------

        // change text sizes, style and colors
        viewHolder.getTvText().setTextColor(Color.BLUE);
        viewHolder.getTvText().setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        // change bullet list text
        viewHolder.getTvBullet().setText("* ");
    }

    // ------------------------
    // Parcelable
    // ------------------------

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ExampleCustomRenderer createFromParcel(Parcel in) {
            return new ExampleCustomRenderer();
        }

        public ExampleCustomRenderer[] newArray(int size) {
            return new ExampleCustomRenderer[size];
        }
    };
}
