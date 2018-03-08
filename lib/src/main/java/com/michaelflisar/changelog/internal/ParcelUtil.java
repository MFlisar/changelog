package com.michaelflisar.changelog.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by flisar on 06.03.2018.
 */

public class ParcelUtil {

    public static void writeBoolean(Parcel dest, boolean b) {
        dest.writeByte((byte) (b ? 1 : 0));
    }

    public static boolean readBoolean(Parcel in) {
        return in.readByte() != 0;
    }

    public static void writeParcelableInterface(Parcel dest, Parcelable data) {
        dest.writeString(data.getClass().getCanonicalName());
        dest.writeParcelable(data, 0);
    }

    public static <T extends Parcelable> T readParcelableInterface(Parcel in) {
        String className = in.readString();
        T data = null;
        try {
            Class<?> clazz = Class.forName(className);
            data = in.readParcelable(clazz.getClassLoader());
        } catch (ClassNotFoundException e) {
            Log.e(Constants.DEBUG_TAG, "Error unparceling filter", e);
            throw new RuntimeException(e);
        }
        return data;
    }

    public static void writeParcelableNullableInterface(Parcel dest, Parcelable data) {
        writeBoolean(dest, data != null);
        if (data != null) {
            writeParcelableInterface(dest, data);
        }
    }

    public static <T extends Parcelable> T readParcelableNullableInterface(Parcel in) {
        boolean hasData = readBoolean(in);
        if (hasData) {
            return readParcelableInterface(in);
        } else {
            return null;
        }
    }
}
