package com.microsoft.kapp;

import android.app.NotificationManager;
import android.content.Context;
import com.microsoft.band.service.crypto.CryptoProvider;
import com.microsoft.band.service.crypto.CryptoProviderImpl;
import com.microsoft.kapp.activities.BaseActivityAdapter;
import com.microsoft.kapp.activities.BaseFragmentActivity;
import com.microsoft.kapp.activities.BingMapActivity;
import com.microsoft.kapp.activities.CaloriesEditGoalActivity;
import com.microsoft.kapp.activities.DebugActivity;
import com.microsoft.kapp.activities.DeviceColorPersonalizationActivity;
import com.microsoft.kapp.activities.DeviceConnectActivity;
import com.microsoft.kapp.activities.DeviceConnectionErrorActivity;
import com.microsoft.kapp.activities.DeviceWallpaperPersonalizationActivity;
import com.microsoft.kapp.activities.EditGoalActivity;
import com.microsoft.kapp.activities.FeedbackActivity;
import com.microsoft.kapp.activities.GolfCourseFilterActivity;
import com.microsoft.kapp.activities.HistoryFilterActivity;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.activities.LevelTwoBaseActivity;
import com.microsoft.kapp.activities.LevelTwoPagesActivity;
import com.microsoft.kapp.activities.OobeConnectPhoneActivity;
import com.microsoft.kapp.activities.OobeEnableNotificationsActivity;
import com.microsoft.kapp.activities.OobeFirmwareUpdateActivity;
import com.microsoft.kapp.activities.OobeProfileActivity;
import com.microsoft.kapp.activities.OobeReadyActivity;
import com.microsoft.kapp.activities.SignInActivity;
import com.microsoft.kapp.activities.SplashActivity;
import com.microsoft.kapp.activities.StepsEditGoalActivity;
import com.microsoft.kapp.activities.TMAGConnectActivity;
import com.microsoft.kapp.activities.TermsOfServiceActivity;
import com.microsoft.kapp.activities.UserEventDetailsActivity;
import com.microsoft.kapp.activities.WhatsNewSecondaryCardActivity;
import com.microsoft.kapp.activities.WorkoutDetailActivity;
import com.microsoft.kapp.activities.WorkoutPlanDiscoveryActivity;
import com.microsoft.kapp.activities.WorkoutPlanFilterActivity;
import com.microsoft.kapp.activities.golf.GolfFindCourseByNameActivity;
import com.microsoft.kapp.activities.golf.GolfFindCourseByRegionActivity;
import com.microsoft.kapp.activities.golf.GolfRequestACourseActivity;
import com.microsoft.kapp.activities.golf.GolfSearchActivity;
import com.microsoft.kapp.activities.settings.BikeSettingsActivity;
import com.microsoft.kapp.activities.settings.FinanceSettingsActivity;
import com.microsoft.kapp.activities.settings.NotificationCenterSettingsActivity;
import com.microsoft.kapp.activities.settings.RunSettingsActivity;
import com.microsoft.kapp.activities.settings.SelectMetricsActivity;
import com.microsoft.kapp.activities.settings.StarbucksStrappSettingsActivity;
import com.microsoft.kapp.activities.settings.StrappAutoRepliesActivity;
import com.microsoft.kapp.activities.settings.StrappReorderActivity;
import com.microsoft.kapp.activities.settings.StrappSettingsActivity;
import com.microsoft.kapp.cache.Cache;
import com.microsoft.kapp.cache.CacheImpl;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.cache.CacheServiceImpl;
import com.microsoft.kapp.cache.MockCacheServiceImpl;
import com.microsoft.kapp.calendar.CalendarEventsDataProvider;
import com.microsoft.kapp.calendar.CalendarManager;
import com.microsoft.kapp.calendar.CalendarObserver;
import com.microsoft.kapp.calendar.DefaultCalendarEventsDataProvider;
import com.microsoft.kapp.calendar.DefaultCalendarManager;
import com.microsoft.kapp.calendar.DefaultCalendarObserver;
import com.microsoft.kapp.database.LoggingContentResolver;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.feedback.FeedbackService;
import com.microsoft.kapp.feedback.FeedbackServiceImpl;
import com.microsoft.kapp.feedback.FeedbackUtilsV1;
import com.microsoft.kapp.fragments.AboutFragment;
import com.microsoft.kapp.fragments.AttachmentFragment;
import com.microsoft.kapp.fragments.CredentialsFragment;
import com.microsoft.kapp.fragments.DebugActionsFragment;
import com.microsoft.kapp.fragments.DebugCalendarFragment;
import com.microsoft.kapp.fragments.DebugSettingsFragment;
import com.microsoft.kapp.fragments.DebugVersionFragment;
import com.microsoft.kapp.fragments.EditGoalDialogFragment;
import com.microsoft.kapp.fragments.EventHistorySummaryFragment;
import com.microsoft.kapp.fragments.HomeTileNoDataFragment;
import com.microsoft.kapp.fragments.HomeTilesFragment;
import com.microsoft.kapp.fragments.LeftNavigationFragment;
import com.microsoft.kapp.fragments.MainContentFragment;
import com.microsoft.kapp.fragments.ManageTilesFragment;
import com.microsoft.kapp.fragments.MotionSettingsFragment;
import com.microsoft.kapp.fragments.NotificationCenterSettingsFragment;
import com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment;
import com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment;
import com.microsoft.kapp.fragments.PermissionsFragment;
import com.microsoft.kapp.fragments.SettingsMyBandFragment;
import com.microsoft.kapp.fragments.SettingsPersonalizeFragment;
import com.microsoft.kapp.fragments.SettingsPreferencesFragment;
import com.microsoft.kapp.fragments.SettingsProfileFragment;
import com.microsoft.kapp.fragments.SignInFragment;
import com.microsoft.kapp.fragments.StarbucksAddCardFragment;
import com.microsoft.kapp.fragments.StarbucksNoCardsOverviewFragment;
import com.microsoft.kapp.fragments.StarbucksOverviewFragment;
import com.microsoft.kapp.fragments.StockAddFragment;
import com.microsoft.kapp.fragments.StockOverviewFragment;
import com.microsoft.kapp.fragments.StockReorderFragment;
import com.microsoft.kapp.fragments.TopMenuFragment;
import com.microsoft.kapp.fragments.bike.BikeDetailsSplitFragment;
import com.microsoft.kapp.fragments.bike.BikeDetailsSummaryFragment;
import com.microsoft.kapp.fragments.calories.CaloriesDailyFragment;
import com.microsoft.kapp.fragments.calories.CaloriesWeeklyFragment;
import com.microsoft.kapp.fragments.debug.DebugSensorFragment;
import com.microsoft.kapp.fragments.exercise.ExerciseDetailsSummaryFragmentV1;
import com.microsoft.kapp.fragments.feedback.FeedbackDescriptionEditFragment;
import com.microsoft.kapp.fragments.feedback.FeedbackDescriptionFragment;
import com.microsoft.kapp.fragments.feedback.FeedbackSummaryFragment;
import com.microsoft.kapp.fragments.feedback.FeedbackTypeFragment;
import com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment;
import com.microsoft.kapp.fragments.golf.GolfCourseProTipFragment;
import com.microsoft.kapp.fragments.golf.GolfCourseTeePickFragment;
import com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment;
import com.microsoft.kapp.fragments.golf.GolfFindByRegionResultsFragment;
import com.microsoft.kapp.fragments.golf.GolfFindCourseByNameFragment;
import com.microsoft.kapp.fragments.golf.GolfFindCourseByNameResultsFragment;
import com.microsoft.kapp.fragments.golf.GolfLandingPageFragment;
import com.microsoft.kapp.fragments.golf.GolfNearbySearchResultsFragment;
import com.microsoft.kapp.fragments.golf.GolfRecentSearchResultsFragment;
import com.microsoft.kapp.fragments.golf.GolfScorecardFragment;
import com.microsoft.kapp.fragments.golf.GolfSelectRegionFragment;
import com.microsoft.kapp.fragments.golf.GolfSelectStateFragment;
import com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBrowseCustomFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutCarouselFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutDetailsFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBaseFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessBrandsFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutFitnessSelectionResultPageFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutSummaryFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutTypesFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowseAllFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsBrowsePlanOverViewFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsByCategoryFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsFavoritesFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutsSearchFragment;
import com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1;
import com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1;
import com.microsoft.kapp.fragments.history.HistorySummaryFragment;
import com.microsoft.kapp.fragments.run.RunDetailsSplitFragmentV1;
import com.microsoft.kapp.fragments.run.RunDetailsSummaryFragmentV1;
import com.microsoft.kapp.fragments.sleep.SleepDetailsSummaryFragment;
import com.microsoft.kapp.fragments.steps.StepDailyFragment;
import com.microsoft.kapp.fragments.steps.StepWeeklyFragment;
import com.microsoft.kapp.fragments.whatsnew.WhatsNewCardFragment;
import com.microsoft.kapp.fragments.whatsnew.WhatsNewFragment;
import com.microsoft.kapp.fragments.whatsnew.WhatsNewSecondaryCardFragment;
import com.microsoft.kapp.logging.DefaultLogFormatManager;
import com.microsoft.kapp.logging.DefaultLogListProvider;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.LogCatLogger;
import com.microsoft.kapp.logging.LogCleanupService;
import com.microsoft.kapp.logging.LogConfiguration;
import com.microsoft.kapp.logging.LogFormatManager;
import com.microsoft.kapp.logging.LogListProvider;
import com.microsoft.kapp.logging.fileLogger.RollingFileLogger;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.logging.http.FiddlerLoggerImpl;
import com.microsoft.kapp.logging.images.ImageLogger;
import com.microsoft.kapp.logging.notification.NotificationManagerLogger;
import com.microsoft.kapp.logging.notification.NotificationManagerTagLogger;
import com.microsoft.kapp.mocks.MockCargoConnection;
import com.microsoft.kapp.mocks.MockCredentialStore;
import com.microsoft.kapp.mocks.MockRestService;
import com.microsoft.kapp.models.golf.CourseDetails;
import com.microsoft.kapp.models.home.GolfDataFetcher;
import com.microsoft.kapp.models.home.GuidedWorkoutDataFetcher;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.models.home.HomeDataFetcher;
import com.microsoft.kapp.models.strapp.DefaultStrappManager;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.multidevice.MultiDeviceManagerImpl;
import com.microsoft.kapp.multidevice.SingleDeviceEnforcementManager;
import com.microsoft.kapp.multidevice.SingleDeviceEnforcementManagerImpl;
import com.microsoft.kapp.multidevice.SyncManager;
import com.microsoft.kapp.multidevice.SyncManagerImpl;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.providers.golf.ScorecardProvider;
import com.microsoft.kapp.providers.golf.ScorecardProviderImpl;
import com.microsoft.kapp.sensor.PhoneSensorDataProvider;
import com.microsoft.kapp.sensor.PhoneSensorDataProviderImpl;
import com.microsoft.kapp.sensor.SensorDataDebugProvider;
import com.microsoft.kapp.sensor.SensorDataLogger;
import com.microsoft.kapp.sensor.SensorDataLoggerImpl;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.sensor.db.SensorDB;
import com.microsoft.kapp.sensor.db.SensorDBImpl;
import com.microsoft.kapp.sensor.service.SensorService;
import com.microsoft.kapp.sensor.upload.BandSensorDataUploader;
import com.microsoft.kapp.sensor.upload.BandSensorDataUploaderImpl;
import com.microsoft.kapp.sensor.upload.EventSensorDataUploader;
import com.microsoft.kapp.sensor.upload.EventSensorDataUploaderImpl;
import com.microsoft.kapp.services.CalendarSyncNotificationHandler;
import com.microsoft.kapp.services.CallDismissManager;
import com.microsoft.kapp.services.CallDismissManagerImpl;
import com.microsoft.kapp.services.CallNotificationHandler;
import com.microsoft.kapp.services.EmailNotificationHandler;
import com.microsoft.kapp.services.FacebookMessengerNotificationHandler;
import com.microsoft.kapp.services.FacebookNotificationHandler;
import com.microsoft.kapp.services.FusedLocationService;
import com.microsoft.kapp.services.KAppBroadcastReceiver;
import com.microsoft.kapp.services.KAppsUpdater;
import com.microsoft.kapp.services.KAppsUpdaterImpl;
import com.microsoft.kapp.services.LocationService;
import com.microsoft.kapp.services.MessageNotificationHandler;
import com.microsoft.kapp.services.NotificationCenterHandler;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.TMAG.TMAGService;
import com.microsoft.kapp.services.TMAG.TMAGServiceImpl;
import com.microsoft.kapp.services.TwitterNotificationHandler;
import com.microsoft.kapp.services.VoicemailNotificationHandler;
import com.microsoft.kapp.services.background.AppConfigurationService;
import com.microsoft.kapp.services.background.AutoRestartService;
import com.microsoft.kapp.services.background.CacheCleanupService;
import com.microsoft.kapp.services.background.KAppsService;
import com.microsoft.kapp.services.background.SyncService;
import com.microsoft.kapp.services.bedrock.BedrockRestService;
import com.microsoft.kapp.services.bedrock.BedrockRestServiceImpl;
import com.microsoft.kapp.services.finance.FinanceService;
import com.microsoft.kapp.services.finance.FinanceServiceImpl;
import com.microsoft.kapp.services.golf.GolfCourseNotificationHandlerImpl;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.services.golf.GolfServiceImpl;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandlerImpl;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutServiceImpl;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessServiceImpl;
import com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler;
import com.microsoft.kapp.services.weather.WeatherService;
import com.microsoft.kapp.services.weather.WeatherServiceImpl;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.style.text.CustomStyleToSpan;
import com.microsoft.kapp.tasks.FirmwareUpdateTask;
import com.microsoft.kapp.tasks.GuidedWorkout.AddFavoritesOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.FetchGuidedWorkoutDataOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.PushWorkoutToDeviceOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.RemoveFavoritesOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.SubscribeOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.SyncFirstWorkoutOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.UnsubscribeOperation;
import com.microsoft.kapp.tasks.golf.PushGolfToDeviceOperation;
import com.microsoft.kapp.telephony.DefaultMessagesManager;
import com.microsoft.kapp.telephony.DefaultSmsRequestManager;
import com.microsoft.kapp.telephony.MessageMetadataByThreadRetriever;
import com.microsoft.kapp.telephony.MessageMetadataRetriever;
import com.microsoft.kapp.telephony.MessageThreadsRetriever;
import com.microsoft.kapp.telephony.MessagesManager;
import com.microsoft.kapp.telephony.MessagesObserver;
import com.microsoft.kapp.telephony.MmsSmsMessagesObserver;
import com.microsoft.kapp.telephony.PhoneNumberPrettifier;
import com.microsoft.kapp.telephony.PhoneStateListener;
import com.microsoft.kapp.telephony.SmsRequestManager;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.GolfUtils;
import com.microsoft.kapp.utils.GooglePlayUtils;
import com.microsoft.kapp.utils.StubDialogManager;
import com.microsoft.kapp.utils.SyncUtils;
import com.microsoft.kapp.utils.stat.StatUtils;
import com.microsoft.kapp.version.ApplicationUpdateLauncher;
import com.microsoft.kapp.version.InternalApplicationUpdateLauncher;
import com.microsoft.kapp.views.CircularSeekBar;
import com.microsoft.kapp.views.ClearableEditText;
import com.microsoft.kapp.views.CustomFontButton;
import com.microsoft.kapp.views.CustomFontEditText;
import com.microsoft.kapp.views.CustomFontTextClock;
import com.microsoft.kapp.views.CustomFontTextGlyphView;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.CustomGlyphViewWithBackground;
import com.microsoft.kapp.views.FormattedNumberEditText;
import com.microsoft.kapp.webtiles.WebtileDownloadActivity;
import com.microsoft.kapp.widgets.BikeHomeTile;
import com.microsoft.kapp.widgets.CaloriesHomeTile;
import com.microsoft.kapp.widgets.GolfHomeTile;
import com.microsoft.kapp.widgets.GuidedWorkoutHomeTile;
import com.microsoft.kapp.widgets.HomeTile;
import com.microsoft.kapp.widgets.ManageStrappsTile;
import com.microsoft.kapp.widgets.MiniHomeTile;
import com.microsoft.kapp.widgets.PagerTitleStrip;
import com.microsoft.kapp.widgets.RunHomeTile;
import com.microsoft.kapp.widgets.SleepHomeTile;
import com.microsoft.kapp.widgets.StepsHomeTile;
import com.microsoft.kapp.widgets.WorkoutHomeTile;
import com.microsoft.krestsdk.auth.KdsAuth;
import com.microsoft.krestsdk.auth.KdsAuthImpl;
import com.microsoft.krestsdk.auth.KdsFetcher;
import com.microsoft.krestsdk.auth.KdsFetcherImpl;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.MsaAuthImpl;
import com.microsoft.krestsdk.auth.credentials.CredentialStore;
import com.microsoft.krestsdk.auth.credentials.CredentialStoreImpl;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.CredentialsManagerImpl;
import com.microsoft.krestsdk.services.KRestServiceV1;
import com.microsoft.krestsdk.services.NetProviderListFetcher;
import com.microsoft.krestsdk.services.NetworkProvider;
import com.microsoft.krestsdk.services.NetworkProviderImpl;
import com.microsoft.krestsdk.services.ProviderListFetcher;
import com.microsoft.krestsdk.services.RestService;
import com.microsoft.krestsdk.services.UserAgentProvider;
import com.microsoft.krestsdk.services.UserAgentProviderImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
@Module(injects = {AboutFragment.class, AddFavoritesOperation.class, AppConfigurationService.class, AttachmentFragment.class, AutoRestartService.class, BaseActivityAdapter.class, BaseFragmentActivity.class, BikeDetailsSplitFragment.class, BikeDetailsSummaryFragment.class, BikeHomeTile.class, BikeSettingsActivity.class, BingMapActivity.class, BrowseGuidedWorkoutsFragment.class, CacheCleanupService.class, CalendarSyncNotificationHandler.class, CalculateNextWorkoutOperation.class, CallNotificationHandler.class, CaloriesDailyFragment.class, CaloriesEditGoalActivity.class, CaloriesHomeTile.class, CaloriesWeeklyFragment.class, ClearableEditText.class, CourseDetails.class, CredentialsFragment.class, CustomFontButton.class, CustomFontEditText.class, CustomFontTextClock.class, CustomFontTextGlyphView.class, CustomFontTextView.class, CustomStyleToSpan.class, CircularSeekBar.class, CustomGlyphView.class, CustomGlyphViewWithBackground.class, PagerTitleStrip.CustomTitleTextView.class, DebugActionsFragment.class, DebugActivity.class, DebugCalendarFragment.class, DebugSettingsFragment.class, DebugSensorFragment.class, DebugVersionFragment.class, DefaultCalendarEventsDataProvider.class, DefaultCalendarManager.class, DefaultCalendarObserver.class, DefaultMessagesManager.class, DefaultSmsRequestManager.class, DefaultStrappManager.class, DeviceColorPersonalizationActivity.class, DeviceConnectActivity.class, DeviceConnectionErrorActivity.class, DeviceWallpaperPersonalizationActivity.class, EditGoalActivity.class, EditGoalDialogFragment.class, EmailNotificationHandler.class, EventHistorySummaryFragment.class, ExerciseDetailsSummaryFragmentV1.class, FacebookMessengerNotificationHandler.class, FacebookNotificationHandler.class, FeedbackActivity.class, FeedbackDescriptionEditFragment.class, FeedbackDescriptionFragment.class, FeedbackSummaryFragment.class, FeedbackTypeFragment.class, FetchGuidedWorkoutDataOperation.class, FinanceSettingsActivity.class, FirmwareUpdateTask.class, FormattedNumberEditText.class, GolfCourseDetailsFragment.class, GolfCourseFilterActivity.class, GolfCourseNotificationHandlerImpl.class, GolfCourseProTipFragment.class, GolfCourseTeePickFragment.class, GolfDataFetcher.class, GolfDetailsSummaryFragment.class, GolfFindByRegionResultsFragment.class, GolfFindCourseByNameActivity.class, GolfFindCourseByNameFragment.class, GolfFindCourseByNameResultsFragment.class, GolfFindCourseByRegionActivity.class, GolfHomeTile.class, GolfLandingPageFragment.class, GolfNearbySearchResultsFragment.class, GolfRecentSearchResultsFragment.class, GolfRequestACourseActivity.class, GolfScorecardFragment.class, GolfSearchActivity.class, GolfSelectRegionFragment.class, GolfSelectStateFragment.class, GuidedWorkoutsByCategoryFragment.class, GuidedWorkoutCarouselFragment.class, GuidedWorkoutDetailsFragment.class, GuidedWorkoutFitnessBaseFragment.class, GuidedWorkoutFitnessBrandsFragment.class, GuidedWorkoutFitnessSelectionResultPageFragment.class, GuidedWorkoutHomeTile.class, GuidedWorkoutNextFragment.class, GuidedWorkoutBrowseCustomFragment.class, GuidedWorkoutsBrowsePlanFragment.class, GuidedWorkoutsBrowsePlanFragment.class, GuidedWorkoutsBrowsePlanOverViewFragment.class, GuidedWorkoutsSearchFragment.class, GuidedWorkoutSummaryFragment.class, GuidedWorkoutTypesFragment.class, GuidedWorkoutDataFetcher.class, GuidedWorkoutNotificationHandlerImpl.class, GuidedWorkoutsFavoritesFragment.class, GuidedWorkoutsBrowseAllFragment.class, HomeActivity.class, HomeData.class, HomeDataFetcher.class, HomeTile.class, HomeTileNoDataFragment.class, HomeTilesFragment.class, HistoryFilterActivity.class, HistorySummaryFragment.class, ImageLogger.class, KAppBroadcastReceiver.class, KApplication.class, KAppsService.class, LeftNavigationFragment.class, LevelTwoBaseActivity.class, LevelTwoPagesActivity.class, LogCatLogger.class, LogCleanupService.class, MainContentFragment.class, ManageStrappsTile.class, ManageTilesFragment.class, MessageNotificationHandler.class, MiniHomeTile.class, MmsSmsMessagesObserver.class, MotionSettingsFragment.class, NotificationCenterHandler.class, NotificationCenterSettingsActivity.class, NotificationCenterSettingsFragment.class, NotificationManagerLogger.class, NotificationManagerTagLogger.class, OobeBluetoothCompleteFragment.class, OobeBluetoothConnectionFragment.class, OobeConnectPhoneActivity.class, OobeEnableNotificationsActivity.class, OobeFirmwareUpdateActivity.class, OobeProfileActivity.class, OobeReadyActivity.class, PagerTitleStrip.class, PermissionsFragment.class, PersonalizationManagerFactory.class, PhoneStateListener.class, PushWorkoutToDeviceOperation.class, PushGolfToDeviceOperation.class, RollingFileLogger.class, RunDetailsSplitFragmentV1.class, RunDetailsSummaryFragmentV1.class, RunHomeTile.class, RunSettingsActivity.class, RemoveFavoritesOperation.class, SensorService.class, SettingsMyBandFragment.class, SettingsPersonalizeFragment.class, SettingsPreferencesFragment.class, SettingsProfileFragment.class, SignInActivity.class, SignInFragment.class, SleepDetailsSummaryFragment.class, SleepHomeTile.class, SplashActivity.class, StarbucksAddCardFragment.class, StarbucksNoCardsOverviewFragment.class, StarbucksOverviewFragment.class, StarbucksStrappSettingsActivity.class, StepDailyFragment.class, StepsEditGoalActivity.class, StepsHomeTile.class, StepWeeklyFragment.class, StockAddFragment.class, StockOverviewFragment.class, StockReorderFragment.class, StrappAutoRepliesActivity.class, StrappReorderActivity.class, SelectMetricsActivity.class, StrappSettingsActivity.class, StrappSettingsManager.class, SyncService.class, SyncUtils.class, SubscribeOperation.class, SyncFirstWorkoutOperation.class, TermsOfServiceActivity.class, TMAGConnectActivity.class, TopMenuFragment.class, TwitterNotificationHandler.class, UserEventDetailsActivity.class, UnsubscribeOperation.class, VoicemailNotificationHandler.class, WebtileDownloadActivity.class, WhatsNewFragment.class, WhatsNewCardFragment.class, WhatsNewSecondaryCardActivity.class, WhatsNewSecondaryCardFragment.class, WorkoutDetailActivity.class, WorkoutHomeTile.class, WorkoutPlanDiscoveryActivity.class, WorkoutPlanFilterActivity.class, WorkoutPlanScheduleFragmentV1.class, WorkoutPlanSummaryFragmentV1.class}, library = true, staticInjections = {KLog.class, FeedbackUtilsV1.class})
/* loaded from: classes.dex */
public class KAppModule {
    private Context mContext;
    private boolean mIsTest;

    public KAppModule(Context context, boolean isTest) {
        this.mIsTest = false;
        this.mContext = context;
        this.mIsTest = isTest;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public ThemeManager provideThemeManager(Context context) {
        return new DefaultThemeManager(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public FontManager provideFontManager(Context context) {
        return new FontManager(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CargoConnection provideCargoConnection(Context context, CredentialsManager credentialsManager, SettingsProvider settingsProvider, UserAgentProvider userAgentProvider, SingleDeviceEnforcementManager singleDeviceEnforcementManager, CallDismissManager callDismissManager) {
        return Compatibility.inEmulator() ? new MockCargoConnection(context) : new CargoConnectionProxy(context, credentialsManager, settingsProvider, userAgentProvider, singleDeviceEnforcementManager, callDismissManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public DeviceStateDisplayManager provideDeviceStateDisplayManager(Context context, CargoConnection cargoConnection) {
        return new DefaultDeviceStateDisplayManager(context, cargoConnection, new DefaultApplicationStatusChecker(context), (NotificationManager) context.getSystemService("notification"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public ProviderListFetcher provideProviderListFetcher(NetworkProvider provider) {
        return new NetProviderListFetcher(provider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public KdsFetcher provideKdsFetcher(NetworkProvider provider) {
        return new KdsFetcherImpl(provider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public NetworkProvider provideNetworkProvider(UserAgentProvider userAgentProvider, FiddlerLogger fiddlerLogger, SettingsProvider settingsProvider, Context context) {
        return new NetworkProviderImpl(userAgentProvider, fiddlerLogger, settingsProvider, context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public FiddlerLogger provideFiddlerLogger(Context context, SettingsProvider settingsProvider, LogConfiguration logConfiguration) {
        return new FiddlerLoggerImpl(context, settingsProvider, logConfiguration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public UserAgentProvider provideUserAgentProvider(Context context, SettingsProvider settingsProvider) {
        return new UserAgentProviderImpl(context, settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public LocationService provideLocationService() {
        return new FusedLocationService();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public Context provideContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public RestService provideRestService(Context context, NetworkProvider provider, CredentialsManager credentialsManager, CacheService cacheService, SettingsProvider settingsProvider, PhoneSensorDataProvider phoneSensorDataProvider) {
        return this.mIsTest ? new MockRestService() : new KRestServiceV1(context, provider, credentialsManager, cacheService, settingsProvider, phoneSensorDataProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public BedrockRestService provideBedrockRestService(NetworkProvider provider) {
        return new BedrockRestServiceImpl(provider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CredentialStore provideCredentialStore(Context context, SettingsProvider settingsProvider, CryptoProvider cryptoProvider) {
        return this.mIsTest ? new MockCredentialStore() : new CredentialStoreImpl(context, settingsProvider, cryptoProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public UserProfileFetcher provideUserProfileFetcher(CargoConnection cargoConnection, CredentialsManager credentialsManager, SettingsProvider settingsProvider) {
        return new UserProfileFetcher(cargoConnection, credentialsManager, settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CalendarEventsDataProvider provideCalendarEventsDataProvider(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        return new DefaultCalendarEventsDataProvider(new LoggingContentResolver(context));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public PersonalizationManagerFactory providePersonalizationManagerFactory(Context context, SettingsProvider settingsProvider, CargoConnection cargoConnection) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        return new PersonalizationManagerFactory(context, settingsProvider, cargoConnection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public KAppsUpdater provideKAppsUpdater(SettingsProvider settingsProvider, CargoConnection connection, WeatherService weatherService, FinanceService financeService, LocationService locationService, RestService restService, HealthAndFitnessService healthAndFitnessService, CalendarEventsDataProvider calendarEventsDataProvider, GuidedWorkoutService guidedWorkoutSyncService, AppConfigurationManager appConfigurationManager, Context context) {
        return new KAppsUpdaterImpl(settingsProvider, connection, weatherService, financeService, locationService, restService, healthAndFitnessService, calendarEventsDataProvider, guidedWorkoutSyncService, appConfigurationManager, context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public GooglePlayUtils provideGooglePlayUtils() {
        return new GooglePlayUtils();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public ShakeDetector provideShakeDetector(Context context, SettingsProvider settingsProvider) {
        return new ShakeDetector(context, settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public SettingsProvider provideSettings(Context context, CryptoProvider cryptoProvider) {
        return new SettingsProvider(context, cryptoProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public FinanceService provideFinanceService(NetworkProvider provider) {
        return new FinanceServiceImpl(provider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public WeatherService provideWeatherService(NetworkProvider provider) {
        return new WeatherServiceImpl(provider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public HealthAndFitnessService provideHealthAndFitnessService(NetworkProvider provider, CredentialsManager credentialsManager, CacheService cacheService, Context context) {
        return new HealthAndFitnessServiceImpl(provider, credentialsManager, cacheService, context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public MsaAuth provideMsaAuth(NetworkProvider provider, CredentialStore credentialStore) {
        return new MsaAuthImpl(provider, credentialStore);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public KdsAuth provideKdsAuth(KdsFetcher kdsFetcher, SettingsProvider settingsProvider) {
        return new KdsAuthImpl(kdsFetcher, settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CredentialsManager provideCredentialsManager(Context context, SettingsProvider settingsProvider, CredentialStore credentialStore, MsaAuth msaAuth, KdsAuth kdsAuth) {
        return new CredentialsManagerImpl(context, settingsProvider, credentialStore, msaAuth, kdsAuth);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public ContactResolver provideContactResolver(Context context) {
        return new ContactResolver(new LoggingContentResolver(context), new PhoneNumberPrettifier());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public SmsRequestManager provideSmsRequestManager(CargoConnection cargoConnection) {
        return new DefaultSmsRequestManager(cargoConnection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CalendarManager provideCalendarManager(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        return new DefaultCalendarManager(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CalendarObserver provideCalendarObserver(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        return new DefaultCalendarObserver(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public ApplicationUpdateLauncher provideApplicationUpdateLauncher() {
        return Compatibility.isPublicRelease() ? new ApplicationUpdateLauncher() : new InternalApplicationUpdateLauncher();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public MessagesManager provideMessagesManager(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        return new DefaultMessagesManager(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public MessagesObserver provideMessagesObserver(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        return new MmsSmsMessagesObserver(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public MessageMetadataRetriever provideMessageMetadataRetriever(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        if (Compatibility.supportsSmsMmsConversation()) {
            MessageMetadataRetriever retriever = new MessageMetadataRetriever(new LoggingContentResolver(context));
            return retriever;
        }
        MessageMetadataRetriever retriever2 = new MessageMetadataByThreadRetriever(new LoggingContentResolver(context), new MessageThreadsRetriever(new LoggingContentResolver(context)));
        return retriever2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public FirmwareUpdateTask provideFirmwareUpdateTask(Context context, CargoConnection cargoConnection) {
        return new FirmwareUpdateTask(context, cargoConnection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CargoVersionRetriever provideCargoVersionRetriever(CargoConnection connection) {
        return connection;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public Cache provideCache(Context context) {
        return new CacheImpl(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CacheService provideCacheService(Cache cache, SettingsProvider provider) {
        return provider.isCachingEnabled() ? new CacheServiceImpl(cache) : new MockCacheServiceImpl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public StrappSettingsManager provideStrappSettingsManager(CargoConnection cargoConnection, SettingsProvider settingsProvider, AppConfigurationManager appConfigurationManager, Context context) {
        return new StrappSettingsManager(cargoConnection, settingsProvider, appConfigurationManager, context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public GuidedWorkoutService provideGuidedWorkoutService(SettingsProvider settingsProvider, RestService restService, HealthAndFitnessService healthAndFitnessService) {
        return new GuidedWorkoutServiceImpl(settingsProvider, restService, healthAndFitnessService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public CryptoProvider provideCryptoProvider(Context context) {
        return new CryptoProviderImpl(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public CallDismissManager provideCallDismissManager(Context context) {
        return new CallDismissManagerImpl(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public TimeZoneChangeHandler provideTimeZoneChangeHandler(Context context) {
        return new TimeZoneChangeHandler(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public DialogManagerImpl provideDialogManager() {
        return new DialogManagerImpl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public StubDialogManager provideStubDialogManager() {
        return new StubDialogManager();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public SensorDB provideSensorDB(Context context) {
        return new SensorDBImpl(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public PhoneSensorDataProvider providePhoneSensorDataProvider(Context context, SensorDB sensorDB, SensorUtils sensorUtils) {
        return new PhoneSensorDataProviderImpl(context, sensorDB, sensorUtils);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public SensorDataDebugProvider provideSensorDataDebugProvider(Context context, SensorDB sensorDB, SensorUtils sensorUtils) {
        return new PhoneSensorDataProviderImpl(context, sensorDB, sensorUtils);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public SensorDataLogger provideSensorDataLogger(SensorDB sensorDB) {
        return new SensorDataLoggerImpl(sensorDB);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public MultiDeviceManager provideMultiDeviceManager(CargoConnection cargoConnection, Context context, SettingsProvider settingsProvider, SyncManager syncManager, SensorUtils sensorUtils) {
        return new MultiDeviceManagerImpl(context, settingsProvider, cargoConnection, syncManager, sensorUtils);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public EventSensorDataUploader provideEventSensorDataUploader(RestService restService) {
        return new EventSensorDataUploaderImpl(restService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public BandSensorDataUploader provideBandSensorDataUploader(Context context, CargoConnection cargoConnection, SingleDeviceEnforcementManager singleDeviceEnforcementManager, SettingsProvider settingsProvider) {
        return new BandSensorDataUploaderImpl(context, cargoConnection, singleDeviceEnforcementManager, settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public SyncManager provideSyncManager(Context context, SettingsProvider settingsProvider, CacheService cacheService, KAppsUpdater updater, FiddlerLogger fiddlerLogger, PhoneSensorDataProvider phoneSensorDataProvider, EventSensorDataUploader eventSensorDataUploader, BandSensorDataUploader bandSensorDataUploader, SensorUtils sensorUtils) {
        return new SyncManagerImpl(context, settingsProvider, cacheService, updater, fiddlerLogger, phoneSensorDataProvider, eventSensorDataUploader, bandSensorDataUploader, sensorUtils);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public SingleDeviceEnforcementManager provideSingleDeviceEnforcementManager(SettingsProvider settingsProvider, CredentialsManager credentialsManager) {
        return new SingleDeviceEnforcementManagerImpl(settingsProvider, credentialsManager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public GolfService provideGolfService(RestService restService, SettingsProvider settingsProvider) {
        return new GolfServiceImpl(restService, settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public ScorecardProvider provideScorecardProvider(Context context, GolfService golfService) {
        return new ScorecardProviderImpl(context, golfService);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public TMAGService provideTMAGService(SettingsProvider settingsProvider) {
        return new TMAGServiceImpl(settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public SensorUtils provideSensorUtils(Context context) {
        return new SensorUtils(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public StatUtils provideStatUtils() {
        return new StatUtils();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public GolfUtils provideGolfUtils() {
        return new GolfUtils();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public LogConfiguration provideLogConfiguration(SettingsProvider settingsProvider) {
        return new LogConfiguration(settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public LogFormatManager provideLogFormatManager(Context context, LogListProvider logListProvider, LogConfiguration logConfiguration) {
        return new DefaultLogFormatManager(context, logListProvider, logConfiguration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public LogListProvider provideLogListProvider(Context context, CredentialsManager credentialsManager, SettingsProvider settingsProvider, LogConfiguration logConfiguration) {
        return new DefaultLogListProvider(context, credentialsManager, settingsProvider, logConfiguration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public FeedbackService provideFeedbackService(Context context, LogConfiguration logConfiguration, LogFormatManager logFormatManager, CredentialsManager credentialsManager, CargoConnection cargoConnection, FiddlerLogger fiddlerLogger, UserAgentProvider userAgentProvider, SettingsProvider settingsProvider) {
        return new FeedbackServiceImpl(context, logConfiguration, logFormatManager, credentialsManager, fiddlerLogger, cargoConnection, userAgentProvider, settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public AppConfigurationManager provideAppConfigurationManager(RestService restService, Cache cache, SettingsProvider settingsProvider) {
        return new AppConfigurationManager(restService, cache, settingsProvider);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    @Singleton
    public DefaultStrappManager provideDefaultStrappManager() {
        return new DefaultStrappManager();
    }
}
