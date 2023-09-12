package com.facebook;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.AuthorizationClient;
import com.facebook.Request;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.SessionAuthorizationType;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class Session implements Serializable {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$facebook$SessionState = null;
    public static final String ACTION_ACTIVE_SESSION_CLOSED = "com.facebook.sdk.ACTIVE_SESSION_CLOSED";
    public static final String ACTION_ACTIVE_SESSION_OPENED = "com.facebook.sdk.ACTIVE_SESSION_OPENED";
    public static final String ACTION_ACTIVE_SESSION_SET = "com.facebook.sdk.ACTIVE_SESSION_SET";
    public static final String ACTION_ACTIVE_SESSION_UNSET = "com.facebook.sdk.ACTIVE_SESSION_UNSET";
    private static final String AUTH_BUNDLE_SAVE_KEY = "com.facebook.sdk.Session.authBundleKey";
    public static final int DEFAULT_AUTHORIZE_ACTIVITY_CODE = 64206;
    private static final String MANAGE_PERMISSION_PREFIX = "manage";
    private static final String PUBLISH_PERMISSION_PREFIX = "publish";
    private static final String SESSION_BUNDLE_SAVE_KEY = "com.facebook.sdk.Session.saveSessionKey";
    private static final int TOKEN_EXTEND_RETRY_SECONDS = 3600;
    private static final int TOKEN_EXTEND_THRESHOLD_SECONDS = 86400;
    public static final String WEB_VIEW_ERROR_CODE_KEY = "com.facebook.sdk.WebViewErrorCode";
    public static final String WEB_VIEW_FAILING_URL_KEY = "com.facebook.sdk.FailingUrl";
    private static Session activeSession = null;
    private static final long serialVersionUID = 1;
    private static volatile Context staticContext;
    private AppEventsLogger appEventsLogger;
    private String applicationId;
    private volatile Bundle authorizationBundle;
    private AuthorizationClient authorizationClient;
    private AutoPublishAsyncTask autoPublishAsyncTask;
    private final List<StatusCallback> callbacks;
    private volatile TokenRefreshRequest currentTokenRefreshRequest;
    private Handler handler;
    private Date lastAttemptedTokenExtendDate;
    private final Object lock;
    private AuthorizationRequest pendingAuthorizationRequest;
    private SessionState state;
    private TokenCachingStrategy tokenCachingStrategy;
    private AccessToken tokenInfo;
    public static final String TAG = Session.class.getCanonicalName();
    private static final Object STATIC_LOCK = new Object();
    private static final Set<String> OTHER_PUBLISH_PERMISSIONS = new HashSet<String>() { // from class: com.facebook.Session.1
        {
            add("ads_management");
            add("create_event");
            add("rsvp_event");
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface StartActivityDelegate {
        Activity getActivityContext();

        void startActivityForResult(Intent intent, int i);
    }

    /* loaded from: classes.dex */
    public interface StatusCallback {
        void call(Session session, SessionState sessionState, Exception exc);
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$facebook$SessionState() {
        int[] iArr = $SWITCH_TABLE$com$facebook$SessionState;
        if (iArr == null) {
            iArr = new int[SessionState.valuesCustom().length];
            try {
                iArr[SessionState.CLOSED.ordinal()] = 7;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SessionState.CLOSED_LOGIN_FAILED.ordinal()] = 6;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SessionState.CREATED.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[SessionState.CREATED_TOKEN_LOADED.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[SessionState.OPENED.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[SessionState.OPENED_TOKEN_UPDATED.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[SessionState.OPENING.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            $SWITCH_TABLE$com$facebook$SessionState = iArr;
        }
        return iArr;
    }

    /* loaded from: classes.dex */
    private static class SerializationProxyV1 implements Serializable {
        private static final long serialVersionUID = 7663436173185080063L;
        private final String applicationId;
        private final Date lastAttemptedTokenExtendDate;
        private final AuthorizationRequest pendingAuthorizationRequest;
        private final boolean shouldAutoPublish;
        private final SessionState state;
        private final AccessToken tokenInfo;

        SerializationProxyV1(String applicationId, SessionState state, AccessToken tokenInfo, Date lastAttemptedTokenExtendDate, boolean shouldAutoPublish, AuthorizationRequest pendingAuthorizationRequest) {
            this.applicationId = applicationId;
            this.state = state;
            this.tokenInfo = tokenInfo;
            this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
            this.shouldAutoPublish = shouldAutoPublish;
            this.pendingAuthorizationRequest = pendingAuthorizationRequest;
        }

        private Object readResolve() {
            return new Session(this.applicationId, this.state, this.tokenInfo, this.lastAttemptedTokenExtendDate, this.shouldAutoPublish, this.pendingAuthorizationRequest, (Session) null);
        }
    }

    /* loaded from: classes.dex */
    private static class SerializationProxyV2 implements Serializable {
        private static final long serialVersionUID = 7663436173185080064L;
        private final String applicationId;
        private final Date lastAttemptedTokenExtendDate;
        private final AuthorizationRequest pendingAuthorizationRequest;
        private final Set<String> requestedPermissions;
        private final boolean shouldAutoPublish;
        private final SessionState state;
        private final AccessToken tokenInfo;

        SerializationProxyV2(String applicationId, SessionState state, AccessToken tokenInfo, Date lastAttemptedTokenExtendDate, boolean shouldAutoPublish, AuthorizationRequest pendingAuthorizationRequest, Set<String> requestedPermissions) {
            this.applicationId = applicationId;
            this.state = state;
            this.tokenInfo = tokenInfo;
            this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
            this.shouldAutoPublish = shouldAutoPublish;
            this.pendingAuthorizationRequest = pendingAuthorizationRequest;
            this.requestedPermissions = requestedPermissions;
        }

        private Object readResolve() {
            return new Session(this.applicationId, this.state, this.tokenInfo, this.lastAttemptedTokenExtendDate, this.shouldAutoPublish, this.pendingAuthorizationRequest, this.requestedPermissions, null);
        }
    }

    private Session(String applicationId, SessionState state, AccessToken tokenInfo, Date lastAttemptedTokenExtendDate, boolean shouldAutoPublish, AuthorizationRequest pendingAuthorizationRequest) {
        this.lastAttemptedTokenExtendDate = new Date(0L);
        this.lock = new Object();
        this.applicationId = applicationId;
        this.state = state;
        this.tokenInfo = tokenInfo;
        this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
        this.pendingAuthorizationRequest = pendingAuthorizationRequest;
        this.handler = new Handler(Looper.getMainLooper());
        this.currentTokenRefreshRequest = null;
        this.tokenCachingStrategy = null;
        this.callbacks = new ArrayList();
    }

    /* synthetic */ Session(String str, SessionState sessionState, AccessToken accessToken, Date date, boolean z, AuthorizationRequest authorizationRequest, Session session) {
        this(str, sessionState, accessToken, date, z, authorizationRequest);
    }

    private Session(String applicationId, SessionState state, AccessToken tokenInfo, Date lastAttemptedTokenExtendDate, boolean shouldAutoPublish, AuthorizationRequest pendingAuthorizationRequest, Set<String> requestedPermissions) {
        this.lastAttemptedTokenExtendDate = new Date(0L);
        this.lock = new Object();
        this.applicationId = applicationId;
        this.state = state;
        this.tokenInfo = tokenInfo;
        this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
        this.pendingAuthorizationRequest = pendingAuthorizationRequest;
        this.handler = new Handler(Looper.getMainLooper());
        this.currentTokenRefreshRequest = null;
        this.tokenCachingStrategy = null;
        this.callbacks = new ArrayList();
    }

    /* synthetic */ Session(String str, SessionState sessionState, AccessToken accessToken, Date date, boolean z, AuthorizationRequest authorizationRequest, Set set, Session session) {
        this(str, sessionState, accessToken, date, z, authorizationRequest, set);
    }

    public Session(Context currentContext) {
        this(currentContext, null, null, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Session(Context context, String applicationId, TokenCachingStrategy tokenCachingStrategy) {
        this(context, applicationId, tokenCachingStrategy, true);
    }

    Session(Context context, String applicationId, TokenCachingStrategy tokenCachingStrategy, boolean loadTokenFromCache) {
        this.lastAttemptedTokenExtendDate = new Date(0L);
        this.lock = new Object();
        if (context != null && applicationId == null) {
            applicationId = Utility.getMetadataApplicationId(context);
        }
        Validate.notNull(applicationId, "applicationId");
        initializeStaticContext(context);
        tokenCachingStrategy = tokenCachingStrategy == null ? new SharedPreferencesTokenCachingStrategy(staticContext) : tokenCachingStrategy;
        this.applicationId = applicationId;
        this.tokenCachingStrategy = tokenCachingStrategy;
        this.state = SessionState.CREATED;
        this.pendingAuthorizationRequest = null;
        this.callbacks = new ArrayList();
        this.handler = new Handler(Looper.getMainLooper());
        Bundle tokenState = loadTokenFromCache ? tokenCachingStrategy.load() : null;
        if (TokenCachingStrategy.hasTokenInformation(tokenState)) {
            Date cachedExpirationDate = TokenCachingStrategy.getDate(tokenState, TokenCachingStrategy.EXPIRATION_DATE_KEY);
            Date now = new Date();
            if (cachedExpirationDate == null || cachedExpirationDate.before(now)) {
                tokenCachingStrategy.clear();
                this.tokenInfo = AccessToken.createEmptyToken();
                return;
            }
            this.tokenInfo = AccessToken.createFromCache(tokenState);
            this.state = SessionState.CREATED_TOKEN_LOADED;
            return;
        }
        this.tokenInfo = AccessToken.createEmptyToken();
    }

    public final Bundle getAuthorizationBundle() {
        Bundle bundle;
        synchronized (this.lock) {
            bundle = this.authorizationBundle;
        }
        return bundle;
    }

    public final boolean isOpened() {
        boolean isOpened;
        synchronized (this.lock) {
            isOpened = this.state.isOpened();
        }
        return isOpened;
    }

    public final boolean isClosed() {
        boolean isClosed;
        synchronized (this.lock) {
            isClosed = this.state.isClosed();
        }
        return isClosed;
    }

    public final SessionState getState() {
        SessionState sessionState;
        synchronized (this.lock) {
            sessionState = this.state;
        }
        return sessionState;
    }

    public final String getApplicationId() {
        return this.applicationId;
    }

    public final String getAccessToken() {
        String token;
        synchronized (this.lock) {
            token = this.tokenInfo == null ? null : this.tokenInfo.getToken();
        }
        return token;
    }

    public final Date getExpirationDate() {
        Date expires;
        synchronized (this.lock) {
            expires = this.tokenInfo == null ? null : this.tokenInfo.getExpires();
        }
        return expires;
    }

    public final List<String> getPermissions() {
        List<String> permissions;
        synchronized (this.lock) {
            permissions = this.tokenInfo == null ? null : this.tokenInfo.getPermissions();
        }
        return permissions;
    }

    public boolean isPermissionGranted(String permission) {
        List<String> grantedPermissions = getPermissions();
        if (grantedPermissions != null) {
            return grantedPermissions.contains(permission);
        }
        return false;
    }

    public final List<String> getDeclinedPermissions() {
        List<String> declinedPermissions;
        synchronized (this.lock) {
            declinedPermissions = this.tokenInfo == null ? null : this.tokenInfo.getDeclinedPermissions();
        }
        return declinedPermissions;
    }

    public final void openForRead(OpenRequest openRequest) {
        open(openRequest, SessionAuthorizationType.READ);
    }

    public final void openForPublish(OpenRequest openRequest) {
        open(openRequest, SessionAuthorizationType.PUBLISH);
    }

    public final void open(AccessToken accessToken, StatusCallback callback) {
        synchronized (this.lock) {
            if (this.pendingAuthorizationRequest != null) {
                throw new UnsupportedOperationException("Session: an attempt was made to open a session that has a pending request.");
            }
            if (this.state.isClosed()) {
                throw new UnsupportedOperationException("Session: an attempt was made to open a previously-closed session.");
            }
            if (this.state != SessionState.CREATED && this.state != SessionState.CREATED_TOKEN_LOADED) {
                throw new UnsupportedOperationException("Session: an attempt was made to open an already opened session.");
            }
            if (callback != null) {
                addCallback(callback);
            }
            this.tokenInfo = accessToken;
            if (this.tokenCachingStrategy != null) {
                this.tokenCachingStrategy.save(accessToken.toCacheBundle());
            }
            SessionState oldState = this.state;
            this.state = SessionState.OPENED;
            postStateChange(oldState, this.state, null);
        }
        autoPublishAsync();
    }

    public final void requestNewReadPermissions(NewPermissionsRequest newPermissionsRequest) {
        requestNewPermissions(newPermissionsRequest, SessionAuthorizationType.READ);
    }

    public final void requestNewPublishPermissions(NewPermissionsRequest newPermissionsRequest) {
        requestNewPermissions(newPermissionsRequest, SessionAuthorizationType.PUBLISH);
    }

    public final void refreshPermissions() {
        Request request = new Request(this, "me/permissions");
        request.setCallback(new Request.Callback() { // from class: com.facebook.Session.2
            @Override // com.facebook.Request.Callback
            public void onCompleted(Response response) {
                PermissionsPair permissionsPair = Session.handlePermissionResponse(response);
                if (permissionsPair != null) {
                    synchronized (Session.this.lock) {
                        Session.this.tokenInfo = AccessToken.createFromTokenWithRefreshedPermissions(Session.this.tokenInfo, permissionsPair.getGrantedPermissions(), permissionsPair.getDeclinedPermissions());
                        Session.this.postStateChange(Session.this.state, SessionState.OPENED_TOKEN_UPDATED, null);
                    }
                }
            }
        });
        request.executeAsync();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class PermissionsPair {
        List<String> declinedPermissions;
        List<String> grantedPermissions;

        public PermissionsPair(List<String> grantedPermissions, List<String> declinedPermissions) {
            this.grantedPermissions = grantedPermissions;
            this.declinedPermissions = declinedPermissions;
        }

        public List<String> getGrantedPermissions() {
            return this.grantedPermissions;
        }

        public List<String> getDeclinedPermissions() {
            return this.declinedPermissions;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PermissionsPair handlePermissionResponse(Response response) {
        GraphMultiResult result;
        GraphObjectList<GraphObject> data;
        if (response.getError() != null || (result = (GraphMultiResult) response.getGraphObjectAs(GraphMultiResult.class)) == null || (data = result.getData()) == null || data.size() == 0) {
            return null;
        }
        List<String> grantedPermissions = new ArrayList<>(data.size());
        List<String> declinedPermissions = new ArrayList<>(data.size());
        GraphObject firstObject = data.get(0);
        if (firstObject.getProperty("permission") != null) {
            for (GraphObject graphObject : data) {
                String permission = (String) graphObject.getProperty("permission");
                if (!permission.equals("installed")) {
                    String status = (String) graphObject.getProperty("status");
                    if (status.equals("granted")) {
                        grantedPermissions.add(permission);
                    } else if (status.equals("declined")) {
                        declinedPermissions.add(permission);
                    }
                }
            }
        } else {
            Map<String, Object> permissionsMap = firstObject.asMap();
            for (Map.Entry<String, Object> entry : permissionsMap.entrySet()) {
                if (!entry.getKey().equals("installed") && ((Integer) entry.getValue()).intValue() == 1) {
                    grantedPermissions.add(entry.getKey());
                }
            }
        }
        return new PermissionsPair(grantedPermissions, declinedPermissions);
    }

    public final boolean onActivityResult(Activity currentActivity, int requestCode, int resultCode, Intent data) {
        Validate.notNull(currentActivity, "currentActivity");
        initializeStaticContext(currentActivity);
        synchronized (this.lock) {
            if (this.pendingAuthorizationRequest == null || requestCode != this.pendingAuthorizationRequest.getRequestCode()) {
                return false;
            }
            Exception exception = null;
            AuthorizationClient.Result.Code code = AuthorizationClient.Result.Code.ERROR;
            if (data != null) {
                AuthorizationClient.Result result = (AuthorizationClient.Result) data.getSerializableExtra("com.facebook.LoginActivity:Result");
                if (result != null) {
                    handleAuthorizationResult(resultCode, result);
                    return true;
                } else if (this.authorizationClient != null) {
                    this.authorizationClient.onActivityResult(requestCode, resultCode, data);
                    return true;
                }
            } else if (resultCode == 0) {
                exception = new FacebookOperationCanceledException("User canceled operation.");
                code = AuthorizationClient.Result.Code.CANCEL;
            }
            if (exception == null) {
                exception = new FacebookException("Unexpected call to Session.onActivityResult");
            }
            logAuthorizationComplete(code, null, exception);
            finishAuthOrReauth(null, exception);
            return true;
        }
    }

    public final void close() {
        synchronized (this.lock) {
            SessionState oldState = this.state;
            switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                case 1:
                case 3:
                    this.state = SessionState.CLOSED_LOGIN_FAILED;
                    postStateChange(oldState, this.state, new FacebookException("Log in attempt aborted."));
                    break;
                case 2:
                case 4:
                case 5:
                    this.state = SessionState.CLOSED;
                    postStateChange(oldState, this.state, null);
                    break;
            }
        }
    }

    public final void closeAndClearTokenInformation() {
        if (this.tokenCachingStrategy != null) {
            this.tokenCachingStrategy.clear();
        }
        Utility.clearFacebookCookies(staticContext);
        Utility.clearCaches(staticContext);
        close();
    }

    public final void addCallback(StatusCallback callback) {
        synchronized (this.callbacks) {
            if (callback != null) {
                if (!this.callbacks.contains(callback)) {
                    this.callbacks.add(callback);
                }
            }
        }
    }

    public final void removeCallback(StatusCallback callback) {
        synchronized (this.callbacks) {
            this.callbacks.remove(callback);
        }
    }

    public String toString() {
        return "{Session state:" + this.state + ", token:" + (this.tokenInfo == null ? "null" : this.tokenInfo) + ", appId:" + (this.applicationId == null ? "null" : this.applicationId) + "}";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void extendTokenCompleted(Bundle bundle) {
        synchronized (this.lock) {
            SessionState oldState = this.state;
            switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                case 4:
                    this.state = SessionState.OPENED_TOKEN_UPDATED;
                    postStateChange(oldState, this.state, null);
                    break;
                case 5:
                    break;
                default:
                    Log.d(TAG, "refreshToken ignored in state " + this.state);
                    return;
            }
            this.tokenInfo = AccessToken.createFromRefresh(this.tokenInfo, bundle);
            if (this.tokenCachingStrategy != null) {
                this.tokenCachingStrategy.save(this.tokenInfo.toCacheBundle());
            }
        }
    }

    private Object writeReplace() {
        return new SerializationProxyV1(this.applicationId, this.state, this.tokenInfo, this.lastAttemptedTokenExtendDate, false, this.pendingAuthorizationRequest);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Cannot readObject, serialization proxy required");
    }

    public static final void saveSession(Session session, Bundle bundle) {
        if (bundle != null && session != null && !bundle.containsKey(SESSION_BUNDLE_SAVE_KEY)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                new ObjectOutputStream(outputStream).writeObject(session);
                bundle.putByteArray(SESSION_BUNDLE_SAVE_KEY, outputStream.toByteArray());
                bundle.putBundle(AUTH_BUNDLE_SAVE_KEY, session.authorizationBundle);
            } catch (IOException e) {
                throw new FacebookException("Unable to save session.", e);
            }
        }
    }

    public static final Session restoreSession(Context context, TokenCachingStrategy cachingStrategy, StatusCallback callback, Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        byte[] data = bundle.getByteArray(SESSION_BUNDLE_SAVE_KEY);
        if (data != null) {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            try {
                Session session = (Session) new ObjectInputStream(is).readObject();
                initializeStaticContext(context);
                if (cachingStrategy != null) {
                    session.tokenCachingStrategy = cachingStrategy;
                } else {
                    session.tokenCachingStrategy = new SharedPreferencesTokenCachingStrategy(context);
                }
                if (callback != null) {
                    session.addCallback(callback);
                }
                session.authorizationBundle = bundle.getBundle(AUTH_BUNDLE_SAVE_KEY);
                return session;
            } catch (IOException e) {
                Log.w(TAG, "Unable to restore session.", e);
            } catch (ClassNotFoundException e2) {
                Log.w(TAG, "Unable to restore session", e2);
            }
        }
        return null;
    }

    public static final Session getActiveSession() {
        Session session;
        synchronized (STATIC_LOCK) {
            session = activeSession;
        }
        return session;
    }

    public static final void setActiveSession(Session session) {
        synchronized (STATIC_LOCK) {
            if (session != activeSession) {
                Session oldSession = activeSession;
                if (oldSession != null) {
                    oldSession.close();
                }
                activeSession = session;
                if (oldSession != null) {
                    postActiveSessionAction(ACTION_ACTIVE_SESSION_UNSET);
                }
                if (session != null) {
                    postActiveSessionAction(ACTION_ACTIVE_SESSION_SET);
                    if (session.isOpened()) {
                        postActiveSessionAction(ACTION_ACTIVE_SESSION_OPENED);
                    }
                }
            }
        }
    }

    public static Session openActiveSessionFromCache(Context context) {
        return openActiveSession(context, false, (OpenRequest) null);
    }

    public static Session openActiveSession(Activity activity, boolean allowLoginUI, StatusCallback callback) {
        return openActiveSession(activity, allowLoginUI, new OpenRequest(activity).setCallback(callback));
    }

    public static Session openActiveSession(Activity activity, boolean allowLoginUI, List<String> permissions, StatusCallback callback) {
        return openActiveSession(activity, allowLoginUI, new OpenRequest(activity).setCallback(callback).setPermissions(permissions));
    }

    public static Session openActiveSession(Context context, Fragment fragment, boolean allowLoginUI, StatusCallback callback) {
        return openActiveSession(context, allowLoginUI, new OpenRequest(fragment).setCallback(callback));
    }

    public static Session openActiveSession(Context context, Fragment fragment, boolean allowLoginUI, List<String> permissions, StatusCallback callback) {
        return openActiveSession(context, allowLoginUI, new OpenRequest(fragment).setCallback(callback).setPermissions(permissions));
    }

    public static Session openActiveSessionWithAccessToken(Context context, AccessToken accessToken, StatusCallback callback) {
        Session session = new Session(context, null, null, false);
        setActiveSession(session);
        session.open(accessToken, callback);
        return session;
    }

    private static Session openActiveSession(Context context, boolean allowLoginUI, OpenRequest openRequest) {
        Session session = new Builder(context).build();
        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
            setActiveSession(session);
            session.openForRead(openRequest);
            return session;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Context getStaticContext() {
        return staticContext;
    }

    static void initializeStaticContext(Context currentContext) {
        if (currentContext != null && staticContext == null) {
            Context applicationContext = currentContext.getApplicationContext();
            if (applicationContext == null) {
                applicationContext = currentContext;
            }
            staticContext = applicationContext;
        }
    }

    void authorize(AuthorizationRequest request) {
        request.setApplicationId(this.applicationId);
        autoPublishAsync();
        logAuthorizationStart();
        boolean started = tryLoginActivity(request);
        this.pendingAuthorizationRequest.loggingExtras.put("try_login_activity", started ? AppEventsConstants.EVENT_PARAM_VALUE_YES : AppEventsConstants.EVENT_PARAM_VALUE_NO);
        if (!started && request.isLegacy) {
            this.pendingAuthorizationRequest.loggingExtras.put("try_legacy", AppEventsConstants.EVENT_PARAM_VALUE_YES);
            tryLegacyAuth(request);
            started = true;
        }
        if (!started) {
            synchronized (this.lock) {
                SessionState oldState = this.state;
                switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                    case 6:
                    case 7:
                        break;
                    default:
                        this.state = SessionState.CLOSED_LOGIN_FAILED;
                        Exception exception = new FacebookException("Log in attempt failed: LoginActivity could not be started, and not legacy request");
                        logAuthorizationComplete(AuthorizationClient.Result.Code.ERROR, null, exception);
                        postStateChange(oldState, this.state, exception);
                        break;
                }
            }
        }
    }

    private void open(OpenRequest openRequest, SessionAuthorizationType authType) {
        SessionState newState;
        validatePermissions(openRequest, authType);
        validateLoginBehavior(openRequest);
        synchronized (this.lock) {
            if (this.pendingAuthorizationRequest != null) {
                postStateChange(this.state, this.state, new UnsupportedOperationException("Session: an attempt was made to open a session that has a pending request."));
                return;
            }
            SessionState oldState = this.state;
            switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                case 1:
                    newState = SessionState.OPENING;
                    this.state = newState;
                    if (openRequest == null) {
                        throw new IllegalArgumentException("openRequest cannot be null when opening a new Session");
                    }
                    this.pendingAuthorizationRequest = openRequest;
                    break;
                case 2:
                    if (openRequest != null && !Utility.isNullOrEmpty(openRequest.getPermissions()) && !Utility.isSubset(openRequest.getPermissions(), getPermissions())) {
                        this.pendingAuthorizationRequest = openRequest;
                    }
                    if (this.pendingAuthorizationRequest == null) {
                        newState = SessionState.OPENED;
                        this.state = newState;
                        break;
                    } else {
                        newState = SessionState.OPENING;
                        this.state = newState;
                        break;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Session: an attempt was made to open an already opened session.");
            }
            if (openRequest != null) {
                addCallback(openRequest.getCallback());
            }
            postStateChange(oldState, newState, null);
            if (newState == SessionState.OPENING) {
                authorize(openRequest);
            }
        }
    }

    private void requestNewPermissions(NewPermissionsRequest newPermissionsRequest, SessionAuthorizationType authType) {
        validatePermissions(newPermissionsRequest, authType);
        validateLoginBehavior(newPermissionsRequest);
        if (newPermissionsRequest != null) {
            synchronized (this.lock) {
                if (this.pendingAuthorizationRequest != null) {
                    throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that has a pending request.");
                }
                if (this.state.isOpened()) {
                    this.pendingAuthorizationRequest = newPermissionsRequest;
                } else if (this.state.isClosed()) {
                    throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that has been closed.");
                } else {
                    throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that is not currently open.");
                }
            }
            newPermissionsRequest.setValidateSameFbidAsToken(getAccessToken());
            addCallback(newPermissionsRequest.getCallback());
            authorize(newPermissionsRequest);
        }
    }

    private void validateLoginBehavior(AuthorizationRequest request) {
        if (request != null && !request.isLegacy) {
            Intent intent = new Intent();
            intent.setClass(getStaticContext(), LoginActivity.class);
            if (!resolveIntent(intent)) {
                throw new FacebookException(String.format("Cannot use SessionLoginBehavior %s when %s is not declared as an activity in AndroidManifest.xml", request.getLoginBehavior(), LoginActivity.class.getName()));
            }
        }
    }

    private void validatePermissions(AuthorizationRequest request, SessionAuthorizationType authType) {
        if (request == null || Utility.isNullOrEmpty(request.getPermissions())) {
            if (SessionAuthorizationType.PUBLISH.equals(authType)) {
                throw new FacebookException("Cannot request publish or manage authorization with no permissions.");
            }
            return;
        }
        for (String permission : request.getPermissions()) {
            if (isPublishPermission(permission)) {
                if (SessionAuthorizationType.READ.equals(authType)) {
                    throw new FacebookException(String.format("Cannot pass a publish or manage permission (%s) to a request for read authorization", permission));
                }
            } else if (SessionAuthorizationType.PUBLISH.equals(authType)) {
                Log.w(TAG, String.format("Should not pass a read permission (%s) to a request for publish or manage authorization", permission));
            }
        }
    }

    public static boolean isPublishPermission(String permission) {
        return permission != null && (permission.startsWith(PUBLISH_PERMISSION_PREFIX) || permission.startsWith(MANAGE_PERMISSION_PREFIX) || OTHER_PUBLISH_PERMISSIONS.contains(permission));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAuthorizationResult(int resultCode, AuthorizationClient.Result result) {
        AccessToken newToken = null;
        Exception exception = null;
        if (resultCode == -1) {
            if (result.code == AuthorizationClient.Result.Code.SUCCESS) {
                newToken = result.token;
            } else {
                exception = new FacebookAuthorizationException(result.errorMessage);
            }
        } else if (resultCode == 0) {
            exception = new FacebookOperationCanceledException(result.errorMessage);
        }
        logAuthorizationComplete(result.code, result.loggingExtras, exception);
        this.authorizationClient = null;
        finishAuthOrReauth(newToken, exception);
    }

    private void logAuthorizationStart() {
        Bundle bundle = AuthorizationClient.newAuthorizationLoggingBundle(this.pendingAuthorizationRequest.getAuthId());
        bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        try {
            JSONObject extras = new JSONObject();
            extras.put("login_behavior", this.pendingAuthorizationRequest.loginBehavior.toString());
            extras.put("request_code", this.pendingAuthorizationRequest.requestCode);
            extras.put("is_legacy", this.pendingAuthorizationRequest.isLegacy);
            extras.put("permissions", TextUtils.join(",", this.pendingAuthorizationRequest.permissions));
            extras.put(ServerProtocol.DIALOG_PARAM_DEFAULT_AUDIENCE, this.pendingAuthorizationRequest.defaultAudience.toString());
            bundle.putString("6_extras", extras.toString());
        } catch (JSONException e) {
        }
        AppEventsLogger logger = getAppEventsLogger();
        logger.logSdkEvent("fb_mobile_login_start", null, bundle);
    }

    private void logAuthorizationComplete(AuthorizationClient.Result.Code result, Map<String, String> resultExtras, Exception exception) {
        Bundle bundle;
        if (this.pendingAuthorizationRequest == null) {
            bundle = AuthorizationClient.newAuthorizationLoggingBundle("");
            bundle.putString("2_result", AuthorizationClient.Result.Code.ERROR.getLoggingValue());
            bundle.putString("5_error_message", "Unexpected call to logAuthorizationComplete with null pendingAuthorizationRequest.");
        } else {
            bundle = AuthorizationClient.newAuthorizationLoggingBundle(this.pendingAuthorizationRequest.getAuthId());
            if (result != null) {
                bundle.putString("2_result", result.getLoggingValue());
            }
            if (exception != null && exception.getMessage() != null) {
                bundle.putString("5_error_message", exception.getMessage());
            }
            JSONObject jsonObject = null;
            if (!this.pendingAuthorizationRequest.loggingExtras.isEmpty()) {
                jsonObject = new JSONObject(this.pendingAuthorizationRequest.loggingExtras);
            }
            if (resultExtras != null) {
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                }
                try {
                    for (Map.Entry<String, String> entry : resultExtras.entrySet()) {
                        jsonObject.put(entry.getKey(), entry.getValue());
                    }
                } catch (JSONException e) {
                }
            }
            if (jsonObject != null) {
                bundle.putString("6_extras", jsonObject.toString());
            }
        }
        bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        AppEventsLogger logger = getAppEventsLogger();
        logger.logSdkEvent("fb_mobile_login_complete", null, bundle);
    }

    private boolean tryLoginActivity(AuthorizationRequest request) {
        Intent intent = getLoginActivityIntent(request);
        if (resolveIntent(intent)) {
            try {
                request.getStartActivityDelegate().startActivityForResult(intent, request.getRequestCode());
                return true;
            } catch (ActivityNotFoundException e) {
                return false;
            }
        }
        return false;
    }

    private boolean resolveIntent(Intent intent) {
        ResolveInfo resolveInfo = getStaticContext().getPackageManager().resolveActivity(intent, 0);
        return resolveInfo != null;
    }

    private Intent getLoginActivityIntent(AuthorizationRequest request) {
        Intent intent = new Intent();
        intent.setClass(getStaticContext(), LoginActivity.class);
        intent.setAction(request.getLoginBehavior().toString());
        AuthorizationClient.AuthorizationRequest authClientRequest = request.getAuthorizationClientRequest();
        Bundle extras = LoginActivity.populateIntentExtras(authClientRequest);
        intent.putExtras(extras);
        return intent;
    }

    private void tryLegacyAuth(AuthorizationRequest request) {
        this.authorizationClient = new AuthorizationClient();
        this.authorizationClient.setOnCompletedListener(new AuthorizationClient.OnCompletedListener() { // from class: com.facebook.Session.3
            @Override // com.facebook.AuthorizationClient.OnCompletedListener
            public void onCompleted(AuthorizationClient.Result result) {
                int activityResult;
                if (result.code == AuthorizationClient.Result.Code.CANCEL) {
                    activityResult = 0;
                } else {
                    activityResult = -1;
                }
                Session.this.handleAuthorizationResult(activityResult, result);
            }
        });
        this.authorizationClient.setContext(getStaticContext());
        this.authorizationClient.startOrContinueAuth(request.getAuthorizationClientRequest());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishAuthOrReauth(AccessToken newToken, Exception exception) {
        if (newToken != null && newToken.isInvalid()) {
            newToken = null;
            exception = new FacebookException("Invalid access token.");
        }
        synchronized (this.lock) {
            switch ($SWITCH_TABLE$com$facebook$SessionState()[this.state.ordinal()]) {
                case 1:
                case 2:
                case 6:
                case 7:
                    Log.d(TAG, "Unexpected call to finishAuthOrReauth in state " + this.state);
                    break;
                case 3:
                    finishAuthorization(newToken, exception);
                    break;
                case 4:
                case 5:
                    finishReauthorization(newToken, exception);
                    break;
            }
        }
    }

    private void finishAuthorization(AccessToken newToken, Exception exception) {
        SessionState oldState = this.state;
        if (newToken != null) {
            this.tokenInfo = newToken;
            saveTokenToCache(newToken);
            this.state = SessionState.OPENED;
        } else if (exception != null) {
            this.state = SessionState.CLOSED_LOGIN_FAILED;
        }
        this.pendingAuthorizationRequest = null;
        postStateChange(oldState, this.state, exception);
    }

    private void finishReauthorization(AccessToken newToken, Exception exception) {
        SessionState oldState = this.state;
        if (newToken != null) {
            this.tokenInfo = newToken;
            saveTokenToCache(newToken);
            this.state = SessionState.OPENED_TOKEN_UPDATED;
        }
        this.pendingAuthorizationRequest = null;
        postStateChange(oldState, this.state, exception);
    }

    private void saveTokenToCache(AccessToken newToken) {
        if (newToken != null && this.tokenCachingStrategy != null) {
            this.tokenCachingStrategy.save(newToken.toCacheBundle());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postStateChange(SessionState oldState, final SessionState newState, final Exception exception) {
        if (oldState != newState || oldState == SessionState.OPENED_TOKEN_UPDATED || exception != null) {
            if (newState.isClosed()) {
                this.tokenInfo = AccessToken.createEmptyToken();
            }
            Runnable runCallbacks = new Runnable() { // from class: com.facebook.Session.4
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (Session.this.callbacks) {
                        for (final StatusCallback callback : Session.this.callbacks) {
                            final SessionState sessionState = newState;
                            final Exception exc = exception;
                            Runnable closure = new Runnable() { // from class: com.facebook.Session.4.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    callback.call(Session.this, sessionState, exc);
                                }
                            };
                            Session.runWithHandlerOrExecutor(Session.this.handler, closure);
                        }
                    }
                }
            };
            runWithHandlerOrExecutor(this.handler, runCallbacks);
            if (this == activeSession && oldState.isOpened() != newState.isOpened()) {
                if (newState.isOpened()) {
                    postActiveSessionAction(ACTION_ACTIVE_SESSION_OPENED);
                } else {
                    postActiveSessionAction(ACTION_ACTIVE_SESSION_CLOSED);
                }
            }
        }
    }

    static void postActiveSessionAction(String action) {
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(getStaticContext()).sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void runWithHandlerOrExecutor(Handler handler, Runnable runnable) {
        if (handler != null) {
            handler.post(runnable);
        } else {
            Settings.getExecutor().execute(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void extendAccessTokenIfNeeded() {
        if (shouldExtendAccessToken()) {
            extendAccessToken();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void extendAccessToken() {
        TokenRefreshRequest newTokenRefreshRequest = null;
        synchronized (this.lock) {
            try {
                if (this.currentTokenRefreshRequest == null) {
                    TokenRefreshRequest newTokenRefreshRequest2 = new TokenRefreshRequest();
                    try {
                        this.currentTokenRefreshRequest = newTokenRefreshRequest2;
                        newTokenRefreshRequest = newTokenRefreshRequest2;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                if (newTokenRefreshRequest != null) {
                    newTokenRefreshRequest.bind();
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldExtendAccessToken() {
        if (this.currentTokenRefreshRequest != null) {
            return false;
        }
        Date now = new Date();
        if (!this.state.isOpened() || !this.tokenInfo.getSource().canExtendToken() || now.getTime() - this.lastAttemptedTokenExtendDate.getTime() <= 3600000 || now.getTime() - this.tokenInfo.getLastRefresh().getTime() <= 86400000) {
            return false;
        }
        return true;
    }

    private AppEventsLogger getAppEventsLogger() {
        AppEventsLogger appEventsLogger;
        synchronized (this.lock) {
            if (this.appEventsLogger == null) {
                this.appEventsLogger = AppEventsLogger.newLogger(staticContext, this.applicationId);
            }
            appEventsLogger = this.appEventsLogger;
        }
        return appEventsLogger;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessToken getTokenInfo() {
        return this.tokenInfo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTokenInfo(AccessToken tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    Date getLastAttemptedTokenExtendDate() {
        return this.lastAttemptedTokenExtendDate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLastAttemptedTokenExtendDate(Date lastAttemptedTokenExtendDate) {
        this.lastAttemptedTokenExtendDate = lastAttemptedTokenExtendDate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCurrentTokenRefreshRequest(TokenRefreshRequest request) {
        this.currentTokenRefreshRequest = request;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class TokenRefreshRequest implements ServiceConnection {
        final Messenger messageReceiver;
        Messenger messageSender = null;

        /* JADX INFO: Access modifiers changed from: package-private */
        public TokenRefreshRequest() {
            this.messageReceiver = new Messenger(new TokenRefreshRequestHandler(Session.this, this));
        }

        public void bind() {
            Intent intent = NativeProtocol.createTokenRefreshIntent(Session.getStaticContext());
            if (intent != null && Session.staticContext.bindService(intent, this, 1)) {
                Session.this.setLastAttemptedTokenExtendDate(new Date());
            } else {
                cleanup();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            this.messageSender = new Messenger(service);
            refreshToken();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName arg) {
            cleanup();
            Session.staticContext.unbindService(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cleanup() {
            if (Session.this.currentTokenRefreshRequest == this) {
                Session.this.currentTokenRefreshRequest = null;
            }
        }

        private void refreshToken() {
            Bundle requestData = new Bundle();
            requestData.putString("access_token", Session.this.getTokenInfo().getToken());
            Message request = Message.obtain();
            request.setData(requestData);
            request.replyTo = this.messageReceiver;
            try {
                this.messageSender.send(request);
            } catch (RemoteException e) {
                cleanup();
            }
        }
    }

    /* loaded from: classes.dex */
    static class TokenRefreshRequestHandler extends Handler {
        private WeakReference<TokenRefreshRequest> refreshRequestWeakReference;
        private WeakReference<Session> sessionWeakReference;

        TokenRefreshRequestHandler(Session session, TokenRefreshRequest refreshRequest) {
            super(Looper.getMainLooper());
            this.sessionWeakReference = new WeakReference<>(session);
            this.refreshRequestWeakReference = new WeakReference<>(refreshRequest);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            String token = msg.getData().getString("access_token");
            Session session = this.sessionWeakReference.get();
            if (session != null && token != null) {
                session.extendTokenCompleted(msg.getData());
            }
            TokenRefreshRequest request = this.refreshRequestWeakReference.get();
            if (request != null) {
                Session.staticContext.unbindService(request);
                request.cleanup();
            }
        }
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object otherObj) {
        if (otherObj instanceof Session) {
            Session other = (Session) otherObj;
            return areEqual(other.applicationId, this.applicationId) && areEqual(other.authorizationBundle, this.authorizationBundle) && areEqual(other.state, this.state) && areEqual(other.getExpirationDate(), getExpirationDate());
        }
        return false;
    }

    private static boolean areEqual(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private String applicationId;
        private final Context context;
        private TokenCachingStrategy tokenCachingStrategy;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setApplicationId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder setTokenCachingStrategy(TokenCachingStrategy tokenCachingStrategy) {
            this.tokenCachingStrategy = tokenCachingStrategy;
            return this;
        }

        public Session build() {
            return new Session(this.context, this.applicationId, this.tokenCachingStrategy);
        }
    }

    private void autoPublishAsync() {
        String applicationId;
        AutoPublishAsyncTask asyncTask = null;
        synchronized (this) {
            if (this.autoPublishAsyncTask == null && Settings.getShouldAutoPublishInstall() && (applicationId = this.applicationId) != null) {
                AutoPublishAsyncTask asyncTask2 = new AutoPublishAsyncTask(applicationId, staticContext);
                this.autoPublishAsyncTask = asyncTask2;
                asyncTask = asyncTask2;
            }
        }
        if (asyncTask != null) {
            asyncTask.execute(new Void[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AutoPublishAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Context mApplicationContext;
        private final String mApplicationId;

        public AutoPublishAsyncTask(String applicationId, Context context) {
            this.mApplicationId = applicationId;
            this.mApplicationContext = context.getApplicationContext();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voids) {
            try {
                Settings.publishInstallAndWaitForResponse(this.mApplicationContext, this.mApplicationId, true);
                return null;
            } catch (Exception e) {
                Utility.logd("Facebook-publish", e);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void result) {
            synchronized (Session.this) {
                Session.this.autoPublishAsyncTask = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class AuthorizationRequest implements Serializable {
        private static final long serialVersionUID = 1;
        private String applicationId;
        private final String authId;
        private SessionDefaultAudience defaultAudience;
        private boolean isLegacy;
        private final Map<String, String> loggingExtras;
        private SessionLoginBehavior loginBehavior;
        private List<String> permissions;
        private int requestCode;
        private final StartActivityDelegate startActivityDelegate;
        private StatusCallback statusCallback;
        private String validateSameFbidAsToken;

        AuthorizationRequest(final Activity activity) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.authId = UUID.randomUUID().toString();
            this.loggingExtras = new HashMap();
            this.startActivityDelegate = new StartActivityDelegate() { // from class: com.facebook.Session.AuthorizationRequest.1
                @Override // com.facebook.Session.StartActivityDelegate
                public void startActivityForResult(Intent intent, int requestCode) {
                    activity.startActivityForResult(intent, requestCode);
                }

                @Override // com.facebook.Session.StartActivityDelegate
                public Activity getActivityContext() {
                    return activity;
                }
            };
        }

        AuthorizationRequest(final Fragment fragment) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.authId = UUID.randomUUID().toString();
            this.loggingExtras = new HashMap();
            this.startActivityDelegate = new StartActivityDelegate() { // from class: com.facebook.Session.AuthorizationRequest.2
                @Override // com.facebook.Session.StartActivityDelegate
                public void startActivityForResult(Intent intent, int requestCode) {
                    fragment.startActivityForResult(intent, requestCode);
                }

                @Override // com.facebook.Session.StartActivityDelegate
                public Activity getActivityContext() {
                    return fragment.getActivity();
                }
            };
        }

        private AuthorizationRequest(SessionLoginBehavior loginBehavior, int requestCode, List<String> permissions, String defaultAudience, boolean isLegacy, String applicationId, String validateSameFbidAsToken) {
            this.loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            this.requestCode = Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE;
            this.isLegacy = false;
            this.permissions = Collections.emptyList();
            this.defaultAudience = SessionDefaultAudience.FRIENDS;
            this.authId = UUID.randomUUID().toString();
            this.loggingExtras = new HashMap();
            this.startActivityDelegate = new StartActivityDelegate() { // from class: com.facebook.Session.AuthorizationRequest.3
                @Override // com.facebook.Session.StartActivityDelegate
                public void startActivityForResult(Intent intent, int requestCode2) {
                    throw new UnsupportedOperationException("Cannot create an AuthorizationRequest without a valid Activity or Fragment");
                }

                @Override // com.facebook.Session.StartActivityDelegate
                public Activity getActivityContext() {
                    throw new UnsupportedOperationException("Cannot create an AuthorizationRequest without a valid Activity or Fragment");
                }
            };
            this.loginBehavior = loginBehavior;
            this.requestCode = requestCode;
            this.permissions = permissions;
            this.defaultAudience = SessionDefaultAudience.valueOf(defaultAudience);
            this.isLegacy = isLegacy;
            this.applicationId = applicationId;
            this.validateSameFbidAsToken = validateSameFbidAsToken;
        }

        /* synthetic */ AuthorizationRequest(SessionLoginBehavior sessionLoginBehavior, int i, List list, String str, boolean z, String str2, String str3, AuthorizationRequest authorizationRequest) {
            this(sessionLoginBehavior, i, list, str, z, str2, str3);
        }

        public void setIsLegacy(boolean isLegacy) {
            this.isLegacy = isLegacy;
        }

        boolean isLegacy() {
            return this.isLegacy;
        }

        AuthorizationRequest setCallback(StatusCallback statusCallback) {
            this.statusCallback = statusCallback;
            return this;
        }

        StatusCallback getCallback() {
            return this.statusCallback;
        }

        AuthorizationRequest setLoginBehavior(SessionLoginBehavior loginBehavior) {
            if (loginBehavior != null) {
                this.loginBehavior = loginBehavior;
            }
            return this;
        }

        SessionLoginBehavior getLoginBehavior() {
            return this.loginBehavior;
        }

        AuthorizationRequest setRequestCode(int requestCode) {
            if (requestCode >= 0) {
                this.requestCode = requestCode;
            }
            return this;
        }

        int getRequestCode() {
            return this.requestCode;
        }

        AuthorizationRequest setPermissions(List<String> permissions) {
            if (permissions != null) {
                this.permissions = permissions;
            }
            return this;
        }

        AuthorizationRequest setPermissions(String... permissions) {
            return setPermissions(Arrays.asList(permissions));
        }

        List<String> getPermissions() {
            return this.permissions;
        }

        AuthorizationRequest setDefaultAudience(SessionDefaultAudience defaultAudience) {
            if (defaultAudience != null) {
                this.defaultAudience = defaultAudience;
            }
            return this;
        }

        SessionDefaultAudience getDefaultAudience() {
            return this.defaultAudience;
        }

        StartActivityDelegate getStartActivityDelegate() {
            return this.startActivityDelegate;
        }

        String getApplicationId() {
            return this.applicationId;
        }

        void setApplicationId(String applicationId) {
            this.applicationId = applicationId;
        }

        String getValidateSameFbidAsToken() {
            return this.validateSameFbidAsToken;
        }

        void setValidateSameFbidAsToken(String validateSameFbidAsToken) {
            this.validateSameFbidAsToken = validateSameFbidAsToken;
        }

        String getAuthId() {
            return this.authId;
        }

        AuthorizationClient.AuthorizationRequest getAuthorizationClientRequest() {
            AuthorizationClient.StartActivityDelegate delegate = new AuthorizationClient.StartActivityDelegate() { // from class: com.facebook.Session.AuthorizationRequest.4
                @Override // com.facebook.AuthorizationClient.StartActivityDelegate
                public void startActivityForResult(Intent intent, int requestCode) {
                    AuthorizationRequest.this.startActivityDelegate.startActivityForResult(intent, requestCode);
                }

                @Override // com.facebook.AuthorizationClient.StartActivityDelegate
                public Activity getActivityContext() {
                    return AuthorizationRequest.this.startActivityDelegate.getActivityContext();
                }
            };
            return new AuthorizationClient.AuthorizationRequest(this.loginBehavior, this.requestCode, this.isLegacy, this.permissions, this.defaultAudience, this.applicationId, this.validateSameFbidAsToken, delegate, this.authId);
        }

        Object writeReplace() {
            return new AuthRequestSerializationProxyV1(this.loginBehavior, this.requestCode, this.permissions, this.defaultAudience.name(), this.isLegacy, this.applicationId, this.validateSameFbidAsToken, null);
        }

        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Cannot readObject, serialization proxy required");
        }

        /* loaded from: classes.dex */
        private static class AuthRequestSerializationProxyV1 implements Serializable {
            private static final long serialVersionUID = -8748347685113614927L;
            private final String applicationId;
            private final String defaultAudience;
            private boolean isLegacy;
            private final SessionLoginBehavior loginBehavior;
            private final List<String> permissions;
            private final int requestCode;
            private final String validateSameFbidAsToken;

            private AuthRequestSerializationProxyV1(SessionLoginBehavior loginBehavior, int requestCode, List<String> permissions, String defaultAudience, boolean isLegacy, String applicationId, String validateSameFbidAsToken) {
                this.loginBehavior = loginBehavior;
                this.requestCode = requestCode;
                this.permissions = permissions;
                this.defaultAudience = defaultAudience;
                this.isLegacy = isLegacy;
                this.applicationId = applicationId;
                this.validateSameFbidAsToken = validateSameFbidAsToken;
            }

            /* synthetic */ AuthRequestSerializationProxyV1(SessionLoginBehavior sessionLoginBehavior, int i, List list, String str, boolean z, String str2, String str3, AuthRequestSerializationProxyV1 authRequestSerializationProxyV1) {
                this(sessionLoginBehavior, i, list, str, z, str2, str3);
            }

            private Object readResolve() {
                return new AuthorizationRequest(this.loginBehavior, this.requestCode, this.permissions, this.defaultAudience, this.isLegacy, this.applicationId, this.validateSameFbidAsToken, null);
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class OpenRequest extends AuthorizationRequest {
        private static final long serialVersionUID = 1;

        @Override // com.facebook.Session.AuthorizationRequest
        public /* bridge */ /* synthetic */ AuthorizationRequest setPermissions(List list) {
            return setPermissions((List<String>) list);
        }

        public OpenRequest(Activity activity) {
            super(activity);
        }

        public OpenRequest(Fragment fragment) {
            super(fragment);
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final OpenRequest setCallback(StatusCallback statusCallback) {
            super.setCallback(statusCallback);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final OpenRequest setLoginBehavior(SessionLoginBehavior loginBehavior) {
            super.setLoginBehavior(loginBehavior);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final OpenRequest setRequestCode(int requestCode) {
            super.setRequestCode(requestCode);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final OpenRequest setPermissions(List<String> permissions) {
            super.setPermissions(permissions);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final OpenRequest setPermissions(String... permissions) {
            super.setPermissions(permissions);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final OpenRequest setDefaultAudience(SessionDefaultAudience defaultAudience) {
            super.setDefaultAudience(defaultAudience);
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static final class NewPermissionsRequest extends AuthorizationRequest {
        private static final long serialVersionUID = 1;

        public NewPermissionsRequest(Activity activity, List<String> permissions) {
            super(activity);
            setPermissions(permissions);
        }

        public NewPermissionsRequest(Fragment fragment, List<String> permissions) {
            super(fragment);
            setPermissions(permissions);
        }

        public NewPermissionsRequest(Activity activity, String... permissions) {
            super(activity);
            setPermissions(permissions);
        }

        public NewPermissionsRequest(Fragment fragment, String... permissions) {
            super(fragment);
            setPermissions(permissions);
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final NewPermissionsRequest setCallback(StatusCallback statusCallback) {
            super.setCallback(statusCallback);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final NewPermissionsRequest setLoginBehavior(SessionLoginBehavior loginBehavior) {
            super.setLoginBehavior(loginBehavior);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final NewPermissionsRequest setRequestCode(int requestCode) {
            super.setRequestCode(requestCode);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        public final NewPermissionsRequest setDefaultAudience(SessionDefaultAudience defaultAudience) {
            super.setDefaultAudience(defaultAudience);
            return this;
        }

        @Override // com.facebook.Session.AuthorizationRequest
        AuthorizationClient.AuthorizationRequest getAuthorizationClientRequest() {
            AuthorizationClient.AuthorizationRequest request = super.getAuthorizationClientRequest();
            request.setRerequest(true);
            return request;
        }
    }
}
