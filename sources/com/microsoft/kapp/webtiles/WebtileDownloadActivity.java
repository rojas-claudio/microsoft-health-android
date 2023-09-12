package com.microsoft.kapp.webtiles;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.webtiles.WebTile;
import com.microsoft.band.webtiles.WebTileException;
import com.microsoft.band.webtiles.WebTileManager;
import com.microsoft.band.webtiles.WebTileResource;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.ManageTilesFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.kapp.widgets.Interstitial;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class WebtileDownloadActivity extends BaseActivity {
    private static final String ACTION = "action";
    private static final String ACTION_DOWNLOAD_MANIFEST = "download-manifest";
    private static final String TAG = WebtileDownloadActivity.class.getSimpleName();
    private static final String URL = "url";
    private static final String WEBTILE_LINK_SCHEME = "mshealth-webtile";
    private String mBasePath;
    @Inject
    protected CargoConnection mCargoConnection;
    private Interstitial mInterstitial;
    @Inject
    SettingsProvider mSettingsProvider;
    private volatile WebTile mWebtile;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtile_download);
        this.mInterstitial = (Interstitial) findViewById(R.id.webtiles_indicator);
        this.mInterstitial.setSlide(6000);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (FreUtils.isOOBEComplete(this.mSettingsProvider) || !FreUtils.freRedirect(this, this.mSettingsProvider)) {
            this.mBasePath = getFilesDir().getAbsolutePath();
            Uri uri = getIntent().getData();
            if (uri != null) {
                KLog.logPrivate(TAG, "URI = %s", uri);
                String scheme = uri.getScheme();
                if (WEBTILE_LINK_SCHEME.equals(scheme)) {
                    handleWeblink(uri);
                    return;
                } else if ("content".equals(scheme) || "file".equals(scheme)) {
                    getWebtileFromFile(uri);
                    return;
                } else {
                    showUrlErrorDialog();
                    return;
                }
            }
            showUrlErrorDialog();
        }
    }

    private void handleWeblink(Uri uri) {
        String actionParam = uri.getQueryParameter(ACTION);
        KLog.logPrivate(TAG, "Action = %s", actionParam);
        String urlParam = uri.getQueryParameter("url");
        KLog.logPrivate(TAG, "URL = %s", urlParam);
        if (actionParam != null && urlParam != null) {
            if (ACTION_DOWNLOAD_MANIFEST.equals(actionParam)) {
                downloadWebtileFromUrl(urlParam);
                return;
            } else {
                showUrlErrorDialog();
                return;
            }
        }
        showUrlErrorDialog();
    }

    private void downloadWebtileFromUrl(String url) {
        DownloadWebtileTask task = new DownloadWebtileTask(this);
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, url);
    }

    private void getWebtileFromFile(Uri uri) {
        GetWebtileFromFileTask task = new GetWebtileFromFileTask(this);
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, uri);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DownloadWebtileTask extends ScopedAsyncTask<String, Void, Void> {
        public DownloadWebtileTask(WebtileDownloadActivity onTaskListener) {
            super(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(String... params) {
            try {
                WebtileDownloadActivity.this.mWebtile = WebTileManager.createWebTileFromWebUrl(params[0], WebtileDownloadActivity.this.mBasePath);
                return null;
            } catch (Exception ex) {
                KLog.e(this.TAG, "Exception caught while downloading webtile:", ex);
                setException(ex);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void result) {
            if (getException() == null) {
                if (WebtileDownloadActivity.this.mWebtile != null) {
                    WebtileDownloadActivity.this.launchAddTileDialog();
                    return;
                }
                return;
            }
            KLog.e(this.TAG, "Error creating webtile from URL", getException());
            WebtileDownloadActivity.this.LogWebtileInstallationResult("Unknown WebTile", getException().getMessage());
            if (getException().getClass() == WebTileException.class) {
                WebtileDownloadActivity.this.showUrlErrorDialog();
            } else {
                WebtileDownloadActivity.this.showTileErrorDialog();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GetWebtileFromFileTask extends ScopedAsyncTask<Uri, Void, Void> {
        private WeakReference<WebtileDownloadActivity> mWeakActivity;

        public GetWebtileFromFileTask(WebtileDownloadActivity onTaskListener) {
            super(onTaskListener);
            this.mWeakActivity = new WeakReference<>(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(Uri... params) {
            try {
                if (this.mWeakActivity.get() != null) {
                    WebtileDownloadActivity.this.mWebtile = WebTileManager.createWebTileFromFileUrl(params[0], WebtileDownloadActivity.this.mBasePath, this.mWeakActivity.get().getContentResolver());
                    return null;
                }
                return null;
            } catch (Exception ex) {
                KLog.e(this.TAG, "Exception caught while getting webtile from file:", ex);
                setException(ex);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void result) {
            if (getException() == null) {
                if (WebtileDownloadActivity.this.mWebtile != null) {
                    WebtileDownloadActivity.this.launchAddTileDialog();
                    return;
                }
                return;
            }
            KLog.e(this.TAG, "Error getting webtile from file", getException());
            WebtileDownloadActivity.this.LogWebtileInstallationResult("Unknown WebTile", getException().getMessage());
            WebtileDownloadActivity.this.showTileErrorDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchAddTileDialog() {
        this.mInterstitial.setVisibility(8);
        OnWebTileDialogClick confirmClickListener = new OnWebTileDialogClick() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.1
            @Override // com.microsoft.kapp.webtiles.OnWebTileDialogClick
            public void onClick() {
                WebtileDownloadActivity.this.authenticateWebTile(WebtileDownloadActivity.this.mWebtile);
            }
        };
        OnWebTileDialogClick cancelClickListener = new OnWebTileDialogClick() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.2
            @Override // com.microsoft.kapp.webtiles.OnWebTileDialogClick
            public void onClick() {
                WebtileDownloadActivity.this.finish();
            }
        };
        WebTileAddDialog dialog = new WebTileAddDialog(this, confirmClickListener, cancelClickListener, this.mWebtile);
        dialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void authenticateWebTile(WebTile webTile) {
        Callback<List<WebTileResource>> callback = new Callback<List<WebTileResource>>() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.3
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.e(WebtileDownloadActivity.TAG, "Exception while authenticating webtile", ex);
                WebtileDownloadActivity.this.LogWebtileInstallationResult("Exception while authenticating webtile", ex.getMessage());
                if (ex.getClass() == WebTileException.class) {
                    WebtileDownloadActivity.this.showWebTileAuthErrorDialog();
                } else {
                    WebtileDownloadActivity.this.showTileErrorDialog();
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(List<WebTileResource> authRequiredResources) {
                if (authRequiredResources == null) {
                    WebtileDownloadActivity.this.mInterstitial.setSlide(Interstitial.SLIDE_ADDING_WEBTILE);
                    WebtileDownloadActivity.this.saveWebtileToBand(WebtileDownloadActivity.this.mWebtile);
                    return;
                }
                OnWebTileDialogClick authCompletedClickListener = new OnWebTileDialogClick() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.3.1
                    @Override // com.microsoft.kapp.webtiles.OnWebTileDialogClick
                    public void onClick() {
                        WebtileDownloadActivity.this.mInterstitial.setSlide(Interstitial.SLIDE_ADDING_WEBTILE);
                        WebtileDownloadActivity.this.saveWebtileToBand(WebtileDownloadActivity.this.mWebtile);
                    }
                };
                OnWebTileDialogClick cancelClickListener = new OnWebTileDialogClick() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.3.2
                    @Override // com.microsoft.kapp.webtiles.OnWebTileDialogClick
                    public void onClick() {
                        WebtileDownloadActivity.this.finish();
                    }
                };
                WebTileAuthDialog dialog = new WebTileAuthDialog(WebtileDownloadActivity.this, authRequiredResources, authCompletedClickListener, cancelClickListener, WebtileDownloadActivity.this.mInterstitial, WebtileDownloadActivity.this.mWebtile);
                dialog.show();
            }
        };
        WebTileAuthTask task = new WebTileAuthTask(this, callback);
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, webTile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveWebtileToBand(WebTile webtile) {
        AddWebtileTask task = new AddWebtileTask(this);
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, webtile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AddWebtileTask extends ScopedAsyncTask<WebTile, Void, Void> {
        public AddWebtileTask(WebtileDownloadActivity onTaskListener) {
            super(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(WebTile... params) {
            try {
                WebtileDownloadActivity.this.mCargoConnection.saveWebTileToDevice(WebtileDownloadActivity.this.mWebtile);
                return null;
            } catch (Exception ex) {
                setException(ex);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void result) {
            if (getException() == null) {
                WebtileDownloadActivity.this.showSuccessDialog();
                WebtileDownloadActivity.this.LogWebtileInstallationResult(WebtileDownloadActivity.this.mWebtile.getName(), null);
                return;
            }
            if (((CargoException) getException()).getResponse() == BandServiceMessage.Response.BAND_IS_FULL_ERROR) {
                WebtileDownloadActivity.this.showBandFullErrorDialog();
            } else {
                WebtileDownloadActivity.this.showBluetoothErrorDialog();
            }
            WebtileDownloadActivity.this.LogWebtileInstallationResult(WebtileDownloadActivity.this.mWebtile.getName(), getException().getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSuccessDialog() {
        this.mInterstitial.setVisibility(8);
        DialogInterface.OnClickListener successListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(WebtileDownloadActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.STARTING_PAGE, ManageTilesFragment.class.getSimpleName());
                WebtileDownloadActivity.this.startActivity(intent);
            }
        };
        getDialogManager().showDialog(this, Integer.valueOf((int) R.string.webtile_success_title), Integer.valueOf((int) R.string.webtile_success_message), successListener, DialogPriority.LOW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUrlErrorDialog() {
        this.mInterstitial.setVisibility(8);
        DialogInterface.OnClickListener urlErrorListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                WebtileDownloadActivity.this.finish();
            }
        };
        getDialogManager().showDialog(this, Integer.valueOf((int) R.string.webtile_url_error_title), Integer.valueOf((int) R.string.webtile_url_error_message), urlErrorListener, DialogPriority.LOW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTileErrorDialog() {
        this.mInterstitial.setVisibility(8);
        DialogInterface.OnClickListener tileErrorListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                WebtileDownloadActivity.this.finish();
            }
        };
        getDialogManager().showDialog(this, Integer.valueOf((int) R.string.webtile_tile_error_title), Integer.valueOf((int) R.string.webtile_tile_error_message), tileErrorListener, DialogPriority.LOW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBluetoothErrorDialog() {
        this.mInterstitial.setVisibility(8);
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                WebtileDownloadActivity.this.finish();
            }
        };
        getDialogManager().showDeviceErrorDialogWithCallback(this, clickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBandFullErrorDialog() {
        this.mInterstitial.setVisibility(8);
        DialogInterface.OnClickListener bandFullErrorListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(WebtileDownloadActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.STARTING_PAGE, ManageTilesFragment.class.getSimpleName());
                WebtileDownloadActivity.this.startActivity(intent);
            }
        };
        getDialogManager().showDialog(this, Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.exceed_maximum_allowed_strapps_toast), bandFullErrorListener, DialogPriority.LOW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWebTileAuthErrorDialog() {
        this.mInterstitial.setVisibility(8);
        DialogInterface.OnClickListener webTileErrorListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.webtiles.WebtileDownloadActivity.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                WebtileDownloadActivity.this.finish();
            }
        };
        getDialogManager().showDialog(this, Integer.valueOf((int) R.string.webtile_tile_error_title), Integer.valueOf((int) R.string.webtile_tile_auth_error_message), webTileErrorListener, DialogPriority.LOW);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void LogWebtileInstallationResult(String webtileName, String failureReason) {
        HashMap<String, String> properties = new HashMap<>();
        properties.put("WebTile Name", webtileName);
        properties.put("WebTile Source", getIntent().getData().toString());
        if (failureReason != null) {
            properties.put(TelemetryConstants.Events.WebTileInstallationFailure.Dimensions.FAILURE_REASON, failureReason);
            Telemetry.logEvent(TelemetryConstants.Events.WebTileInstallationFailure.EVENT_NAME, properties, null);
            return;
        }
        Telemetry.logEvent(TelemetryConstants.Events.WebTileInstallationSuccess.EVENT_NAME, properties, null);
    }
}
