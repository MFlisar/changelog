package com.michaelflisar.changelog.internal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogUtil;
import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.classes.IChangelogRateHandler;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getContext().getString(R.string.changelog_dialog_title, ChangelogUtil.getAppVersionName(getContext())))
                .setPositiveButton(getContext().getString(R.string.changelog_dialog_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        if (mBuilder.isUseRateButton()) {
            builder.setNeutralButton(getContext().getString(R.string.changelog_dialog_rate), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean handled = false;
                    Object target = getTargetFragment();
                    if (target != null) {
                        handled = onUserWantsToRate(target);
                    }

                    if (handled)
                        return;

                    target = getActivity();
                    if (target != null) {
                        handled = onUserWantsToRate(target);
                    }

                    if (handled)
                        return;

                    openPlayStore(getActivity());
                }
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
        if (target instanceof IChangelogRateHandler)
            return ((IChangelogRateHandler)target).onRateButtonClicked();
        return false;
    }

    private void openPlayStore(Context context) {
        if (context == null)
            return;

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