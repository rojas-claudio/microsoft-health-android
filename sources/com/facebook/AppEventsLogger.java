package com.facebook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import bolts.AppLinks;
import com.facebook.Request;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import com.microsoft.kapp.utils.Constants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class AppEventsLogger {
    public static final String ACTION_APP_EVENTS_FLUSHED = "com.facebook.sdk.APP_EVENTS_FLUSHED";
    public static final String APP_EVENTS_EXTRA_FLUSH_RESULT = "com.facebook.sdk.APP_EVENTS_FLUSH_RESULT";
    public static final String APP_EVENTS_EXTRA_NUM_EVENTS_FLUSHED = "com.facebook.sdk.APP_EVENTS_NUM_EVENTS_FLUSHED";
    private static final int APP_SUPPORTS_ATTRIBUTION_ID_RECHECK_PERIOD_IN_SECONDS = 86400;
    private static final int FLUSH_APP_SESSION_INFO_IN_SECONDS = 30;
    private static final int FLUSH_PERIOD_IN_SECONDS = 15;
    private static final int NUM_LOG_EVENTS_TO_TRY_TO_FLUSH_AFTER = 100;
    private static final String SOURCE_APPLICATION_HAS_BEEN_SET_BY_THIS_INTENT = "_fbSourceApplicationHasBeenSet";
    private static Context applicationContext;
    private static ScheduledThreadPoolExecutor backgroundExecutor;
    private static String hashedDeviceAndAppId;
    private static boolean isOpenedByApplink;
    private static boolean requestInFlight;
    private static String sourceApplication;
    private final AccessTokenAppIdPair accessTokenAppId;
    private final Context context;
    private static final String TAG = AppEventsLogger.class.getCanonicalName();
    private static Map<AccessTokenAppIdPair, SessionEventsState> stateMap = new ConcurrentHashMap();
    private static FlushBehavior flushBehavior = FlushBehavior.AUTO;
    private static Object staticLock = new Object();

    /* loaded from: classes.dex */
    public enum FlushBehavior {
        AUTO,
        EXPLICIT_ONLY;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static FlushBehavior[] valuesCustom() {
            FlushBehavior[] valuesCustom = values();
            int length = valuesCustom.length;
            FlushBehavior[] flushBehaviorArr = new FlushBehavior[length];
            System.arraycopy(valuesCustom, 0, flushBehaviorArr, 0, length);
            return flushBehaviorArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum FlushReason {
        EXPLICIT,
        TIMER,
        SESSION_CHANGE,
        PERSISTED_EVENTS,
        EVENT_THRESHOLD,
        EAGER_FLUSHING_EVENT;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static FlushReason[] valuesCustom() {
            FlushReason[] valuesCustom = values();
            int length = valuesCustom.length;
            FlushReason[] flushReasonArr = new FlushReason[length];
            System.arraycopy(valuesCustom, 0, flushReasonArr, 0, length);
            return flushReasonArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum FlushResult {
        SUCCESS,
        SERVER_ERROR,
        NO_CONNECTIVITY,
        UNKNOWN_ERROR;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static FlushResult[] valuesCustom() {
            FlushResult[] valuesCustom = values();
            int length = valuesCustom.length;
            FlushResult[] flushResultArr = new FlushResult[length];
            System.arraycopy(valuesCustom, 0, flushResultArr, 0, length);
            return flushResultArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AccessTokenAppIdPair implements Serializable {
        private static final long serialVersionUID = 1;
        private final String accessToken;
        private final String applicationId;

        AccessTokenAppIdPair(Session session) {
            this(session.getAccessToken(), session.getApplicationId());
        }

        AccessTokenAppIdPair(String accessToken, String applicationId) {
            this.accessToken = Utility.isNullOrEmpty(accessToken) ? null : accessToken;
            this.applicationId = applicationId;
        }

        String getAccessToken() {
            return this.accessToken;
        }

        String getApplicationId() {
            return this.applicationId;
        }

        public int hashCode() {
            return (this.accessToken == null ? 0 : this.accessToken.hashCode()) ^ (this.applicationId != null ? this.applicationId.hashCode() : 0);
        }

        public boolean equals(Object o) {
            if (o instanceof AccessTokenAppIdPair) {
                AccessTokenAppIdPair p = (AccessTokenAppIdPair) o;
                return Utility.areObjectsEqual(p.accessToken, this.accessToken) && Utility.areObjectsEqual(p.applicationId, this.applicationId);
            }
            return false;
        }

        /* loaded from: classes.dex */
        private static class SerializationProxyV1 implements Serializable {
            private static final long serialVersionUID = -2488473066578201069L;
            private final String accessToken;
            private final String appId;

            private SerializationProxyV1(String accessToken, String appId) {
                this.accessToken = accessToken;
                this.appId = appId;
            }

            /* synthetic */ SerializationProxyV1(String str, String str2, SerializationProxyV1 serializationProxyV1) {
                this(str, str2);
            }

            private Object readResolve() {
                return new AccessTokenAppIdPair(this.accessToken, this.appId);
            }
        }

        private Object writeReplace() {
            return new SerializationProxyV1(this.accessToken, this.applicationId, null);
        }
    }

    @Deprecated
    public static boolean getLimitEventUsage(Context context) {
        return Settings.getLimitEventAndDataUsage(context);
    }

    @Deprecated
    public static void setLimitEventUsage(Context context, boolean limitEventUsage) {
        Settings.setLimitEventAndDataUsage(context, limitEventUsage);
    }

    public static void activateApp(Context context) {
        Settings.sdkInitialize(context);
        activateApp(context, Utility.getMetadataApplicationId(context));
    }

    public static void activateApp(Context context, String applicationId) {
        if (context == null || applicationId == null) {
            throw new IllegalArgumentException("Both context and applicationId must be non-null");
        }
        if (context instanceof Activity) {
            setSourceApplication((Activity) context);
        } else {
            resetSourceApplication();
            Log.d(AppEventsLogger.class.getName(), "To set source application the context of activateApp must be an instance of Activity");
        }
        Settings.publishInstallAsync(context, applicationId, null);
        AppEventsLogger logger = new AppEventsLogger(context, applicationId, null);
        final long eventTime = System.currentTimeMillis();
        final String sourceApplicationInfo = getSourceApplication();
        backgroundExecutor.execute(new Runnable() { // from class: com.facebook.AppEventsLogger.1
            @Override // java.lang.Runnable
            public void run() {
                AppEventsLogger.this.logAppSessionResumeEvent(eventTime, sourceApplicationInfo);
            }
        });
    }

    public static void deactivateApp(Context context) {
        deactivateApp(context, Utility.getMetadataApplicationId(context));
    }

    public static void deactivateApp(Context context, String applicationId) {
        if (context == null || applicationId == null) {
            throw new IllegalArgumentException("Both context and applicationId must be non-null");
        }
        resetSourceApplication();
        AppEventsLogger logger = new AppEventsLogger(context, applicationId, null);
        final long eventTime = System.currentTimeMillis();
        backgroundExecutor.execute(new Runnable() { // from class: com.facebook.AppEventsLogger.2
            @Override // java.lang.Runnable
            public void run() {
                AppEventsLogger.this.logAppSessionSuspendEvent(eventTime);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppSessionResumeEvent(long eventTime, String sourceApplicationInfo) {
        PersistedAppSessionInfo.onResume(applicationContext, this.accessTokenAppId, this, eventTime, sourceApplicationInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppSessionSuspendEvent(long eventTime) {
        PersistedAppSessionInfo.onSuspend(applicationContext, this.accessTokenAppId, this, eventTime);
    }

    public static AppEventsLogger newLogger(Context context) {
        return new AppEventsLogger(context, null, null);
    }

    public static AppEventsLogger newLogger(Context context, Session session) {
        return new AppEventsLogger(context, null, session);
    }

    public static AppEventsLogger newLogger(Context context, String applicationId, Session session) {
        return new AppEventsLogger(context, applicationId, session);
    }

    public static AppEventsLogger newLogger(Context context, String applicationId) {
        return new AppEventsLogger(context, applicationId, null);
    }

    public static FlushBehavior getFlushBehavior() {
        FlushBehavior flushBehavior2;
        synchronized (staticLock) {
            flushBehavior2 = flushBehavior;
        }
        return flushBehavior2;
    }

    public static void setFlushBehavior(FlushBehavior flushBehavior2) {
        synchronized (staticLock) {
            flushBehavior = flushBehavior2;
        }
    }

    public void logEvent(String eventName) {
        logEvent(eventName, (Bundle) null);
    }

    public void logEvent(String eventName, double valueToSum) {
        logEvent(eventName, valueToSum, (Bundle) null);
    }

    public void logEvent(String eventName, Bundle parameters) {
        logEvent(eventName, null, parameters, false);
    }

    public void logEvent(String eventName, double valueToSum, Bundle parameters) {
        logEvent(eventName, Double.valueOf(valueToSum), parameters, false);
    }

    public void logPurchase(BigDecimal purchaseAmount, Currency currency) {
        logPurchase(purchaseAmount, currency, null);
    }

    public void logPurchase(BigDecimal purchaseAmount, Currency currency, Bundle parameters) {
        if (purchaseAmount == null) {
            notifyDeveloperError("purchaseAmount cannot be null");
        } else if (currency == null) {
            notifyDeveloperError("currency cannot be null");
        } else {
            if (parameters == null) {
                parameters = new Bundle();
            }
            parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency.getCurrencyCode());
            logEvent(AppEventsConstants.EVENT_NAME_PURCHASED, purchaseAmount.doubleValue(), parameters);
            eagerFlush();
        }
    }

    public void flush() {
        flush(FlushReason.EXPLICIT);
    }

    public static void onContextStop() {
        PersistedEvents.persistEvents(applicationContext, stateMap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidForSession(Session session) {
        AccessTokenAppIdPair other = new AccessTokenAppIdPair(session);
        return this.accessTokenAppId.equals(other);
    }

    public void logSdkEvent(String eventName, Double valueToSum, Bundle parameters) {
        logEvent(eventName, valueToSum, parameters, true);
    }

    public String getApplicationId() {
        return this.accessTokenAppId.getApplicationId();
    }

    private AppEventsLogger(Context context, String applicationId, Session session) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.context = context;
        session = session == null ? Session.getActiveSession() : session;
        if (session != null && (applicationId == null || applicationId.equals(session.getApplicationId()))) {
            this.accessTokenAppId = new AccessTokenAppIdPair(session);
        } else {
            applicationId = applicationId == null ? Utility.getMetadataApplicationId(context) : applicationId;
            this.accessTokenAppId = new AccessTokenAppIdPair(null, applicationId);
        }
        synchronized (staticLock) {
            if (hashedDeviceAndAppId == null) {
                hashedDeviceAndAppId = Utility.getHashedDeviceAndAppID(context, applicationId);
            }
            if (applicationContext == null) {
                applicationContext = context.getApplicationContext();
            }
        }
        initializeTimersIfNeeded();
    }

    private static void initializeTimersIfNeeded() {
        synchronized (staticLock) {
            if (backgroundExecutor == null) {
                backgroundExecutor = new ScheduledThreadPoolExecutor(1);
                Runnable flushRunnable = new Runnable() { // from class: com.facebook.AppEventsLogger.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (AppEventsLogger.getFlushBehavior() == FlushBehavior.EXPLICIT_ONLY) {
                            return;
                        }
                        AppEventsLogger.flushAndWait(FlushReason.TIMER);
                    }
                };
                backgroundExecutor.scheduleAtFixedRate(flushRunnable, 0L, 15L, TimeUnit.SECONDS);
                Runnable attributionRecheckRunnable = new Runnable() { // from class: com.facebook.AppEventsLogger.4
                    @Override // java.lang.Runnable
                    public void run() {
                        Set<String> applicationIds = new HashSet<>();
                        synchronized (AppEventsLogger.staticLock) {
                            for (AccessTokenAppIdPair accessTokenAppId : AppEventsLogger.stateMap.keySet()) {
                                applicationIds.add(accessTokenAppId.getApplicationId());
                            }
                        }
                        for (String applicationId : applicationIds) {
                            Utility.queryAppSettings(applicationId, true);
                        }
                    }
                };
                backgroundExecutor.scheduleAtFixedRate(attributionRecheckRunnable, 0L, 86400L, TimeUnit.SECONDS);
            }
        }
    }

    private void logEvent(String eventName, Double valueToSum, Bundle parameters, boolean isImplicitlyLogged) {
        AppEvent event = new AppEvent(this.context, eventName, valueToSum, parameters, isImplicitlyLogged);
        logEvent(this.context, event, this.accessTokenAppId);
    }

    private static void logEvent(final Context context, final AppEvent event, final AccessTokenAppIdPair accessTokenAppId) {
        Settings.getExecutor().execute(new Runnable() { // from class: com.facebook.AppEventsLogger.5
            @Override // java.lang.Runnable
            public void run() {
                SessionEventsState state = AppEventsLogger.getSessionEventsState(context, accessTokenAppId);
                state.addEvent(event);
                AppEventsLogger.flushIfNecessary();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void eagerFlush() {
        if (getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY) {
            flush(FlushReason.EAGER_FLUSHING_EVENT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void flushIfNecessary() {
        synchronized (staticLock) {
            if (getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY && getAccumulatedEventCount() > 100) {
                flush(FlushReason.EVENT_THRESHOLD);
            }
        }
    }

    private static int getAccumulatedEventCount() {
        int result;
        synchronized (staticLock) {
            result = 0;
            for (SessionEventsState state : stateMap.values()) {
                result += state.getAccumulatedEventCount();
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SessionEventsState getSessionEventsState(Context context, AccessTokenAppIdPair accessTokenAppId) {
        AttributionIdentifiers attributionIdentifiers = null;
        if (stateMap.get(accessTokenAppId) == null) {
            attributionIdentifiers = AttributionIdentifiers.getAttributionIdentifiers(context);
        }
        synchronized (staticLock) {
            try {
                SessionEventsState state = stateMap.get(accessTokenAppId);
                if (state == null) {
                    SessionEventsState state2 = new SessionEventsState(attributionIdentifiers, context.getPackageName(), hashedDeviceAndAppId);
                    try {
                        stateMap.put(accessTokenAppId, state2);
                        state = state2;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                return state;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    private static SessionEventsState getSessionEventsState(AccessTokenAppIdPair accessTokenAppId) {
        SessionEventsState sessionEventsState;
        synchronized (staticLock) {
            sessionEventsState = stateMap.get(accessTokenAppId);
        }
        return sessionEventsState;
    }

    private static void flush(final FlushReason reason) {
        Settings.getExecutor().execute(new Runnable() { // from class: com.facebook.AppEventsLogger.6
            @Override // java.lang.Runnable
            public void run() {
                AppEventsLogger.flushAndWait(FlushReason.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void flushAndWait(FlushReason reason) {
        synchronized (staticLock) {
            if (!requestInFlight) {
                requestInFlight = true;
                Set<AccessTokenAppIdPair> keysToFlush = new HashSet<>(stateMap.keySet());
                accumulatePersistedEvents();
                FlushStatistics flushResults = null;
                try {
                    flushResults = buildAndExecuteRequests(reason, keysToFlush);
                } catch (Exception e) {
                    Utility.logd(TAG, "Caught unexpected exception while flushing: ", e);
                }
                synchronized (staticLock) {
                    requestInFlight = false;
                }
                if (flushResults != null) {
                    Intent intent = new Intent(ACTION_APP_EVENTS_FLUSHED);
                    intent.putExtra(APP_EVENTS_EXTRA_NUM_EVENTS_FLUSHED, flushResults.numEvents);
                    intent.putExtra(APP_EVENTS_EXTRA_FLUSH_RESULT, flushResults.result);
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent);
                }
            }
        }
    }

    private static FlushStatistics buildAndExecuteRequests(FlushReason reason, Set<AccessTokenAppIdPair> keysToFlush) {
        Request request;
        FlushStatistics flushResults = new FlushStatistics(null);
        boolean limitEventUsage = Settings.getLimitEventAndDataUsage(applicationContext);
        List<Request> requestsToExecute = new ArrayList<>();
        for (AccessTokenAppIdPair accessTokenAppId : keysToFlush) {
            SessionEventsState sessionEventsState = getSessionEventsState(accessTokenAppId);
            if (sessionEventsState != null && (request = buildRequestForSession(accessTokenAppId, sessionEventsState, limitEventUsage, flushResults)) != null) {
                requestsToExecute.add(request);
            }
        }
        if (requestsToExecute.size() > 0) {
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flushing %d events due to %s.", Integer.valueOf(flushResults.numEvents), reason.toString());
            for (Request request2 : requestsToExecute) {
                request2.executeAndWait();
            }
            return flushResults;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FlushStatistics {
        public int numEvents;
        public FlushResult result;

        private FlushStatistics() {
            this.numEvents = 0;
            this.result = FlushResult.SUCCESS;
        }

        /* synthetic */ FlushStatistics(FlushStatistics flushStatistics) {
            this();
        }
    }

    private static Request buildRequestForSession(final AccessTokenAppIdPair accessTokenAppId, final SessionEventsState sessionEventsState, boolean limitEventUsage, final FlushStatistics flushState) {
        int numEvents;
        String applicationId = accessTokenAppId.getApplicationId();
        Utility.FetchedAppSettings fetchedAppSettings = Utility.queryAppSettings(applicationId, false);
        final Request postRequest = Request.newPostRequest(null, String.format("%s/activities", applicationId), null, null);
        Bundle requestParameters = postRequest.getParameters();
        if (requestParameters == null) {
            requestParameters = new Bundle();
        }
        requestParameters.putString("access_token", accessTokenAppId.getAccessToken());
        postRequest.setParameters(requestParameters);
        if (fetchedAppSettings != null && (numEvents = sessionEventsState.populateRequest(postRequest, fetchedAppSettings.supportsImplicitLogging(), fetchedAppSettings.supportsAttribution(), limitEventUsage)) != 0) {
            flushState.numEvents += numEvents;
            postRequest.setCallback(new Request.Callback() { // from class: com.facebook.AppEventsLogger.7
                @Override // com.facebook.Request.Callback
                public void onCompleted(Response response) {
                    AppEventsLogger.handleResponse(AccessTokenAppIdPair.this, postRequest, response, sessionEventsState, flushState);
                }
            });
            return postRequest;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleResponse(AccessTokenAppIdPair accessTokenAppId, Request request, Response response, SessionEventsState sessionEventsState, FlushStatistics flushState) {
        String prettyPrintedEvents;
        FacebookRequestError error = response.getError();
        String resultDescription = "Success";
        FlushResult flushResult = FlushResult.SUCCESS;
        if (error != null) {
            if (error.getErrorCode() == -1) {
                resultDescription = "Failed: No Connectivity";
                flushResult = FlushResult.NO_CONNECTIVITY;
            } else {
                resultDescription = String.format("Failed:\n  Response: %s\n  Error %s", response.toString(), error.toString());
                flushResult = FlushResult.SERVER_ERROR;
            }
        }
        if (Settings.isLoggingBehaviorEnabled(LoggingBehavior.APP_EVENTS)) {
            String eventsJsonString = (String) request.getTag();
            try {
                JSONArray jsonArray = new JSONArray(eventsJsonString);
                prettyPrintedEvents = jsonArray.toString(2);
            } catch (JSONException e) {
                prettyPrintedEvents = "<Can't encode events for debug logging>";
            }
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flush completed\nParams: %s\n  Result: %s\n  Events JSON: %s", request.getGraphObject().toString(), resultDescription, prettyPrintedEvents);
        }
        sessionEventsState.clearInFlightAndStats(error != null);
        if (flushResult == FlushResult.NO_CONNECTIVITY) {
            PersistedEvents.persistEvents(applicationContext, accessTokenAppId, sessionEventsState);
        }
        if (flushResult != FlushResult.SUCCESS && flushState.result != FlushResult.NO_CONNECTIVITY) {
            flushState.result = flushResult;
        }
    }

    private static int accumulatePersistedEvents() {
        PersistedEvents persistedEvents = PersistedEvents.readAndClearStore(applicationContext);
        int result = 0;
        for (AccessTokenAppIdPair accessTokenAppId : persistedEvents.keySet()) {
            SessionEventsState sessionEventsState = getSessionEventsState(applicationContext, accessTokenAppId);
            List<AppEvent> events = persistedEvents.getEvents(accessTokenAppId);
            sessionEventsState.accumulatePersistedEvents(events);
            result += events.size();
        }
        return result;
    }

    private static void notifyDeveloperError(String message) {
        Logger.log(LoggingBehavior.DEVELOPER_ERRORS, "AppEvents", message);
    }

    private static void setSourceApplication(Activity activity) {
        ComponentName callingApplication = activity.getCallingActivity();
        if (callingApplication != null) {
            String callingApplicationPackage = callingApplication.getPackageName();
            if (callingApplicationPackage.equals(activity.getPackageName())) {
                resetSourceApplication();
                return;
            }
            sourceApplication = callingApplicationPackage;
        }
        Intent openIntent = activity.getIntent();
        if (openIntent == null || openIntent.getBooleanExtra(SOURCE_APPLICATION_HAS_BEEN_SET_BY_THIS_INTENT, false)) {
            resetSourceApplication();
            return;
        }
        Bundle applinkData = AppLinks.getAppLinkData(openIntent);
        if (applinkData == null) {
            resetSourceApplication();
            return;
        }
        isOpenedByApplink = true;
        Bundle applinkReferrerData = applinkData.getBundle("referer_app_link");
        if (applinkReferrerData == null) {
            sourceApplication = null;
            return;
        }
        String applinkReferrerPackage = applinkReferrerData.getString("package");
        sourceApplication = applinkReferrerPackage;
        openIntent.putExtra(SOURCE_APPLICATION_HAS_BEEN_SET_BY_THIS_INTENT, true);
    }

    static void setSourceApplication(String applicationPackage, boolean openByAppLink) {
        sourceApplication = applicationPackage;
        isOpenedByApplink = openByAppLink;
    }

    static String getSourceApplication() {
        String openType = "Unclassified";
        if (isOpenedByApplink) {
            openType = "Applink";
        }
        if (sourceApplication != null) {
            return String.valueOf(openType) + "(" + sourceApplication + ")";
        }
        return openType;
    }

    static void resetSourceApplication() {
        sourceApplication = null;
        isOpenedByApplink = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SessionEventsState {
        public static final String ENCODED_EVENTS_KEY = "encoded_events";
        public static final String EVENT_COUNT_KEY = "event_count";
        public static final String NUM_SKIPPED_KEY = "num_skipped";
        private AttributionIdentifiers attributionIdentifiers;
        private String hashedDeviceAndAppId;
        private int numSkippedEventsDueToFullBuffer;
        private String packageName;
        private List<AppEvent> accumulatedEvents = new ArrayList();
        private List<AppEvent> inFlightEvents = new ArrayList();
        private final int MAX_ACCUMULATED_LOG_EVENTS = 1000;

        public SessionEventsState(AttributionIdentifiers identifiers, String packageName, String hashedDeviceAndAppId) {
            this.attributionIdentifiers = identifiers;
            this.packageName = packageName;
            this.hashedDeviceAndAppId = hashedDeviceAndAppId;
        }

        public synchronized void addEvent(AppEvent event) {
            if (this.accumulatedEvents.size() + this.inFlightEvents.size() >= 1000) {
                this.numSkippedEventsDueToFullBuffer++;
            } else {
                this.accumulatedEvents.add(event);
            }
        }

        public synchronized int getAccumulatedEventCount() {
            return this.accumulatedEvents.size();
        }

        public synchronized void clearInFlightAndStats(boolean moveToAccumulated) {
            if (moveToAccumulated) {
                this.accumulatedEvents.addAll(this.inFlightEvents);
            }
            this.inFlightEvents.clear();
            this.numSkippedEventsDueToFullBuffer = 0;
        }

        public int populateRequest(Request request, boolean includeImplicitEvents, boolean includeAttribution, boolean limitEventUsage) {
            synchronized (this) {
                int numSkipped = this.numSkippedEventsDueToFullBuffer;
                this.inFlightEvents.addAll(this.accumulatedEvents);
                this.accumulatedEvents.clear();
                JSONArray jsonArray = new JSONArray();
                for (AppEvent event : this.inFlightEvents) {
                    if (includeImplicitEvents || !event.getIsImplicit()) {
                        jsonArray.put(event.getJSONObject());
                    }
                }
                if (jsonArray.length() == 0) {
                    return 0;
                }
                populateRequest(request, numSkipped, jsonArray, includeAttribution, limitEventUsage);
                return jsonArray.length();
            }
        }

        public synchronized List<AppEvent> getEventsToPersist() {
            List<AppEvent> result;
            result = this.accumulatedEvents;
            this.accumulatedEvents = new ArrayList();
            return result;
        }

        public synchronized void accumulatePersistedEvents(List<AppEvent> events) {
            this.accumulatedEvents.addAll(events);
        }

        private void populateRequest(Request request, int numSkipped, JSONArray events, boolean includeAttribution, boolean limitEventUsage) {
            GraphObject publishParams = GraphObject.Factory.create();
            publishParams.setProperty("event", "CUSTOM_APP_EVENTS");
            if (this.numSkippedEventsDueToFullBuffer > 0) {
                publishParams.setProperty("num_skipped_events", Integer.valueOf(numSkipped));
            }
            if (includeAttribution) {
                Utility.setAppEventAttributionParameters(publishParams, this.attributionIdentifiers, this.hashedDeviceAndAppId, limitEventUsage);
            }
            try {
                Utility.setAppEventExtendedDeviceInfoParameters(publishParams, AppEventsLogger.applicationContext);
            } catch (Exception e) {
            }
            publishParams.setProperty("application_package_name", this.packageName);
            request.setGraphObject(publishParams);
            Bundle requestParameters = request.getParameters();
            if (requestParameters == null) {
                requestParameters = new Bundle();
            }
            String jsonString = events.toString();
            if (jsonString != null) {
                requestParameters.putByteArray("custom_events_file", getStringAsByteArray(jsonString));
                request.setTag(jsonString);
            }
            request.setParameters(requestParameters);
        }

        private byte[] getStringAsByteArray(String jsonString) {
            try {
                byte[] jsonUtf8 = jsonString.getBytes("UTF-8");
                return jsonUtf8;
            } catch (UnsupportedEncodingException e) {
                Utility.logd("Encoding exception: ", e);
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class AppEvent implements Serializable {
        private static final long serialVersionUID = 1;
        private static final HashSet<String> validatedIdentifiers = new HashSet<>();
        private boolean isImplicit;
        private JSONObject jsonObject;
        private String name;

        public AppEvent(Context context, String eventName, Double valueToSum, Bundle parameters, boolean isImplicitlyLogged) {
            try {
                validateIdentifier(eventName);
                this.name = eventName;
                this.isImplicit = isImplicitlyLogged;
                this.jsonObject = new JSONObject();
                this.jsonObject.put("_eventName", eventName);
                this.jsonObject.put("_logTime", System.currentTimeMillis() / 1000);
                this.jsonObject.put("_ui", Utility.getActivityName(context));
                if (valueToSum != null) {
                    this.jsonObject.put("_valueToSum", valueToSum.doubleValue());
                }
                if (this.isImplicit) {
                    this.jsonObject.put("_implicitlyLogged", AppEventsConstants.EVENT_PARAM_VALUE_YES);
                }
                String appVersion = Settings.getAppVersion();
                if (appVersion != null) {
                    this.jsonObject.put("_appVersion", appVersion);
                }
                if (parameters != null) {
                    for (String key : parameters.keySet()) {
                        validateIdentifier(key);
                        Object value = parameters.get(key);
                        if (!(value instanceof String) && !(value instanceof Number)) {
                            throw new FacebookException(String.format("Parameter value '%s' for key '%s' should be a string or a numeric type.", value, key));
                        }
                        this.jsonObject.put(key, value.toString());
                    }
                }
                if (!this.isImplicit) {
                    Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Created app event '%s'", this.jsonObject.toString());
                }
            } catch (FacebookException e) {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Invalid app event name or parameter:", e.toString());
                this.jsonObject = null;
            } catch (JSONException jsonException) {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "JSON encoding for app event failed: '%s'", jsonException.toString());
                this.jsonObject = null;
            }
        }

        public String getName() {
            return this.name;
        }

        private AppEvent(String jsonString, boolean isImplicit) throws JSONException {
            this.jsonObject = new JSONObject(jsonString);
            this.isImplicit = isImplicit;
        }

        /* synthetic */ AppEvent(String str, boolean z, AppEvent appEvent) throws JSONException {
            this(str, z);
        }

        public boolean getIsImplicit() {
            return this.isImplicit;
        }

        public JSONObject getJSONObject() {
            return this.jsonObject;
        }

        private void validateIdentifier(String identifier) throws FacebookException {
            boolean alreadyValidated;
            if (identifier == null || identifier.length() == 0 || identifier.length() > 40) {
                if (identifier == null) {
                    identifier = "<None Provided>";
                }
                throw new FacebookException(String.format("Identifier '%s' must be less than %d characters", identifier, 40));
            }
            synchronized (validatedIdentifiers) {
                alreadyValidated = validatedIdentifiers.contains(identifier);
            }
            if (!alreadyValidated) {
                if (identifier.matches("^[0-9a-zA-Z_]+[0-9a-zA-Z _-]*$")) {
                    synchronized (validatedIdentifiers) {
                        validatedIdentifiers.add(identifier);
                    }
                    return;
                }
                throw new FacebookException(String.format("Skipping event named '%s' due to illegal name - must be under 40 chars and alphanumeric, _, - or space, and not start with a space or hyphen.", identifier));
            }
        }

        /* loaded from: classes.dex */
        private static class SerializationProxyV1 implements Serializable {
            private static final long serialVersionUID = -2488473066578201069L;
            private final boolean isImplicit;
            private final String jsonString;

            private SerializationProxyV1(String jsonString, boolean isImplicit) {
                this.jsonString = jsonString;
                this.isImplicit = isImplicit;
            }

            /* synthetic */ SerializationProxyV1(String str, boolean z, SerializationProxyV1 serializationProxyV1) {
                this(str, z);
            }

            private Object readResolve() throws JSONException {
                return new AppEvent(this.jsonString, this.isImplicit, null);
            }
        }

        private Object writeReplace() {
            return new SerializationProxyV1(this.jsonObject.toString(), this.isImplicit, null);
        }

        public String toString() {
            return String.format("\"%s\", implicit: %b, json: %s", this.jsonObject.optString("_eventName"), Boolean.valueOf(this.isImplicit), this.jsonObject.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class PersistedAppSessionInfo {
        private static final String PERSISTED_SESSION_INFO_FILENAME = "AppEventsLogger.persistedsessioninfo";
        private static Map<AccessTokenAppIdPair, FacebookTimeSpentData> appSessionInfoMap;
        private static final Object staticLock = new Object();
        private static boolean hasChanges = false;
        private static boolean isLoaded = false;
        private static final Runnable appSessionInfoFlushRunnable = new Runnable() { // from class: com.facebook.AppEventsLogger.PersistedAppSessionInfo.1
            @Override // java.lang.Runnable
            public void run() {
                PersistedAppSessionInfo.saveAppSessionInformation(AppEventsLogger.applicationContext);
            }
        };

        PersistedAppSessionInfo() {
        }

        private static void restoreAppSessionInformation(Context context) {
            ObjectInputStream ois;
            ObjectInputStream ois2 = null;
            synchronized (staticLock) {
                try {
                    try {
                        if (!isLoaded) {
                            try {
                                ois = new ObjectInputStream(context.openFileInput(PERSISTED_SESSION_INFO_FILENAME));
                            } catch (FileNotFoundException e) {
                            } catch (Exception e2) {
                                e = e2;
                            }
                            try {
                                appSessionInfoMap = (HashMap) ois.readObject();
                                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "App session info loaded");
                                try {
                                    Utility.closeQuietly(ois);
                                    context.deleteFile(PERSISTED_SESSION_INFO_FILENAME);
                                    if (appSessionInfoMap == null) {
                                        appSessionInfoMap = new HashMap();
                                    }
                                    isLoaded = true;
                                    hasChanges = false;
                                    ois2 = ois;
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            } catch (FileNotFoundException e3) {
                                ois2 = ois;
                                Utility.closeQuietly(ois2);
                                context.deleteFile(PERSISTED_SESSION_INFO_FILENAME);
                                if (appSessionInfoMap == null) {
                                    appSessionInfoMap = new HashMap();
                                }
                                isLoaded = true;
                                hasChanges = false;
                            } catch (Exception e4) {
                                e = e4;
                                ois2 = ois;
                                Log.d(AppEventsLogger.TAG, "Got unexpected exception: " + e.toString());
                                Utility.closeQuietly(ois2);
                                context.deleteFile(PERSISTED_SESSION_INFO_FILENAME);
                                if (appSessionInfoMap == null) {
                                    appSessionInfoMap = new HashMap();
                                }
                                isLoaded = true;
                                hasChanges = false;
                            } catch (Throwable th2) {
                                th = th2;
                                ois2 = ois;
                                Utility.closeQuietly(ois2);
                                context.deleteFile(PERSISTED_SESSION_INFO_FILENAME);
                                if (appSessionInfoMap == null) {
                                    appSessionInfoMap = new HashMap();
                                }
                                isLoaded = true;
                                hasChanges = false;
                                throw th;
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }

        static void saveAppSessionInformation(Context context) {
            ObjectOutputStream oos;
            ObjectOutputStream oos2 = null;
            synchronized (staticLock) {
                try {
                    try {
                        if (hasChanges) {
                            try {
                                oos = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput(PERSISTED_SESSION_INFO_FILENAME, 0)));
                            } catch (Exception e) {
                                e = e;
                            }
                            try {
                                oos.writeObject(appSessionInfoMap);
                                hasChanges = false;
                                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "App session info saved");
                                try {
                                    Utility.closeQuietly(oos);
                                    oos2 = oos;
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            } catch (Exception e2) {
                                e = e2;
                                oos2 = oos;
                                Log.d(AppEventsLogger.TAG, "Got unexpected exception: " + e.toString());
                                Utility.closeQuietly(oos2);
                            } catch (Throwable th2) {
                                th = th2;
                                oos2 = oos;
                                Utility.closeQuietly(oos2);
                                throw th;
                            }
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
        }

        static void onResume(Context context, AccessTokenAppIdPair accessTokenAppId, AppEventsLogger logger, long eventTime, String sourceApplicationInfo) {
            synchronized (staticLock) {
                FacebookTimeSpentData timeSpentData = getTimeSpentData(context, accessTokenAppId);
                timeSpentData.onResume(logger, eventTime, sourceApplicationInfo);
                onTimeSpentDataUpdate();
            }
        }

        static void onSuspend(Context context, AccessTokenAppIdPair accessTokenAppId, AppEventsLogger logger, long eventTime) {
            synchronized (staticLock) {
                FacebookTimeSpentData timeSpentData = getTimeSpentData(context, accessTokenAppId);
                timeSpentData.onSuspend(logger, eventTime);
                onTimeSpentDataUpdate();
            }
        }

        private static FacebookTimeSpentData getTimeSpentData(Context context, AccessTokenAppIdPair accessTokenAppId) {
            restoreAppSessionInformation(context);
            FacebookTimeSpentData result = appSessionInfoMap.get(accessTokenAppId);
            if (result == null) {
                FacebookTimeSpentData result2 = new FacebookTimeSpentData();
                appSessionInfoMap.put(accessTokenAppId, result2);
                return result2;
            }
            return result;
        }

        private static void onTimeSpentDataUpdate() {
            if (!hasChanges) {
                hasChanges = true;
                AppEventsLogger.backgroundExecutor.schedule(appSessionInfoFlushRunnable, 30L, TimeUnit.SECONDS);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class PersistedEvents {
        static final String PERSISTED_EVENTS_FILENAME = "AppEventsLogger.persistedevents";
        private static Object staticLock = new Object();
        private Context context;
        private HashMap<AccessTokenAppIdPair, List<AppEvent>> persistedEvents = new HashMap<>();

        private PersistedEvents(Context context) {
            this.context = context;
        }

        public static PersistedEvents readAndClearStore(Context context) {
            PersistedEvents persistedEvents;
            synchronized (staticLock) {
                persistedEvents = new PersistedEvents(context);
                persistedEvents.readAndClearStore();
            }
            return persistedEvents;
        }

        public static void persistEvents(Context context, AccessTokenAppIdPair accessTokenAppId, SessionEventsState eventsToPersist) {
            Map<AccessTokenAppIdPair, SessionEventsState> map = new HashMap<>();
            map.put(accessTokenAppId, eventsToPersist);
            persistEvents(context, map);
        }

        public static void persistEvents(Context context, Map<AccessTokenAppIdPair, SessionEventsState> eventsToPersist) {
            synchronized (staticLock) {
                PersistedEvents persistedEvents = readAndClearStore(context);
                for (Map.Entry<AccessTokenAppIdPair, SessionEventsState> entry : eventsToPersist.entrySet()) {
                    List<AppEvent> events = entry.getValue().getEventsToPersist();
                    if (events.size() != 0) {
                        persistedEvents.addEvents(entry.getKey(), events);
                    }
                }
                persistedEvents.write();
            }
        }

        public Set<AccessTokenAppIdPair> keySet() {
            return this.persistedEvents.keySet();
        }

        public List<AppEvent> getEvents(AccessTokenAppIdPair accessTokenAppId) {
            return this.persistedEvents.get(accessTokenAppId);
        }

        private void write() {
            ObjectOutputStream oos;
            ObjectOutputStream oos2 = null;
            try {
                try {
                    oos = new ObjectOutputStream(new BufferedOutputStream(this.context.openFileOutput(PERSISTED_EVENTS_FILENAME, 0)));
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                oos.writeObject(this.persistedEvents);
                Utility.closeQuietly(oos);
            } catch (Exception e2) {
                e = e2;
                oos2 = oos;
                Log.d(AppEventsLogger.TAG, "Got unexpected exception: " + e.toString());
                Utility.closeQuietly(oos2);
            } catch (Throwable th2) {
                th = th2;
                oos2 = oos;
                Utility.closeQuietly(oos2);
                throw th;
            }
        }

        private void readAndClearStore() {
            ObjectInputStream ois;
            ObjectInputStream ois2 = null;
            try {
                try {
                    ois = new ObjectInputStream(new BufferedInputStream(this.context.openFileInput(PERSISTED_EVENTS_FILENAME)));
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    HashMap<AccessTokenAppIdPair, List<AppEvent>> obj = (HashMap) ois.readObject();
                    this.context.getFileStreamPath(PERSISTED_EVENTS_FILENAME).delete();
                    this.persistedEvents = obj;
                    Utility.closeQuietly(ois);
                    ois2 = ois;
                } catch (FileNotFoundException e) {
                    ois2 = ois;
                    Utility.closeQuietly(ois2);
                } catch (Exception e2) {
                    e = e2;
                    ois2 = ois;
                    Log.d(AppEventsLogger.TAG, "Got unexpected exception: " + e.toString());
                    Utility.closeQuietly(ois2);
                } catch (Throwable th2) {
                    th = th2;
                    ois2 = ois;
                    Utility.closeQuietly(ois2);
                    throw th;
                }
            } catch (FileNotFoundException e3) {
            } catch (Exception e4) {
                e = e4;
            }
        }

        public void addEvents(AccessTokenAppIdPair accessTokenAppId, List<AppEvent> eventsToPersist) {
            if (!this.persistedEvents.containsKey(accessTokenAppId)) {
                this.persistedEvents.put(accessTokenAppId, new ArrayList());
            }
            this.persistedEvents.get(accessTokenAppId).addAll(eventsToPersist);
        }
    }
}
