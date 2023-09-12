package com.microsoft.kapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.views.BingMapView;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class BingMapActivity extends BaseFragmentActivityWithOfflineSupport {
    public static final String ARG_IN_EVENT = "event_id";
    public static final String ARG_IN_EVENT_TYPE = "event_type";
    private static final String TAG = BingMapActivity.class.getSimpleName();
    private BingMapView mBingMapView;
    private String mEventId;
    private EventType mEventType;
    @Inject
    RestService mRestService;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport
    protected void onCreate(ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.activity_bing_map);
        this.mEventId = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mEventId = bundle.getString(ARG_IN_EVENT);
            this.mEventType = EventType.values()[bundle.getInt(ARG_IN_EVENT_TYPE)];
            this.mBingMapView = (BingMapView) ActivityUtils.getAndValidateView(this, R.id.bing_map_view, BingMapView.class);
            this.mBingMapView.enableTouchEvents();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_RUN_EXPANDED_MAP);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        switch (this.mEventType) {
            case Biking:
                showBikeEvent();
                return;
            case Running:
                showRunEvent();
                return;
            default:
                return;
        }
    }

    public void onClosePressed(View view) {
        onBackPressed();
    }

    private void showBikeEvent() {
        setState(1233);
        if (TextUtils.isEmpty(this.mEventId)) {
            setState(1235);
            return;
        }
        ArrayList<RestService.ExpandType> expandTypes = new ArrayList<>();
        expandTypes.add(RestService.ExpandType.INFO);
        expandTypes.add(RestService.ExpandType.MAPPOINTS);
        expandTypes.add(RestService.ExpandType.SEQUENCES);
        this.mRestService.getBikeEventById(this.mSettingsProvider.isDistanceHeightMetric(), this.mEventId, expandTypes, new ActivityScopedCallback(this, new Callback<BikeEvent>() { // from class: com.microsoft.kapp.activities.BingMapActivity.1
            @Override // com.microsoft.kapp.Callback
            public void callback(BikeEvent result) {
                if (result != null && result.getMapPoints() != null) {
                    try {
                        BingMapActivity.this.mBingMapView.loadData(null, result, false, result.getSplitGroupSize(), false, false);
                        BingMapActivity.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        BingMapActivity.this.setState(1235);
                        KLog.e(BingMapActivity.TAG, "Exception loading run", ex);
                        return;
                    }
                }
                BingMapActivity.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                BingMapActivity.this.setState(1235);
                KLog.e(BingMapActivity.TAG, "Exception loading run", ex);
            }
        }));
    }

    private void showRunEvent() {
        setState(1233);
        if (TextUtils.isEmpty(this.mEventId)) {
            setState(1235);
            return;
        }
        ArrayList<RestService.ExpandType> expandTypes = new ArrayList<>();
        expandTypes.add(RestService.ExpandType.INFO);
        expandTypes.add(RestService.ExpandType.MAPPOINTS);
        expandTypes.add(RestService.ExpandType.SEQUENCES);
        this.mRestService.getRunEventById(this.mSettingsProvider.isDistanceHeightMetric(), this.mEventId, expandTypes, new ActivityScopedCallback(this, new Callback<RunEvent>() { // from class: com.microsoft.kapp.activities.BingMapActivity.2
            @Override // com.microsoft.kapp.Callback
            public void callback(RunEvent result) {
                if (result != null && result.getMapPoints() != null) {
                    try {
                        BingMapActivity.this.mBingMapView.loadData(null, result, false, 1, false, true);
                        BingMapActivity.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        BingMapActivity.this.setState(1235);
                        KLog.e(BingMapActivity.TAG, "Exception loading run", ex);
                        return;
                    }
                }
                BingMapActivity.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                BingMapActivity.this.setState(1235);
                KLog.e(BingMapActivity.TAG, "Exception loading run", ex);
            }
        }));
    }
}
