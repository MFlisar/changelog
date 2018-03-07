package com.michaelflisar.changelog.classes;

import android.os.Parcelable;

/**
 * Created by flisar on 07.03.2018.
 */

public interface IAutoVersionNameFormatter extends Parcelable {
    String deriveVersionName(int versionCode);
}
