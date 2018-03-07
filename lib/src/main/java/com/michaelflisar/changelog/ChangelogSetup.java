package com.michaelflisar.changelog;

import com.michaelflisar.changelog.tags.ChangelogTagBugfix;
import com.michaelflisar.changelog.tags.ChangelogTagInfo;
import com.michaelflisar.changelog.tags.ChangelogTagNew;
import com.michaelflisar.changelog.tags.IChangelogTag;

import java.util.HashSet;

/**
 * Created by flisar on 07.03.2018.
 */

public class ChangelogSetup {

    private final HashSet<IChangelogTag> mValidTags = new HashSet<>();

    private static ChangelogSetup INSTANCE = null;

    private ChangelogSetup() {
        // register default tag types
        mValidTags.add(new ChangelogTagInfo());
        mValidTags.add(new ChangelogTagNew());
        mValidTags.add(new ChangelogTagBugfix());
    }

    public static ChangelogSetup get() {
        if (INSTANCE == null) {
            INSTANCE = new ChangelogSetup();
        }
        return INSTANCE;
    }

    public ChangelogSetup clearTags() {
        mValidTags.clear();
        return this;
    }

    public ChangelogSetup registerTag(IChangelogTag tag) {
        mValidTags.add(tag);
        return this;
    }

    public IChangelogTag findTag(String tag) {
        for (IChangelogTag t : mValidTags) {
            if (t.getXMLTagName().equals(tag)) {
                return t;
            }
        }
        return null;
    }

}
