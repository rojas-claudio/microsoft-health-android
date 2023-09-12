package com.microsoft.kapp;

import android.content.Context;
import com.microsoft.band.service.crypto.CryptoProvider;
import com.microsoft.kapp.cache.Cache;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.calendar.CalendarEventsDataProvider;
import com.microsoft.kapp.calendar.CalendarManager;
import com.microsoft.kapp.calendar.CalendarObserver;
import com.microsoft.kapp.feedback.FeedbackService;
import com.microsoft.kapp.feedback.FeedbackUtilsV1;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.LogConfiguration;
import com.microsoft.kapp.logging.LogFormatManager;
import com.microsoft.kapp.logging.LogListProvider;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.models.strapp.DefaultStrappManager;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager;
import com.microsoft.kapp.multidevice.SyncManager;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.providers.golf.ScorecardProvider;
import com.microsoft.kapp.sensor.PhoneSensorDataProvider;
import com.microsoft.kapp.sensor.SensorDataDebugProvider;
import com.microsoft.kapp.sensor.SensorDataLogger;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.sensor.db.SensorDB;
import com.microsoft.kapp.sensor.upload.BandSensorDataUploader;
import com.microsoft.kapp.sensor.upload.EventSensorDataUploader;
import com.microsoft.kapp.services.CallDismissManager;
import com.microsoft.kapp.services.KAppsUpdater;
import com.microsoft.kapp.services.LocationService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.TMAG.TMAGService;
import com.microsoft.kapp.services.bedrock.BedrockRestService;
import com.microsoft.kapp.services.finance.FinanceService;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler;
import com.microsoft.kapp.services.weather.WeatherService;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.tasks.FirmwareUpdateTask;
import com.microsoft.kapp.telephony.MessageMetadataRetriever;
import com.microsoft.kapp.telephony.MessagesManager;
import com.microsoft.kapp.telephony.MessagesObserver;
import com.microsoft.kapp.telephony.SmsRequestManager;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.GolfUtils;
import com.microsoft.kapp.utils.GooglePlayUtils;
import com.microsoft.kapp.utils.StubDialogManager;
import com.microsoft.kapp.utils.stat.StatUtils;
import com.microsoft.kapp.version.ApplicationUpdateLauncher;
import com.microsoft.krestsdk.auth.KdsAuth;
import com.microsoft.krestsdk.auth.KdsFetcher;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialStore;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.services.NetworkProvider;
import com.microsoft.krestsdk.services.ProviderListFetcher;
import com.microsoft.krestsdk.services.RestService;
import com.microsoft.krestsdk.services.UserAgentProvider;
import dagger.internal.Binding;
import dagger.internal.BindingsGroup;
import dagger.internal.Linker;
import dagger.internal.ModuleAdapter;
import dagger.internal.ProvidesBinding;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KAppModule$$ModuleAdapter extends ModuleAdapter<KAppModule> {
    private static final String[] INJECTS = {"members/com.microsoft.kapp.fragments.AboutFragment", "members/com.microsoft.kapp.tasks.GuidedWorkout.AddFavoritesOperation", "members/com.microsoft.kapp.services.background.AppConfigurationService", "members/com.microsoft.kapp.fragments.AttachmentFragment", "members/com.microsoft.kapp.services.background.AutoRestartService", "members/com.microsoft.kapp.activities.BaseActivityAdapter", "members/com.microsoft.kapp.activities.BaseFragmentActivity", "members/com.microsoft.kapp.fragments.bike.BikeDetailsSplitFragment", "members/com.microsoft.kapp.fragments.bike.BikeDetailsSummaryFragment", "members/com.microsoft.kapp.widgets.BikeHomeTile", "members/com.microsoft.kapp.activities.settings.BikeSettingsActivity", "members/com.microsoft.kapp.activities.BingMapActivity", "members/com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment", "members/com.microsoft.kapp.services.background.CacheCleanupService", "members/com.microsoft.kapp.services.CalendarSyncNotificationHandler", "members/com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation", "members/com.microsoft.kapp.services.CallNotificationHandler", "members/com.microsoft.kapp.fragments.calories.CaloriesDailyFragment", "members/com.microsoft.kapp.activities.CaloriesEditGoalActivity", "members/com.microsoft.kapp.widgets.CaloriesHomeTile", "members/com.microsoft.kapp.fragments.calories.CaloriesWeeklyFragment", "members/com.microsoft.kapp.views.ClearableEditText", "members/com.microsoft.kapp.models.golf.CourseDetails", "members/com.microsoft.kapp.fragments.CredentialsFragment", "members/com.microsoft.kapp.views.CustomFontButton", "members/com.microsoft.kapp.views.CustomFontEditText", "members/com.microsoft.kapp.views.CustomFontTextClock", "members/com.microsoft.kapp.views.CustomFontTextGlyphView", "members/com.microsoft.kapp.views.CustomFontTextView", "members/com.microsoft.kapp.style.text.CustomStyleToSpan", "members/com.microsoft.kapp.views.CircularSeekBar", "members/com.microsoft.kapp.views.CustomGlyphView", "members/com.microsoft.kapp.views.CustomGlyphViewWithBackground", "members/com.microsoft.kapp.widgets.PagerTitleStrip$CustomTitleTextView", "members/com.microsoft.kapp.fragments.DebugActionsFragment", "members/com.microsoft.kapp.activities.DebugActivity", "members/com.microsoft.kapp.fragments.DebugCalendarFragment", "members/com.microsoft.kapp.fragments.DebugSettingsFragment", "members/com.microsoft.kapp.fragments.debug.DebugSensorFragment", "members/com.microsoft.kapp.fragments.DebugVersionFragment", "members/com.microsoft.kapp.calendar.DefaultCalendarEventsDataProvider", "members/com.microsoft.kapp.calendar.DefaultCalendarManager", "members/com.microsoft.kapp.calendar.DefaultCalendarObserver", "members/com.microsoft.kapp.telephony.DefaultMessagesManager", "members/com.microsoft.kapp.telephony.DefaultSmsRequestManager", "members/com.microsoft.kapp.models.strapp.DefaultStrappManager", "members/com.microsoft.kapp.activities.DeviceColorPersonalizationActivity", "members/com.microsoft.kapp.activities.DeviceConnectActivity", "members/com.microsoft.kapp.activities.DeviceConnectionErrorActivity", "members/com.microsoft.kapp.activities.DeviceWallpaperPersonalizationActivity", "members/com.microsoft.kapp.activities.EditGoalActivity", "members/com.microsoft.kapp.fragments.EditGoalDialogFragment", "members/com.microsoft.kapp.services.EmailNotificationHandler", "members/com.microsoft.kapp.fragments.EventHistorySummaryFragment", "members/com.microsoft.kapp.fragments.exercise.ExerciseDetailsSummaryFragmentV1", "members/com.microsoft.kapp.services.FacebookMessengerNotificationHandler", "members/com.microsoft.kapp.services.FacebookNotificationHandler", "members/com.microsoft.kapp.activities.FeedbackActivity", "members/com.microsoft.kapp.fragments.feedback.FeedbackDescriptionEditFragment", "members/com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment", "members/com.microsoft.kapp.fragments.feedback.FeedbackSummaryFragment", "members/com.microsoft.kapp.fragments.feedback.FeedbackTypeFragment", "members/com.microsoft.kapp.tasks.GuidedWorkout.FetchGuidedWorkoutDataOperation", "members/com.microsoft.kapp.activities.settings.FinanceSettingsActivity", "members/com.microsoft.kapp.tasks.FirmwareUpdateTask", "members/com.microsoft.kapp.views.FormattedNumberEditText", "members/com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment", "members/com.microsoft.kapp.activities.GolfCourseFilterActivity", "members/com.microsoft.kapp.services.golf.GolfCourseNotificationHandlerImpl", "members/com.microsoft.kapp.fragments.golf.GolfCourseProTipFragment", "members/com.microsoft.kapp.fragments.golf.GolfCourseTeePickFragment", "members/com.microsoft.kapp.models.home.GolfDataFetcher", "members/com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment", "members/com.microsoft.kapp.fragments.golf.GolfFindByRegionResultsFragment", "members/com.microsoft.kapp.activities.golf.GolfFindCourseByNameActivity", "members/com.microsoft.kapp.fragments.golf.GolfFindCourseByNameFragment", "members/com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment", "members/com.microsoft.kapp.activities.golf.GolfFindCourseByRegionActivity", "members/com.microsoft.kapp.widgets.GolfHomeTile", "members/com.microsoft.kapp.fragments.golf.GolfLandingPageFragment", "members/com.microsoft.kapp.fragments.golf.GolfNearbySearchResultsFragment", "members/com.microsoft.kapp.fragments.golf.GolfRecentSearchResultsFragment", "members/com.microsoft.kapp.activities.golf.GolfRequestACourseActivity", "members/com.microsoft.kapp.fragments.golf.GolfScorecardFragment", "members/com.microsoft.kapp.activities.golf.GolfSearchActivity", "members/com.microsoft.kapp.fragments.golf.GolfSelectRegionFragment", "members/com.microsoft.kapp.fragments.golf.GolfSelectStateFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutCarouselFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutDetailsFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBrandsFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment", "members/com.microsoft.kapp.widgets.GuidedWorkoutHomeTile", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBrowseCustomFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanOverViewFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsSearchFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutSummaryFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutTypesFragment", "members/com.microsoft.kapp.models.home.GuidedWorkoutDataFetcher", "members/com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandlerImpl", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsFavoritesFragment", "members/com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowseAllFragment", "members/com.microsoft.kapp.activities.HomeActivity", "members/com.microsoft.kapp.models.home.HomeData", "members/com.microsoft.kapp.models.home.HomeDataFetcher", "members/com.microsoft.kapp.widgets.HomeTile", "members/com.microsoft.kapp.fragments.HomeTileNoDataFragment", "members/com.microsoft.kapp.fragments.HomeTilesFragment", "members/com.microsoft.kapp.activities.HistoryFilterActivity", "members/com.microsoft.kapp.fragments.history.HistorySummaryFragment", "members/com.microsoft.kapp.logging.images.ImageLogger", "members/com.microsoft.kapp.services.KAppBroadcastReceiver", "members/com.microsoft.kapp.KApplication", "members/com.microsoft.kapp.services.background.KAppsService", "members/com.microsoft.kapp.fragments.LeftNavigationFragment", "members/com.microsoft.kapp.activities.LevelTwoBaseActivity", "members/com.microsoft.kapp.activities.LevelTwoPagesActivity", "members/com.microsoft.kapp.logging.LogCatLogger", "members/com.microsoft.kapp.logging.LogCleanupService", "members/com.microsoft.kapp.fragments.MainContentFragment", "members/com.microsoft.kapp.widgets.ManageStrappsTile", "members/com.microsoft.kapp.fragments.ManageTilesFragment", "members/com.microsoft.kapp.services.MessageNotificationHandler", "members/com.microsoft.kapp.widgets.MiniHomeTile", "members/com.microsoft.kapp.telephony.MmsSmsMessagesObserver", "members/com.microsoft.kapp.fragments.MotionSettingsFragment", "members/com.microsoft.kapp.services.NotificationCenterHandler", "members/com.microsoft.kapp.activities.settings.NotificationCenterSettingsActivity", "members/com.microsoft.kapp.fragments.NotificationCenterSettingsFragment", "members/com.microsoft.kapp.logging.notification.NotificationManagerLogger", "members/com.microsoft.kapp.logging.notification.NotificationManagerTagLogger", "members/com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment", "members/com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment", "members/com.microsoft.kapp.activities.OobeConnectPhoneActivity", "members/com.microsoft.kapp.activities.OobeEnableNotificationsActivity", "members/com.microsoft.kapp.activities.OobeFirmwareUpdateActivity", "members/com.microsoft.kapp.activities.OobeProfileActivity", "members/com.microsoft.kapp.activities.OobeReadyActivity", "members/com.microsoft.kapp.widgets.PagerTitleStrip", "members/com.microsoft.kapp.fragments.PermissionsFragment", "members/com.microsoft.kapp.personalization.PersonalizationManagerFactory", "members/com.microsoft.kapp.telephony.PhoneStateListener", "members/com.microsoft.kapp.tasks.GuidedWorkout.PushWorkoutToDeviceOperation", "members/com.microsoft.kapp.tasks.golf.PushGolfToDeviceOperation", "members/com.microsoft.kapp.logging.fileLogger.RollingFileLogger", "members/com.microsoft.kapp.fragments.run.RunDetailsSplitFragmentV1", "members/com.microsoft.kapp.fragments.run.RunDetailsSummaryFragmentV1", "members/com.microsoft.kapp.widgets.RunHomeTile", "members/com.microsoft.kapp.activities.settings.RunSettingsActivity", "members/com.microsoft.kapp.tasks.GuidedWorkout.RemoveFavoritesOperation", "members/com.microsoft.kapp.sensor.service.SensorService", "members/com.microsoft.kapp.fragments.SettingsMyBandFragment", "members/com.microsoft.kapp.fragments.SettingsPersonalizeFragment", "members/com.microsoft.kapp.fragments.SettingsPreferencesFragment", "members/com.microsoft.kapp.fragments.SettingsProfileFragment", "members/com.microsoft.kapp.activities.SignInActivity", "members/com.microsoft.kapp.fragments.SignInFragment", "members/com.microsoft.kapp.fragments.sleep.SleepDetailsSummaryFragment", "members/com.microsoft.kapp.widgets.SleepHomeTile", "members/com.microsoft.kapp.activities.SplashActivity", "members/com.microsoft.kapp.fragments.StarbucksAddCardFragment", "members/com.microsoft.kapp.fragments.StarbucksNoCardsOverviewFragment", "members/com.microsoft.kapp.fragments.StarbucksOverviewFragment", "members/com.microsoft.kapp.activities.settings.StarbucksStrappSettingsActivity", "members/com.microsoft.kapp.fragments.steps.StepDailyFragment", "members/com.microsoft.kapp.activities.StepsEditGoalActivity", "members/com.microsoft.kapp.widgets.StepsHomeTile", "members/com.microsoft.kapp.fragments.steps.StepWeeklyFragment", "members/com.microsoft.kapp.fragments.StockAddFragment", "members/com.microsoft.kapp.fragments.StockOverviewFragment", "members/com.microsoft.kapp.fragments.StockReorderFragment", "members/com.microsoft.kapp.activities.settings.StrappAutoRepliesActivity", "members/com.microsoft.kapp.activities.settings.StrappReorderActivity", "members/com.microsoft.kapp.activities.settings.SelectMetricsActivity", "members/com.microsoft.kapp.activities.settings.StrappSettingsActivity", "members/com.microsoft.kapp.strappsettings.StrappSettingsManager", "members/com.microsoft.kapp.services.background.SyncService", "members/com.microsoft.kapp.utils.SyncUtils", "members/com.microsoft.kapp.tasks.GuidedWorkout.SubscribeOperation", "members/com.microsoft.kapp.tasks.GuidedWorkout.SyncFirstWorkoutOperation", "members/com.microsoft.kapp.activities.TermsOfServiceActivity", "members/com.microsoft.kapp.activities.TMAGConnectActivity", "members/com.microsoft.kapp.fragments.TopMenuFragment", "members/com.microsoft.kapp.services.TwitterNotificationHandler", "members/com.microsoft.kapp.activities.UserEventDetailsActivity", "members/com.microsoft.kapp.tasks.GuidedWorkout.UnsubscribeOperation", "members/com.microsoft.kapp.services.VoicemailNotificationHandler", "members/com.microsoft.kapp.webtiles.WebtileDownloadActivity", "members/com.microsoft.kapp.fragments.whatsnew.WhatsNewFragment", "members/com.microsoft.kapp.fragments.whatsnew.WhatsNewCardFragment", "members/com.microsoft.kapp.activities.WhatsNewSecondaryCardActivity", "members/com.microsoft.kapp.fragments.whatsnew.WhatsNewSecondaryCardFragment", "members/com.microsoft.kapp.activities.WorkoutDetailActivity", "members/com.microsoft.kapp.widgets.WorkoutHomeTile", "members/com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivity", "members/com.microsoft.kapp.activities.WorkoutPlanFilterActivity", "members/com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1", "members/com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1"};
    private static final Class<?>[] STATIC_INJECTIONS = {KLog.class, FeedbackUtilsV1.class};
    private static final Class<?>[] INCLUDES = new Class[0];

    public KAppModule$$ModuleAdapter() {
        super(KAppModule.class, INJECTS, STATIC_INJECTIONS, false, INCLUDES, true, true);
    }

    @Override // dagger.internal.ModuleAdapter
    public void getBindings(BindingsGroup bindings, KAppModule module) {
        bindings.contributeProvidesBinding("com.microsoft.kapp.ThemeManager", new ProvideThemeManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.FontManager", new ProvideFontManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.CargoConnection", new ProvideCargoConnectionProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.DeviceStateDisplayManager", new ProvideDeviceStateDisplayManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.services.ProviderListFetcher", new ProvideProviderListFetcherProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.auth.KdsFetcher", new ProvideKdsFetcherProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.services.NetworkProvider", new ProvideNetworkProviderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.logging.http.FiddlerLogger", new ProvideFiddlerLoggerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.services.UserAgentProvider", new ProvideUserAgentProviderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.LocationService", new ProvideLocationServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("android.content.Context", new ProvideContextProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.services.RestService", new ProvideRestServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.bedrock.BedrockRestService", new ProvideBedrockRestServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.auth.credentials.CredentialStore", new ProvideCredentialStoreProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.UserProfileFetcher", new ProvideUserProfileFetcherProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.calendar.CalendarEventsDataProvider", new ProvideCalendarEventsDataProviderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.personalization.PersonalizationManagerFactory", new ProvidePersonalizationManagerFactoryProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.KAppsUpdater", new ProvideKAppsUpdaterProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.utils.GooglePlayUtils", new ProvideGooglePlayUtilsProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.ShakeDetector", new ProvideShakeDetectorProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.SettingsProvider", new ProvideSettingsProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.finance.FinanceService", new ProvideFinanceServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.weather.WeatherService", new ProvideWeatherServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", new ProvideHealthAndFitnessServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.auth.MsaAuth", new ProvideMsaAuthProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.auth.KdsAuth", new ProvideKdsAuthProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", new ProvideCredentialsManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.ContactResolver", new ProvideContactResolverProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.telephony.SmsRequestManager", new ProvideSmsRequestManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.calendar.CalendarManager", new ProvideCalendarManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.calendar.CalendarObserver", new ProvideCalendarObserverProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.version.ApplicationUpdateLauncher", new ProvideApplicationUpdateLauncherProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.telephony.MessagesManager", new ProvideMessagesManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.telephony.MessagesObserver", new ProvideMessagesObserverProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.telephony.MessageMetadataRetriever", new ProvideMessageMetadataRetrieverProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.tasks.FirmwareUpdateTask", new ProvideFirmwareUpdateTaskProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.CargoVersionRetriever", new ProvideCargoVersionRetrieverProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.cache.Cache", new ProvideCacheProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.cache.CacheService", new ProvideCacheServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.strappsettings.StrappSettingsManager", new ProvideStrappSettingsManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", new ProvideGuidedWorkoutServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.band.service.crypto.CryptoProvider", new ProvideCryptoProviderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.CallDismissManager", new ProvideCallDismissManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler", new ProvideTimeZoneChangeHandlerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.utils.DialogManagerImpl", new ProvideDialogManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.utils.StubDialogManager", new ProvideStubDialogManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.sensor.db.SensorDB", new ProvideSensorDBProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.sensor.PhoneSensorDataProvider", new ProvidePhoneSensorDataProviderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.sensor.SensorDataDebugProvider", new ProvideSensorDataDebugProviderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.sensor.SensorDataLogger", new ProvideSensorDataLoggerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.multidevice.MultiDeviceManager", new ProvideMultiDeviceManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.sensor.upload.EventSensorDataUploader", new ProvideEventSensorDataUploaderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.sensor.upload.BandSensorDataUploader", new ProvideBandSensorDataUploaderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.multidevice.SyncManager", new ProvideSyncManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager", new ProvideSingleDeviceEnforcementManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.golf.GolfService", new ProvideGolfServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.providers.golf.ScorecardProvider", new ProvideScorecardProviderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.services.TMAG.TMAGService", new ProvideTMAGServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.sensor.SensorUtils", new ProvideSensorUtilsProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.utils.stat.StatUtils", new ProvideStatUtilsProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.utils.GolfUtils", new ProvideGolfUtilsProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.logging.LogConfiguration", new ProvideLogConfigurationProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.logging.LogFormatManager", new ProvideLogFormatManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.logging.LogListProvider", new ProvideLogListProviderProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.feedback.FeedbackService", new ProvideFeedbackServiceProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.utils.AppConfigurationManager", new ProvideAppConfigurationManagerProvidesAdapter(module));
        bindings.contributeProvidesBinding("com.microsoft.kapp.models.strapp.DefaultStrappManager", new ProvideDefaultStrappManagerProvidesAdapter(module));
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideThemeManagerProvidesAdapter extends ProvidesBinding<ThemeManager> implements Provider<ThemeManager> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideThemeManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.ThemeManager", true, "com.microsoft.kapp.KAppModule", "provideThemeManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public ThemeManager get() {
            return this.module.provideThemeManager(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideFontManagerProvidesAdapter extends ProvidesBinding<FontManager> implements Provider<FontManager> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideFontManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.FontManager", true, "com.microsoft.kapp.KAppModule", "provideFontManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public FontManager get() {
            return this.module.provideFontManager(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCargoConnectionProvidesAdapter extends ProvidesBinding<CargoConnection> implements Provider<CargoConnection> {
        private Binding<CallDismissManager> callDismissManager;
        private Binding<Context> context;
        private Binding<CredentialsManager> credentialsManager;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;
        private Binding<SingleDeviceEnforcementManager> singleDeviceEnforcementManager;
        private Binding<UserAgentProvider> userAgentProvider;

        public ProvideCargoConnectionProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.CargoConnection", true, "com.microsoft.kapp.KAppModule", "provideCargoConnection");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.credentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.userAgentProvider = linker.requestBinding("com.microsoft.krestsdk.services.UserAgentProvider", KAppModule.class, getClass().getClassLoader());
            this.singleDeviceEnforcementManager = linker.requestBinding("com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager", KAppModule.class, getClass().getClassLoader());
            this.callDismissManager = linker.requestBinding("com.microsoft.kapp.services.CallDismissManager", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.credentialsManager);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.userAgentProvider);
            getBindings.add(this.singleDeviceEnforcementManager);
            getBindings.add(this.callDismissManager);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CargoConnection get() {
            return this.module.provideCargoConnection(this.context.get(), this.credentialsManager.get(), this.settingsProvider.get(), this.userAgentProvider.get(), this.singleDeviceEnforcementManager.get(), this.callDismissManager.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideDeviceStateDisplayManagerProvidesAdapter extends ProvidesBinding<DeviceStateDisplayManager> implements Provider<DeviceStateDisplayManager> {
        private Binding<CargoConnection> cargoConnection;
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideDeviceStateDisplayManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.DeviceStateDisplayManager", true, "com.microsoft.kapp.KAppModule", "provideDeviceStateDisplayManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.cargoConnection);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public DeviceStateDisplayManager get() {
            return this.module.provideDeviceStateDisplayManager(this.context.get(), this.cargoConnection.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideProviderListFetcherProvidesAdapter extends ProvidesBinding<ProviderListFetcher> implements Provider<ProviderListFetcher> {
        private final KAppModule module;
        private Binding<NetworkProvider> provider;

        public ProvideProviderListFetcherProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.services.ProviderListFetcher", false, "com.microsoft.kapp.KAppModule", "provideProviderListFetcher");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.provider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public ProviderListFetcher get() {
            return this.module.provideProviderListFetcher(this.provider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideKdsFetcherProvidesAdapter extends ProvidesBinding<KdsFetcher> implements Provider<KdsFetcher> {
        private final KAppModule module;
        private Binding<NetworkProvider> provider;

        public ProvideKdsFetcherProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.auth.KdsFetcher", false, "com.microsoft.kapp.KAppModule", "provideKdsFetcher");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.provider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public KdsFetcher get() {
            return this.module.provideKdsFetcher(this.provider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideNetworkProviderProvidesAdapter extends ProvidesBinding<NetworkProvider> implements Provider<NetworkProvider> {
        private Binding<Context> context;
        private Binding<FiddlerLogger> fiddlerLogger;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;
        private Binding<UserAgentProvider> userAgentProvider;

        public ProvideNetworkProviderProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.services.NetworkProvider", true, "com.microsoft.kapp.KAppModule", "provideNetworkProvider");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.userAgentProvider = linker.requestBinding("com.microsoft.krestsdk.services.UserAgentProvider", KAppModule.class, getClass().getClassLoader());
            this.fiddlerLogger = linker.requestBinding("com.microsoft.kapp.logging.http.FiddlerLogger", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.userAgentProvider);
            getBindings.add(this.fiddlerLogger);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public NetworkProvider get() {
            return this.module.provideNetworkProvider(this.userAgentProvider.get(), this.fiddlerLogger.get(), this.settingsProvider.get(), this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideFiddlerLoggerProvidesAdapter extends ProvidesBinding<FiddlerLogger> implements Provider<FiddlerLogger> {
        private Binding<Context> context;
        private Binding<LogConfiguration> logConfiguration;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideFiddlerLoggerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.logging.http.FiddlerLogger", true, "com.microsoft.kapp.KAppModule", "provideFiddlerLogger");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.logConfiguration = linker.requestBinding("com.microsoft.kapp.logging.LogConfiguration", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.logConfiguration);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public FiddlerLogger get() {
            return this.module.provideFiddlerLogger(this.context.get(), this.settingsProvider.get(), this.logConfiguration.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideUserAgentProviderProvidesAdapter extends ProvidesBinding<UserAgentProvider> implements Provider<UserAgentProvider> {
        private Binding<Context> context;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideUserAgentProviderProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.services.UserAgentProvider", true, "com.microsoft.kapp.KAppModule", "provideUserAgentProvider");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public UserAgentProvider get() {
            return this.module.provideUserAgentProvider(this.context.get(), this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideLocationServiceProvidesAdapter extends ProvidesBinding<LocationService> implements Provider<LocationService> {
        private final KAppModule module;

        public ProvideLocationServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.LocationService", false, "com.microsoft.kapp.KAppModule", "provideLocationService");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public LocationService get() {
            return this.module.provideLocationService();
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideContextProvidesAdapter extends ProvidesBinding<Context> implements Provider<Context> {
        private final KAppModule module;

        public ProvideContextProvidesAdapter(KAppModule module) {
            super("android.content.Context", false, "com.microsoft.kapp.KAppModule", "provideContext");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public Context get() {
            return this.module.provideContext();
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideRestServiceProvidesAdapter extends ProvidesBinding<RestService> implements Provider<RestService> {
        private Binding<CacheService> cacheService;
        private Binding<Context> context;
        private Binding<CredentialsManager> credentialsManager;
        private final KAppModule module;
        private Binding<PhoneSensorDataProvider> phoneSensorDataProvider;
        private Binding<NetworkProvider> provider;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideRestServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.services.RestService", false, "com.microsoft.kapp.KAppModule", "provideRestService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", KAppModule.class, getClass().getClassLoader());
            this.credentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", KAppModule.class, getClass().getClassLoader());
            this.cacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.phoneSensorDataProvider = linker.requestBinding("com.microsoft.kapp.sensor.PhoneSensorDataProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.provider);
            getBindings.add(this.credentialsManager);
            getBindings.add(this.cacheService);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.phoneSensorDataProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public RestService get() {
            return this.module.provideRestService(this.context.get(), this.provider.get(), this.credentialsManager.get(), this.cacheService.get(), this.settingsProvider.get(), this.phoneSensorDataProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideBedrockRestServiceProvidesAdapter extends ProvidesBinding<BedrockRestService> implements Provider<BedrockRestService> {
        private final KAppModule module;
        private Binding<NetworkProvider> provider;

        public ProvideBedrockRestServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.bedrock.BedrockRestService", false, "com.microsoft.kapp.KAppModule", "provideBedrockRestService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.provider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public BedrockRestService get() {
            return this.module.provideBedrockRestService(this.provider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCredentialStoreProvidesAdapter extends ProvidesBinding<CredentialStore> implements Provider<CredentialStore> {
        private Binding<Context> context;
        private Binding<CryptoProvider> cryptoProvider;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideCredentialStoreProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.auth.credentials.CredentialStore", true, "com.microsoft.kapp.KAppModule", "provideCredentialStore");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.cryptoProvider = linker.requestBinding("com.microsoft.band.service.crypto.CryptoProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.cryptoProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CredentialStore get() {
            return this.module.provideCredentialStore(this.context.get(), this.settingsProvider.get(), this.cryptoProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideUserProfileFetcherProvidesAdapter extends ProvidesBinding<UserProfileFetcher> implements Provider<UserProfileFetcher> {
        private Binding<CargoConnection> cargoConnection;
        private Binding<CredentialsManager> credentialsManager;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideUserProfileFetcherProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.UserProfileFetcher", true, "com.microsoft.kapp.KAppModule", "provideUserProfileFetcher");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
            this.credentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.cargoConnection);
            getBindings.add(this.credentialsManager);
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public UserProfileFetcher get() {
            return this.module.provideUserProfileFetcher(this.cargoConnection.get(), this.credentialsManager.get(), this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCalendarEventsDataProviderProvidesAdapter extends ProvidesBinding<CalendarEventsDataProvider> implements Provider<CalendarEventsDataProvider> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideCalendarEventsDataProviderProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.calendar.CalendarEventsDataProvider", true, "com.microsoft.kapp.KAppModule", "provideCalendarEventsDataProvider");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CalendarEventsDataProvider get() {
            return this.module.provideCalendarEventsDataProvider(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvidePersonalizationManagerFactoryProvidesAdapter extends ProvidesBinding<PersonalizationManagerFactory> implements Provider<PersonalizationManagerFactory> {
        private Binding<CargoConnection> cargoConnection;
        private Binding<Context> context;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvidePersonalizationManagerFactoryProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.personalization.PersonalizationManagerFactory", true, "com.microsoft.kapp.KAppModule", "providePersonalizationManagerFactory");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.cargoConnection);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public PersonalizationManagerFactory get() {
            return this.module.providePersonalizationManagerFactory(this.context.get(), this.settingsProvider.get(), this.cargoConnection.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideKAppsUpdaterProvidesAdapter extends ProvidesBinding<KAppsUpdater> implements Provider<KAppsUpdater> {
        private Binding<AppConfigurationManager> appConfigurationManager;
        private Binding<CalendarEventsDataProvider> calendarEventsDataProvider;
        private Binding<CargoConnection> connection;
        private Binding<Context> context;
        private Binding<FinanceService> financeService;
        private Binding<GuidedWorkoutService> guidedWorkoutSyncService;
        private Binding<HealthAndFitnessService> healthAndFitnessService;
        private Binding<LocationService> locationService;
        private final KAppModule module;
        private Binding<RestService> restService;
        private Binding<SettingsProvider> settingsProvider;
        private Binding<WeatherService> weatherService;

        public ProvideKAppsUpdaterProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.KAppsUpdater", false, "com.microsoft.kapp.KAppModule", "provideKAppsUpdater");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.connection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
            this.weatherService = linker.requestBinding("com.microsoft.kapp.services.weather.WeatherService", KAppModule.class, getClass().getClassLoader());
            this.financeService = linker.requestBinding("com.microsoft.kapp.services.finance.FinanceService", KAppModule.class, getClass().getClassLoader());
            this.locationService = linker.requestBinding("com.microsoft.kapp.services.LocationService", KAppModule.class, getClass().getClassLoader());
            this.restService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", KAppModule.class, getClass().getClassLoader());
            this.healthAndFitnessService = linker.requestBinding("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", KAppModule.class, getClass().getClassLoader());
            this.calendarEventsDataProvider = linker.requestBinding("com.microsoft.kapp.calendar.CalendarEventsDataProvider", KAppModule.class, getClass().getClassLoader());
            this.guidedWorkoutSyncService = linker.requestBinding("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", KAppModule.class, getClass().getClassLoader());
            this.appConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", KAppModule.class, getClass().getClassLoader());
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.settingsProvider);
            getBindings.add(this.connection);
            getBindings.add(this.weatherService);
            getBindings.add(this.financeService);
            getBindings.add(this.locationService);
            getBindings.add(this.restService);
            getBindings.add(this.healthAndFitnessService);
            getBindings.add(this.calendarEventsDataProvider);
            getBindings.add(this.guidedWorkoutSyncService);
            getBindings.add(this.appConfigurationManager);
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public KAppsUpdater get() {
            return this.module.provideKAppsUpdater(this.settingsProvider.get(), this.connection.get(), this.weatherService.get(), this.financeService.get(), this.locationService.get(), this.restService.get(), this.healthAndFitnessService.get(), this.calendarEventsDataProvider.get(), this.guidedWorkoutSyncService.get(), this.appConfigurationManager.get(), this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideGooglePlayUtilsProvidesAdapter extends ProvidesBinding<GooglePlayUtils> implements Provider<GooglePlayUtils> {
        private final KAppModule module;

        public ProvideGooglePlayUtilsProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.utils.GooglePlayUtils", true, "com.microsoft.kapp.KAppModule", "provideGooglePlayUtils");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public GooglePlayUtils get() {
            return this.module.provideGooglePlayUtils();
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideShakeDetectorProvidesAdapter extends ProvidesBinding<ShakeDetector> implements Provider<ShakeDetector> {
        private Binding<Context> context;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideShakeDetectorProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.ShakeDetector", true, "com.microsoft.kapp.KAppModule", "provideShakeDetector");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public ShakeDetector get() {
            return this.module.provideShakeDetector(this.context.get(), this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideSettingsProvidesAdapter extends ProvidesBinding<SettingsProvider> implements Provider<SettingsProvider> {
        private Binding<Context> context;
        private Binding<CryptoProvider> cryptoProvider;
        private final KAppModule module;

        public ProvideSettingsProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.SettingsProvider", true, "com.microsoft.kapp.KAppModule", "provideSettings");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.cryptoProvider = linker.requestBinding("com.microsoft.band.service.crypto.CryptoProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.cryptoProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public SettingsProvider get() {
            return this.module.provideSettings(this.context.get(), this.cryptoProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideFinanceServiceProvidesAdapter extends ProvidesBinding<FinanceService> implements Provider<FinanceService> {
        private final KAppModule module;
        private Binding<NetworkProvider> provider;

        public ProvideFinanceServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.finance.FinanceService", false, "com.microsoft.kapp.KAppModule", "provideFinanceService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.provider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public FinanceService get() {
            return this.module.provideFinanceService(this.provider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideWeatherServiceProvidesAdapter extends ProvidesBinding<WeatherService> implements Provider<WeatherService> {
        private final KAppModule module;
        private Binding<NetworkProvider> provider;

        public ProvideWeatherServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.weather.WeatherService", false, "com.microsoft.kapp.KAppModule", "provideWeatherService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.provider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public WeatherService get() {
            return this.module.provideWeatherService(this.provider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideHealthAndFitnessServiceProvidesAdapter extends ProvidesBinding<HealthAndFitnessService> implements Provider<HealthAndFitnessService> {
        private Binding<CacheService> cacheService;
        private Binding<Context> context;
        private Binding<CredentialsManager> credentialsManager;
        private final KAppModule module;
        private Binding<NetworkProvider> provider;

        public ProvideHealthAndFitnessServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", false, "com.microsoft.kapp.KAppModule", "provideHealthAndFitnessService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", KAppModule.class, getClass().getClassLoader());
            this.credentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", KAppModule.class, getClass().getClassLoader());
            this.cacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", KAppModule.class, getClass().getClassLoader());
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.provider);
            getBindings.add(this.credentialsManager);
            getBindings.add(this.cacheService);
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public HealthAndFitnessService get() {
            return this.module.provideHealthAndFitnessService(this.provider.get(), this.credentialsManager.get(), this.cacheService.get(), this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideMsaAuthProvidesAdapter extends ProvidesBinding<MsaAuth> implements Provider<MsaAuth> {
        private Binding<CredentialStore> credentialStore;
        private final KAppModule module;
        private Binding<NetworkProvider> provider;

        public ProvideMsaAuthProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.auth.MsaAuth", false, "com.microsoft.kapp.KAppModule", "provideMsaAuth");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.provider = linker.requestBinding("com.microsoft.krestsdk.services.NetworkProvider", KAppModule.class, getClass().getClassLoader());
            this.credentialStore = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialStore", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.provider);
            getBindings.add(this.credentialStore);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public MsaAuth get() {
            return this.module.provideMsaAuth(this.provider.get(), this.credentialStore.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideKdsAuthProvidesAdapter extends ProvidesBinding<KdsAuth> implements Provider<KdsAuth> {
        private Binding<KdsFetcher> kdsFetcher;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideKdsAuthProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.auth.KdsAuth", false, "com.microsoft.kapp.KAppModule", "provideKdsAuth");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.kdsFetcher = linker.requestBinding("com.microsoft.krestsdk.auth.KdsFetcher", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.kdsFetcher);
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public KdsAuth get() {
            return this.module.provideKdsAuth(this.kdsFetcher.get(), this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCredentialsManagerProvidesAdapter extends ProvidesBinding<CredentialsManager> implements Provider<CredentialsManager> {
        private Binding<Context> context;
        private Binding<CredentialStore> credentialStore;
        private Binding<KdsAuth> kdsAuth;
        private final KAppModule module;
        private Binding<MsaAuth> msaAuth;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideCredentialsManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.krestsdk.auth.credentials.CredentialsManager", true, "com.microsoft.kapp.KAppModule", "provideCredentialsManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.credentialStore = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialStore", KAppModule.class, getClass().getClassLoader());
            this.msaAuth = linker.requestBinding("com.microsoft.krestsdk.auth.MsaAuth", KAppModule.class, getClass().getClassLoader());
            this.kdsAuth = linker.requestBinding("com.microsoft.krestsdk.auth.KdsAuth", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.credentialStore);
            getBindings.add(this.msaAuth);
            getBindings.add(this.kdsAuth);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CredentialsManager get() {
            return this.module.provideCredentialsManager(this.context.get(), this.settingsProvider.get(), this.credentialStore.get(), this.msaAuth.get(), this.kdsAuth.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideContactResolverProvidesAdapter extends ProvidesBinding<ContactResolver> implements Provider<ContactResolver> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideContactResolverProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.ContactResolver", true, "com.microsoft.kapp.KAppModule", "provideContactResolver");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public ContactResolver get() {
            return this.module.provideContactResolver(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideSmsRequestManagerProvidesAdapter extends ProvidesBinding<SmsRequestManager> implements Provider<SmsRequestManager> {
        private Binding<CargoConnection> cargoConnection;
        private final KAppModule module;

        public ProvideSmsRequestManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.telephony.SmsRequestManager", true, "com.microsoft.kapp.KAppModule", "provideSmsRequestManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.cargoConnection);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public SmsRequestManager get() {
            return this.module.provideSmsRequestManager(this.cargoConnection.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCalendarManagerProvidesAdapter extends ProvidesBinding<CalendarManager> implements Provider<CalendarManager> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideCalendarManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.calendar.CalendarManager", true, "com.microsoft.kapp.KAppModule", "provideCalendarManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CalendarManager get() {
            return this.module.provideCalendarManager(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCalendarObserverProvidesAdapter extends ProvidesBinding<CalendarObserver> implements Provider<CalendarObserver> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideCalendarObserverProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.calendar.CalendarObserver", true, "com.microsoft.kapp.KAppModule", "provideCalendarObserver");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CalendarObserver get() {
            return this.module.provideCalendarObserver(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideApplicationUpdateLauncherProvidesAdapter extends ProvidesBinding<ApplicationUpdateLauncher> implements Provider<ApplicationUpdateLauncher> {
        private final KAppModule module;

        public ProvideApplicationUpdateLauncherProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.version.ApplicationUpdateLauncher", true, "com.microsoft.kapp.KAppModule", "provideApplicationUpdateLauncher");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public ApplicationUpdateLauncher get() {
            return this.module.provideApplicationUpdateLauncher();
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideMessagesManagerProvidesAdapter extends ProvidesBinding<MessagesManager> implements Provider<MessagesManager> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideMessagesManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.telephony.MessagesManager", true, "com.microsoft.kapp.KAppModule", "provideMessagesManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public MessagesManager get() {
            return this.module.provideMessagesManager(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideMessagesObserverProvidesAdapter extends ProvidesBinding<MessagesObserver> implements Provider<MessagesObserver> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideMessagesObserverProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.telephony.MessagesObserver", true, "com.microsoft.kapp.KAppModule", "provideMessagesObserver");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public MessagesObserver get() {
            return this.module.provideMessagesObserver(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideMessageMetadataRetrieverProvidesAdapter extends ProvidesBinding<MessageMetadataRetriever> implements Provider<MessageMetadataRetriever> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideMessageMetadataRetrieverProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.telephony.MessageMetadataRetriever", true, "com.microsoft.kapp.KAppModule", "provideMessageMetadataRetriever");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public MessageMetadataRetriever get() {
            return this.module.provideMessageMetadataRetriever(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideFirmwareUpdateTaskProvidesAdapter extends ProvidesBinding<FirmwareUpdateTask> implements Provider<FirmwareUpdateTask> {
        private Binding<CargoConnection> cargoConnection;
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideFirmwareUpdateTaskProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.tasks.FirmwareUpdateTask", false, "com.microsoft.kapp.KAppModule", "provideFirmwareUpdateTask");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.cargoConnection);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public FirmwareUpdateTask get() {
            return this.module.provideFirmwareUpdateTask(this.context.get(), this.cargoConnection.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCargoVersionRetrieverProvidesAdapter extends ProvidesBinding<CargoVersionRetriever> implements Provider<CargoVersionRetriever> {
        private Binding<CargoConnection> connection;
        private final KAppModule module;

        public ProvideCargoVersionRetrieverProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.CargoVersionRetriever", true, "com.microsoft.kapp.KAppModule", "provideCargoVersionRetriever");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.connection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.connection);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CargoVersionRetriever get() {
            return this.module.provideCargoVersionRetriever(this.connection.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCacheProvidesAdapter extends ProvidesBinding<Cache> implements Provider<Cache> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideCacheProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.cache.Cache", true, "com.microsoft.kapp.KAppModule", "provideCache");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public Cache get() {
            return this.module.provideCache(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCacheServiceProvidesAdapter extends ProvidesBinding<CacheService> implements Provider<CacheService> {
        private Binding<Cache> cache;
        private final KAppModule module;
        private Binding<SettingsProvider> provider;

        public ProvideCacheServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.cache.CacheService", true, "com.microsoft.kapp.KAppModule", "provideCacheService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.cache = linker.requestBinding("com.microsoft.kapp.cache.Cache", KAppModule.class, getClass().getClassLoader());
            this.provider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.cache);
            getBindings.add(this.provider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CacheService get() {
            return this.module.provideCacheService(this.cache.get(), this.provider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideStrappSettingsManagerProvidesAdapter extends ProvidesBinding<StrappSettingsManager> implements Provider<StrappSettingsManager> {
        private Binding<AppConfigurationManager> appConfigurationManager;
        private Binding<CargoConnection> cargoConnection;
        private Binding<Context> context;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideStrappSettingsManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.strappsettings.StrappSettingsManager", true, "com.microsoft.kapp.KAppModule", "provideStrappSettingsManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.appConfigurationManager = linker.requestBinding("com.microsoft.kapp.utils.AppConfigurationManager", KAppModule.class, getClass().getClassLoader());
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.cargoConnection);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.appConfigurationManager);
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public StrappSettingsManager get() {
            return this.module.provideStrappSettingsManager(this.cargoConnection.get(), this.settingsProvider.get(), this.appConfigurationManager.get(), this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideGuidedWorkoutServiceProvidesAdapter extends ProvidesBinding<GuidedWorkoutService> implements Provider<GuidedWorkoutService> {
        private Binding<HealthAndFitnessService> healthAndFitnessService;
        private final KAppModule module;
        private Binding<RestService> restService;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideGuidedWorkoutServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService", true, "com.microsoft.kapp.KAppModule", "provideGuidedWorkoutService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.restService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", KAppModule.class, getClass().getClassLoader());
            this.healthAndFitnessService = linker.requestBinding("com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.settingsProvider);
            getBindings.add(this.restService);
            getBindings.add(this.healthAndFitnessService);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public GuidedWorkoutService get() {
            return this.module.provideGuidedWorkoutService(this.settingsProvider.get(), this.restService.get(), this.healthAndFitnessService.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCryptoProviderProvidesAdapter extends ProvidesBinding<CryptoProvider> implements Provider<CryptoProvider> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideCryptoProviderProvidesAdapter(KAppModule module) {
            super("com.microsoft.band.service.crypto.CryptoProvider", false, "com.microsoft.kapp.KAppModule", "provideCryptoProvider");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CryptoProvider get() {
            return this.module.provideCryptoProvider(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideCallDismissManagerProvidesAdapter extends ProvidesBinding<CallDismissManager> implements Provider<CallDismissManager> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideCallDismissManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.CallDismissManager", true, "com.microsoft.kapp.KAppModule", "provideCallDismissManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public CallDismissManager get() {
            return this.module.provideCallDismissManager(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideTimeZoneChangeHandlerProvidesAdapter extends ProvidesBinding<TimeZoneChangeHandler> implements Provider<TimeZoneChangeHandler> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideTimeZoneChangeHandlerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler", true, "com.microsoft.kapp.KAppModule", "provideTimeZoneChangeHandler");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public TimeZoneChangeHandler get() {
            return this.module.provideTimeZoneChangeHandler(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideDialogManagerProvidesAdapter extends ProvidesBinding<DialogManagerImpl> implements Provider<DialogManagerImpl> {
        private final KAppModule module;

        public ProvideDialogManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.utils.DialogManagerImpl", false, "com.microsoft.kapp.KAppModule", "provideDialogManager");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public DialogManagerImpl get() {
            return this.module.provideDialogManager();
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideStubDialogManagerProvidesAdapter extends ProvidesBinding<StubDialogManager> implements Provider<StubDialogManager> {
        private final KAppModule module;

        public ProvideStubDialogManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.utils.StubDialogManager", false, "com.microsoft.kapp.KAppModule", "provideStubDialogManager");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public StubDialogManager get() {
            return this.module.provideStubDialogManager();
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideSensorDBProvidesAdapter extends ProvidesBinding<SensorDB> implements Provider<SensorDB> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideSensorDBProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.sensor.db.SensorDB", true, "com.microsoft.kapp.KAppModule", "provideSensorDB");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public SensorDB get() {
            return this.module.provideSensorDB(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvidePhoneSensorDataProviderProvidesAdapter extends ProvidesBinding<PhoneSensorDataProvider> implements Provider<PhoneSensorDataProvider> {
        private Binding<Context> context;
        private final KAppModule module;
        private Binding<SensorDB> sensorDB;
        private Binding<SensorUtils> sensorUtils;

        public ProvidePhoneSensorDataProviderProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.sensor.PhoneSensorDataProvider", true, "com.microsoft.kapp.KAppModule", "providePhoneSensorDataProvider");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.sensorDB = linker.requestBinding("com.microsoft.kapp.sensor.db.SensorDB", KAppModule.class, getClass().getClassLoader());
            this.sensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.sensorDB);
            getBindings.add(this.sensorUtils);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public PhoneSensorDataProvider get() {
            return this.module.providePhoneSensorDataProvider(this.context.get(), this.sensorDB.get(), this.sensorUtils.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideSensorDataDebugProviderProvidesAdapter extends ProvidesBinding<SensorDataDebugProvider> implements Provider<SensorDataDebugProvider> {
        private Binding<Context> context;
        private final KAppModule module;
        private Binding<SensorDB> sensorDB;
        private Binding<SensorUtils> sensorUtils;

        public ProvideSensorDataDebugProviderProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.sensor.SensorDataDebugProvider", true, "com.microsoft.kapp.KAppModule", "provideSensorDataDebugProvider");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.sensorDB = linker.requestBinding("com.microsoft.kapp.sensor.db.SensorDB", KAppModule.class, getClass().getClassLoader());
            this.sensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.sensorDB);
            getBindings.add(this.sensorUtils);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public SensorDataDebugProvider get() {
            return this.module.provideSensorDataDebugProvider(this.context.get(), this.sensorDB.get(), this.sensorUtils.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideSensorDataLoggerProvidesAdapter extends ProvidesBinding<SensorDataLogger> implements Provider<SensorDataLogger> {
        private final KAppModule module;
        private Binding<SensorDB> sensorDB;

        public ProvideSensorDataLoggerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.sensor.SensorDataLogger", true, "com.microsoft.kapp.KAppModule", "provideSensorDataLogger");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.sensorDB = linker.requestBinding("com.microsoft.kapp.sensor.db.SensorDB", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.sensorDB);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public SensorDataLogger get() {
            return this.module.provideSensorDataLogger(this.sensorDB.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideMultiDeviceManagerProvidesAdapter extends ProvidesBinding<MultiDeviceManager> implements Provider<MultiDeviceManager> {
        private Binding<CargoConnection> cargoConnection;
        private Binding<Context> context;
        private final KAppModule module;
        private Binding<SensorUtils> sensorUtils;
        private Binding<SettingsProvider> settingsProvider;
        private Binding<SyncManager> syncManager;

        public ProvideMultiDeviceManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.multidevice.MultiDeviceManager", true, "com.microsoft.kapp.KAppModule", "provideMultiDeviceManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.syncManager = linker.requestBinding("com.microsoft.kapp.multidevice.SyncManager", KAppModule.class, getClass().getClassLoader());
            this.sensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.cargoConnection);
            getBindings.add(this.context);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.syncManager);
            getBindings.add(this.sensorUtils);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public MultiDeviceManager get() {
            return this.module.provideMultiDeviceManager(this.cargoConnection.get(), this.context.get(), this.settingsProvider.get(), this.syncManager.get(), this.sensorUtils.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideEventSensorDataUploaderProvidesAdapter extends ProvidesBinding<EventSensorDataUploader> implements Provider<EventSensorDataUploader> {
        private final KAppModule module;
        private Binding<RestService> restService;

        public ProvideEventSensorDataUploaderProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.sensor.upload.EventSensorDataUploader", true, "com.microsoft.kapp.KAppModule", "provideEventSensorDataUploader");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.restService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.restService);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public EventSensorDataUploader get() {
            return this.module.provideEventSensorDataUploader(this.restService.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideBandSensorDataUploaderProvidesAdapter extends ProvidesBinding<BandSensorDataUploader> implements Provider<BandSensorDataUploader> {
        private Binding<CargoConnection> cargoConnection;
        private Binding<Context> context;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;
        private Binding<SingleDeviceEnforcementManager> singleDeviceEnforcementManager;

        public ProvideBandSensorDataUploaderProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.sensor.upload.BandSensorDataUploader", true, "com.microsoft.kapp.KAppModule", "provideBandSensorDataUploader");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
            this.singleDeviceEnforcementManager = linker.requestBinding("com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.cargoConnection);
            getBindings.add(this.singleDeviceEnforcementManager);
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public BandSensorDataUploader get() {
            return this.module.provideBandSensorDataUploader(this.context.get(), this.cargoConnection.get(), this.singleDeviceEnforcementManager.get(), this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideSyncManagerProvidesAdapter extends ProvidesBinding<SyncManager> implements Provider<SyncManager> {
        private Binding<BandSensorDataUploader> bandSensorDataUploader;
        private Binding<CacheService> cacheService;
        private Binding<Context> context;
        private Binding<EventSensorDataUploader> eventSensorDataUploader;
        private Binding<FiddlerLogger> fiddlerLogger;
        private final KAppModule module;
        private Binding<PhoneSensorDataProvider> phoneSensorDataProvider;
        private Binding<SensorUtils> sensorUtils;
        private Binding<SettingsProvider> settingsProvider;
        private Binding<KAppsUpdater> updater;

        public ProvideSyncManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.multidevice.SyncManager", false, "com.microsoft.kapp.KAppModule", "provideSyncManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.cacheService = linker.requestBinding("com.microsoft.kapp.cache.CacheService", KAppModule.class, getClass().getClassLoader());
            this.updater = linker.requestBinding("com.microsoft.kapp.services.KAppsUpdater", KAppModule.class, getClass().getClassLoader());
            this.fiddlerLogger = linker.requestBinding("com.microsoft.kapp.logging.http.FiddlerLogger", KAppModule.class, getClass().getClassLoader());
            this.phoneSensorDataProvider = linker.requestBinding("com.microsoft.kapp.sensor.PhoneSensorDataProvider", KAppModule.class, getClass().getClassLoader());
            this.eventSensorDataUploader = linker.requestBinding("com.microsoft.kapp.sensor.upload.EventSensorDataUploader", KAppModule.class, getClass().getClassLoader());
            this.bandSensorDataUploader = linker.requestBinding("com.microsoft.kapp.sensor.upload.BandSensorDataUploader", KAppModule.class, getClass().getClassLoader());
            this.sensorUtils = linker.requestBinding("com.microsoft.kapp.sensor.SensorUtils", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.cacheService);
            getBindings.add(this.updater);
            getBindings.add(this.fiddlerLogger);
            getBindings.add(this.phoneSensorDataProvider);
            getBindings.add(this.eventSensorDataUploader);
            getBindings.add(this.bandSensorDataUploader);
            getBindings.add(this.sensorUtils);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public SyncManager get() {
            return this.module.provideSyncManager(this.context.get(), this.settingsProvider.get(), this.cacheService.get(), this.updater.get(), this.fiddlerLogger.get(), this.phoneSensorDataProvider.get(), this.eventSensorDataUploader.get(), this.bandSensorDataUploader.get(), this.sensorUtils.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideSingleDeviceEnforcementManagerProvidesAdapter extends ProvidesBinding<SingleDeviceEnforcementManager> implements Provider<SingleDeviceEnforcementManager> {
        private Binding<CredentialsManager> credentialsManager;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideSingleDeviceEnforcementManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager", true, "com.microsoft.kapp.KAppModule", "provideSingleDeviceEnforcementManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.credentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.settingsProvider);
            getBindings.add(this.credentialsManager);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public SingleDeviceEnforcementManager get() {
            return this.module.provideSingleDeviceEnforcementManager(this.settingsProvider.get(), this.credentialsManager.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideGolfServiceProvidesAdapter extends ProvidesBinding<GolfService> implements Provider<GolfService> {
        private final KAppModule module;
        private Binding<RestService> restService;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideGolfServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.golf.GolfService", false, "com.microsoft.kapp.KAppModule", "provideGolfService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.restService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.restService);
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public GolfService get() {
            return this.module.provideGolfService(this.restService.get(), this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideScorecardProviderProvidesAdapter extends ProvidesBinding<ScorecardProvider> implements Provider<ScorecardProvider> {
        private Binding<Context> context;
        private Binding<GolfService> golfService;
        private final KAppModule module;

        public ProvideScorecardProviderProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.providers.golf.ScorecardProvider", false, "com.microsoft.kapp.KAppModule", "provideScorecardProvider");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.golfService = linker.requestBinding("com.microsoft.kapp.services.golf.GolfService", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.golfService);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public ScorecardProvider get() {
            return this.module.provideScorecardProvider(this.context.get(), this.golfService.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideTMAGServiceProvidesAdapter extends ProvidesBinding<TMAGService> implements Provider<TMAGService> {
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideTMAGServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.services.TMAG.TMAGService", true, "com.microsoft.kapp.KAppModule", "provideTMAGService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public TMAGService get() {
            return this.module.provideTMAGService(this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideSensorUtilsProvidesAdapter extends ProvidesBinding<SensorUtils> implements Provider<SensorUtils> {
        private Binding<Context> context;
        private final KAppModule module;

        public ProvideSensorUtilsProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.sensor.SensorUtils", true, "com.microsoft.kapp.KAppModule", "provideSensorUtils");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public SensorUtils get() {
            return this.module.provideSensorUtils(this.context.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideStatUtilsProvidesAdapter extends ProvidesBinding<StatUtils> implements Provider<StatUtils> {
        private final KAppModule module;

        public ProvideStatUtilsProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.utils.stat.StatUtils", true, "com.microsoft.kapp.KAppModule", "provideStatUtils");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public StatUtils get() {
            return this.module.provideStatUtils();
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideGolfUtilsProvidesAdapter extends ProvidesBinding<GolfUtils> implements Provider<GolfUtils> {
        private final KAppModule module;

        public ProvideGolfUtilsProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.utils.GolfUtils", true, "com.microsoft.kapp.KAppModule", "provideGolfUtils");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public GolfUtils get() {
            return this.module.provideGolfUtils();
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideLogConfigurationProvidesAdapter extends ProvidesBinding<LogConfiguration> implements Provider<LogConfiguration> {
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideLogConfigurationProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.logging.LogConfiguration", true, "com.microsoft.kapp.KAppModule", "provideLogConfiguration");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public LogConfiguration get() {
            return this.module.provideLogConfiguration(this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideLogFormatManagerProvidesAdapter extends ProvidesBinding<LogFormatManager> implements Provider<LogFormatManager> {
        private Binding<Context> context;
        private Binding<LogConfiguration> logConfiguration;
        private Binding<LogListProvider> logListProvider;
        private final KAppModule module;

        public ProvideLogFormatManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.logging.LogFormatManager", true, "com.microsoft.kapp.KAppModule", "provideLogFormatManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.logListProvider = linker.requestBinding("com.microsoft.kapp.logging.LogListProvider", KAppModule.class, getClass().getClassLoader());
            this.logConfiguration = linker.requestBinding("com.microsoft.kapp.logging.LogConfiguration", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.logListProvider);
            getBindings.add(this.logConfiguration);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public LogFormatManager get() {
            return this.module.provideLogFormatManager(this.context.get(), this.logListProvider.get(), this.logConfiguration.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideLogListProviderProvidesAdapter extends ProvidesBinding<LogListProvider> implements Provider<LogListProvider> {
        private Binding<Context> context;
        private Binding<CredentialsManager> credentialsManager;
        private Binding<LogConfiguration> logConfiguration;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideLogListProviderProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.logging.LogListProvider", true, "com.microsoft.kapp.KAppModule", "provideLogListProvider");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.credentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
            this.logConfiguration = linker.requestBinding("com.microsoft.kapp.logging.LogConfiguration", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.credentialsManager);
            getBindings.add(this.settingsProvider);
            getBindings.add(this.logConfiguration);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public LogListProvider get() {
            return this.module.provideLogListProvider(this.context.get(), this.credentialsManager.get(), this.settingsProvider.get(), this.logConfiguration.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideFeedbackServiceProvidesAdapter extends ProvidesBinding<FeedbackService> implements Provider<FeedbackService> {
        private Binding<CargoConnection> cargoConnection;
        private Binding<Context> context;
        private Binding<CredentialsManager> credentialsManager;
        private Binding<FiddlerLogger> fiddlerLogger;
        private Binding<LogConfiguration> logConfiguration;
        private Binding<LogFormatManager> logFormatManager;
        private final KAppModule module;
        private Binding<SettingsProvider> settingsProvider;
        private Binding<UserAgentProvider> userAgentProvider;

        public ProvideFeedbackServiceProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.feedback.FeedbackService", false, "com.microsoft.kapp.KAppModule", "provideFeedbackService");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.context = linker.requestBinding("android.content.Context", KAppModule.class, getClass().getClassLoader());
            this.logConfiguration = linker.requestBinding("com.microsoft.kapp.logging.LogConfiguration", KAppModule.class, getClass().getClassLoader());
            this.logFormatManager = linker.requestBinding("com.microsoft.kapp.logging.LogFormatManager", KAppModule.class, getClass().getClassLoader());
            this.credentialsManager = linker.requestBinding("com.microsoft.krestsdk.auth.credentials.CredentialsManager", KAppModule.class, getClass().getClassLoader());
            this.cargoConnection = linker.requestBinding("com.microsoft.kapp.CargoConnection", KAppModule.class, getClass().getClassLoader());
            this.fiddlerLogger = linker.requestBinding("com.microsoft.kapp.logging.http.FiddlerLogger", KAppModule.class, getClass().getClassLoader());
            this.userAgentProvider = linker.requestBinding("com.microsoft.krestsdk.services.UserAgentProvider", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.context);
            getBindings.add(this.logConfiguration);
            getBindings.add(this.logFormatManager);
            getBindings.add(this.credentialsManager);
            getBindings.add(this.cargoConnection);
            getBindings.add(this.fiddlerLogger);
            getBindings.add(this.userAgentProvider);
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public FeedbackService get() {
            return this.module.provideFeedbackService(this.context.get(), this.logConfiguration.get(), this.logFormatManager.get(), this.credentialsManager.get(), this.cargoConnection.get(), this.fiddlerLogger.get(), this.userAgentProvider.get(), this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideAppConfigurationManagerProvidesAdapter extends ProvidesBinding<AppConfigurationManager> implements Provider<AppConfigurationManager> {
        private Binding<Cache> cache;
        private final KAppModule module;
        private Binding<RestService> restService;
        private Binding<SettingsProvider> settingsProvider;

        public ProvideAppConfigurationManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.utils.AppConfigurationManager", true, "com.microsoft.kapp.KAppModule", "provideAppConfigurationManager");
            this.module = module;
            setLibrary(true);
        }

        @Override // dagger.internal.Binding
        public void attach(Linker linker) {
            this.restService = linker.requestBinding("com.microsoft.krestsdk.services.RestService", KAppModule.class, getClass().getClassLoader());
            this.cache = linker.requestBinding("com.microsoft.kapp.cache.Cache", KAppModule.class, getClass().getClassLoader());
            this.settingsProvider = linker.requestBinding("com.microsoft.kapp.services.SettingsProvider", KAppModule.class, getClass().getClassLoader());
        }

        @Override // dagger.internal.Binding
        public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
            getBindings.add(this.restService);
            getBindings.add(this.cache);
            getBindings.add(this.settingsProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public AppConfigurationManager get() {
            return this.module.provideAppConfigurationManager(this.restService.get(), this.cache.get(), this.settingsProvider.get());
        }
    }

    /* compiled from: KAppModule$$ModuleAdapter.java */
    /* loaded from: classes.dex */
    public static final class ProvideDefaultStrappManagerProvidesAdapter extends ProvidesBinding<DefaultStrappManager> implements Provider<DefaultStrappManager> {
        private final KAppModule module;

        public ProvideDefaultStrappManagerProvidesAdapter(KAppModule module) {
            super("com.microsoft.kapp.models.strapp.DefaultStrappManager", true, "com.microsoft.kapp.KAppModule", "provideDefaultStrappManager");
            this.module = module;
            setLibrary(true);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // dagger.internal.ProvidesBinding, dagger.internal.Binding, javax.inject.Provider
        public DefaultStrappManager get() {
            return this.module.provideDefaultStrappManager();
        }
    }
}
