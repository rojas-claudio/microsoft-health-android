package com.microsoft.kapp.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import java.util.UUID;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class StrappFitnessEventSettingsActivity extends BaseActivity {
    public static final String EXTRA_UUID_STRAPPID = "uuid_strappId";
    private CustomGlyphView mBackArrow;
    private Button mEditMetrics;
    private TextView mMetric1;
    private TextView mMetric2;
    private TextView mMetric3;
    @Inject
    StrappSettingsManager mStrappSettingsManager;
    protected UUID mStrappUUID;

    protected abstract String getMetricStringResource(int i);

    protected abstract void launchSelectMetricsActivity();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mStrappUUID = (UUID) extras.getSerializable("uuid_strappId");
        }
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager");
        setContentView(R.layout.activity_settings_fitness_events);
        this.mBackArrow = (CustomGlyphView) ActivityUtils.getAndValidateView(this, R.id.back, CustomGlyphView.class);
        this.mBackArrow.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappFitnessEventSettingsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StrappFitnessEventSettingsActivity.this.finish();
            }
        });
        this.mMetric1 = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_settings_selected_metric1, TextView.class);
        this.mMetric2 = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_settings_selected_metric2, TextView.class);
        this.mMetric3 = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_settings_selected_metric3, TextView.class);
        this.mEditMetrics = (Button) ActivityUtils.getAndValidateView(this, R.id.edit_selected_metrics_button, Button.class);
        this.mEditMetrics.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappFitnessEventSettingsActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StrappFitnessEventSettingsActivity.this.launchSelectMetricsActivity();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mMetric1.setText(getMetricStringResource(0));
        this.mMetric2.setText(getMetricStringResource(1));
        this.mMetric3.setText(getMetricStringResource(2));
    }
}
