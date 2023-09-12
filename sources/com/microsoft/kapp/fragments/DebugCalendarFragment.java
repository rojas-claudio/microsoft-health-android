package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.ViewUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DebugCalendarFragment extends BaseFragment {
    private TextView mLastErrorMessage;
    private TextView mLastErrorTime;
    private TextView mLastSentEvents;
    private TextView mLastSentTime;
    private Button mRefresh;
    @Inject
    SettingsProvider mSettingsProvider;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.debug_fragment_calendar, container, false);
        this.mLastSentTime = (TextView) ViewUtils.getValidView(rootView, R.id.last_sent_time, TextView.class);
        this.mLastSentEvents = (TextView) ViewUtils.getValidView(rootView, R.id.last_sent_events, TextView.class);
        this.mLastErrorTime = (TextView) ViewUtils.getValidView(rootView, R.id.last_error_time, TextView.class);
        this.mLastErrorMessage = (TextView) ViewUtils.getValidView(rootView, R.id.last_error_message, TextView.class);
        this.mRefresh = (Button) ViewUtils.getValidView(rootView, R.id.refresh_button, Button.class);
        this.mRefresh.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.DebugCalendarFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DebugCalendarFragment.this.load();
            }
        });
        load();
        return rootView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.fragments.BaseFragment
    public void load() {
        super.load();
        this.mLastSentTime.setText(this.mSettingsProvider.getCalendarLastSyncTime());
        this.mLastSentEvents.setText(this.mSettingsProvider.getCalendarLastSyncEvents());
        this.mLastErrorTime.setText(this.mSettingsProvider.getCalendarLastErrorTime());
        this.mLastErrorMessage.setText(this.mSettingsProvider.getCalendarLastErrorMessage());
    }
}
