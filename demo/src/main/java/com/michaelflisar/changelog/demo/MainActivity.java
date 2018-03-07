package com.michaelflisar.changelog.demo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.classes.ChangelogFilter;
import com.michaelflisar.changelog.classes.IChangelogRenderer;
import com.michaelflisar.changelog.classes.Release;
import com.michaelflisar.changelog.classes.Row;
import com.michaelflisar.changelog.demo.databinding.ActivityMainBinding;
import com.michaelflisar.changelog.internal.ChangelogRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        disableEnableControls(false, mBinding.cvCustomFilter);
        mBinding.rgCustomFilter.setOnCheckedChangeListener((group, checkedId) -> {
            disableEnableControls(checkedId != R.id.rbAll, mBinding.cvCustomFilter);
        });

        mBinding.btShowChangelog.setOnClickListener(view -> {
            showChangelog();
        });
    }

    private void showChangelog() {
        boolean showAsDialog = mBinding.cbShowAsDialog.isChecked();
        boolean bulletList = mBinding.cbBullets.isChecked();
        boolean showVersion11OrHigherOnly = mBinding.cbFilterVersion11OrHigher.isChecked();
        boolean rowsShouldInheritFilterTextFromReleaseTag = mBinding.cbInheritFilter.isChecked();
        boolean managed = mBinding.cbManaged.isChecked();

        String stringToFilter = null;
        int rgCustomFilterCheckedItemId = mBinding.rgCustomFilter.getCheckedRadioButtonId();
        switch (rgCustomFilterCheckedItemId) {
            case R.id.rbAll:
                break;
            case R.id.rbCats:
                stringToFilter = "cats";
                break;
            case R.id.rbDogs:
                stringToFilter = "dogs";
                break;
        }

        ChangelogFilter.Mode filterMode = null;
        int rgCustomFilterModeCheckedItemId = mBinding.rgCustomFilterMode.getCheckedRadioButtonId();
        switch (rgCustomFilterModeCheckedItemId) {
            case R.id.rbExact:
                filterMode = ChangelogFilter.Mode.Exact;
                break;
            case R.id.rbContains:
                filterMode = ChangelogFilter.Mode.Contains;
                break;
            case R.id.rbNotContains:
                filterMode = ChangelogFilter.Mode.NotContains;
                break;
        }

        // Changelog
        ChangelogBuilder builder = new ChangelogBuilder()
                .withUseBulletList(bulletList)
                .withManagedShowOnStart(managed);
        if (showVersion11OrHigherOnly) {
            builder.withMinVersionToShow(110);
        }
        if (stringToFilter != null) {
            ChangelogFilter changelogFilter = new ChangelogFilter(filterMode, stringToFilter, true, rowsShouldInheritFilterTextFromReleaseTag);
            builder.withFilter(changelogFilter);
        }
        if (showAsDialog) {
            builder.buildAndShowDialog(this, false);
        } else {
            builder.buildAndStartActivity(this);
        }
    }

    private void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }
}