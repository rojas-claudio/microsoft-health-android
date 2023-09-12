package com.facebook.internal;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.AppEventsLogger;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.internal.FileLruCache;
import com.facebook.internal.PlatformServiceClient;
import com.facebook.widget.FacebookDialog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class LikeActionController {
    public static final String ACTION_LIKE_ACTION_CONTROLLER_DID_ERROR = "com.facebook.sdk.LikeActionController.DID_ERROR";
    public static final String ACTION_LIKE_ACTION_CONTROLLER_DID_RESET = "com.facebook.sdk.LikeActionController.DID_RESET";
    public static final String ACTION_LIKE_ACTION_CONTROLLER_UPDATED = "com.facebook.sdk.LikeActionController.UPDATED";
    public static final String ACTION_OBJECT_ID_KEY = "com.facebook.sdk.LikeActionController.OBJECT_ID";
    private static final int ERROR_CODE_OBJECT_ALREADY_LIKED = 3501;
    public static final String ERROR_INVALID_OBJECT_ID = "Invalid Object Id";
    private static final String JSON_BOOL_IS_OBJECT_LIKED_KEY = "is_object_liked";
    private static final String JSON_BUNDLE_PENDING_CALL_ANALYTICS_BUNDLE = "pending_call_analytics_bundle";
    private static final String JSON_INT_VERSION_KEY = "com.facebook.internal.LikeActionController.version";
    private static final String JSON_STRING_LIKE_COUNT_WITHOUT_LIKE_KEY = "like_count_string_without_like";
    private static final String JSON_STRING_LIKE_COUNT_WITH_LIKE_KEY = "like_count_string_with_like";
    private static final String JSON_STRING_OBJECT_ID_KEY = "object_id";
    private static final String JSON_STRING_PENDING_CALL_ID_KEY = "pending_call_id";
    private static final String JSON_STRING_SOCIAL_SENTENCE_WITHOUT_LIKE_KEY = "social_sentence_without_like";
    private static final String JSON_STRING_SOCIAL_SENTENCE_WITH_LIKE_KEY = "social_sentence_with_like";
    private static final String JSON_STRING_UNLIKE_TOKEN_KEY = "unlike_token";
    private static final String LIKE_ACTION_CONTROLLER_STORE = "com.facebook.LikeActionController.CONTROLLER_STORE_KEY";
    private static final String LIKE_ACTION_CONTROLLER_STORE_OBJECT_SUFFIX_KEY = "OBJECT_SUFFIX";
    private static final String LIKE_ACTION_CONTROLLER_STORE_PENDING_OBJECT_ID_KEY = "PENDING_CONTROLLER_KEY";
    private static final int LIKE_ACTION_CONTROLLER_VERSION = 2;
    private static final String LIKE_DIALOG_RESPONSE_LIKE_COUNT_STRING_KEY = "like_count_string";
    private static final String LIKE_DIALOG_RESPONSE_OBJECT_IS_LIKED_KEY = "object_is_liked";
    private static final String LIKE_DIALOG_RESPONSE_SOCIAL_SENTENCE_KEY = "social_sentence";
    private static final String LIKE_DIALOG_RESPONSE_UNLIKE_TOKEN_KEY = "unlike_token";
    private static final int MAX_CACHE_SIZE = 128;
    private static final int MAX_OBJECT_SUFFIX = 1000;
    private static FileLruCache controllerDiskCache;
    private static Handler handler;
    private static boolean isInitialized;
    private static boolean isPendingBroadcastReset;
    private static String objectIdForPendingController;
    private static volatile int objectSuffix;
    private AppEventsLogger appEventsLogger;
    private Context context;
    private boolean isObjectLiked;
    private boolean isObjectLikedOnServer;
    private boolean isPendingLikeOrUnlike;
    private String likeCountStringWithLike;
    private String likeCountStringWithoutLike;
    private String objectId;
    private boolean objectIsPage;
    private Bundle pendingCallAnalyticsBundle;
    private UUID pendingCallId;
    private Session session;
    private String socialSentenceWithLike;
    private String socialSentenceWithoutLike;
    private String unlikeToken;
    private String verifiedObjectId;
    private static final String TAG = LikeActionController.class.getSimpleName();
    private static final ConcurrentHashMap<String, LikeActionController> cache = new ConcurrentHashMap<>();
    private static WorkQueue mruCacheWorkQueue = new WorkQueue(1);
    private static WorkQueue diskIOWorkQueue = new WorkQueue(1);

    /* loaded from: classes.dex */
    public interface CreationCallback {
        void onComplete(LikeActionController likeActionController);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface RequestCompletionCallback {
        void onComplete();
    }

    public static boolean handleOnActivityResult(Context context, final int requestCode, final int resultCode, final Intent data) {
        final UUID callId = NativeProtocol.getCallIdFromIntent(data);
        if (callId == null) {
            return false;
        }
        if (Utility.isNullOrEmpty(objectIdForPendingController)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(LIKE_ACTION_CONTROLLER_STORE, 0);
            objectIdForPendingController = sharedPreferences.getString(LIKE_ACTION_CONTROLLER_STORE_PENDING_OBJECT_ID_KEY, null);
        }
        if (Utility.isNullOrEmpty(objectIdForPendingController)) {
            return false;
        }
        getControllerForObjectId(context, objectIdForPendingController, new CreationCallback() { // from class: com.facebook.internal.LikeActionController.1
            @Override // com.facebook.internal.LikeActionController.CreationCallback
            public void onComplete(LikeActionController likeActionController) {
                likeActionController.onActivityResult(requestCode, resultCode, data, callId);
            }
        });
        return true;
    }

    public static void getControllerForObjectId(Context context, String objectId, CreationCallback callback) {
        if (!isInitialized) {
            performFirstInitialize(context);
        }
        LikeActionController controllerForObject = getControllerFromInMemoryCache(objectId);
        if (controllerForObject != null) {
            invokeCallbackWithController(callback, controllerForObject);
        } else {
            diskIOWorkQueue.addActiveWorkItem(new CreateLikeActionControllerWorkItem(context, objectId, callback));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void createControllerForObjectId(Context context, String objectId, CreationCallback callback) {
        LikeActionController controllerForObject = getControllerFromInMemoryCache(objectId);
        if (controllerForObject != null) {
            invokeCallbackWithController(callback, controllerForObject);
            return;
        }
        LikeActionController controllerForObject2 = deserializeFromDiskSynchronously(context, objectId);
        if (controllerForObject2 == null) {
            controllerForObject2 = new LikeActionController(context, Session.getActiveSession(), objectId);
            serializeToDiskAsync(controllerForObject2);
        }
        putControllerInMemoryCache(objectId, controllerForObject2);
        LikeActionController controllerToRefresh = controllerForObject2;
        handler.post(new Runnable() { // from class: com.facebook.internal.LikeActionController.2
            @Override // java.lang.Runnable
            public void run() {
                LikeActionController.this.refreshStatusAsync();
            }
        });
        invokeCallbackWithController(callback, controllerToRefresh);
    }

    private static synchronized void performFirstInitialize(Context context) {
        synchronized (LikeActionController.class) {
            if (!isInitialized) {
                handler = new Handler(Looper.getMainLooper());
                SharedPreferences sharedPreferences = context.getSharedPreferences(LIKE_ACTION_CONTROLLER_STORE, 0);
                objectSuffix = sharedPreferences.getInt(LIKE_ACTION_CONTROLLER_STORE_OBJECT_SUFFIX_KEY, 1);
                controllerDiskCache = new FileLruCache(context, TAG, new FileLruCache.Limits());
                registerSessionBroadcastReceivers(context);
                isInitialized = true;
            }
        }
    }

    private static void invokeCallbackWithController(final CreationCallback callback, final LikeActionController controller) {
        if (callback != null) {
            handler.post(new Runnable() { // from class: com.facebook.internal.LikeActionController.3
                @Override // java.lang.Runnable
                public void run() {
                    CreationCallback.this.onComplete(controller);
                }
            });
        }
    }

    private static void registerSessionBroadcastReceivers(Context context) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Session.ACTION_ACTIVE_SESSION_UNSET);
        filter.addAction(Session.ACTION_ACTIVE_SESSION_CLOSED);
        filter.addAction(Session.ACTION_ACTIVE_SESSION_OPENED);
        broadcastManager.registerReceiver(new BroadcastReceiver() { // from class: com.facebook.internal.LikeActionController.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(final Context receiverContext, Intent intent) {
                if (!LikeActionController.isPendingBroadcastReset) {
                    String action = intent.getAction();
                    final boolean shouldClearDisk = Utility.areObjectsEqual(Session.ACTION_ACTIVE_SESSION_UNSET, action) || Utility.areObjectsEqual(Session.ACTION_ACTIVE_SESSION_CLOSED, action);
                    LikeActionController.isPendingBroadcastReset = true;
                    LikeActionController.handler.postDelayed(new Runnable() { // from class: com.facebook.internal.LikeActionController.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (shouldClearDisk) {
                                LikeActionController.objectSuffix = (LikeActionController.objectSuffix + 1) % 1000;
                                receiverContext.getSharedPreferences(LikeActionController.LIKE_ACTION_CONTROLLER_STORE, 0).edit().putInt(LikeActionController.LIKE_ACTION_CONTROLLER_STORE_OBJECT_SUFFIX_KEY, LikeActionController.objectSuffix).apply();
                                LikeActionController.cache.clear();
                                LikeActionController.controllerDiskCache.clearCache();
                            }
                            LikeActionController.broadcastAction(receiverContext, null, LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_RESET);
                            LikeActionController.isPendingBroadcastReset = false;
                        }
                    }, 100L);
                }
            }
        }, filter);
    }

    private static void putControllerInMemoryCache(String objectId, LikeActionController controllerForObject) {
        String cacheKey = getCacheKeyForObjectId(objectId);
        mruCacheWorkQueue.addActiveWorkItem(new MRUCacheWorkItem(cacheKey, true));
        cache.put(cacheKey, controllerForObject);
    }

    private static LikeActionController getControllerFromInMemoryCache(String objectId) {
        String cacheKey = getCacheKeyForObjectId(objectId);
        LikeActionController controller = cache.get(cacheKey);
        if (controller != null) {
            mruCacheWorkQueue.addActiveWorkItem(new MRUCacheWorkItem(cacheKey, false));
        }
        return controller;
    }

    private static void serializeToDiskAsync(LikeActionController controller) {
        String controllerJson = serializeToJson(controller);
        String cacheKey = getCacheKeyForObjectId(controller.objectId);
        if (!Utility.isNullOrEmpty(controllerJson) && !Utility.isNullOrEmpty(cacheKey)) {
            diskIOWorkQueue.addActiveWorkItem(new SerializeToDiskWorkItem(cacheKey, controllerJson));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void serializeToDiskSynchronously(String cacheKey, String controllerJson) {
        OutputStream outputStream = null;
        try {
            try {
                outputStream = controllerDiskCache.openPutStream(cacheKey);
                outputStream.write(controllerJson.getBytes());
                if (outputStream != null) {
                    Utility.closeQuietly(outputStream);
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to serialize controller to disk", e);
                if (outputStream != null) {
                    Utility.closeQuietly(outputStream);
                }
            }
        } catch (Throwable th) {
            if (outputStream != null) {
                Utility.closeQuietly(outputStream);
            }
            throw th;
        }
    }

    private static LikeActionController deserializeFromDiskSynchronously(Context context, String objectId) {
        LikeActionController controller = null;
        InputStream inputStream = null;
        try {
            try {
                String cacheKey = getCacheKeyForObjectId(objectId);
                inputStream = controllerDiskCache.get(cacheKey);
                if (inputStream != null) {
                    String controllerJsonString = Utility.readStreamToString(inputStream);
                    if (!Utility.isNullOrEmpty(controllerJsonString)) {
                        controller = deserializeFromJson(context, controllerJsonString);
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to deserialize controller from disk", e);
                controller = null;
                if (inputStream != null) {
                    Utility.closeQuietly(inputStream);
                }
            }
            return controller;
        } finally {
            if (inputStream != null) {
                Utility.closeQuietly(inputStream);
            }
        }
    }

    private static LikeActionController deserializeFromJson(Context context, String controllerJsonString) {
        try {
            JSONObject controllerJson = new JSONObject(controllerJsonString);
            int version = controllerJson.optInt(JSON_INT_VERSION_KEY, -1);
            if (version != 2) {
                return null;
            }
            LikeActionController controller = new LikeActionController(context, Session.getActiveSession(), controllerJson.getString("object_id"));
            controller.likeCountStringWithLike = controllerJson.optString(JSON_STRING_LIKE_COUNT_WITH_LIKE_KEY, null);
            controller.likeCountStringWithoutLike = controllerJson.optString(JSON_STRING_LIKE_COUNT_WITHOUT_LIKE_KEY, null);
            controller.socialSentenceWithLike = controllerJson.optString(JSON_STRING_SOCIAL_SENTENCE_WITH_LIKE_KEY, null);
            controller.socialSentenceWithoutLike = controllerJson.optString(JSON_STRING_SOCIAL_SENTENCE_WITHOUT_LIKE_KEY, null);
            controller.isObjectLiked = controllerJson.optBoolean(JSON_BOOL_IS_OBJECT_LIKED_KEY);
            controller.unlikeToken = controllerJson.optString("unlike_token", null);
            String pendingCallIdString = controllerJson.optString(JSON_STRING_PENDING_CALL_ID_KEY, null);
            if (!Utility.isNullOrEmpty(pendingCallIdString)) {
                controller.pendingCallId = UUID.fromString(pendingCallIdString);
            }
            JSONObject analyticsJSON = controllerJson.optJSONObject(JSON_BUNDLE_PENDING_CALL_ANALYTICS_BUNDLE);
            if (analyticsJSON != null) {
                controller.pendingCallAnalyticsBundle = BundleJSONConverter.convertToBundle(analyticsJSON);
                return controller;
            }
            return controller;
        } catch (JSONException e) {
            Log.e(TAG, "Unable to deserialize controller from JSON", e);
            return null;
        }
    }

    private static String serializeToJson(LikeActionController controller) {
        JSONObject analyticsJSON;
        JSONObject controllerJson = new JSONObject();
        try {
            controllerJson.put(JSON_INT_VERSION_KEY, 2);
            controllerJson.put("object_id", controller.objectId);
            controllerJson.put(JSON_STRING_LIKE_COUNT_WITH_LIKE_KEY, controller.likeCountStringWithLike);
            controllerJson.put(JSON_STRING_LIKE_COUNT_WITHOUT_LIKE_KEY, controller.likeCountStringWithoutLike);
            controllerJson.put(JSON_STRING_SOCIAL_SENTENCE_WITH_LIKE_KEY, controller.socialSentenceWithLike);
            controllerJson.put(JSON_STRING_SOCIAL_SENTENCE_WITHOUT_LIKE_KEY, controller.socialSentenceWithoutLike);
            controllerJson.put(JSON_BOOL_IS_OBJECT_LIKED_KEY, controller.isObjectLiked);
            controllerJson.put("unlike_token", controller.unlikeToken);
            if (controller.pendingCallId != null) {
                controllerJson.put(JSON_STRING_PENDING_CALL_ID_KEY, controller.pendingCallId.toString());
            }
            if (controller.pendingCallAnalyticsBundle != null && (analyticsJSON = BundleJSONConverter.convertToJSON(controller.pendingCallAnalyticsBundle)) != null) {
                controllerJson.put(JSON_BUNDLE_PENDING_CALL_ANALYTICS_BUNDLE, analyticsJSON);
            }
            return controllerJson.toString();
        } catch (JSONException e) {
            Log.e(TAG, "Unable to serialize controller to JSON", e);
            return null;
        }
    }

    private static String getCacheKeyForObjectId(String objectId) {
        String accessTokenPortion = null;
        Session activeSession = Session.getActiveSession();
        if (activeSession != null && activeSession.isOpened()) {
            accessTokenPortion = activeSession.getAccessToken();
        }
        if (accessTokenPortion != null) {
            accessTokenPortion = Utility.md5hash(accessTokenPortion);
        }
        return String.format("%s|%s|com.fb.sdk.like|%d", objectId, Utility.coerceValueIfNullOrEmpty(accessTokenPortion, ""), Integer.valueOf(objectSuffix));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void broadcastAction(Context context, LikeActionController controller, String action) {
        broadcastAction(context, controller, action, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void broadcastAction(Context context, LikeActionController controller, String action, Bundle data) {
        Intent broadcastIntent = new Intent(action);
        if (controller != null) {
            if (data == null) {
                data = new Bundle();
            }
            data.putString(ACTION_OBJECT_ID_KEY, controller.getObjectId());
        }
        if (data != null) {
            broadcastIntent.putExtras(data);
        }
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(broadcastIntent);
    }

    private LikeActionController(Context context, Session session, String objectId) {
        this.context = context;
        this.session = session;
        this.objectId = objectId;
        this.appEventsLogger = AppEventsLogger.newLogger(context, session);
    }

    public String getObjectId() {
        return this.objectId;
    }

    public String getLikeCountString() {
        return this.isObjectLiked ? this.likeCountStringWithLike : this.likeCountStringWithoutLike;
    }

    public String getSocialSentence() {
        return this.isObjectLiked ? this.socialSentenceWithLike : this.socialSentenceWithoutLike;
    }

    public boolean isObjectLiked() {
        return this.isObjectLiked;
    }

    public void toggleLike(Activity activity, Bundle analyticsParameters) {
        this.appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_LIKE_VIEW_DID_TAP, null, analyticsParameters);
        boolean shouldLikeObject = !this.isObjectLiked;
        if (canUseOGPublish(shouldLikeObject)) {
            updateState(shouldLikeObject, this.likeCountStringWithLike, this.likeCountStringWithoutLike, this.socialSentenceWithLike, this.socialSentenceWithoutLike, this.unlikeToken);
            if (this.isPendingLikeOrUnlike) {
                this.appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_LIKE_VIEW_DID_UNDO_QUICKLY, null, analyticsParameters);
                return;
            }
        }
        performLikeOrUnlike(activity, shouldLikeObject, analyticsParameters);
    }

    private void performLikeOrUnlike(Activity activity, boolean shouldLikeObject, Bundle analyticsParameters) {
        if (canUseOGPublish(shouldLikeObject)) {
            if (shouldLikeObject) {
                publishLikeAsync(activity, analyticsParameters);
                return;
            } else {
                publishUnlikeAsync(activity, analyticsParameters);
                return;
            }
        }
        presentLikeDialog(activity, analyticsParameters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateState(boolean isObjectLiked, String likeCountStringWithLike, String likeCountStringWithoutLike, String socialSentenceWithLike, String socialSentenceWithoutLike, String unlikeToken) {
        String likeCountStringWithLike2 = Utility.coerceValueIfNullOrEmpty(likeCountStringWithLike, null);
        String likeCountStringWithoutLike2 = Utility.coerceValueIfNullOrEmpty(likeCountStringWithoutLike, null);
        String socialSentenceWithLike2 = Utility.coerceValueIfNullOrEmpty(socialSentenceWithLike, null);
        String socialSentenceWithoutLike2 = Utility.coerceValueIfNullOrEmpty(socialSentenceWithoutLike, null);
        String unlikeToken2 = Utility.coerceValueIfNullOrEmpty(unlikeToken, null);
        boolean stateChanged = (isObjectLiked == this.isObjectLiked && Utility.areObjectsEqual(likeCountStringWithLike2, this.likeCountStringWithLike) && Utility.areObjectsEqual(likeCountStringWithoutLike2, this.likeCountStringWithoutLike) && Utility.areObjectsEqual(socialSentenceWithLike2, this.socialSentenceWithLike) && Utility.areObjectsEqual(socialSentenceWithoutLike2, this.socialSentenceWithoutLike) && Utility.areObjectsEqual(unlikeToken2, this.unlikeToken)) ? false : true;
        if (stateChanged) {
            this.isObjectLiked = isObjectLiked;
            this.likeCountStringWithLike = likeCountStringWithLike2;
            this.likeCountStringWithoutLike = likeCountStringWithoutLike2;
            this.socialSentenceWithLike = socialSentenceWithLike2;
            this.socialSentenceWithoutLike = socialSentenceWithoutLike2;
            this.unlikeToken = unlikeToken2;
            serializeToDiskAsync(this);
            broadcastAction(this.context, this, ACTION_LIKE_ACTION_CONTROLLER_UPDATED);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void presentLikeDialog(Activity activity, Bundle analyticsParameters) {
        LikeDialogBuilder likeDialogBuilder = new LikeDialogBuilder(activity, this.objectId);
        if (likeDialogBuilder.canPresent()) {
            trackPendingCall(likeDialogBuilder.build().present(), analyticsParameters);
            this.appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_LIKE_VIEW_DID_PRESENT_DIALOG, null, analyticsParameters);
            return;
        }
        String webFallbackUrl = likeDialogBuilder.getWebFallbackUrl();
        if (!Utility.isNullOrEmpty(webFallbackUrl)) {
            boolean webFallbackShown = FacebookWebFallbackDialog.presentWebFallback(activity, webFallbackUrl, likeDialogBuilder.getApplicationId(), likeDialogBuilder.getAppCall(), getFacebookDialogCallback(analyticsParameters));
            if (webFallbackShown) {
                this.appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_LIKE_VIEW_DID_PRESENT_FALLBACK, null, analyticsParameters);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data, UUID callId) {
        FacebookDialog.PendingCall pendingCall;
        if (this.pendingCallId == null || !this.pendingCallId.equals(callId) || (pendingCall = PendingCallStore.getInstance().getPendingCallById(this.pendingCallId)) == null) {
            return false;
        }
        FacebookDialog.handleActivityResult(this.context, pendingCall, requestCode, data, getFacebookDialogCallback(this.pendingCallAnalyticsBundle));
        stopTrackingPendingCall();
        return true;
    }

    private FacebookDialog.Callback getFacebookDialogCallback(final Bundle analyticsParameters) {
        return new FacebookDialog.Callback() { // from class: com.facebook.internal.LikeActionController.5
            @Override // com.facebook.widget.FacebookDialog.Callback
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                if (data.containsKey(LikeActionController.LIKE_DIALOG_RESPONSE_OBJECT_IS_LIKED_KEY)) {
                    boolean isObjectLiked = data.getBoolean(LikeActionController.LIKE_DIALOG_RESPONSE_OBJECT_IS_LIKED_KEY);
                    String likeCountString = data.getString(LikeActionController.LIKE_DIALOG_RESPONSE_LIKE_COUNT_STRING_KEY);
                    String socialSentence = data.getString(LikeActionController.LIKE_DIALOG_RESPONSE_SOCIAL_SENTENCE_KEY);
                    String unlikeToken = data.getString("unlike_token");
                    Bundle logParams = analyticsParameters == null ? new Bundle() : analyticsParameters;
                    logParams.putString(AnalyticsEvents.PARAMETER_CALL_ID, pendingCall.getCallId().toString());
                    LikeActionController.this.appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_LIKE_VIEW_DIALOG_DID_SUCCEED, null, logParams);
                    LikeActionController.this.updateState(isObjectLiked, likeCountString, likeCountString, socialSentence, socialSentence, unlikeToken);
                }
            }

            @Override // com.facebook.widget.FacebookDialog.Callback
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Like Dialog failed with error : %s", error);
                Bundle logParams = analyticsParameters == null ? new Bundle() : analyticsParameters;
                logParams.putString(AnalyticsEvents.PARAMETER_CALL_ID, pendingCall.getCallId().toString());
                LikeActionController.this.logAppEventForError("present_dialog", logParams);
                LikeActionController.broadcastAction(LikeActionController.this.context, LikeActionController.this, LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_ERROR, data);
            }
        };
    }

    private void trackPendingCall(FacebookDialog.PendingCall pendingCall, Bundle analyticsParameters) {
        PendingCallStore.getInstance().trackPendingCall(pendingCall);
        this.pendingCallId = pendingCall.getCallId();
        storeObjectIdForPendingController(this.objectId);
        this.pendingCallAnalyticsBundle = analyticsParameters;
        serializeToDiskAsync(this);
    }

    private void stopTrackingPendingCall() {
        PendingCallStore.getInstance().stopTrackingPendingCall(this.pendingCallId);
        this.pendingCallId = null;
        this.pendingCallAnalyticsBundle = null;
        storeObjectIdForPendingController(null);
    }

    private void storeObjectIdForPendingController(String objectId) {
        objectIdForPendingController = objectId;
        this.context.getSharedPreferences(LIKE_ACTION_CONTROLLER_STORE, 0).edit().putString(LIKE_ACTION_CONTROLLER_STORE_PENDING_OBJECT_ID_KEY, objectIdForPendingController).apply();
    }

    private boolean canUseOGPublish(boolean willPerformLike) {
        return (this.objectIsPage || this.verifiedObjectId == null || this.session == null || this.session.getPermissions() == null || !this.session.getPermissions().contains("publish_actions") || (!willPerformLike && Utility.isNullOrEmpty(this.unlikeToken))) ? false : true;
    }

    private void publishLikeAsync(final Activity activity, final Bundle analyticsParameters) {
        this.isPendingLikeOrUnlike = true;
        fetchVerifiedObjectId(new RequestCompletionCallback() { // from class: com.facebook.internal.LikeActionController.6
            @Override // com.facebook.internal.LikeActionController.RequestCompletionCallback
            public void onComplete() {
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    Bundle errorBundle = new Bundle();
                    errorBundle.putString(NativeProtocol.STATUS_ERROR_DESCRIPTION, LikeActionController.ERROR_INVALID_OBJECT_ID);
                    LikeActionController.broadcastAction(LikeActionController.this.context, LikeActionController.this, LikeActionController.ACTION_LIKE_ACTION_CONTROLLER_DID_ERROR, errorBundle);
                    return;
                }
                RequestBatch requestBatch = new RequestBatch();
                final PublishLikeRequestWrapper likeRequest = new PublishLikeRequestWrapper(LikeActionController.this.verifiedObjectId);
                likeRequest.addToBatch(requestBatch);
                final Activity activity2 = activity;
                final Bundle bundle = analyticsParameters;
                requestBatch.addCallback(new RequestBatch.Callback() { // from class: com.facebook.internal.LikeActionController.6.1
                    @Override // com.facebook.RequestBatch.Callback
                    public void onBatchCompleted(RequestBatch batch) {
                        LikeActionController.this.isPendingLikeOrUnlike = false;
                        if (likeRequest.error != null) {
                            LikeActionController.this.updateState(false, LikeActionController.this.likeCountStringWithLike, LikeActionController.this.likeCountStringWithoutLike, LikeActionController.this.socialSentenceWithLike, LikeActionController.this.socialSentenceWithoutLike, LikeActionController.this.unlikeToken);
                            LikeActionController.this.presentLikeDialog(activity2, bundle);
                            return;
                        }
                        LikeActionController.this.unlikeToken = Utility.coerceValueIfNullOrEmpty(likeRequest.unlikeToken, null);
                        LikeActionController.this.isObjectLikedOnServer = true;
                        LikeActionController.this.appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_LIKE_VIEW_DID_LIKE, null, bundle);
                        LikeActionController.this.toggleAgainIfNeeded(activity2, bundle);
                    }
                });
                requestBatch.executeAsync();
            }
        });
    }

    private void publishUnlikeAsync(final Activity activity, final Bundle analyticsParameters) {
        this.isPendingLikeOrUnlike = true;
        RequestBatch requestBatch = new RequestBatch();
        final PublishUnlikeRequestWrapper unlikeRequest = new PublishUnlikeRequestWrapper(this.unlikeToken);
        unlikeRequest.addToBatch(requestBatch);
        requestBatch.addCallback(new RequestBatch.Callback() { // from class: com.facebook.internal.LikeActionController.7
            @Override // com.facebook.RequestBatch.Callback
            public void onBatchCompleted(RequestBatch batch) {
                LikeActionController.this.isPendingLikeOrUnlike = false;
                if (unlikeRequest.error == null) {
                    LikeActionController.this.unlikeToken = null;
                    LikeActionController.this.isObjectLikedOnServer = false;
                    LikeActionController.this.appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_LIKE_VIEW_DID_UNLIKE, null, analyticsParameters);
                    LikeActionController.this.toggleAgainIfNeeded(activity, analyticsParameters);
                    return;
                }
                LikeActionController.this.updateState(true, LikeActionController.this.likeCountStringWithLike, LikeActionController.this.likeCountStringWithoutLike, LikeActionController.this.socialSentenceWithLike, LikeActionController.this.socialSentenceWithoutLike, LikeActionController.this.unlikeToken);
                LikeActionController.this.presentLikeDialog(activity, analyticsParameters);
            }
        });
        requestBatch.executeAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshStatusAsync() {
        if (this.session == null || this.session.isClosed() || SessionState.CREATED.equals(this.session.getState())) {
            refreshStatusViaService();
        } else {
            fetchVerifiedObjectId(new RequestCompletionCallback() { // from class: com.facebook.internal.LikeActionController.8
                @Override // com.facebook.internal.LikeActionController.RequestCompletionCallback
                public void onComplete() {
                    final GetOGObjectLikesRequestWrapper objectLikesRequest = new GetOGObjectLikesRequestWrapper(LikeActionController.this.verifiedObjectId);
                    final GetEngagementRequestWrapper engagementRequest = new GetEngagementRequestWrapper(LikeActionController.this.verifiedObjectId);
                    RequestBatch requestBatch = new RequestBatch();
                    objectLikesRequest.addToBatch(requestBatch);
                    engagementRequest.addToBatch(requestBatch);
                    requestBatch.addCallback(new RequestBatch.Callback() { // from class: com.facebook.internal.LikeActionController.8.1
                        @Override // com.facebook.RequestBatch.Callback
                        public void onBatchCompleted(RequestBatch batch) {
                            if (objectLikesRequest.error == null && engagementRequest.error == null) {
                                LikeActionController.this.updateState(objectLikesRequest.objectIsLiked, engagementRequest.likeCountStringWithLike, engagementRequest.likeCountStringWithoutLike, engagementRequest.socialSentenceStringWithLike, engagementRequest.socialSentenceStringWithoutLike, objectLikesRequest.unlikeToken);
                            } else {
                                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Unable to refresh like state for id: '%s'", LikeActionController.this.objectId);
                            }
                        }
                    });
                    requestBatch.executeAsync();
                }
            });
        }
    }

    private void refreshStatusViaService() {
        LikeStatusClient likeStatusClient = new LikeStatusClient(this.context, Settings.getApplicationId(), this.objectId);
        if (likeStatusClient.start()) {
            PlatformServiceClient.CompletedListener callback = new PlatformServiceClient.CompletedListener() { // from class: com.facebook.internal.LikeActionController.9
                @Override // com.facebook.internal.PlatformServiceClient.CompletedListener
                public void completed(Bundle result) {
                    if (result != null && result.containsKey(NativeProtocol.EXTRA_OBJECT_IS_LIKED)) {
                        boolean objectIsLiked = result.getBoolean(NativeProtocol.EXTRA_OBJECT_IS_LIKED);
                        String likeCountWithLike = result.getString(NativeProtocol.EXTRA_LIKE_COUNT_STRING_WITH_LIKE);
                        String likeCountWithoutLike = result.getString(NativeProtocol.EXTRA_LIKE_COUNT_STRING_WITHOUT_LIKE);
                        String socialSentenceWithLike = result.getString(NativeProtocol.EXTRA_SOCIAL_SENTENCE_WITH_LIKE);
                        String socialSentenceWithoutLike = result.getString(NativeProtocol.EXTRA_SOCIAL_SENTENCE_WITHOUT_LIKE);
                        String unlikeToken = result.getString(NativeProtocol.EXTRA_UNLIKE_TOKEN);
                        LikeActionController.this.updateState(objectIsLiked, likeCountWithLike, likeCountWithoutLike, socialSentenceWithLike, socialSentenceWithoutLike, unlikeToken);
                    }
                }
            };
            likeStatusClient.setCompletedListener(callback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleAgainIfNeeded(Activity activity, Bundle analyticsParameters) {
        if (this.isObjectLiked != this.isObjectLikedOnServer) {
            performLikeOrUnlike(activity, this.isObjectLiked, analyticsParameters);
        }
    }

    private void fetchVerifiedObjectId(final RequestCompletionCallback completionHandler) {
        if (!Utility.isNullOrEmpty(this.verifiedObjectId)) {
            if (completionHandler != null) {
                completionHandler.onComplete();
                return;
            }
            return;
        }
        final GetOGObjectIdRequestWrapper objectIdRequest = new GetOGObjectIdRequestWrapper(this.objectId);
        final GetPageIdRequestWrapper pageIdRequest = new GetPageIdRequestWrapper(this.objectId);
        RequestBatch requestBatch = new RequestBatch();
        objectIdRequest.addToBatch(requestBatch);
        pageIdRequest.addToBatch(requestBatch);
        requestBatch.addCallback(new RequestBatch.Callback() { // from class: com.facebook.internal.LikeActionController.10
            @Override // com.facebook.RequestBatch.Callback
            public void onBatchCompleted(RequestBatch batch) {
                LikeActionController.this.verifiedObjectId = objectIdRequest.verifiedObjectId;
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    LikeActionController.this.verifiedObjectId = pageIdRequest.verifiedObjectId;
                    LikeActionController.this.objectIsPage = pageIdRequest.objectIsPage;
                }
                if (Utility.isNullOrEmpty(LikeActionController.this.verifiedObjectId)) {
                    Logger.log(LoggingBehavior.DEVELOPER_ERRORS, LikeActionController.TAG, "Unable to verify the FB id for '%s'. Verify that it is a valid FB object or page", LikeActionController.this.objectId);
                    LikeActionController.this.logAppEventForError("get_verified_id", pageIdRequest.error != null ? pageIdRequest.error : objectIdRequest.error);
                }
                if (completionHandler != null) {
                    completionHandler.onComplete();
                }
            }
        });
        requestBatch.executeAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppEventForError(String action, Bundle parameters) {
        Bundle logParams = new Bundle(parameters);
        logParams.putString("object_id", this.objectId);
        logParams.putString(AnalyticsEvents.PARAMETER_LIKE_VIEW_CURRENT_ACTION, action);
        this.appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_LIKE_VIEW_ERROR, null, logParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logAppEventForError(String action, FacebookRequestError error) {
        JSONObject requestResult;
        Bundle logParams = new Bundle();
        if (error != null && (requestResult = error.getRequestResult()) != null) {
            logParams.putString("error", requestResult.toString());
        }
        logAppEventForError(action, logParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GetOGObjectIdRequestWrapper extends AbstractRequestWrapper {
        String verifiedObjectId;

        GetOGObjectIdRequestWrapper(String objectId) {
            super(objectId);
            Bundle objectIdRequestParams = new Bundle();
            objectIdRequestParams.putString("fields", "og_object.fields(id)");
            objectIdRequestParams.putString("ids", objectId);
            setRequest(new Request(LikeActionController.this.session, "", objectIdRequestParams, HttpMethod.GET));
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            if (error.getErrorMessage().contains("og_object")) {
                this.error = null;
            } else {
                Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error getting the FB id for object '%s' : %s", this.objectId, error);
            }
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(Response response) {
            JSONObject ogObject;
            JSONObject results = Utility.tryGetJSONObjectFromResponse(response.getGraphObject(), this.objectId);
            if (results != null && (ogObject = results.optJSONObject("og_object")) != null) {
                this.verifiedObjectId = ogObject.optString("id");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GetPageIdRequestWrapper extends AbstractRequestWrapper {
        boolean objectIsPage;
        String verifiedObjectId;

        GetPageIdRequestWrapper(String objectId) {
            super(objectId);
            Bundle pageIdRequestParams = new Bundle();
            pageIdRequestParams.putString("fields", "id");
            pageIdRequestParams.putString("ids", objectId);
            setRequest(new Request(LikeActionController.this.session, "", pageIdRequestParams, HttpMethod.GET));
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(Response response) {
            JSONObject results = Utility.tryGetJSONObjectFromResponse(response.getGraphObject(), this.objectId);
            if (results != null) {
                this.verifiedObjectId = results.optString("id");
                this.objectIsPage = !Utility.isNullOrEmpty(this.verifiedObjectId);
            }
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error getting the FB id for object '%s' : %s", this.objectId, error);
        }
    }

    /* loaded from: classes.dex */
    private class PublishLikeRequestWrapper extends AbstractRequestWrapper {
        String unlikeToken;

        PublishLikeRequestWrapper(String objectId) {
            super(objectId);
            Bundle likeRequestParams = new Bundle();
            likeRequestParams.putString("object", objectId);
            setRequest(new Request(LikeActionController.this.session, "me/og.likes", likeRequestParams, HttpMethod.POST));
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(Response response) {
            this.unlikeToken = Utility.safeGetStringFromResponse(response.getGraphObject(), "id");
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            int errorCode = error.getErrorCode();
            if (errorCode == LikeActionController.ERROR_CODE_OBJECT_ALREADY_LIKED) {
                this.error = null;
                return;
            }
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error liking object '%s' : %s", this.objectId, error);
            LikeActionController.this.logAppEventForError("publish_like", error);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PublishUnlikeRequestWrapper extends AbstractRequestWrapper {
        private String unlikeToken;

        PublishUnlikeRequestWrapper(String unlikeToken) {
            super(null);
            this.unlikeToken = unlikeToken;
            setRequest(new Request(LikeActionController.this.session, unlikeToken, null, HttpMethod.DELETE));
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(Response response) {
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error unliking object with unlike token '%s' : %s", this.unlikeToken, error);
            LikeActionController.this.logAppEventForError("publish_unlike", error);
        }
    }

    /* loaded from: classes.dex */
    private class GetOGObjectLikesRequestWrapper extends AbstractRequestWrapper {
        boolean objectIsLiked;
        String unlikeToken;

        GetOGObjectLikesRequestWrapper(String objectId) {
            super(objectId);
            Bundle requestParams = new Bundle();
            requestParams.putString("fields", "id,application");
            requestParams.putString("object", objectId);
            setRequest(new Request(LikeActionController.this.session, "me/og.likes", requestParams, HttpMethod.GET));
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(Response response) {
            JSONArray dataSet = Utility.tryGetJSONArrayFromResponse(response.getGraphObject(), "data");
            if (dataSet != null) {
                for (int i = 0; i < dataSet.length(); i++) {
                    JSONObject data = dataSet.optJSONObject(i);
                    if (data != null) {
                        this.objectIsLiked = true;
                        JSONObject appData = data.optJSONObject("application");
                        if (appData != null && Utility.areObjectsEqual(LikeActionController.this.session.getApplicationId(), appData.optString("id"))) {
                            this.unlikeToken = data.optString("id");
                        }
                    }
                }
            }
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error fetching like status for object '%s' : %s", this.objectId, error);
            LikeActionController.this.logAppEventForError("get_og_object_like", error);
        }
    }

    /* loaded from: classes.dex */
    private class GetEngagementRequestWrapper extends AbstractRequestWrapper {
        String likeCountStringWithLike;
        String likeCountStringWithoutLike;
        String socialSentenceStringWithLike;
        String socialSentenceStringWithoutLike;

        GetEngagementRequestWrapper(String objectId) {
            super(objectId);
            Bundle requestParams = new Bundle();
            requestParams.putString("fields", "engagement.fields(count_string_with_like,count_string_without_like,social_sentence_with_like,social_sentence_without_like)");
            setRequest(new Request(LikeActionController.this.session, objectId, requestParams, HttpMethod.GET));
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processSuccess(Response response) {
            JSONObject engagementResults = Utility.tryGetJSONObjectFromResponse(response.getGraphObject(), "engagement");
            if (engagementResults != null) {
                this.likeCountStringWithLike = engagementResults.optString("count_string_with_like");
                this.likeCountStringWithoutLike = engagementResults.optString("count_string_without_like");
                this.socialSentenceStringWithLike = engagementResults.optString(LikeActionController.JSON_STRING_SOCIAL_SENTENCE_WITH_LIKE_KEY);
                this.socialSentenceStringWithoutLike = engagementResults.optString(LikeActionController.JSON_STRING_SOCIAL_SENTENCE_WITHOUT_LIKE_KEY);
            }
        }

        @Override // com.facebook.internal.LikeActionController.AbstractRequestWrapper
        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error fetching engagement for object '%s' : %s", this.objectId, error);
            LikeActionController.this.logAppEventForError("get_engagement", error);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class AbstractRequestWrapper {
        FacebookRequestError error;
        protected String objectId;
        private Request request;

        protected abstract void processSuccess(Response response);

        protected AbstractRequestWrapper(String objectId) {
            this.objectId = objectId;
        }

        void addToBatch(RequestBatch batch) {
            batch.add(this.request);
        }

        protected void setRequest(Request request) {
            this.request = request;
            request.setVersion(ServerProtocol.GRAPH_API_VERSION);
            request.setCallback(new Request.Callback() { // from class: com.facebook.internal.LikeActionController.AbstractRequestWrapper.1
                @Override // com.facebook.Request.Callback
                public void onCompleted(Response response) {
                    AbstractRequestWrapper.this.error = response.getError();
                    if (AbstractRequestWrapper.this.error != null) {
                        AbstractRequestWrapper.this.processError(AbstractRequestWrapper.this.error);
                    } else {
                        AbstractRequestWrapper.this.processSuccess(response);
                    }
                }
            });
        }

        protected void processError(FacebookRequestError error) {
            Logger.log(LoggingBehavior.REQUESTS, LikeActionController.TAG, "Error running request for object '%s' : %s", this.objectId, error);
        }
    }

    /* loaded from: classes.dex */
    private enum LikeDialogFeature implements FacebookDialog.DialogFeature {
        LIKE_DIALOG(NativeProtocol.PROTOCOL_VERSION_20140701);
        
        private int minVersion;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static LikeDialogFeature[] valuesCustom() {
            LikeDialogFeature[] valuesCustom = values();
            int length = valuesCustom.length;
            LikeDialogFeature[] likeDialogFeatureArr = new LikeDialogFeature[length];
            System.arraycopy(valuesCustom, 0, likeDialogFeatureArr, 0, length);
            return likeDialogFeatureArr;
        }

        LikeDialogFeature(int minVersion) {
            this.minVersion = minVersion;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public String getAction() {
            return NativeProtocol.ACTION_LIKE_DIALOG;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public int getMinVersion() {
            return this.minVersion;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LikeDialogBuilder extends FacebookDialog.Builder<LikeDialogBuilder> {
        private String objectId;

        public LikeDialogBuilder(Activity activity, String objectId) {
            super(activity);
            this.objectId = objectId;
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected EnumSet<? extends FacebookDialog.DialogFeature> getDialogFeatures() {
            return EnumSet.of(LikeDialogFeature.LIKE_DIALOG);
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected Bundle getMethodArguments() {
            Bundle methodArgs = new Bundle();
            methodArgs.putString("object_id", this.objectId);
            return methodArgs;
        }

        public FacebookDialog.PendingCall getAppCall() {
            return this.appCall;
        }

        public String getApplicationId() {
            return this.applicationId;
        }

        public String getWebFallbackUrl() {
            return getWebFallbackUrlInternal();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MRUCacheWorkItem implements Runnable {
        private static ArrayList<String> mruCachedItems = new ArrayList<>();
        private String cacheItem;
        private boolean shouldTrim;

        MRUCacheWorkItem(String cacheItem, boolean shouldTrim) {
            this.cacheItem = cacheItem;
            this.shouldTrim = shouldTrim;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.cacheItem != null) {
                mruCachedItems.remove(this.cacheItem);
                mruCachedItems.add(0, this.cacheItem);
            }
            if (this.shouldTrim && mruCachedItems.size() >= 128) {
                while (64 < mruCachedItems.size()) {
                    String cacheKey = mruCachedItems.remove(mruCachedItems.size() - 1);
                    LikeActionController.cache.remove(cacheKey);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SerializeToDiskWorkItem implements Runnable {
        private String cacheKey;
        private String controllerJson;

        SerializeToDiskWorkItem(String cacheKey, String controllerJson) {
            this.cacheKey = cacheKey;
            this.controllerJson = controllerJson;
        }

        @Override // java.lang.Runnable
        public void run() {
            LikeActionController.serializeToDiskSynchronously(this.cacheKey, this.controllerJson);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CreateLikeActionControllerWorkItem implements Runnable {
        private CreationCallback callback;
        private Context context;
        private String objectId;

        CreateLikeActionControllerWorkItem(Context context, String objectId, CreationCallback callback) {
            this.context = context;
            this.objectId = objectId;
            this.callback = callback;
        }

        @Override // java.lang.Runnable
        public void run() {
            LikeActionController.createControllerForObjectId(this.context, this.objectId, this.callback);
        }
    }
}
