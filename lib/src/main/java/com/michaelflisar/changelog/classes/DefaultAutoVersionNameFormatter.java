package com.michaelflisar.changelog.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.michaelflisar.changelog.interfaces.IAutoVersionNameFormatter;

/**
 * Created by flisar on 07.03.2018.
 */

public class DefaultAutoVersionNameFormatter implements IAutoVersionNameFormatter {

    public enum Type {
        MajorMinor,
        MajorMinorPatch
    }

    private Type mType;
    private String mAddon;

    public DefaultAutoVersionNameFormatter(Type type, String addon) {
        mType = type;
        mAddon = addon;
    }

    @Override
    public String deriveVersionName(int versionCode) {
        if (versionCode >= 0) {
            switch (mType) {
                case MajorMinor: {
                    int major = (int) Math.floor((float) versionCode / 100f);
                    int minor = versionCode - major * 100;
                    return "v" + major + "." + String.format("%02d", minor) + mAddon;
                }
                case MajorMinorPatch: {
                    int major = (int) Math.floor((double) ((float) versionCode / 10000f));
                    int minor = (int) Math.floor((double) ((versionCode - major * 10000f) / 100f));
                    int patch = versionCode - major * 10000 - minor * 100;
                    return "v" + major + "." + String.format("%02d", minor) + "." + String.format("%02d", patch) + mAddon;
                }
            }
        }

        // should not happen but we must handle this
        return "v" + versionCode + mAddon;
    }

    // ------------------------
    // Parcelable
    // ------------------------

    DefaultAutoVersionNameFormatter(Parcel in) {
        mType = Type.values()[in.readInt()];
        mAddon = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType.ordinal());
        dest.writeString(mAddon);
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
