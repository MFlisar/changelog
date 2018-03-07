package com.michaelflisar.changelog.classes;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.R;

/**
 * Created by flisar on 06.03.2018.
 */

public class ChangelogRenderer implements IChangelogRenderer<ChangelogRenderer.ViewHolderHeader, ChangelogRenderer.ViewHolderRow> {

    public ChangelogRenderer() {
    }

    @Override
    public ViewHolderHeader createHeaderViewHolder(LayoutInflater inflater, ViewGroup parent, ChangelogBuilder builder) {
        return new ViewHolderHeader(inflater.inflate(R.layout.changelog_header, parent, false), builder);
    }

    @Override
    public ViewHolderRow createRowViewHolder(LayoutInflater inflater, ViewGroup parent, ChangelogBuilder builder) {
        return new ViewHolderRow(inflater.inflate(R.layout.changelog_row, parent, false), builder);
    }


    @Override
    public void bindHeader(Context context, ViewHolderHeader viewHolder, Release release, ChangelogBuilder builder) {
        if (release != null) {
            // 1) update version
            String version = release.getVersionName() != null ? release.getVersionName() : "";
            version = context.getString(R.string.changelog_version_title, version);
            // default layout has a TextView
            viewHolder.tvVersion.setText(version);

            // 2) Update date
            String date = release.getDate() != null ? release.getDate() : "";
            // default layout has a TextView
            viewHolder.tvDate.setText(date);
            viewHolder.tvDate.setVisibility(date.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void bindRow(Context context, ViewHolderRow viewHolder, Row row, ChangelogBuilder builder) {
        if (row != null) {
            // 1) update text
            String text = row.getText(context);
            viewHolder.tvText.setText(Html.fromHtml(text));
            viewHolder.tvText.setMovementMethod(LinkMovementMethod.getInstance());

            // 2) update bullet list item
            viewHolder.tvBullet.setVisibility(builder.isUseBulletList() ? View.VISIBLE : View.GONE);
        }
    }

    // -------------------------------------------------------------
    // ViewHolder
    // -------------------------------------------------------------

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        private final TextView tvVersion;
        private final TextView tvDate;

        public ViewHolderHeader(View itemView, ChangelogBuilder builder) {
            super(itemView);
            tvVersion = itemView.findViewById(R.id.tvHeaderVersion);
            tvDate = itemView.findViewById(R.id.tvHeaderDate);
        }

        public TextView getTvVersion() {
            return tvVersion;
        }

        public TextView getTvDate() {
            return tvDate;
        }

    }

    public static class ViewHolderRow extends RecyclerView.ViewHolder {

        private final TextView tvText;
        private final TextView tvBullet;

        public ViewHolderRow(View itemView, ChangelogBuilder builder) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            tvBullet = itemView.findViewById(R.id.tvBullet);
        }

        public TextView getTvText() {
            return tvText;
        }

        public TextView getTvBullet() {
            return tvBullet;
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
