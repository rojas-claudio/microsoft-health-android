package com.facebook.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Settings;
import com.facebook.model.GraphObject;
import com.j256.ormlite.stmt.query.SimpleComparison;
import com.microsoft.kapp.utils.Constants;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
/* loaded from: classes.dex */
public final class Utility {
    private static final String APPLICATION_FIELDS = "fields";
    private static final String APP_SETTINGS_PREFS_KEY_FORMAT = "com.facebook.internal.APP_SETTINGS.%s";
    private static final String APP_SETTINGS_PREFS_STORE = "com.facebook.internal.preferences.APP_SETTINGS";
    public static final int DEFAULT_STREAM_BUFFER_SIZE = 8192;
    private static final String DIALOG_CONFIG_DIALOG_NAME_FEATURE_NAME_SEPARATOR = "\\|";
    private static final String DIALOG_CONFIG_NAME_KEY = "name";
    private static final String DIALOG_CONFIG_URL_KEY = "url";
    private static final String DIALOG_CONFIG_VERSIONS_KEY = "versions";
    private static final String EXTRA_APP_EVENTS_INFO_FORMAT_VERSION = "a1";
    private static final String HASH_ALGORITHM_MD5 = "MD5";
    private static final String HASH_ALGORITHM_SHA1 = "SHA-1";
    static final String LOG_TAG = "FacebookSDK";
    private static final String URL_SCHEME = "https";
    private static final String UTF8 = "UTF-8";
    private static AsyncTask<Void, Void, GraphObject> initialAppSettingsLoadTask;
    private static final String APP_SETTING_SUPPORTS_ATTRIBUTION = "supports_attribution";
    private static final String APP_SETTING_SUPPORTS_IMPLICIT_SDK_LOGGING = "supports_implicit_sdk_logging";
    private static final String APP_SETTING_NUX_CONTENT = "gdpv4_nux_content";
    private static final String APP_SETTING_NUX_ENABLED = "gdpv4_nux_enabled";
    private static final String APP_SETTING_DIALOG_CONFIGS = "android_dialog_configs";
    private static final String[] APP_SETTING_FIELDS = {APP_SETTING_SUPPORTS_ATTRIBUTION, APP_SETTING_SUPPORTS_IMPLICIT_SDK_LOGGING, APP_SETTING_NUX_CONTENT, APP_SETTING_NUX_ENABLED, APP_SETTING_DIALOG_CONFIGS};
    private static Map<String, FetchedAppSettings> fetchedAppSettings = new ConcurrentHashMap();

    /* loaded from: classes.dex */
    public static class FetchedAppSettings {
        private Map<String, Map<String, DialogFeatureConfig>> dialogConfigMap;
        private String nuxContent;
        private boolean nuxEnabled;
        private boolean supportsAttribution;
        private boolean supportsImplicitLogging;

        private FetchedAppSettings(boolean supportsAttribution, boolean supportsImplicitLogging, String nuxContent, boolean nuxEnabled, Map<String, Map<String, DialogFeatureConfig>> dialogConfigMap) {
            this.supportsAttribution = supportsAttribution;
            this.supportsImplicitLogging = supportsImplicitLogging;
            this.nuxContent = nuxContent;
            this.nuxEnabled = nuxEnabled;
            this.dialogConfigMap = dialogConfigMap;
        }

        /* synthetic */ FetchedAppSettings(boolean z, boolean z2, String str, boolean z3, Map map, FetchedAppSettings fetchedAppSettings) {
            this(z, z2, str, z3, map);
        }

        public boolean supportsAttribution() {
            return this.supportsAttribution;
        }

        public boolean supportsImplicitLogging() {
            return this.supportsImplicitLogging;
        }

        public String getNuxContent() {
            return this.nuxContent;
        }

        public boolean getNuxEnabled() {
            return this.nuxEnabled;
        }

        public Map<String, Map<String, DialogFeatureConfig>> getDialogConfigurations() {
            return this.dialogConfigMap;
        }
    }

    /* loaded from: classes.dex */
    public static class DialogFeatureConfig {
        private String dialogName;
        private Uri fallbackUrl;
        private String featureName;
        private int[] featureVersionSpec;

        /* JADX INFO: Access modifiers changed from: private */
        public static DialogFeatureConfig parseDialogConfig(JSONObject dialogConfigJSON) {
            String dialogNameWithFeature = dialogConfigJSON.optString("name");
            if (Utility.isNullOrEmpty(dialogNameWithFeature)) {
                return null;
            }
            String[] components = dialogNameWithFeature.split(Utility.DIALOG_CONFIG_DIALOG_NAME_FEATURE_NAME_SEPARATOR);
            if (components.length == 2) {
                String dialogName = components[0];
                String featureName = components[1];
                if (Utility.isNullOrEmpty(dialogName) || Utility.isNullOrEmpty(featureName)) {
                    return null;
                }
                String urlString = dialogConfigJSON.optString("url");
                Uri fallbackUri = null;
                if (!Utility.isNullOrEmpty(urlString)) {
                    fallbackUri = Uri.parse(urlString);
                }
                JSONArray versionsJSON = dialogConfigJSON.optJSONArray(Utility.DIALOG_CONFIG_VERSIONS_KEY);
                int[] featureVersionSpec = parseVersionSpec(versionsJSON);
                return new DialogFeatureConfig(dialogName, featureName, fallbackUri, featureVersionSpec);
            }
            return null;
        }

        private static int[] parseVersionSpec(JSONArray versionsJSON) {
            int[] versionSpec = null;
            if (versionsJSON != null) {
                int numVersions = versionsJSON.length();
                versionSpec = new int[numVersions];
                for (int i = 0; i < numVersions; i++) {
                    int version = versionsJSON.optInt(i, -1);
                    if (version == -1) {
                        String versionString = versionsJSON.optString(i);
                        if (!Utility.isNullOrEmpty(versionString)) {
                            try {
                                version = Integer.parseInt(versionString);
                            } catch (NumberFormatException nfe) {
                                Utility.logd(Utility.LOG_TAG, nfe);
                                version = -1;
                            }
                        }
                    }
                    versionSpec[i] = version;
                }
            }
            return versionSpec;
        }

        private DialogFeatureConfig(String dialogName, String featureName, Uri fallbackUrl, int[] featureVersionSpec) {
            this.dialogName = dialogName;
            this.featureName = featureName;
            this.fallbackUrl = fallbackUrl;
            this.featureVersionSpec = featureVersionSpec;
        }

        public String getDialogName() {
            return this.dialogName;
        }

        public String getFeatureName() {
            return this.featureName;
        }

        public Uri getFallbackUrl() {
            return this.fallbackUrl;
        }

        public int[] getVersionSpec() {
            return this.featureVersionSpec;
        }
    }

    public static int[] intersectRanges(int[] range1, int[] range2) {
        if (range1 != null) {
            if (range2 == null) {
                return range1;
            }
            int[] outputRange = new int[range1.length + range2.length];
            int outputIndex = 0;
            int index1 = 0;
            int index2 = 0;
            while (true) {
                if (index1 >= range1.length || index2 >= range2.length) {
                    break;
                }
                int newRangeLower = Integer.MIN_VALUE;
                int newRangeUpper = Integer.MAX_VALUE;
                int lower1 = range1[index1];
                int upper1 = Integer.MAX_VALUE;
                int lower2 = range2[index2];
                int upper2 = Integer.MAX_VALUE;
                if (index1 < range1.length - 1) {
                    upper1 = range1[index1 + 1];
                }
                if (index2 < range2.length - 1) {
                    upper2 = range2[index2 + 1];
                }
                if (lower1 < lower2) {
                    if (upper1 > lower2) {
                        newRangeLower = lower2;
                        if (upper1 > upper2) {
                            newRangeUpper = upper2;
                            index2 += 2;
                        } else {
                            newRangeUpper = upper1;
                            index1 += 2;
                        }
                    } else {
                        index1 += 2;
                    }
                } else if (upper2 > lower1) {
                    newRangeLower = lower1;
                    if (upper2 > upper1) {
                        newRangeUpper = upper1;
                        index1 += 2;
                    } else {
                        newRangeUpper = upper2;
                        index2 += 2;
                    }
                } else {
                    index2 += 2;
                }
                if (newRangeLower != Integer.MIN_VALUE) {
                    int outputIndex2 = outputIndex + 1;
                    outputRange[outputIndex] = newRangeLower;
                    if (newRangeUpper == Integer.MAX_VALUE) {
                        outputIndex = outputIndex2;
                        break;
                    }
                    outputIndex = outputIndex2 + 1;
                    outputRange[outputIndex2] = newRangeUpper;
                }
            }
            return Arrays.copyOf(outputRange, outputIndex);
        }
        return range2;
    }

    public static <T> boolean isSubset(Collection<T> subset, Collection<T> superset) {
        if (superset == null || superset.size() == 0) {
            return subset == null || subset.size() == 0;
        }
        HashSet<T> hash = new HashSet<>((Collection<? extends T>) superset);
        for (T t : subset) {
            if (!hash.contains(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return c == null || c.size() == 0;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String coerceValueIfNullOrEmpty(String s, String valueIfNullOrEmpty) {
        return isNullOrEmpty(s) ? valueIfNullOrEmpty : s;
    }

    public static <T> Collection<T> unmodifiableCollection(T... tArr) {
        return Collections.unmodifiableCollection(Arrays.asList(tArr));
    }

    public static <T> ArrayList<T> arrayList(T... tArr) {
        ArrayList<T> arrayList = new ArrayList<>(tArr.length);
        for (T t : tArr) {
            arrayList.add(t);
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String md5hash(String key) {
        return hashWithAlgorithm(HASH_ALGORITHM_MD5, key);
    }

    static String sha1hash(String key) {
        return hashWithAlgorithm(HASH_ALGORITHM_SHA1, key);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String sha1hash(byte[] bytes) {
        return hashWithAlgorithm(HASH_ALGORITHM_SHA1, bytes);
    }

    private static String hashWithAlgorithm(String algorithm, String key) {
        return hashWithAlgorithm(algorithm, key.getBytes());
    }

    private static String hashWithAlgorithm(String algorithm, byte[] bytes) {
        try {
            MessageDigest hash = MessageDigest.getInstance(algorithm);
            return hashBytes(hash, bytes);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String hashBytes(MessageDigest hash, byte[] bytes) {
        hash.update(bytes);
        byte[] digest = hash.digest();
        StringBuilder builder = new StringBuilder();
        for (int b : digest) {
            builder.append(Integer.toHexString((b >> 4) & 15));
            builder.append(Integer.toHexString((b >> 0) & 15));
        }
        return builder.toString();
    }

    public static Uri buildUri(String authority, String path, Bundle parameters) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(URL_SCHEME);
        builder.authority(authority);
        builder.path(path);
        for (String key : parameters.keySet()) {
            Object parameter = parameters.get(key);
            if (parameter instanceof String) {
                builder.appendQueryParameter(key, (String) parameter);
            }
        }
        return builder.build();
    }

    public static Bundle parseUrlQueryString(String queryString) {
        Bundle params = new Bundle();
        if (!isNullOrEmpty(queryString)) {
            String[] array = queryString.split("&");
            for (String parameter : array) {
                String[] keyValuePair = parameter.split(SimpleComparison.EQUAL_TO_OPERATION);
                try {
                    if (keyValuePair.length == 2) {
                        params.putString(URLDecoder.decode(keyValuePair[0], "UTF-8"), URLDecoder.decode(keyValuePair[1], "UTF-8"));
                    } else if (keyValuePair.length == 1) {
                        params.putString(URLDecoder.decode(keyValuePair[0], "UTF-8"), "");
                    }
                } catch (UnsupportedEncodingException e) {
                    logd(LOG_TAG, e);
                }
            }
        }
        return params;
    }

    public static void putObjectInBundle(Bundle bundle, String key, Object value) {
        if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else {
            throw new FacebookException("attempted to add unsupported type to Bundle");
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void disconnectQuietly(URLConnection connection) {
        if (connection instanceof HttpURLConnection) {
            ((HttpURLConnection) connection).disconnect();
        }
    }

    public static String getMetadataApplicationId(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Settings.loadDefaultsFromMetadata(context);
        return Settings.getApplicationId();
    }

    static Map<String, Object> convertJSONObjectToHashMap(JSONObject jsonObject) {
        HashMap<String, Object> map = new HashMap<>();
        JSONArray keys = jsonObject.names();
        for (int i = 0; i < keys.length(); i++) {
            try {
                String key = keys.getString(i);
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    value = convertJSONObjectToHashMap((JSONObject) value);
                }
                map.put(key, value);
            } catch (JSONException e) {
            }
        }
        return map;
    }

    public static Object getStringPropertyAsJSON(JSONObject jsonObject, String key, String nonJSONPropertyKey) throws JSONException {
        Object value = jsonObject.opt(key);
        if (value != null && (value instanceof String)) {
            JSONTokener tokener = new JSONTokener((String) value);
            value = tokener.nextValue();
        }
        if (value == null || (value instanceof JSONObject) || (value instanceof JSONArray)) {
            return value;
        }
        if (nonJSONPropertyKey != null) {
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.putOpt(nonJSONPropertyKey, value);
            return jsonObject2;
        }
        throw new FacebookException("Got an unexpected non-JSON object.");
    }

    public static String readStreamToString(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream;
        InputStreamReader reader;
        BufferedInputStream bufferedInputStream2 = null;
        InputStreamReader reader2 = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            try {
                reader = new InputStreamReader(bufferedInputStream);
            } catch (Throwable th) {
                th = th;
                bufferedInputStream2 = bufferedInputStream;
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            StringBuilder stringBuilder = new StringBuilder();
            char[] buffer = new char[2048];
            while (true) {
                int n = reader.read(buffer);
                if (n != -1) {
                    stringBuilder.append(buffer, 0, n);
                } else {
                    String sb = stringBuilder.toString();
                    closeQuietly(bufferedInputStream);
                    closeQuietly(reader);
                    return sb;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            reader2 = reader;
            bufferedInputStream2 = bufferedInputStream;
            closeQuietly(bufferedInputStream2);
            closeQuietly(reader2);
            throw th;
        }
    }

    public static boolean stringsEqualOrEmpty(String a, String b) {
        boolean aEmpty = TextUtils.isEmpty(a);
        boolean bEmpty = TextUtils.isEmpty(b);
        if (aEmpty && bEmpty) {
            return true;
        }
        if (!aEmpty && !bEmpty) {
            return a.equals(b);
        }
        return false;
    }

    private static void clearCookiesForDomain(Context context, String domain) {
        CookieSyncManager syncManager = CookieSyncManager.createInstance(context);
        syncManager.sync();
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(domain);
        if (cookies != null) {
            String[] splitCookies = cookies.split(";");
            for (String cookie : splitCookies) {
                String[] cookieParts = cookie.split(SimpleComparison.EQUAL_TO_OPERATION);
                if (cookieParts.length > 0) {
                    String newCookie = String.valueOf(cookieParts[0].trim()) + "=;expires=Sat, 1 Jan 2000 00:00:01 UTC;";
                    cookieManager.setCookie(domain, newCookie);
                }
            }
            cookieManager.removeExpiredCookie();
        }
    }

    public static void clearFacebookCookies(Context context) {
        clearCookiesForDomain(context, "facebook.com");
        clearCookiesForDomain(context, ".facebook.com");
        clearCookiesForDomain(context, "https://facebook.com");
        clearCookiesForDomain(context, "https://.facebook.com");
    }

    public static void logd(String tag, Exception e) {
        if (Settings.isDebugEnabled() && tag != null && e != null) {
            Log.d(tag, String.valueOf(e.getClass().getSimpleName()) + ": " + e.getMessage());
        }
    }

    public static void logd(String tag, String msg) {
        if (Settings.isDebugEnabled() && tag != null && msg != null) {
            Log.d(tag, msg);
        }
    }

    public static void logd(String tag, String msg, Throwable t) {
        if (Settings.isDebugEnabled() && !isNullOrEmpty(tag)) {
            Log.d(tag, msg, t);
        }
    }

    public static <T> boolean areObjectsEqual(T a, T b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    public static void loadAppSettingsAsync(final Context context, final String applicationId) {
        if (!isNullOrEmpty(applicationId) && !fetchedAppSettings.containsKey(applicationId) && initialAppSettingsLoadTask == null) {
            final String settingsKey = String.format(APP_SETTINGS_PREFS_KEY_FORMAT, applicationId);
            initialAppSettingsLoadTask = new AsyncTask<Void, Void, GraphObject>() { // from class: com.facebook.internal.Utility.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public GraphObject doInBackground(Void... params) {
                    return Utility.getAppSettingsQueryResponse(applicationId);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public void onPostExecute(GraphObject result) {
                    if (result != null) {
                        JSONObject resultJSON = result.getInnerJSONObject();
                        Utility.parseAppSettingsFromJSON(applicationId, resultJSON);
                        SharedPreferences sharedPrefs = context.getSharedPreferences(Utility.APP_SETTINGS_PREFS_STORE, 0);
                        sharedPrefs.edit().putString(settingsKey, resultJSON.toString()).apply();
                    }
                    Utility.initialAppSettingsLoadTask = null;
                }
            };
            initialAppSettingsLoadTask.execute((Void[]) null);
            SharedPreferences sharedPrefs = context.getSharedPreferences(APP_SETTINGS_PREFS_STORE, 0);
            String settingsJSONString = sharedPrefs.getString(settingsKey, null);
            if (!isNullOrEmpty(settingsJSONString)) {
                JSONObject settingsJSON = null;
                try {
                    JSONObject settingsJSON2 = new JSONObject(settingsJSONString);
                    settingsJSON = settingsJSON2;
                } catch (JSONException je) {
                    logd(LOG_TAG, je);
                }
                if (settingsJSON != null) {
                    parseAppSettingsFromJSON(applicationId, settingsJSON);
                }
            }
        }
    }

    public static FetchedAppSettings queryAppSettings(String applicationId, boolean forceRequery) {
        if (!forceRequery && fetchedAppSettings.containsKey(applicationId)) {
            return fetchedAppSettings.get(applicationId);
        }
        GraphObject response = getAppSettingsQueryResponse(applicationId);
        if (response == null) {
            return null;
        }
        return parseAppSettingsFromJSON(applicationId, response.getInnerJSONObject());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static FetchedAppSettings parseAppSettingsFromJSON(String applicationId, JSONObject settingsJSON) {
        FetchedAppSettings result = new FetchedAppSettings(settingsJSON.optBoolean(APP_SETTING_SUPPORTS_ATTRIBUTION, false), settingsJSON.optBoolean(APP_SETTING_SUPPORTS_IMPLICIT_SDK_LOGGING, false), settingsJSON.optString(APP_SETTING_NUX_CONTENT, ""), settingsJSON.optBoolean(APP_SETTING_NUX_ENABLED, false), parseDialogConfigurations(settingsJSON.optJSONObject(APP_SETTING_DIALOG_CONFIGS)), null);
        fetchedAppSettings.put(applicationId, result);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static GraphObject getAppSettingsQueryResponse(String applicationId) {
        Bundle appSettingsParams = new Bundle();
        appSettingsParams.putString(APPLICATION_FIELDS, TextUtils.join(",", APP_SETTING_FIELDS));
        Request request = Request.newGraphPathRequest(null, applicationId, null);
        request.setSkipClientToken(true);
        request.setParameters(appSettingsParams);
        GraphObject response = request.executeAndWait().getGraphObject();
        return response;
    }

    public static DialogFeatureConfig getDialogFeatureConfig(String applicationId, String actionName, String featureName) {
        FetchedAppSettings settings;
        Map<String, DialogFeatureConfig> featureMap;
        if (isNullOrEmpty(actionName) || isNullOrEmpty(featureName) || (settings = fetchedAppSettings.get(applicationId)) == null || (featureMap = settings.getDialogConfigurations().get(actionName)) == null) {
            return null;
        }
        return featureMap.get(featureName);
    }

    private static Map<String, Map<String, DialogFeatureConfig>> parseDialogConfigurations(JSONObject dialogConfigResponse) {
        JSONArray dialogConfigData;
        HashMap<String, Map<String, DialogFeatureConfig>> dialogConfigMap = new HashMap<>();
        if (dialogConfigResponse != null && (dialogConfigData = dialogConfigResponse.optJSONArray("data")) != null) {
            for (int i = 0; i < dialogConfigData.length(); i++) {
                DialogFeatureConfig dialogConfig = DialogFeatureConfig.parseDialogConfig(dialogConfigData.optJSONObject(i));
                if (dialogConfig != null) {
                    String dialogName = dialogConfig.getDialogName();
                    Map<String, DialogFeatureConfig> featureMap = dialogConfigMap.get(dialogName);
                    if (featureMap == null) {
                        featureMap = new HashMap<>();
                        dialogConfigMap.put(dialogName, featureMap);
                    }
                    featureMap.put(dialogConfig.getFeatureName(), dialogConfig);
                }
            }
        }
        return dialogConfigMap;
    }

    public static boolean safeGetBooleanFromResponse(GraphObject response, String propertyName) {
        boolean z = false;
        if (response != null) {
            z = response.getProperty(propertyName);
        }
        return ((Boolean) (!(z instanceof Boolean) ? false : z)).booleanValue();
    }

    public static String safeGetStringFromResponse(GraphObject response, String propertyName) {
        Object obj = "";
        if (response != null) {
            obj = response.getProperty(propertyName);
        }
        return (String) (!(obj instanceof String) ? "" : obj);
    }

    public static JSONObject tryGetJSONObjectFromResponse(GraphObject response, String propertyKey) {
        if (response == null) {
            return null;
        }
        Object property = response.getProperty(propertyKey);
        if (property instanceof JSONObject) {
            return (JSONObject) property;
        }
        return null;
    }

    public static JSONArray tryGetJSONArrayFromResponse(GraphObject response, String propertyKey) {
        if (response == null) {
            return null;
        }
        Object property = response.getProperty(propertyKey);
        if (property instanceof JSONArray) {
            return (JSONArray) property;
        }
        return null;
    }

    public static void clearCaches(Context context) {
        ImageDownloader.clearCache(context);
    }

    public static void deleteDirectory(File directoryOrFile) {
        File[] listFiles;
        if (directoryOrFile.exists()) {
            if (directoryOrFile.isDirectory()) {
                for (File child : directoryOrFile.listFiles()) {
                    deleteDirectory(child);
                }
            }
            directoryOrFile.delete();
        }
    }

    public static <T> List<T> asListNoNulls(T... tArr) {
        ArrayList<T> result = new ArrayList<>();
        for (T t : tArr) {
            if (t != null) {
                result.add(t);
            }
        }
        return result;
    }

    public static String getHashedDeviceAndAppID(Context context, String applicationId) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        if (androidId == null) {
            return null;
        }
        return sha1hash(String.valueOf(androidId) + applicationId);
    }

    public static void setAppEventAttributionParameters(GraphObject params, AttributionIdentifiers attributionIdentifiers, String hashedDeviceAndAppId, boolean limitEventUsage) {
        if (attributionIdentifiers != null && attributionIdentifiers.getAttributionId() != null) {
            params.setProperty("attribution", attributionIdentifiers.getAttributionId());
        }
        if (attributionIdentifiers != null && attributionIdentifiers.getAndroidAdvertiserId() != null) {
            params.setProperty("advertiser_id", attributionIdentifiers.getAndroidAdvertiserId());
            params.setProperty("advertiser_tracking_enabled", Boolean.valueOf(!attributionIdentifiers.isTrackingLimited()));
        } else if (hashedDeviceAndAppId != null) {
            params.setProperty("advertiser_id", hashedDeviceAndAppId);
        }
        params.setProperty("application_tracking_enabled", Boolean.valueOf(limitEventUsage ? false : true));
    }

    public static void setAppEventExtendedDeviceInfoParameters(GraphObject params, Context appContext) {
        JSONArray extraInfoArray = new JSONArray();
        extraInfoArray.put(EXTRA_APP_EVENTS_INFO_FORMAT_VERSION);
        String pkgName = appContext.getPackageName();
        int versionCode = -1;
        String versionName = "";
        try {
            PackageInfo pi = appContext.getPackageManager().getPackageInfo(pkgName, 0);
            versionCode = pi.versionCode;
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        extraInfoArray.put(pkgName);
        extraInfoArray.put(versionCode);
        extraInfoArray.put(versionName);
        params.setProperty("extinfo", extraInfoArray.toString());
    }

    public static Method getMethodQuietly(Class<?> clazz, String methodName, Class<?>... clsArr) {
        try {
            return clazz.getMethod(methodName, clsArr);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getMethodQuietly(String className, String methodName, Class<?>... clsArr) {
        try {
            Class<?> clazz = Class.forName(className);
            return getMethodQuietly(clazz, methodName, clsArr);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object invokeMethodQuietly(Object receiver, Method method, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e2) {
            return null;
        }
    }

    public static String getActivityName(Context context) {
        if (context == null) {
            return "null";
        }
        if (context == context.getApplicationContext()) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return context.getClass().getSimpleName();
    }
}
