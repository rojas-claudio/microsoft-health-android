package com.facebook;

import android.net.Uri;
import android.os.Bundle;
import bolts.AppLink;
import bolts.AppLinkResolver;
import bolts.Continuation;
import bolts.Task;
import com.facebook.Request;
import com.facebook.model.GraphObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class FacebookAppLinkResolver implements AppLinkResolver {
    private static final String APP_LINK_ANDROID_TARGET_KEY = "android";
    private static final String APP_LINK_KEY = "app_links";
    private static final String APP_LINK_TARGET_APP_NAME_KEY = "app_name";
    private static final String APP_LINK_TARGET_CLASS_KEY = "class";
    private static final String APP_LINK_TARGET_PACKAGE_KEY = "package";
    private static final String APP_LINK_TARGET_SHOULD_FALLBACK_KEY = "should_fallback";
    private static final String APP_LINK_TARGET_URL_KEY = "url";
    private static final String APP_LINK_WEB_TARGET_KEY = "web";
    private final HashMap<Uri, AppLink> cachedAppLinks = new HashMap<>();

    public Task<AppLink> getAppLinkFromUrlInBackground(final Uri uri) {
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(uri);
        Task<Map<Uri, AppLink>> resolveTask = getAppLinkFromUrlsInBackground(uris);
        return resolveTask.onSuccess(new Continuation<Map<Uri, AppLink>, AppLink>() { // from class: com.facebook.FacebookAppLinkResolver.1
            /* renamed from: then  reason: collision with other method in class */
            public /* bridge */ /* synthetic */ Object m5then(Task task) throws Exception {
                return then((Task<Map<Uri, AppLink>>) task);
            }

            public AppLink then(Task<Map<Uri, AppLink>> resolveUrisTask) throws Exception {
                return (AppLink) ((Map) resolveUrisTask.getResult()).get(uri);
            }
        });
    }

    public Task<Map<Uri, AppLink>> getAppLinkFromUrlsInBackground(List<Uri> uris) {
        AppLink appLink;
        final Map<Uri, AppLink> appLinkResults = new HashMap<>();
        final HashSet<Uri> urisToRequest = new HashSet<>();
        StringBuilder graphRequestFields = new StringBuilder();
        for (Uri uri : uris) {
            synchronized (this.cachedAppLinks) {
                appLink = this.cachedAppLinks.get(uri);
            }
            if (appLink != null) {
                appLinkResults.put(uri, appLink);
            } else {
                if (!urisToRequest.isEmpty()) {
                    graphRequestFields.append(',');
                }
                graphRequestFields.append(uri.toString());
                urisToRequest.add(uri);
            }
        }
        if (urisToRequest.isEmpty()) {
            return Task.forResult(appLinkResults);
        }
        final Task<Map<Uri, AppLink>>.TaskCompletionSource taskCompletionSource = Task.create();
        Bundle appLinkRequestParameters = new Bundle();
        appLinkRequestParameters.putString("ids", graphRequestFields.toString());
        appLinkRequestParameters.putString("fields", String.format("%s.fields(%s,%s)", APP_LINK_KEY, APP_LINK_ANDROID_TARGET_KEY, APP_LINK_WEB_TARGET_KEY));
        Request appLinkRequest = new Request(null, "", appLinkRequestParameters, null, new Request.Callback() { // from class: com.facebook.FacebookAppLinkResolver.2
            @Override // com.facebook.Request.Callback
            public void onCompleted(Response response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    taskCompletionSource.setError(error.getException());
                    return;
                }
                GraphObject responseObject = response.getGraphObject();
                JSONObject responseJson = responseObject != null ? responseObject.getInnerJSONObject() : null;
                if (responseJson == null) {
                    taskCompletionSource.setResult(appLinkResults);
                    return;
                }
                Iterator it = urisToRequest.iterator();
                while (it.hasNext()) {
                    Uri uri2 = (Uri) it.next();
                    String uriString = uri2.toString();
                    if (responseJson.has(uriString)) {
                        try {
                            JSONObject urlData = responseJson.getJSONObject(uri2.toString());
                            JSONObject appLinkData = urlData.getJSONObject(FacebookAppLinkResolver.APP_LINK_KEY);
                            JSONArray rawTargets = appLinkData.getJSONArray(FacebookAppLinkResolver.APP_LINK_ANDROID_TARGET_KEY);
                            int targetsCount = rawTargets.length();
                            List<AppLink.Target> targets = new ArrayList<>(targetsCount);
                            for (int i = 0; i < targetsCount; i++) {
                                AppLink.Target target = FacebookAppLinkResolver.getAndroidTargetFromJson(rawTargets.getJSONObject(i));
                                if (target != null) {
                                    targets.add(target);
                                }
                            }
                            Uri webFallbackUrl = FacebookAppLinkResolver.getWebFallbackUriFromJson(uri2, appLinkData);
                            AppLink appLink2 = new AppLink(uri2, targets, webFallbackUrl);
                            appLinkResults.put(uri2, appLink2);
                            synchronized (FacebookAppLinkResolver.this.cachedAppLinks) {
                                FacebookAppLinkResolver.this.cachedAppLinks.put(uri2, appLink2);
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
                taskCompletionSource.setResult(appLinkResults);
            }
        });
        appLinkRequest.executeAsync();
        return taskCompletionSource.getTask();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AppLink.Target getAndroidTargetFromJson(JSONObject targetJson) {
        String packageName = tryGetStringFromJson(targetJson, APP_LINK_TARGET_PACKAGE_KEY, null);
        if (packageName == null) {
            return null;
        }
        String className = tryGetStringFromJson(targetJson, APP_LINK_TARGET_CLASS_KEY, null);
        String appName = tryGetStringFromJson(targetJson, "app_name", null);
        String targetUrlString = tryGetStringFromJson(targetJson, "url", null);
        Uri targetUri = null;
        if (targetUrlString != null) {
            targetUri = Uri.parse(targetUrlString);
        }
        return new AppLink.Target(packageName, className, targetUri, appName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Uri getWebFallbackUriFromJson(Uri sourceUrl, JSONObject urlData) {
        try {
            JSONObject webTarget = urlData.getJSONObject(APP_LINK_WEB_TARGET_KEY);
            boolean shouldFallback = tryGetBooleanFromJson(webTarget, APP_LINK_TARGET_SHOULD_FALLBACK_KEY, true);
            if (shouldFallback) {
                String webTargetUrlString = tryGetStringFromJson(webTarget, "url", null);
                Uri webUri = null;
                if (webTargetUrlString != null) {
                    webUri = Uri.parse(webTargetUrlString);
                }
                return webUri == null ? sourceUrl : webUri;
            }
            return null;
        } catch (JSONException e) {
            return sourceUrl;
        }
    }

    private static String tryGetStringFromJson(JSONObject json, String propertyName, String defaultValue) {
        try {
            String defaultValue2 = json.getString(propertyName);
            return defaultValue2;
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    private static boolean tryGetBooleanFromJson(JSONObject json, String propertyName, boolean defaultValue) {
        try {
            boolean defaultValue2 = json.getBoolean(propertyName);
            return defaultValue2;
        } catch (JSONException e) {
            return defaultValue;
        }
    }
}
