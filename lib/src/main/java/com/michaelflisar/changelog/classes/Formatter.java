package com.michaelflisar.changelog.classes;

import android.content.Context;

import com.michaelflisar.changelog.Changelog;
import com.michaelflisar.changelog.R;

/**
 * Created by flisar on 20.04.2017.
 */

public class Formatter {
    public interface IFormatter {
        String formatChangelogRow(Context context, Changelog.Type type, String changeText);
    }

    private static IFormatter FORMATTER = null;

    public static IFormatter getFormatter() {
        if (FORMATTER == null) {
            FORMATTER = new DefaultFormatter();
        }
        return FORMATTER;
    }

    public static void setFormatter(IFormatter formatter) {
        FORMATTER = formatter;
    }

    static class DefaultFormatter implements IFormatter {
        @Override
        public String formatChangelogRow(Context context, Changelog.Type type, String changeText) {
            String prefix = "";
            switch (type) {
                case Info:
                    break;
                case Bug:
                    prefix = context.getResources().getString(R.string.changelog_bug_prefix);
                    prefix = prefix.replaceAll("\\[", "<").replaceAll("\\]", ">");
                    break;
                case Improvement:
                    prefix = context.getResources().getString(R.string.changelog_bug_improvement);
                    prefix = prefix.replaceAll("\\[", "<").replaceAll("\\]", ">");
                    break;
            }
            return prefix + changeText;
        }
    }
}
