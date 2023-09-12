package com.facebook.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.SessionDefaultAudience;
import com.facebook.Settings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
/* loaded from: classes.dex */
public final class NativeProtocol {
    public static final String ACTION_FEED_DIALOG = "com.facebook.platform.action.request.FEED_DIALOG";
    public static final String ACTION_FEED_DIALOG_REPLY = "com.facebook.platform.action.reply.FEED_DIALOG";
    public static final String ACTION_LIKE_DIALOG = "com.facebook.platform.action.request.LIKE_DIALOG";
    public static final String ACTION_LIKE_DIALOG_REPLY = "com.facebook.platform.action.reply.LIKE_DIALOG";
    public static final String ACTION_MESSAGE_DIALOG = "com.facebook.platform.action.request.MESSAGE_DIALOG";
    public static final String ACTION_MESSAGE_DIALOG_REPLY = "com.facebook.platform.action.reply.MESSAGE_DIALOG";
    public static final String ACTION_OGACTIONPUBLISH_DIALOG = "com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG";
    public static final String ACTION_OGACTIONPUBLISH_DIALOG_REPLY = "com.facebook.platform.action.reply.OGACTIONPUBLISH_DIALOG";
    public static final String ACTION_OGMESSAGEPUBLISH_DIALOG = "com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG";
    public static final String ACTION_OGMESSAGEPUBLISH_DIALOG_REPLY = "com.facebook.platform.action.reply.OGMESSAGEPUBLISH_DIALOG";
    public static final String AUDIENCE_EVERYONE = "everyone";
    public static final String AUDIENCE_FRIENDS = "friends";
    public static final String AUDIENCE_ME = "only_me";
    public static final String BRIDGE_ARG_ACTION_ID_STRING = "action_id";
    public static final String BRIDGE_ARG_APP_NAME_STRING = "app_name";
    public static final String BRIDGE_ARG_ERROR_BUNDLE = "error";
    private static final String CONTENT_SCHEME = "content://";
    public static final int DIALOG_REQUEST_CODE = 64207;
    public static final String ERROR_APPLICATION_ERROR = "ApplicationError";
    public static final String ERROR_NETWORK_ERROR = "NetworkError";
    public static final String ERROR_PERMISSION_DENIED = "PermissionDenied";
    public static final String ERROR_PROTOCOL_ERROR = "ProtocolError";
    public static final String ERROR_SERVICE_DISABLED = "ServiceDisabled";
    public static final String ERROR_UNKNOWN_ERROR = "UnknownError";
    public static final String ERROR_USER_CANCELED = "UserCanceled";
    public static final String EXTRA_ACCESS_TOKEN = "com.facebook.platform.extra.ACCESS_TOKEN";
    public static final String EXTRA_ACTION = "com.facebook.platform.extra.ACTION";
    public static final String EXTRA_ACTION_TYPE = "com.facebook.platform.extra.ACTION_TYPE";
    public static final String EXTRA_APPLICATION_ID = "com.facebook.platform.extra.APPLICATION_ID";
    public static final String EXTRA_APPLICATION_NAME = "com.facebook.platform.extra.APPLICATION_NAME";
    public static final String EXTRA_DATA_FAILURES_FATAL = "com.facebook.platform.extra.DATA_FAILURES_FATAL";
    public static final String EXTRA_DESCRIPTION = "com.facebook.platform.extra.DESCRIPTION";
    public static final String EXTRA_EXPIRES_SECONDS_SINCE_EPOCH = "com.facebook.platform.extra.EXPIRES_SECONDS_SINCE_EPOCH";
    public static final String EXTRA_FRIEND_TAGS = "com.facebook.platform.extra.FRIENDS";
    public static final String EXTRA_GET_INSTALL_DATA_PACKAGE = "com.facebook.platform.extra.INSTALLDATA_PACKAGE";
    public static final String EXTRA_IMAGE = "com.facebook.platform.extra.IMAGE";
    public static final String EXTRA_LIKE_COUNT_STRING_WITHOUT_LIKE = "com.facebook.platform.extra.LIKE_COUNT_STRING_WITHOUT_LIKE";
    public static final String EXTRA_LIKE_COUNT_STRING_WITH_LIKE = "com.facebook.platform.extra.LIKE_COUNT_STRING_WITH_LIKE";
    public static final String EXTRA_LINK = "com.facebook.platform.extra.LINK";
    public static final String EXTRA_OBJECT_ID = "com.facebook.platform.extra.OBJECT_ID";
    public static final String EXTRA_OBJECT_IS_LIKED = "com.facebook.platform.extra.OBJECT_IS_LIKED";
    public static final String EXTRA_PERMISSIONS = "com.facebook.platform.extra.PERMISSIONS";
    public static final String EXTRA_PHOTOS = "com.facebook.platform.extra.PHOTOS";
    public static final String EXTRA_PLACE_TAG = "com.facebook.platform.extra.PLACE";
    public static final String EXTRA_PREVIEW_PROPERTY_NAME = "com.facebook.platform.extra.PREVIEW_PROPERTY_NAME";
    public static final String EXTRA_PROTOCOL_ACTION = "com.facebook.platform.protocol.PROTOCOL_ACTION";
    public static final String EXTRA_PROTOCOL_BRIDGE_ARGS = "com.facebook.platform.protocol.BRIDGE_ARGS";
    public static final String EXTRA_PROTOCOL_CALL_ID = "com.facebook.platform.protocol.CALL_ID";
    public static final String EXTRA_PROTOCOL_METHOD_ARGS = "com.facebook.platform.protocol.METHOD_ARGS";
    public static final String EXTRA_PROTOCOL_METHOD_RESULTS = "com.facebook.platform.protocol.RESULT_ARGS";
    public static final String EXTRA_PROTOCOL_VERSION = "com.facebook.platform.protocol.PROTOCOL_VERSION";
    static final String EXTRA_PROTOCOL_VERSIONS = "com.facebook.platform.extra.PROTOCOL_VERSIONS";
    public static final String EXTRA_REF = "com.facebook.platform.extra.REF";
    public static final String EXTRA_SOCIAL_SENTENCE_WITHOUT_LIKE = "com.facebook.platform.extra.SOCIAL_SENTENCE_WITHOUT_LIKE";
    public static final String EXTRA_SOCIAL_SENTENCE_WITH_LIKE = "com.facebook.platform.extra.SOCIAL_SENTENCE_WITH_LIKE";
    public static final String EXTRA_SUBTITLE = "com.facebook.platform.extra.SUBTITLE";
    public static final String EXTRA_TITLE = "com.facebook.platform.extra.TITLE";
    public static final String EXTRA_UNLIKE_TOKEN = "com.facebook.platform.extra.UNLIKE_TOKEN";
    private static final String FACEBOOK_PROXY_AUTH_ACTIVITY = "com.facebook.katana.ProxyAuth";
    public static final String FACEBOOK_PROXY_AUTH_APP_ID_KEY = "client_id";
    public static final String FACEBOOK_PROXY_AUTH_E2E_KEY = "e2e";
    public static final String FACEBOOK_PROXY_AUTH_PERMISSIONS_KEY = "scope";
    private static final String FACEBOOK_TOKEN_REFRESH_ACTIVITY = "com.facebook.katana.platform.TokenRefreshService";
    public static final String IMAGE_URL_KEY = "url";
    public static final String IMAGE_USER_GENERATED_KEY = "user_generated";
    static final String INTENT_ACTION_PLATFORM_ACTIVITY = "com.facebook.platform.PLATFORM_ACTIVITY";
    static final String INTENT_ACTION_PLATFORM_SERVICE = "com.facebook.platform.PLATFORM_SERVICE";
    public static final int MESSAGE_GET_ACCESS_TOKEN_REPLY = 65537;
    public static final int MESSAGE_GET_ACCESS_TOKEN_REQUEST = 65536;
    public static final int MESSAGE_GET_INSTALL_DATA_REPLY = 65541;
    public static final int MESSAGE_GET_INSTALL_DATA_REQUEST = 65540;
    public static final int MESSAGE_GET_LIKE_STATUS_REPLY = 65543;
    public static final int MESSAGE_GET_LIKE_STATUS_REQUEST = 65542;
    static final int MESSAGE_GET_PROTOCOL_VERSIONS_REPLY = 65539;
    static final int MESSAGE_GET_PROTOCOL_VERSIONS_REQUEST = 65538;
    public static final String METHOD_ARGS_ACTION = "ACTION";
    public static final String METHOD_ARGS_ACTION_TYPE = "ACTION_TYPE";
    public static final String METHOD_ARGS_DATA_FAILURES_FATAL = "DATA_FAILURES_FATAL";
    public static final String METHOD_ARGS_DESCRIPTION = "DESCRIPTION";
    public static final String METHOD_ARGS_FRIEND_TAGS = "FRIENDS";
    public static final String METHOD_ARGS_IMAGE = "IMAGE";
    public static final String METHOD_ARGS_LINK = "LINK";
    public static final String METHOD_ARGS_OBJECT_ID = "object_id";
    public static final String METHOD_ARGS_PHOTOS = "PHOTOS";
    public static final String METHOD_ARGS_PLACE_TAG = "PLACE";
    public static final String METHOD_ARGS_PREVIEW_PROPERTY_NAME = "PREVIEW_PROPERTY_NAME";
    public static final String METHOD_ARGS_REF = "REF";
    public static final String METHOD_ARGS_SUBTITLE = "SUBTITLE";
    public static final String METHOD_ARGS_TITLE = "TITLE";
    public static final int NO_PROTOCOL_AVAILABLE = -1;
    public static final String OPEN_GRAPH_CREATE_OBJECT_KEY = "fbsdk:create_object";
    private static final String PLATFORM_PROVIDER_VERSIONS = ".provider.PlatformProvider/versions";
    private static final String PLATFORM_PROVIDER_VERSION_COLUMN = "version";
    public static final String STATUS_ERROR_CODE = "com.facebook.platform.status.ERROR_CODE";
    public static final String STATUS_ERROR_DESCRIPTION = "com.facebook.platform.status.ERROR_DESCRIPTION";
    public static final String STATUS_ERROR_JSON = "com.facebook.platform.status.ERROR_JSON";
    public static final String STATUS_ERROR_SUBCODE = "com.facebook.platform.status.ERROR_SUBCODE";
    public static final String STATUS_ERROR_TYPE = "com.facebook.platform.status.ERROR_TYPE";
    private static final NativeAppInfo FACEBOOK_APP_INFO = new KatanaAppInfo(null);
    private static List<NativeAppInfo> facebookAppInfoList = buildFacebookAppList();
    private static Map<String, List<NativeAppInfo>> actionToAppInfoMap = buildActionToAppInfoMap();
    public static final int PROTOCOL_VERSION_20141001 = 20141001;
    public static final int PROTOCOL_VERSION_20140701 = 20140701;
    public static final int PROTOCOL_VERSION_20140324 = 20140324;
    public static final int PROTOCOL_VERSION_20140204 = 20140204;
    public static final int PROTOCOL_VERSION_20131107 = 20131107;
    public static final int PROTOCOL_VERSION_20130618 = 20130618;
    public static final int PROTOCOL_VERSION_20130502 = 20130502;
    public static final int PROTOCOL_VERSION_20121101 = 20121101;
    private static final List<Integer> KNOWN_PROTOCOL_VERSIONS = Arrays.asList(Integer.valueOf((int) PROTOCOL_VERSION_20141001), Integer.valueOf((int) PROTOCOL_VERSION_20140701), Integer.valueOf((int) PROTOCOL_VERSION_20140324), Integer.valueOf((int) PROTOCOL_VERSION_20140204), Integer.valueOf((int) PROTOCOL_VERSION_20131107), Integer.valueOf((int) PROTOCOL_VERSION_20130618), Integer.valueOf((int) PROTOCOL_VERSION_20130502), Integer.valueOf((int) PROTOCOL_VERSION_20121101));

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static abstract class NativeAppInfo {
        private static final String FBI_HASH = "a4b7452e2ed8f5f191058ca7bbfd26b0d3214bfc";
        private static final String FBL_HASH = "5e8f16062ea3cd2c4a0d547876baa6f38cabf625";
        private static final String FBR_HASH = "8a3c4b262d721acd49a4bf97d5213199c86fa2b9";
        private static final HashSet<String> validAppSignatureHashes = buildAppSignatureHashes();

        protected abstract String getPackage();

        private NativeAppInfo() {
        }

        /* synthetic */ NativeAppInfo(NativeAppInfo nativeAppInfo) {
            this();
        }

        private static HashSet<String> buildAppSignatureHashes() {
            HashSet<String> set = new HashSet<>();
            set.add(FBR_HASH);
            set.add(FBI_HASH);
            set.add(FBL_HASH);
            return set;
        }

        public boolean validateSignature(Context context, String packageName) {
            Signature[] signatureArr;
            String brand = Build.BRAND;
            int applicationFlags = context.getApplicationInfo().flags;
            if (!brand.startsWith("generic") || (applicationFlags & 2) == 0) {
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 64);
                    for (Signature signature : packageInfo.signatures) {
                        String hashedSignature = Utility.sha1hash(signature.toByteArray());
                        if (validAppSignatureHashes.contains(hashedSignature)) {
                            return true;
                        }
                    }
                    return false;
                } catch (PackageManager.NameNotFoundException e) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: classes.dex */
    private static class KatanaAppInfo extends NativeAppInfo {
        static final String KATANA_PACKAGE = "com.facebook.katana";

        private KatanaAppInfo() {
            super(null);
        }

        /* synthetic */ KatanaAppInfo(KatanaAppInfo katanaAppInfo) {
            this();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return "com.facebook.katana";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MessengerAppInfo extends NativeAppInfo {
        static final String MESSENGER_PACKAGE = "com.facebook.orca";

        private MessengerAppInfo() {
            super(null);
        }

        /* synthetic */ MessengerAppInfo(MessengerAppInfo messengerAppInfo) {
            this();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return "com.facebook.orca";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class WakizashiAppInfo extends NativeAppInfo {
        static final String WAKIZASHI_PACKAGE = "com.facebook.wakizashi";

        private WakizashiAppInfo() {
            super(null);
        }

        /* synthetic */ WakizashiAppInfo(WakizashiAppInfo wakizashiAppInfo) {
            this();
        }

        @Override // com.facebook.internal.NativeProtocol.NativeAppInfo
        protected String getPackage() {
            return WAKIZASHI_PACKAGE;
        }
    }

    private static List<NativeAppInfo> buildFacebookAppList() {
        List<NativeAppInfo> list = new ArrayList<>();
        list.add(FACEBOOK_APP_INFO);
        list.add(new WakizashiAppInfo(null));
        return list;
    }

    private static Map<String, List<NativeAppInfo>> buildActionToAppInfoMap() {
        Map<String, List<NativeAppInfo>> map = new HashMap<>();
        ArrayList<NativeAppInfo> messengerAppInfoList = new ArrayList<>();
        messengerAppInfoList.add(new MessengerAppInfo(null));
        map.put(ACTION_OGACTIONPUBLISH_DIALOG, facebookAppInfoList);
        map.put(ACTION_FEED_DIALOG, facebookAppInfoList);
        map.put(ACTION_LIKE_DIALOG, facebookAppInfoList);
        map.put(ACTION_MESSAGE_DIALOG, messengerAppInfoList);
        map.put(ACTION_OGMESSAGEPUBLISH_DIALOG, messengerAppInfoList);
        return map;
    }

    static Intent validateActivityIntent(Context context, Intent intent, NativeAppInfo appInfo) {
        ResolveInfo resolveInfo;
        if (intent == null || (resolveInfo = context.getPackageManager().resolveActivity(intent, 0)) == null || !appInfo.validateSignature(context, resolveInfo.activityInfo.packageName)) {
            return null;
        }
        return intent;
    }

    static Intent validateServiceIntent(Context context, Intent intent, NativeAppInfo appInfo) {
        ResolveInfo resolveInfo;
        if (intent == null || (resolveInfo = context.getPackageManager().resolveService(intent, 0)) == null || !appInfo.validateSignature(context, resolveInfo.serviceInfo.packageName)) {
            return null;
        }
        return intent;
    }

    public static Intent createProxyAuthIntent(Context context, String applicationId, List<String> permissions, String e2e, boolean isRerequest, SessionDefaultAudience defaultAudience) {
        for (NativeAppInfo appInfo : facebookAppInfoList) {
            Intent intent = new Intent().setClassName(appInfo.getPackage(), FACEBOOK_PROXY_AUTH_ACTIVITY).putExtra("client_id", applicationId);
            if (!Utility.isNullOrEmpty(permissions)) {
                intent.putExtra("scope", TextUtils.join(",", permissions));
            }
            if (!Utility.isNullOrEmpty(e2e)) {
                intent.putExtra("e2e", e2e);
            }
            intent.putExtra(ServerProtocol.DIALOG_PARAM_RESPONSE_TYPE, ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN);
            intent.putExtra(ServerProtocol.DIALOG_PARAM_RETURN_SCOPES, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
            intent.putExtra(ServerProtocol.DIALOG_PARAM_DEFAULT_AUDIENCE, defaultAudience.getNativeProtocolAudience());
            if (!Settings.getPlatformCompatibilityEnabled()) {
                intent.putExtra(ServerProtocol.DIALOG_PARAM_LEGACY_OVERRIDE, ServerProtocol.GRAPH_API_VERSION);
                if (isRerequest) {
                    intent.putExtra(ServerProtocol.DIALOG_PARAM_AUTH_TYPE, ServerProtocol.DIALOG_REREQUEST_AUTH_TYPE);
                }
            }
            Intent intent2 = validateActivityIntent(context, intent, appInfo);
            if (intent2 != null) {
                return intent2;
            }
        }
        return null;
    }

    public static Intent createTokenRefreshIntent(Context context) {
        for (NativeAppInfo appInfo : facebookAppInfoList) {
            Intent intent = validateServiceIntent(context, new Intent().setClassName(appInfo.getPackage(), FACEBOOK_TOKEN_REFRESH_ACTIVITY), appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return null;
    }

    public static final int getLatestKnownVersion() {
        return KNOWN_PROTOCOL_VERSIONS.get(0).intValue();
    }

    private static Intent findActivityIntent(Context context, String activityAction, String internalAction) {
        List<NativeAppInfo> list = actionToAppInfoMap.get(internalAction);
        if (list == null) {
            return null;
        }
        Intent intent = null;
        for (NativeAppInfo appInfo : list) {
            Intent intent2 = new Intent().setAction(activityAction).setPackage(appInfo.getPackage()).addCategory("android.intent.category.DEFAULT");
            intent = validateActivityIntent(context, intent2, appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return intent;
    }

    public static boolean isVersionCompatibleWithBucketedIntent(int version) {
        return KNOWN_PROTOCOL_VERSIONS.contains(Integer.valueOf(version)) && version >= 20140701;
    }

    public static Intent createPlatformActivityIntent(Context context, String callId, String action, int version, String applicationName, Bundle extras) {
        Intent intent = findActivityIntent(context, INTENT_ACTION_PLATFORM_ACTIVITY, action);
        if (intent == null) {
            return null;
        }
        String applicationId = Utility.getMetadataApplicationId(context);
        intent.putExtra(EXTRA_PROTOCOL_VERSION, version).putExtra(EXTRA_PROTOCOL_ACTION, action).putExtra(EXTRA_APPLICATION_ID, applicationId);
        if (isVersionCompatibleWithBucketedIntent(version)) {
            Bundle bridgeArguments = new Bundle();
            bridgeArguments.putString("action_id", callId);
            bridgeArguments.putString(BRIDGE_ARG_APP_NAME_STRING, applicationName);
            intent.putExtra(EXTRA_PROTOCOL_BRIDGE_ARGS, bridgeArguments);
            Bundle methodArguments = extras == null ? new Bundle() : extras;
            intent.putExtra(EXTRA_PROTOCOL_METHOD_ARGS, methodArguments);
            return intent;
        }
        intent.putExtra(EXTRA_PROTOCOL_CALL_ID, callId);
        intent.putExtra(EXTRA_APPLICATION_NAME, applicationName);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent createPlatformServiceIntent(Context context) {
        for (NativeAppInfo appInfo : facebookAppInfoList) {
            Intent intent = validateServiceIntent(context, new Intent(INTENT_ACTION_PLATFORM_SERVICE).setPackage(appInfo.getPackage()).addCategory("android.intent.category.DEFAULT"), appInfo);
            if (intent != null) {
                return intent;
            }
        }
        return null;
    }

    public static int getProtocolVersionFromIntent(Intent intent) {
        return intent.getIntExtra(EXTRA_PROTOCOL_VERSION, 0);
    }

    public static UUID getCallIdFromIntent(Intent intent) {
        if (intent == null) {
            return null;
        }
        int version = getProtocolVersionFromIntent(intent);
        String callIdString = null;
        if (isVersionCompatibleWithBucketedIntent(version)) {
            Bundle bridgeArgs = intent.getBundleExtra(EXTRA_PROTOCOL_BRIDGE_ARGS);
            if (bridgeArgs != null) {
                callIdString = bridgeArgs.getString("action_id");
            }
        } else {
            callIdString = intent.getStringExtra(EXTRA_PROTOCOL_CALL_ID);
        }
        if (callIdString == null) {
            return null;
        }
        try {
            UUID callId = UUID.fromString(callIdString);
            return callId;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Bundle getBridgeArgumentsFromIntent(Intent intent) {
        int version = getProtocolVersionFromIntent(intent);
        if (isVersionCompatibleWithBucketedIntent(version)) {
            return intent.getBundleExtra(EXTRA_PROTOCOL_BRIDGE_ARGS);
        }
        return null;
    }

    public static Bundle getSuccessResultsFromIntent(Intent resultIntent) {
        int version = getProtocolVersionFromIntent(resultIntent);
        Bundle extras = resultIntent.getExtras();
        return (!isVersionCompatibleWithBucketedIntent(version) || extras == null) ? extras : extras.getBundle(EXTRA_PROTOCOL_METHOD_RESULTS);
    }

    public static boolean isErrorResult(Intent resultIntent) {
        Bundle bridgeArgs = getBridgeArgumentsFromIntent(resultIntent);
        return bridgeArgs != null ? bridgeArgs.containsKey("error") : resultIntent.hasExtra(STATUS_ERROR_TYPE);
    }

    public static Bundle getErrorDataFromResultIntent(Intent resultIntent) {
        if (!isErrorResult(resultIntent)) {
            return null;
        }
        Bundle bridgeArgs = getBridgeArgumentsFromIntent(resultIntent);
        if (bridgeArgs != null) {
            return bridgeArgs.getBundle("error");
        }
        return resultIntent.getExtras();
    }

    public static Exception getExceptionFromErrorData(Bundle errorData) {
        if (errorData == null) {
            return null;
        }
        String type = errorData.getString(STATUS_ERROR_TYPE);
        String description = errorData.getString(STATUS_ERROR_DESCRIPTION);
        if (type != null && type.equalsIgnoreCase(ERROR_USER_CANCELED)) {
            return new FacebookOperationCanceledException(description);
        }
        return new FacebookException(description);
    }

    public static int getLatestAvailableProtocolVersionForService(Context context, int minimumVersion) {
        return getLatestAvailableProtocolVersionForAppInfoList(context, facebookAppInfoList, new int[]{minimumVersion});
    }

    public static int getLatestAvailableProtocolVersionForAction(Context context, String action, int[] versionSpec) {
        List<NativeAppInfo> appInfoList = actionToAppInfoMap.get(action);
        return getLatestAvailableProtocolVersionForAppInfoList(context, appInfoList, versionSpec);
    }

    private static int getLatestAvailableProtocolVersionForAppInfoList(Context context, List<NativeAppInfo> appInfoList, int[] versionSpec) {
        if (appInfoList == null) {
            return -1;
        }
        for (NativeAppInfo appInfo : appInfoList) {
            int protocolVersion = getLatestAvailableProtocolVersionForAppInfo(context, appInfo, versionSpec);
            if (protocolVersion != -1) {
                return protocolVersion;
            }
        }
        return -1;
    }

    private static int getLatestAvailableProtocolVersionForAppInfo(Context context, NativeAppInfo appInfo, int[] versionSpec) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {"version"};
        Uri uri = buildPlatformProviderVersionURI(appInfo);
        Cursor c = null;
        try {
            c = contentResolver.query(uri, projection, null, null, null);
            if (c == null) {
                if (c != null) {
                    c.close();
                }
                return -1;
            }
            TreeSet<Integer> fbAppVersions = new TreeSet<>();
            while (c.moveToNext()) {
                int version = c.getInt(c.getColumnIndex("version"));
                fbAppVersions.add(Integer.valueOf(version));
            }
            int versionSpecIndex = versionSpec.length - 1;
            Iterator<Integer> fbAppVersionsIterator = fbAppVersions.descendingIterator();
            int latestAllowedVersion = getLatestKnownVersion();
            while (fbAppVersionsIterator.hasNext()) {
                int fbAppVersion = fbAppVersionsIterator.next().intValue();
                while (versionSpecIndex >= 0 && versionSpec[versionSpecIndex] > fbAppVersion) {
                    versionSpecIndex--;
                }
                if (versionSpecIndex < 0) {
                    if (c != null) {
                        c.close();
                    }
                    return -1;
                } else if (versionSpec[versionSpecIndex] == fbAppVersion) {
                    int min = versionSpecIndex % 2 == 0 ? Math.min(fbAppVersion, latestAllowedVersion) : -1;
                }
            }
            if (c != null) {
                c.close();
            }
            return -1;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private static Uri buildPlatformProviderVersionURI(NativeAppInfo appInfo) {
        return Uri.parse(CONTENT_SCHEME + appInfo.getPackage() + PLATFORM_PROVIDER_VERSIONS);
    }
}
