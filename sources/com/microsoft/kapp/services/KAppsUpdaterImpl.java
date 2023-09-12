package com.microsoft.kapp.services;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.device.StrappPageElement;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.R;
import com.microsoft.kapp.calendar.CalendarEventsDataProvider;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.CustomStrappData;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.performance.Perf;
import com.microsoft.kapp.performance.PerfAttribute;
import com.microsoft.kapp.performance.PerfEvent;
import com.microsoft.kapp.services.finance.FinanceService;
import com.microsoft.kapp.services.finance.Stock;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandlerImpl;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.services.weather.WeatherDay;
import com.microsoft.kapp.services.weather.WeatherService;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;
/* loaded from: classes.dex */
public class KAppsUpdaterImpl implements KAppsUpdater, LocationCallbacks {
    private static final String TAG = KAppsUpdaterImpl.class.getSimpleName();
    private AppConfigurationManager mAppConfigurationManager;
    CalendarEventsDataProvider mCalendarEventsDataProvider;
    CargoConnection mCargoConnection;
    Context mContext;
    FinanceService mFinanceService;
    GuidedWorkoutService mGuidedWorkoutSyncService;
    HealthAndFitnessService mHnFService;
    private Location mLocation;
    private Semaphore mLocationLock;
    LocationService mLocationService;
    RestService mRestService;
    SettingsProvider mSettingsProvider;
    WeatherService mWeatherService;

    public KAppsUpdaterImpl(SettingsProvider settingsProvider, CargoConnection cargoConnection, WeatherService weatherService, FinanceService financeService, LocationService locationService, RestService restService, HealthAndFitnessService healthAndFitnessService, CalendarEventsDataProvider calendarEventsDataProvider, GuidedWorkoutService guidedWorkoutSyncService, AppConfigurationManager appConfigurationManager, Context context) {
        this.mSettingsProvider = settingsProvider;
        this.mCargoConnection = cargoConnection;
        this.mWeatherService = weatherService;
        this.mFinanceService = financeService;
        this.mLocationService = locationService;
        this.mRestService = restService;
        this.mHnFService = healthAndFitnessService;
        this.mCalendarEventsDataProvider = calendarEventsDataProvider;
        this.mGuidedWorkoutSyncService = guidedWorkoutSyncService;
        this.mAppConfigurationManager = appConfigurationManager;
        this.mContext = context;
    }

    @Override // com.microsoft.kapp.services.LocationCallbacks
    public void onLocationFound(Location location) {
        this.mLocation = location;
        this.mSettingsProvider.setWeatherLocationLatitude((float) this.mLocation.getLatitude());
        this.mSettingsProvider.setWeatherLocationLongitude((float) this.mLocation.getLongitude());
        WeatherUpdater fetcher = new WeatherUpdater();
        fetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    @Override // com.microsoft.kapp.services.LocationCallbacks
    public void onError() {
        this.mLocationLock.release();
        this.mLocation = new Location("");
        this.mLocation.setLatitude(this.mSettingsProvider.getWeatherLocationLatitude());
        this.mLocation.setLongitude(this.mSettingsProvider.getWeatherLocationLongitude());
        WeatherUpdater fetcher = new WeatherUpdater();
        fetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    @Override // com.microsoft.kapp.services.KAppsUpdater
    public void updateCalendar() {
        Intent intent = new Intent(this.mContext, NotificationIntentService.class);
        intent.setAction(Constants.NOTIFICATION_CALENDAR_SYNC);
        this.mContext.startService(intent);
    }

    @Override // com.microsoft.kapp.services.KAppsUpdater
    public void updateAll() {
        Perf.mark(PerfEvent.KAPPS_UPDATER_UPDATE_ALL, PerfAttribute.START, new String[0]);
        List<UUID> strappsUuidOnDevice = this.mSettingsProvider.getUUIDsOnDevice();
        if (strappsUuidOnDevice.contains(DefaultStrappUUID.STRAPP_STARBUCKS)) {
            updateStarbucks();
        }
        if (CommonUtils.isNetworkAvailable(this.mContext)) {
            if (strappsUuidOnDevice.contains(DefaultStrappUUID.STRAPP_BING_WEATHER)) {
                updateWeather();
            }
            if (strappsUuidOnDevice.contains(DefaultStrappUUID.STRAPP_BING_FINANCE)) {
                updateFinance();
            }
            if (strappsUuidOnDevice.contains(DefaultStrappUUID.STRAPP_CALENDAR)) {
                updateCalendar();
            }
            if (strappsUuidOnDevice.contains(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS)) {
                updateGuidedWorkouts();
            }
        }
        Perf.mark(PerfEvent.KAPPS_UPDATER_UPDATE_ALL, PerfAttribute.END, new String[0]);
        try {
            StrappUtils.confirmLocalDeviceBackgroundDataPopulated(this.mSettingsProvider, this.mCargoConnection);
        } catch (CargoException e) {
            KLog.w(TAG, "Failed to update locally stored band data.", e);
        }
    }

    @Override // com.microsoft.kapp.services.KAppsUpdater
    public void updateWeather() {
        try {
            if (this.mLocationService.isLocationServiceAvailable(this.mContext)) {
                this.mLocationLock = new Semaphore(1);
                this.mLocationLock.drainPermits();
                this.mLocationService.getCurrentLocation(this.mContext, this);
                this.mLocationLock.acquire();
            } else {
                sendWeatherUpdates(this.mSettingsProvider.getWeatherLocationLatitude(), this.mSettingsProvider.getWeatherLocationLongitude());
            }
        } catch (Exception exception) {
            KLog.w(TAG, "Unexpected exception updating weather.", exception);
        }
    }

    @Override // com.microsoft.kapp.services.KAppsUpdater
    public void updateFinance() {
        try {
            List<StockCompanyInformation> stockCompanies = this.mAppConfigurationManager.getStockInformation();
            List<String> symbols = new ArrayList<>();
            for (StockCompanyInformation company : stockCompanies) {
                symbols.add(company.getBingValue());
            }
            List<Stock> stocks = this.mFinanceService.getStockInformation(symbols);
            ArrayList<ArrayList<StrappPageElement>> pageElements = StrappUtils.createFinanceStrappElements(stocks);
            List<Integer> layoutArrays = new ArrayList<>();
            for (int i = 0; i < pageElements.size(); i++) {
                if (stocks.get(i).getChange() >= 0) {
                    layoutArrays.add(2);
                } else {
                    layoutArrays.add(1);
                }
            }
            CustomStrappData data = new CustomStrappData(DefaultStrappUUID.STRAPP_BING_FINANCE, DefaultStrappUUID.STRAPP_BING_FINANCE_PAGES, pageElements, layoutArrays, true, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE, 0, true);
            this.mCargoConnection.sendCustomStrappData(data);
        } catch (Exception exception) {
            KLog.w(TAG, "Unexpected exception updating finance.", exception);
        }
    }

    public void updateStarbucks() {
        ArrayList<StrappPageElement> page;
        int layoutToUse;
        try {
            String cardNumber = this.mSettingsProvider.getStarbucksCardNumber();
            KLog.v(TAG, "Updating starbucks");
            if (cardNumber == null || cardNumber.isEmpty()) {
                page = StrappUtils.createStarbucksNoCardPage(this.mContext.getString(R.string.starbucks_no_card_on_device_message_line1), this.mContext.getString(R.string.starbucks_no_card_on_device_message_line2), this.mContext.getString(R.string.starbucks_no_card_on_device_message_line3));
                layoutToUse = 1;
                KLog.v(TAG, "Updating starbucks to no card ");
            } else {
                page = StrappUtils.createStarbucksPage(cardNumber);
                layoutToUse = 0;
                KLog.logPrivate(TAG, "Updating starbucks to card " + cardNumber);
            }
            ArrayList<ArrayList<StrappPageElement>> pages = new ArrayList<>();
            pages.add(page);
            List<Integer> layoutList = new ArrayList<>();
            layoutList.add(Integer.valueOf(layoutToUse));
            CustomStrappData strapp = new CustomStrappData(DefaultStrappUUID.STRAPP_STARBUCKS, DefaultStrappUUID.STRAPP_STARBUCKS_PAGES, pages, layoutList, false, null, 0, false);
            this.mCargoConnection.sendCustomStrappData(strapp);
        } catch (Exception exception) {
            KLog.w(TAG, "Unexpected exception updating starbucks.", exception);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendWeatherUpdates(double latitude, double longitude) {
        try {
            List<WeatherDay> dailyWeather = this.mWeatherService.getDailyWeather(latitude, longitude, this.mSettingsProvider.isTemperatureMetric() ? Constants.WEATHER_CELSIUS_PARAM : Constants.WEATHER_FAHRENHEIT_PARAM);
            ArrayList<ArrayList<StrappPageElement>> pageElements = StrappUtils.createWeatherStrappElements(dailyWeather, this.mContext);
            List<Integer> layoutArrays = new ArrayList<>();
            for (int i = 0; i < pageElements.size(); i++) {
                layoutArrays.add(1);
            }
            String lastUpdatedText = MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE;
            if (dailyWeather != null && dailyWeather.size() > 0) {
                lastUpdatedText = dailyWeather.get(0).getLocation();
            }
            CustomStrappData data = new CustomStrappData(DefaultStrappUUID.STRAPP_BING_WEATHER, DefaultStrappUUID.STRAPP_BING_WEATHER_PAGES, pageElements, layoutArrays, true, lastUpdatedText, 0, true);
            this.mCargoConnection.sendCustomStrappData(data);
        } catch (Exception exception) {
            KLog.w(TAG, "Unexpected exception updating weather.", exception);
        }
    }

    /* loaded from: classes.dex */
    private class WeatherUpdater extends AsyncTask<Void, Void, Void> {
        private WeatherUpdater() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            double latitude = KAppsUpdaterImpl.this.mLocation.getLatitude();
            double longitude = KAppsUpdaterImpl.this.mLocation.getLongitude();
            try {
                try {
                    KAppsUpdaterImpl.this.sendWeatherUpdates(latitude, longitude);
                    KAppsUpdaterImpl.this.mLocationLock.release();
                    return null;
                } catch (Exception e) {
                    KLog.w(KAppsUpdaterImpl.TAG, "Unexpected exception while sending weather update.", e);
                    KAppsUpdaterImpl.this.mLocationLock.release();
                    return null;
                }
            } catch (Throwable th) {
                KAppsUpdaterImpl.this.mLocationLock.release();
                throw th;
            }
        }
    }

    @Override // com.microsoft.kapp.services.KAppsUpdater
    public void updateGuidedWorkouts() {
        GuidedWorkoutNotificationHandlerImpl mNotificationCenter = new GuidedWorkoutNotificationHandlerImpl(this.mGuidedWorkoutSyncService);
        new CalculateNextWorkoutOperation(null, mNotificationCenter, null, false).execute();
    }

    private void reSyncWorkoutAfterMiniOobe(SyncedWorkoutInfo lastSyncedWorkoutInfo) {
        if (lastSyncedWorkoutInfo != null) {
            ScheduledWorkout scheduledWorkout = new ScheduledWorkout(lastSyncedWorkoutInfo);
            this.mGuidedWorkoutSyncService.pushGuidedWorkoutToDevice(scheduledWorkout);
        }
    }
}
