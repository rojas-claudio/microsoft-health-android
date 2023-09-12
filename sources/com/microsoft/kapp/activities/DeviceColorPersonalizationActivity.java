package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.DeviceColorsAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.krestsdk.models.BandVersion;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DeviceColorPersonalizationActivity extends BaseActivity {
    public static final String ARG_IN_CURRENT_DEVICE_THEME_ID = "in_current_device_theme_id";
    public static final String ARG_OUT_DEVICE_THEME_ID = "out_device_theme_id";
    private GridView mColorsGrid;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    @Inject
    SettingsProvider mSettingsProvider;
    private DeviceColorsAdapter mThemesAdapter;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_color_personalization_activity);
        this.mColorsGrid = (GridView) ActivityUtils.getAndValidateView(this, R.id.colors_grid_container, GridView.class);
        int currentThemeId = getIntent().getIntExtra(ARG_IN_CURRENT_DEVICE_THEME_ID, -1);
        BandVersion bandVersion = BandVersion.values()[getIntent().getIntExtra(PersonalizationManagerFactory.BAND_VERSION_ID, 1)];
        this.mThemesAdapter = new DeviceColorsAdapter(this, this.mPersonalizationManagerFactory.getPersonalizationManager(bandVersion).getThemes(), currentThemeId);
        this.mColorsGrid.setAdapter((ListAdapter) this.mThemesAdapter);
        this.mColorsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.activities.DeviceColorPersonalizationActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                DeviceColorPersonalizationActivity.this.setResultsAndExit(position);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        FreStatus status = this.mSettingsProvider.getFreStatus();
        if (status == FreStatus.SHOWN) {
            Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_PERSONALIZE_COLOR_CHOOSER);
        } else {
            Telemetry.logPage(TelemetryConstants.PageViews.OOBE_PERSONALIZE_COLOR_CHOOSER);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResultsAndExit(int position) {
        Intent mIntent = new Intent();
        DeviceTheme selectedDeviceTheme = this.mThemesAdapter.getItem(position);
        mIntent.putExtra(ARG_OUT_DEVICE_THEME_ID, selectedDeviceTheme != null ? selectedDeviceTheme.getThemeId() : -1);
        setResult(-1, mIntent);
        finish();
    }
}
