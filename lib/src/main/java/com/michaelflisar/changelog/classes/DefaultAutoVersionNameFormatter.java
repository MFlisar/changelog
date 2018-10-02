package com.michaelflisar.changelog.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.michaelflisar.changelog.interfaces.IAutoVersionNameFormatter;

/**
 * Created by flisar on 07.03.2018.
 */

public class DefaultAutoVersionNameFormatter implements IAutoVersionNameFormatter {

    public DefaultAutoVersionNameFormatter() {
    }

    @Override
    public String deriveVersionName(int versionCode) {
        // should not happen but we must handle this
        if (versionCode < 0) {
            return "v" + versionCode;
        }

        int versionMain = (int) Math.floor((float) versionCode / 100f);
        int versionDetails = versionCode - versionMain * 100;

        return "v" + versionMain + "." + String.format("%02d", versionDetails);
    }

    // ------------------------
    // Parcelable
    // ------------------------

    DefaultAutoVersionNameFormatter(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DefaultAutoVersionNameFormatter createFromParcel(Parcel in) {
            return new DefaultAutoVersionNameFormatter(in);
        }

        public DefaultAutoVersionNameFormatter[] newArray(int size) {
            return new DefaultAutoVersionNameFormatter[size];
        }
    };
}
