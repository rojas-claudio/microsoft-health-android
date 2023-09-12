package com.microsoft.kapp;

import android.app.Application;
import android.content.Intent;
import com.microsoft.band.service.BandService;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.diagnostics.ActivityLifecycleLogger;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryEvents;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.performance.Perf;
import com.microsoft.kapp.performance.PerfAttribute;
import com.microsoft.kapp.performance.PerfEvent;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.sensor.service.SensorService;
import com.microsoft.kapp.services.LoggingServiceConnection;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.background.AutoRestartService;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.UpgradeUtils;
import com.microsoft.kapp.version.ActivityLifecycleListener;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import dagger.ObjectGraph;
import javax.inject.Inject;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConfigurationException;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
@ReportsCrashes(customReportContent = {ReportField.ANDROID_VERSION, ReportField.APP_VERSION_NAME, ReportField.BRAND, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT}, formKey = "", logcatArguments = {"-t", "50", "-v", "time"}, mailTo = Constants.FEEDBACK_EMAIL, mode = ReportingInteractionMode.DIALOG, resDialogText = R.string.crash_dialog_text)
/* loaded from: classes.dex */
public class KApplication extends Application {
    private static final String TAG = KApplication.class.getSimpleName();
    @Inject
    AppConfigurationManager mAppConfigurationManager;
    private ObjectGraph mApplicationGraph;
    @Inject
    CacheService mCacheService;
    @Inject
    CargoConnection mCargoConnection;
    @Inject
    CredentialsManager mCredentialsManager;
    @Inject
    DeviceStateDisplayManager mDeviceStateDisplayManager;
    @Inject
    FiddlerLogger mFiddlerLogger;
    protected boolean mIsTestRun = false;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    @Inject
    SensorUtils mSensorUtils;
    @Inject
    SettingsProvider mSettingsProvider;
    @Inject
    ShakeDetector mShakeDetector;

    @Override // android.app.Application
    public void onCreate() {
        Perf.mark(PerfEvent.APPLICATION, PerfAttribute.START, new String[0]);
        super.onCreate();
        KappConfig.isDebbuging = (getApplicationInfo().flags & 2) != 0;
        createApplicationGraph();
        initAcra();
        if (Compatibility.supportsRegisterActivityLifecycleCallbacks()) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleLogger(this));
            registerActivityLifecycleCallbacks(new ActivityLifecycleListener(this.mCargoConnection, this.mSettingsProvider, this.mMultiDeviceManager, this.mShakeDetector));
        }
        Intent intent = new Intent(this, AutoRestartService.class);
        bindService(intent, new LoggingServiceConnection(), 1);
        Intent startServiceIntent = new Intent(this, BandService.class);
        startService(startServiceIntent);
        if (this.mSettingsProvider.isSensorLoggingEnabled() && this.mSensorUtils.isKitkatWithStepSensor()) {
            Intent sensorServiceIntent = new Intent(this, SensorService.class);
            startService(sensorServiceIntent);
        }
        boolean appJustUpgraded = UpgradeUtils.isAppJustUpgraded(this.mSettingsProvider, this);
        if (appJustUpgraded) {
            new Thread(new Runnable() { // from class: com.microsoft.kapp.KApplication.1
                @Override // java.lang.Runnable
                public void run() {
                    KApplication.this.mCacheService.removeAll();
                }
            }).start();
        }
        loadAppConfiguration();
        Telemetry.initialize(this);
        if (this.mCredentialsManager != null && this.mCredentialsManager.getAccountMetada() != null) {
            Telemetry.setUserId(this.mCredentialsManager.getAccountMetada().getUserId());
        }
        Telemetry.logEvent(TelemetryEvents.ApplicationLaunched);
    }

    private void initAcra() {
        ACRAConfiguration config = ACRA.getConfig();
        config.setFormKey("");
        config.setCustomReportContent(new ReportField[]{ReportField.ANDROID_VERSION, ReportField.APP_VERSION_NAME, ReportField.BRAND, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT});
        config.setLogcatArguments(new String[]{"-t", "50", "-v", "time"});
        ReportingInteractionMode reportingInteractionMode = ReportingInteractionMode.SILENT;
        if (!Compatibility.isPublicRelease()) {
            config.setMailTo(Constants.FEEDBACK_EMAIL);
            config.setResDialogText(R.string.crash_dialog_text);
            reportingInteractionMode = ReportingInteractionMode.DIALOG;
        }
        try {
            config.setMode(reportingInteractionMode);
        } catch (ACRAConfigurationException e) {
            KLog.e(TAG, "ACRAConfiguration.setMode() failed.", e);
        }
        ACRA.init(this);
        ACRA.getErrorReporter().addReportSender(new TelemetryCrashReportSender(this.mCredentialsManager));
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        Intent startServiceIntent = new Intent(this, BandService.class);
        stopService(startServiceIntent);
    }

    public ObjectGraph getApplicationGraph() {
        return this.mApplicationGraph;
    }

    public void inject(Object object) {
        this.mApplicationGraph.inject(object);
    }

    public <T> T get(Class<T> type) {
        return (T) this.mApplicationGraph.get(type);
    }

    protected void createApplicationGraph() {
        Object[] modules = getModules();
        this.mApplicationGraph = ObjectGraph.create(modules);
        KApplicationGraph.setApplicationGraph(this.mApplicationGraph);
        this.mApplicationGraph.injectStatics();
        inject(this);
    }

    protected Object[] getModules() {
        return new Object[]{new KAppModule(getApplicationContext(), false)};
    }

    public boolean isTestRun() {
        return this.mIsTestRun;
    }

    private void loadAppConfiguration() {
        this.mAppConfigurationManager.loadConfiguration(this);
    }
}
