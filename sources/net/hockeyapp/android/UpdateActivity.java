package net.hockeyapp.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import net.hockeyapp.android.listeners.DownloadFileListener;
import net.hockeyapp.android.objects.ErrorObject;
import net.hockeyapp.android.tasks.DownloadFileTask;
import net.hockeyapp.android.tasks.GetFileSizeTask;
import net.hockeyapp.android.utils.AsyncTaskUtils;
import net.hockeyapp.android.utils.VersionHelper;
import net.hockeyapp.android.views.UpdateView;
import org.acra.ACRAConstants;
/* loaded from: classes.dex */
public class UpdateActivity extends Activity implements UpdateActivityInterface, UpdateInfoListener, View.OnClickListener {
    private final int DIALOG_ERROR_ID = 0;
    private Context context;
    protected DownloadFileTask downloadTask;
    private ErrorObject error;
    protected VersionHelper versionHelper;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("App Update");
        setContentView(getLayoutView());
        this.context = this;
        this.versionHelper = new VersionHelper(this, getIntent().getStringExtra("json"), this);
        configureView();
        this.downloadTask = (DownloadFileTask) getLastNonConfigurationInstance();
        if (this.downloadTask != null) {
            this.downloadTask.attach(this);
        }
    }

    protected void configureView() {
        TextView nameLabel = (TextView) findViewById(UpdateView.NAME_LABEL_ID);
        nameLabel.setText(getAppName());
        final TextView versionLabel = (TextView) findViewById(4099);
        final String versionString = "Version " + this.versionHelper.getVersionString();
        final String fileDate = this.versionHelper.getFileDateString();
        String appSizeString = "Unknown size";
        long appSize = this.versionHelper.getFileSizeBytes();
        if (appSize >= 0) {
            appSizeString = String.format("%.2f", Float.valueOf(((float) appSize) / 1048576.0f)) + " MB";
        } else {
            GetFileSizeTask task = new GetFileSizeTask(this, getIntent().getStringExtra("url"), new DownloadFileListener() { // from class: net.hockeyapp.android.UpdateActivity.1
                @Override // net.hockeyapp.android.listeners.DownloadFileListener
                public void downloadSuccessful(DownloadFileTask task2) {
                    if (task2 instanceof GetFileSizeTask) {
                        long appSize2 = ((GetFileSizeTask) task2).getSize();
                        String appSizeString2 = String.format("%.2f", Float.valueOf(((float) appSize2) / 1048576.0f)) + " MB";
                        versionLabel.setText(versionString + "\n" + fileDate + " - " + appSizeString2);
                    }
                }
            });
            AsyncTaskUtils.execute(task);
        }
        versionLabel.setText(versionString + "\n" + fileDate + " - " + appSizeString);
        Button updateButton = (Button) findViewById(UpdateView.UPDATE_BUTTON_ID);
        updateButton.setOnClickListener(this);
        WebView webView = (WebView) findViewById(UpdateView.WEB_VIEW_ID);
        webView.clearCache(true);
        webView.destroyDrawingCache();
        webView.loadDataWithBaseURL(Constants.BASE_URL, getReleaseNotes(), "text/html", "utf-8", null);
    }

    protected String getReleaseNotes() {
        return this.versionHelper.getReleaseNotes(false);
    }

    @Override // android.app.Activity
    public Object onRetainNonConfigurationInstance() {
        if (this.downloadTask != null) {
            this.downloadTask.detach();
        }
        return this.downloadTask;
    }

    protected void startDownloadTask() {
        String url = getIntent().getStringExtra("url");
        startDownloadTask(url);
    }

    protected void startDownloadTask(String url) {
        createDownloadTask(url, new DownloadFileListener() { // from class: net.hockeyapp.android.UpdateActivity.2
            @Override // net.hockeyapp.android.listeners.DownloadFileListener
            public void downloadSuccessful(DownloadFileTask task) {
                UpdateActivity.this.enableUpdateButton();
            }

            @Override // net.hockeyapp.android.listeners.DownloadFileListener
            public void downloadFailed(DownloadFileTask task, Boolean userWantsRetry) {
                if (userWantsRetry.booleanValue()) {
                    UpdateActivity.this.startDownloadTask();
                } else {
                    UpdateActivity.this.enableUpdateButton();
                }
            }

            @Override // net.hockeyapp.android.StringListener
            public String getStringForResource(int resourceID) {
                UpdateManagerListener listener = UpdateManager.getLastListener();
                if (listener != null) {
                    return listener.getStringForResource(resourceID);
                }
                return null;
            }
        });
        AsyncTaskUtils.execute(this.downloadTask);
    }

    protected void createDownloadTask(String url, DownloadFileListener listener) {
        this.downloadTask = new DownloadFileTask(this, url, listener);
    }

    public void enableUpdateButton() {
        View updateButton = findViewById(UpdateView.UPDATE_BUTTON_ID);
        updateButton.setEnabled(true);
    }

    @Override // net.hockeyapp.android.UpdateInfoListener
    public int getCurrentVersionCode() {
        try {
            int currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 128).versionCode;
            return currentVersionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    @Override // net.hockeyapp.android.UpdateActivityInterface
    public ViewGroup getLayoutView() {
        return new UpdateView(this);
    }

    public String getAppName() {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(getPackageName(), 0);
            return pm.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    private boolean isWriteExternalStorageSet(Context context) {
        int res = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return res == 0;
    }

    private boolean isUnknownSourcesChecked() {
        boolean z = true;
        try {
            if (Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 21) {
                if (Settings.Global.getInt(getContentResolver(), "install_non_market_apps") != 1) {
                    z = false;
                }
            } else if (Settings.Secure.getInt(getContentResolver(), "install_non_market_apps") != 1) {
                z = false;
            }
        } catch (Settings.SettingNotFoundException e) {
        }
        return z;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (!isWriteExternalStorageSet(this.context)) {
            this.error = new ErrorObject();
            this.error.setMessage("The permission to access the external storage permission is not set. Please contact the developer.");
            runOnUiThread(new Runnable() { // from class: net.hockeyapp.android.UpdateActivity.3
                @Override // java.lang.Runnable
                public void run() {
                    UpdateActivity.this.showDialog(0);
                }
            });
        } else if (!isUnknownSourcesChecked()) {
            this.error = new ErrorObject();
            this.error.setMessage("The installation from unknown sources is not enabled. Please check the device settings.");
            runOnUiThread(new Runnable() { // from class: net.hockeyapp.android.UpdateActivity.4
                @Override // java.lang.Runnable
                public void run() {
                    UpdateActivity.this.showDialog(0);
                }
            });
        } else {
            startDownloadTask();
            v.setEnabled(false);
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new AlertDialog.Builder(this).setMessage("An error has occured").setCancelable(false).setTitle(TelemetryConstants.Events.Error.Dimensions.LOG_TYPE_ERROR).setIcon(ACRAConstants.DEFAULT_DIALOG_ICON).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: net.hockeyapp.android.UpdateActivity.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int id2) {
                        UpdateActivity.this.error = null;
                        dialog.cancel();
                    }
                }).create();
            default:
                return null;
        }
    }

    @Override // android.app.Activity
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case 0:
                AlertDialog messageDialogError = (AlertDialog) dialog;
                if (this.error != null) {
                    messageDialogError.setMessage(this.error.getMessage());
                    return;
                } else {
                    messageDialogError.setMessage("An unknown error has occured.");
                    return;
                }
            default:
                return;
        }
    }
}
