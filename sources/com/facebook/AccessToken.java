package com.facebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
/* loaded from: classes.dex */
public final class AccessToken implements Serializable {
    static final String ACCESS_TOKEN_KEY = "access_token";
    static final String EXPIRES_IN_KEY = "expires_in";
    private static final long serialVersionUID = 1;
    private final List<String> declinedPermissions;
    private final Date expires;
    private final Date lastRefresh;
    private final List<String> permissions;
    private final AccessTokenSource source;
    private final String token;
    private static final Date MIN_DATE = new Date(Long.MIN_VALUE);
    private static final Date MAX_DATE = new Date(Long.MAX_VALUE);
    private static final Date DEFAULT_EXPIRATION_TIME = MAX_DATE;
    private static final Date DEFAULT_LAST_REFRESH_TIME = new Date();
    private static final AccessTokenSource DEFAULT_ACCESS_TOKEN_SOURCE = AccessTokenSource.FACEBOOK_APPLICATION_WEB;
    private static final Date ALREADY_EXPIRED_EXPIRATION_TIME = MIN_DATE;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessToken(String token, Date expires, List<String> permissions, List<String> declinedPermissions, AccessTokenSource source, Date lastRefresh) {
        permissions = permissions == null ? Collections.emptyList() : permissions;
        declinedPermissions = declinedPermissions == null ? Collections.emptyList() : declinedPermissions;
        this.expires = expires;
        this.permissions = Collections.unmodifiableList(permissions);
        this.declinedPermissions = Collections.unmodifiableList(declinedPermissions);
        this.token = token;
        this.source = source;
        this.lastRefresh = lastRefresh;
    }

    public String getToken() {
        return this.token;
    }

    public Date getExpires() {
        return this.expires;
    }

    public List<String> getPermissions() {
        return this.permissions;
    }

    public List<String> getDeclinedPermissions() {
        return this.declinedPermissions;
    }

    public AccessTokenSource getSource() {
        return this.source;
    }

    public Date getLastRefresh() {
        return this.lastRefresh;
    }

    public static AccessToken createFromExistingAccessToken(String accessToken, Date expirationTime, Date lastRefreshTime, AccessTokenSource accessTokenSource, List<String> permissions) {
        if (expirationTime == null) {
            expirationTime = DEFAULT_EXPIRATION_TIME;
        }
        if (lastRefreshTime == null) {
            lastRefreshTime = DEFAULT_LAST_REFRESH_TIME;
        }
        if (accessTokenSource == null) {
            accessTokenSource = DEFAULT_ACCESS_TOKEN_SOURCE;
        }
        return new AccessToken(accessToken, expirationTime, permissions, null, accessTokenSource, lastRefreshTime);
    }

    public static AccessToken createFromNativeLinkingIntent(Intent intent) {
        Validate.notNull(intent, "intent");
        if (intent.getExtras() == null) {
            return null;
        }
        return createFromBundle(null, intent.getExtras(), AccessTokenSource.FACEBOOK_APPLICATION_WEB, new Date());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{AccessToken");
        builder.append(" token:").append(tokenToString());
        appendPermissions(builder);
        builder.append("}");
        return builder.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessToken createEmptyToken() {
        return new AccessToken("", ALREADY_EXPIRED_EXPIRATION_TIME, null, null, AccessTokenSource.NONE, DEFAULT_LAST_REFRESH_TIME);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessToken createFromString(String token, List<String> permissions, AccessTokenSource source) {
        return new AccessToken(token, DEFAULT_EXPIRATION_TIME, permissions, null, source, DEFAULT_LAST_REFRESH_TIME);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessToken createFromNativeLogin(Bundle bundle, AccessTokenSource source) {
        Date expires = getBundleLongAsDate(bundle, NativeProtocol.EXTRA_EXPIRES_SECONDS_SINCE_EPOCH, new Date(0L));
        ArrayList<String> permissions = bundle.getStringArrayList(NativeProtocol.EXTRA_PERMISSIONS);
        String token = bundle.getString(NativeProtocol.EXTRA_ACCESS_TOKEN);
        return createNew(permissions, null, token, expires, source);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessToken createFromWebBundle(List<String> requestedPermissions, Bundle bundle, AccessTokenSource source) {
        Date expires = getBundleLongAsDate(bundle, "expires_in", new Date());
        String token = bundle.getString("access_token");
        String grantedPermissions = bundle.getString("granted_scopes");
        if (!Utility.isNullOrEmpty(grantedPermissions)) {
            requestedPermissions = new ArrayList<>(Arrays.asList(grantedPermissions.split(",")));
        }
        String deniedPermissions = bundle.getString("denied_scopes");
        List<String> declinedPermissions = null;
        if (!Utility.isNullOrEmpty(deniedPermissions)) {
            declinedPermissions = new ArrayList<>(Arrays.asList(deniedPermissions.split(",")));
        }
        return createNew(requestedPermissions, declinedPermissions, token, expires, source);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"FieldGetter"})
    public static AccessToken createFromRefresh(AccessToken current, Bundle bundle) {
        if (current.source != AccessTokenSource.FACEBOOK_APPLICATION_WEB && current.source != AccessTokenSource.FACEBOOK_APPLICATION_NATIVE && current.source != AccessTokenSource.FACEBOOK_APPLICATION_SERVICE) {
            throw new FacebookException("Invalid token source: " + current.source);
        }
        Date expires = getBundleLongAsDate(bundle, "expires_in", new Date(0L));
        String token = bundle.getString("access_token");
        return createNew(current.getPermissions(), current.getDeclinedPermissions(), token, expires, current.source);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessToken createFromTokenWithRefreshedPermissions(AccessToken token, List<String> grantedPermissions, List<String> declinedPermissions) {
        return new AccessToken(token.token, token.expires, grantedPermissions, declinedPermissions, token.source, token.lastRefresh);
    }

    private static AccessToken createNew(List<String> grantedPermissions, List<String> declinedPermissions, String accessToken, Date expires, AccessTokenSource source) {
        return (Utility.isNullOrEmpty(accessToken) || expires == null) ? createEmptyToken() : new AccessToken(accessToken, expires, grantedPermissions, declinedPermissions, source, new Date());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AccessToken createFromCache(Bundle bundle) {
        List<String> permissions = getPermissionsFromBundle(bundle, TokenCachingStrategy.PERMISSIONS_KEY);
        List<String> declinedPermissions = getPermissionsFromBundle(bundle, TokenCachingStrategy.DECLINED_PERMISSIONS_KEY);
        return new AccessToken(bundle.getString(TokenCachingStrategy.TOKEN_KEY), TokenCachingStrategy.getDate(bundle, TokenCachingStrategy.EXPIRATION_DATE_KEY), permissions, declinedPermissions, TokenCachingStrategy.getSource(bundle), TokenCachingStrategy.getDate(bundle, TokenCachingStrategy.LAST_REFRESH_DATE_KEY));
    }

    static List<String> getPermissionsFromBundle(Bundle bundle, String key) {
        List<String> originalPermissions = bundle.getStringArrayList(key);
        if (originalPermissions == null) {
            List<String> permissions = Collections.emptyList();
            return permissions;
        }
        List<String> permissions2 = Collections.unmodifiableList(new ArrayList(originalPermissions));
        return permissions2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle toCacheBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(TokenCachingStrategy.TOKEN_KEY, this.token);
        TokenCachingStrategy.putDate(bundle, TokenCachingStrategy.EXPIRATION_DATE_KEY, this.expires);
        bundle.putStringArrayList(TokenCachingStrategy.PERMISSIONS_KEY, new ArrayList<>(this.permissions));
        bundle.putStringArrayList(TokenCachingStrategy.DECLINED_PERMISSIONS_KEY, new ArrayList<>(this.declinedPermissions));
        bundle.putSerializable(TokenCachingStrategy.TOKEN_SOURCE_KEY, this.source);
        TokenCachingStrategy.putDate(bundle, TokenCachingStrategy.LAST_REFRESH_DATE_KEY, this.lastRefresh);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInvalid() {
        return Utility.isNullOrEmpty(this.token) || new Date().after(this.expires);
    }

    private static AccessToken createFromBundle(List<String> requestedPermissions, Bundle bundle, AccessTokenSource source, Date expirationBase) {
        String token = bundle.getString("access_token");
        Date expires = getBundleLongAsDate(bundle, "expires_in", expirationBase);
        if (Utility.isNullOrEmpty(token) || expires == null) {
            return null;
        }
        return new AccessToken(token, expires, requestedPermissions, null, source, new Date());
    }

    private String tokenToString() {
        if (this.token == null) {
            return "null";
        }
        if (Settings.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS)) {
            return this.token;
        }
        return "ACCESS_TOKEN_REMOVED";
    }

    private void appendPermissions(StringBuilder builder) {
        builder.append(" permissions:");
        if (this.permissions == null) {
            builder.append("null");
            return;
        }
        builder.append("[");
        builder.append(TextUtils.join(", ", this.permissions));
        builder.append("]");
    }

    /* loaded from: classes.dex */
    private static class SerializationProxyV1 implements Serializable {
        private static final long serialVersionUID = -2488473066578201069L;
        private final Date expires;
        private final Date lastRefresh;
        private final List<String> permissions;
        private final AccessTokenSource source;
        private final String token;

        private SerializationProxyV1(String token, Date expires, List<String> permissions, AccessTokenSource source, Date lastRefresh) {
            this.expires = expires;
            this.permissions = permissions;
            this.token = token;
            this.source = source;
            this.lastRefresh = lastRefresh;
        }

        private Object readResolve() {
            return new AccessToken(this.token, this.expires, this.permissions, null, this.source, this.lastRefresh);
        }
    }

    /* loaded from: classes.dex */
    private static class SerializationProxyV2 implements Serializable {
        private static final long serialVersionUID = -2488473066578201068L;
        private final List<String> declinedPermissions;
        private final Date expires;
        private final Date lastRefresh;
        private final List<String> permissions;
        private final AccessTokenSource source;
        private final String token;

        private SerializationProxyV2(String token, Date expires, List<String> permissions, List<String> declinedPermissions, AccessTokenSource source, Date lastRefresh) {
            this.expires = expires;
            this.permissions = permissions;
            this.declinedPermissions = declinedPermissions;
            this.token = token;
            this.source = source;
            this.lastRefresh = lastRefresh;
        }

        /* synthetic */ SerializationProxyV2(String str, Date date, List list, List list2, AccessTokenSource accessTokenSource, Date date2, SerializationProxyV2 serializationProxyV2) {
            this(str, date, list, list2, accessTokenSource, date2);
        }

        private Object readResolve() {
            return new AccessToken(this.token, this.expires, this.permissions, this.declinedPermissions, this.source, this.lastRefresh);
        }
    }

    private Object writeReplace() {
        return new SerializationProxyV2(this.token, this.expires, this.permissions, this.declinedPermissions, this.source, this.lastRefresh, null);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Cannot readObject, serialization proxy required");
    }

    private static Date getBundleLongAsDate(Bundle bundle, String key, Date dateBase) {
        long secondsFromBase;
        if (bundle == null) {
            return null;
        }
        Object secondsObject = bundle.get(key);
        if (secondsObject instanceof Long) {
            secondsFromBase = ((Long) secondsObject).longValue();
        } else if (!(secondsObject instanceof String)) {
            return null;
        } else {
            try {
                secondsFromBase = Long.parseLong((String) secondsObject);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        if (secondsFromBase == 0) {
            return new Date(Long.MAX_VALUE);
        }
        return new Date(dateBase.getTime() + (1000 * secondsFromBase));
    }
}
