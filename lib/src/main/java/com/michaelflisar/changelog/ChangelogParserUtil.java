package com.michaelflisar.changelog;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.michaelflisar.changelog.interfaces.IAutoVersionNameFormatter;
import com.michaelflisar.changelog.interfaces.IChangelogSorter;
import com.michaelflisar.changelog.items.ItemRelease;
import com.michaelflisar.changelog.items.ItemRow;
import com.michaelflisar.changelog.internal.ChangelogException;
import com.michaelflisar.changelog.internal.Constants;
import com.michaelflisar.changelog.tags.IChangelogTag;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by flisar on 05.03.2018.
 */

class ChangelogParserUtil {

    static Changelog readChangeLogFile(Context context, int changelogXmlFileId, IAutoVersionNameFormatter autoVersionNameFormatter, IChangelogSorter sorter) throws Exception {
        Changelog changelog;

        try {
            XmlPullParser parser;

            String resourceType = context.getResources().getResourceTypeName(changelogXmlFileId);
            if (resourceType.equals("raw")) {
                InputStream in = context.getResources().openRawResource(changelogXmlFileId);
                parser = Xml.newPullParser();
                parser.setInput(in, null);
            } else if (resourceType.equals("xml")) {
                parser = context.getResources().getXml(changelogXmlFileId);
            } else
                throw new RuntimeException("Wrong changelog resource type, provide xml or raw resource!");

            // 1) Create Changelog object
            changelog = new Changelog();

            // 2) Parse file into Changelog object
            parseMainNode(parser, changelog, autoVersionNameFormatter);

            // 3) sort changelogs
            changelog.sort(sorter);
        } catch (XmlPullParserException xpe) {
            Log.d(Constants.DEBUG_TAG, "XmlPullParseException while parsing changelog file", xpe);
            throw xpe;
        } catch (IOException ioe) {
            Log.d(Constants.DEBUG_TAG, "IOException with changelog.xml", ioe);
            throw ioe;
        }

        return changelog;
    }

    private static void parseMainNode(XmlPullParser parser, Changelog changelog, IAutoVersionNameFormatter autoVersionNameFormatter) throws Exception {
        // safety checks
        if (parser == null || changelog == null) {
            return;
        }

        // Parse all nested (=release) nodes
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                if (tag.equals(Constants.XML_RELEASE_TAG)) {
                    readReleaseNode(parser, changelog, autoVersionNameFormatter);
                }
            }
            parser.next();
        }
    }

    private static void readReleaseNode(XmlPullParser parser, Changelog changelog, IAutoVersionNameFormatter autoVersionNameFormatter) throws Exception {
        // safety checks
        if (parser == null || changelog == null) {
            return;
        }

        // 1) parse release tag
        parser.require(XmlPullParser.START_TAG, null, Constants.XML_RELEASE_TAG);

        // 2) real all attributes of release tag
        String versionName = parser.getAttributeValue(null, Constants.XML_ATTR_VERSION_NAME);
        String versionCodeAsString = parser.getAttributeValue(null, Constants.XML_ATTR_VERSION_CODE);
        int versionCode = -1;
        if (versionCodeAsString != null) {
            try {
                versionCode = Integer.parseInt(versionCodeAsString);
            } catch (NumberFormatException ne) {
                Log.w(Constants.DEBUG_TAG, String.format("Could not parse versionCode value '%s' to an Integer, found following value: '%s'. Filtering based on versionCode can't work in this case!"));
            }
        }
        String date = parser.getAttributeValue(null, Constants.XML_ATTR_DATE);
        String filter = parser.getAttributeValue(null, Constants.XML_ATTR_FILTER);

        if (versionName == null) {
            versionName = autoVersionNameFormatter.deriveVersionName(versionCode);
        }

        // 3) Create release element and add it to changelog object
        ItemRelease release = new ItemRelease(
                versionName,
                versionCode,
                date,
                filter
        );
        changelog.add(release);

        // 4) Parse all nested tags in release
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            // make sure we understand the tag we found
            String tag = parser.getName();
            IChangelogTag changelogTag = ChangelogSetup.get().findTag(tag);
            if (changelogTag != null) {
                readReleaseRowNode(changelogTag, parser, changelog, release);
            }
        }
    }

    private static void readReleaseRowNode(IChangelogTag changelogTag, XmlPullParser parser, Changelog changelog, ItemRelease release) throws Exception {
        // safety checks
        if (parser == null || changelog == null) {
            return;
        }

        // 1) real all attributes of row tag
        String title = parser.getAttributeValue(null, Constants.XML_ATTR_TITLE);
        String filter = parser.getAttributeValue(null, Constants.XML_ATTR_FILTER);
        boolean isSummary = Constants.XML_VALUE_SUMMARY.equalsIgnoreCase(parser.getAttributeValue(null, Constants.XML_ATTR_TYPE));

        // 2) read content (=text) of row tag
        String text = null;
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.getText();
            if (text == null) {
                throw new ChangelogException("ChangeLogText required in changeLogText node");
            }
            parser.nextTag();
        }

        // 3) create row element and add it to release element
        ItemRow row = new ItemRow(release, changelogTag, title, text, filter, isSummary);
        release.add(row);
    }
}
