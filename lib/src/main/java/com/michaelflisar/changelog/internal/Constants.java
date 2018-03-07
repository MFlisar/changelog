package com.michaelflisar.changelog.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flisar on 05.03.2018.
 */

public class Constants {
    public static final String DEBUG_TAG = "Changelog Library";

    // XML Tags
    public static final String XML_ROOT_TAG = "changelog";
    public static final String XML_RELEASE_TAG = "release";

    // XML Attributes
    public static final String XML_ATTR_VERSION_CODE = "versionCode";
    public static final String XML_ATTR_VERSION_NAME = "versionName";
    public static final String XML_ATTR_DATE = "date";
    public static final String XML_ATTR_FILTER = "filter";
    public static final String XML_ATTR_TITLE = "title";
}

// TODO: Beispiel
// <?xml version="1.0" encoding="utf-8"?>
// <changelog>
//
//     <release versionCode="153" versionName="v1.53" date="2018-03-05" filter="normal">
//
//         <info filter="normal">Some info</info>
//         <improvement filter="normal">Some improvement</improvement>
//         <bugfix filter="normal">Some bugfix</bugfix>
//
//     </release>
//
// </changelog>

