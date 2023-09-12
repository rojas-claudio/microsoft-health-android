package com.microsoft.kapp.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.R;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.LogConfiguration;
import com.microsoft.kapp.logging.LogConstants;
import com.microsoft.kapp.logging.LogFormatManager;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.logging.models.FeedbackContext;
import com.microsoft.kapp.logging.models.FeedbackDescription;
import com.microsoft.kapp.logging.models.FeedbackMetadata;
import com.microsoft.kapp.logging.models.LogEntryType;
import com.microsoft.kapp.logging.models.LogMode;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FileUtils;
import com.microsoft.kapp.utils.GsonUtils;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.services.UserAgentProvider;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class FeedbackServiceImpl implements FeedbackService {
    private static final String TAG = FeedbackServiceImpl.class.getSimpleName();
    private CargoConnection mCargoConnection;
    private Context mContext;
    private CredentialsManager mCredentialsManager;
    private FiddlerLogger mFiddlerLogger;
    private LogConfiguration mLogConfiguration;
    private LogFormatManager mLogFormatManager;
    private SettingsProvider mSettingsProvider;
    private UserAgentProvider mUserAgentProvider;

    public FeedbackServiceImpl(Context context, LogConfiguration logConfiguration, LogFormatManager logFormatManager, CredentialsManager credentialsManager, FiddlerLogger fiddlerLogger, CargoConnection cargoConnection, UserAgentProvider userAgentProvider, SettingsProvider settingsProvider) {
        this.mContext = context;
        this.mLogConfiguration = logConfiguration;
        this.mLogFormatManager = logFormatManager;
        this.mFiddlerLogger = fiddlerLogger;
        this.mCredentialsManager = credentialsManager;
        this.mUserAgentProvider = userAgentProvider;
        this.mCargoConnection = cargoConnection;
        this.mSettingsProvider = settingsProvider;
    }

    public File createFeedbackArchiveFile(boolean shouldIncludeDiagnosticLogs, FeedbackContext feedbackContext, FeedbackDescription feedbackDescription, FeedbackMetadata feedbackMetadata, UUID packageName) throws IOException {
        File baseFolderLocation = getBaseStagingFolderLocation();
        if (shouldIncludeDiagnosticLogs) {
            this.mLogFormatManager.packageLog(baseFolderLocation.getAbsolutePath(), LogEntryType.MESSAGE);
        }
        this.mLogFormatManager.packageLog(baseFolderLocation.getAbsolutePath(), LogEntryType.IMAGE);
        if (shouldIncludeDiagnosticLogs && this.mLogConfiguration.getLogMode() == LogMode.CAN_LOG_PRIVATE_DATA) {
            this.mFiddlerLogger.createArchive(baseFolderLocation.getAbsolutePath());
        }
        if (feedbackContext != null) {
            FileUtils.writeStringToFile(new File(baseFolderLocation, LogConstants.FEEDBACK_CONTEXT_FILE), GsonUtils.getCustomSerializerWithPrettyPrinting().toJson(feedbackContext));
        }
        if (feedbackMetadata != null) {
            FileUtils.writeStringToFile(new File(baseFolderLocation, LogConstants.FEEDBACK_METADATA_FILE), GsonUtils.getCustomSerializerWithPrettyPrinting().toJson(feedbackMetadata));
        }
        if (feedbackDescription != null) {
            FileUtils.writeStringToFile(new File(baseFolderLocation, LogConstants.FEEDBACK_USER_FILE), GsonUtils.getCustomSerializerWithPrettyPrinting().toJson(feedbackDescription));
        }
        File feedbackZipFile = getFeedbackZipFile(packageName);
        FileUtils.zipFilesInDir(baseFolderLocation, feedbackZipFile);
        FileUtils.Delete(baseFolderLocation);
        return feedbackZipFile;
    }

    private File getBaseStagingFolderLocation() {
        UUID tempUUID = UUID.randomUUID();
        File baseFolderLocation = new File(this.mContext.getFilesDir(), tempUUID.toString());
        baseFolderLocation.mkdirs();
        return baseFolderLocation;
    }

    private File getFeedbackZipFile(UUID packageName) {
        File storageLocation;
        if (this.mLogConfiguration.getLogMode() == LogMode.DO_NOT_LOG_PRIVATE_DATA) {
            storageLocation = this.mContext.getFilesDir();
        } else {
            storageLocation = Environment.getExternalStorageDirectory();
        }
        File diagnosticsZipStore = new File(storageLocation, LogConstants.DIAGNOSTICS_ZIP_FOLDER);
        diagnosticsZipStore.mkdirs();
        File diagnosticsZipFile = new File(diagnosticsZipStore, packageName.toString() + LogConstants.DIAGNOSTICS_FILE_EXTENSION);
        return diagnosticsZipFile;
    }

    @Override // com.microsoft.kapp.feedback.FeedbackService
    public final boolean uploadArchive(String archiveLocation) {
        return true;
    }

    @Override // com.microsoft.kapp.feedback.FeedbackService
    public void sendFeedbackAsync(Activity activity, String sendingActivity, FeedbackDescription feedbackDescription, String emailText, ArrayList<Uri> imageUris, CargoUserProfile profile, boolean shouldSendLogs, Callback callback) {
        FeedbackTask feedbackTask = new FeedbackTask(activity, sendingActivity, feedbackDescription, emailText, imageUris, profile, shouldSendLogs, callback);
        feedbackTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* loaded from: classes.dex */
    public class FeedbackTask extends AsyncTask<Void, Void, Boolean> {
        private Activity mActivity;
        private File mArchive;
        private Callback<Boolean> mCallback;
        private String mEmailText;
        private FeedbackDescription mFeedbackDescription;
        private ArrayList<Uri> mImageUris;
        private CargoUserProfile mProfile;
        private String mSendingActivity;
        private boolean mShouldIncludeDiagnosticLogs;

        public FeedbackTask(Activity activity, String sendingActivity, FeedbackDescription feedbackDescription, String emailText, ArrayList<Uri> imageUris, CargoUserProfile profile, boolean shouldIncludeLogs, Callback<Boolean> callback) {
            this.mSendingActivity = sendingActivity;
            this.mFeedbackDescription = feedbackDescription;
            this.mProfile = profile;
            this.mImageUris = imageUris;
            this.mEmailText = emailText;
            this.mShouldIncludeDiagnosticLogs = shouldIncludeLogs;
            this.mCallback = callback;
            this.mActivity = activity;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... params) {
            if (this.mImageUris != null) {
                InputStream image_stream = null;
                Iterator i$ = this.mImageUris.iterator();
                while (i$.hasNext()) {
                    Uri uri = i$.next();
                    try {
                        image_stream = this.mActivity.getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(image_stream);
                        KLog.image(FeedbackServiceImpl.TAG, bitmap, "Feedback Image");
                    } catch (Exception ex) {
                        KLog.w(FeedbackServiceImpl.TAG, "Failed to log images", ex);
                    } finally {
                        StreamUtils.closeQuietly(image_stream);
                    }
                }
            }
            boolean success = true;
            try {
                FeedbackContext feedbackContext = FeedbackServiceImpl.this.getFeedbackContext(this.mSendingActivity, this.mProfile, this.mShouldIncludeDiagnosticLogs);
                UUID packageUUID = UUID.randomUUID();
                FeedbackMetadata feedbackMetadata = FeedbackServiceImpl.this.getFeedbackMetadata(packageUUID);
                this.mArchive = FeedbackServiceImpl.this.createFeedbackArchiveFile(this.mShouldIncludeDiagnosticLogs, feedbackContext, this.mFeedbackDescription, feedbackMetadata, packageUUID);
                File outputFileForUpload = new File(this.mArchive.getParentFile(), UUID.randomUUID().toString());
                FileUtils.copyFileUsingFileChannels(this.mArchive, outputFileForUpload);
                FeedbackServiceImpl.this.mCargoConnection.sendFileToCloud(outputFileForUpload.getAbsolutePath());
            } catch (Exception ex2) {
                success = false;
                KLog.e(FeedbackServiceImpl.TAG, "unable to create/upload feedback data", ex2);
            }
            return Boolean.valueOf(success);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public final void onPostExecute(Boolean success) {
            if (FeedbackServiceImpl.this.mLogConfiguration.getLogMode() == LogMode.CAN_LOG_PRIVATE_DATA) {
                try {
                    ArrayList<Uri> attachments = new ArrayList<>();
                    attachments.add(Uri.fromFile(this.mArchive));
                    FeedbackServiceImpl.this.sendEmail(this.mActivity, new String[]{Constants.FEEDBACK_EMAIL}, FeedbackServiceImpl.this.getEmailSubject(), FeedbackServiceImpl.this.generateHtmlBody(this.mEmailText), attachments);
                } catch (Exception ex) {
                    KLog.e(FeedbackServiceImpl.TAG, "unable to send feedback email", ex);
                }
            }
            if (this.mCallback != null) {
                this.mCallback.callback(success);
            }
        }
    }

    public void sendEmail(Activity activity, String[] recipients, String subject, String htmlBody, ArrayList<Uri> attachmentUris) {
        Intent emailIntent = new Intent("android.intent.action.SEND_MULTIPLE");
        emailIntent.setType("text/html");
        if (recipients != null) {
            emailIntent.putExtra("android.intent.extra.EMAIL", recipients);
        }
        if (subject != null) {
            emailIntent.putExtra("android.intent.extra.SUBJECT", subject);
        }
        if (htmlBody != null) {
            emailIntent.putExtra("android.intent.extra.TEXT", Html.fromHtml(htmlBody));
        }
        if (attachmentUris != null && !attachmentUris.isEmpty()) {
            emailIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", attachmentUris);
        }
        activity.startActivity(Intent.createChooser(emailIntent, this.mContext.getString(R.string.feedback_select_email_app)));
    }

    public FeedbackMetadata getFeedbackMetadata(UUID packageUUID) {
        FeedbackMetadata metadata = new FeedbackMetadata();
        metadata.setDate(DateTime.now());
        metadata.setVersion(LogConstants.METADATA_VERSION);
        metadata.setId(packageUUID.toString());
        try {
            metadata.setGeneratedBy(getPackageUserAgentString());
        } catch (Exception e) {
        }
        return metadata;
    }

    private String getPackageUserAgentString() throws PackageManager.NameNotFoundException {
        return String.format(LogConstants.PACKAGE_USER_AGENT_FORMAT, getApplicationVersion().toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getEmailSubject() throws PackageManager.NameNotFoundException {
        String appName = this.mContext.getResources().getString(R.string.app_name);
        String version = getApplicationVersion();
        return String.format("Feedback: Android %s App v%s", appName, version);
    }

    private String getApplicationVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0);
        String version = pInfo.versionName;
        return version;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String generateHtmlBody(String emailText) {
        StringBuilder content = new StringBuilder();
        content.append(this.mContext.getString(R.string.feedback_message));
        content.append("<br><br>");
        if (!TextUtils.isEmpty(emailText)) {
            content.append(emailText);
            content.append("<br><br>");
        }
        return content.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public FeedbackContext getFeedbackContext(String sender, CargoUserProfile profile, boolean shouldIncludeLogs) {
        return new FeedbackContext(this.mContext, sender, profile, this.mCargoConnection, this.mCredentialsManager, this.mLogConfiguration, this.mSettingsProvider, shouldIncludeLogs);
    }
}
