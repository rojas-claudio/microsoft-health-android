package com.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import com.microsoft.kapp.utils.Constants;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class AppLinkData {
    private static final String APPLINK_BRIDGE_ARGS_KEY = "bridge_args";
    private static final String APPLINK_METHOD_ARGS_KEY = "method_args";
    private static final String APPLINK_VERSION_KEY = "version";
    public static final String ARGUMENTS_NATIVE_CLASS_KEY = "com.facebook.platform.APPLINK_NATIVE_CLASS";
    public static final String ARGUMENTS_NATIVE_URL = "com.facebook.platform.APPLINK_NATIVE_URL";
    public static final String ARGUMENTS_REFERER_DATA_KEY = "referer_data";
    public static final String ARGUMENTS_TAPTIME_KEY = "com.facebook.platform.APPLINK_TAP_TIME_UTC";
    private static final String BRIDGE_ARGS_METHOD_KEY = "method";
    private static final String BUNDLE_AL_APPLINK_DATA_KEY = "al_applink_data";
    static final String BUNDLE_APPLINK_ARGS_KEY = "com.facebook.platform.APPLINK_ARGS";
    private static final String DEFERRED_APP_LINK_ARGS_FIELD = "applink_args";
    private static final String DEFERRED_APP_LINK_CLASS_FIELD = "applink_class";
    private static final String DEFERRED_APP_LINK_CLICK_TIME_FIELD = "click_time";
    private static final String DEFERRED_APP_LINK_EVENT = "DEFERRED_APP_LINK";
    private static final String DEFERRED_APP_LINK_PATH = "%s/activities";
    private static final String DEFERRED_APP_LINK_URL_FIELD = "applink_url";
    private static final String METHOD_ARGS_REF_KEY = "ref";
    private static final String METHOD_ARGS_TARGET_URL_KEY = "target_url";
    private static final String REFERER_DATA_REF_KEY = "fb_ref";
    private static final String TAG = AppLinkData.class.getCanonicalName();
    private Bundle argumentBundle;
    private JSONObject arguments;
    private String ref;
    private Uri targetUri;

    /* loaded from: classes.dex */
    public interface CompletionHandler {
        void onDeferredAppLinkDataFetched(AppLinkData appLinkData);
    }

    public static void fetchDeferredAppLinkData(Context context, CompletionHandler completionHandler) {
        fetchDeferredAppLinkData(context, null, completionHandler);
    }

    public static void fetchDeferredAppLinkData(Context context, String applicationId, final CompletionHandler completionHandler) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(completionHandler, "completionHandler");
        if (applicationId == null) {
            applicationId = Utility.getMetadataApplicationId(context);
        }
        Validate.notNull(applicationId, "applicationId");
        final Context applicationContext = context.getApplicationContext();
        final String applicationIdCopy = applicationId;
        Settings.getExecutor().execute(new Runnable() { // from class: com.facebook.AppLinkData.1
            @Override // java.lang.Runnable
            public void run() {
                AppLinkData.fetchDeferredAppLinkFromServer(applicationContext, applicationIdCopy, completionHandler);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fetchDeferredAppLinkFromServer(Context context, String applicationId, CompletionHandler completionHandler) {
        GraphObject deferredApplinkParams = GraphObject.Factory.create();
        deferredApplinkParams.setProperty("event", DEFERRED_APP_LINK_EVENT);
        Utility.setAppEventAttributionParameters(deferredApplinkParams, AttributionIdentifiers.getAttributionIdentifiers(context), Utility.getHashedDeviceAndAppID(context, applicationId), Settings.getLimitEventAndDataUsage(context));
        deferredApplinkParams.setProperty("application_package_name", context.getPackageName());
        String deferredApplinkUrlPath = String.format(DEFERRED_APP_LINK_PATH, applicationId);
        AppLinkData appLinkData = null;
        try {
            Request deferredApplinkRequest = Request.newPostRequest(null, deferredApplinkUrlPath, deferredApplinkParams, null);
            Response deferredApplinkResponse = deferredApplinkRequest.executeAndWait();
            GraphObject wireResponse = deferredApplinkResponse.getGraphObject();
            JSONObject jsonResponse = wireResponse != null ? wireResponse.getInnerJSONObject() : null;
            if (jsonResponse != null) {
                String appLinkArgsJsonString = jsonResponse.optString(DEFERRED_APP_LINK_ARGS_FIELD);
                long tapTimeUtc = jsonResponse.optLong(DEFERRED_APP_LINK_CLICK_TIME_FIELD, -1L);
                String appLinkClassName = jsonResponse.optString(DEFERRED_APP_LINK_CLASS_FIELD);
                String appLinkUrl = jsonResponse.optString(DEFERRED_APP_LINK_URL_FIELD);
                if (!TextUtils.isEmpty(appLinkArgsJsonString)) {
                    appLinkData = createFromJson(appLinkArgsJsonString);
                    if (tapTimeUtc != -1) {
                        try {
                            if (appLinkData.arguments != null) {
                                appLinkData.arguments.put(ARGUMENTS_TAPTIME_KEY, tapTimeUtc);
                            }
                            if (appLinkData.argumentBundle != null) {
                                appLinkData.argumentBundle.putString(ARGUMENTS_TAPTIME_KEY, Long.toString(tapTimeUtc));
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "Unable to put tap time in AppLinkData.arguments");
                        }
                    }
                    if (appLinkClassName != null) {
                        try {
                            if (appLinkData.arguments != null) {
                                appLinkData.arguments.put(ARGUMENTS_NATIVE_CLASS_KEY, appLinkClassName);
                            }
                            if (appLinkData.argumentBundle != null) {
                                appLinkData.argumentBundle.putString(ARGUMENTS_NATIVE_CLASS_KEY, appLinkClassName);
                            }
                        } catch (JSONException e2) {
                            Log.d(TAG, "Unable to put tap time in AppLinkData.arguments");
                        }
                    }
                    if (appLinkUrl != null) {
                        try {
                            if (appLinkData.arguments != null) {
                                appLinkData.arguments.put(ARGUMENTS_NATIVE_URL, appLinkUrl);
                            }
                            if (appLinkData.argumentBundle != null) {
                                appLinkData.argumentBundle.putString(ARGUMENTS_NATIVE_URL, appLinkUrl);
                            }
                        } catch (JSONException e3) {
                            Log.d(TAG, "Unable to put tap time in AppLinkData.arguments");
                        }
                    }
                }
            }
        } catch (Exception e4) {
            Utility.logd(TAG, "Unable to fetch deferred applink from server");
        }
        completionHandler.onDeferredAppLinkDataFetched(appLinkData);
    }

    public static AppLinkData createFromActivity(Activity activity) {
        Validate.notNull(activity, "activity");
        Intent intent = activity.getIntent();
        if (intent == null) {
            return null;
        }
        AppLinkData appLinkData = createFromAlApplinkData(intent);
        if (appLinkData == null) {
            String appLinkArgsJsonString = intent.getStringExtra(BUNDLE_APPLINK_ARGS_KEY);
            appLinkData = createFromJson(appLinkArgsJsonString);
        }
        if (appLinkData == null) {
            return createFromUri(intent.getData());
        }
        return appLinkData;
    }

    private static AppLinkData createFromAlApplinkData(Intent intent) {
        String targetUriString;
        Bundle applinks = intent.getBundleExtra(BUNDLE_AL_APPLINK_DATA_KEY);
        if (applinks == null) {
            return null;
        }
        AppLinkData appLinkData = new AppLinkData();
        appLinkData.targetUri = intent.getData();
        if (appLinkData.targetUri == null && (targetUriString = applinks.getString(METHOD_ARGS_TARGET_URL_KEY)) != null) {
            appLinkData.targetUri = Uri.parse(targetUriString);
        }
        appLinkData.argumentBundle = applinks;
        appLinkData.arguments = null;
        Bundle refererData = applinks.getBundle(ARGUMENTS_REFERER_DATA_KEY);
        if (refererData != null) {
            appLinkData.ref = refererData.getString(REFERER_DATA_REF_KEY);
            return appLinkData;
        }
        return appLinkData;
    }

    private static AppLinkData createFromJson(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        try {
            JSONObject appLinkArgsJson = new JSONObject(jsonString);
            String version = appLinkArgsJson.getString("version");
            JSONObject bridgeArgs = appLinkArgsJson.getJSONObject("bridge_args");
            String method = bridgeArgs.getString(BRIDGE_ARGS_METHOD_KEY);
            if (method.equals("applink") && version.equals("2")) {
                AppLinkData appLinkData = new AppLinkData();
                appLinkData.arguments = appLinkArgsJson.getJSONObject("method_args");
                if (appLinkData.arguments.has(METHOD_ARGS_REF_KEY)) {
                    appLinkData.ref = appLinkData.arguments.getString(METHOD_ARGS_REF_KEY);
                } else if (appLinkData.arguments.has(ARGUMENTS_REFERER_DATA_KEY)) {
                    JSONObject refererData = appLinkData.arguments.getJSONObject(ARGUMENTS_REFERER_DATA_KEY);
                    if (refererData.has(REFERER_DATA_REF_KEY)) {
                        appLinkData.ref = refererData.getString(REFERER_DATA_REF_KEY);
                    }
                }
                if (appLinkData.arguments.has(METHOD_ARGS_TARGET_URL_KEY)) {
                    appLinkData.targetUri = Uri.parse(appLinkData.arguments.getString(METHOD_ARGS_TARGET_URL_KEY));
                }
                appLinkData.argumentBundle = toBundle(appLinkData.arguments);
                return appLinkData;
            }
        } catch (FacebookException e) {
            Log.d(TAG, "Unable to parse AppLink JSON", e);
        } catch (JSONException e2) {
            Log.d(TAG, "Unable to parse AppLink JSON", e2);
        }
        return null;
    }

    private static AppLinkData createFromUri(Uri appLinkDataUri) {
        if (appLinkDataUri == null) {
            return null;
        }
        AppLinkData appLinkData = new AppLinkData();
        appLinkData.targetUri = appLinkDataUri;
        return appLinkData;
    }

    private static Bundle toBundle(JSONObject node) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator<String> fields = node.keys();
        while (fields.hasNext()) {
            String key = fields.next();
            Object value = node.get(key);
            if (value instanceof JSONObject) {
                bundle.putBundle(key, toBundle((JSONObject) value));
            } else if (value instanceof JSONArray) {
                JSONArray valueArr = (JSONArray) value;
                if (valueArr.length() == 0) {
                    bundle.putStringArray(key, new String[0]);
                } else {
                    Object firstNode = valueArr.get(0);
                    if (firstNode instanceof JSONObject) {
                        Bundle[] bundles = new Bundle[valueArr.length()];
                        for (int i = 0; i < valueArr.length(); i++) {
                            bundles[i] = toBundle(valueArr.getJSONObject(i));
                        }
                        bundle.putParcelableArray(key, bundles);
                    } else if (firstNode instanceof JSONArray) {
                        throw new FacebookException("Nested arrays are not supported.");
                    } else {
                        String[] arrValues = new String[valueArr.length()];
                        for (int i2 = 0; i2 < valueArr.length(); i2++) {
                            arrValues[i2] = valueArr.get(i2).toString();
                        }
                        bundle.putStringArray(key, arrValues);
                    }
                }
            } else {
                bundle.putString(key, value.toString());
            }
        }
        return bundle;
    }

    private AppLinkData() {
    }

    public Uri getTargetUri() {
        return this.targetUri;
    }

    public String getRef() {
        return this.ref;
    }

    @Deprecated
    public JSONObject getArguments() {
        return this.arguments;
    }

    public Bundle getArgumentBundle() {
        return this.argumentBundle;
    }

    public Bundle getRefererData() {
        if (this.argumentBundle != null) {
            return this.argumentBundle.getBundle(ARGUMENTS_REFERER_DATA_KEY);
        }
        return null;
    }
}
