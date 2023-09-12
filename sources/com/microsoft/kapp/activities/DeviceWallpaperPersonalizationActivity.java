package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.DeviceWallpaperAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.personalization.DeviceWallpaper;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.krestsdk.models.BandVersion;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DeviceWallpaperPersonalizationActivity extends BaseActivity {
    public static final String ARG_IN_CURRENT_WALLPAPER_RES_ID = "in_current_wallpaper_res_id";
    public static final String ARG_IN_DEVICE_THEME_ID = "in_device_theme_id";
    public static final String ARG_OUT_WALLPAPER_PATTERN_ID = "out_wallpaper_pattern_id";
    private int mCurrentWallpaperResId;
    private DeviceTheme mDeviceTheme;
    private Integer mDeviceThemeId;
    private ArrayAdapter<DeviceWallpaper> mGridAdapter;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    @Inject
    SettingsProvider mSettingsProvider;
    private GridView mWallpaperGrid;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_wallpaper_personalization_activity);
        this.mDeviceThemeId = Integer.valueOf(getIntent().getIntExtra(ARG_IN_DEVICE_THEME_ID, 0));
        this.mCurrentWallpaperResId = getIntent().getIntExtra(ARG_IN_CURRENT_WALLPAPER_RES_ID, -1);
        BandVersion bandVersion = BandVersion.values()[getIntent().getIntExtra(PersonalizationManagerFactory.BAND_VERSION_ID, 1)];
        this.mDeviceTheme = this.mPersonalizationManagerFactory.getPersonalizationManager(bandVersion).getThemeById(this.mDeviceThemeId.intValue());
        this.mWallpaperGrid = (GridView) ActivityUtils.getAndValidateView(this, R.id.wallpaper_grid_container, GridView.class);
        this.mGridAdapter = new DeviceWallpaperAdapter(this, this.mDeviceTheme.getWallpapers(), this.mCurrentWallpaperResId);
        this.mWallpaperGrid.setAdapter((ListAdapter) this.mGridAdapter);
        this.mWallpaperGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.activities.DeviceWallpaperPersonalizationActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceWallpaperPersonalizationActivity.this.setResultsAndExit(position);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        FreStatus status = this.mSettingsProvider.getFreStatus();
        if (status == FreStatus.SHOWN) {
            Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_PERSONALIZE_WALLPAPER_CHOOSER);
        } else {
            Telemetry.logPage(TelemetryConstants.PageViews.OOBE_PERSONALIZE_WALLPAPER_CHOOSER);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResultsAndExit(int position) {
        Intent mIntent = new Intent();
        DeviceWallpaper deviceWallpaper = this.mGridAdapter.getItem(position);
        mIntent.putExtra(ARG_OUT_WALLPAPER_PATTERN_ID, deviceWallpaper != null ? deviceWallpaper.getId() : -1);
        setResult(-1, mIntent);
        finish();
    }
}
