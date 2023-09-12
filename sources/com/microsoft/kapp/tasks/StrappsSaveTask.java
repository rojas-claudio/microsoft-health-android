package com.microsoft.kapp.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.StartStrip;
import com.microsoft.band.device.StrappColorPalette;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.models.strapp.StrappState;
import com.microsoft.kapp.models.strapp.StrappStateCollection;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.NeonStrappUtils;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class StrappsSaveTask extends StrappsTask<Void> {
    protected Context mApplicationContext;
    private BandVersion mBandVersion;
    private Map<UUID, CargoStrapp> mGenericStrappsList;
    protected StartStrip mNewStartStrip;
    protected PersonalizationManagerFactory mPersonalizationManagerFactory;
    private SettingsProvider mSettingsProvider;
    private StrappSettingsManager mStrappSettingsManager;
    private StrappStateCollection mStrapps;
    protected ArrayList<UUID> mUUIDs;

    public StrappsSaveTask(CargoConnection cargoConnection, StrappStateCollection strapps, SettingsProvider settingsProvider, PersonalizationManagerFactory personalizationManagerFactory, StrappSettingsManager strappSettingsManager, Context applicationContext, OnTaskListener onTaskListener) {
        super(cargoConnection, onTaskListener);
        this.mBandVersion = BandVersion.UNKNOWN;
        Validate.notNull(strapps, "strapps");
        Validate.notNull(settingsProvider, "settingsProvider");
        Validate.notNull(applicationContext, "applicationContext");
        Validate.notNull(personalizationManagerFactory, "Personalization Factory");
        this.mPersonalizationManagerFactory = personalizationManagerFactory;
        this.mStrapps = strapps;
        this.mSettingsProvider = settingsProvider;
        this.mApplicationContext = applicationContext;
        this.mStrappSettingsManager = strappSettingsManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public final Void doInBackground(Void... params) {
        try {
            StartStrip defaultStrip = getCargoConnection().getDefaultStrapps();
            this.mBandVersion = getCargoConnection().getBandVersion();
            StrappUtils strappUtils = null;
            switch (this.mBandVersion) {
                case NEON:
                    strappUtils = new NeonStrappUtils();
                    break;
                case CARGO:
                    strappUtils = new StrappUtils();
                    break;
                case UNKNOWN:
                    strappUtils = new StrappUtils();
                    break;
            }
            try {
                instantiateGenericStrapps(strappUtils);
            } catch (Exception ex) {
                setException(ex);
                KLog.e(this.TAG, "Error instantiating generic strapps", ex);
            }
            this.mUUIDs = new ArrayList<>();
            List<CargoStrapp> deviceDefaultStrappList = new ArrayList<>(defaultStrip.getAppList());
            if (this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
                StartStrip installedStrip = getCargoConnection().getStartStrip();
                for (CargoStrapp strapp : installedStrip.getAppList()) {
                    if (!deviceDefaultStrappList.contains(strapp)) {
                        deviceDefaultStrappList.add(strapp);
                    }
                }
            }
            List<CargoStrapp> strappsSelection = new ArrayList<>();
            populateStrapps(strappsSelection, deviceDefaultStrappList);
            setNotificationSettingsForStrapps(strappsSelection);
            this.mNewStartStrip = new StartStrip(strappsSelection);
            doInBackgroundFinish();
            return null;
        } catch (Exception ex2) {
            setException(ex2);
            KLog.w(this.TAG, "Posting strapp failed!", ex2);
            return null;
        }
    }

    private void setNotificationSettingsForStrapps(List<CargoStrapp> strappsSelection) {
        if (this.mStrappSettingsManager != null) {
            for (CargoStrapp strapp : strappsSelection) {
                if (this.mStrappSettingsManager.isNotificationStatusManagedTile(strapp.getStrappData().getAppId())) {
                    int settingsMask = 0;
                    if (this.mStrappSettingsManager.areSettingsEnabledForStrapp(strapp.getStrappData().getAppId())) {
                        settingsMask = 1;
                    }
                    if (strapp.getId() != DefaultStrappUUID.STRAPP_CALENDAR) {
                        settingsMask |= 2;
                    }
                    strapp.setSettings(settingsMask);
                }
            }
        }
    }

    private void populateStrapps(List<CargoStrapp> strappsSelection, List<CargoStrapp> deviceDefaultStrappsList) {
        for (Map.Entry<UUID, StrappState> strapp : this.mStrapps.entrySet()) {
            this.mUUIDs.add(strapp.getKey());
            CargoStrapp app = getStrappForUUID(strapp.getKey(), deviceDefaultStrappsList);
            if (app == null && this.mGenericStrappsList != null) {
                app = this.mGenericStrappsList.get(strapp.getKey());
                if (app == null) {
                    KLog.e(this.TAG, "Strapp %s doesnt exist!", strapp.getKey());
                } else {
                    this.mSettingsProvider.clearLastSyncDataForCustomStrapp(strapp.getKey());
                }
            }
            strappsSelection.add(app);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SuppressLint({"ResourceAsColor"})
    public void doInBackgroundFinish() throws CargoException {
        getCargoConnection().setStartStrip(this.mNewStartStrip);
        if (this.mNewStartStrip.contains(DefaultStrappUUID.STRAPP_FACEBOOK)) {
            setResourcesForFacebookAndTwitterWithHighlightColor(R.color.facebook_color_highlight, DefaultStrappUUID.STRAPP_FACEBOOK);
        }
        if (this.mNewStartStrip.contains(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER)) {
            setResourcesForFacebookAndTwitterWithHighlightColor(R.color.facebook_color_highlight, DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER);
        }
        if (this.mNewStartStrip.contains(DefaultStrappUUID.STRAPP_TWITTER)) {
            setResourcesForFacebookAndTwitterWithHighlightColor(R.color.twitter_color_highlight, DefaultStrappUUID.STRAPP_TWITTER);
        }
    }

    private void setResourcesForFacebookAndTwitterWithHighlightColor(int highlightColor, UUID appUUID) throws CargoException {
        int themeId = this.mSettingsProvider.getCurrentWallpaperId();
        DeviceTheme deviceTheme = this.mPersonalizationManagerFactory.getPersonalizationManager(this.mBandVersion).getThemeById(themeId);
        Resources resources = this.mApplicationContext.getResources();
        StrappColorPalette colors = new StrappColorPalette(resources.getColor(R.color.facebook_twitter_base), resources.getColor(highlightColor), deviceTheme.getLowlight(), deviceTheme.getSecondaryText(), deviceTheme.getHighContrast(), deviceTheme.getMuted());
        getCargoConnection().setStrappTheme(colors, appUUID);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.StrappsTask
    public final void onPostExecuteCalldown(Void result) {
        if (getException() == null) {
            saveNotificationSettingsForTiles();
            this.mSettingsProvider.setUUIDsOnDevice(this.mUUIDs);
            HashMap<String, String> telemetryProperties = new HashMap<>();
            telemetryProperties.put(TelemetryConstants.Events.TilesChange.Dimensions.ACTIVE_TILES, String.valueOf(this.mUUIDs.size()));
            for (CargoStrapp strapp : this.mNewStartStrip.getAppList()) {
                telemetryProperties.put(strapp.getName(), Boolean.toString(true));
            }
            if (this.mStrappSettingsManager != null) {
                telemetryProperties.put(TelemetryConstants.Events.TilesChange.Dimensions.CHANGED_ORDER, String.valueOf(this.mStrappSettingsManager.isOrderChanged()));
            }
            if (this.mUUIDs.contains(DefaultStrappUUID.STRAPP_GOLF)) {
                telemetryProperties.put("Golf", TelemetryConstants.Events.TilesChange.Dimensions.TILE_ENABLED);
            }
            telemetryProperties.put(TelemetryConstants.Events.TilesChange.Dimensions.ACTIVE_TILES, String.valueOf(this.mUUIDs.size()));
            Telemetry.logEvent("Settings/Band/Manage Tiles", telemetryProperties, null);
        }
    }

    private void saveNotificationSettingsForTiles() {
        if (this.mStrappSettingsManager == null) {
            this.mSettingsProvider.setMessagingDeviceNotificationsEnabled(true);
            this.mSettingsProvider.setCallsDeviceNotificationsEnabled(true);
            this.mSettingsProvider.setCalendarDeviceNotificationsEnabled(true);
            this.mSettingsProvider.setEmailDeviceNotificationsEnabled(true);
            this.mSettingsProvider.setFacebookDeviceNotificationsEnabled(true);
            this.mSettingsProvider.setFacebookMessengerDeviceNotificationsEnabled(true);
            this.mSettingsProvider.setNotificationCenterDeviceNotificationsEnabled(true);
            this.mSettingsProvider.setTwitterDeviceNotificationsEnabled(true);
            return;
        }
        this.mSettingsProvider.setMessagingDeviceNotificationsEnabled(this.mStrappSettingsManager.areSettingsEnabledForStrapp(DefaultStrappUUID.STRAPP_MESSAGING));
        this.mSettingsProvider.setCallsDeviceNotificationsEnabled(this.mStrappSettingsManager.areSettingsEnabledForStrapp(DefaultStrappUUID.STRAPP_CALLS));
        this.mSettingsProvider.setCalendarDeviceNotificationsEnabled(this.mStrappSettingsManager.areSettingsEnabledForStrapp(DefaultStrappUUID.STRAPP_CALENDAR));
        this.mSettingsProvider.setEmailDeviceNotificationsEnabled(this.mStrappSettingsManager.areSettingsEnabledForStrapp(DefaultStrappUUID.STRAPP_EMAIL));
        this.mSettingsProvider.setFacebookDeviceNotificationsEnabled(this.mStrappSettingsManager.areSettingsEnabledForStrapp(DefaultStrappUUID.STRAPP_FACEBOOK));
        this.mSettingsProvider.setFacebookMessengerDeviceNotificationsEnabled(this.mStrappSettingsManager.areSettingsEnabledForStrapp(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER));
        this.mSettingsProvider.setNotificationCenterDeviceNotificationsEnabled(this.mStrappSettingsManager.areSettingsEnabledForStrapp(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER));
        this.mSettingsProvider.setTwitterDeviceNotificationsEnabled(this.mStrappSettingsManager.areSettingsEnabledForStrapp(DefaultStrappUUID.STRAPP_TWITTER));
    }

    private void instantiateGenericStrapps(StrappUtils strappUtils) throws Exception {
        Map<UUID, CargoStrapp> list = new HashMap<>();
        list.put(DefaultStrappUUID.STRAPP_FACEBOOK, StrappUtils.createGenericStrapp(strappUtils.getTileIconForTile(DefaultStrappUUID.STRAPP_FACEBOOK), strappUtils.getBadgingIconForTile(DefaultStrappUUID.STRAPP_FACEBOOK), strappUtils.getNotificiationIconForTile(DefaultStrappUUID.STRAPP_FACEBOOK), "Facebook", null, (short) 0, DefaultStrappUUID.STRAPP_FACEBOOK, this.mApplicationContext));
        list.put(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER, StrappUtils.createGenericStrapp(strappUtils.getTileIconForTile(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER), strappUtils.getBadgingIconForTile(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER), strappUtils.getNotificiationIconForTile(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER), "Facebook Messenger", null, (short) 0, DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER, this.mApplicationContext));
        list.put(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER, StrappUtils.createGenericStrapp(strappUtils.getTileIconForTile(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER), strappUtils.getBadgingIconForTile(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER), strappUtils.getNotificiationIconForTile(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER), "Notification Center", null, (short) 0, DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER, this.mApplicationContext));
        list.put(DefaultStrappUUID.STRAPP_EMAIL, StrappUtils.createGenericStrapp(strappUtils.getTileIconForTile(DefaultStrappUUID.STRAPP_EMAIL), strappUtils.getBadgingIconForTile(DefaultStrappUUID.STRAPP_EMAIL), strappUtils.getNotificiationIconForTile(DefaultStrappUUID.STRAPP_EMAIL), "Email", null, (short) 0, DefaultStrappUUID.STRAPP_EMAIL, this.mApplicationContext));
        list.put(DefaultStrappUUID.STRAPP_TWITTER, StrappUtils.createGenericStrapp(strappUtils.getTileIconForTile(DefaultStrappUUID.STRAPP_TWITTER), strappUtils.getBadgingIconForTile(DefaultStrappUUID.STRAPP_TWITTER), strappUtils.getNotificiationIconForTile(DefaultStrappUUID.STRAPP_TWITTER), "Twitter", null, (short) 0, DefaultStrappUUID.STRAPP_TWITTER, this.mApplicationContext));
        list.put(DefaultStrappUUID.STRAPP_BING_WEATHER, strappUtils.createWeatherStrapp(this.mApplicationContext));
        list.put(DefaultStrappUUID.STRAPP_BING_FINANCE, strappUtils.createFinanceStrapp(this.mApplicationContext));
        list.put(DefaultStrappUUID.STRAPP_STARBUCKS, strappUtils.createStarbucksStrapp(this.mApplicationContext));
        this.mGenericStrappsList = list;
    }

    private CargoStrapp getStrappForUUID(UUID id, List<CargoStrapp> strappList) {
        for (CargoStrapp strapp : strappList) {
            if (strapp.getStrappData().getAppId().compareTo(id) == 0) {
                return strapp;
            }
        }
        return null;
    }
}
