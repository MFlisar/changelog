package com.michaelflisar.changelog.internal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogUtil;
import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.interfaces.IChangelogRateHandler;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by flisar on 05.03.2018.
 */

public class ChangelogDialogFragment extends DialogFragment {

    public static ChangelogDialogFragment create(ChangelogBuilder changelogBuilder, boolean darkTheme) {
        Bundle args = new Bundle();
        args.putParcelable("builder", changelogBuilder);
        ChangelogDialogFragment dlg = new ChangelogDialogFragment();
        dlg.setStyle(DialogFragment.STYLE_NORMAL, darkTheme ? R.style.ChangelogDialogDarkTheme : R.style.ChangelogDialogLightTheme);
        dlg.setArguments(args);
        return dlg;
    }

    private ChangelogBuilder mBuilder;
    private ChangelogParserAsyncTask mAsyncTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBuilder = getArguments().getParcelable("builder");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = mBuilder.getCustomTitle();
        if (title == null) {
            title = getContext().getString(R.string.changelog_dialog_title, ChangelogUtil.getAppVersionName(getContext()));
        }
        String okButtonText = mBuilder.getCustomOkLabel();
        String rateButtonText = mBuilder.getCustomRateLabel();
        if (okButtonText == null) {
            okButtonText = getContext().getString(R.string.changelog_dialog_button);
        }
        if (rateButtonText == null) {
            rateButtonText = getContext().getString(R.string.changelog_dialog_rate);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(okButtonText, (dialog, which) -> dialog.dismiss());

        if (mBuilder.isUseRateButton()) {
            builder.setNeutralButton(rateButtonText, (dialog, which) -> {
                boolean handled = false;
                Object target = getTargetFragment();
                if (target != null) {
                    handled = onUserWantsToRate(target);
                }

                if (handled) {
                    return;
                }

                target = getActivity();
                if (target != null) {
                    handled = onUserWantsToRate(target);
                }

                if (handled) {
                    return;
                }

                openPlayStore(getActivity());
            });
        }

        View v = getActivity().getLayoutInflater().inflate(R.layout.changelog_dialog, null, false);
        ProgressBar pb = v.findViewById(R.id.pbLoading);
        RecyclerView rv = v.findViewById(R.id.rvChangelog);
        ChangelogRecyclerViewAdapter adapter = mBuilder.setupEmptyRecyclerView(rv);

        mAsyncTask = new ChangelogParserAsyncTask(getContext(), pb, adapter, mBuilder);
        mAsyncTask.execute();

        builder.setView(v);

        return builder.create();
    }

    private boolean onUserWantsToRate(Object target) {
        if (target instanceof IChangelogRateHandler) {
            return ((IChangelogRateHandler) target).onRateButtonClicked();
        }
        return false;
    }

    private void openPlayStore(Context context) {
        if (context == null) {
            return;
        }

        final String appPackageName = context.getPackageName();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
        super.onDestroy();
    }
}