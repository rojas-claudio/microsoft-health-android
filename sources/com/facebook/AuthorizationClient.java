package com.facebook;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Session;
import com.facebook.android.R;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.PlatformServiceClient;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AuthorizationClient implements Serializable {
    static final String EVENT_EXTRAS_DEFAULT_AUDIENCE = "default_audience";
    static final String EVENT_EXTRAS_IS_LEGACY = "is_legacy";
    static final String EVENT_EXTRAS_LOGIN_BEHAVIOR = "login_behavior";
    static final String EVENT_EXTRAS_MISSING_INTERNET_PERMISSION = "no_internet_permission";
    static final String EVENT_EXTRAS_NEW_PERMISSIONS = "new_permissions";
    static final String EVENT_EXTRAS_NOT_TRIED = "not_tried";
    static final String EVENT_EXTRAS_PERMISSIONS = "permissions";
    static final String EVENT_EXTRAS_REQUEST_CODE = "request_code";
    static final String EVENT_EXTRAS_TRY_LEGACY = "try_legacy";
    static final String EVENT_EXTRAS_TRY_LOGIN_ACTIVITY = "try_login_activity";
    static final String EVENT_NAME_LOGIN_COMPLETE = "fb_mobile_login_complete";
    private static final String EVENT_NAME_LOGIN_METHOD_COMPLETE = "fb_mobile_login_method_complete";
    private static final String EVENT_NAME_LOGIN_METHOD_START = "fb_mobile_login_method_start";
    static final String EVENT_NAME_LOGIN_START = "fb_mobile_login_start";
    static final String EVENT_PARAM_AUTH_LOGGER_ID = "0_auth_logger_id";
    static final String EVENT_PARAM_ERROR_CODE = "4_error_code";
    static final String EVENT_PARAM_ERROR_MESSAGE = "5_error_message";
    static final String EVENT_PARAM_EXTRAS = "6_extras";
    static final String EVENT_PARAM_LOGIN_RESULT = "2_result";
    static final String EVENT_PARAM_METHOD = "3_method";
    private static final String EVENT_PARAM_METHOD_RESULT_SKIPPED = "skipped";
    static final String EVENT_PARAM_TIMESTAMP = "1_timestamp_ms";
    private static final String TAG = "Facebook-AuthorizationClient";
    private static final String WEB_VIEW_AUTH_HANDLER_STORE = "com.facebook.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY";
    private static final String WEB_VIEW_AUTH_HANDLER_TOKEN_KEY = "TOKEN";
    private static final long serialVersionUID = 1;
    private transient AppEventsLogger appEventsLogger;
    transient BackgroundProcessingListener backgroundProcessingListener;
    transient boolean checkedInternetPermission;
    transient Context context;
    AuthHandler currentHandler;
    List<AuthHandler> handlersToTry;
    Map<String, String> loggingExtras;
    transient OnCompletedListener onCompletedListener;
    AuthorizationRequest pendingRequest;
    transient StartActivityDelegate startActivityDelegate;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface BackgroundProcessingListener {
        void onBackgroundProcessingStarted();

        void onBackgroundProcessingStopped();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnCompletedListener {
        void onCompleted(Result result);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface StartActivityDelegate {
        Activity getActivityContext();

        void startActivityForResult(Intent intent, int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setContext(Context context) {
        this.context = context;
        this.startActivityDelegate = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setContext(final Activity activity) {
        this.context = activity;
        this.startActivityDelegate = new StartActivityDelegate() { // from class: com.facebook.AuthorizationClient.1
            @Override // com.facebook.AuthorizationClient.StartActivityDelegate
            public void startActivityForResult(Intent intent, int requestCode) {
                activity.startActivityForResult(intent, requestCode);
            }

            @Override // com.facebook.AuthorizationClient.StartActivityDelegate
            public Activity getActivityContext() {
                return activity;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startOrContinueAuth(AuthorizationRequest request) {
        if (getInProgress()) {
            continueAuth();
        } else {
            authorize(request);
        }
    }

    void authorize(AuthorizationRequest request) {
        if (request != null) {
            if (this.pendingRequest != null) {
                throw new FacebookException("Attempted to authorize while a request is pending.");
            }
            if (!request.needsNewTokenValidation() || checkInternetPermission()) {
                this.pendingRequest = request;
                this.handlersToTry = getHandlerTypes(request);
                tryNextHandler();
            }
        }
    }

    void continueAuth() {
        if (this.pendingRequest == null || this.currentHandler == null) {
            throw new FacebookException("Attempted to continue authorization without a pending request.");
        }
        if (this.currentHandler.needsRestart()) {
            this.currentHandler.cancel();
            tryCurrentHandler();
        }
    }

    boolean getInProgress() {
        return (this.pendingRequest == null || this.currentHandler == null) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void cancelCurrentHandler() {
        if (this.currentHandler != null) {
            this.currentHandler.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.pendingRequest.getRequestCode()) {
            return this.currentHandler.onActivityResult(requestCode, resultCode, data);
        }
        return false;
    }

    private List<AuthHandler> getHandlerTypes(AuthorizationRequest request) {
        ArrayList<AuthHandler> handlers = new ArrayList<>();
        SessionLoginBehavior behavior = request.getLoginBehavior();
        if (behavior.allowsKatanaAuth()) {
            if (!request.isLegacy()) {
                handlers.add(new GetTokenAuthHandler());
            }
            handlers.add(new KatanaProxyAuthHandler());
        }
        if (behavior.allowsWebViewAuth()) {
            handlers.add(new WebViewAuthHandler());
        }
        return handlers;
    }

    boolean checkInternetPermission() {
        if (this.checkedInternetPermission) {
            return true;
        }
        int permissionCheck = checkPermission("android.permission.INTERNET");
        if (permissionCheck != 0) {
            String errorType = this.context.getString(R.string.com_facebook_internet_permission_error_title);
            String errorDescription = this.context.getString(R.string.com_facebook_internet_permission_error_message);
            complete(Result.createErrorResult(this.pendingRequest, errorType, errorDescription));
            return false;
        }
        this.checkedInternetPermission = true;
        return true;
    }

    void tryNextHandler() {
        if (this.currentHandler != null) {
            logAuthorizationMethodComplete(this.currentHandler.getNameForLogging(), EVENT_PARAM_METHOD_RESULT_SKIPPED, null, null, this.currentHandler.methodLoggingExtras);
        }
        while (this.handlersToTry != null && !this.handlersToTry.isEmpty()) {
            this.currentHandler = this.handlersToTry.remove(0);
            boolean started = tryCurrentHandler();
            if (started) {
                return;
            }
        }
        if (this.pendingRequest != null) {
            completeWithFailure();
        }
    }

    private void completeWithFailure() {
        complete(Result.createErrorResult(this.pendingRequest, "Login attempt failed.", null));
    }

    private void addLoggingExtra(String key, String value, boolean accumulate) {
        if (this.loggingExtras == null) {
            this.loggingExtras = new HashMap();
        }
        if (this.loggingExtras.containsKey(key) && accumulate) {
            value = String.valueOf(this.loggingExtras.get(key)) + "," + value;
        }
        this.loggingExtras.put(key, value);
    }

    boolean tryCurrentHandler() {
        boolean tried = false;
        if (this.currentHandler.needsInternetPermission() && !checkInternetPermission()) {
            addLoggingExtra(EVENT_EXTRAS_MISSING_INTERNET_PERMISSION, AppEventsConstants.EVENT_PARAM_VALUE_YES, false);
        } else {
            tried = this.currentHandler.tryAuthorize(this.pendingRequest);
            if (tried) {
                logAuthorizationMethodStart(this.currentHandler.getNameForLogging());
            } else {
                addLoggingExtra(EVENT_EXTRAS_NOT_TRIED, this.currentHandler.getNameForLogging(), true);
            }
        }
        return tried;
    }

    void completeAndValidate(Result outcome) {
        if (outcome.token != null && this.pendingRequest.needsNewTokenValidation()) {
            validateSameFbidAndFinish(outcome);
        } else {
            complete(outcome);
        }
    }

    void complete(Result outcome) {
        if (this.currentHandler != null) {
            logAuthorizationMethodComplete(this.currentHandler.getNameForLogging(), outcome, this.currentHandler.methodLoggingExtras);
        }
        if (this.loggingExtras != null) {
            outcome.loggingExtras = this.loggingExtras;
        }
        this.handlersToTry = null;
        this.currentHandler = null;
        this.pendingRequest = null;
        this.loggingExtras = null;
        notifyOnCompleteListener(outcome);
    }

    OnCompletedListener getOnCompletedListener() {
        return this.onCompletedListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        this.onCompletedListener = onCompletedListener;
    }

    BackgroundProcessingListener getBackgroundProcessingListener() {
        return this.backgroundProcessingListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackgroundProcessingListener(BackgroundProcessingListener backgroundProcessingListener) {
        this.backgroundProcessingListener = backgroundProcessingListener;
    }

    StartActivityDelegate getStartActivityDelegate() {
        if (this.startActivityDelegate != null) {
            return this.startActivityDelegate;
        }
        if (this.pendingRequest != null) {
            return new StartActivityDelegate() { // from class: com.facebook.AuthorizationClient.2
                @Override // com.facebook.AuthorizationClient.StartActivityDelegate
                public void startActivityForResult(Intent intent, int requestCode) {
                    AuthorizationClient.this.pendingRequest.getStartActivityDelegate().startActivityForResult(intent, requestCode);
                }

                @Override // com.facebook.AuthorizationClient.StartActivityDelegate
                public Activity getActivityContext() {
                    return AuthorizationClient.this.pendingRequest.getStartActivityDelegate().getActivityContext();
                }
            };
        }
        return null;
    }

    int checkPermission(String permission) {
        return this.context.checkCallingOrSelfPermission(permission);
    }

    void validateSameFbidAndFinish(Result pendingResult) {
        if (pendingResult.token == null) {
            throw new FacebookException("Can't validate without a token");
        }
        RequestBatch batch = createReauthValidationBatch(pendingResult);
        notifyBackgroundProcessingStart();
        batch.executeAsync();
    }

    RequestBatch createReauthValidationBatch(final Result pendingResult) {
        final ArrayList<String> fbids = new ArrayList<>();
        final ArrayList<String> grantedPermissions = new ArrayList<>();
        final ArrayList<String> declinedPermissions = new ArrayList<>();
        String newToken = pendingResult.token.getToken();
        Request.Callback meCallback = new Request.Callback() { // from class: com.facebook.AuthorizationClient.3
            @Override // com.facebook.Request.Callback
            public void onCompleted(Response response) {
                try {
                    GraphUser user = (GraphUser) response.getGraphObjectAs(GraphUser.class);
                    if (user != null) {
                        fbids.add(user.getId());
                    }
                } catch (Exception e) {
                }
            }
        };
        String validateSameFbidAsToken = this.pendingRequest.getPreviousAccessToken();
        Request requestCurrentTokenMe = createGetProfileIdRequest(validateSameFbidAsToken);
        requestCurrentTokenMe.setCallback(meCallback);
        Request requestNewTokenMe = createGetProfileIdRequest(newToken);
        requestNewTokenMe.setCallback(meCallback);
        Request requestCurrentTokenPermissions = createGetPermissionsRequest(validateSameFbidAsToken);
        requestCurrentTokenPermissions.setCallback(new Request.Callback() { // from class: com.facebook.AuthorizationClient.4
            @Override // com.facebook.Request.Callback
            public void onCompleted(Response response) {
                try {
                    Session.PermissionsPair permissionsPair = Session.handlePermissionResponse(response);
                    if (permissionsPair != null) {
                        grantedPermissions.addAll(permissionsPair.getGrantedPermissions());
                        declinedPermissions.addAll(permissionsPair.getDeclinedPermissions());
                    }
                } catch (Exception e) {
                }
            }
        });
        RequestBatch batch = new RequestBatch(requestCurrentTokenMe, requestNewTokenMe, requestCurrentTokenPermissions);
        batch.setBatchApplicationId(this.pendingRequest.getApplicationId());
        batch.addCallback(new RequestBatch.Callback() { // from class: com.facebook.AuthorizationClient.5
            @Override // com.facebook.RequestBatch.Callback
            public void onBatchCompleted(RequestBatch batch2) {
                Result result;
                try {
                    if (fbids.size() == 2 && fbids.get(0) != null && fbids.get(1) != null && ((String) fbids.get(0)).equals(fbids.get(1))) {
                        AccessToken tokenWithPermissions = AccessToken.createFromTokenWithRefreshedPermissions(pendingResult.token, grantedPermissions, declinedPermissions);
                        result = Result.createTokenResult(AuthorizationClient.this.pendingRequest, tokenWithPermissions);
                    } else {
                        result = Result.createErrorResult(AuthorizationClient.this.pendingRequest, "User logged in as different Facebook user.", null);
                    }
                    AuthorizationClient.this.complete(result);
                } catch (Exception ex) {
                    AuthorizationClient.this.complete(Result.createErrorResult(AuthorizationClient.this.pendingRequest, "Caught exception", ex.getMessage()));
                } finally {
                    AuthorizationClient.this.notifyBackgroundProcessingStop();
                }
            }
        });
        return batch;
    }

    Request createGetPermissionsRequest(String accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("access_token", accessToken);
        return new Request(null, "me/permissions", parameters, HttpMethod.GET, null);
    }

    Request createGetProfileIdRequest(String accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        parameters.putString("access_token", accessToken);
        return new Request(null, "me", parameters, HttpMethod.GET, null);
    }

    private AppEventsLogger getAppEventsLogger() {
        if (this.appEventsLogger == null || !this.appEventsLogger.getApplicationId().equals(this.pendingRequest.getApplicationId())) {
            this.appEventsLogger = AppEventsLogger.newLogger(this.context, this.pendingRequest.getApplicationId());
        }
        return this.appEventsLogger;
    }

    private void notifyOnCompleteListener(Result outcome) {
        if (this.onCompletedListener != null) {
            this.onCompletedListener.onCompleted(outcome);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBackgroundProcessingStart() {
        if (this.backgroundProcessingListener != null) {
            this.backgroundProcessingListener.onBackgroundProcessingStarted();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyBackgroundProcessingStop() {
        if (this.backgroundProcessingListener != null) {
            this.backgroundProcessingListener.onBackgroundProcessingStopped();
        }
    }

    private void logAuthorizationMethodStart(String method) {
        Bundle bundle = newAuthorizationLoggingBundle(this.pendingRequest.getAuthId());
        bundle.putLong(EVENT_PARAM_TIMESTAMP, System.currentTimeMillis());
        bundle.putString(EVENT_PARAM_METHOD, method);
        getAppEventsLogger().logSdkEvent(EVENT_NAME_LOGIN_METHOD_START, null, bundle);
    }

    private void logAuthorizationMethodComplete(String method, Result result, Map<String, String> loggingExtras) {
        logAuthorizationMethodComplete(method, result.code.getLoggingValue(), result.errorMessage, result.errorCode, loggingExtras);
    }

    private void logAuthorizationMethodComplete(String method, String result, String errorMessage, String errorCode, Map<String, String> loggingExtras) {
        Bundle bundle;
        if (this.pendingRequest == null) {
            bundle = newAuthorizationLoggingBundle("");
            bundle.putString(EVENT_PARAM_LOGIN_RESULT, Result.Code.ERROR.getLoggingValue());
            bundle.putString(EVENT_PARAM_ERROR_MESSAGE, "Unexpected call to logAuthorizationMethodComplete with null pendingRequest.");
        } else {
            bundle = newAuthorizationLoggingBundle(this.pendingRequest.getAuthId());
            if (result != null) {
                bundle.putString(EVENT_PARAM_LOGIN_RESULT, result);
            }
            if (errorMessage != null) {
                bundle.putString(EVENT_PARAM_ERROR_MESSAGE, errorMessage);
            }
            if (errorCode != null) {
                bundle.putString(EVENT_PARAM_ERROR_CODE, errorCode);
            }
            if (loggingExtras != null && !loggingExtras.isEmpty()) {
                JSONObject jsonObject = new JSONObject(loggingExtras);
                bundle.putString(EVENT_PARAM_EXTRAS, jsonObject.toString());
            }
        }
        bundle.putString(EVENT_PARAM_METHOD, method);
        bundle.putLong(EVENT_PARAM_TIMESTAMP, System.currentTimeMillis());
        getAppEventsLogger().logSdkEvent(EVENT_NAME_LOGIN_METHOD_COMPLETE, null, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bundle newAuthorizationLoggingBundle(String authLoggerId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EVENT_PARAM_TIMESTAMP, System.currentTimeMillis());
        bundle.putString(EVENT_PARAM_AUTH_LOGGER_ID, authLoggerId);
        bundle.putString(EVENT_PARAM_METHOD, "");
        bundle.putString(EVENT_PARAM_LOGIN_RESULT, "");
        bundle.putString(EVENT_PARAM_ERROR_MESSAGE, "");
        bundle.putString(EVENT_PARAM_ERROR_CODE, "");
        bundle.putString(EVENT_PARAM_EXTRAS, "");
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public abstract class AuthHandler implements Serializable {
        private static final long serialVersionUID = 1;
        Map<String, String> methodLoggingExtras;

        abstract String getNameForLogging();

        abstract boolean tryAuthorize(AuthorizationRequest authorizationRequest);

        AuthHandler() {
        }

        boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            return false;
        }

        boolean needsRestart() {
            return false;
        }

        boolean needsInternetPermission() {
            return false;
        }

        void cancel() {
        }

        protected void addLoggingExtra(String key, Object value) {
            if (this.methodLoggingExtras == null) {
                this.methodLoggingExtras = new HashMap();
            }
            this.methodLoggingExtras.put(key, value == null ? null : value.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class WebViewAuthHandler extends AuthHandler {
        private static final long serialVersionUID = 1;
        private String applicationId;
        private String e2e;
        private transient WebDialog loginDialog;

        WebViewAuthHandler() {
            super();
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        String getNameForLogging() {
            return "web_view";
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        boolean needsRestart() {
            return true;
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        boolean needsInternetPermission() {
            return true;
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        void cancel() {
            if (this.loginDialog != null) {
                this.loginDialog.dismiss();
                this.loginDialog = null;
            }
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        boolean tryAuthorize(final AuthorizationRequest request) {
            this.applicationId = request.getApplicationId();
            Bundle parameters = new Bundle();
            if (!Utility.isNullOrEmpty(request.getPermissions())) {
                String scope = TextUtils.join(",", request.getPermissions());
                parameters.putString("scope", scope);
                addLoggingExtra("scope", scope);
            }
            SessionDefaultAudience audience = request.getDefaultAudience();
            parameters.putString("default_audience", audience.getNativeProtocolAudience());
            String previousToken = request.getPreviousAccessToken();
            if (!Utility.isNullOrEmpty(previousToken) && previousToken.equals(loadCookieToken())) {
                parameters.putString("access_token", previousToken);
                addLoggingExtra("access_token", AppEventsConstants.EVENT_PARAM_VALUE_YES);
            } else {
                Utility.clearFacebookCookies(AuthorizationClient.this.context);
                addLoggingExtra("access_token", AppEventsConstants.EVENT_PARAM_VALUE_NO);
            }
            WebDialog.OnCompleteListener listener = new WebDialog.OnCompleteListener() { // from class: com.facebook.AuthorizationClient.WebViewAuthHandler.1
                @Override // com.facebook.widget.WebDialog.OnCompleteListener
                public void onComplete(Bundle values, FacebookException error) {
                    WebViewAuthHandler.this.onWebDialogComplete(request, values, error);
                }
            };
            this.e2e = AuthorizationClient.access$0();
            addLoggingExtra("e2e", this.e2e);
            WebDialog.Builder builder = (WebDialog.Builder) new AuthDialogBuilder(AuthorizationClient.this.getStartActivityDelegate().getActivityContext(), this.applicationId, parameters).setE2E(this.e2e).setIsRerequest(request.isRerequest()).setOnCompleteListener(listener);
            this.loginDialog = builder.build();
            this.loginDialog.show();
            return true;
        }

        void onWebDialogComplete(AuthorizationRequest request, Bundle values, FacebookException error) {
            Result outcome;
            if (values != null) {
                if (values.containsKey("e2e")) {
                    this.e2e = values.getString("e2e");
                }
                AccessToken token = AccessToken.createFromWebBundle(request.getPermissions(), values, AccessTokenSource.WEB_VIEW);
                outcome = Result.createTokenResult(AuthorizationClient.this.pendingRequest, token);
                CookieSyncManager syncManager = CookieSyncManager.createInstance(AuthorizationClient.this.context);
                syncManager.sync();
                saveCookieToken(token.getToken());
            } else if (error instanceof FacebookOperationCanceledException) {
                outcome = Result.createCancelResult(AuthorizationClient.this.pendingRequest, "User canceled log in.");
            } else {
                this.e2e = null;
                String errorCode = null;
                String errorMessage = error.getMessage();
                if (error instanceof FacebookServiceException) {
                    FacebookRequestError requestError = ((FacebookServiceException) error).getRequestError();
                    errorCode = String.format("%d", Integer.valueOf(requestError.getErrorCode()));
                    errorMessage = requestError.toString();
                }
                outcome = Result.createErrorResult(AuthorizationClient.this.pendingRequest, null, errorMessage, errorCode);
            }
            if (!Utility.isNullOrEmpty(this.e2e)) {
                AuthorizationClient.this.logWebLoginCompleted(this.applicationId, this.e2e);
            }
            AuthorizationClient.this.completeAndValidate(outcome);
        }

        private void saveCookieToken(String token) {
            Context context = AuthorizationClient.this.getStartActivityDelegate().getActivityContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(AuthorizationClient.WEB_VIEW_AUTH_HANDLER_STORE, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AuthorizationClient.WEB_VIEW_AUTH_HANDLER_TOKEN_KEY, token);
            if (!editor.commit()) {
                Utility.logd(AuthorizationClient.TAG, "Could not update saved web view auth handler token.");
            }
        }

        private String loadCookieToken() {
            Context context = AuthorizationClient.this.getStartActivityDelegate().getActivityContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(AuthorizationClient.WEB_VIEW_AUTH_HANDLER_STORE, 0);
            return sharedPreferences.getString(AuthorizationClient.WEB_VIEW_AUTH_HANDLER_TOKEN_KEY, "");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class GetTokenAuthHandler extends AuthHandler {
        private static final long serialVersionUID = 1;
        private transient GetTokenClient getTokenClient;

        GetTokenAuthHandler() {
            super();
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        String getNameForLogging() {
            return "get_token";
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        void cancel() {
            if (this.getTokenClient != null) {
                this.getTokenClient.cancel();
                this.getTokenClient = null;
            }
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        boolean needsRestart() {
            return this.getTokenClient == null;
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        boolean tryAuthorize(final AuthorizationRequest request) {
            this.getTokenClient = new GetTokenClient(AuthorizationClient.this.context, request.getApplicationId());
            if (this.getTokenClient.start()) {
                AuthorizationClient.this.notifyBackgroundProcessingStart();
                PlatformServiceClient.CompletedListener callback = new PlatformServiceClient.CompletedListener() { // from class: com.facebook.AuthorizationClient.GetTokenAuthHandler.1
                    @Override // com.facebook.internal.PlatformServiceClient.CompletedListener
                    public void completed(Bundle result) {
                        GetTokenAuthHandler.this.getTokenCompleted(request, result);
                    }
                };
                this.getTokenClient.setCompletedListener(callback);
                return true;
            }
            return false;
        }

        void getTokenCompleted(AuthorizationRequest request, Bundle result) {
            this.getTokenClient = null;
            AuthorizationClient.this.notifyBackgroundProcessingStop();
            if (result != null) {
                ArrayList<String> currentPermissions = result.getStringArrayList(NativeProtocol.EXTRA_PERMISSIONS);
                List<String> permissions = request.getPermissions();
                if (currentPermissions != null && (permissions == null || currentPermissions.containsAll(permissions))) {
                    AccessToken token = AccessToken.createFromNativeLogin(result, AccessTokenSource.FACEBOOK_APPLICATION_SERVICE);
                    Result outcome = Result.createTokenResult(AuthorizationClient.this.pendingRequest, token);
                    AuthorizationClient.this.completeAndValidate(outcome);
                    return;
                }
                List<String> newPermissions = new ArrayList<>();
                for (String permission : permissions) {
                    if (!currentPermissions.contains(permission)) {
                        newPermissions.add(permission);
                    }
                }
                if (!newPermissions.isEmpty()) {
                    addLoggingExtra(AuthorizationClient.EVENT_EXTRAS_NEW_PERMISSIONS, TextUtils.join(",", newPermissions));
                }
                request.setPermissions(newPermissions);
            }
            AuthorizationClient.this.tryNextHandler();
        }
    }

    /* loaded from: classes.dex */
    abstract class KatanaAuthHandler extends AuthHandler {
        private static final long serialVersionUID = 1;

        KatanaAuthHandler() {
            super();
        }

        protected boolean tryIntent(Intent intent, int requestCode) {
            if (intent == null) {
                return false;
            }
            try {
                AuthorizationClient.this.getStartActivityDelegate().startActivityForResult(intent, requestCode);
                return true;
            } catch (ActivityNotFoundException e) {
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class KatanaProxyAuthHandler extends KatanaAuthHandler {
        private static final long serialVersionUID = 1;
        private String applicationId;

        KatanaProxyAuthHandler() {
            super();
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        String getNameForLogging() {
            return "katana_proxy_auth";
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        boolean tryAuthorize(AuthorizationRequest request) {
            this.applicationId = request.getApplicationId();
            String e2e = AuthorizationClient.access$0();
            Intent intent = NativeProtocol.createProxyAuthIntent(AuthorizationClient.this.context, request.getApplicationId(), request.getPermissions(), e2e, request.isRerequest(), request.getDefaultAudience());
            addLoggingExtra("e2e", e2e);
            return tryIntent(intent, request.getRequestCode());
        }

        @Override // com.facebook.AuthorizationClient.AuthHandler
        boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            Result outcome;
            if (data == null) {
                outcome = Result.createCancelResult(AuthorizationClient.this.pendingRequest, "Operation canceled");
            } else if (resultCode == 0) {
                outcome = Result.createCancelResult(AuthorizationClient.this.pendingRequest, data.getStringExtra("error"));
            } else if (resultCode != -1) {
                outcome = Result.createErrorResult(AuthorizationClient.this.pendingRequest, "Unexpected resultCode from authorization.", null);
            } else {
                outcome = handleResultOk(data);
            }
            if (outcome != null) {
                AuthorizationClient.this.completeAndValidate(outcome);
                return true;
            }
            AuthorizationClient.this.tryNextHandler();
            return true;
        }

        private Result handleResultOk(Intent data) {
            Bundle extras = data.getExtras();
            String error = extras.getString("error");
            if (error == null) {
                error = extras.getString("error_type");
            }
            String errorCode = extras.getString("error_code");
            String errorMessage = extras.getString("error_message");
            if (errorMessage == null) {
                errorMessage = extras.getString("error_description");
            }
            String e2e = extras.getString("e2e");
            if (!Utility.isNullOrEmpty(e2e)) {
                AuthorizationClient.this.logWebLoginCompleted(this.applicationId, e2e);
            }
            if (error == null && errorCode == null && errorMessage == null) {
                AccessToken token = AccessToken.createFromWebBundle(AuthorizationClient.this.pendingRequest.getPermissions(), extras, AccessTokenSource.FACEBOOK_APPLICATION_WEB);
                return Result.createTokenResult(AuthorizationClient.this.pendingRequest, token);
            } else if (ServerProtocol.errorsProxyAuthDisabled.contains(error)) {
                return null;
            } else {
                if (ServerProtocol.errorsUserCanceled.contains(error)) {
                    return Result.createCancelResult(AuthorizationClient.this.pendingRequest, null);
                }
                return Result.createErrorResult(AuthorizationClient.this.pendingRequest, error, errorMessage, errorCode);
            }
        }
    }

    static /* synthetic */ String access$0() {
        return getE2E();
    }

    private static String getE2E() {
        JSONObject e2e = new JSONObject();
        try {
            e2e.put("init", System.currentTimeMillis());
        } catch (JSONException e) {
        }
        return e2e.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWebLoginCompleted(String applicationId, String e2e) {
        AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(this.context, applicationId);
        Bundle parameters = new Bundle();
        parameters.putString(AnalyticsEvents.PARAMETER_WEB_LOGIN_E2E, e2e);
        parameters.putLong(AnalyticsEvents.PARAMETER_WEB_LOGIN_SWITCHBACK_TIME, System.currentTimeMillis());
        parameters.putString("app_id", applicationId);
        appEventsLogger.logSdkEvent(AnalyticsEvents.EVENT_WEB_LOGIN_COMPLETE, null, parameters);
    }

    /* loaded from: classes.dex */
    static class AuthDialogBuilder extends WebDialog.Builder {
        private static final String OAUTH_DIALOG = "oauth";
        static final String REDIRECT_URI = "fbconnect://success";
        private String e2e;
        private boolean isRerequest;

        public AuthDialogBuilder(Context context, String applicationId, Bundle parameters) {
            super(context, applicationId, OAUTH_DIALOG, parameters);
        }

        public AuthDialogBuilder setE2E(String e2e) {
            this.e2e = e2e;
            return this;
        }

        public AuthDialogBuilder setIsRerequest(boolean isRerequest) {
            this.isRerequest = isRerequest;
            return this;
        }

        @Override // com.facebook.widget.WebDialog.Builder, com.facebook.widget.WebDialog.BuilderBase
        public WebDialog build() {
            Bundle parameters = getParameters();
            parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, "fbconnect://success");
            parameters.putString("client_id", getApplicationId());
            parameters.putString("e2e", this.e2e);
            parameters.putString(ServerProtocol.DIALOG_PARAM_RESPONSE_TYPE, ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN);
            parameters.putString(ServerProtocol.DIALOG_PARAM_RETURN_SCOPES, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
            if (this.isRerequest && !Settings.getPlatformCompatibilityEnabled()) {
                parameters.putString(ServerProtocol.DIALOG_PARAM_AUTH_TYPE, ServerProtocol.DIALOG_REREQUEST_AUTH_TYPE);
            }
            return new WebDialog(getContext(), OAUTH_DIALOG, parameters, getTheme(), getListener());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class AuthorizationRequest implements Serializable {
        private static final long serialVersionUID = 1;
        private final String applicationId;
        private final String authId;
        private final SessionDefaultAudience defaultAudience;
        private boolean isLegacy;
        private boolean isRerequest = false;
        private final SessionLoginBehavior loginBehavior;
        private List<String> permissions;
        private final String previousAccessToken;
        private final int requestCode;
        private final transient StartActivityDelegate startActivityDelegate;

        /* JADX INFO: Access modifiers changed from: package-private */
        public AuthorizationRequest(SessionLoginBehavior loginBehavior, int requestCode, boolean isLegacy, List<String> permissions, SessionDefaultAudience defaultAudience, String applicationId, String validateSameFbidAsToken, StartActivityDelegate startActivityDelegate, String authId) {
            this.isLegacy = false;
            this.loginBehavior = loginBehavior;
            this.requestCode = requestCode;
            this.isLegacy = isLegacy;
            this.permissions = permissions;
            this.defaultAudience = defaultAudience;
            this.applicationId = applicationId;
            this.previousAccessToken = validateSameFbidAsToken;
            this.startActivityDelegate = startActivityDelegate;
            this.authId = authId;
        }

        StartActivityDelegate getStartActivityDelegate() {
            return this.startActivityDelegate;
        }

        List<String> getPermissions() {
            return this.permissions;
        }

        void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }

        SessionLoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }

        int getRequestCode() {
            return this.requestCode;
        }

        SessionDefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }

        String getApplicationId() {
            return this.applicationId;
        }

        boolean isLegacy() {
            return this.isLegacy;
        }

        void setIsLegacy(boolean isLegacy) {
            this.isLegacy = isLegacy;
        }

        String getPreviousAccessToken() {
            return this.previousAccessToken;
        }

        boolean needsNewTokenValidation() {
            return (this.previousAccessToken == null || this.isLegacy) ? false : true;
        }

        String getAuthId() {
            return this.authId;
        }

        boolean isRerequest() {
            return this.isRerequest;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setRerequest(boolean isRerequest) {
            this.isRerequest = isRerequest;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Result implements Serializable {
        private static final long serialVersionUID = 1;
        final Code code;
        final String errorCode;
        final String errorMessage;
        Map<String, String> loggingExtras;
        final AuthorizationRequest request;
        final AccessToken token;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public enum Code {
            SUCCESS(Response.SUCCESS_KEY),
            CANCEL(FacebookDialog.COMPLETION_GESTURE_CANCEL),
            ERROR("error");
            
            private final String loggingValue;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static Code[] valuesCustom() {
                Code[] valuesCustom = values();
                int length = valuesCustom.length;
                Code[] codeArr = new Code[length];
                System.arraycopy(valuesCustom, 0, codeArr, 0, length);
                return codeArr;
            }

            Code(String loggingValue) {
                this.loggingValue = loggingValue;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public String getLoggingValue() {
                return this.loggingValue;
            }
        }

        private Result(AuthorizationRequest request, Code code, AccessToken token, String errorMessage, String errorCode) {
            this.request = request;
            this.token = token;
            this.errorMessage = errorMessage;
            this.code = code;
            this.errorCode = errorCode;
        }

        static Result createTokenResult(AuthorizationRequest request, AccessToken token) {
            return new Result(request, Code.SUCCESS, token, null, null);
        }

        static Result createCancelResult(AuthorizationRequest request, String message) {
            return new Result(request, Code.CANCEL, null, message, null);
        }

        static Result createErrorResult(AuthorizationRequest request, String errorType, String errorDescription) {
            return createErrorResult(request, errorType, errorDescription, null);
        }

        static Result createErrorResult(AuthorizationRequest request, String errorType, String errorDescription, String errorCode) {
            String message = TextUtils.join(": ", Utility.asListNoNulls(errorType, errorDescription));
            return new Result(request, Code.ERROR, null, message, errorCode);
        }
    }
}
