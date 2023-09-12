package com.microsoft.kapp.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BaseActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.models.strapp.StrappState;
import com.microsoft.kapp.models.strapp.StrappStateCollection;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.views.HorizontalSortableStrappListView;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class StrappReorderActivity extends BaseActivity {
    private final String TAG = getClass().getName();
    private ConfirmationBar mConfirmationBar;
    private TextView mErrorMessageTextView;
    private FrameLayout mLoadingFrameLayout;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    @Inject
    SettingsProvider mSettingsProvider;
    private HorizontalSortableStrappListView mSortableStrappList;
    @Inject
    StrappSettingsManager mStrappSettingsManager;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum State {
        LOADING,
        ERROR,
        READY
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strapp_reorder);
        this.mSortableStrappList = (HorizontalSortableStrappListView) findViewById(R.id.strapp_sort_list);
        BandVersion bandVersion = this.mStrappSettingsManager.getBandVersion();
        int wallpaperPatternId = this.mSettingsProvider.getCurrentWallpaperId();
        int baseColor = this.mPersonalizationManagerFactory.getPersonalizationManager(bandVersion).getThemeById(wallpaperPatternId).getBase();
        this.mSortableStrappList.setStrappBackgroundColor(baseColor);
        this.mErrorMessageTextView = (TextView) findViewById(R.id.error_message_text_view);
        this.mLoadingFrameLayout = (FrameLayout) findViewById(R.id.loading_frame_layout);
        this.mConfirmationBar = (ConfirmationBar) findViewById(R.id.confirmation_bar);
        this.mConfirmationBar.setVisibility(0);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappReorderActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (StrappReorderActivity.this.mErrorMessageTextView.getVisibility() != 0) {
                    StrappStateCollection finalStrapps = new StrappStateCollection();
                    LinkedHashMap<UUID, StrappState> newStrapps = StrappReorderActivity.this.mSortableStrappList.getElementsAsLinkedHashMap();
                    for (Map.Entry<UUID, StrappState> strapp : newStrapps.entrySet()) {
                        finalStrapps.put(strapp.getKey(), strapp.getValue());
                    }
                    StrappStateCollection originalStrapps = StrappReorderActivity.this.mStrappSettingsManager.getTransactionStrapps();
                    if (originalStrapps == null) {
                        originalStrapps = StrappReorderActivity.this.mStrappSettingsManager.getRetrievedStrapps();
                    }
                    if (originalStrapps != null) {
                        for (Map.Entry<UUID, StrappState> strapp2 : originalStrapps.entrySet()) {
                            if (!finalStrapps.containsKey(strapp2.getKey())) {
                                finalStrapps.put(strapp2.getKey(), strapp2.getValue());
                            }
                        }
                    }
                    StrappReorderActivity.this.mStrappSettingsManager.setTransactionStrapps(finalStrapps);
                    StrappReorderActivity.this.mStrappSettingsManager.setIsOrderChanged(true);
                    StrappReorderActivity.this.finish();
                }
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.settings.StrappReorderActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StrappReorderActivity.this.finish();
            }
        });
        if (this.mStrappSettingsManager.getRetrievedStrapps() == null || !initializeStrapps()) {
            KLog.e(this.TAG, "Unable to initialize from StrappSettingsManager data.");
            switchPanel(State.ERROR);
            return;
        }
        switchPanel(State.READY);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.SETTINGS_BAND_MANAGE_TILES_REORDER);
    }

    private void switchPanel(State state) {
        Validate.notNull(state, "state");
        this.mErrorMessageTextView.setVisibility(state == State.ERROR ? 0 : 8);
        this.mLoadingFrameLayout.setVisibility(state != State.LOADING ? 8 : 0);
    }

    private boolean initializeStrapps() {
        StrappStateCollection transactionStrapps = this.mStrappSettingsManager.getTransactionStrapps();
        StrappStateCollection retrievedStrapps = this.mStrappSettingsManager.getRetrievedStrapps();
        if (transactionStrapps != null) {
            populateDataLocally(transactionStrapps.entrySet());
        } else if (retrievedStrapps != null) {
            populateDataLocally(retrievedStrapps.entrySet());
        } else {
            KLog.e(this.TAG, "Error retrieving strapps from StrappSettingsManager.");
            return false;
        }
        return true;
    }

    public void populateDataLocally(Set<Map.Entry<UUID, StrappState>> strappList) {
        this.mSortableStrappList.clear();
        for (Map.Entry<UUID, StrappState> strappEntry : strappList) {
            if (!DefaultStrappUUID.STRAPP_THIRD_PARTY_FILTER.contains(strappEntry.getKey())) {
                this.mSortableStrappList.addElement(strappEntry.getValue());
            }
        }
    }
}
