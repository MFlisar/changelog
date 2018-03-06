package com.michaelflisar.changelog.internal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogUtil;
import com.michaelflisar.changelog.R;
import com.michaelflisar.changelog.classes.IRecyclerViewItem;

import java.util.List;

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
    private ParseAsyncTask mAsyncTask = null;

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

        View v = getActivity().getLayoutInflater().inflate(R.layout.changelog_dialog, null, false);
        ProgressBar pb = v.findViewById(R.id.pbLoading);
        RecyclerView rv = v.findViewById(R.id.rvChangelog);
        ChangelogRecyclerViewAdapter adapter = mBuilder.setupEmptyRecyclerView(rv);

        mAsyncTask = new ParseAsyncTask(getContext(), pb, adapter, mBuilder);
        mAsyncTask.execute();

        builder.setView(v);

        return builder.create();
    }

    @Override
    public void onDestroy() {
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
        super.onDestroy();
    }

    protected class ParseAsyncTask extends AsyncTask<Void, Void, List<IRecyclerViewItem>> {

        private Context mContext;
        private ProgressBar mPbLoading;
        private ChangelogRecyclerViewAdapter mAdapter;
        private ChangelogBuilder mBuilder;

        public ParseAsyncTask(Context context, ProgressBar pbLoading, ChangelogRecyclerViewAdapter adapter, ChangelogBuilder builder) {
            mContext = context;
            mPbLoading = pbLoading;
            mAdapter = adapter;
            mBuilder = builder;
        }

        @Override
        protected List<IRecyclerViewItem> doInBackground(Void... params) {
            try {
                if (mBuilder != null) {
                    return mBuilder.getRecyclerViewItems(mContext);
                }
            } catch (Exception e) {
                Log.e(Constants.DEBUG_TAG, "Exception occured while building changelog's RecyclerView items", e);
            }
            return null;
        }

        protected void onPostExecute(List<IRecyclerViewItem> result) {
            if (result != null) {
                mAdapter.setItems(result);
            }
            mPbLoading.setVisibility(View.GONE);
        }
    }
}