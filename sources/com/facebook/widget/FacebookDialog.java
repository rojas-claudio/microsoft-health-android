package com.facebook.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import com.facebook.AppEventsLogger;
import com.facebook.FacebookException;
import com.facebook.FacebookGraphObjectException;
import com.facebook.NativeAppCallAttachmentStore;
import com.facebook.NativeAppCallContentProvider;
import com.facebook.Settings;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class FacebookDialog {
    public static final String COMPLETION_GESTURE_CANCEL = "cancel";
    private static final String EXTRA_DIALOG_COMPLETE_KEY = "com.facebook.platform.extra.DID_COMPLETE";
    private static final String EXTRA_DIALOG_COMPLETION_GESTURE_KEY = "com.facebook.platform.extra.COMPLETION_GESTURE";
    private static final String EXTRA_DIALOG_COMPLETION_ID_KEY = "com.facebook.platform.extra.POST_ID";
    private static NativeAppCallAttachmentStore attachmentStore;
    private Activity activity;
    private PendingCall appCall;
    private Fragment fragment;
    private OnPresentCallback onPresentCallback;

    /* loaded from: classes.dex */
    public interface Callback {
        void onComplete(PendingCall pendingCall, Bundle bundle);

        void onError(PendingCall pendingCall, Exception exc, Bundle bundle);
    }

    /* loaded from: classes.dex */
    public interface DialogFeature {
        String getAction();

        int getMinVersion();

        String name();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnPresentCallback {
        void onPresent(Context context) throws Exception;
    }

    /* loaded from: classes.dex */
    public enum ShareDialogFeature implements DialogFeature {
        SHARE_DIALOG(NativeProtocol.PROTOCOL_VERSION_20130618),
        PHOTOS(NativeProtocol.PROTOCOL_VERSION_20140204);
        
        private int minVersion;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static ShareDialogFeature[] valuesCustom() {
            ShareDialogFeature[] valuesCustom = values();
            int length = valuesCustom.length;
            ShareDialogFeature[] shareDialogFeatureArr = new ShareDialogFeature[length];
            System.arraycopy(valuesCustom, 0, shareDialogFeatureArr, 0, length);
            return shareDialogFeatureArr;
        }

        ShareDialogFeature(int minVersion) {
            this.minVersion = minVersion;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public String getAction() {
            return NativeProtocol.ACTION_FEED_DIALOG;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public int getMinVersion() {
            return this.minVersion;
        }
    }

    /* loaded from: classes.dex */
    public enum MessageDialogFeature implements DialogFeature {
        MESSAGE_DIALOG(NativeProtocol.PROTOCOL_VERSION_20140204),
        PHOTOS(NativeProtocol.PROTOCOL_VERSION_20140324);
        
        private int minVersion;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static MessageDialogFeature[] valuesCustom() {
            MessageDialogFeature[] valuesCustom = values();
            int length = valuesCustom.length;
            MessageDialogFeature[] messageDialogFeatureArr = new MessageDialogFeature[length];
            System.arraycopy(valuesCustom, 0, messageDialogFeatureArr, 0, length);
            return messageDialogFeatureArr;
        }

        MessageDialogFeature(int minVersion) {
            this.minVersion = minVersion;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public String getAction() {
            return NativeProtocol.ACTION_MESSAGE_DIALOG;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public int getMinVersion() {
            return this.minVersion;
        }
    }

    /* loaded from: classes.dex */
    public enum OpenGraphActionDialogFeature implements DialogFeature {
        OG_ACTION_DIALOG(NativeProtocol.PROTOCOL_VERSION_20130618);
        
        private int minVersion;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static OpenGraphActionDialogFeature[] valuesCustom() {
            OpenGraphActionDialogFeature[] valuesCustom = values();
            int length = valuesCustom.length;
            OpenGraphActionDialogFeature[] openGraphActionDialogFeatureArr = new OpenGraphActionDialogFeature[length];
            System.arraycopy(valuesCustom, 0, openGraphActionDialogFeatureArr, 0, length);
            return openGraphActionDialogFeatureArr;
        }

        OpenGraphActionDialogFeature(int minVersion) {
            this.minVersion = minVersion;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public String getAction() {
            return NativeProtocol.ACTION_OGACTIONPUBLISH_DIALOG;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public int getMinVersion() {
            return this.minVersion;
        }
    }

    /* loaded from: classes.dex */
    public enum OpenGraphMessageDialogFeature implements DialogFeature {
        OG_MESSAGE_DIALOG(NativeProtocol.PROTOCOL_VERSION_20140204);
        
        private int minVersion;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static OpenGraphMessageDialogFeature[] valuesCustom() {
            OpenGraphMessageDialogFeature[] valuesCustom = values();
            int length = valuesCustom.length;
            OpenGraphMessageDialogFeature[] openGraphMessageDialogFeatureArr = new OpenGraphMessageDialogFeature[length];
            System.arraycopy(valuesCustom, 0, openGraphMessageDialogFeatureArr, 0, length);
            return openGraphMessageDialogFeatureArr;
        }

        OpenGraphMessageDialogFeature(int minVersion) {
            this.minVersion = minVersion;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public String getAction() {
            return NativeProtocol.ACTION_OGMESSAGEPUBLISH_DIALOG;
        }

        @Override // com.facebook.widget.FacebookDialog.DialogFeature
        public int getMinVersion() {
            return this.minVersion;
        }
    }

    public static boolean getNativeDialogDidComplete(Bundle result) {
        return result.getBoolean(EXTRA_DIALOG_COMPLETE_KEY, false);
    }

    public static String getNativeDialogCompletionGesture(Bundle result) {
        return result.getString(EXTRA_DIALOG_COMPLETION_GESTURE_KEY);
    }

    public static String getNativeDialogPostId(Bundle result) {
        return result.getString(EXTRA_DIALOG_COMPLETION_ID_KEY);
    }

    private FacebookDialog(Activity activity, Fragment fragment, PendingCall appCall, OnPresentCallback onPresentCallback) {
        this.activity = activity;
        this.fragment = fragment;
        this.appCall = appCall;
        this.onPresentCallback = onPresentCallback;
    }

    /* synthetic */ FacebookDialog(Activity activity, Fragment fragment, PendingCall pendingCall, OnPresentCallback onPresentCallback, FacebookDialog facebookDialog) {
        this(activity, fragment, pendingCall, onPresentCallback);
    }

    public PendingCall present() {
        logDialogActivity(this.activity, this.fragment, getEventName(this.appCall.getRequestIntent()), AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_COMPLETED);
        if (this.onPresentCallback != null) {
            try {
                this.onPresentCallback.onPresent(this.activity);
            } catch (Exception e) {
                throw new FacebookException(e);
            }
        }
        if (this.fragment != null) {
            this.fragment.startActivityForResult(this.appCall.getRequestIntent(), this.appCall.getRequestCode());
        } else {
            this.activity.startActivityForResult(this.appCall.getRequestIntent(), this.appCall.getRequestCode());
        }
        return this.appCall;
    }

    public static boolean handleActivityResult(Context context, PendingCall appCall, int requestCode, Intent data, Callback callback) {
        if (requestCode != appCall.getRequestCode()) {
            return false;
        }
        if (attachmentStore != null) {
            attachmentStore.cleanupAttachmentsForCall(context, appCall.getCallId());
        }
        if (callback != null) {
            if (NativeProtocol.isErrorResult(data)) {
                Bundle errorData = NativeProtocol.getErrorDataFromResultIntent(data);
                Exception error = NativeProtocol.getExceptionFromErrorData(errorData);
                callback.onError(appCall, error, errorData);
            } else {
                Bundle successResults = NativeProtocol.getSuccessResultsFromIntent(data);
                callback.onComplete(appCall, successResults);
            }
        }
        return true;
    }

    public static boolean canPresentShareDialog(Context context, ShareDialogFeature... features) {
        return handleCanPresent(context, EnumSet.of(ShareDialogFeature.SHARE_DIALOG, features));
    }

    public static boolean canPresentMessageDialog(Context context, MessageDialogFeature... features) {
        return handleCanPresent(context, EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG, features));
    }

    public static boolean canPresentOpenGraphActionDialog(Context context, OpenGraphActionDialogFeature... features) {
        return handleCanPresent(context, EnumSet.of(OpenGraphActionDialogFeature.OG_ACTION_DIALOG, features));
    }

    public static boolean canPresentOpenGraphMessageDialog(Context context, OpenGraphMessageDialogFeature... features) {
        return handleCanPresent(context, EnumSet.of(OpenGraphMessageDialogFeature.OG_MESSAGE_DIALOG, features));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean handleCanPresent(Context context, Iterable<? extends DialogFeature> features) {
        String actionName = getActionForFeatures(features);
        String applicationId = Settings.getApplicationId();
        if (Utility.isNullOrEmpty(applicationId)) {
            applicationId = Utility.getMetadataApplicationId(context);
        }
        return getProtocolVersionForNativeDialog(context, actionName, getVersionSpecForFeatures(applicationId, actionName, features)) != -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getProtocolVersionForNativeDialog(Context context, String action, int[] versionSpec) {
        return NativeProtocol.getLatestAvailableProtocolVersionForAction(context, action, versionSpec);
    }

    static /* synthetic */ NativeAppCallAttachmentStore access$7() {
        return getAttachmentStore();
    }

    private static NativeAppCallAttachmentStore getAttachmentStore() {
        if (attachmentStore == null) {
            attachmentStore = new NativeAppCallAttachmentStore();
        }
        return attachmentStore;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] getVersionSpecForFeatures(String applicationId, String actionName, Iterable<? extends DialogFeature> features) {
        int[] intersectedRange = null;
        for (DialogFeature feature : features) {
            int[] featureVersionSpec = getVersionSpecForFeature(applicationId, actionName, feature);
            intersectedRange = Utility.intersectRanges(intersectedRange, featureVersionSpec);
        }
        return intersectedRange;
    }

    private static int[] getVersionSpecForFeature(String applicationId, String actionName, DialogFeature feature) {
        Utility.DialogFeatureConfig config = Utility.getDialogFeatureConfig(applicationId, actionName, feature.name());
        return config != null ? config.getVersionSpec() : new int[]{feature.getMinVersion()};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getActionForFeatures(Iterable<? extends DialogFeature> features) {
        Iterator<? extends DialogFeature> it = features.iterator();
        if (!it.hasNext()) {
            return null;
        }
        DialogFeature feature = it.next();
        String action = feature.getAction();
        return action;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logDialogActivity(Activity activity, Fragment fragment, String eventName, String outcome) {
        if (fragment != null) {
            activity = fragment.getActivity();
        }
        AppEventsLogger logger = AppEventsLogger.newLogger(activity);
        Bundle parameters = new Bundle();
        parameters.putString(AnalyticsEvents.PARAMETER_DIALOG_OUTCOME, outcome);
        logger.logSdkEvent(eventName, null, parameters);
    }

    private static String getEventName(Intent intent) {
        String action = intent.getStringExtra(NativeProtocol.EXTRA_PROTOCOL_ACTION);
        boolean hasPhotos = intent.hasExtra(NativeProtocol.EXTRA_PHOTOS);
        return getEventName(action, hasPhotos);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getEventName(String action, boolean hasPhotos) {
        if (action.equals(NativeProtocol.ACTION_FEED_DIALOG)) {
            if (hasPhotos) {
                return AnalyticsEvents.EVENT_NATIVE_DIALOG_TYPE_PHOTO_SHARE;
            }
            return AnalyticsEvents.EVENT_NATIVE_DIALOG_TYPE_SHARE;
        } else if (action.equals(NativeProtocol.ACTION_MESSAGE_DIALOG)) {
            if (hasPhotos) {
                return AnalyticsEvents.EVENT_NATIVE_DIALOG_TYPE_PHOTO_MESSAGE;
            }
            return AnalyticsEvents.EVENT_NATIVE_DIALOG_TYPE_MESSAGE;
        } else if (action.equals(NativeProtocol.ACTION_OGACTIONPUBLISH_DIALOG)) {
            return AnalyticsEvents.EVENT_NATIVE_DIALOG_TYPE_OG_SHARE;
        } else {
            if (action.equals(NativeProtocol.ACTION_OGMESSAGEPUBLISH_DIALOG)) {
                return AnalyticsEvents.EVENT_NATIVE_DIALOG_TYPE_OG_MESSAGE;
            }
            if (action.equals(NativeProtocol.ACTION_LIKE_DIALOG)) {
                return AnalyticsEvents.EVENT_NATIVE_DIALOG_TYPE_LIKE;
            }
            throw new FacebookException("An unspecified action was presented");
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Builder<CONCRETE extends Builder<?>> {
        protected final Activity activity;
        protected final PendingCall appCall;
        protected final String applicationId;
        protected String applicationName;
        protected Fragment fragment;
        protected HashMap<String, Bitmap> imageAttachments = new HashMap<>();
        protected HashMap<String, File> imageAttachmentFiles = new HashMap<>();

        protected abstract EnumSet<? extends DialogFeature> getDialogFeatures();

        protected abstract Bundle getMethodArguments();

        public Builder(Activity activity) {
            Validate.notNull(activity, "activity");
            this.activity = activity;
            this.applicationId = Utility.getMetadataApplicationId(activity);
            this.appCall = new PendingCall((int) NativeProtocol.DIALOG_REQUEST_CODE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setRequestCode(int requestCode) {
            this.appCall.setRequestCode(requestCode);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setApplicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setFragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public FacebookDialog build() {
            Bundle extras;
            validate();
            String action = FacebookDialog.getActionForFeatures(getDialogFeatures());
            int protocolVersion = FacebookDialog.getProtocolVersionForNativeDialog(this.activity, action, FacebookDialog.getVersionSpecForFeatures(this.applicationId, action, getDialogFeatures()));
            if (NativeProtocol.isVersionCompatibleWithBucketedIntent(protocolVersion)) {
                extras = getMethodArguments();
            } else {
                extras = setBundleExtras(new Bundle());
            }
            Intent intent = NativeProtocol.createPlatformActivityIntent(this.activity, this.appCall.getCallId().toString(), action, protocolVersion, this.applicationName, extras);
            if (intent == null) {
                FacebookDialog.logDialogActivity(this.activity, this.fragment, FacebookDialog.getEventName(action, extras.containsKey(NativeProtocol.EXTRA_PHOTOS)), AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_FAILED);
                throw new FacebookException("Unable to create Intent; this likely means the Facebook app is not installed.");
            }
            this.appCall.setRequestIntent(intent);
            return new FacebookDialog(this.activity, this.fragment, this.appCall, getOnPresentCallback(), null);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public String getWebFallbackUrlInternal() {
            Uri fallbackUrl;
            Iterable<? extends DialogFeature> features = getDialogFeatures();
            String featureName = null;
            String action = null;
            Iterator<? extends DialogFeature> it = features.iterator();
            if (it.hasNext()) {
                DialogFeature feature = (DialogFeature) it.next();
                featureName = feature.name();
                action = feature.getAction();
            }
            Utility.DialogFeatureConfig config = Utility.getDialogFeatureConfig(this.applicationId, action, featureName);
            if (config == null || (fallbackUrl = config.getFallbackUrl()) == null) {
                return null;
            }
            Bundle methodArguments = getMethodArguments();
            int protocolVersion = NativeProtocol.getLatestKnownVersion();
            Bundle webParams = ServerProtocol.getQueryParamsForPlatformActivityIntentWebFallback(this.activity, this.appCall.getCallId().toString(), protocolVersion, this.applicationName, methodArguments);
            if (webParams != null) {
                if (fallbackUrl.isRelative()) {
                    fallbackUrl = Utility.buildUri(ServerProtocol.getDialogAuthority(), fallbackUrl.toString(), webParams);
                }
                return fallbackUrl.toString();
            }
            return null;
        }

        public boolean canPresent() {
            return FacebookDialog.handleCanPresent(this.activity, getDialogFeatures());
        }

        void validate() {
        }

        OnPresentCallback getOnPresentCallback() {
            return new OnPresentCallback() { // from class: com.facebook.widget.FacebookDialog.Builder.1
                @Override // com.facebook.widget.FacebookDialog.OnPresentCallback
                public void onPresent(Context context) throws Exception {
                    if (Builder.this.imageAttachments != null && Builder.this.imageAttachments.size() > 0) {
                        FacebookDialog.access$7().addAttachmentsForCall(context, Builder.this.appCall.getCallId(), Builder.this.imageAttachments);
                    }
                    if (Builder.this.imageAttachmentFiles != null && Builder.this.imageAttachmentFiles.size() > 0) {
                        FacebookDialog.access$7().addAttachmentFilesForCall(context, Builder.this.appCall.getCallId(), Builder.this.imageAttachmentFiles);
                    }
                }
            };
        }

        protected List<String> addImageAttachments(Collection<Bitmap> bitmaps) {
            ArrayList<String> attachmentUrls = new ArrayList<>();
            for (Bitmap bitmap : bitmaps) {
                String attachmentName = UUID.randomUUID().toString();
                addImageAttachment(attachmentName, bitmap);
                String url = NativeAppCallContentProvider.getAttachmentUrl(this.applicationId, this.appCall.getCallId(), attachmentName);
                attachmentUrls.add(url);
            }
            return attachmentUrls;
        }

        protected List<String> addImageAttachmentFiles(Collection<File> bitmapFiles) {
            ArrayList<String> attachmentUrls = new ArrayList<>();
            for (File bitmapFile : bitmapFiles) {
                String attachmentName = UUID.randomUUID().toString();
                addImageAttachment(attachmentName, bitmapFile);
                String url = NativeAppCallContentProvider.getAttachmentUrl(this.applicationId, this.appCall.getCallId(), attachmentName);
                attachmentUrls.add(url);
            }
            return attachmentUrls;
        }

        List<String> getImageAttachmentNames() {
            return new ArrayList(this.imageAttachments.keySet());
        }

        protected Bundle setBundleExtras(Bundle extras) {
            return extras;
        }

        protected void putExtra(Bundle extras, String key, String value) {
            if (value != null) {
                extras.putString(key, value);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        protected CONCRETE addImageAttachment(String imageName, Bitmap bitmap) {
            this.imageAttachments.put(imageName, bitmap);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        protected CONCRETE addImageAttachment(String imageName, File attachment) {
            this.imageAttachmentFiles.put(imageName, attachment);
            return this;
        }
    }

    /* loaded from: classes.dex */
    private static abstract class ShareDialogBuilderBase<CONCRETE extends ShareDialogBuilderBase<?>> extends Builder<CONCRETE> {
        private String caption;
        private boolean dataErrorsFatal;
        private String description;
        private ArrayList<String> friends;
        protected String link;
        private String name;
        private String picture;
        private String place;
        private String ref;

        public ShareDialogBuilderBase(Activity activity) {
            super(activity);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setName(String name) {
            this.name = name;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setCaption(String caption) {
            this.caption = caption;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setDescription(String description) {
            this.description = description;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setLink(String link) {
            this.link = link;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setPicture(String picture) {
            this.picture = picture;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setPlace(String place) {
            this.place = place;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setFriends(List<String> friends) {
            this.friends = new ArrayList<>(friends);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setRef(String ref) {
            this.ref = ref;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setDataErrorsFatal(boolean dataErrorsFatal) {
            this.dataErrorsFatal = dataErrorsFatal;
            return this;
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected Bundle setBundleExtras(Bundle extras) {
            putExtra(extras, NativeProtocol.EXTRA_APPLICATION_ID, this.applicationId);
            putExtra(extras, NativeProtocol.EXTRA_APPLICATION_NAME, this.applicationName);
            putExtra(extras, NativeProtocol.EXTRA_TITLE, this.name);
            putExtra(extras, NativeProtocol.EXTRA_SUBTITLE, this.caption);
            putExtra(extras, NativeProtocol.EXTRA_DESCRIPTION, this.description);
            putExtra(extras, NativeProtocol.EXTRA_LINK, this.link);
            putExtra(extras, NativeProtocol.EXTRA_IMAGE, this.picture);
            putExtra(extras, NativeProtocol.EXTRA_PLACE_TAG, this.place);
            putExtra(extras, NativeProtocol.EXTRA_TITLE, this.name);
            putExtra(extras, NativeProtocol.EXTRA_REF, this.ref);
            extras.putBoolean(NativeProtocol.EXTRA_DATA_FAILURES_FATAL, this.dataErrorsFatal);
            if (!Utility.isNullOrEmpty(this.friends)) {
                extras.putStringArrayList(NativeProtocol.EXTRA_FRIEND_TAGS, this.friends);
            }
            return extras;
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected Bundle getMethodArguments() {
            Bundle methodArguments = new Bundle();
            putExtra(methodArguments, NativeProtocol.METHOD_ARGS_TITLE, this.name);
            putExtra(methodArguments, NativeProtocol.METHOD_ARGS_SUBTITLE, this.caption);
            putExtra(methodArguments, NativeProtocol.METHOD_ARGS_DESCRIPTION, this.description);
            putExtra(methodArguments, NativeProtocol.METHOD_ARGS_LINK, this.link);
            putExtra(methodArguments, NativeProtocol.METHOD_ARGS_IMAGE, this.picture);
            putExtra(methodArguments, NativeProtocol.METHOD_ARGS_PLACE_TAG, this.place);
            putExtra(methodArguments, NativeProtocol.METHOD_ARGS_TITLE, this.name);
            putExtra(methodArguments, NativeProtocol.METHOD_ARGS_REF, this.ref);
            methodArguments.putBoolean(NativeProtocol.METHOD_ARGS_DATA_FAILURES_FATAL, this.dataErrorsFatal);
            if (!Utility.isNullOrEmpty(this.friends)) {
                methodArguments.putStringArrayList(NativeProtocol.METHOD_ARGS_FRIEND_TAGS, this.friends);
            }
            return methodArguments;
        }
    }

    /* loaded from: classes.dex */
    public static class ShareDialogBuilder extends ShareDialogBuilderBase<ShareDialogBuilder> {
        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setCaption(String str) {
            return super.setCaption(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setDataErrorsFatal(boolean z) {
            return super.setDataErrorsFatal(z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setDescription(String str) {
            return super.setDescription(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setFriends(List list) {
            return super.setFriends(list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setLink(String str) {
            return super.setLink(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setName(String str) {
            return super.setName(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setPicture(String str) {
            return super.setPicture(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setPlace(String str) {
            return super.setPlace(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$ShareDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ ShareDialogBuilder setRef(String str) {
            return super.setRef(str);
        }

        public ShareDialogBuilder(Activity activity) {
            super(activity);
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of(ShareDialogFeature.SHARE_DIALOG);
        }
    }

    /* loaded from: classes.dex */
    private static abstract class PhotoDialogBuilderBase<CONCRETE extends PhotoDialogBuilderBase<?>> extends Builder<CONCRETE> {
        static int MAXIMUM_PHOTO_COUNT = 6;
        private ArrayList<String> friends;
        private ArrayList<String> imageAttachmentUrls;
        private String place;

        abstract int getMaximumNumberOfPhotos();

        public PhotoDialogBuilderBase(Activity activity) {
            super(activity);
            this.imageAttachmentUrls = new ArrayList<>();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setPlace(String place) {
            this.place = place;
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setFriends(List<String> friends) {
            this.friends = new ArrayList<>(friends);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE addPhotos(Collection<Bitmap> photos) {
            this.imageAttachmentUrls.addAll(addImageAttachments(photos));
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE addPhotoFiles(Collection<File> photos) {
            this.imageAttachmentUrls.addAll(addImageAttachmentFiles(photos));
            return this;
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        void validate() {
            super.validate();
            if (this.imageAttachmentUrls.isEmpty()) {
                throw new FacebookException("Must specify at least one photo.");
            }
            if (this.imageAttachmentUrls.size() > getMaximumNumberOfPhotos()) {
                throw new FacebookException(String.format("Cannot add more than %d photos.", Integer.valueOf(getMaximumNumberOfPhotos())));
            }
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected Bundle setBundleExtras(Bundle extras) {
            putExtra(extras, NativeProtocol.EXTRA_APPLICATION_ID, this.applicationId);
            putExtra(extras, NativeProtocol.EXTRA_APPLICATION_NAME, this.applicationName);
            putExtra(extras, NativeProtocol.EXTRA_PLACE_TAG, this.place);
            extras.putStringArrayList(NativeProtocol.EXTRA_PHOTOS, this.imageAttachmentUrls);
            if (!Utility.isNullOrEmpty(this.friends)) {
                extras.putStringArrayList(NativeProtocol.EXTRA_FRIEND_TAGS, this.friends);
            }
            return extras;
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected Bundle getMethodArguments() {
            Bundle methodArgs = new Bundle();
            putExtra(methodArgs, NativeProtocol.METHOD_ARGS_PLACE_TAG, this.place);
            methodArgs.putStringArrayList(NativeProtocol.METHOD_ARGS_PHOTOS, this.imageAttachmentUrls);
            if (!Utility.isNullOrEmpty(this.friends)) {
                methodArgs.putStringArrayList(NativeProtocol.METHOD_ARGS_FRIEND_TAGS, this.friends);
            }
            return methodArgs;
        }
    }

    /* loaded from: classes.dex */
    public static class PhotoShareDialogBuilder extends PhotoDialogBuilderBase<PhotoShareDialogBuilder> {
        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$PhotoDialogBuilderBase, com.facebook.widget.FacebookDialog$PhotoShareDialogBuilder] */
        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        public /* bridge */ /* synthetic */ PhotoShareDialogBuilder addPhotoFiles(Collection collection) {
            return super.addPhotoFiles(collection);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$PhotoDialogBuilderBase, com.facebook.widget.FacebookDialog$PhotoShareDialogBuilder] */
        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        public /* bridge */ /* synthetic */ PhotoShareDialogBuilder addPhotos(Collection collection) {
            return super.addPhotos(collection);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$PhotoDialogBuilderBase, com.facebook.widget.FacebookDialog$PhotoShareDialogBuilder] */
        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        public /* bridge */ /* synthetic */ PhotoShareDialogBuilder setFriends(List list) {
            return super.setFriends(list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$PhotoDialogBuilderBase, com.facebook.widget.FacebookDialog$PhotoShareDialogBuilder] */
        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        public /* bridge */ /* synthetic */ PhotoShareDialogBuilder setPlace(String str) {
            return super.setPlace(str);
        }

        public PhotoShareDialogBuilder(Activity activity) {
            super(activity);
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of(ShareDialogFeature.SHARE_DIALOG, ShareDialogFeature.PHOTOS);
        }

        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        int getMaximumNumberOfPhotos() {
            return MAXIMUM_PHOTO_COUNT;
        }
    }

    /* loaded from: classes.dex */
    public static class PhotoMessageDialogBuilder extends PhotoDialogBuilderBase<PhotoMessageDialogBuilder> {
        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$PhotoMessageDialogBuilder, com.facebook.widget.FacebookDialog$PhotoDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        public /* bridge */ /* synthetic */ PhotoMessageDialogBuilder addPhotoFiles(Collection collection) {
            return super.addPhotoFiles(collection);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$PhotoMessageDialogBuilder, com.facebook.widget.FacebookDialog$PhotoDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        public /* bridge */ /* synthetic */ PhotoMessageDialogBuilder addPhotos(Collection collection) {
            return super.addPhotos(collection);
        }

        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        public /* bridge */ /* synthetic */ PhotoMessageDialogBuilder setFriends(List list) {
            return setFriends2((List<String>) list);
        }

        public PhotoMessageDialogBuilder(Activity activity) {
            super(activity);
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG, MessageDialogFeature.PHOTOS);
        }

        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        int getMaximumNumberOfPhotos() {
            return MAXIMUM_PHOTO_COUNT;
        }

        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        public PhotoMessageDialogBuilder setPlace(String place) {
            return this;
        }

        @Override // com.facebook.widget.FacebookDialog.PhotoDialogBuilderBase
        /* renamed from: setFriends  reason: avoid collision after fix types in other method */
        public PhotoMessageDialogBuilder setFriends2(List<String> friends) {
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static class MessageDialogBuilder extends ShareDialogBuilderBase<MessageDialogBuilder> {
        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$MessageDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ MessageDialogBuilder setCaption(String str) {
            return super.setCaption(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$MessageDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ MessageDialogBuilder setDataErrorsFatal(boolean z) {
            return super.setDataErrorsFatal(z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$MessageDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ MessageDialogBuilder setDescription(String str) {
            return super.setDescription(str);
        }

        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ MessageDialogBuilder setFriends(List list) {
            return setFriends((List<String>) list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$MessageDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ MessageDialogBuilder setLink(String str) {
            return super.setLink(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$MessageDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ MessageDialogBuilder setName(String str) {
            return super.setName(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$MessageDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ MessageDialogBuilder setPicture(String str) {
            return super.setPicture(str);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$MessageDialogBuilder, com.facebook.widget.FacebookDialog$ShareDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public /* bridge */ /* synthetic */ MessageDialogBuilder setRef(String str) {
            return super.setRef(str);
        }

        public MessageDialogBuilder(Activity activity) {
            super(activity);
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public MessageDialogBuilder setPlace(String place) {
            return this;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.facebook.widget.FacebookDialog.ShareDialogBuilderBase
        public MessageDialogBuilder setFriends(List<String> friends) {
            return this;
        }
    }

    /* loaded from: classes.dex */
    private static abstract class OpenGraphDialogBuilderBase<CONCRETE extends OpenGraphDialogBuilderBase<?>> extends Builder<CONCRETE> {
        private OpenGraphAction action;
        private String actionType;
        private boolean dataErrorsFatal;
        private String previewPropertyName;

        @Deprecated
        public OpenGraphDialogBuilderBase(Activity activity, OpenGraphAction action, String actionType, String previewPropertyName) {
            super(activity);
            Validate.notNull(action, "action");
            Validate.notNullOrEmpty(actionType, "actionType");
            Validate.notNullOrEmpty(previewPropertyName, "previewPropertyName");
            if (action.getProperty(previewPropertyName) == null) {
                throw new IllegalArgumentException("A property named \"" + previewPropertyName + "\" was not found on the action.  The name of the preview property must match the name of an action property.");
            }
            String typeOnAction = action.getType();
            if (!Utility.isNullOrEmpty(typeOnAction) && !typeOnAction.equals(actionType)) {
                throw new IllegalArgumentException("'actionType' must match the type of 'action' if it is specified. Consider using OpenGraphDialogBuilderBase(Activity activity, OpenGraphAction action, String previewPropertyName) instead.");
            }
            this.action = action;
            this.actionType = actionType;
            this.previewPropertyName = previewPropertyName;
        }

        public OpenGraphDialogBuilderBase(Activity activity, OpenGraphAction action, String previewPropertyName) {
            super(activity);
            Validate.notNull(action, "action");
            Validate.notNullOrEmpty(action.getType(), "action.getType()");
            Validate.notNullOrEmpty(previewPropertyName, "previewPropertyName");
            if (action.getProperty(previewPropertyName) == null) {
                throw new IllegalArgumentException("A property named \"" + previewPropertyName + "\" was not found on the action.  The name of the preview property must match the name of an action property.");
            }
            this.action = action;
            this.actionType = action.getType();
            this.previewPropertyName = previewPropertyName;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setDataErrorsFatal(boolean dataErrorsFatal) {
            this.dataErrorsFatal = dataErrorsFatal;
            return this;
        }

        public CONCRETE setImageAttachmentsForAction(List<Bitmap> bitmaps) {
            return setImageAttachmentsForAction(bitmaps, false);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setImageAttachmentsForAction(List<Bitmap> bitmaps, boolean isUserGenerated) {
            Validate.containsNoNulls(bitmaps, "bitmaps");
            if (this.action == null) {
                throw new FacebookException("Can not set attachments prior to setting action.");
            }
            List<String> attachmentUrls = addImageAttachments(bitmaps);
            updateActionAttachmentUrls(attachmentUrls, isUserGenerated);
            return this;
        }

        public CONCRETE setImageAttachmentFilesForAction(List<File> bitmapFiles) {
            return setImageAttachmentFilesForAction(bitmapFiles, false);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setImageAttachmentFilesForAction(List<File> bitmapFiles, boolean isUserGenerated) {
            Validate.containsNoNulls(bitmapFiles, "bitmapFiles");
            if (this.action == null) {
                throw new FacebookException("Can not set attachments prior to setting action.");
            }
            List<String> attachmentUrls = addImageAttachmentFiles(bitmapFiles);
            updateActionAttachmentUrls(attachmentUrls, isUserGenerated);
            return this;
        }

        private void updateActionAttachmentUrls(List<String> attachmentUrls, boolean isUserGenerated) {
            List<JSONObject> attachments = this.action.getImage();
            if (attachments == null) {
                attachments = new ArrayList<>(attachmentUrls.size());
            }
            for (String url : attachmentUrls) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("url", url);
                    if (isUserGenerated) {
                        jsonObject.put(NativeProtocol.IMAGE_USER_GENERATED_KEY, true);
                    }
                    attachments.add(jsonObject);
                } catch (JSONException e) {
                    throw new FacebookException("Unable to attach images", e);
                }
            }
            this.action.setImage(attachments);
        }

        public CONCRETE setImageAttachmentsForObject(String objectProperty, List<Bitmap> bitmaps) {
            return setImageAttachmentsForObject(objectProperty, bitmaps, false);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setImageAttachmentsForObject(String objectProperty, List<Bitmap> bitmaps, boolean isUserGenerated) {
            Validate.notNull(objectProperty, "objectProperty");
            Validate.containsNoNulls(bitmaps, "bitmaps");
            if (this.action == null) {
                throw new FacebookException("Can not set attachments prior to setting action.");
            }
            List<String> attachmentUrls = addImageAttachments(bitmaps);
            updateObjectAttachmentUrls(objectProperty, attachmentUrls, isUserGenerated);
            return this;
        }

        public CONCRETE setImageAttachmentFilesForObject(String objectProperty, List<File> bitmapFiles) {
            return setImageAttachmentFilesForObject(objectProperty, bitmapFiles, false);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CONCRETE setImageAttachmentFilesForObject(String objectProperty, List<File> bitmapFiles, boolean isUserGenerated) {
            Validate.notNull(objectProperty, "objectProperty");
            Validate.containsNoNulls(bitmapFiles, "bitmapFiles");
            if (this.action == null) {
                throw new FacebookException("Can not set attachments prior to setting action.");
            }
            List<String> attachmentUrls = addImageAttachmentFiles(bitmapFiles);
            updateObjectAttachmentUrls(objectProperty, attachmentUrls, isUserGenerated);
            return this;
        }

        void updateObjectAttachmentUrls(String objectProperty, List<String> attachmentUrls, boolean isUserGenerated) {
            try {
                OpenGraphObject object = (OpenGraphObject) this.action.getPropertyAs(objectProperty, OpenGraphObject.class);
                if (object == null) {
                    throw new IllegalArgumentException("Action does not contain a property '" + objectProperty + "'");
                } else if (!object.getCreateObject()) {
                    throw new IllegalArgumentException("The Open Graph object in '" + objectProperty + "' is not marked for creation");
                } else {
                    GraphObjectList<GraphObject> attachments = object.getImage();
                    if (attachments == null) {
                        attachments = GraphObject.Factory.createList(GraphObject.class);
                    }
                    for (String url : attachmentUrls) {
                        GraphObject graphObject = GraphObject.Factory.create();
                        graphObject.setProperty("url", url);
                        if (isUserGenerated) {
                            graphObject.setProperty(NativeProtocol.IMAGE_USER_GENERATED_KEY, true);
                        }
                        attachments.add(graphObject);
                    }
                    object.setImage(attachments);
                }
            } catch (FacebookGraphObjectException e) {
                throw new IllegalArgumentException("Property '" + objectProperty + "' is not a graph object");
            }
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected Bundle setBundleExtras(Bundle extras) {
            putExtra(extras, NativeProtocol.EXTRA_PREVIEW_PROPERTY_NAME, this.previewPropertyName);
            putExtra(extras, NativeProtocol.EXTRA_ACTION_TYPE, this.actionType);
            extras.putBoolean(NativeProtocol.EXTRA_DATA_FAILURES_FATAL, this.dataErrorsFatal);
            JSONObject jsonAction = this.action.getInnerJSONObject();
            String jsonString = flattenChildrenOfGraphObject(jsonAction).toString();
            putExtra(extras, NativeProtocol.EXTRA_ACTION, jsonString);
            return extras;
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected Bundle getMethodArguments() {
            Bundle methodArgs = new Bundle();
            putExtra(methodArgs, NativeProtocol.METHOD_ARGS_PREVIEW_PROPERTY_NAME, this.previewPropertyName);
            putExtra(methodArgs, NativeProtocol.METHOD_ARGS_ACTION_TYPE, this.actionType);
            methodArgs.putBoolean(NativeProtocol.METHOD_ARGS_DATA_FAILURES_FATAL, this.dataErrorsFatal);
            JSONObject jsonAction = this.action.getInnerJSONObject();
            String jsonString = flattenChildrenOfGraphObject(jsonAction).toString();
            putExtra(methodArgs, NativeProtocol.METHOD_ARGS_ACTION, jsonString);
            return methodArgs;
        }

        private JSONObject flattenChildrenOfGraphObject(JSONObject graphObject) {
            JSONObject graphObject2;
            try {
                graphObject2 = new JSONObject(graphObject.toString());
            } catch (JSONException e) {
                e = e;
            }
            try {
                Iterator<String> keys = graphObject2.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (!key.equalsIgnoreCase(WorkoutSummary.IMAGE)) {
                        Object object = graphObject2.get(key);
                        graphObject2.put(key, flattenObject(object));
                    }
                }
                return graphObject2;
            } catch (JSONException e2) {
                e = e2;
                throw new FacebookException(e);
            }
        }

        private Object flattenObject(Object object) throws JSONException {
            if (object == null) {
                return null;
            }
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                if (!jsonObject.optBoolean(NativeProtocol.OPEN_GRAPH_CREATE_OBJECT_KEY)) {
                    if (jsonObject.has("id")) {
                        return jsonObject.getString("id");
                    }
                    if (jsonObject.has("url")) {
                        return jsonObject.getString("url");
                    }
                    return object;
                }
                return object;
            } else if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                JSONArray newArray = new JSONArray();
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    newArray.put(flattenObject(jsonArray.get(i)));
                }
                return newArray;
            } else {
                return object;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class OpenGraphActionDialogBuilder extends OpenGraphDialogBuilderBase<OpenGraphActionDialogBuilder> {
        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setDataErrorsFatal(boolean z) {
            return super.setDataErrorsFatal(z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setImageAttachmentFilesForAction(List list) {
            return super.setImageAttachmentFilesForAction(list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setImageAttachmentFilesForAction(List list, boolean z) {
            return super.setImageAttachmentFilesForAction(list, z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setImageAttachmentFilesForObject(String str, List list) {
            return super.setImageAttachmentFilesForObject(str, list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setImageAttachmentFilesForObject(String str, List list, boolean z) {
            return super.setImageAttachmentFilesForObject(str, list, z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setImageAttachmentsForAction(List list) {
            return super.setImageAttachmentsForAction(list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setImageAttachmentsForAction(List list, boolean z) {
            return super.setImageAttachmentsForAction(list, z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setImageAttachmentsForObject(String str, List list) {
            return super.setImageAttachmentsForObject(str, list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphActionDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphActionDialogBuilder setImageAttachmentsForObject(String str, List list, boolean z) {
            return super.setImageAttachmentsForObject(str, list, z);
        }

        @Deprecated
        public OpenGraphActionDialogBuilder(Activity activity, OpenGraphAction action, String actionType, String previewPropertyName) {
            super(activity, action, actionType, previewPropertyName);
        }

        public OpenGraphActionDialogBuilder(Activity activity, OpenGraphAction action, String previewPropertyName) {
            super(activity, action, previewPropertyName);
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of(OpenGraphActionDialogFeature.OG_ACTION_DIALOG);
        }
    }

    /* loaded from: classes.dex */
    public static class OpenGraphMessageDialogBuilder extends OpenGraphDialogBuilderBase<OpenGraphMessageDialogBuilder> {
        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setDataErrorsFatal(boolean z) {
            return super.setDataErrorsFatal(z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setImageAttachmentFilesForAction(List list) {
            return super.setImageAttachmentFilesForAction(list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setImageAttachmentFilesForAction(List list, boolean z) {
            return super.setImageAttachmentFilesForAction(list, z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setImageAttachmentFilesForObject(String str, List list) {
            return super.setImageAttachmentFilesForObject(str, list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setImageAttachmentFilesForObject(String str, List list, boolean z) {
            return super.setImageAttachmentFilesForObject(str, list, z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setImageAttachmentsForAction(List list) {
            return super.setImageAttachmentsForAction(list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setImageAttachmentsForAction(List list, boolean z) {
            return super.setImageAttachmentsForAction(list, z);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setImageAttachmentsForObject(String str, List list) {
            return super.setImageAttachmentsForObject(str, list);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.facebook.widget.FacebookDialog$OpenGraphMessageDialogBuilder, com.facebook.widget.FacebookDialog$OpenGraphDialogBuilderBase] */
        @Override // com.facebook.widget.FacebookDialog.OpenGraphDialogBuilderBase
        public /* bridge */ /* synthetic */ OpenGraphMessageDialogBuilder setImageAttachmentsForObject(String str, List list, boolean z) {
            return super.setImageAttachmentsForObject(str, list, z);
        }

        public OpenGraphMessageDialogBuilder(Activity activity, OpenGraphAction action, String previewPropertyName) {
            super(activity, action, previewPropertyName);
        }

        @Override // com.facebook.widget.FacebookDialog.Builder
        protected EnumSet<? extends DialogFeature> getDialogFeatures() {
            return EnumSet.of(OpenGraphMessageDialogFeature.OG_MESSAGE_DIALOG);
        }
    }

    /* loaded from: classes.dex */
    public static class PendingCall implements Parcelable {
        public static final Parcelable.Creator<PendingCall> CREATOR = new Parcelable.Creator<PendingCall>() { // from class: com.facebook.widget.FacebookDialog.PendingCall.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PendingCall createFromParcel(Parcel in) {
                return new PendingCall(in, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PendingCall[] newArray(int size) {
                return new PendingCall[size];
            }
        };
        private UUID callId;
        private int requestCode;
        private Intent requestIntent;

        public PendingCall(int requestCode) {
            this.callId = UUID.randomUUID();
            this.requestCode = requestCode;
        }

        private PendingCall(Parcel in) {
            this.callId = UUID.fromString(in.readString());
            this.requestIntent = (Intent) in.readParcelable(null);
            this.requestCode = in.readInt();
        }

        /* synthetic */ PendingCall(Parcel parcel, PendingCall pendingCall) {
            this(parcel);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRequestIntent(Intent requestIntent) {
            this.requestIntent = requestIntent;
        }

        public Intent getRequestIntent() {
            return this.requestIntent;
        }

        public UUID getCallId() {
            return this.callId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public int getRequestCode() {
            return this.requestCode;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.callId.toString());
            parcel.writeParcelable(this.requestIntent, 0);
            parcel.writeInt(this.requestCode);
        }
    }
}
