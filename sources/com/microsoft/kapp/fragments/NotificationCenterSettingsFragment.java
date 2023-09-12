package com.microsoft.kapp.fragments;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.adapters.NotificationCenterListAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.SettingsUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class NotificationCenterSettingsFragment extends BaseFragment implements View.OnClickListener {
    private TreeMap<CharSequence, ApplicationInfo> mAllApps;
    private ConfirmationBar mConfirmationBar;
    private ListView mCurrentAppsListView;
    private TextView mDisableAll;
    private TextView mEnableAll;
    private ArrayList<String> mEnabledApps;
    private NotificationCenterListAdapter mNotificationAdapter;
    @Inject
    StrappSettingsManager mStrappSettingsManager;

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notification_center_settings, container, false);
        View header = inflater.inflate(R.layout.notification_center_settings_header, (ViewGroup) null);
        this.mEnableAll = (TextView) ViewUtils.getValidView(header, R.id.notification_apps_select_all, TextView.class);
        this.mEnableAll.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.NotificationCenterSettingsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                NotificationCenterSettingsFragment.this.enableAllApps();
                NotificationCenterSettingsFragment.this.mConfirmationBar.setVisibility(0);
                if (NotificationCenterSettingsFragment.this.mNotificationAdapter != null) {
                    NotificationCenterSettingsFragment.this.mNotificationAdapter.notifyDataSetChanged();
                }
            }
        });
        this.mDisableAll = (TextView) ViewUtils.getValidView(header, R.id.notification_apps_select_none, TextView.class);
        this.mDisableAll.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.NotificationCenterSettingsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                NotificationCenterSettingsFragment.this.mEnabledApps.clear();
                NotificationCenterSettingsFragment.this.mConfirmationBar.setVisibility(0);
                if (NotificationCenterSettingsFragment.this.mNotificationAdapter != null) {
                    NotificationCenterSettingsFragment.this.mNotificationAdapter.notifyDataSetChanged();
                }
            }
        });
        EditText searchEditText = (EditText) ViewUtils.getValidView(header, R.id.notification_apps_search, EditText.class);
        searchEditText.addTextChangedListener(new TextWatcher() { // from class: com.microsoft.kapp.fragments.NotificationCenterSettingsFragment.3
            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                NotificationCenterSettingsFragment.this.mEnableAll.setVisibility(text.length() == 0 ? 0 : 8);
                NotificationCenterSettingsFragment.this.mDisableAll.setVisibility(text.length() != 0 ? 8 : 0);
                if (NotificationCenterSettingsFragment.this.mNotificationAdapter != null) {
                    NotificationCenterSettingsFragment.this.mNotificationAdapter.getFilter().filter(text);
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        });
        this.mCurrentAppsListView = (ListView) ViewUtils.getValidView(view, R.id.app_enable_listview, ListView.class);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(view, R.id.confirmation_bar, ConfirmationBar.class);
        this.mCurrentAppsListView.addHeaderView(header, null, false);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.NotificationCenterSettingsFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (NotificationCenterSettingsFragment.this.mEnabledApps.size() > 0) {
                    NotificationCenterSettingsFragment.this.mStrappSettingsManager.setTransactionNotificationCenterApps(NotificationCenterSettingsFragment.this.mEnabledApps);
                    ActivityUtils.performBackButton(NotificationCenterSettingsFragment.this.getActivity());
                    return;
                }
                NotificationCenterSettingsFragment.this.getDialogManager().showDialog(NotificationCenterSettingsFragment.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.error_no_apps_selected), DialogPriority.LOW);
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.NotificationCenterSettingsFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ActivityUtils.performBackButton(NotificationCenterSettingsFragment.this.getActivity());
            }
        });
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableAllApps() {
        this.mEnabledApps = new ArrayList<>();
        for (Map.Entry<CharSequence, ApplicationInfo> app : this.mAllApps.entrySet()) {
            this.mEnabledApps.add(app.getValue().packageName);
        }
        setEnabledApps(this.mEnabledApps);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_CONNECTED_NOTIFICATIONS_NOTIFICATION_CENTER_APPS);
        LoadAppsTask task = new LoadAppsTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void setEnabledApps(ArrayList<String> currentEnabledApps) {
        this.mEnabledApps = new ArrayList<>(currentEnabledApps);
        this.mCurrentAppsListView.setVisibility(0);
        this.mNotificationAdapter = new NotificationCenterListAdapter(getActivity(), this.mAllApps, this.mEnabledApps);
        this.mNotificationAdapter.setOnToggleAppListener(this);
        this.mCurrentAppsListView.setAdapter((ListAdapter) this.mNotificationAdapter);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        this.mConfirmationBar.setVisibility(0);
        if (v.getTag() != null && this.mEnabledApps.contains(v.getTag())) {
            this.mEnabledApps.remove(v.getTag());
        } else {
            this.mEnabledApps.add((String) v.getTag());
        }
    }

    /* loaded from: classes.dex */
    private class LoadAppsTask extends AsyncTask<Void, Void, TreeMap<CharSequence, ApplicationInfo>> {
        protected LoadAppsTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public final TreeMap<CharSequence, ApplicationInfo> doInBackground(Void... params) {
            return SettingsUtils.getAllInstalledApps(NotificationCenterSettingsFragment.this.getActivity());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public final void onPostExecute(TreeMap<CharSequence, ApplicationInfo> installedApps) {
            if (NotificationCenterSettingsFragment.this.isAdded()) {
                NotificationCenterSettingsFragment.this.mAllApps = installedApps;
                ArrayList<String> currentEnabledApps = NotificationCenterSettingsFragment.this.mStrappSettingsManager.getNotificationCenterApps();
                if (currentEnabledApps.size() == 0) {
                    NotificationCenterSettingsFragment.this.enableAllApps();
                } else {
                    NotificationCenterSettingsFragment.this.setEnabledApps(currentEnabledApps);
                }
            }
        }
    }
}
