package com.microsoft.kapp.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class SelectMetricsActivity extends BaseActivity {
    public static final String EXTRA_UUID_STRAPPID = "uuid_strappId";
    private ConfirmationBar mConfirmationBar;
    protected TextView mDescriptionText;
    protected TextView mErrorText;
    private FrameLayout mLoadingFrameLayout;
    protected ArrayList<Spinner> mMetricsSpinners = new ArrayList<>();
    @Inject
    SettingsProvider mSettingsProvider;
    @Inject
    StrappSettingsManager mStrappSettingsManager;
    protected UUID mStrappUUID;

    protected abstract void initSpinners();

    protected abstract void saveSettings();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
        if (extras != null) {
            this.mStrappUUID = (UUID) extras.getSerializable("uuid_strappId");
        }
        if (!DefaultStrappUUID.STRAPP_RUN.equals(this.mStrappUUID) && !DefaultStrappUUID.STRAPP_BIKE.equals(this.mStrappUUID)) {
            finish();
            return;
        }
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager");
        setContentView(R.layout.activity_strapp_select_metrics);
        this.mErrorText = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_error_text, TextView.class);
        this.mDescriptionText = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_description, TextView.class);
        this.mConfirmationBar = (ConfirmationBar) ActivityUtils.getAndValidateView(this, R.id.confirmation_bar, ConfirmationBar.class);
        this.mConfirmationBar.setVisibility(0);
        this.mLoadingFrameLayout = (FrameLayout) ActivityUtils.getAndValidateView(this, R.id.loading_frame_layout, FrameLayout.class);
        this.mLoadingFrameLayout.setOnClickListener(null);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.SelectMetricsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (SelectMetricsActivity.this.validateSelection()) {
                    SelectMetricsActivity.this.saveSettings();
                    return;
                }
                SelectMetricsActivity.this.mErrorText.setText(R.string.strapp_select_metrics_save_failed_message);
                SelectMetricsActivity.this.mErrorText.setVisibility(0);
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.SelectMetricsActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                SelectMetricsActivity.this.finish();
            }
        });
        initSpinners();
    }

    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("uuid_strappId", this.mStrappUUID);
    }

    protected final boolean validateSelection() {
        HashSet<Integer> indexes = new HashSet<>();
        Iterator i$ = this.mMetricsSpinners.iterator();
        while (i$.hasNext()) {
            Spinner spinner = i$.next();
            int index = spinner.getSelectedItemPosition();
            if (indexes.contains(Integer.valueOf(index))) {
                return false;
            }
            indexes.add(Integer.valueOf(index));
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public void instantiateBaseAdapters(ArrayAdapter<CharSequence> sharedSpinnerAdapter) {
        this.mMetricsSpinners.add(ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_1_spinner, Spinner.class));
        this.mMetricsSpinners.get(0).setAdapter((SpinnerAdapter) sharedSpinnerAdapter);
        this.mMetricsSpinners.add(ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_2_spinner, Spinner.class));
        this.mMetricsSpinners.get(1).setAdapter((SpinnerAdapter) sharedSpinnerAdapter);
        this.mMetricsSpinners.add(ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_3_spinner, Spinner.class));
        this.mMetricsSpinners.get(2).setAdapter((SpinnerAdapter) sharedSpinnerAdapter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public void instantiateNeonSpinners(ArrayAdapter<CharSequence> sharedSpinnerAdapter) {
        this.mMetricsSpinners.add(ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_4_spinner, Spinner.class));
        this.mMetricsSpinners.get(3).setAdapter((SpinnerAdapter) sharedSpinnerAdapter);
        this.mMetricsSpinners.add(ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_5_spinner, Spinner.class));
        this.mMetricsSpinners.get(4).setAdapter((SpinnerAdapter) sharedSpinnerAdapter);
        this.mMetricsSpinners.add(ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_6_spinner, Spinner.class));
        this.mMetricsSpinners.get(5).setAdapter((SpinnerAdapter) sharedSpinnerAdapter);
        this.mMetricsSpinners.add(ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_7_spinner, Spinner.class));
        this.mMetricsSpinners.get(6).setAdapter((SpinnerAdapter) sharedSpinnerAdapter);
    }
}
