package org.acra;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;
/* loaded from: classes.dex */
public class ACRAConfiguration implements ReportsCrashes {
    private Map<String, String> mHttpHeaders;
    private ReportsCrashes mReportsCrashes;
    private String[] mAdditionalDropboxTags = null;
    private String[] mAdditionalSharedPreferences = null;
    private Integer mConnectionTimeout = null;
    private ReportField[] mCustomReportContent = null;
    private Boolean mDeleteUnapprovedReportsOnApplicationStart = null;
    private Boolean mDeleteOldUnsentReportsOnApplicationStart = null;
    private Integer mDropboxCollectionMinutes = null;
    private Boolean mForceCloseDialogAfterToast = null;
    private String mFormKey = null;
    private String mFormUri = null;
    private String mFormUriBasicAuthLogin = null;
    private String mFormUriBasicAuthPassword = null;
    private Boolean mIncludeDropboxSystemTags = null;
    private String[] mLogcatArguments = null;
    private String mMailTo = null;
    private Integer mMaxNumberOfRequestRetries = null;
    private ReportingInteractionMode mMode = null;
    private Integer mResDialogCommentPrompt = null;
    private Integer mResDialogEmailPrompt = null;
    private Integer mResDialogIcon = null;
    private Integer mResDialogOkToast = null;
    private Integer mResDialogText = null;
    private Integer mResDialogTitle = null;
    private Integer mResNotifIcon = null;
    private Integer mResNotifText = null;
    private Integer mResNotifTickerText = null;
    private Integer mResNotifTitle = null;
    private Integer mResToastText = null;
    private Integer mSharedPreferenceMode = null;
    private String mSharedPreferenceName = null;
    private Integer mSocketTimeout = null;
    private Boolean mLogcatFilterByPid = null;
    private Boolean mSendReportsInDevMode = null;
    private String[] mExcludeMatchingSharedPreferencesKeys = null;
    private String[] mExcludeMatchingSettingsKeys = null;
    private String mApplicationLogFile = null;
    private Integer mApplicationLogFileLines = null;
    private String mGoogleFormUrlFormat = null;
    private Boolean mDisableSSLCertValidation = null;
    private HttpSender.Method mHttpMethod = null;
    private HttpSender.Type mReportType = null;

    public void setHttpHeaders(Map<String, String> headers) {
        this.mHttpHeaders = headers;
    }

    public Map<String, String> getHttpHeaders() {
        return this.mHttpHeaders;
    }

    public void setAdditionalDropboxTags(String[] additionalDropboxTags) {
        this.mAdditionalDropboxTags = additionalDropboxTags;
    }

    public void setAdditionalSharedPreferences(String[] additionalSharedPreferences) {
        this.mAdditionalSharedPreferences = additionalSharedPreferences;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.mConnectionTimeout = connectionTimeout;
    }

    public void setCustomReportContent(ReportField[] customReportContent) {
        this.mCustomReportContent = customReportContent;
    }

    public void setDeleteUnapprovedReportsOnApplicationStart(Boolean deleteUnapprovedReportsOnApplicationStart) {
        this.mDeleteUnapprovedReportsOnApplicationStart = deleteUnapprovedReportsOnApplicationStart;
    }

    public void setDeleteOldUnsentReportsOnApplicationStart(Boolean deleteOldUnsetReportsOnApplicationStart) {
        this.mDeleteOldUnsentReportsOnApplicationStart = deleteOldUnsetReportsOnApplicationStart;
    }

    public void setDropboxCollectionMinutes(Integer dropboxCollectionMinutes) {
        this.mDropboxCollectionMinutes = dropboxCollectionMinutes;
    }

    public void setForceCloseDialogAfterToast(Boolean forceCloseDialogAfterToast) {
        this.mForceCloseDialogAfterToast = forceCloseDialogAfterToast;
    }

    public void setFormKey(String formKey) {
        this.mFormKey = formKey;
    }

    public void setFormUri(String formUri) {
        this.mFormUri = formUri;
    }

    public void setFormUriBasicAuthLogin(String formUriBasicAuthLogin) {
        this.mFormUriBasicAuthLogin = formUriBasicAuthLogin;
    }

    public void setFormUriBasicAuthPassword(String formUriBasicAuthPassword) {
        this.mFormUriBasicAuthPassword = formUriBasicAuthPassword;
    }

    public void setIncludeDropboxSystemTags(Boolean includeDropboxSystemTags) {
        this.mIncludeDropboxSystemTags = includeDropboxSystemTags;
    }

    public void setLogcatArguments(String[] logcatArguments) {
        this.mLogcatArguments = logcatArguments;
    }

    public void setMailTo(String mailTo) {
        this.mMailTo = mailTo;
    }

    public void setMaxNumberOfRequestRetries(Integer maxNumberOfRequestRetries) {
        this.mMaxNumberOfRequestRetries = maxNumberOfRequestRetries;
    }

    public void setMode(ReportingInteractionMode mode) throws ACRAConfigurationException {
        this.mMode = mode;
        ACRA.checkCrashResources();
    }

    public void setResDialogCommentPrompt(int resId) {
        this.mResDialogCommentPrompt = Integer.valueOf(resId);
    }

    public void setResDialogEmailPrompt(int resId) {
        this.mResDialogEmailPrompt = Integer.valueOf(resId);
    }

    public void setResDialogIcon(int resId) {
        this.mResDialogIcon = Integer.valueOf(resId);
    }

    public void setResDialogOkToast(int resId) {
        this.mResDialogOkToast = Integer.valueOf(resId);
    }

    public void setResDialogText(int resId) {
        this.mResDialogText = Integer.valueOf(resId);
    }

    public void setResDialogTitle(int resId) {
        this.mResDialogTitle = Integer.valueOf(resId);
    }

    public void setResNotifIcon(int resId) {
        this.mResNotifIcon = Integer.valueOf(resId);
    }

    public void setResNotifText(int resId) {
        this.mResNotifText = Integer.valueOf(resId);
    }

    public void setResNotifTickerText(int resId) {
        this.mResNotifTickerText = Integer.valueOf(resId);
    }

    public void setResNotifTitle(int resId) {
        this.mResNotifTitle = Integer.valueOf(resId);
    }

    public void setResToastText(int resId) {
        this.mResToastText = Integer.valueOf(resId);
    }

    public void setSharedPreferenceMode(Integer sharedPreferenceMode) {
        this.mSharedPreferenceMode = sharedPreferenceMode;
    }

    public void setSharedPreferenceName(String sharedPreferenceName) {
        this.mSharedPreferenceName = sharedPreferenceName;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.mSocketTimeout = socketTimeout;
    }

    public void setLogcatFilterByPid(Boolean filterByPid) {
        this.mLogcatFilterByPid = filterByPid;
    }

    public void setSendReportsInDevMode(Boolean sendReportsInDevMode) {
        this.mSendReportsInDevMode = sendReportsInDevMode;
    }

    public void setExcludeMatchingSharedPreferencesKeys(String[] excludeMatchingSharedPreferencesKeys) {
        this.mExcludeMatchingSharedPreferencesKeys = excludeMatchingSharedPreferencesKeys;
    }

    public void setExcludeMatchingSettingsKeys(String[] excludeMatchingSettingsKeys) {
        this.mExcludeMatchingSettingsKeys = excludeMatchingSettingsKeys;
    }

    public void setApplicationLogFile(String applicationLogFile) {
        this.mApplicationLogFile = applicationLogFile;
    }

    public void setApplicationLogFileLines(int applicationLogFileLines) {
        this.mApplicationLogFileLines = Integer.valueOf(applicationLogFileLines);
    }

    public void setDisableSSLCertValidation(boolean disableSSLCertValidation) {
        this.mDisableSSLCertValidation = Boolean.valueOf(disableSSLCertValidation);
    }

    public void setHttpMethod(HttpSender.Method httpMethod) {
        this.mHttpMethod = httpMethod;
    }

    public void setReportType(HttpSender.Type type) {
        this.mReportType = type;
    }

    public ACRAConfiguration(ReportsCrashes defaults) {
        this.mReportsCrashes = null;
        this.mReportsCrashes = defaults;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String[] additionalDropBoxTags() {
        if (this.mAdditionalDropboxTags != null) {
            return this.mAdditionalDropboxTags;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.additionalDropBoxTags();
        }
        return new String[0];
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String[] additionalSharedPreferences() {
        if (this.mAdditionalSharedPreferences != null) {
            return this.mAdditionalSharedPreferences;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.additionalSharedPreferences();
        }
        return new String[0];
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return this.mReportsCrashes.annotationType();
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int connectionTimeout() {
        if (this.mConnectionTimeout != null) {
            return this.mConnectionTimeout.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.connectionTimeout();
        }
        return 3000;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public ReportField[] customReportContent() {
        if (this.mCustomReportContent != null) {
            return this.mCustomReportContent;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.customReportContent();
        }
        return new ReportField[0];
    }

    @Override // org.acra.annotation.ReportsCrashes
    public boolean deleteUnapprovedReportsOnApplicationStart() {
        if (this.mDeleteUnapprovedReportsOnApplicationStart != null) {
            return this.mDeleteUnapprovedReportsOnApplicationStart.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.deleteUnapprovedReportsOnApplicationStart();
        }
        return true;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public boolean deleteOldUnsentReportsOnApplicationStart() {
        if (this.mDeleteOldUnsentReportsOnApplicationStart != null) {
            return this.mDeleteOldUnsentReportsOnApplicationStart.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.deleteOldUnsentReportsOnApplicationStart();
        }
        return true;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int dropboxCollectionMinutes() {
        if (this.mDropboxCollectionMinutes != null) {
            return this.mDropboxCollectionMinutes.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.dropboxCollectionMinutes();
        }
        return 5;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public boolean forceCloseDialogAfterToast() {
        if (this.mForceCloseDialogAfterToast != null) {
            return this.mForceCloseDialogAfterToast.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.forceCloseDialogAfterToast();
        }
        return false;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String formKey() {
        if (this.mFormKey != null) {
            return this.mFormKey;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.formKey();
        }
        return "";
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String formUri() {
        if (this.mFormUri != null) {
            return this.mFormUri;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.formUri();
        }
        return "";
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String formUriBasicAuthLogin() {
        if (this.mFormUriBasicAuthLogin != null) {
            return this.mFormUriBasicAuthLogin;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.formUriBasicAuthLogin();
        }
        return ACRAConstants.NULL_VALUE;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String formUriBasicAuthPassword() {
        if (this.mFormUriBasicAuthPassword != null) {
            return this.mFormUriBasicAuthPassword;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.formUriBasicAuthPassword();
        }
        return ACRAConstants.NULL_VALUE;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public boolean includeDropBoxSystemTags() {
        if (this.mIncludeDropboxSystemTags != null) {
            return this.mIncludeDropboxSystemTags.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.includeDropBoxSystemTags();
        }
        return false;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String[] logcatArguments() {
        if (this.mLogcatArguments != null) {
            return this.mLogcatArguments;
        }
        return this.mReportsCrashes != null ? this.mReportsCrashes.logcatArguments() : new String[]{"-t", Integer.toString(100), "-v", "time"};
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String mailTo() {
        if (this.mMailTo != null) {
            return this.mMailTo;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.mailTo();
        }
        return "";
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int maxNumberOfRequestRetries() {
        if (this.mMaxNumberOfRequestRetries != null) {
            return this.mMaxNumberOfRequestRetries.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.maxNumberOfRequestRetries();
        }
        return 3;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public ReportingInteractionMode mode() {
        if (this.mMode != null) {
            return this.mMode;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.mode();
        }
        return ReportingInteractionMode.SILENT;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resDialogCommentPrompt() {
        if (this.mResDialogCommentPrompt != null) {
            return this.mResDialogCommentPrompt.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogCommentPrompt();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resDialogEmailPrompt() {
        if (this.mResDialogEmailPrompt != null) {
            return this.mResDialogEmailPrompt.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogEmailPrompt();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resDialogIcon() {
        if (this.mResDialogIcon != null) {
            return this.mResDialogIcon.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogIcon();
        }
        return ACRAConstants.DEFAULT_DIALOG_ICON;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resDialogOkToast() {
        if (this.mResDialogOkToast != null) {
            return this.mResDialogOkToast.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogOkToast();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resDialogText() {
        if (this.mResDialogText != null) {
            return this.mResDialogText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogText();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resDialogTitle() {
        if (this.mResDialogTitle != null) {
            return this.mResDialogTitle.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resDialogTitle();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resNotifIcon() {
        if (this.mResNotifIcon != null) {
            return this.mResNotifIcon.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resNotifIcon();
        }
        return ACRAConstants.DEFAULT_NOTIFICATION_ICON;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resNotifText() {
        if (this.mResNotifText != null) {
            return this.mResNotifText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resNotifText();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resNotifTickerText() {
        if (this.mResNotifTickerText != null) {
            return this.mResNotifTickerText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resNotifTickerText();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resNotifTitle() {
        if (this.mResNotifTitle != null) {
            return this.mResNotifTitle.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resNotifTitle();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int resToastText() {
        if (this.mResToastText != null) {
            return this.mResToastText.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.resToastText();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int sharedPreferencesMode() {
        if (this.mSharedPreferenceMode != null) {
            return this.mSharedPreferenceMode.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.sharedPreferencesMode();
        }
        return 0;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String sharedPreferencesName() {
        if (this.mSharedPreferenceName != null) {
            return this.mSharedPreferenceName;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.sharedPreferencesName();
        }
        return "";
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int socketTimeout() {
        if (this.mSocketTimeout != null) {
            return this.mSocketTimeout.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.socketTimeout();
        }
        return 5000;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public boolean logcatFilterByPid() {
        if (this.mLogcatFilterByPid != null) {
            return this.mLogcatFilterByPid.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.logcatFilterByPid();
        }
        return false;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public boolean sendReportsInDevMode() {
        if (this.mSendReportsInDevMode != null) {
            return this.mSendReportsInDevMode.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.sendReportsInDevMode();
        }
        return true;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String[] excludeMatchingSharedPreferencesKeys() {
        if (this.mExcludeMatchingSharedPreferencesKeys != null) {
            return this.mExcludeMatchingSharedPreferencesKeys;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.excludeMatchingSharedPreferencesKeys();
        }
        return new String[0];
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String[] excludeMatchingSettingsKeys() {
        if (this.mExcludeMatchingSettingsKeys != null) {
            return this.mExcludeMatchingSettingsKeys;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.excludeMatchingSettingsKeys();
        }
        return new String[0];
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String applicationLogFile() {
        if (this.mApplicationLogFile != null) {
            return this.mApplicationLogFile;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.applicationLogFile();
        }
        return "";
    }

    @Override // org.acra.annotation.ReportsCrashes
    public int applicationLogFileLines() {
        if (this.mApplicationLogFileLines != null) {
            return this.mApplicationLogFileLines.intValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.applicationLogFileLines();
        }
        return 100;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public String googleFormUrlFormat() {
        if (this.mGoogleFormUrlFormat != null) {
            return this.mGoogleFormUrlFormat;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.googleFormUrlFormat();
        }
        return ACRAConstants.DEFAULT_GOOGLE_FORM_URL_FORMAT;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public boolean disableSSLCertValidation() {
        if (this.mDisableSSLCertValidation != null) {
            return this.mDisableSSLCertValidation.booleanValue();
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.disableSSLCertValidation();
        }
        return false;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public HttpSender.Method httpMethod() {
        if (this.mHttpMethod != null) {
            return this.mHttpMethod;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.httpMethod();
        }
        return HttpSender.Method.POST;
    }

    @Override // org.acra.annotation.ReportsCrashes
    public HttpSender.Type reportType() {
        if (this.mReportType != null) {
            return this.mReportType;
        }
        if (this.mReportsCrashes != null) {
            return this.mReportsCrashes.reportType();
        }
        return HttpSender.Type.FORM;
    }

    public static boolean isNull(String aString) {
        return aString == null || ACRAConstants.NULL_VALUE.equals(aString);
    }
}
