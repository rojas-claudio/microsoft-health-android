package com.microsoft.kapp.tasks.golf;

import android.os.AsyncTask;
import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.AsyncTaskOperation;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfCourseNotificationHandler;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.SerialAsyncOperation;
import com.microsoft.kapp.utils.SyncUtils;
import com.microsoft.krestsdk.services.KRestException;
import java.util.HashMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class PushGolfToDeviceOperation implements SerialAsyncOperation {
    private static final String TAG = PushGolfToDeviceOperation.class.getSimpleName();
    @Inject
    CacheService mCacheService;
    @Inject
    CargoConnection mCargoConnection;
    private String mCourseId;
    private Callback<Void> mEndOfOperationCallback;
    @Inject
    GolfService mGolfService;
    @Inject
    MultiDeviceManager mMultiDeviceManger;
    private SerialAsyncOperation mNextAsyncOperation;
    private GolfCourseNotificationHandler mNotificationHandler;
    @Inject
    SettingsProvider mSettingsProvider;
    private int mSyncResult = 0;
    private String mTeeId;

    public PushGolfToDeviceOperation(String courseId, String teeId, GolfCourseNotificationHandler notificationHandler, Callback<Void> endOfOperationCallback, SerialAsyncOperation nextOperation) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mCourseId = courseId;
        this.mTeeId = teeId;
        this.mNotificationHandler = notificationHandler;
        this.mEndOfOperationCallback = endOfOperationCallback;
        this.mNextAsyncOperation = nextOperation;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.microsoft.kapp.tasks.golf.PushGolfToDeviceOperation$1] */
    @Override // com.microsoft.kapp.utils.SerialAsyncOperation
    public void execute() {
        int result = SyncUtils.canStrappSync(DefaultStrappUUID.STRAPP_GOLF, this.mSettingsProvider, this.mMultiDeviceManger);
        if (result != 0) {
            this.mEndOfOperationCallback.onError(null);
            this.mNotificationHandler.notifyGolfCourseSyncError(this.mCourseId, result);
            return;
        }
        new AsyncTaskOperation(this.mNextAsyncOperation) { // from class: com.microsoft.kapp.tasks.golf.PushGolfToDeviceOperation.1
            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationStarted() {
                PushGolfToDeviceOperation.this.mNotificationHandler.notifyGolfCourseSyncStarted(PushGolfToDeviceOperation.this.mCourseId);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationSuccess() {
                PushGolfToDeviceOperation.this.mEndOfOperationCallback.callback(null);
                PushGolfToDeviceOperation.this.mNotificationHandler.notifyGolfCourseSyncSuccess(PushGolfToDeviceOperation.this.mCourseId);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationError() {
                PushGolfToDeviceOperation.this.mEndOfOperationCallback.onError(null);
                PushGolfToDeviceOperation.this.mNotificationHandler.notifyGolfCourseSyncError(PushGolfToDeviceOperation.this.mCourseId, PushGolfToDeviceOperation.this.mSyncResult);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                try {
                    byte[] golfCourseData = PushGolfToDeviceOperation.this.mGolfService.getDeviceGolfCourse(PushGolfToDeviceOperation.this.mCourseId, PushGolfToDeviceOperation.this.mTeeId);
                    if (!PushGolfToDeviceOperation.this.mCargoConnection.isDeviceAvailable(null)) {
                        PushGolfToDeviceOperation.this.mSyncResult = 4;
                    } else {
                        try {
                            PushGolfToDeviceOperation.this.mCargoConnection.downloadEphemerisAndTimeZone();
                        } catch (CargoException ex) {
                            KLog.w(PushGolfToDeviceOperation.TAG, "Exception while pushing the ephemeris file", ex);
                        }
                        PushGolfToDeviceOperation.this.mSyncResult = PushGolfToDeviceOperation.this.mCargoConnection.pushGolfCourseData(golfCourseData);
                    }
                    if (PushGolfToDeviceOperation.this.mSyncResult == 2) {
                        PushGolfToDeviceOperation.this.mGolfService.updateLastSyncedGolfCourse(PushGolfToDeviceOperation.this.mCourseId, PushGolfToDeviceOperation.this.mTeeId);
                        setOperationResultStatus(1, 0);
                        PushGolfToDeviceOperation.this.mCacheService.removeForTag("TMAG");
                        HashMap<String, String> telemetryProperties = new HashMap<>();
                        telemetryProperties.put("Course ID", PushGolfToDeviceOperation.this.mCourseId);
                        telemetryProperties.put(TelemetryConstants.Events.Golf.GOLF_TEE_ID, PushGolfToDeviceOperation.this.mTeeId);
                        Telemetry.logEvent(TelemetryConstants.PageViews.FITNESS_GOLF_SYNC, telemetryProperties, null);
                    } else {
                        setOperationResultStatus(2, 1);
                    }
                } catch (KRestException exception) {
                    KLog.e(PushGolfToDeviceOperation.TAG, "Error during uploading course details to cloud", exception);
                    PushGolfToDeviceOperation.this.mSyncResult = 3;
                    setOperationResultStatus(2, 1);
                } catch (Exception exception2) {
                    KLog.e(PushGolfToDeviceOperation.TAG, "Unknown Error during Golf Course Sync!", exception2);
                    PushGolfToDeviceOperation.this.mSyncResult = 0;
                    setOperationResultStatus(2, 1);
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}
