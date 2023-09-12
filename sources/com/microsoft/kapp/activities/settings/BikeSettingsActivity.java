package com.microsoft.kapp.activities.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.kapp.utils.StringUtils;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class BikeSettingsActivity extends SelectMetricsActivity {
    private static int CARGO_BIKE_METRICS_REQUIRED_SIZE = 6;
    private BikeDisplayMetricType[] mMetrics;
    private Spinner mSplitsDistanceSpinner;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.settings.SelectMetricsActivity, com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DefaultStrappUUID.STRAPP_BIKE.equals(this.mStrappUUID)) {
            setBikeText();
        }
    }

    private void setBikeText() {
        TextView header = (TextView) ActivityUtils.getAndValidateView(this, R.id.strapp_select_metrics_header, TextView.class);
        header.setText(R.string.strapp_bike_settings_header);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_FITNESS_BIKE_DATA_POINTS);
    }

    @Override // com.microsoft.kapp.activities.settings.SelectMetricsActivity
    protected void saveSettings() {
        ArrayList<BikeDisplayMetricType> newList = new ArrayList<>();
        BikeDisplayMetricType[] defaultBikeMetricsArray = EventUtils.getDefaultBikingMetricsOrder(this.mStrappSettingsManager.getBandVersion());
        ArrayList<BikeDisplayMetricType> remainderList = new ArrayList<>(Arrays.asList(defaultBikeMetricsArray));
        for (int i = 0; i < this.mMetricsSpinners.size(); i++) {
            int index = this.mMetricsSpinners.get(i).getSelectedItemPosition();
            BikeDisplayMetricType metric = defaultBikeMetricsArray[index];
            remainderList.remove(metric);
            newList.add(metric);
        }
        if (this.mStrappSettingsManager.getBandVersion() != BandVersion.NEON) {
            newList.addAll(remainderList);
            while (newList.size() < CARGO_BIKE_METRICS_REQUIRED_SIZE) {
                newList.add(BikeDisplayMetricType.NONE);
            }
        }
        BikeDisplayMetricType[] newArray = (BikeDisplayMetricType[]) newList.toArray(new BikeDisplayMetricType[newList.size()]);
        this.mStrappSettingsManager.setTransactionBikeMetrics(newArray);
        this.mStrappSettingsManager.setTransactionBikeSplitDistance(getResources().getIntArray(R.array.bike_splits_spinner_choices)[this.mSplitsDistanceSpinner.getSelectedItemPosition()]);
        finish();
    }

    @Override // com.microsoft.kapp.activities.settings.SelectMetricsActivity
    protected void initSpinners() {
        ArrayAdapter<CharSequence> sharedSpinnerAdapter = ArrayAdapter.createFromResource(this, this.mStrappSettingsManager.getBikeMetricStrings(), R.drawable.settings_spinner_item);
        sharedSpinnerAdapter.setDropDownViewResource(R.drawable.settings_spinner_dropdown_item);
        instantiateBaseAdapters(sharedSpinnerAdapter);
        this.mMetrics = this.mStrappSettingsManager.getBikingMetricsOrder();
        if (this.mStrappSettingsManager.getBandVersion() == BandVersion.NEON) {
            findViewById(R.id.strapp_select_metrics_main_label).setVisibility(0);
            View metricsLayout = findViewById(R.id.drawer_metrics_layout);
            this.mDescriptionText.setText(R.string.strapp_select_bike_metrics_description_neon);
            metricsLayout.setVisibility(0);
            instantiateNeonSpinners(sharedSpinnerAdapter);
        } else {
            this.mDescriptionText.setText(R.string.strapp_select_bike_metrics_description);
        }
        for (int i = 0; i < this.mMetricsSpinners.size(); i++) {
            this.mMetricsSpinners.get(i).setSelection(getSpinnerIndexFor(this.mMetrics[i]));
        }
        initBikeSplitsSpinner();
    }

    private void initBikeSplitsSpinner() {
        this.mSplitsDistanceSpinner = (Spinner) ActivityUtils.getAndValidateView(this, R.id.strapp_select_splits_spinner, Spinner.class);
        findViewById(R.id.drawer_bike_split_distance_layout).setVisibility(0);
        int currentSelectedValueIndex = 0;
        ArrayList<String> displayValues = new ArrayList<>();
        int currentSplitDistance = this.mStrappSettingsManager.getBikeSplitDistance();
        int[] values = getResources().getIntArray(R.array.bike_splits_spinner_choices);
        for (int i = 0; i < values.length; i++) {
            String metric = this.mSettingsProvider.isDistanceHeightMetric() ? StringUtils.unitKilometers(values[i], getResources()) : StringUtils.unitMiles(values[i], getResources());
            displayValues.add(metric);
            if (values[i] == currentSplitDistance) {
                currentSelectedValueIndex = i;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, (int) R.drawable.settings_spinner_item, displayValues);
        adapter.setDropDownViewResource(R.drawable.settings_spinner_dropdown_item);
        this.mSplitsDistanceSpinner.setAdapter((SpinnerAdapter) adapter);
        this.mSplitsDistanceSpinner.setSelection(currentSelectedValueIndex);
    }

    protected int getSpinnerIndexFor(BikeDisplayMetricType metric) {
        return getResourceIndexFor(this, this.mStrappSettingsManager, metric);
    }

    public static int getResourceIndexFor(Context context, StrappSettingsManager manager, BikeDisplayMetricType metric) {
        String[] bikeMetricsArray = context.getResources().getStringArray(manager.getBikeMetricStrings());
        List<String> list = Arrays.asList(bikeMetricsArray);
        switch (metric) {
            case DURATION:
                return list.indexOf(context.getString(R.string.event_metric_duration));
            case HEART_RATE:
                return list.indexOf(context.getString(R.string.event_metric_heart_rate));
            case CALORIES:
                return list.indexOf(context.getString(R.string.event_metric_calories));
            case DISTANCE:
                return list.indexOf(context.getString(R.string.event_metric_distance));
            case SPEED:
                return list.indexOf(context.getString(R.string.event_metric_speed));
            case AVERAGE_SPEED:
                return list.indexOf(context.getString(R.string.event_metric_average_speed));
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
