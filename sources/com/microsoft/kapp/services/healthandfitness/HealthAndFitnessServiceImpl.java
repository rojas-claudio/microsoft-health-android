package com.microsoft.kapp.services.healthandfitness;

import android.content.Context;
import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.cache.CacheUtils;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GsonUtils;
import com.microsoft.kapp.utils.LocaleProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.KRestServiceUtils;
import com.microsoft.krestsdk.services.NetworkProvider;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes.dex */
public class HealthAndFitnessServiceImpl implements HealthAndFitnessService {
    private static final String BASE_URL = "http://kint-appexhealthandfitness-wus.cloudapp.net/FeedsIndexWindowsService.svc";
    private static final String HTTPS_PREFIX = "https:";
    private static final String HnF_RESOURCE = "FeedsIndexWindowsService.svc/ViewFiltered?query=&cluster=fitness&scenario=workout&gender=&types=&goals=&filter=&onlycardio=&version=V3&market=%s&count=238&";
    private static final String HnF_RESOURCE_STRENGTH = "FeedsIndexWindowsService.svc/ViewFiltered?query=&cluster=fitness&scenario=workout&gender=&types=Strength&goals=&filter=&onlycardio=&version=V3&market=%s&count=238&";
    private static final String HnF_RESOURCE_STRENGTH_ALL = "FeedsIndexWindowsService.svc/ViewFiltered?query=&cluster=fitness&scenario=&gender=&types=Strength&goals=&filter=&onlycardio=&version=V3&market=%s&count=238&";
    private static final String HnF_RESOURCE_STRENGTH_Filter = "FeedsIndexWindowsService.svc/ViewFiltered?query=%s&PublishedBy=%s&cluster=fitness&scenario=workout&gender=&types=Strength&goals=&filter=%s&onlycardio=&version=V3&market=%s&count=238&";
    private static final String STRENGTH_WORKOUTS_PATH_FORMAT = "/ViewFiltered?query=&cluster=fitness&scenario=workout&gender=&types=Strength&goals=&filter=&onlycardio=&version=V3&market=%s&count=238&Client-AppVersion=3.0.2.258&version=KINT1&isKClient=1";
    private static final String WORKOUTS_PATH_FORMAT = "/ViewFiltered?query=&cluster=fitness&scenario=workout&gender=&types=&goals=&filter=&onlycardio=&version=V3&market=%s&count=238&Client-AppVersion=3.0.2.258&version=KINT1&isKClient=1";
    private static final String WorkoutDetailsURI = "FitnessPhoneService.svc/GetWorkoutDetails/%1$s?market=%2$s&";
    private CacheService mCacheService;
    private Context mContext;
    private CredentialsManager mCredentialsManager;
    private NetworkProvider mNetworkProvider;
    private static String HnFEndPoint = "http://kint-appexhealthandfitness-wus.cloudapp.net/";
    private static String HnFQueryParameters = "version=KINT1&isKClient=1&Client-AppVersion=3.0.2.258";
    private static final String TAG = HealthAndFitnessServiceImpl.class.getSimpleName();
    private static final Gson CUSTOM_GSON_DESERIALIZER = GsonUtils.getCustomDeserializer();

    public HealthAndFitnessServiceImpl(NetworkProvider provider, CredentialsManager credentialsManager, CacheService cacheService, Context context) {
        this.mNetworkProvider = provider;
        setHnFEndPoint(credentialsManager);
        setHnFQueryParameters(credentialsManager);
        this.mCacheService = cacheService;
        this.mCredentialsManager = credentialsManager;
        this.mContext = context;
    }

    @Override // com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService
    public void getHnFWorkoutPlans(Callback<WorkoutsResponseEnvelope> callback) {
        String marketingString = LocaleProvider.getLocaleSettings(this.mContext).getLocaleName();
        String restUrl = String.format(Locale.US, "%s%s%s", HnFEndPoint, String.format(Locale.US, HnF_RESOURCE, marketingString), HnFQueryParameters);
        RestQuery<WorkoutsResponseEnvelope> query = new RestQuery<>(restUrl, Arrays.asList(CacheUtils.HNFWORKOUTLISTTAG), new TypeToken<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.services.healthandfitness.HealthAndFitnessServiceImpl.1
        }, callback);
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService
    public void getHnFStrengthWorkoutPlans(WorkoutProviderType providerType, Callback<WorkoutsResponseEnvelope> callback) {
        getHnFStrengthWorkoutPlans("", "", providerType, callback);
    }

    @Override // com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService
    public void getHnFStrengthWorkoutPlans(String filterString, WorkoutProviderType providerType, Callback<WorkoutsResponseEnvelope> callback) {
        getHnFStrengthWorkoutPlans("", filterString, providerType, callback);
    }

    @Override // com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService
    public void getHnFStrengthWorkoutPlans(String workoutName, String filterString, WorkoutProviderType providerType, Callback<WorkoutsResponseEnvelope> callback) {
        String name;
        String marketingString = LocaleProvider.getLocaleSettings(this.mContext).getLocaleName();
        try {
            name = URLEncoder.encode(workoutName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            KLog.e(TAG, "Error encoding %s", workoutName);
            name = workoutName;
        }
        String restUrl = String.format("%s%s%s", HnFEndPoint, String.format(Locale.US, HnF_RESOURCE_STRENGTH_Filter, name, providerType.toString(), filterString, marketingString), HnFQueryParameters);
        List<String> tags = null;
        switch (providerType) {
            case Me:
                tags = Arrays.asList(CacheUtils.HNFCUSTOMWORKOUTLISTTAG, CacheUtils.SYNCTAG);
                break;
            case Provider:
                tags = Arrays.asList(CacheUtils.HNFWORKOUTLISTTAG);
                break;
            case All:
                tags = Arrays.asList(CacheUtils.HNFCUSTOMWORKOUTLISTTAG, CacheUtils.SYNCTAG, CacheUtils.HNFWORKOUTLISTTAG);
                break;
        }
        RestQuery<WorkoutsResponseEnvelope> query = new RestQuery<>(restUrl, tags, new TypeToken<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.services.healthandfitness.HealthAndFitnessServiceImpl.2
        }, callback);
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService
    public void getHnFStrengthExerciseAndWorkoutPlans(Callback<WorkoutsResponseEnvelope> callback) {
        String marketingString = LocaleProvider.getLocaleSettings(this.mContext).getLocaleName();
        String restUrl = String.format("%s%s%s", HnFEndPoint, String.format(Locale.US, HnF_RESOURCE_STRENGTH_ALL, marketingString), HnFQueryParameters);
        RestQuery<WorkoutsResponseEnvelope> query = new RestQuery<>(restUrl, Arrays.asList(CacheUtils.HNFWORKOUTLISTTAG), new TypeToken<WorkoutsResponseEnvelope>() { // from class: com.microsoft.kapp.services.healthandfitness.HealthAndFitnessServiceImpl.3
        }, callback);
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService
    public void getHnFWorkoutPlanDetails(String workoutId, Callback<WorkoutPlan> callback) {
        String marketingString = LocaleProvider.getLocaleSettings(this.mContext).getLocaleName();
        String restUrl = String.format("%s%s%s", HnFEndPoint, String.format(WorkoutDetailsURI, workoutId, marketingString), HnFQueryParameters);
        RestQuery<WorkoutPlan> query = new RestQuery<>(restUrl, Arrays.asList(CacheUtils.HNFWORKOUTTAG), new TypeToken<WorkoutPlan>() { // from class: com.microsoft.kapp.services.healthandfitness.HealthAndFitnessServiceImpl.4
        }, callback);
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    @Override // com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService
    public WorkoutPlan getHnFWorkoutPlanDetails(String workoutId) throws KRestException {
        String marketingString = LocaleProvider.getLocaleSettings(this.mContext).getLocaleName();
        String restUrl = String.format("%s%s%s", HnFEndPoint, String.format(WorkoutDetailsURI, workoutId, marketingString), HnFQueryParameters);
        String response = getText(restUrl, Arrays.asList(CacheUtils.HNFWORKOUTTAG));
        try {
            return (WorkoutPlan) CUSTOM_GSON_DESERIALIZER.fromJson(response, new TypeToken<WorkoutPlan>() { // from class: com.microsoft.kapp.services.healthandfitness.HealthAndFitnessServiceImpl.5
            }.getType());
        } catch (JsonParseException exception) {
            throw new KRestException("Invalid JSON on HnF workoutPlan response.", exception);
        }
    }

    private String getText(String restUrl, List<String> tags) throws KRestException {
        String cachedResponse = null;
        try {
            if (this.mCacheService != null && this.mCacheService.isResponseCached(restUrl)) {
                cachedResponse = this.mCacheService.getCachedResponse(restUrl);
            }
            Map<String, String> headers = new HashMap<>();
            if (restUrl != null && restUrl.toLowerCase(Locale.US).contains(HTTPS_PREFIX)) {
                headers.put(Constants.AUTHORIZATION_HEADER_NAME, KRestServiceUtils.wrapToken(this.mCredentialsManager.getCredentials().getAccessToken()));
            }
            String response = cachedResponse != null ? cachedResponse : this.mNetworkProvider.executeHttpGet(restUrl, headers);
            if (this.mCacheService != null && cachedResponse == null) {
                this.mCacheService.put(restUrl, response, tags);
            }
            KLog.logPrivate(TAG, "[Request URL]:  " + restUrl + " [Response]: " + response);
            return response;
        } catch (Exception ex) {
            KRestServiceUtils.logException(ex, "GET", restUrl);
            throw new KRestException("Exception encountered calling service.", ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RestQuery<T> extends AsyncTask<String, Void, T> {
        private Callback<T> mCallback;
        private Exception mException;
        private String mRestUrl;
        private List<String> mTags;
        private Type mType;

        public RestQuery(String restUrl, List<String> tags, TypeToken<T> token, Callback<T> callback) {
            this.mRestUrl = restUrl;
            this.mTags = tags;
            this.mCallback = callback;
            this.mType = token.getType();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public T doInBackground(String... strings) {
            String requestUrl = null;
            String response = null;
            T parsedResponse = null;
            try {
                requestUrl = this.mRestUrl;
                Map<String, String> headers = new HashMap<>();
                String cachedResponse = null;
                if (HealthAndFitnessServiceImpl.this.mCacheService != null && HealthAndFitnessServiceImpl.this.mCacheService.isResponseCached(requestUrl)) {
                    cachedResponse = HealthAndFitnessServiceImpl.this.mCacheService.getCachedResponse(requestUrl);
                }
                if (cachedResponse != null) {
                    response = cachedResponse;
                } else {
                    if (requestUrl != null && requestUrl.toLowerCase(Locale.US).contains(HealthAndFitnessServiceImpl.HTTPS_PREFIX)) {
                        headers.put(Constants.AUTHORIZATION_HEADER_NAME, KRestServiceUtils.wrapToken(HealthAndFitnessServiceImpl.this.mCredentialsManager.getCredentials().getAccessToken()));
                    }
                    response = HealthAndFitnessServiceImpl.this.mNetworkProvider.executeHttpGet(requestUrl, headers);
                }
                if (HealthAndFitnessServiceImpl.this.mCacheService != null && cachedResponse == null) {
                    HealthAndFitnessServiceImpl.this.mCacheService.put(requestUrl, response, this.mTags);
                }
                parsedResponse = (T) HealthAndFitnessServiceImpl.CUSTOM_GSON_DESERIALIZER.fromJson(response, this.mType);
            } catch (Exception e) {
                KRestServiceUtils.logException(e, "GET", requestUrl);
                this.mException = e;
            }
            KLog.logPrivate(HealthAndFitnessServiceImpl.TAG, "[Request URL]:  " + requestUrl + " [Response]: " + response);
            return parsedResponse;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(T result) {
            if (this.mException == null) {
                this.mCallback.callback(result);
            } else {
                this.mCallback.onError(this.mException);
            }
        }
    }

    private void setHnFEndPoint(final CredentialsManager credentialsManager) {
        if (credentialsManager != null) {
            new Thread(new Runnable() { // from class: com.microsoft.kapp.services.healthandfitness.HealthAndFitnessServiceImpl.6
                @Override // java.lang.Runnable
                public void run() {
                    KCredential credentials = credentialsManager.getCredentials();
                    if (credentials != null && credentials.getHnFEndPoint() != null) {
                        String unused = HealthAndFitnessServiceImpl.HnFEndPoint = credentials.getHnFEndPoint();
                    }
                }
            }).start();
        }
    }

    private void setHnFQueryParameters(final CredentialsManager credentialsManager) {
        if (credentialsManager != null) {
            new Thread(new Runnable() { // from class: com.microsoft.kapp.services.healthandfitness.HealthAndFitnessServiceImpl.7
                @Override // java.lang.Runnable
                public void run() {
                    KCredential credentials = credentialsManager.getCredentials();
                    if (credentials != null && credentials.getHnFQueryParameters() != null) {
                        String unused = HealthAndFitnessServiceImpl.HnFQueryParameters = credentials.getHnFQueryParameters();
                    }
                }
            }).start();
        }
    }
}
