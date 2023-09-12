package com.microsoft.kapp.activities.settings;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class RunSettingsActivity extends SelectMetricsActivity {
    private RunDisplayMetricType[] mMetrics;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_FITNESS_RUN_DATA_POINTS);
    }

    @Override // com.microsoft.kapp.activities.settings.SelectMetricsActivity
    protected void saveSettings() {
        ArrayList<RunDisplayMetricType> newList = new ArrayList<>();
        RunDisplayMetricType[] defaultRunMetricsArray = EventUtils.getDefaultRunMetricsOrder(this.mStrappSettingsManager.getBandVersion());
        ArrayList<RunDisplayMetricType> remainderList = new ArrayList<>(Arrays.asList(defaultRunMetricsArray));
        for (int i = 0; i < this.mMetricsSpinners.size(); i++) {
            int index = this.mMetricsSpinners.get(i).getSelectedItemPosition();
            RunDisplayMetricType metric = defaultRunMetricsArray[index];
            remainderList.remove(metric);
            newList.add(metric);
        }
        if (this.mStrappSettingsManager.getBandVersion() != BandVersion.NEON) {
            newList.addAll(remainderList);
        }
        RunDisplayMetricType[] newArray = (RunDisplayMetricType[]) newList.toArray(new RunDisplayMetricType[newList.size()]);
        this.mStrappSettingsManager.setTransactionRunMetrics(newArray);
        finish();
    }

    @Override // com.microsoft.kapp.activities.settings.SelectMetricsActivity
    protected void initSpinners() {
        ArrayAdapter<CharSequence> sharedSpinnerAdapter = ArrayAdapter.createFromResource(this, this.mStrappSettingsManager.getRunMetricStrings(), R.drawable.settings_spinner_item);
        sharedSpinnerAdapter.setDropDownViewResource(R.drawable.settings_spinner_dropdown_item);
        instantiateBaseAdapters(sharedSpinnerAdapter);
        this.mMetrics = this.mStrappSettingsManager.getRunMetricsOrder();
        if (this.mStrappSettingsManager.getBandVersion() == BandVersion.NEON) {
            findViewById(R.id.strapp_select_metrics_main_label).setVisibility(0);
            this.mDescriptionText.setText(R.string.strapp_select_run_metrics_description_neon);
            View metricsLayout = findViewById(R.id.drawer_metrics_layout);
            metricsLayout.setVisibility(0);
            instantiateNeonSpinners(sharedSpinnerAdapter);
        }
        for (int i = 0; i < this.mMetricsSpinners.size(); i++) {
            this.mMetricsSpinners.get(i).setSelection(getSpinnerIndexFor(this.mMetrics[i]));
        }
    }

    protected int getSpinnerIndexFor(RunDisplayMetricType metric) {
        return getResourceIndexFor(this, this.mStrappSettingsManager, metric);
    }

    public static int getResourceIndexFor(Context context, StrappSettingsManager manager, RunDisplayMetricType metric) {
        String[] runMetricsArray = context.getResources().getStringArray(manager.getRunMetricStrings());
        List<String> list = Arrays.asList(runMetricsArray);
        switch (metric) {
            case DURATION:
                return list.indexOf(context.getString(R.string.event_metric_duration));
            case HEART_RATE:
                return list.indexOf(context.getString(R.string.event_metric_heart_rate));
            case CALORIES:
                return list.indexOf(context.getString(R.string.event_metric_calories));
            case DISTANCE:
                return list.indexOf(context.getString(R.string.event_metric_distance));
            case PACE:
                return list.indexOf(context.getString(R.string.event_metric_pace));
            case AVERAGE_PACE:
                return list.indexOf(context.getString(R.string.event_metric_average_pace));
            case ELEVATION_GAIN:
                return list.indexOf(context.getString(R.string.event_metric_elevation_gain));
            case TIME:
                return list.indexOf(context.getString(R.string.event_metric_time));
            case NONE:
            default:
                return 0;
        }
    }
}
