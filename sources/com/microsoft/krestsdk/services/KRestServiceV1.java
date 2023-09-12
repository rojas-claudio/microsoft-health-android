package com.microsoft.krestsdk.services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.AppEventsConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.cache.CacheUtils;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.models.golf.GolfRegionResponse;
import com.microsoft.kapp.models.golf.GolfStateResponse;
import com.microsoft.kapp.sensor.PhoneSensorDataProvider;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.CourseFilters;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GsonUtils;
import com.microsoft.kapp.utils.LocaleProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.models.AppConfigInfo;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.CategoryType;
import com.microsoft.krestsdk.models.ConnectedDevice;
import com.microsoft.krestsdk.models.CurrentGuidedWorkout;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.FeaturedWorkout;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalOperationAddDto;
import com.microsoft.krestsdk.models.GoalOperationResultDto;
import com.microsoft.krestsdk.models.GoalOperationUpdateDto;
import com.microsoft.krestsdk.models.GoalStatus;
import com.microsoft.krestsdk.models.GoalTemplateDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GolfCourse;
import com.microsoft.krestsdk.models.GolfCourseSearchResults;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.InsightMetadata;
import com.microsoft.krestsdk.models.ODataResponse;
import com.microsoft.krestsdk.models.Partners;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.models.UserProfile;
import com.microsoft.krestsdk.models.sensor.SensorData;
import com.microsoft.krestsdk.services.RestService;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.http.client.HttpResponseException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class KRestServiceV1 implements RestService {
    private static final String DAILY_SUMMARY_METHOD = "/v2/UserDailySummary";
    private static final int DELETE_REQUEST = 2;
    private static final String EVENTS_METHOD = "/v1/Events";
    private static final String HOURLY_SUMMARIES_METHOD = "/v2/UserHourlySummary";
    private static final String INSIGHTS_METHOD = "/v1/Insights";
    private static final int POST_REQUEST = 0;
    private static final int PUT_REQUEST = 1;
    private static final String RAISED_INSIGHTS_METHOD = "/v1/RaisedInsights";
    public static final String TIME_ZONE_UTC_OFFSET = "timeZoneUtcOffset";
    public static final String TMAG_BASE = "/v2/5d784f21-6bd2-4d9d-b9a1-8af7f85839c0/";
    public static final String TMAG_LIST_COURSES_BY_REGION_PATH = "/v2/5d784f21-6bd2-4d9d-b9a1-8af7f85839c0/course/regions/%d";
    public static final String TMAG_LIST_COURSES_BY_STATE_PATH = "/v2/5d784f21-6bd2-4d9d-b9a1-8af7f85839c0/course/states/%d";
    public static final String TMAG_LIST_REGIONS_PATH = "/v2/5d784f21-6bd2-4d9d-b9a1-8af7f85839c0/course/regions";
    public static final String TMAG_LIST_STATES_PATH = "/v2/5d784f21-6bd2-4d9d-b9a1-8af7f85839c0/course/regions/%d/states";
    public static final String TMAG_SEARCH_PATH = "/v2/5d784f21-6bd2-4d9d-b9a1-8af7f85839c0/course/search";
    private CacheService mCacheService;
    private Context mContext;
    private CredentialsManager mCredentialsManager;
    private NetworkProvider mNetworkProvider;
    private PhoneSensorDataProvider mPhoneSensorDataProvider;
    private SettingsProvider mSettingsProvider;
    private Callback<Void> noOpCallback = new Callback<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.1
        @Override // com.microsoft.kapp.Callback
        public void callback(Void result) {
        }

        @Override // com.microsoft.kapp.Callback
        public void onError(Exception ex) {
        }
    };
    private static final String TAG = KRestServiceV1.class.getSimpleName();
    private static final Gson CUSTOM_GSON_DESERIALIZER = GsonUtils.getCustomDeserializer();
    private static final Gson CUSTOM_GSON_SERIALIZER = GsonUtils.getCustomSerializer();

    public KRestServiceV1(Context context, NetworkProvider provider, CredentialsManager credentialsManager, CacheService cacheService, SettingsProvider settingsProvider, PhoneSensorDataProvider phoneSensorDataProvider) {
        this.mContext = context;
        this.mNetworkProvider = provider;
        this.mCredentialsManager = credentialsManager;
        this.mCacheService = cacheService;
        this.mSettingsProvider = settingsProvider;
        this.mPhoneSensorDataProvider = phoneSensorDataProvider;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getDailySummaries(LocalDate startDate, final LocalDate endDate, final Callback<List<UserDailySummary>> callback) {
        DateTime dt = endDate.plusDays(-1).toDateTimeAtStartOfDay();
        final long endTimezoneOffsetInMins = CommonUtils.getLocalTimeZoneOffsetInMinsAtInstant(dt);
        long startTimezoneOffsetInMins = CommonUtils.getLocalTimeZoneOffsetInMinsAtInstant(startDate.toDateTimeAtStartOfDay());
        if (endTimezoneOffsetInMins == startTimezoneOffsetInMins) {
            getDailySummaries(startDate, endDate, endTimezoneOffsetInMins, callback);
            return;
        }
        LocalDate changeDate = endDate;
        LocalDate date = startDate;
        while (true) {
            if (date.isAfter(endDate)) {
                break;
            }
            long timezoneOffsetInMins = CommonUtils.getLocalTimeZoneOffsetInMinsAtInstant(date.toDateTimeAtStartOfDay());
            if (timezoneOffsetInMins == startTimezoneOffsetInMins) {
                date = date.plusDays(1);
            } else {
                changeDate = date;
                break;
            }
        }
        final LocalDate boundaryDate = changeDate;
        final List<UserDailySummary> aggregateResult = new ArrayList<>();
        final Callback<List<UserDailySummary>> chainedCallback2 = new Callback<List<UserDailySummary>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.2
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserDailySummary> result) {
                aggregateResult.addAll(result);
                callback.callback(aggregateResult);
            }
        };
        Callback<List<UserDailySummary>> chainedCallback1 = new Callback<List<UserDailySummary>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.3
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserDailySummary> result) {
                aggregateResult.addAll(result);
                KRestServiceV1.this.getDailySummaries(boundaryDate, endDate, endTimezoneOffsetInMins, chainedCallback2);
            }
        };
        getDailySummaries(startDate, boundaryDate, startTimezoneOffsetInMins, chainedCallback1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDailySummaries(LocalDate startDate, LocalDate endDate, long timezoneOffsetInMins, Callback<List<UserDailySummary>> callback) {
        ODataRequest request = new ODataRequest(DAILY_SUMMARY_METHOD);
        request.addArgumentQuotes(TIME_ZONE_UTC_OFFSET, Long.toString(timezoneOffsetInMins));
        request.addArgumentQuotes("startDate", KRestServiceUtils.formatDate(startDate));
        request.addArgumentQuotes("endDate", KRestServiceUtils.formatDate(endDate));
        if (this.mSettingsProvider.isUsePhoneSensorData()) {
            request.addNonOdataParameterQuotes("deviceId", this.mPhoneSensorDataProvider.getDeviceId().toString());
        }
        KRestQueryOData<List<UserDailySummary>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG), CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<UserDailySummary>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.4
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getBestGoalsByCategory(CategoryType category, Callback<List<GoalDto>> callback) {
        Validate.notNull(category, "Category");
        Validate.notNull(callback, "callback");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(KCloudConstants.CATEGORY_TYPE, String.valueOf(category.value()));
        KRestQuery<List<GoalDto>> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.GOALSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOALS_BESTS, wrapCallback(callback), urlParams, new TypeToken<List<GoalDto>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.5
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRaisedInsights(Callback<List<RaisedInsight>> callback, RaisedInsightQuery query) {
        ODataRequest request = new ODataRequest(RAISED_INSIGHTS_METHOD);
        request.addArgumentQuotes(KCloudConstants.DATA_USED, query.getDataUsed());
        request.addArgumentQuotes(KCloudConstants.TIMESPAN, query.getTimeSpan());
        request.addArgumentQuotes("scope", query.getScope());
        KRestQueryOData<List<RaisedInsight>> getCall = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG), CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<RaisedInsight>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.6
        }, wrapCallback(callback));
        getCall.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getInsights(Callback<List<InsightMetadata>> callback) {
        ODataRequest request = new ODataRequest(INSIGHTS_METHOD);
        KRestQueryOData<List<InsightMetadata>> queryInsight = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG), CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<InsightMetadata>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.7
        }, wrapCallback(callback));
        queryInsight.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getUserActivitiesByHour(LocalDate date, Callback<List<UserActivity>> callback) {
        ODataRequest request = new ODataRequest(HOURLY_SUMMARIES_METHOD);
        request.addArgumentQuotes("period", "h");
        request.setFilter("TimeOfDay ge datetimeoffset'%s' and TimeOfDay lt datetimeoffset'%s'", KRestServiceUtils.formatDateTime(date), KRestServiceUtils.formatDateTime(date.plusDays(1)));
        if (this.mSettingsProvider.isUsePhoneSensorData()) {
            request.addNonOdataParameterQuotes("deviceId", this.mPhoneSensorDataProvider.getDeviceId().toString());
        }
        KRestQueryOData<List<UserActivity>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG), CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<UserActivity>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.8
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getUserProfile(Callback<UserProfile> callback) {
        KRestQuery<UserProfile> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.USERPROFILETAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.USER_PROFILE_PROVIDER, wrapCallback(callback), new TypeToken<UserProfile>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.9
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void updateUserProfile(UserProfile updatedProfile, Callback<Void> callback) {
        Validate.notNull(updatedProfile, "updatedProfile");
        KRestQueryPut<UserProfile, Void> query = new KRestQueryPut<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.USERPROFILETAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.USER_GOAL_UPDATE, updatedProfile, wrapCallback(callback), new TypeToken<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.10
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getEventsSince(DateTime time, boolean expandMapPoints, Callback<List<UserEvent>> callback) {
        Validate.notNull(time, "time");
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        if (expandMapPoints) {
            request.addParameter("expand", "MapPoints");
        }
        request.setFilter("StartTime ge datetimeoffset'%s'", KRestServiceUtils.formatDateTime(time));
        KRestQueryOData<List<UserEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG), CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<UserEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.11
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getSleepEvents(LocalDate startDayId, LocalDate endDayId, Callback<List<SleepEvent>> callback) {
        if (startDayId.isAfter(endDayId)) {
            throw new IllegalArgumentException("startDayId cannot be after endDayId.");
        }
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Sleeping.toString());
        request.setFilter("DayId ge datetime'%s' and DayId le datetime'%s'", KRestServiceUtils.formatDate(startDayId), KRestServiceUtils.formatDate(endDayId));
        request.addParameter("expand", "Sequences,Info");
        KRestQueryOData<List<SleepEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Sleeping.toString())), CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<SleepEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.12
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentSleepEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<SleepEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Sleeping.toString());
        request.addParameter("top", Integer.toString(count));
        request.setFilter("StartTime lt datetimeoffset'%s'", KRestServiceUtils.formatDateTime(startTime));
        if (expandSequences) {
            request.addParameter("expand", "Info");
        }
        getSleepEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Sleeping.toString())), callback);
    }

    private void getSleepEvents(ODataRequest request, List<String> tags, Callback<List<SleepEvent>> callback) {
        KRestQueryOData<List<SleepEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, tags, CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<SleepEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.13
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopSleepEvents(int count, boolean expandSequences, Callback<List<SleepEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Sleeping.toString());
        request.addParameter("top", Integer.toString(count));
        if (expandSequences) {
            request.addParameter("expand", "Sequences,Info");
        }
        getSleepEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Sleeping.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public synchronized void getSleepEventById(final String eventId, ArrayList<RestService.ExpandType> expandTypes, final Callback<SleepEvent> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventId", eventId);
        String expandParams = getExpandParams(expandTypes);
        if (!TextUtils.isEmpty(expandParams)) {
            request.addParameter("expand", expandParams);
        }
        getSleepEvents(request, Arrays.asList(CacheUtils.getEventTag(String.valueOf(eventId))), new Callback<List<SleepEvent>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.14
            @Override // com.microsoft.kapp.Callback
            public void callback(List<SleepEvent> result) {
                if (result != null && result.size() > 0) {
                    callback.callback(result.get(0));
                } else {
                    callback.onError(new IllegalStateException(String.format("No sleep event found with id %s", eventId)));
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopGolfEvents(int count, boolean expandSequences, Callback<List<GolfEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Golf.toString());
        request.addParameter("top", Integer.toString(count));
        if (expandSequences) {
            request.addParameter("expand", "Sequences");
        }
        getGolfEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.GOLFEVENTSLOWCACHE, CacheUtils.getEventTypeTag(EventType.Golf.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfEventById(final String eventId, boolean expandSequences, final Callback<GolfEvent> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventId", eventId);
        if (expandSequences) {
            request.addParameter("expand", "Sequences");
        }
        getGolfEvents(request, Arrays.asList(CacheUtils.getEventTag(String.valueOf(eventId)), CacheUtils.GOLFEVENTSLOWCACHE), new Callback<List<GolfEvent>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.15
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GolfEvent> result) {
                if (result != null && result.size() > 0) {
                    callback.callback(result.get(0));
                } else {
                    callback.onError(new IllegalStateException(String.format("No golf event found with id %s", eventId)));
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentGolfEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<GolfEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Golf.toString());
        request.addParameter("top", Integer.toString(count));
        request.setFilter("StartTime lt datetimeoffset'%s'", KRestServiceUtils.formatDateTime(startTime));
        if (expandSequences) {
            request.addParameter("expand", "Sequences");
        }
        getGolfEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Golf.toString())), callback);
    }

    private void getGolfEvents(ODataRequest request, List<String> tags, Callback<List<GolfEvent>> callback) {
        KRestQueryOData<List<GolfEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, tags, CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<GolfEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.16
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopUserEvents(int count, Callback<List<UserEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addParameter("top", Integer.toString(count));
        getUserEvents(request, callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentUserEvents(DateTime startTime, int count, Callback<List<UserEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addParameter("top", Integer.toString(count));
        request.setFilter("StartTime lt datetimeoffset'%s'", KRestServiceUtils.formatDateTime(startTime));
        getUserEvents(request, callback);
    }

    private void getUserEvents(ODataRequest request, Callback<List<UserEvent>> callback) {
        KRestQueryOData<List<UserEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG), CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<UserEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.17
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopRunEvents(boolean isMetric, int count, boolean expandSequencesAndMappoints, Callback<List<RunEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Running.toString());
        request.addArgumentQuotes("selectedSplitDistance", isMetric ? KCloudConstants.RUN_SPLIT_DISTANCE_METRIC : KCloudConstants.RUN_SPLIT_DISTANCE_IMPERIAL);
        request.addParameter("top", Integer.toString(count));
        if (expandSequencesAndMappoints) {
            request.addParameter("expand", "Sequences,MapPoints,Info");
        }
        getRunEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Running.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopRunEvents(boolean isMetric, int count, Callback<List<RunEvent>> callback) {
        getTopRunEvents(isMetric, count, false, callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopBikeEvents(boolean isMetric, int count, boolean expandSequencesAndMappoints, Callback<List<BikeEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Biking.toString());
        request.addArgumentQuotes("selectedSplitDistance", isMetric ? KCloudConstants.RUN_SPLIT_DISTANCE_METRIC : KCloudConstants.RUN_SPLIT_DISTANCE_IMPERIAL);
        request.addParameter("top", Integer.toString(count));
        if (expandSequencesAndMappoints) {
            request.addParameter("expand", "Sequences,MapPoints,Info");
        }
        getBikeEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Running.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopGolfEvents(int count, ArrayList<RestService.ExpandType> expandTypes, Callback<List<GolfEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Golf.toString());
        request.addParameter("top", Integer.toString(count));
        String expandParams = getExpandParams(expandTypes);
        if (!TextUtils.isEmpty(expandParams)) {
            request.addParameter("expand", expandParams);
        }
        getGolfEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.GOLFEVENTSLOWCACHE, CacheUtils.getEventTypeTag(EventType.Golf.toString())), callback);
    }

    public void getRecentRunEvents(boolean isMetric, int skip, int count, Callback<List<RunEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Running.toString());
        request.addArgumentQuotes("selectedSplitDistance", isMetric ? KCloudConstants.RUN_SPLIT_DISTANCE_METRIC : KCloudConstants.RUN_SPLIT_DISTANCE_IMPERIAL);
        request.addParameter("top", Integer.toString(count));
        request.addParameter("skip", Integer.toString(skip));
        getRunEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Running.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentRunEvents(boolean isMetric, DateTime startTime, int count, Callback<List<RunEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Running.toString());
        request.addArgumentQuotes("selectedSplitDistance", isMetric ? KCloudConstants.RUN_SPLIT_DISTANCE_METRIC : KCloudConstants.RUN_SPLIT_DISTANCE_IMPERIAL);
        request.addParameter("top", Integer.toString(count));
        request.setFilter("StartTime lt datetimeoffset'%s'", KRestServiceUtils.formatDateTime(startTime));
        getRunEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Running.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public synchronized void getRunEventById(boolean isMetric, final String eventId, ArrayList<RestService.ExpandType> expandTypes, final Callback<RunEvent> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventId", eventId);
        request.addArgumentQuotes("selectedSplitDistance", isMetric ? KCloudConstants.RUN_SPLIT_DISTANCE_METRIC : KCloudConstants.RUN_SPLIT_DISTANCE_IMPERIAL);
        String expandParams = getExpandParams(expandTypes);
        if (!TextUtils.isEmpty(expandParams)) {
            request.addParameter("expand", expandParams);
        }
        getRunEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.getEventTag(String.valueOf(eventId))), new Callback<List<RunEvent>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.18
            @Override // com.microsoft.kapp.Callback
            public void callback(List<RunEvent> result) {
                if (result != null && result.size() > 0) {
                    callback.callback(result.get(0));
                } else {
                    callback.onError(new IllegalStateException(String.format("No run found with id %s", eventId)));
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfEventById(final String eventId, ArrayList<RestService.ExpandType> expandTypes, final Callback<GolfEvent> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventId", eventId);
        String expandParams = getExpandParams(expandTypes);
        if (!TextUtils.isEmpty(expandParams)) {
            request.addParameter("expand", expandParams);
        }
        getGolfEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.getEventTag(String.valueOf(eventId))), new Callback<List<GolfEvent>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.19
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GolfEvent> result) {
                if (result != null && result.size() > 0) {
                    callback.callback(result.get(0));
                } else {
                    callback.onError(new IllegalStateException(String.format("No run found with id %s", eventId)));
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    private void getRunEvents(ODataRequest request, List<String> tags, Callback<List<RunEvent>> callback) {
        KRestQueryOData<List<RunEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, tags, CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<RunEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.20
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentBikeEvents(boolean isMetric, DateTime startTime, int count, Callback<List<BikeEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("selectedSplitDistance", isMetric ? KCloudConstants.RUN_SPLIT_DISTANCE_METRIC : KCloudConstants.RUN_SPLIT_DISTANCE_IMPERIAL);
        request.addArgumentQuotes("eventType", EventType.Biking.toString());
        request.addParameter("top", Integer.toString(count));
        request.setFilter("StartTime lt datetimeoffset'%s'", KRestServiceUtils.formatDateTime(startTime));
        getBikeEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Biking.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public synchronized void getBikeEventById(boolean isMetric, final String eventId, ArrayList<RestService.ExpandType> expandTypes, final Callback<BikeEvent> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventId", eventId);
        request.addArgumentQuotes("selectedSplitDistance", isMetric ? KCloudConstants.RUN_SPLIT_DISTANCE_METRIC : KCloudConstants.RUN_SPLIT_DISTANCE_IMPERIAL);
        String expandParams = getExpandParams(expandTypes);
        if (!TextUtils.isEmpty(expandParams)) {
            request.addParameter("expand", expandParams);
        }
        getBikeEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.getEventTag(String.valueOf(eventId))), new Callback<List<BikeEvent>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.21
            @Override // com.microsoft.kapp.Callback
            public void callback(List<BikeEvent> result) {
                if (result != null && result.size() > 0) {
                    callback.callback(result.get(0));
                } else {
                    callback.onError(new IllegalStateException(String.format("No bike event found with id %s", eventId)));
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    private void getBikeEvents(ODataRequest request, List<String> tags, Callback<List<BikeEvent>> callback) {
        KRestQueryOData<List<BikeEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, tags, CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<BikeEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.22
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopExerciseEvents(int count, boolean expandSequences, Callback<List<ExerciseEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Workout.toString());
        request.addParameter("top", Integer.toString(count));
        if (expandSequences) {
            request.addParameter("expand", "Info");
        }
        getExerciseEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Workout.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentExerciseEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<ExerciseEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.Workout.toString());
        request.addParameter("top", Integer.toString(count));
        if (expandSequences) {
            request.addParameter("expand", "Info");
        }
        request.setFilter("StartTime lt datetimeoffset'%s'", KRestServiceUtils.formatDateTime(startTime));
        getExerciseEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.Workout.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public synchronized void getExerciseEventById(final String eventId, ArrayList<RestService.ExpandType> expandTypes, final Callback<ExerciseEvent> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventId", eventId);
        String expandParams = getExpandParams(expandTypes);
        if (!TextUtils.isEmpty(expandParams)) {
            request.addParameter("expand", expandParams);
        }
        getExerciseEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.getEventTag(String.valueOf(eventId))), new Callback<List<ExerciseEvent>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.23
            @Override // com.microsoft.kapp.Callback
            public void callback(List<ExerciseEvent> result) {
                if (result != null && result.size() > 0) {
                    callback.callback(result.get(0));
                } else {
                    callback.onError(new IllegalStateException(String.format("No exercise found with id %s", eventId)));
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getUserEventById(final String eventId, final Callback<UserEvent> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventId", eventId);
        request.addParameter("expand", "Info");
        getUserEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.getEventTag(String.valueOf(eventId))), new Callback<List<UserEvent>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.24
            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserEvent> result) {
                if (result != null && result.size() > 0) {
                    callback.callback(result.get(0));
                } else {
                    callback.onError(new IllegalStateException(String.format("No user event found with id %s", eventId)));
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGuidedWorkoutEventById(String eventId, Callback<GuidedWorkoutEvent> callback) {
        getGuidedWorkoutEventById(eventId, false, callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public synchronized void getGuidedWorkoutEventById(String eventId, boolean hasSequences, Callback<GuidedWorkoutEvent> callback) {
        ArrayList<RestService.ExpandType> expandTypes = new ArrayList<>();
        if (hasSequences) {
            expandTypes.add(RestService.ExpandType.INFO);
            expandTypes.add(RestService.ExpandType.SEQUENCES);
        }
        getGuidedWorkoutEventById(eventId, expandTypes, callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGuidedWorkoutEventById(final String eventId, ArrayList<RestService.ExpandType> expandTypes, final Callback<GuidedWorkoutEvent> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventId", eventId);
        String params = getExpandParams(expandTypes);
        if (!TextUtils.isEmpty(params)) {
            request.addParameter("expand", params);
        }
        getGuidedWorkoutEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.getEventTag(String.valueOf(eventId))), new Callback<List<GuidedWorkoutEvent>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.25
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GuidedWorkoutEvent> result) {
                if (result != null && result.size() > 0) {
                    callback.callback(result.get(0));
                } else {
                    callback.onError(new IllegalStateException(String.format("No exercise found with id %s", eventId)));
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public CurrentGuidedWorkout getCurrentGuidedWorkout() throws KRestException {
        Map<String, String> urlParams = new HashMap<>();
        try {
            String response = getText(KCloudConstants.CURRENT_GUIDED_WORKOUT, urlParams, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.LASTSYNCEDWORKOUT));
            CurrentGuidedWorkout currentWorkout = (CurrentGuidedWorkout) CUSTOM_GSON_DESERIALIZER.fromJson(response, new TypeToken<CurrentGuidedWorkout>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.26
            }.getType());
            QueryUtils.validateDeserializedObject(currentWorkout);
            return currentWorkout;
        } catch (JsonParseException exception) {
            throw new KRestException("Invalid JSON on current workout response.", exception);
        } catch (Exception exception2) {
            throw new KRestException("Error during when fetching Current Guided Workout.", exception2);
        }
    }

    private void getExerciseEvents(ODataRequest request, List<String> tags, Callback<List<ExerciseEvent>> callback) {
        KRestQueryOData<List<ExerciseEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, tags, CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<ExerciseEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.27
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    private void getUserEvents(ODataRequest request, List<String> tags, Callback<List<UserEvent>> callback) {
        KRestQueryOData<List<UserEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, tags, CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<UserEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.28
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopGuidedWorkoutEvents(int count, boolean expandSequences, Callback<List<GuidedWorkoutEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.GuidedWorkout.toString());
        request.addParameter("top", Integer.toString(count));
        if (expandSequences) {
            request.addParameter("expand", "Info");
        }
        getGuidedWorkoutEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.GuidedWorkout.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getPostGuidedWorkoutDetails(Callback<List<GuidedWorkoutEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.GuidedWorkout.toString());
        request.addParameter("top", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        request.addParameter("expand", "Info,Sequences");
        getGuidedWorkoutEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.GuidedWorkout.toString())), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public GuidedWorkoutEvent getPostGuidedWorkoutDetails() throws KRestException {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.GuidedWorkout.toString());
        request.addParameter("top", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        request.addParameter("expand", "Info,Sequences");
        Map<String, String> urlParams = new HashMap<>();
        String response = getText(request.getUrl(), urlParams, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.GuidedWorkout.toString())));
        try {
            ODataResponse<List<GuidedWorkoutEvent>> oDataResponse = (ODataResponse) CUSTOM_GSON_DESERIALIZER.fromJson(response, new TypeToken<ODataResponse<List<GuidedWorkoutEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.29
            }.getType());
            QueryUtils.validateDeserializedObject(oDataResponse);
            List<GuidedWorkoutEvent> list = oDataResponse.getValue();
            if (list == null || list.size() == 0) {
                return null;
            }
            return list.get(0);
        } catch (JsonParseException exception) {
            throw new KRestException("Invalid JSON on GuidedWorkoutEvent response.", exception);
        } catch (Exception exception2) {
            throw new KRestException("Error during when fetching GuidedWorkoutEvent.", exception2);
        }
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentGuidedWorkoutEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<GuidedWorkoutEvent>> callback) {
        ODataRequest request = new ODataRequest(EVENTS_METHOD);
        request.addArgumentQuotes("eventType", EventType.GuidedWorkout.toString());
        request.addParameter("top", Integer.toString(count));
        if (expandSequences) {
            request.addParameter("expand", "Info");
        }
        request.setFilter("StartTime lt datetimeoffset'%s'", KRestServiceUtils.formatDateTime(startTime));
        getGuidedWorkoutEvents(request, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.EVENTSTAG, CacheUtils.getEventTypeTag(EventType.GuidedWorkout.toString())), callback);
    }

    private void getGuidedWorkoutEvents(ODataRequest request, List<String> tags, Callback<List<GuidedWorkoutEvent>> callback) {
        KRestQueryOData<List<GuidedWorkoutEvent>> query = new KRestQueryOData<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, tags, CUSTOM_GSON_DESERIALIZER, request, new TypeToken<ODataResponse<List<GuidedWorkoutEvent>>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.30
        }, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getFeaturedWorkouts(int age, String gender, Callback<List<FeaturedWorkout>> callback) {
        Map<String, String> urlParams = new HashMap<>();
        String langLocale = LocaleProvider.getLocaleSettings(this.mContext).getLocaleName();
        urlParams.put(KCloudConstants.AGE, Integer.toString(age));
        urlParams.put(KCloudConstants.GENDER, gender);
        urlParams.put(KCloudConstants.REGION, langLocale);
        KRestQuery<List<FeaturedWorkout>> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.FEATUREDWORKOUTSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.FEATURED_WORKOUT, wrapCallback(callback), urlParams, new TypeToken<List<FeaturedWorkout>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.31
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getNearbyCourseList(double latitude, double longitude, CourseFilters filters, int page, int count, Callback<GolfCourseSearchResults> callback) {
        Map<String, String> urlParams = new HashMap<>();
        ODataRequest request = new ODataRequest(TMAG_SEARCH_PATH);
        request.addNonOdataParameter("latitude", String.valueOf(latitude));
        request.addNonOdataParameter("longitude", String.valueOf(longitude));
        addFiltersToRequest(request, filters);
        request.addNonOdataParameter("page", String.valueOf(page));
        request.addNonOdataParameter("per_page", String.valueOf(count));
        HashMap<String, String> telemetryParams = new HashMap<>();
        telemetryParams.put("Type", TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NEARBY);
        telemetryParams.put(TelemetryConstants.TimedEvents.Cloud.Golf.COURSE_TYPE, filters.getCourseTypeFilter().toString());
        telemetryParams.put(TelemetryConstants.TimedEvents.Cloud.Golf.HOLE_COUNT, filters.getHoleTypeFilter().toString());
        KRestQuery<GolfCourseSearchResults> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList("TMAG"), CUSTOM_GSON_DESERIALIZER, request.getUrl(), wrapCallback(callback), urlParams, new TypeToken<GolfCourseSearchResults>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.32
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    private void addFiltersToRequest(ODataRequest request, CourseFilters filters) {
        Map<String, String> cloudFilters = new HashMap<>();
        if (filters != null) {
            cloudFilters = filters.getCloudFilterList();
        }
        for (String key : cloudFilters.keySet()) {
            request.addNonOdataParameter(key, cloudFilters.get(key));
        }
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentCourseList(Callback<GolfCourseSearchResults> callback) {
        Map<String, String> urlParams = new HashMap<>();
        KRestQuery<GolfCourseSearchResults> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList("TMAG"), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOLF_RECENT, wrapCallback(callback), urlParams, new TypeToken<GolfCourseSearchResults>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.33
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfCoursesByName(String name, CourseFilters filters, int page, int count, Callback<GolfCourseSearchResults> callback) {
        Map<String, String> urlParams = new HashMap<>();
        ODataRequest request = new ODataRequest(TMAG_SEARCH_PATH);
        request.addNonOdataParameter("q", name);
        addFiltersToRequest(request, filters);
        request.addNonOdataParameter("page", String.valueOf(page));
        request.addNonOdataParameter("per_page", String.valueOf(count));
        HashMap<String, String> telemetryParams = new HashMap<>();
        telemetryParams.put("Type", TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME);
        telemetryParams.put(TelemetryConstants.TimedEvents.Cloud.Golf.COURSE_TYPE, filters.getCourseTypeFilter().name());
        telemetryParams.put(TelemetryConstants.TimedEvents.Cloud.Golf.HOLE_COUNT, filters.getHoleTypeFilter().toString());
        KRestQuery<GolfCourseSearchResults> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList("TMAG"), CUSTOM_GSON_DESERIALIZER, request.getUrl(), wrapCallback(callback), urlParams, new TypeToken<GolfCourseSearchResults>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.34
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getAvailableTMaGStates(int stateId, ActivityScopedCallback<GolfStateResponse> callback) {
        Map<String, String> urlParams = new HashMap<>();
        ODataRequest request = new ODataRequest(String.format(TMAG_LIST_STATES_PATH, Integer.valueOf(stateId)));
        KRestQuery<GolfStateResponse> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList("TMAG"), CUSTOM_GSON_DESERIALIZER, request.getUrl(), wrapCallback(callback), urlParams, new TypeToken<GolfStateResponse>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.35
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfCoursesByState(int stateId, int numberOfCourses, CourseFilters filters, ActivityScopedCallback<GolfCourseSearchResults> callback) {
        Map<String, String> urlParams = new HashMap<>();
        ODataRequest request = new ODataRequest(String.format(TMAG_LIST_COURSES_BY_STATE_PATH, Integer.valueOf(stateId)));
        request.addNonOdataParameter("page", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        request.addNonOdataParameter("per_page", String.valueOf(numberOfCourses));
        addFiltersToRequest(request, filters);
        KRestQuery<GolfCourseSearchResults> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList("TMAG"), CUSTOM_GSON_DESERIALIZER, request.getUrl(), wrapCallback(callback), urlParams, new TypeToken<GolfCourseSearchResults>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.36
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfCoursesByRegion(int regionId, CourseFilters filters, ActivityScopedCallback<GolfCourseSearchResults> callback) {
        Map<String, String> urlParams = new HashMap<>();
        ODataRequest request = new ODataRequest(String.format(TMAG_LIST_COURSES_BY_REGION_PATH, Integer.valueOf(regionId)));
        addFiltersToRequest(request, filters);
        KRestQuery<GolfCourseSearchResults> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList("TMAG"), CUSTOM_GSON_DESERIALIZER, request.getUrl(), wrapCallback(callback), urlParams, new TypeToken<GolfCourseSearchResults>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.37
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getAvailableTMaGRegions(ActivityScopedCallback<GolfRegionResponse> callback) {
        Map<String, String> urlParams = new HashMap<>();
        ODataRequest request = new ODataRequest(TMAG_LIST_REGIONS_PATH);
        KRestQuery<GolfRegionResponse> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList("TMAG"), CUSTOM_GSON_DESERIALIZER, request.getUrl(), wrapCallback(callback), urlParams, new TypeToken<GolfRegionResponse>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.38
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGoalTemplates(Callback<List<GoalTemplateDto>> callback) {
        KRestQuery<List<GoalTemplateDto>> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.GOALSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOALS_TEMPLATES, wrapCallback(callback), new HashMap(), new TypeToken<List<GoalTemplateDto>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.39
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void addGoal(GoalOperationAddDto operationAdd, Callback<GoalOperationResultDto> callback) {
        Validate.notNull(operationAdd, "operationAdd");
        Validate.notNull(callback, "callback");
        List<GoalOperationAddDto> operations = new ArrayList<>();
        operations.add(operationAdd);
        KRestQueryPost<List<GoalOperationAddDto>, List<GoalOperationResultDto>> query = new KRestQueryPost<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.GOALSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOALS, new HashMap(), operations, wrapCallback(new SingleItemCallback(callback)), new TypeToken<List<GoalOperationResultDto>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.40
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void addDevice(ConnectedDevice device) throws KRestException {
        Validate.notNull(device, "device");
        JsonObject deviceJson = new JsonObject();
        deviceJson.add(device.getUUID().toString(), CUSTOM_GSON_SERIALIZER.toJsonTree(device));
        String body = deviceJson.toString();
        HashMap<String, String> params = new HashMap<>();
        postRequest(KCloudConstants.ADD_DEVICE, params, body, Arrays.asList(CacheUtils.SYNCTAG));
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void updateGoal(GoalOperationUpdateDto operationUpdate, Callback<GoalOperationResultDto> callback) {
        Validate.notNull(operationUpdate, "operationUpdate");
        Validate.notNull(callback, "callback");
        List<GoalOperationUpdateDto> operations = new ArrayList<>();
        operations.add(operationUpdate);
        KRestQueryPut<List<GoalOperationUpdateDto>, List<GoalOperationResultDto>> query = new KRestQueryPut<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.GOALSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOALS_UPDATE, new HashMap(), operations, wrapCallback(new SingleItemCallback(callback)), new TypeToken<List<GoalOperationResultDto>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.41
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void deleteGoal(String goalId, Callback<Void> callback) {
        Validate.notNullOrEmpty(goalId, Constants.GOAL_ID);
        Validate.notNull(callback, "callback");
        Map<String, String> params = new HashMap<>();
        params.put("id", goalId);
        KRestQueryDelete<Void, Void> query = new KRestQueryDelete<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.GOALSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOAL, wrapCallback(callback), params, new TypeToken<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.42
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void deleteEvent(String eventId, Callback<Void> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(KCloudConstants.EVENT_ID, eventId);
        KRestQueryDelete<Void, Void> query = new KRestQueryDelete<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.EVENTSTAG, CacheUtils.GOALSTAG, CacheUtils.getEventTag(String.valueOf(eventId))), CUSTOM_GSON_DESERIALIZER, KCloudConstants.EVENT_METHOD_ID, wrapCallback(callback), params, new TypeToken<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.43
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGoals(Callback<List<GoalDto>> callback) {
        getGoals(callback, false);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGoals(Callback<List<GoalDto>> callback, boolean isHistory) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(KCloudConstants.IS_EXPAND, String.valueOf(isHistory));
        KRestQuery<List<GoalDto>> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.GOALSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOALS, wrapCallback(callback), urlParams, new TypeToken<List<GoalDto>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.44
        });
        query.setTreatNotFoundAsEmptyResult(true);
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getActiveCaloriesGoals(Callback<List<GoalDto>> callback, boolean isHistory) {
        getActiveGoalByType(callback, isHistory, CategoryType.PERSONALGOAL, GoalType.CALORIE);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getActiveStepsGoals(Callback<List<GoalDto>> callback, boolean isHistory) {
        getActiveGoalByType(callback, isHistory, CategoryType.PERSONALGOAL, GoalType.STEP);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getActiveGoalByType(Callback<List<GoalDto>> callback, boolean isHistory, CategoryType category, GoalType type) {
        getGoalByParameters(callback, GoalStatus.ACTIVE, isHistory, category, type);
    }

    public void getGoalByParameters(Callback<List<GoalDto>> callback, GoalStatus status, boolean isHistory, CategoryType category, GoalType type) {
        Validate.notNull(category, "GoalCategory");
        Validate.notNull(type, "GoalType");
        Validate.notNull(status, "GoalStatus");
        Validate.notNull(callback, "callback");
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("status", String.valueOf(status.value()));
        urlParams.put(KCloudConstants.IS_EXPAND, String.valueOf(isHistory));
        urlParams.put(KCloudConstants.CATEGORY_TYPE, String.valueOf(category.value()));
        urlParams.put("type", String.valueOf(type.value()));
        KRestQuery<List<GoalDto>> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.SYNCTAG, CacheUtils.GOALSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOALS_BY_PARAM, wrapCallback(callback), urlParams, new TypeToken<List<GoalDto>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.45
        });
        query.setTreatNotFoundAsEmptyResult(true);
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getWorkoutPlanSchedules(String workoutPlanId, int workoutInstanceId, Callback<List<ScheduledWorkout>> callback) {
        getWorkoutPlanSchedules(workoutPlanId, workoutInstanceId, LocaleProvider.getLocaleSettings(this.mContext).getLocaleName(), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getWorkoutPlanSchedules(String workoutPlanId, Callback<List<ScheduledWorkout>> callback) {
        getWorkoutPlanSchedules(workoutPlanId, -1, LocaleProvider.getLocaleSettings(this.mContext).getLocaleName(), callback);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getWorkoutPlanSchedules(String workoutPlanId, int workoutInstanceId, String locale, Callback<List<ScheduledWorkout>> callback) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("workoutPlanId", workoutPlanId);
        urlParams.put(KCloudConstants.LOCALE, locale);
        String cloudUrl = KCloudConstants.WORKOUT_DETAILS;
        if (workoutInstanceId == -1) {
            cloudUrl = KCloudConstants.WORKOUT_DETAILS_LATEST_INSTANCE;
        } else {
            urlParams.put(KCloudConstants.WORKOUT_INSTANCE_ID, Integer.toString(workoutInstanceId));
        }
        KRestQuery<List<ScheduledWorkout>> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.WORKOUTPLANTAG, CacheUtils.SYNCTAG), CUSTOM_GSON_DESERIALIZER, cloudUrl, wrapCallback(callback), urlParams, new TypeToken<List<ScheduledWorkout>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.46
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<ScheduledWorkout> getWorkoutPlanSchedules(String workoutPlanId, int workoutInstanceId, String locale) throws KRestException {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("workoutPlanId", workoutPlanId);
        urlParams.put(KCloudConstants.LOCALE, locale);
        String cloudUrl = KCloudConstants.WORKOUT_DETAILS;
        if (workoutInstanceId == -1) {
            cloudUrl = KCloudConstants.WORKOUT_DETAILS_LATEST_INSTANCE;
        } else {
            urlParams.put(KCloudConstants.WORKOUT_INSTANCE_ID, Integer.toString(workoutInstanceId));
        }
        String response = getText(cloudUrl, urlParams, Arrays.asList(CacheUtils.WORKOUTPLANTAG, CacheUtils.SYNCTAG));
        try {
            List<ScheduledWorkout> list = (List) CUSTOM_GSON_DESERIALIZER.fromJson(response, new TypeToken<List<ScheduledWorkout>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.47
            }.getType());
            QueryUtils.validateDeserializedObject(list);
            return list;
        } catch (JsonParseException exception) {
            throw new KRestException("Invalid JSON on workout response.", exception);
        } catch (Exception exception2) {
            throw new KRestException("Error during when fetching WorkoutPlanSchedules.", exception2);
        }
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<ScheduledWorkout> getWorkoutPlanSchedules(String workoutPlanId) throws KRestException {
        return getWorkoutPlanSchedules(workoutPlanId, -1, LocaleProvider.getLocaleSettings(this.mContext).getLocaleName());
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<ScheduledWorkout> getWorkoutPlanSchedules(String workoutPlanId, String locale) throws KRestException {
        return getWorkoutPlanSchedules(workoutPlanId, -1, locale);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<ScheduledWorkout> getWorkoutPlanSchedules(String workoutPlanId, int workoutInstanceId) throws KRestException {
        return getWorkoutPlanSchedules(workoutPlanId, workoutInstanceId, LocaleProvider.getLocaleSettings(this.mContext).getLocaleName());
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public byte[] getDeviceWorkout(String workoutPlanId, int workoutPlanInstanceId, int workoutIndex, int day, int week, String locale) throws KRestException {
        HashMap<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        params.put(KCloudConstants.WORKOUT_PLAN_INSTANCE_ID, Integer.toString(workoutPlanInstanceId));
        params.put(KCloudConstants.WORKOUT_INDEX, Integer.toString(workoutIndex));
        params.put(KCloudConstants.WORKOUT_DAY, Integer.toString(day));
        params.put(KCloudConstants.WORKOUT_WEEK, Integer.toString(week));
        params.put(KCloudConstants.LOCALE, locale);
        return getBinary(KCloudConstants.DEVICE_WORKOUT, params);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getDeviceWorkout(String workoutPlanId, int workoutPlanInstanceId, int workoutIndex, int day, int week, String locale, Callback<byte[]> callback) throws KRestException {
        HashMap<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        params.put(KCloudConstants.WORKOUT_PLAN_INSTANCE_ID, Integer.toString(workoutPlanInstanceId));
        params.put(KCloudConstants.WORKOUT_INDEX, Integer.toString(workoutIndex));
        params.put(KCloudConstants.WORKOUT_DAY, Integer.toString(day));
        params.put(KCloudConstants.WORKOUT_WEEK, Integer.toString(week));
        params.put(KCloudConstants.LOCALE, locale);
        KRestQueryBinary query = new KRestQueryBinary(this.mNetworkProvider, this.mCredentialsManager, KCloudConstants.DEVICE_WORKOUT, params, wrapCallback(callback));
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void updateLastSyncedWorkout(String workoutPlanId, int workoutPlanInstanceId, int workoutIndex, int day, int week) throws KRestException {
        HashMap<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        params.put(KCloudConstants.WORKOUT_PLAN_INSTANCE_ID, Integer.toString(workoutPlanInstanceId));
        params.put(KCloudConstants.WORKOUT_INDEX, Integer.toString(workoutIndex));
        params.put(KCloudConstants.WORKOUT_DAY, Integer.toString(day));
        params.put(KCloudConstants.WORKOUT_WEEK, Integer.toString(week));
        postRequest(KCloudConstants.LAST_SYNCED_WORKOUT, params, "", Arrays.asList(CacheUtils.LASTSYNCEDWORKOUT));
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void updateLastSyncedWorkoutManual(String workoutPlanId, int workoutIndex, int day, int week) throws KRestException {
        HashMap<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        params.put(KCloudConstants.WORKOUT_INDEX, Integer.toString(workoutIndex));
        params.put(KCloudConstants.WORKOUT_DAY, Integer.toString(day));
        params.put(KCloudConstants.WORKOUT_WEEK, Integer.toString(week));
        postRequest(KCloudConstants.NEXT_WORKOUT, params, "", Arrays.asList(CacheUtils.LASTSYNCEDWORKOUT));
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getLastSyncedWorkout(Callback<SyncedWorkoutInfo> callback) {
        KRestQuery<SyncedWorkoutInfo> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.LASTSYNCEDWORKOUT), CUSTOM_GSON_DESERIALIZER, KCloudConstants.LAST_SYNCED_WORKOUT_INFO, wrapCallback(callback), new HashMap(), new TypeToken<SyncedWorkoutInfo>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.48
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public SyncedWorkoutInfo getLastSyncedWorkout() throws KRestException {
        Map<String, String> urlParams = new HashMap<>();
        String response = getText(KCloudConstants.LAST_SYNCED_WORKOUT_INFO, urlParams, Arrays.asList(CacheUtils.LASTSYNCEDWORKOUT));
        try {
            SyncedWorkoutInfo syncedWorkoutInfo = (SyncedWorkoutInfo) CUSTOM_GSON_DESERIALIZER.fromJson(response, new TypeToken<SyncedWorkoutInfo>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.49
            }.getType());
            QueryUtils.validateDeserializedObject(syncedWorkoutInfo);
            return syncedWorkoutInfo;
        } catch (JsonParseException exception) {
            throw new KRestException("Invalid JSON on last Synced workout response.", exception);
        } catch (Exception exception2) {
            throw new KRestException("Error during when fetching LastSyncedWorkout.", exception2);
        }
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getFavoriteWorkoutPlans(Callback<List<FavoriteWorkoutPlan>> callback) {
        KRestQuery<List<FavoriteWorkoutPlan>> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG, CacheUtils.SYNCTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.WORKOUT_FAVORITES, wrapCallback(callback), new HashMap(), new TypeToken<List<FavoriteWorkoutPlan>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.50
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void addFavoriteWorkoutPlan(String workoutPlanId, Callback<Void> callback) {
        Validate.notNullOrEmpty(workoutPlanId, "workoutPlanId");
        Validate.notNull(callback, "callback");
        Map<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        KRestQueryPost<Void, Void> query = new KRestQueryPost<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG, CacheUtils.WORKOUTPLANTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.WORKOUT_ADD_FAVORITE, params, null, wrapCallback(callback), new TypeToken<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.51
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String addFavoriteWorkoutPlan(String workoutPlanId) throws KRestException {
        Validate.notNullOrEmpty(workoutPlanId, "workoutPlanId");
        HashMap<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        return postRequest(KCloudConstants.WORKOUT_ADD_FAVORITE, params, "", Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG, CacheUtils.WORKOUTPLANTAG));
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void removeFavoriteWorkoutPlan(String workoutPlanId, Callback<Void> callback) {
        Validate.notNullOrEmpty(workoutPlanId, "workoutPlanId");
        Validate.notNull(callback, "callback");
        Map<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        KRestQueryDelete<Void, Void> query = new KRestQueryDelete<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.WORKOUT_REMOVE_FAVORITE, wrapCallback(callback), params, new TypeToken<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.52
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String removeFavoriteWorkoutPlan(String workoutPlanId) throws KRestException {
        Validate.notNullOrEmpty(workoutPlanId, "workoutPlanId");
        HashMap<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        return deleteRequest(KCloudConstants.WORKOUT_REMOVE_FAVORITE, params, "", Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG));
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void subscribeToWorkoutPlan(String workoutPlanId, Callback<String[]> callback) {
        Validate.notNullOrEmpty(workoutPlanId, "workoutPlanId");
        Validate.notNull(callback, "callback");
        Map<String, String> params = new HashMap<>();
        List<String> workoutPlanIds = new ArrayList<>();
        workoutPlanIds.add(workoutPlanId);
        KRestQueryPut<List<String>, String[]> query = new KRestQueryPut<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG, CacheUtils.WORKOUTPLANTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.WORKOUT_SUBSCRIBE, params, workoutPlanIds, wrapCallback(callback), new TypeToken<String[]>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.53
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String subscribeToWorkoutPlan(String workoutPlanId) throws KRestException {
        Validate.notNullOrEmpty(workoutPlanId, "workoutPlanId");
        Map<String, String> params = new HashMap<>();
        List<String> workoutPlanIds = new ArrayList<>();
        workoutPlanIds.add(workoutPlanId);
        String body = CUSTOM_GSON_DESERIALIZER.toJson(workoutPlanIds);
        return putRequest(KCloudConstants.WORKOUT_SUBSCRIBE, params, body, Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG, CacheUtils.WORKOUTPLANTAG));
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void unsubscribeFromWorkoutPlan(String workoutPlanId, Callback<Void> callback) {
        Validate.notNullOrEmpty(workoutPlanId, "workoutPlanId");
        Validate.notNull(callback, "callback");
        Map<String, String> params = new HashMap<>();
        List<String> workoutPlanIds = new ArrayList<>();
        workoutPlanIds.add(workoutPlanId);
        KRestQueryPut<List<String>, Void> query = new KRestQueryPut<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG, CacheUtils.WORKOUTPLANTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.WORKOUT_UNSUBSCRIBE, params, workoutPlanIds, wrapCallback(callback), new TypeToken<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.54
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String unsubscribeFromWorkoutPlan(String workoutPlanId) throws KRestException {
        Validate.notNullOrEmpty(workoutPlanId, "workoutPlanId");
        Map<String, String> params = new HashMap<>();
        List<String> workoutPlanIds = new ArrayList<>();
        workoutPlanIds.add(workoutPlanId);
        String body = CUSTOM_GSON_DESERIALIZER.toJson(workoutPlanIds);
        return putRequest(KCloudConstants.WORKOUT_UNSUBSCRIBE, params, body, Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG, CacheUtils.WORKOUTPLANTAG));
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getSubscribedWorkoutPlan(final Callback<FavoriteWorkoutPlan> callback) {
        getFavoriteWorkoutPlans(new Callback<List<FavoriteWorkoutPlan>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.55
            @Override // com.microsoft.kapp.Callback
            public void callback(List<FavoriteWorkoutPlan> favoriteWorkoutPlans) {
                for (FavoriteWorkoutPlan favoriteWorkoutPlan : favoriteWorkoutPlans) {
                    if (favoriteWorkoutPlan != null && favoriteWorkoutPlan.isSubscribed()) {
                        callback.callback(favoriteWorkoutPlan);
                        return;
                    }
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void addGoals(ArrayList<GoalOperationAddDto> operations, Callback<GoalOperationResultDto> callback) {
        Validate.notNull(operations, "operationAdd");
        Validate.notNull(callback, "callback");
        KRestQueryPost<List<GoalOperationAddDto>, List<GoalOperationResultDto>> query = new KRestQueryPost<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.GOALSTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.GOALS, new HashMap(), operations, wrapCallback(new SingleItemCallback(callback)), new TypeToken<List<GoalOperationResultDto>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.56
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<FavoriteWorkoutPlan> getFavoriteWorkoutPlans() throws KRestException {
        Map<String, String> urlParams = new HashMap<>();
        String response = getText(KCloudConstants.WORKOUT_FAVORITES, urlParams, Arrays.asList(CacheUtils.WORKOUTFAVORITESTAG, CacheUtils.SYNCTAG));
        try {
            List<FavoriteWorkoutPlan> list = (List) CUSTOM_GSON_DESERIALIZER.fromJson(response, new TypeToken<List<FavoriteWorkoutPlan>>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.57
            }.getType());
            QueryUtils.validateDeserializedObject(list);
            return list;
        } catch (JsonParseException exception) {
            throw new KRestException("Invalid JSON on workout response.", exception);
        } catch (Exception exception2) {
            throw new KRestException("Error fetching FavoriteWorkoutPlans.", exception2);
        }
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public FavoriteWorkoutPlan getSubscribedWorkoutPlan() throws KRestException {
        List<FavoriteWorkoutPlan> favoriteWorkoutPlans = getFavoriteWorkoutPlans();
        for (FavoriteWorkoutPlan favoriteWorkoutPlan : favoriteWorkoutPlans) {
            if (favoriteWorkoutPlan != null && favoriteWorkoutPlan.isSubscribed()) {
                return favoriteWorkoutPlan;
            }
        }
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void skipWorkout(String workoutPlanId, int workoutIndex, int day, int week, Callback<Void> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("workoutPlanId", workoutPlanId);
        params.put(KCloudConstants.WORKOUT_INDEX, Integer.toString(workoutIndex));
        params.put(KCloudConstants.WORKOUT_DAY, Integer.toString(day));
        params.put(KCloudConstants.WORKOUT_WEEK, Integer.toString(week));
        KRestQueryPost<Void, Void> query = new KRestQueryPost<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.WORKOUTPLANTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.WORKOUT_SKIP, params, null, wrapCallback(callback), new TypeToken<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.58
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void nameEvent(String eventId, JsonObject nameOfEvent, Callback<Void> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(KCloudConstants.EVENT_ID, String.valueOf(eventId));
        KRestQueryPatch<JsonObject, Void> query = new KRestQueryPatch<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.EVENTSTAG, CacheUtils.getEventTag(String.valueOf(eventId))), CUSTOM_GSON_DESERIALIZER, KCloudConstants.EVENT_METHOD_ID, nameOfEvent, wrapCallback(callback), params, new TypeToken<Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.59
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String uploadSensorData(SensorData sensorData) throws KRestException {
        Validate.notNull(sensorData, "sensorData");
        HashMap<String, String> params = new HashMap<>();
        String body = CUSTOM_GSON_SERIALIZER.toJson(sensorData);
        return postRequest(KCloudConstants.SENSOR_UPLOAD, params, body, Arrays.asList(CacheUtils.SYNCTAG));
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfCourseDetail(String courseId, Callback<GolfCourse> callback) {
        Validate.notNull(callback, "callback");
        String restUrl = String.format(KCloudConstants.GOLF_COURSE_DETAILS, courseId);
        KRestQuery<GolfCourse> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.GOLFCOURSEDETAILS), CUSTOM_GSON_DESERIALIZER, restUrl, wrapCallback(callback), new TypeToken<GolfCourse>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.60
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getConnectedApps(Callback<Partners> callback) {
        Validate.notNull(callback, "callback");
        KRestQuery<Partners> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.CONNECTEDAPPS, CacheUtils.SYNCTAG), CUSTOM_GSON_DESERIALIZER, KCloudConstants.CONNECTED_APPS, wrapCallback(callback), new TypeToken<Partners>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.61
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public byte[] getDeviceGolfCourse(String courseId, String teeId) throws KRestException {
        Validate.notNullOrEmpty(courseId, "courseId");
        Validate.notNull(teeId, KCloudConstants.GOLF_COURSE_TEE_ID);
        String url = String.format(KCloudConstants.DEVICE_GOLF_COURSE, courseId);
        HashMap<String, String> params = new HashMap<>();
        params.put(KCloudConstants.GOLF_COURSE_TEE_ID, teeId);
        return getBinary(url, params);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void downloadAppConfiguration(AppConfigInfo appConfigInfo, final Callback<Void> callback) {
        Validate.notNull(appConfigInfo, "AppConfigInfo");
        final String url = appConfigInfo.getPrimaryURL();
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.62
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                try {
                    KRestServiceV1.this.downloadFile(url, Arrays.asList(CacheUtils.APPCONFIGTAG));
                    return null;
                } catch (Exception ex) {
                    Log.e(KRestServiceV1.TAG, "Exception while downloading app configuration file", ex);
                    String appConfigUrl = KRestServiceV1.this.mCredentialsManager.getCredentials().getFUSEndPoint() + KCloudConstants.APP_CONFIG_INFO;
                    KRestServiceV1.this.mCacheService.remove(appConfigUrl);
                    if (callback != null) {
                        callback.onError(ex);
                        return null;
                    }
                    return null;
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Void result) {
                Log.i(KRestServiceV1.TAG, String.format("Downloaded the app config file for url {0}", url));
                if (callback != null) {
                    callback.callback(result);
                }
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getAppConfigurationInfo(String fusEndPoint, Callback<AppConfigInfo> callback) {
        Validate.notNull(callback, "callback");
        Validate.notNullOrEmpty(fusEndPoint, "fusEndPoint");
        String appConfigUrl = fusEndPoint + KCloudConstants.APP_CONFIG_INFO;
        KRestQuery<AppConfigInfo> query = new KRestQuery<>(this.mNetworkProvider, this.mCredentialsManager, this.mCacheService, Arrays.asList(CacheUtils.APPCONFIGINFOTAG), CUSTOM_GSON_DESERIALIZER, appConfigUrl, wrapCallback(callback), new TypeToken<AppConfigInfo>() { // from class: com.microsoft.krestsdk.services.KRestServiceV1.63
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    private String getText(String restUrl, Map<String, String> urlParams, List<String> tags) throws KRestException {
        String response;
        try {
            Map<String, String> headers = new HashMap<>();
            KCredential credentials = this.mCredentialsManager.getCredentials();
            if (!KRestServiceUtils.addKRestQueryHeaders(headers, urlParams, credentials)) {
                throw new HttpResponseException((int) RestService.HTTP_ERROR_CODE_UNAUTHORIZE, "There are no credentials");
            }
            String requestUrl = KRestServiceUtils.populateUrl(restUrl, urlParams);
            String cachedResponse = null;
            if (this.mCacheService != null) {
                cachedResponse = this.mCacheService.getCachedResponse(requestUrl);
            }
            if (cachedResponse != null) {
                response = cachedResponse;
            } else {
                response = this.mNetworkProvider.executeHttpGet(requestUrl, headers);
            }
            if (this.mCacheService != null && cachedResponse == null) {
                this.mCacheService.put(requestUrl, response, tags);
            }
            return response;
        } catch (Exception exception) {
            KRestServiceUtils.logException(exception, "GET", null);
            throw new KRestException("Exception encountered calling service.", exception);
        }
    }

    private String postRequest(String restUrl, Map<String, String> urlParams, String body, List<String> tags) throws KRestException {
        return writeRequest(restUrl, urlParams, body, tags, 0);
    }

    private String putRequest(String restUrl, Map<String, String> urlParams, String body, List<String> tags) throws KRestException {
        return writeRequest(restUrl, urlParams, body, tags, 1);
    }

    private String deleteRequest(String restUrl, Map<String, String> urlParams, String body, List<String> tags) throws KRestException {
        return writeRequest(restUrl, urlParams, body, tags, 2);
    }

    private String writeRequest(String restUrl, Map<String, String> urlParams, String body, List<String> tags, int requestType) throws KRestException {
        String response = null;
        Map<String, String> headers = new HashMap<>();
        try {
            headers.put("Content-Type", "application/json");
            headers.put(KCloudConstants.ENCODING_TYPE, "UTF-8");
            KCredential credentials = this.mCredentialsManager.getCredentials();
            if (!KRestServiceUtils.addKRestQueryHeaders(headers, urlParams, credentials)) {
                throw new HttpResponseException((int) RestService.HTTP_ERROR_CODE_UNAUTHORIZE, "There are no credentials");
            }
            String requestUrl = KRestServiceUtils.populateUrl(restUrl, urlParams);
            switch (requestType) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                default:
                    KLog.logPrivate(TAG, "Unknown Request! This Rest-request should be either POST, PUT, or DELETE! [Request URL]:  " + requestUrl);
                    return null;
            }
            String guid = UUID.randomUUID().toString();
            headers.put(TelemetryConstants.TimedEvents.Cloud.CLOUD_CALL_UUID, guid);
            switch (requestType) {
                case 0:
                    response = this.mNetworkProvider.executeHttpPost(requestUrl, headers, body);
                    break;
                case 1:
                    response = this.mNetworkProvider.executeHttpPut(requestUrl, headers, body);
                    break;
                case 2:
                    response = this.mNetworkProvider.executeHttpDelete(requestUrl, headers);
                    break;
                default:
                    KLog.logPrivate(TAG, "Unknown Request! This Rest-request should be either POST, PUT, or DELETE! [Request URL]:  " + requestUrl);
                    break;
            }
            if (this.mCacheService != null) {
                this.mCacheService.removeForTags(tags);
            }
            KLog.logPrivate(TAG, "[Request URL]:  " + requestUrl + " [Response]: " + response);
            return response;
        } catch (Exception exception) {
            KRestServiceUtils.logException(exception, null, null);
            throw new KRestException("Exception encountered calling service.", exception);
        }
    }

    private byte[] getBinary(String restUrl, Map<String, String> urlParams) throws KRestException {
        try {
            Map<String, String> headers = new HashMap<>();
            KCredential credentials = this.mCredentialsManager.getCredentials();
            if (!KRestServiceUtils.addKRestQueryHeaders(headers, urlParams, credentials)) {
                throw new HttpResponseException((int) RestService.HTTP_ERROR_CODE_UNAUTHORIZE, "There are no credentials");
            }
            return this.mNetworkProvider.executeHttpGetBinary(KRestServiceUtils.populateUrl(restUrl, urlParams), headers);
        } catch (Exception exception) {
            KRestServiceUtils.logException(exception, "GET", null);
            throw new KRestException("Exception encountered calling service.", exception);
        }
    }

    private <T> NetworkCallback<T> wrapCallback(Callback<T> callback) {
        return new NetworkCallback<>(this.mContext, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class NetworkCallback<T> implements Callback<T> {
        private Callback<T> mCallback;
        private WeakReference<Context> mWeakContext;

        public NetworkCallback(Context context, Callback<T> callback) {
            this.mWeakContext = new WeakReference<>(context);
            this.mCallback = callback;
        }

        @Override // com.microsoft.kapp.Callback
        public void callback(T result) {
            if (this.mCallback != null) {
                this.mCallback.callback(result);
            }
        }

        @Override // com.microsoft.kapp.Callback
        public void onError(Exception ex) {
            Context context;
            if (this.mCallback != null) {
                if (ex instanceof HttpResponseException) {
                    HttpResponseException httpException = (HttpResponseException) ex;
                    int errCode = httpException.getStatusCode();
                    if (errCode == 401 && (context = this.mWeakContext.get()) != null) {
                        Intent signinIntent = new Intent(RestService.SIGN_IN_REQUIRED_INTENT);
                        context.sendBroadcast(signinIntent);
                    }
                }
                this.mCallback.onError(ex);
            }
        }
    }

    private String getExpandParams(ArrayList<RestService.ExpandType> expandTypes) {
        StringBuilder sb = new StringBuilder();
        if (expandTypes != null && !expandTypes.isEmpty()) {
            String seperator = "";
            Iterator i$ = expandTypes.iterator();
            while (i$.hasNext()) {
                RestService.ExpandType type = i$.next();
                sb.append(seperator);
                sb.append(type.name);
                seperator = ",";
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadFile(String url, List<String> tags) throws MalformedURLException, IOException {
        Map<String, String> headers = new HashMap<>();
        String guid = UUID.randomUUID().toString();
        headers.put(TelemetryConstants.TimedEvents.Cloud.CLOUD_CALL_UUID, guid);
        this.mNetworkProvider.executeHttpGetAndWriteToCache(url, headers, this.mCacheService, tags);
    }
}
