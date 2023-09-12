package com.microsoft.kapp.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Pair;
import com.google.gson.Gson;
import com.microsoft.band.device.StrappPageElement;
import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.band.service.crypto.CryptoException;
import com.microsoft.band.service.crypto.CryptoProvider;
import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.ApiVersion;
import com.microsoft.kapp.models.CloudEnvironment;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.telephony.KNotification;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.kapp.utils.GsonUtils;
import com.microsoft.kapp.utils.RegionUtils;
import com.microsoft.kapp.version.CheckedFirmwareUpdateInfo;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public class SettingsProvider {
    private static final String API_VERSION_KEY = "ApiVersion";
    private static final String APP_PREFERENCES_KEY = "AppPrefs";
    private static final String APP_SESSION_COUNT_BEFORE_CLICK_WHATS_NEW = "appSessionCountBeforeClickWhatsNew";
    private static final String APP_VERSION_KEY = "AppVersion";
    private static final String AUTO_DEVICE_SYNC_ENABLED_KEY = "AutoDeviceSyncEnabled";
    private static final String BIKE_SPLIT_MULTIPLIER = "BikeSplitMultiplier";
    private static final String BIKING_METRICS_ORDER = "BikingMetricsOrder";
    private static final String CALENDAR_DEVICE_NOTIFICATIONS_ENABLED_KEY = "CalendarDeviceNotificationsEnabled";
    private static final String CALENDAR_LAST_ERROR_MESSAGE_KEY = "CalendarLastErrorMessage";
    private static final String CALENDAR_LAST_ERROR_TIME_KEY = "CalendarLastErrorTime";
    private static final String CALENDAR_LAST_SYNC_EVENTS_KEY = "CalendarLastSyncEvents";
    private static final String CALENDAR_LAST_SYNC_TIME_KEY = "CalendarLastSyncTime";
    private static final String CALLS_AUTO_REPLY_PREFIX = "CallsAutoReply";
    private static final String CALLS_DEVICE_NOTIFICATIONS_ENABLED_KEY = "CallsDeviceNotificationsEnabled";
    private static final String CALLS_ENABLED_KEY = "CallsEnabled";
    private static final String CHECKED_FIRMWARE_UPDATE_INFO = "FirmwareUpdateInfo";
    public static final String CUSTOM_ENVIRONMENT_URL_KEY = "CustomEnvironmentUrl";
    private static final String DEBUG_HEADER_SESSION_NAME = "DebugHeaderSessionName";
    public static final String DEBUG_PREFERENCES_KEY = "DebugPrefs";
    private static final String DEVICE_FW_VERSION_KEY = "FwVersion";
    private static final String DEVICE_IN_PLUS_MODE = "DeviceInPlusMode";
    private static final String DEVICE_PERSONALIZATION_INITIALIZED = "DevicePersonalizationInitialized";
    private static final String DYNAMIC_CONFIGURATION_LOAD_FROM_CLOUD = "dynamicConfigurationLoadFromCloud";
    private static final String EMAIL_DEVICE_NOTIFICATIONS_ENABLED_KEY = "EmailDeviceNotificationsEnabled";
    private static final String ENV_KEY = "Env";
    private static final String FACEBOOK_DEVICE_NOTIFICATIONS_ENABLED_KEY = "FacebookDeviceNotificationsEnabled";
    private static final String FACEBOOK_MESSENGER_DEVICE_NOTIFICATIONS_ENABLED_KEY = "FacebookMessengerDeviceNotificationsEnabled";
    private static final String FIDDLER_END_SESSION_ID = "FiddlerEndSessionId";
    private static final String FIDDLER_START_SESSION_ID = "FiddlerStartSessionId";
    private static final String FIRST_RUN_KEY = "FirstRun";
    private static final String FRE_STATUS_KEY_PREFIX = "FreStatus_";
    private static final String FUS_END_POINT = "fusEndPoint";
    private static final String HERO_SHOWN_PREVIOUSLY_KEY = "HeroShownPreviously";
    private static final String IS_CACHE_ENABLED = "IsCacheEnabled";
    private static final String IS_DISTANCE_HEIGHT_METRIC_KEY = "IsDistanceHeightMetric";
    private static final String IS_LOGGING_FOR_PUBLIC_RELEASE = "IsLoggingForPublicRelease";
    private static final String IS_SENSOR_LOGGING_ENABLED = "IsSensorLoggingEnabled";
    private static final String IS_SHAKE_TO_SEND_FEEDBACK_ENABLED = "IsShakeToSendFeedbackEnabled";
    private static final String IS_TEMPERATURE_METRIC_KEY = "IsTemperatureMetric";
    private static final String IS_TOKEN_REFRESH_ENABLED = "IsTokenRefreshEnabled";
    private static final String IS_USE_LOCAL_SENSOR_DATA_ENABLED = "IsUseLocalSensorDataEnabled";
    private static final String IS_USE_PHONE_SENSOR_DATA_ENABLED = "IsDisplayPhoneSensorData";
    private static final String IS_WEIGHT_METRIC_KEY = "IsWeightMetric";
    private static final String LAST_BIKE_METRICS_SAVED_VERSION = "BikeMetricsSavedVersion";
    private static final String LAST_BIKING_CLICK_TIME_KEY = "LastBikingTileClickTime";
    private static final String LAST_CALORIES_CLICK_TIME_KEY = "LastCaloriesTimeClickedKey";
    private static final String LAST_GOLF_CLICK_TIME_KEY = "LastGolfTimeClickedKey";
    private static final String LAST_GUIDED_WORKOUT_CALENDAR_CLICK_TIME_KEY = "LastGuidedWorkoutCalendarClickTime";
    private static final String LAST_GUIDED_WORKOUT_CLICK_TIME_KEY = "LastGuidedWorkoutTileClickTime";
    private static final String LAST_HOME_REFRESH_TIME_KEY = "LastHomeRefreshTime";
    private static final String LAST_RUN_CLICK_TIME_KEY = "LastRunTileClickTime";
    private static final String LAST_RUN_METRICS_SAVED_VERSION = "RunMetricsSavedVersion";
    private static final String LAST_SENT_INSIGHT_ID = "LastSentInsightId";
    private static final String LAST_SLEEP_CLICK_TIME_KEY = "LastSleepTileClickTime";
    private static final String LAST_STEPS_CLICK_TIME_KEY = "LastStepsTimeClickedKey";
    private static final String LAST_SYNC_TIME_KEY = "LastSyncTime";
    private static final String LAST_WORKOUT_CLICK_TIME_KEY = "LastWorkoutTileClickTime";
    private static final String LATEST_FIRMWARE_VERSION_KEY = "LatestFirmwareVersion";
    private static final String MESSAGING_AUTO_REPLY_PREFIX = "MessagingAutoReply";
    private static final String MESSAGING_DEVICE_NOTIFICATIONS_ENABLED_KEY = "MessagingDeviceNotificationsEnabled";
    private static final String MESSAGING_ENABLED_KEY = "MessagingEnabled";
    private static final String NOTIFICATION_CENTER_APPS = "NotificationCenterApps";
    private static final String NOTIFICATION_CENTER_DEVICE_NOTIFICATIONS_ENABLED_KEY = "NotificationCenterDeviceNotificationsEnabled";
    private static final String NOTIFICATION_CENTER_LOGGING_ENABLED_KEY = "NotificationCenterLoggingEnabled";
    private static final String NOTIFICATION_WHATS_NEW = "notificationWhatsNew";
    private static final String NUMBER_OF_ALLOWED_STRAPPS = "NumberOfAllowedStrapps";
    private static final String OOBE_BAND_CONNECTION_SKIPPED = "OobeBandPairingSkipped";
    private static final String OOBE_BAND_UI_VERSION = "OobeBandUiVersion";
    private static final String OOBE_OR_DEVICE_CONNECTION = "OobeOrDeviceConnection";
    private static final String OOBE_SHOULD_CONNECT_PHONE = "OobeShouldConnectPhone";
    private static final String PROFILE_NAME = "UserProfile";
    private static final String RAISED_INSIGHTS_ENABLED_KEY = "RaisedInsights";
    private static final String RESYNC_GW_AFTER_MINI_OOBE_KEY = "ResyncWorkoutToDeviceAfterMiniOobe";
    private static final String ROLLING_LOG_FILE_NAME = "RollingLogFileName";
    private static final String RUN_METRICS_ORDER = "RunMetricsOrder";
    private static final String SHOULD_SEND_DATA_OVER_WIFI_ONLY_KEY = "ShouldSendDataOverWiFiOnly";
    private static final String STARBUCKS_CARD_NUMBER = "StarbucksCardNumber";
    private static final String STRAPP_PREFERENCES_KEY = "StrappPrefs";
    private static final String SYNC_DEBUG_INFORMATION = "SyncDebugInformation";
    private static final String SYNC_GOLF_COURSE_ID = "SYNC_GOLF_COURSE_ID";
    private static final String SYNC_GOLF_COURSE_TEE_ID = "SYNC_GOLF_COURSE_TEE_ID";
    private static final String SYNC_GOLF_TIMESTAMP = "SYNC_GOLF_TIMESTAMP";
    private static final String SYNC_NUMBER_KEY = "SyncNumber";
    private static final String SYNC_PREFERENCES_KEY = "SyncPrefs";
    private static final String TEMP_OOBE_DEVICE_NAME = "TempDeviceName";
    private static final String TEMP_OOBE_HWAG_INFORMATION = "TempHWAG";
    private static final String THIRD_PARTY_PARTNERS_PORTAL_ENDPOINT_KEY = "ThirdPartyPartnersPortalEndpoint";
    private static final String TMAG_CONNECTION_ENABLED_KEY = "SimulateTMAGConnection";
    private static final String TWITTER_DEVICE_NOTIFICATIONS_ENABLED_KEY = "TwitterDeviceNotificationsEnabled";
    private static final String USER_SAVED_BAND_REGION_SETTINGS = "BandRegionSettings";
    private static final String USER_STOCKS = "StockCompanies";
    private static final String UUIDS_ON_DEVICE = "CurrentUUIDsOnDevice";
    private static final String WALLPAPER_SETTING_KEY = "CurrentWallpaperId";
    private static final String WEATHER_LOCATION_LATITUDE = "WeatherLocationLatitude";
    private static final String WEATHER_LOCATION_LONGITUDE = "WeatherLocationLongitude";
    private static final String WHATS_NEW_CARDS_VERSION = "whatsNewCardsVersion";
    private CargoUserProfile mCacheCargoUserProfile;
    private final Context mContext;
    private CryptoProvider mCryptoProvider;
    private final Object mUserProfileLock = new Object();
    private static AtomicReference<HashMap<String, List<String>>> mLastWorkoutFilter = new AtomicReference<>();
    private static AtomicReference<String> mLastGuidedWorkoutFilterString = new AtomicReference<>();
    private static final String TAG = SettingsProvider.class.getSimpleName();

    public SettingsProvider(Context context, CryptoProvider cryptoProvider) {
        this.mContext = context;
        this.mCryptoProvider = cryptoProvider;
    }

    public boolean isMessagingEnabled() {
        return getAppPreferences().getBoolean(MESSAGING_ENABLED_KEY, true);
    }

    public void setMessagingEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), MESSAGING_ENABLED_KEY, enabled);
    }

    public boolean isCallsEnabled() {
        return getAppPreferences().getBoolean(CALLS_ENABLED_KEY, true);
    }

    public void setCallsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), CALLS_ENABLED_KEY, enabled);
    }

    public boolean isRaisedInsightsEnabled() {
        return getAppPreferences().getBoolean(RAISED_INSIGHTS_ENABLED_KEY, true);
    }

    public void setRaisedInsightsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), RAISED_INSIGHTS_ENABLED_KEY, enabled);
    }

    public boolean canDynamicConfigurationLoadFromCloud() {
        if (Compatibility.isPublicRelease() || !KappConfig.isDebbuging) {
            return true;
        }
        return getAppPreferences().getBoolean(DYNAMIC_CONFIGURATION_LOAD_FROM_CLOUD, true);
    }

    public void setDynamicConfigurationLoadFromCloud(boolean value) {
        setBoolean(getAppPreferences(), DYNAMIC_CONFIGURATION_LOAD_FROM_CLOUD, value);
    }

    public boolean isTMAGConnectionEnabled() {
        return getDebugPreferences().getBoolean(TMAG_CONNECTION_ENABLED_KEY, false);
    }

    public void setTMAGConnectionEnabled(boolean enabled) {
        setBoolean(getDebugPreferences(), TMAG_CONNECTION_ENABLED_KEY, enabled);
    }

    public boolean isAutoDeviceSyncEnabled() {
        return getAppPreferences().getBoolean(AUTO_DEVICE_SYNC_ENABLED_KEY, true);
    }

    public void setAutoDeviceSyncEnabled(boolean value) {
        setBoolean(getAppPreferences(), AUTO_DEVICE_SYNC_ENABLED_KEY, value);
    }

    public List<UUID> getUUIDsOnDevice() {
        UUID[] uuidArray;
        String uuidArrayString = getAppPreferences().getString(UUIDS_ON_DEVICE, null);
        List<UUID> strappsUuidOnDevice = new ArrayList<>();
        if (uuidArrayString != null && (uuidArray = (UUID[]) new Gson().fromJson(uuidArrayString, (Class<Object>) UUID[].class)) != null) {
            strappsUuidOnDevice.addAll(Arrays.asList(uuidArray));
        }
        return strappsUuidOnDevice;
    }

    public void setUUIDsOnDevice(ArrayList<UUID> UUIDs) {
        Gson gson = new Gson();
        String uuidString = gson.toJson(UUIDs.toArray());
        setString(getAppPreferences(), UUIDS_ON_DEVICE, uuidString);
    }

    public boolean isWeightMetric() {
        return getAppPreferences().getBoolean(IS_WEIGHT_METRIC_KEY, RegionUtils.isMetric());
    }

    public void setIsWeightMetric(boolean isMetric) {
        setBoolean(getAppPreferences(), IS_WEIGHT_METRIC_KEY, isMetric);
    }

    public boolean isTemperatureMetric() {
        return getAppPreferences().getBoolean(IS_TEMPERATURE_METRIC_KEY, RegionUtils.isMetric());
    }

    public void setIsTemperatureMetric(boolean isMetric) {
        setBoolean(getAppPreferences(), IS_TEMPERATURE_METRIC_KEY, isMetric);
    }

    public boolean isCachingEnabled() {
        return getAppPreferences().getBoolean(IS_CACHE_ENABLED, true);
    }

    public void setIsCachingEnabled(boolean isCacheEnabled) {
        setBoolean(getAppPreferences(), IS_CACHE_ENABLED, isCacheEnabled);
    }

    public boolean isUseLocalSensorDataEnabled() {
        return getDebugPreferences().getBoolean(IS_USE_LOCAL_SENSOR_DATA_ENABLED, false);
    }

    public void setIsUseLocalSensorDataEnabled(boolean isUseLocalSensorDataEnabled) {
        setBoolean(getDebugPreferences(), IS_USE_LOCAL_SENSOR_DATA_ENABLED, isUseLocalSensorDataEnabled);
    }

    public boolean isUsePhoneSensorData() {
        return getDebugPreferences().getBoolean(IS_USE_PHONE_SENSOR_DATA_ENABLED, false);
    }

    public void setIsUsePhoneSensorData(boolean isUseUsePhoneSensorData) {
        setBoolean(getDebugPreferences(), IS_USE_PHONE_SENSOR_DATA_ENABLED, isUseUsePhoneSensorData);
    }

    public boolean isSensorLoggingEnabled() {
        return getAppPreferences().getBoolean(IS_SENSOR_LOGGING_ENABLED, false);
    }

    public void setIsSensorLoggingEnabled(boolean isSensorLoggingEnabled) {
        setBoolean(getAppPreferences(), IS_SENSOR_LOGGING_ENABLED, isSensorLoggingEnabled);
    }

    public boolean isTokenRefreshEnabled() {
        return getAppPreferences().getBoolean(IS_TOKEN_REFRESH_ENABLED, true);
    }

    public boolean shouldEmulatePublicReleaseForLogging() {
        return getDebugPreferences().getBoolean(IS_LOGGING_FOR_PUBLIC_RELEASE, false);
    }

    public void setShouldEmulatePublicReleaseForLogging(boolean shouldEmulatePublicRelease) {
        setBoolean(getDebugPreferences(), IS_LOGGING_FOR_PUBLIC_RELEASE, shouldEmulatePublicRelease);
    }

    public void setIsTokenRefreshEnabled(boolean isTokenRefreshEnabled) {
        setBoolean(getAppPreferences(), IS_TOKEN_REFRESH_ENABLED, isTokenRefreshEnabled);
    }

    public boolean isDistanceHeightMetric() {
        return getAppPreferences().getBoolean("IsDistanceHeightMetric", RegionUtils.isMetric());
    }

    public void setIsDistanceHeightMetric(boolean isMetric) {
        setBoolean(getAppPreferences(), "IsDistanceHeightMetric", isMetric);
    }

    public boolean shouldSendDataOverWiFiOnly() {
        return getAppPreferences().getBoolean(SHOULD_SEND_DATA_OVER_WIFI_ONLY_KEY, false);
    }

    public void setShouldSendDataOverWiFiOnly(boolean value) {
        setBoolean(getAppPreferences(), SHOULD_SEND_DATA_OVER_WIFI_ONLY_KEY, value);
    }

    public FreStatus getFreStatus() {
        return (FreStatus) getEnum(getAppPreferences(), getFreStatusKey(), FreStatus.NOT_SHOWN);
    }

    public void setHeroShownPreviously(boolean isHeroShownPreviously) {
        setBoolean(getAppPreferences(), HERO_SHOWN_PREVIOUSLY_KEY, isHeroShownPreviously);
    }

    public boolean getHeroShownPreviously() {
        return getAppPreferences().getBoolean(HERO_SHOWN_PREVIOUSLY_KEY, false);
    }

    public void setFreStatus(FreStatus status) {
        setEnum(getAppPreferences(), getFreStatusKey(), status);
    }

    public String getLastFirmwareVersion() {
        return getAppPreferences().getString(LATEST_FIRMWARE_VERSION_KEY, null);
    }

    public void setLastFirmwareVersion(String firmWareversion) {
        setString(getAppPreferences(), LATEST_FIRMWARE_VERSION_KEY, firmWareversion);
    }

    public String getCalendarLastSyncTime() {
        return getAppPreferences().getString(CALENDAR_LAST_SYNC_TIME_KEY, null);
    }

    public void setCalendarLastSyncTime(String calendarLastSyncTime) {
        setString(getAppPreferences(), CALENDAR_LAST_SYNC_TIME_KEY, calendarLastSyncTime);
    }

    public String getCalendarLastSyncEvents() {
        return getAppPreferences().getString(CALENDAR_LAST_SYNC_EVENTS_KEY, null);
    }

    public void setCalendarLastSyncEvents(String calendarLastSyncEvents) {
        setString(getAppPreferences(), CALENDAR_LAST_SYNC_EVENTS_KEY, calendarLastSyncEvents);
    }

    public String getCalendarLastErrorMessage() {
        return getAppPreferences().getString(CALENDAR_LAST_ERROR_MESSAGE_KEY, null);
    }

    public void setCalendarLastErrorMessage(String errorMsg) {
        setString(getAppPreferences(), CALENDAR_LAST_ERROR_MESSAGE_KEY, errorMsg);
    }

    public String getCalendarLastErrorTime() {
        return getAppPreferences().getString(CALENDAR_LAST_ERROR_TIME_KEY, null);
    }

    public void setCalendarLastErrorTime(String errorTime) {
        setString(getAppPreferences(), CALENDAR_LAST_ERROR_TIME_KEY, errorTime);
    }

    public boolean isMessagingDeviceNotificationsEnabled() {
        return getAppPreferences().getBoolean(MESSAGING_DEVICE_NOTIFICATIONS_ENABLED_KEY, true);
    }

    public void setMessagingDeviceNotificationsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), MESSAGING_DEVICE_NOTIFICATIONS_ENABLED_KEY, enabled);
    }

    public boolean isCallsDeviceNotificationsEnabled() {
        return getAppPreferences().getBoolean(CALLS_DEVICE_NOTIFICATIONS_ENABLED_KEY, true);
    }

    public void setCallsDeviceNotificationsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), CALLS_DEVICE_NOTIFICATIONS_ENABLED_KEY, enabled);
    }

    public boolean isCalendarDeviceNotificationsEnabled() {
        return getAppPreferences().getBoolean(CALENDAR_DEVICE_NOTIFICATIONS_ENABLED_KEY, true);
    }

    public void setCalendarDeviceNotificationsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), CALENDAR_DEVICE_NOTIFICATIONS_ENABLED_KEY, enabled);
    }

    public boolean isEmailDeviceNotificationsEnabled() {
        return getAppPreferences().getBoolean(EMAIL_DEVICE_NOTIFICATIONS_ENABLED_KEY, true);
    }

    public void setEmailDeviceNotificationsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), EMAIL_DEVICE_NOTIFICATIONS_ENABLED_KEY, enabled);
    }

    public boolean isFacebookDeviceNotificationsEnabled() {
        return getAppPreferences().getBoolean(FACEBOOK_DEVICE_NOTIFICATIONS_ENABLED_KEY, true);
    }

    public void setFacebookDeviceNotificationsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), FACEBOOK_DEVICE_NOTIFICATIONS_ENABLED_KEY, enabled);
    }

    public boolean isFacebookMessengerDeviceNotificationsEnabled() {
        return getAppPreferences().getBoolean(FACEBOOK_MESSENGER_DEVICE_NOTIFICATIONS_ENABLED_KEY, true);
    }

    public void setFacebookMessengerDeviceNotificationsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), FACEBOOK_MESSENGER_DEVICE_NOTIFICATIONS_ENABLED_KEY, enabled);
    }

    public boolean isNotificationCenterDeviceNotificationsEnabled() {
        return getAppPreferences().getBoolean(NOTIFICATION_CENTER_DEVICE_NOTIFICATIONS_ENABLED_KEY, false);
    }

    public void setNotificationCenterDeviceNotificationsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), NOTIFICATION_CENTER_DEVICE_NOTIFICATIONS_ENABLED_KEY, enabled);
    }

    public boolean isTwitterDeviceNotificationsEnabled() {
        return getAppPreferences().getBoolean(TWITTER_DEVICE_NOTIFICATIONS_ENABLED_KEY, true);
    }

    public void seNotificationCenterLoggingEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), NOTIFICATION_CENTER_LOGGING_ENABLED_KEY, enabled);
    }

    public boolean isNotificationCenterLoggingEnabled() {
        return getAppPreferences().getBoolean(NOTIFICATION_CENTER_LOGGING_ENABLED_KEY, false);
    }

    public void setTwitterDeviceNotificationsEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), TWITTER_DEVICE_NOTIFICATIONS_ENABLED_KEY, enabled);
    }

    public String getMessagingAutoReply(int replyNum, String defaultReply) {
        return getAppPreferences().getString(MESSAGING_AUTO_REPLY_PREFIX.concat(Integer.toString(replyNum)), defaultReply);
    }

    public String[] getMessagingAutoReplies() {
        String[] results = {getAppPreferences().getString("MessagingAutoReply0", this.mContext.getResources().getString(StrappSettingsManager.getMessagingReplyDefaultId(1))), getAppPreferences().getString("MessagingAutoReply0", this.mContext.getResources().getString(StrappSettingsManager.getMessagingReplyDefaultId(2))), getAppPreferences().getString("MessagingAutoReply0", this.mContext.getResources().getString(StrappSettingsManager.getMessagingReplyDefaultId(3))), getAppPreferences().getString("MessagingAutoReply0", this.mContext.getResources().getString(StrappSettingsManager.getMessagingReplyDefaultId(4)))};
        return results;
    }

    public String[] getCallsAutoReplies() {
        String[] results = {getAppPreferences().getString("CallsAutoReply0", this.mContext.getResources().getString(StrappSettingsManager.getCallAutoReplyDefaultId(1))), getAppPreferences().getString("CallsAutoReply0", this.mContext.getResources().getString(StrappSettingsManager.getCallAutoReplyDefaultId(2))), getAppPreferences().getString("CallsAutoReply0", this.mContext.getResources().getString(StrappSettingsManager.getCallAutoReplyDefaultId(3))), getAppPreferences().getString("CallsAutoReply0", this.mContext.getResources().getString(StrappSettingsManager.getCallAutoReplyDefaultId(4)))};
        return results;
    }

    public void setMessagingAutoReply(int replyNum, String reply) {
        setString(getAppPreferences(), MESSAGING_AUTO_REPLY_PREFIX.concat(Integer.toString(replyNum)), reply);
    }

    public String getCallsAutoReply(int replyNum, String defaultReply) {
        return getAppPreferences().getString(CALLS_AUTO_REPLY_PREFIX.concat(Integer.toString(replyNum)), defaultReply);
    }

    public void setCallsAutoReply(int replyNum, String reply) {
        setString(getAppPreferences(), CALLS_AUTO_REPLY_PREFIX.concat(Integer.toString(replyNum)), reply);
    }

    public ApiVersion getApiVersion() {
        return (ApiVersion) getEnum(getAppPreferences(), API_VERSION_KEY, ApiVersion.V1);
    }

    public void setApiVersion(ApiVersion version) {
        setEnum(getAppPreferences(), API_VERSION_KEY, version);
    }

    public int getLastSentInsightId() {
        return getAppPreferences().getInt(LAST_SENT_INSIGHT_ID, 0);
    }

    public void setLastSentInsightId(int insightId) {
        setInt(getAppPreferences(), LAST_SENT_INSIGHT_ID, insightId);
    }

    public int getFiddlerCurrentSessionId() {
        return getAppPreferences().getInt(FIDDLER_END_SESSION_ID, 1);
    }

    public void setFiddlerCurrentSessionId(int sessionId) {
        setInt(getAppPreferences(), FIDDLER_END_SESSION_ID, sessionId);
    }

    public int getFiddlerStartSessionId() {
        return getAppPreferences().getInt(FIDDLER_START_SESSION_ID, 1);
    }

    public void setFiddlerStartSessionId(int sessionId) {
        setInt(getAppPreferences(), FIDDLER_START_SESSION_ID, sessionId);
    }

    public void setCurrentRollingLogFilename(String fileName) {
        setString(getAppPreferences(), ROLLING_LOG_FILE_NAME, fileName);
    }

    public String getCurrentRollingLogFilename() {
        return getAppPreferences().getString(ROLLING_LOG_FILE_NAME, null);
    }

    public String getAuthUrl() {
        CloudEnvironment env = getEnvironment();
        return env.getUrl();
    }

    public CloudEnvironment getEnvironment() {
        return (CloudEnvironment) getEnum(getDebugPreferences(), ENV_KEY, CloudEnvironment.getDefault());
    }

    public void setEnvironment(CloudEnvironment env) {
        setEnum(getDebugPreferences(), ENV_KEY, env);
    }

    public String getCustomEnvironmentUrl() {
        return getDebugPreferences().getString(CUSTOM_ENVIRONMENT_URL_KEY, null);
    }

    public void setCustomEnvironmentUrl(String authUrl) {
        setString(getDebugPreferences(), CUSTOM_ENVIRONMENT_URL_KEY, authUrl);
    }

    public String getAppVersion() {
        return getAppPreferences().getString(APP_VERSION_KEY, null);
    }

    public void setAppVersion(String version) {
        setString(getAppPreferences(), APP_VERSION_KEY, version);
    }

    public String getThirdPartyPartnersPortalEndpoint() {
        return getAppPreferences().getString(THIRD_PARTY_PARTNERS_PORTAL_ENDPOINT_KEY, null);
    }

    public void setThirdPartyPartnersPortalEndpoint(String endpoint) {
        setString(getAppPreferences(), THIRD_PARTY_PARTNERS_PORTAL_ENDPOINT_KEY, endpoint);
    }

    public String getDeviceFirmwareVersion() {
        return getAppPreferences().getString(DEVICE_FW_VERSION_KEY, null);
    }

    public void setDeviceFirmwareVersion(String version) {
        setString(getAppPreferences(), DEVICE_FW_VERSION_KEY, version);
    }

    public void clearLastSyncDataForCustomStrapp(UUID uuid) {
        setString(getStrappPreferences(), uuid.toString(), "");
    }

    public void setSyncDataForCustomStrapp(UUID uuid, ArrayList<ArrayList<StrappPageElement>> strappPageElements) {
        String strappSyncData = new Gson().toJson(strappPageElements);
        setString(getStrappPreferences(), uuid.toString(), strappSyncData);
    }

    public boolean isSyncDataDifferentThanLastSyncData(UUID uuid, ArrayList<ArrayList<StrappPageElement>> elements) {
        String oldValues = getStrappPreferences().getString(uuid.toString(), null);
        String newValues = new Gson().toJson(elements);
        return !newValues.equals(oldValues);
    }

    public void setNotificationDataForCustomStrapp(UUID uuid, KNotification notification) {
        String strappSyncData = notification.getTitle() + notification.getSubtitle();
        setString(getStrappPreferences(), uuid.toString(), strappSyncData);
    }

    public boolean isNotificationDataDifferentThanLastSyncData(UUID uuid, KNotification notification) {
        String oldValues = getStrappPreferences().getString(uuid.toString(), null);
        String newValues = notification.getTitle() + notification.getSubtitle();
        return !newValues.equals(oldValues);
    }

    public DateTime getLastStrappSyncTime(UUID uuid) {
        return getDateTime(getStrappPreferences(), String.format("%s:%s", uuid.toString(), LAST_SYNC_TIME_KEY));
    }

    public void setLastStrappSyncTime(UUID uuid, DateTime syncTime) {
        setDateTime(getStrappPreferences(), String.format("%s:%s", uuid.toString(), LAST_SYNC_TIME_KEY), syncTime);
    }

    public DateTime getLastSyncTime() {
        return getDateTime(getSyncPreferences(), LAST_SYNC_TIME_KEY);
    }

    public void setLastSyncTime(DateTime syncTime) {
        setDateTime(getSyncPreferences(), LAST_SYNC_TIME_KEY, syncTime);
    }

    public DateTime getLastSyncTimeForDevice(MultiDeviceConstants.DeviceType deviceType) {
        return getDateTime(getSyncPreferences(), LAST_SYNC_TIME_KEY + deviceType);
    }

    public void setLastSyncTimeForDevice(DateTime syncTime, MultiDeviceConstants.DeviceType deviceType) {
        setDateTime(getSyncPreferences(), LAST_SYNC_TIME_KEY + deviceType, syncTime);
    }

    public DateTime getLastHomeRefreshTime() {
        return getDateTime(getAppPreferences(), LAST_HOME_REFRESH_TIME_KEY);
    }

    public void setLastHomeRefreshTime(DateTime refreshTime) {
        setDateTime(getAppPreferences(), LAST_HOME_REFRESH_TIME_KEY, refreshTime);
    }

    public void setSyncDebugInfo(String syncDebugInfo) {
        setString(getDebugPreferences(), SYNC_DEBUG_INFORMATION, syncDebugInfo);
    }

    public String getSyncDebugInfo() {
        return getDebugPreferences().getString(SYNC_DEBUG_INFORMATION, null);
    }

    public long getSyncNumber() {
        return getSyncPreferences().getLong(SYNC_NUMBER_KEY, 0L);
    }

    public void setSyncNumber(long value) {
        setLong(getSyncPreferences(), SYNC_NUMBER_KEY, value);
    }

    public boolean setUserProfile(CargoUserProfile profile) {
        boolean profileSaveSucceed;
        synchronized (this.mUserProfileLock) {
            try {
                String userProfileString = GsonUtils.getCustomSerializer().toJson(profile);
                String encryptedUserProfile = this.mCryptoProvider.encrypt(userProfileString, 0);
                profileSaveSucceed = setString(getAppPreferences(), PROFILE_NAME, encryptedUserProfile);
                if (profileSaveSucceed) {
                    this.mCacheCargoUserProfile = null;
                    Intent profileUpdateIntent = new Intent(Constants.INTENT_USER_PROFILE_UPDATED);
                    LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(profileUpdateIntent);
                }
            } catch (CryptoException e) {
                KLog.e(TAG, "Unable to encrypt profile");
                return false;
            }
        }
        return profileSaveSucceed;
    }

    public CargoUserProfile getUserProfile() {
        CargoUserProfile profile = null;
        synchronized (this.mUserProfileLock) {
            if (this.mCacheCargoUserProfile != null) {
                profile = this.mCacheCargoUserProfile;
            } else {
                String profileData = getAppPreferences().getString(PROFILE_NAME, null);
                if (profileData != null) {
                    String decryptedProfileData = null;
                    try {
                        decryptedProfileData = this.mCryptoProvider.decrypt(profileData, 0);
                    } catch (CryptoException e) {
                        KLog.e(TAG, "Unable to decrypt profile");
                    }
                    if (decryptedProfileData != null) {
                        profile = (CargoUserProfile) GsonUtils.getCustomDeserializer().fromJson(decryptedProfileData, (Class<Object>) CargoUserProfile.class);
                        this.mCacheCargoUserProfile = profile;
                    }
                }
            }
        }
        return profile;
    }

    public void setCheckedFirmwareUpdateInfo(CheckedFirmwareUpdateInfo firmwareUpdateInfo) {
        if (firmwareUpdateInfo != null) {
            setString(getAppPreferences(), CHECKED_FIRMWARE_UPDATE_INFO, GsonUtils.getCustomSerializer().toJson(firmwareUpdateInfo));
        } else {
            setString(getAppPreferences(), CHECKED_FIRMWARE_UPDATE_INFO, null);
        }
    }

    public CheckedFirmwareUpdateInfo getCheckedFirmwareUpdateInfo() {
        String firmwareUpdateInfo = getAppPreferences().getString(CHECKED_FIRMWARE_UPDATE_INFO, null);
        if (firmwareUpdateInfo == null) {
            return null;
        }
        return (CheckedFirmwareUpdateInfo) GsonUtils.getCustomDeserializer().fromJson(firmwareUpdateInfo, (Class<Object>) CheckedFirmwareUpdateInfo.class);
    }

    public HashMap<String, List<String>> getLastWorkoutFilter() {
        if (mLastWorkoutFilter.get() == null) {
            mLastWorkoutFilter.set(new HashMap<>());
        }
        return (HashMap) mLastWorkoutFilter.get().clone();
    }

    public void setLastWorkoutFilter(HashMap<String, List<String>> lastFilterList) {
        Validate.notNull(lastFilterList, "lastFilterList");
        mLastWorkoutFilter.set((HashMap) lastFilterList.clone());
    }

    public String getLastGuidedWorkoutFilterString() {
        return mLastGuidedWorkoutFilterString.get();
    }

    public void setLastGuidedWorkoutFilterString(String filterString) {
        mLastGuidedWorkoutFilterString.set(filterString);
    }

    private String getFreStatusKey() {
        return FRE_STATUS_KEY_PREFIX + getAuthUrl().toLowerCase(Locale.US);
    }

    public void setCurrentWallpaperId(int wallpaperId) {
        setIsDevicePersonalizationInitialized(true);
        setInt(getAppPreferences(), WALLPAPER_SETTING_KEY, wallpaperId);
    }

    public int getCurrentWallpaperId() {
        return getAppPreferences().getInt(WALLPAPER_SETTING_KEY, -1);
    }

    public boolean setOobeUserProfile(String hwagInfo) {
        try {
            String encrypted = this.mCryptoProvider.encrypt(hwagInfo, 0);
            return setString(getAppPreferences(), TEMP_OOBE_HWAG_INFORMATION, encrypted);
        } catch (CryptoException e) {
            KLog.e(TAG, "Unable to encrypt profile");
            return false;
        }
    }

    public String getOobeUserProfile() {
        String hwagInfo = getAppPreferences().getString(TEMP_OOBE_HWAG_INFORMATION, "");
        if (!TextUtils.isEmpty(hwagInfo)) {
            String decrypted = null;
            try {
                decrypted = this.mCryptoProvider.decrypt(hwagInfo, 0);
            } catch (CryptoException e) {
                KLog.e(TAG, "Unable to decrypt profile");
            }
            return decrypted;
        }
        return hwagInfo;
    }

    public void setDeviceName(String deviceName) {
        setString(getAppPreferences(), TEMP_OOBE_DEVICE_NAME, deviceName);
    }

    public String getDeviceName() {
        return getAppPreferences().getString(TEMP_OOBE_DEVICE_NAME, "");
    }

    public boolean isTelemetryEnabled() {
        return true;
    }

    public RunDisplayMetricType[] getRunMetricsOrder(BandVersion version) {
        String runMetricsArrayString = getAppPreferences().getString(RUN_METRICS_ORDER, null);
        if (runMetricsArrayString != null) {
            RunDisplayMetricType[] metrics = (RunDisplayMetricType[]) new Gson().fromJson(runMetricsArrayString, (Class<Object>) RunDisplayMetricType[].class);
            boolean isProperLength = metrics.length >= (version.equals(BandVersion.NEON) ? 7 : 3);
            if (version == BandVersion.getValueOf(getAppPreferences().getString(LAST_RUN_METRICS_SAVED_VERSION, BandVersion.UNKNOWN.toString())) && isProperLength) {
                return metrics;
            }
        }
        return EventUtils.getDefaultRunMetricsOrder(version);
    }

    public void setRunMetricsOrder(BandVersion version, RunDisplayMetricType... metrics) {
        String runMetricsArrayString = new Gson().toJson(metrics);
        setString(getAppPreferences(), RUN_METRICS_ORDER, runMetricsArrayString);
        setString(getAppPreferences(), LAST_RUN_METRICS_SAVED_VERSION, version.toString());
    }

    public BikeDisplayMetricType[] getBikingMetricsOrder(BandVersion version) {
        String bikingMetricsArrayString = getAppPreferences().getString(BIKING_METRICS_ORDER, null);
        if (bikingMetricsArrayString != null) {
            BikeDisplayMetricType[] metrics = (BikeDisplayMetricType[]) new Gson().fromJson(bikingMetricsArrayString, (Class<Object>) BikeDisplayMetricType[].class);
            boolean isProperLength = metrics.length >= (version.equals(BandVersion.NEON) ? 7 : 3);
            if (version == BandVersion.getValueOf(getAppPreferences().getString(LAST_BIKE_METRICS_SAVED_VERSION, BandVersion.UNKNOWN.toString())) && isProperLength) {
                return metrics;
            }
        }
        return EventUtils.getDefaultBikingMetricsOrder(version);
    }

    public void setBikingMetricsOrder(BandVersion version, BikeDisplayMetricType... metrics) {
        String bikeMetricsArrayString = new Gson().toJson(metrics);
        setString(getAppPreferences(), BIKING_METRICS_ORDER, bikeMetricsArrayString);
        setString(getAppPreferences(), LAST_BIKE_METRICS_SAVED_VERSION, version.toString());
    }

    public void setWeatherLocationLatitude(float latitudeSetting) {
        setFloat(getAppPreferences(), WEATHER_LOCATION_LATITUDE, latitudeSetting);
    }

    public void setWeatherLocationLongitude(float longitudeSetting) {
        setFloat(getAppPreferences(), WEATHER_LOCATION_LONGITUDE, longitudeSetting);
    }

    public float getWeatherLocationLatitude() {
        return getAppPreferences().getFloat(WEATHER_LOCATION_LATITUDE, 40.67f);
    }

    public float getWeatherLocationLongitude() {
        return getAppPreferences().getFloat(WEATHER_LOCATION_LONGITUDE, -73.94f);
    }

    private <E extends Enum<E>> E getEnum(SharedPreferences prefs, String key, E defaultValue) {
        String enumName = prefs.getString(key, null);
        if (enumName != null) {
            try {
                return (E) Enum.valueOf(defaultValue.getClass(), enumName);
            } catch (IllegalArgumentException e) {
                KLog.w(TAG, "Unsupported enum value trying to read preference key %s = %s. Using default value.", key, enumName);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private <E extends Enum<E>> void setEnum(SharedPreferences prefs, String key, E value) {
        setString(prefs, key, value.name());
    }

    private boolean setString(SharedPreferences prefs, String key, String value) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(key, value);
        return prefsEditor.commit();
    }

    private boolean setBoolean(SharedPreferences prefs, String key, boolean value) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(key, value);
        return prefsEditor.commit();
    }

    private boolean setFloat(SharedPreferences prefs, String key, float value) {
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putFloat(key, value);
        return prefsEditor.commit();
    }

    private boolean setDateTime(SharedPreferences prefs, String key, DateTime value) {
        if (value != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(Constants.SETTINGS_DATETIME_PATTERN);
            String dateStr = formatter.print(value);
            return setString(prefs, key, dateStr);
        }
        return setString(prefs, key, null);
    }

    private DateTime getDateTime(SharedPreferences prefs, String key) {
        String value = prefs.getString(key, null);
        if (value == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern(Constants.SETTINGS_DATETIME_PATTERN);
        DateTime lastRefreshTime = formatter.parseDateTime(value);
        return lastRefreshTime;
    }

    private boolean setInt(SharedPreferences prefs, String key, int value) {
        Validate.notNull(prefs, "prefs");
        Validate.notNullOrEmpty(key, "key");
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt(key, value);
        return prefsEditor.commit();
    }

    private boolean setLong(SharedPreferences prefs, String key, long value) {
        Validate.notNull(prefs, "prefs");
        Validate.notNullOrEmpty(key, "key");
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putLong(key, value);
        return prefsEditor.commit();
    }

    private SharedPreferences getAppPreferences() {
        return getSharedPreferences(APP_PREFERENCES_KEY);
    }

    private SharedPreferences getSyncPreferences() {
        return getSharedPreferences(SYNC_PREFERENCES_KEY);
    }

    private SharedPreferences getDebugPreferences() {
        return getSharedPreferences(DEBUG_PREFERENCES_KEY);
    }

    private SharedPreferences getStrappPreferences() {
        return getSharedPreferences(String.format(STRAPP_PREFERENCES_KEY, new Object[0]));
    }

    private SharedPreferences getSharedPreferences(String name) {
        return this.mContext.getSharedPreferences(name, 0);
    }

    public void setLastRunClickedTime() {
        setDateTime(getStrappPreferences(), LAST_RUN_CLICK_TIME_KEY, DateTime.now());
    }

    public boolean isSinceLastRunClickedTime(DateTime runTime) {
        return checkTileTime(runTime, LAST_RUN_CLICK_TIME_KEY);
    }

    public void setLastBikingClickedTime() {
        setDateTime(getStrappPreferences(), LAST_BIKING_CLICK_TIME_KEY, DateTime.now());
    }

    public boolean isSinceLastBikingClickedTime(DateTime eventTime) {
        return checkTileTime(eventTime, LAST_BIKING_CLICK_TIME_KEY);
    }

    public void setLastWorkoutClickedTime() {
        setDateTime(getStrappPreferences(), LAST_WORKOUT_CLICK_TIME_KEY, DateTime.now());
    }

    public void setLastGuidedWorkoutClickedTime() {
        setDateTime(getStrappPreferences(), LAST_GUIDED_WORKOUT_CLICK_TIME_KEY, DateTime.now());
    }

    public boolean isSinceLastWorkoutClickedTime(DateTime workoutTime) {
        return checkTileTime(workoutTime, LAST_WORKOUT_CLICK_TIME_KEY);
    }

    public boolean isSinceLastGuidedWorkoutClickedTime(DateTime guidedWorkoutTime) {
        return checkTileTime(guidedWorkoutTime, LAST_GUIDED_WORKOUT_CLICK_TIME_KEY);
    }

    public void setLastGuidedWorkoutCalendarTileClickedTime() {
        setDateTime(getStrappPreferences(), LAST_GUIDED_WORKOUT_CALENDAR_CLICK_TIME_KEY, DateTime.now());
    }

    public boolean hasGuidedWorkoutCalendarTileBeenClicked() {
        DateTime lastClickTime = getSavedTime(LAST_GUIDED_WORKOUT_CALENDAR_CLICK_TIME_KEY);
        return lastClickTime != null;
    }

    public boolean isSinceLastGuidedWorkoutCalendarTileClickedTime(DateTime workoutTime) {
        return checkTileTime(workoutTime, LAST_GUIDED_WORKOUT_CALENDAR_CLICK_TIME_KEY);
    }

    public void setLastSleepClickedTime() {
        setDateTime(getStrappPreferences(), LAST_SLEEP_CLICK_TIME_KEY, DateTime.now());
    }

    public boolean isSinceLastSleepClickedTime(DateTime sleepTime) {
        return checkTileTime(sleepTime, LAST_SLEEP_CLICK_TIME_KEY);
    }

    public void setFirstRunTime(DateTime firstRunTime) {
        setDateTime(getStrappPreferences(), FIRST_RUN_KEY, firstRunTime);
    }

    public boolean isSinceLastGolfClickedTime(DateTime golfTime) {
        return checkTileTime(golfTime, LAST_GOLF_CLICK_TIME_KEY);
    }

    public void setLastGolfTileClickedTime() {
        setDateTime(getStrappPreferences(), LAST_GOLF_CLICK_TIME_KEY, DateTime.now());
    }

    public void saveStarbucksCardNumber(String starbucksCardNumber) {
        String oldNumber = getStarbucksCardNumber();
        if ((oldNumber == null && starbucksCardNumber != null) || ((starbucksCardNumber == null && oldNumber != null) || (oldNumber != null && oldNumber.compareTo(starbucksCardNumber) != 0))) {
            KLog.v(TAG, "Telemetry Event a starbucks card has changed");
            Telemetry.logEvent(TelemetryConstants.Events.CONNECTED_SETTINGS_BAND_MANAGE_TILES_STARBUCKS_SEND_CARD_TO_BAND);
        }
        setString(getAppPreferences(), STARBUCKS_CARD_NUMBER, starbucksCardNumber);
    }

    public String getStarbucksCardNumber() {
        return getAppPreferences().getString(STARBUCKS_CARD_NUMBER, "");
    }

    public void saveNotificationCenterApps(ArrayList<String> notificationCenterApps) {
        Gson gson = new Gson();
        setString(getAppPreferences(), NOTIFICATION_CENTER_APPS, gson.toJson(notificationCenterApps));
    }

    public ArrayList<String> getNotificationCenterApps() {
        String notificationCenterApps = getAppPreferences().getString(NOTIFICATION_CENTER_APPS, null);
        ArrayList<String> appList = new ArrayList<>();
        String[] apps = (String[]) new Gson().fromJson(notificationCenterApps, (Class<Object>) String[].class);
        if (apps != null) {
            appList.addAll(Arrays.asList(apps));
        }
        return appList;
    }

    private boolean checkTileTime(DateTime time, String key) {
        DateTime lastClickTime = getSavedTime(key);
        if (lastClickTime == null || time == null) {
            return true;
        }
        return time.isAfter(lastClickTime);
    }

    public DateTime getSavedTime(String key) {
        return getDateTime(getStrappPreferences(), key);
    }

    public boolean isFirstRun() {
        DateTime firstRunTime = getSavedTime(FIRST_RUN_KEY);
        return firstRunTime != null && Days.daysBetween(firstRunTime, DateTime.now()).getDays() < 1;
    }

    public ArrayList<StockCompanyInformation> getStockCompanies() {
        ArrayList<StockCompanyInformation> watchedStocksList = null;
        String stockCompanyInformationData = getAppPreferences().getString(USER_STOCKS, null);
        if (stockCompanyInformationData != null) {
            watchedStocksList = new ArrayList<>();
            StockCompanyInformation[] watchedStocks = (StockCompanyInformation[]) new Gson().fromJson(stockCompanyInformationData, (Class<Object>) StockCompanyInformation[].class);
            if (watchedStocks != null) {
                watchedStocksList.addAll(Arrays.asList(watchedStocks));
            }
        }
        return watchedStocksList;
    }

    public void saveStockCompanies(ArrayList<StockCompanyInformation> stockCompanies) {
        Gson gson = new Gson();
        String uuidString = gson.toJson(stockCompanies.toArray());
        setString(getAppPreferences(), USER_STOCKS, uuidString);
    }

    public boolean isSinceLastCaloriesTileClickedTime() {
        return checkTileTime(getLastSyncTime(), LAST_CALORIES_CLICK_TIME_KEY);
    }

    public void setLastCaloriesTileClickedTime() {
        setDateTime(getStrappPreferences(), LAST_CALORIES_CLICK_TIME_KEY, DateTime.now());
    }

    public boolean isSinceLastStepsTileClickedTime() {
        return checkTileTime(getLastSyncTime(), LAST_STEPS_CLICK_TIME_KEY);
    }

    public void setLastStepsTileClickedTime() {
        setDateTime(getStrappPreferences(), LAST_STEPS_CLICK_TIME_KEY, DateTime.now());
    }

    public void setNumberOfAllowedStrapps(int numOfAllowedStrapps) {
        setInt(getAppPreferences(), NUMBER_OF_ALLOWED_STRAPPS, numOfAllowedStrapps);
    }

    public int getNumberOfAllowedStrapps() {
        return getAppPreferences().getInt(NUMBER_OF_ALLOWED_STRAPPS, 10);
    }

    public void setIsInNoDevicePairedModeState(Boolean isInPlusMode) {
        setBoolean(getAppPreferences(), DEVICE_IN_PLUS_MODE, isInPlusMode.booleanValue());
    }

    public boolean isInNoDevicePairedMode() {
        return getAppPreferences().getBoolean(DEVICE_IN_PLUS_MODE, false);
    }

    private static void clearSharedPreferences(SharedPreferences sharedPreferences) {
        Validate.notNull(sharedPreferences, "sharedPreferences");
        sharedPreferences.edit().clear().commit();
    }

    public void handleLogout() {
        setFreStatus(FreStatus.NOT_SHOWN);
        clearSharedPreferences(getAppPreferences());
        clearSharedPreferences(getDebugPreferences());
        clearSharedPreferences(getStrappPreferences());
        clearSharedPreferences(getSyncPreferences());
    }

    public void setResyncLastWorkoutNextSync(boolean isAfterMiniOobe) {
        setBoolean(getAppPreferences(), RESYNC_GW_AFTER_MINI_OOBE_KEY, isAfterMiniOobe);
    }

    public boolean getResyncLastWorkoutNextSync() {
        return getAppPreferences().getBoolean(RESYNC_GW_AFTER_MINI_OOBE_KEY, false);
    }

    public int getBikeSplitDistance() {
        return getAppPreferences().getInt(BIKE_SPLIT_MULTIPLIER, 1);
    }

    public void setBikeSplitDistance(Integer bikeSplitDistance) {
        setInt(getAppPreferences(), BIKE_SPLIT_MULTIPLIER, bikeSplitDistance.intValue());
    }

    public boolean shouldOobeConnectBand() {
        return getAppPreferences().getBoolean(OOBE_BAND_CONNECTION_SKIPPED, false);
    }

    public void setShouldOobeConnectBand(Boolean connect) {
        setBoolean(getAppPreferences(), OOBE_BAND_CONNECTION_SKIPPED, connect.booleanValue());
    }

    public boolean shouldOobeConnectPhone() {
        return getAppPreferences().getBoolean(OOBE_SHOULD_CONNECT_PHONE, false);
    }

    public void setShouldOobeConnectPhone(Boolean connect) {
        setBoolean(getAppPreferences(), OOBE_SHOULD_CONNECT_PHONE, connect.booleanValue());
    }

    public boolean isDeviceConnectionFlow() {
        return getAppPreferences().getBoolean(OOBE_OR_DEVICE_CONNECTION, false);
    }

    public void setIsDeviceConnectionFlow(Boolean isDeviceConnection) {
        setBoolean(getAppPreferences(), OOBE_OR_DEVICE_CONNECTION, isDeviceConnection.booleanValue());
    }

    public String getSessionHeaderValue() {
        return getDebugPreferences().getString(DEBUG_HEADER_SESSION_NAME, "");
    }

    public void setSessionHeaderValue(String headerValue) {
        setString(getDebugPreferences(), DEBUG_HEADER_SESSION_NAME, headerValue);
    }

    public boolean isDevicePersonalizationInitialized() {
        return getAppPreferences().getBoolean(DEVICE_PERSONALIZATION_INITIALIZED, false);
    }

    public void setIsDevicePersonalizationInitialized(Boolean isPersonalizationInitialized) {
        setBoolean(getAppPreferences(), DEVICE_PERSONALIZATION_INITIALIZED, isPersonalizationInitialized.booleanValue());
    }

    public void updateLastSyncedGolfCourse(String courseId, String teeId, DateTime syncTime) {
        setString(getAppPreferences(), SYNC_GOLF_COURSE_ID, courseId);
        setString(getAppPreferences(), SYNC_GOLF_COURSE_TEE_ID, teeId);
        setLastSyncedGolfTime(syncTime);
    }

    public Pair<String, String> getSyncedGolfCourse() {
        String courseId = getAppPreferences().getString(SYNC_GOLF_COURSE_ID, null);
        String teeId = getAppPreferences().getString(SYNC_GOLF_COURSE_TEE_ID, null);
        return new Pair<>(courseId, teeId);
    }

    public DateTime getLastSyncedGolfTime() {
        return getDateTime(getStrappPreferences(), SYNC_GOLF_TIMESTAMP);
    }

    public void setLastSyncedGolfTime(DateTime syncTime) {
        setDateTime(getStrappPreferences(), SYNC_GOLF_TIMESTAMP, syncTime);
    }

    public boolean isNotificationWhatsNewEnabled() {
        return getAppPreferences().getBoolean(NOTIFICATION_WHATS_NEW, false);
    }

    public void setNotificationWhatsNewEnabled(boolean enabled) {
        setBoolean(getAppPreferences(), NOTIFICATION_WHATS_NEW, enabled);
    }

    public int getAppSessionCountBeforeClickWhatsNew() {
        return getAppPreferences().getInt(APP_SESSION_COUNT_BEFORE_CLICK_WHATS_NEW, 0);
    }

    public void setAppSessionCountBeforeClickWhatsNew(int count) {
        setInt(getAppPreferences(), APP_SESSION_COUNT_BEFORE_CLICK_WHATS_NEW, count);
    }

    public void clearSettingsWhileDeviceUnregister() {
        setUUIDsOnDevice(new ArrayList<>());
        updateLastSyncedGolfCourse("", "", DateTime.now());
    }

    public int getWhatsNewCardsVersion() {
        return getAppPreferences().getInt(WHATS_NEW_CARDS_VERSION, 0);
    }

    public void setWhatsNewCardsVersion(int hashCode) {
        setInt(getAppPreferences(), WHATS_NEW_CARDS_VERSION, hashCode);
    }

    public boolean isShakeToSendFeedbackEnabled() {
        return getAppPreferences().getBoolean(IS_SHAKE_TO_SEND_FEEDBACK_ENABLED, true);
    }

    public void setShakeToSendFeedbackEnabled(boolean isEnabled) {
        setBoolean(getAppPreferences(), IS_SHAKE_TO_SEND_FEEDBACK_ENABLED, isEnabled);
    }

    public void setFUSEndPoint(String endPoint) {
        setString(getAppPreferences(), FUS_END_POINT, endPoint);
    }

    public String getFUSEndPoint() {
        return getAppPreferences().getString(FUS_END_POINT, "");
    }

    public String getBandRegionSettings() {
        return getAppPreferences().getString(USER_SAVED_BAND_REGION_SETTINGS, Locale.getDefault().getCountry());
    }

    public void setBandRegionSettings(String region) {
        setString(getAppPreferences(), USER_SAVED_BAND_REGION_SETTINGS, region);
    }

    public BandVersion getOobeUiBandVersion() {
        return BandVersion.getValueOf(getAppPreferences().getString(OOBE_BAND_UI_VERSION, BandVersion.UNKNOWN.toString()));
    }

    public void setOobeUiBandVersion(BandVersion bandVersion) {
        setString(getAppPreferences(), OOBE_BAND_UI_VERSION, bandVersion.toString());
    }
}
