package com.michaelflisar.changelog.demo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.ChangelogSetup;
import com.michaelflisar.changelog.classes.ChangelogFilter;
import com.michaelflisar.changelog.classes.ImportanceChangelogSorter;
import com.michaelflisar.changelog.demo.databinding.ActivityMainBinding;
import com.michaelflisar.changelog.interfaces.IChangelogRateHandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity implements IChangelogRateHandler {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ----------------------------------------
        // OPTIONAL GLOBAL SETUP
        // should be done ONCE only, preferable in a custom application class!
        // ----------------------------------------

        // register a custom tag
        ChangelogSetup.get().registerTag(new MyCustomXMLTag());

        // ---------
        // Views...
        // ---------

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
        boolean useCustomRenderer = mBinding.cbCustomRenderer.isChecked();
        boolean useSorter = mBinding.cbUseSorter.isChecked();
        boolean rateButton = mBinding.chRateButton.isChecked();
        boolean showSummmary = mBinding.cbShowSummary.isChecked();

        // Changelog
        ChangelogBuilder builder = new ChangelogBuilder()
                // Everything is optional!
                .withUseBulletList(bulletList) // default: false
                .withManagedShowOnStart(managed) // default: false
                .withMinVersionToShow(showVersion11OrHigherOnly ? 110 : -1) // default: -1, will show all version
                .withSorter(useSorter ? new ImportanceChangelogSorter() : null) // default: null, will show the logs in the same order as they are in the xml file
                .withRateButton(rateButton) // default: false
                .withSummary(showSummmary, true) // default: false
                ;

        // add a custom filter if desired
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
        if (stringToFilter != null) {
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
            ChangelogFilter changelogFilter = new ChangelogFilter(filterMode, stringToFilter, true, rowsShouldInheritFilterTextFromReleaseTag);
            builder.withFilter(changelogFilter);
        }

        // add a custom renderer if desired
        if (useCustomRenderer) {
            builder.withRenderer(new ExampleCustomRenderer());
        }

        // finally, show the dialog or the activity
        if (showAsDialog) {
            builder.buildAndShowDialog(this, false);
        } else {
            builder.buildAndStartActivity(this, true);
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

    @Override
    public boolean onRateButtonClicked() {
        Toast.makeText(this, "Rate button was clicked", Toast.LENGTH_LONG).show();
        // button click handled
        return true;
    }
}