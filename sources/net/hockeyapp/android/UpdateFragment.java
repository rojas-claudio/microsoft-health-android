package net.hockeyapp.android;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import net.hockeyapp.android.listeners.DownloadFileListener;
import net.hockeyapp.android.tasks.DownloadFileTask;
import net.hockeyapp.android.tasks.GetFileSizeTask;
import net.hockeyapp.android.utils.AsyncTaskUtils;
import net.hockeyapp.android.utils.VersionHelper;
import net.hockeyapp.android.views.UpdateView;
import org.json.JSONArray;
import org.json.JSONException;
/* loaded from: classes.dex */
public class UpdateFragment extends DialogFragment implements View.OnClickListener, UpdateInfoListener {
    private DownloadFileTask downloadTask;
    private String urlString;
    private VersionHelper versionHelper;
    private JSONArray versionInfo;

    public static UpdateFragment newInstance(JSONArray versionInfo, String urlString) {
        Bundle state = new Bundle();
        state.putString("url", urlString);
        state.putString("versionInfo", versionInfo.toString());
        UpdateFragment fragment = new UpdateFragment();
        fragment.setArguments(state);
        return fragment;
    }

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.urlString = getArguments().getString("url");
            this.versionInfo = new JSONArray(getArguments().getString("versionInfo"));
            setStyle(1, 16973939);
        } catch (JSONException e) {
            dismiss();
        }
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutView();
        this.versionHelper = new VersionHelper(getActivity(), this.versionInfo.toString(), this);
        TextView nameLabel = (TextView) view.findViewById(UpdateView.NAME_LABEL_ID);
        nameLabel.setText(getAppName());
        final TextView versionLabel = (TextView) view.findViewById(4099);
        final String versionString = "Version " + this.versionHelper.getVersionString();
        final String fileDate = this.versionHelper.getFileDateString();
        String appSizeString = "Unknown size";
        long appSize = this.versionHelper.getFileSizeBytes();
        if (appSize >= 0) {
            appSizeString = String.format("%.2f", Float.valueOf(((float) appSize) / 1048576.0f)) + " MB";
        } else {
            GetFileSizeTask task = new GetFileSizeTask(getActivity(), this.urlString, new DownloadFileListener() { // from class: net.hockeyapp.android.UpdateFragment.1
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
        Button updateButton = (Button) view.findViewById(UpdateView.UPDATE_BUTTON_ID);
        updateButton.setOnClickListener(this);
        WebView webView = (WebView) view.findViewById(UpdateView.WEB_VIEW_ID);
        webView.clearCache(true);
        webView.destroyDrawingCache();
        webView.loadDataWithBaseURL(Constants.BASE_URL, this.versionHelper.getReleaseNotes(false), "text/html", "utf-8", null);
        return view;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        startDownloadTask(getActivity());
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDownloadTask(final Activity activity) {
        this.downloadTask = new DownloadFileTask(activity, this.urlString, new DownloadFileListener() { // from class: net.hockeyapp.android.UpdateFragment.2
            @Override // net.hockeyapp.android.listeners.DownloadFileListener
            public void downloadFailed(DownloadFileTask task, Boolean userWantsRetry) {
                if (userWantsRetry.booleanValue()) {
                    UpdateFragment.this.startDownloadTask(activity);
                }
            }

            @Override // net.hockeyapp.android.listeners.DownloadFileListener
            public void downloadSuccessful(DownloadFileTask task) {
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

    @Override // net.hockeyapp.android.UpdateInfoListener
    public int getCurrentVersionCode() {
        try {
            int currentVersionCode = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 128).versionCode;
            return currentVersionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        } catch (NullPointerException e2) {
            return -1;
        }
    }

    public String getAppName() {
        Activity activity = getActivity();
        try {
            PackageManager pm = activity.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(activity.getPackageName(), 0);
            return pm.getApplicationLabel(applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public View getLayoutView() {
        return new UpdateView(getActivity(), false, true);
    }
}
