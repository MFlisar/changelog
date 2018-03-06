package com.michaelflisar.changelog.internal;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.classes.IRecyclerViewItem;

import java.util.List;

/**
 * Created by flisar on 06.03.2018.
 */

public class ChangelogParserAsyncTask extends AsyncTask<Void, Void, List<IRecyclerViewItem>> {

    private Context mContext;
    private ProgressBar mPbLoading;
    private ChangelogRecyclerViewAdapter mAdapter;
    private ChangelogBuilder mBuilder;

    public ChangelogParserAsyncTask(Context context, ProgressBar pbLoading, ChangelogRecyclerViewAdapter adapter, ChangelogBuilder builder) {
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
        if (mPbLoading != null) {
            mPbLoading.setVisibility(View.GONE);
        }
    }
}
