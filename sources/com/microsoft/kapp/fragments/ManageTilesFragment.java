package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.microsoft.applicationinsights.contracts.EventData;
import com.microsoft.band.device.StartStrip;
import com.microsoft.exceptions.TileNotAvailableException;
import com.microsoft.kapp.CargoExtensions;
import com.microsoft.kapp.DeviceErrorState;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.settings.BikeSettingsActivity;
import com.microsoft.kapp.activities.settings.FinanceSettingsActivity;
import com.microsoft.kapp.activities.settings.RunSettingsActivity;
import com.microsoft.kapp.activities.settings.StarbucksStrappSettingsActivity;
import com.microsoft.kapp.activities.settings.StrappReorderActivity;
import com.microsoft.kapp.activities.settings.StrappSettingsActivity;
import com.microsoft.kapp.adapters.StrappStateAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.DefaultStrappManager;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.models.strapp.StrappDefinition;
import com.microsoft.kapp.models.strapp.StrappState;
import com.microsoft.kapp.models.strapp.StrappStateCollection;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.kapp.tasks.PingDeviceTask;
import com.microsoft.kapp.tasks.StrappsRetrieveTask;
import com.microsoft.kapp.tasks.StrappsSaveTask;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.ColorUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.DialogManager;
import com.microsoft.kapp.utils.EnvironmentUtils;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.kapp.utils.SyncUtils;
import com.microsoft.kapp.utils.ToastUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.PicassoImageView;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.kapp.widgets.Interstitial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class ManageTilesFragment extends BaseFragment implements StrappSettingsManager.CommitCallback {
    private static final int DEVICE_CONNECTED = 101;
    private static final int DEVICE_NOT_CONNECTED = 102;
    private static final int ERROR_CONNECTING_TO_DEVICE = 106;
    private static final int ERROR_ON_DEVICE = 105;
    private static final int OFF = 0;
    private static final int ON = 1;
    private static final int SETTINGS_PUSHED_ON_DEVICE = 104;
    private static final int START = 100;
    private static final int STRAPPS_PUSHED_ON_DEVICE = 103;
    public static final String STRAPP_DATA = "strapp_data";
    @Inject
    Context mAppContext;
    private ExpandableListView mAppListView;
    private boolean mAreStrappChangesAllowed;
    private TextView mChooseStrappsTextView;
    private ConfirmationBar mConfirmationBar;
    @Inject
    DefaultStrappManager mDefaultStrappManager;
    private StrappStateCollection mEnabledStrapps;
    private Button mGalleryButton;
    private TextView mGalleryText;
    private Interstitial mInterstitial;
    private boolean mIsSaving;
    private int mMaxEnabledStrapps;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    private StrappStateCollection mSavedStrapps;
    @Inject
    SettingsProvider mSettingsProvider;
    private StrappStateAdapter mStrappInfoAdapter;
    @Inject
    StrappSettingsManager mStrappSettingsManager;
    private StrappStateCollection mStrapps;
    private EventData mSyncTimedEvent;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum State {
        LOADING,
        ERROR,
        READY,
        SAVING,
        INITIAL_LOADING_ERROR
    }

    /* loaded from: classes.dex */
    private class RetrieveTask extends StrappsRetrieveTask {
        public RetrieveTask() {
            super(ManageTilesFragment.this.mCargoConnection, ManageTilesFragment.this, ManageTilesFragment.this.mSettingsProvider, ManageTilesFragment.this.mStrappSettingsManager);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.StrappsTask
        public void onExecuteSucceeded(StartStrip result) {
            if (ManageTilesFragment.this.isAdded()) {
                checkIfContainsNotificationStrapps(result);
                StrappStateCollection defaultStrapps = StrappUtils.createStrappStateCollection(ManageTilesFragment.this.mDefaultStrappManager.getStrappDefinitions(), false);
                StrappStateCollection collection = StrappUtils.createRetrievedStrappStateCollection(result, defaultStrapps);
                ManageTilesFragment.this.mStrappSettingsManager.setRetrievedStrapps(collection);
                ManageTilesFragment.this.mStrappSettingsManager.setBandVersion(getBandVersion());
                DeviceTheme deviceTheme = ManageTilesFragment.this.mPersonalizationManagerFactory.getPersonalizationManager(ManageTilesFragment.this.mStrappSettingsManager.getBandVersion()).getThemeById(ManageTilesFragment.this.mSettingsProvider.getCurrentWallpaperId());
                int themeColor = ColorUtils.lowerLimitBlack(deviceTheme.getBase(), ManageTilesFragment.this.getResources().getColor(R.color.greyHigh));
                ((PicassoImageView) ViewUtils.getValidView(ManageTilesFragment.this.getView(), R.id.strapps_image, PicassoImageView.class)).setBackgroundColor(themeColor);
                ((RelativeLayout) ViewUtils.getValidView(ManageTilesFragment.this.getView(), R.id.strapps_image_tile, RelativeLayout.class)).setBackgroundColor(themeColor);
                if (ManageTilesFragment.this.mMaxEnabledStrapps != ManageTilesFragment.this.mSettingsProvider.getNumberOfAllowedStrapps()) {
                    ManageTilesFragment.this.mMaxEnabledStrapps = ManageTilesFragment.this.mSettingsProvider.getNumberOfAllowedStrapps();
                    ManageTilesFragment.this.mChooseStrappsTextView.setText(ManageTilesFragment.this.getActivity().getString(R.string.strapp_choose_strapps_message, new Object[]{Integer.valueOf(ManageTilesFragment.this.mMaxEnabledStrapps - ManageTilesFragment.this.mEnabledStrapps.size())}));
                }
                if (!ManageTilesFragment.this.initializeStrapps()) {
                    ManageTilesFragment.this.switchPanel(State.INITIAL_LOADING_ERROR);
                }
            }
        }

        private void checkIfContainsNotificationStrapps(StartStrip result) {
            if ((result.contains(DefaultStrappUUID.STRAPP_EMAIL) || result.contains(DefaultStrappUUID.STRAPP_FACEBOOK) || result.contains(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER) || result.contains(DefaultStrappUUID.STRAPP_TWITTER) || result.contains(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER)) && CommonUtils.areNotificationsSupported() && !CommonUtils.areNotificationsEnabled(ManageTilesFragment.this.getActivity())) {
                ManageTilesFragment.this.showEnableNotificationsDialog(Html.fromHtml(ManageTilesFragment.this.getString(R.string.notifications_disabled_on_phone_text)));
            }
        }

        @Override // com.microsoft.kapp.tasks.StrappsTask
        protected void onExecuteFailed(Exception exception) {
            DeviceErrorState response = null;
            if (exception instanceof CargoExtensions.SingleDeviceCheckFailedException) {
                response = ((CargoExtensions.SingleDeviceCheckFailedException) exception).getDeviceErrorState();
            }
            ManageTilesFragment.this.switchPanel(State.INITIAL_LOADING_ERROR, response);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class SaveTask extends StrappsSaveTask {
        public SaveTask(OnTaskListener onTaskListener) {
            super(ManageTilesFragment.this.mCargoConnection, ManageTilesFragment.this.mEnabledStrapps, ManageTilesFragment.this.mSettingsProvider, ManageTilesFragment.this.mPersonalizationManagerFactory, ManageTilesFragment.this.mStrappSettingsManager, ManageTilesFragment.this.getActivity().getApplicationContext(), onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPreExecute() {
            ManageTilesFragment.this.mIsSaving = true;
            ManageTilesFragment.this.disableStrappChange(false);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.StrappsTask
        public void onExecuteSucceeded(Void result) {
            ManageTilesFragment.this.mIsSaving = false;
            ManageTilesFragment.this.mStrappSettingsManager.completeTransactionStrapps(ManageTilesFragment.this.mEnabledStrapps);
            ManageTilesFragment.this.initializeStrapps();
            ManageTilesFragment.this.saveToDeviceProgress(103);
        }

        @Override // com.microsoft.kapp.tasks.StrappsTask
        protected void onExecuteFailed(Exception exception) {
            ManageTilesFragment.this.mIsSaving = false;
            if (exception instanceof TileNotAvailableException) {
                ManageTilesFragment.this.saveToDeviceProgress(105);
            } else {
                ManageTilesFragment.this.saveToDeviceProgress(106);
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mStrappSettingsManager, "mStrappSettingsManager");
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        setTopMenuBarColor(getResources().getColor(R.color.settingsMenuBar));
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage("Settings/Band/Manage Tiles");
        if (this.mStrappSettingsManager.isTransactionPending()) {
            if (!this.mIsSaving) {
                this.mConfirmationBar.setVisibility(0);
            }
            if (this.mStrappSettingsManager.getTransactionStrapps() != null) {
                this.mEnabledStrapps = this.mStrappSettingsManager.getTransactionStrapps();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.fragments.BaseFragment
    public void load() {
        this.mInterstitial.setSlide(Interstitial.SLIDE_GETTING_BAND_INFO);
        new RetrieveTask().execute(new Void[0]);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnBackButtonPressedListener
    public boolean handleBackButton() {
        if (isAdded() && this.mStrappSettingsManager.isTransactionPending()) {
            showPendingChangesDialog(true, null, false);
            return true;
        }
        clearRetrievedStrappsFromTransactionManager();
        return false;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    protected boolean doForceRefresh() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnNavigateToFragmentListener
    public boolean handleNavigateToFragment(Class fragmentClass, boolean addToBackStack, boolean shouldToggleSlidingMenu) {
        if (fragmentClass.equals(ManageTilesFragment.class)) {
            return false;
        }
        if (isAdded() && this.mStrappSettingsManager.isTransactionPending()) {
            showPendingChangesDialog(false, fragmentClass, addToBackStack);
            return true;
        }
        clearRetrievedStrappsFromTransactionManager();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearRetrievedStrappsFromTransactionManager() {
        this.mStrappSettingsManager.clearRetrievedStrapps();
    }

    private void showPendingChangesDialog(final boolean exitOnSaveSuccess, final Class fragmentClass, final boolean addToBackStack) {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.strapps_lose_changes_confirmation_title), Integer.valueOf((int) R.string.strapps_lose_changes_confirmation_message), R.string.strapps_button_continue, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ManageTilesFragment.this.mStrappSettingsManager.clearTransaction();
                ManageTilesFragment.this.clearRetrievedStrappsFromTransactionManager();
                ManageTilesFragment.this.performNavigateAway(exitOnSaveSuccess, fragmentClass, addToBackStack);
            }
        }, R.string.strapps_button_cancel, null, DialogPriority.HIGH);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View.OnClickListener reorderClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (ManageTilesFragment.this.mAreStrappChangesAllowed) {
                    ManageTilesFragment.this.startStrappReorderActivity();
                }
            }
        };
        View header = inflater.inflate(R.layout.manage_tiles_header, (ViewGroup) null);
        View footer = inflater.inflate(R.layout.manage_tiles_footer, (ViewGroup) null);
        FrameLayout strappsImage = (FrameLayout) ViewUtils.getValidView(header, R.id.strapps_image_layout, FrameLayout.class);
        strappsImage.setOnClickListener(reorderClickListener);
        ViewGroup strappsReorderText = (ViewGroup) ViewUtils.getValidView(header, R.id.strapps_reorder_layout, ViewGroup.class);
        strappsReorderText.setOnClickListener(reorderClickListener);
        this.mGalleryText = (TextView) ViewUtils.getValidView(footer, R.id.tiles_gallery_text, TextView.class);
        this.mGalleryButton = (Button) ViewUtils.getValidView(footer, R.id.tiles_gallery_button, Button.class);
        this.mGalleryButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String url = EnvironmentUtils.getTileGalleryUrl();
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                ManageTilesFragment.this.startActivity(intent);
            }
        });
        View view = inflater.inflate(R.layout.fragment_connected_apps, container, false);
        this.mAppListView = (ExpandableListView) ViewUtils.getValidView(view, R.id.app_list_view, ExpandableListView.class);
        this.mAppListView.addHeaderView(header, null, false);
        this.mAppListView.addFooterView(footer, null, false);
        this.mAppListView.setRecyclerListener(new AbsListView.RecyclerListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.4
            @Override // android.widget.AbsListView.RecyclerListener
            public void onMovedToScrapHeap(View view1) {
                ImageView imageView = (ImageView) view1.findViewById(R.id.manage_strapps_tile_icon);
                if (imageView != null) {
                    imageView.setImageBitmap(null);
                }
            }
        });
        this.mSavedStrapps = new StrappStateCollection();
        this.mEnabledStrapps = new StrappStateCollection();
        this.mMaxEnabledStrapps = this.mSettingsProvider.getNumberOfAllowedStrapps();
        this.mAppListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.5
            @Override // android.widget.ExpandableListView.OnChildClickListener
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                StrappState strappState = (StrappState) ManageTilesFragment.this.mStrappInfoAdapter.getChild(groupPosition, childPosition);
                UUID strappId = strappState.getDefinition().getStrappId();
                if (strappState.isEnabled() && StrappUtils.isSettingsEnabledStrapp(strappId)) {
                    ManageTilesFragment.this.launchStrappSettings(strappId);
                    return true;
                }
                return true;
            }
        });
        this.mInterstitial = (Interstitial) ViewUtils.getValidView(view, R.id.manage_strapps_interstitial, Interstitial.class);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(view, R.id.confirmation_bar, ConfirmationBar.class);
        this.mChooseStrappsTextView = (TextView) ViewUtils.getValidView(view, R.id.strapp_choose_strapps_message, TextView.class);
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!ManageTilesFragment.this.areThirdPartyStrappsDisabled(ManageTilesFragment.this.mStrappSettingsManager.getTransactionStrapps(), ManageTilesFragment.this.mStrappSettingsManager.getRetrievedStrapps())) {
                    ManageTilesFragment.this.saveToDeviceProgress(100);
                } else {
                    ManageTilesFragment.this.getDialogManager().showDialog(ManageTilesFragment.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.strapps_third_party_confirmation_message), R.string.ok, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.6.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            ManageTilesFragment.this.saveToDeviceProgress(100);
                        }
                    }, R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.6.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            ManageTilesFragment.this.cancelTransaction();
                        }
                    }, DialogPriority.HIGH);
                }
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ManageTilesFragment.this.cancelTransaction();
            }
        });
        return view;
    }

    protected void cancelTransaction() {
        this.mStrappSettingsManager.clearTransaction();
        initializeStrapps();
        handleConfirmationBar(0);
    }

    protected boolean areThirdPartyStrappsDisabled(StrappStateCollection currentStrapps, StrappStateCollection originalStrapps) {
        boolean disabled = false;
        if (currentStrapps != null && originalStrapps != null) {
            for (Map.Entry<UUID, StrappState> entry : originalStrapps.entrySet()) {
                if (entry.getValue().getDefinition().isThirdPartyStrapp() && !currentStrapps.containsKey(entry.getKey())) {
                    disabled = true;
                }
            }
        }
        return disabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchStrappSettings(UUID strappId) {
        Intent notificationSettingsIntent;
        if (strappId != null) {
            if (strappId.equals(DefaultStrappUUID.STRAPP_RUN)) {
                notificationSettingsIntent = new Intent(getActivity(), RunSettingsActivity.class);
            } else if (strappId.equals(DefaultStrappUUID.STRAPP_BIKE)) {
                notificationSettingsIntent = new Intent(getActivity(), BikeSettingsActivity.class);
            } else if (strappId.equals(DefaultStrappUUID.STRAPP_BING_FINANCE)) {
                notificationSettingsIntent = new Intent(getActivity(), FinanceSettingsActivity.class);
            } else if (strappId.equals(DefaultStrappUUID.STRAPP_STARBUCKS)) {
                notificationSettingsIntent = new Intent(getActivity(), StarbucksStrappSettingsActivity.class);
            } else {
                notificationSettingsIntent = new Intent(getActivity(), StrappSettingsActivity.class);
            }
            notificationSettingsIntent.putExtra("uuid_strappId", strappId);
            startActivity(notificationSettingsIntent);
        }
    }

    public void startStrappReorderActivity() {
        if (isAdded()) {
            Intent intent = new Intent(getActivity(), StrappReorderActivity.class);
            getActivity().startActivity(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchPanel(State state) {
        switchPanel(state, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchPanel(State state, DeviceErrorState response) {
        Validate.notNull(state, "state");
        Activity activity = getActivity();
        if (state == State.ERROR || (state == State.INITIAL_LOADING_ERROR && this.mStrappSettingsManager.isTransactionPending())) {
            if (activity != null && Validate.isActivityAlive(activity)) {
                if (state == State.INITIAL_LOADING_ERROR) {
                    this.mInterstitial.setVisibility(8);
                }
                DialogManager.showDeviceErrorDialog(activity);
            }
        } else if (state == State.INITIAL_LOADING_ERROR) {
            if (activity != null && Validate.isActivityAlive(activity)) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityUtils.performBackButton(ManageTilesFragment.this.getActivity());
                    }
                };
                if (response == DeviceErrorState.MULTIPLE_DEVICES_BONDED) {
                    getDialogManager().showMultipleDevicesConnectedError(activity, listener);
                } else {
                    getDialogManager().showDialog(activity, Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf(response != null ? CommonUtils.getResIdForDeviceErrorState(response).intValue() : R.string.error_manage_strapps_loading_error), listener, DialogPriority.HIGH);
                }
            }
        } else if (state != State.LOADING && state != State.SAVING) {
            this.mInterstitial.setVisibility(8);
        } else {
            this.mInterstitial.setSlide(state == State.LOADING ? Interstitial.SLIDE_GETTING_BAND_INFO : Interstitial.SLIDE_UPDATING_BAND_INFO);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean initializeStrapps() {
        StrappStateCollection transactionStrapps = this.mStrappSettingsManager.getTransactionStrapps();
        StrappStateCollection retrievedStrapps = this.mStrappSettingsManager.getRetrievedStrapps();
        populateStrappList(retrievedStrapps);
        if (transactionStrapps != null) {
            populateDataLocally(transactionStrapps.entrySet());
        } else if (retrievedStrapps != null) {
            StrappStateCollection mergedCollection = this.mDefaultStrappManager.applyAppConfigChanges(retrievedStrapps);
            populateDataLocally(mergedCollection.entrySet());
        } else {
            KLog.e(this.TAG, "Error retrieving strapps from StrappSettingsManager.");
            return false;
        }
        enableStrappChange();
        if (isAdded() && this.mMaxEnabledStrapps > 0) {
            this.mChooseStrappsTextView.setVisibility(0);
            this.mChooseStrappsTextView.setText(getActivity().getString(R.string.strapp_choose_strapps_message, new Object[]{Integer.valueOf(this.mMaxEnabledStrapps - this.mEnabledStrapps.size())}));
        }
        return true;
    }

    public void populateDataLocally(Set<Map.Entry<UUID, StrappState>> strappList) {
        for (StrappState strapp : this.mStrapps.values()) {
            strapp.setIsEnabled(false);
        }
        this.mSavedStrapps.clear();
        this.mEnabledStrapps.clear();
        for (Map.Entry<UUID, StrappState> strapp2 : strappList) {
            StrappState strappState = this.mStrapps.get(strapp2.getKey());
            if (strappState != null) {
                this.mSavedStrapps.put(strappState);
                this.mEnabledStrapps.put(strappState);
                this.mStrapps.get(strapp2.getKey()).setIsEnabled(true);
            }
        }
    }

    private void enableStrappChange() {
        this.mAreStrappChangesAllowed = true;
        this.mIsSaving = false;
        for (StrappState strapp : this.mStrapps.values()) {
            strapp.setIsStateChangeAllowed(true);
        }
        this.mStrappInfoAdapter.notifyDataSetChanged();
        switchPanel(State.READY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableStrappChange(boolean isLoading) {
        this.mAreStrappChangesAllowed = false;
        switchPanel(isLoading ? State.LOADING : State.SAVING);
        for (StrappState strapp : this.mStrapps.values()) {
            strapp.setIsStateChangeAllowed(false);
        }
        this.mStrappInfoAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConfirmationBar(int offOrOn) {
        switch (offOrOn) {
            case 0:
                this.mConfirmationBar.setVisibility(8);
                return;
            case 1:
                this.mConfirmationBar.setVisibility(0);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void saveToDeviceProgress(int state) {
        switch (state) {
            case 100:
                isDeviceConnected();
                disableStrappChange(false);
                handleConfirmationBar(0);
                this.mSyncTimedEvent = Telemetry.startTimedEvent(TelemetryConstants.TimedEvents.Sync.EVENT_NAME);
                return;
            case 101:
                if (this.mStrappSettingsManager.isTransactionStrappsPending()) {
                    new SaveTask(this).execute(new Void[0]);
                    return;
                }
                StrappUtils.clearStrappAndCalendarCacheData(this.mSettingsProvider, this.mCargoConnection);
                saveToDeviceProgress(103);
                return;
            case 102:
                getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.strapps_pop_up_dialog_title), Integer.valueOf((int) R.string.strapps_pop_up_dialog_body), DialogPriority.HIGH);
                enableStrappChange();
                handleConfirmationBar(1);
                logTelemetryEvent(false);
                return;
            case 103:
                this.mStrappSettingsManager.clearTransactionStrapps();
                if (this.mStrappSettingsManager.isTransactionSettingsPending()) {
                    this.mStrappSettingsManager.commitTransactionSettingsAsync(this, this.mSavedStrapps);
                    return;
                }
                break;
            case 104:
                break;
            case 105:
                getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.strapps_pop_up_dialog_title), Integer.valueOf((int) R.string.strapps_error_on_band_body), DialogPriority.HIGH);
                enableStrappChange();
                handleConfirmationBar(1);
                logTelemetryEvent(false);
                return;
            case 106:
                getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.strapps_pop_up_dialog_title), Integer.valueOf((int) R.string.strapps_pop_up_dialog_body), DialogPriority.HIGH);
                enableStrappChange();
                handleConfirmationBar(1);
                logTelemetryEvent(false);
                return;
            default:
                return;
        }
        this.mStrappSettingsManager.clearTransactionSettings();
        enableStrappChange();
        handleConfirmationBar(0);
        SyncUtils.startStrappsDataSync(this.mAppContext);
        this.mStrappSettingsManager.setRetrievedStrapps(this.mSavedStrapps);
        logTelemetryEvent(true);
    }

    private void logTelemetryEvent(boolean success) {
        if (this.mSyncTimedEvent != null) {
            this.mSyncTimedEvent.getProperties().put("Status", success ? "Success" : "Failure");
            Telemetry.endTimedEvent(this.mSyncTimedEvent);
            this.mSyncTimedEvent = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performNavigateAway(boolean exitBackPressed, Class navigateFragmentClass, boolean navigateAddToBackStack) {
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            if (exitBackPressed) {
                ActivityUtils.performBackButton(activity);
                return;
            } else if (navigateFragmentClass != null && (activity instanceof FragmentNavigationCommandV1.FragmentNavigationListener)) {
                ((FragmentNavigationCommandV1.FragmentNavigationListener) activity).navigateToFragment(navigateFragmentClass, null, navigateAddToBackStack, false);
                return;
            } else {
                return;
            }
        }
        KLog.w(this.TAG, "Activity is no longer alive.");
    }

    @Override // com.microsoft.kapp.strappsettings.StrappSettingsManager.CommitCallback
    public void onStrappSettingsManagerCommitSuccess() {
        saveToDeviceProgress(104);
    }

    @Override // com.microsoft.kapp.strappsettings.StrappSettingsManager.CommitCallback
    public void onStrappSettingsManagerCommitFail() {
        saveToDeviceProgress(105);
    }

    private void isDeviceConnected() {
        PingDeviceTask pingDeviceTask = new PingDeviceTask(this.mCargoConnection, this) { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.9
            @Override // com.microsoft.kapp.tasks.PingDeviceTask
            protected void onExecuteSucceeded(Boolean isDeviceConnected) {
                if (isDeviceConnected.booleanValue()) {
                    ManageTilesFragment.this.saveToDeviceProgress(101);
                } else {
                    ManageTilesFragment.this.saveToDeviceProgress(102);
                }
            }

            @Override // com.microsoft.kapp.tasks.PingDeviceTask
            protected void onExecuteFailed(Exception exception) {
                ManageTilesFragment.this.saveToDeviceProgress(102);
                KLog.d(ManageTilesFragment.this.TAG, "Managing Strapps: pinging the device failed with the following exception:", exception);
            }
        };
        pingDeviceTask.execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEnableNotificationsDialog(CharSequence dialogMessage) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Constants.INTENT_ANDROID_NOTIFICATION_LISTENER);
                ManageTilesFragment.this.startActivity(intent);
            }
        };
        getDialogManager().showDialog(getActivity(), Html.fromHtml(getString(R.string.notifications_disabled_on_phone_header)), dialogMessage, getString(R.string.ok), listener, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
    }

    private void populateStrappList(StrappStateCollection collection) {
        Collection<StrappDefinition> strapDefinitions = this.mDefaultStrappManager.getStrappDefinitions();
        StrappStateCollection configChangeCollection = this.mDefaultStrappManager.applyAppConfigChanges(collection);
        this.mStrapps = StrappUtils.createStrappStateCollection(this.mDefaultStrappManager.getStrappDefinitions(), false);
        this.mStrapps = this.mStrapps.addAll(configChangeCollection);
        ArrayList<StrappState> orderedList = new ArrayList<>();
        ArrayList<StrappState> orderedThirdPartyList = new ArrayList<>();
        StrappUtils.createOrderedLists(this.mStrapps, orderedList, orderedThirdPartyList, strapDefinitions);
        if (orderedThirdPartyList.size() == 0) {
            this.mGalleryText.setText(getResources().getString(R.string.strapps_gallery_text_no_developer_tiles));
        } else {
            this.mGalleryText.setText(getResources().getString(R.string.strapps_gallery_text));
        }
        this.mStrappInfoAdapter = new StrappStateAdapter(getActivity(), orderedList, orderedThirdPartyList);
        this.mStrappInfoAdapter.setOnSwitchClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ManageTilesFragment.this.mStrappInfoAdapter.notifyDataSetChanged();
                StrappState currentStrapp = ManageTilesFragment.this.mStrapps.get(v.getTag());
                if (currentStrapp == null) {
                    ManageTilesFragment.this.switchPanel(State.ERROR);
                    return;
                }
                UUID strappId = currentStrapp.getDefinition().getStrappId();
                boolean isActive = currentStrapp.isEnabled();
                ManageTilesFragment.this.mDefaultStrappManager.applyAppConfigChanges(ManageTilesFragment.this.mEnabledStrapps);
                if (isActive && ManageTilesFragment.this.mEnabledStrapps.size() < ManageTilesFragment.this.mMaxEnabledStrapps) {
                    if (checkStrappForNotificationRequirement(currentStrapp) && ManageTilesFragment.this.checkStrappForLocationRequirement(currentStrapp)) {
                        ManageTilesFragment.this.mEnabledStrapps.put(strappId, currentStrapp);
                        ManageTilesFragment.this.mStrappSettingsManager.enableTransactionStrapp(strappId, currentStrapp);
                        if (ManageTilesFragment.this.mStrappSettingsManager.isNotificationStatusManagedTile(strappId)) {
                            ManageTilesFragment.this.mStrappSettingsManager.setTransactionNotificationsEnabled(strappId, true);
                        }
                        ManageTilesFragment.this.handleConfirmationBar(1);
                        if (ManageTilesFragment.this.mMaxEnabledStrapps > 0) {
                            ManageTilesFragment.this.mChooseStrappsTextView.setVisibility(0);
                            ManageTilesFragment.this.mChooseStrappsTextView.setText(ManageTilesFragment.this.getActivity().getString(R.string.strapp_choose_strapps_message, new Object[]{Integer.valueOf(ManageTilesFragment.this.mMaxEnabledStrapps - ManageTilesFragment.this.mEnabledStrapps.size())}));
                            return;
                        }
                        return;
                    }
                    currentStrapp.setIsEnabled(false);
                    ((Switch) v).setChecked(false);
                } else if (!isActive && ManageTilesFragment.this.mEnabledStrapps.size() > 0) {
                    ManageTilesFragment.this.mEnabledStrapps.remove(strappId);
                    ManageTilesFragment.this.mStrappSettingsManager.disableTransactionStrapp(strappId);
                    ManageTilesFragment.this.handleConfirmationBar(1);
                    ManageTilesFragment.this.mChooseStrappsTextView.setVisibility(0);
                    ManageTilesFragment.this.mChooseStrappsTextView.setText(ManageTilesFragment.this.getActivity().getString(R.string.strapp_choose_strapps_message, new Object[]{Integer.valueOf(ManageTilesFragment.this.mMaxEnabledStrapps - ManageTilesFragment.this.mEnabledStrapps.size())}));
                } else {
                    currentStrapp.setIsEnabled(!isActive);
                    ((Switch) v).setChecked(isActive ? false : true);
                    ToastUtils.showToast(ManageTilesFragment.this.getActivity(), isActive ? R.string.exceed_maximum_allowed_strapps_toast : R.string.exceed_minimum_allowed_strapps_toast, 2);
                }
            }

            private boolean checkStrappForNotificationRequirement(StrappState currentStrapp) {
                UUID uuid = currentStrapp.getDefinition().getStrappId();
                return !(DefaultStrappUUID.STRAPP_EMAIL.equals(uuid) || DefaultStrappUUID.STRAPP_TWITTER.equals(uuid) || DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER.equals(uuid) || DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER.equals(uuid) || DefaultStrappUUID.STRAPP_FACEBOOK.equals(uuid)) || checkForNotificationRequirementsMet();
            }

            private boolean checkForNotificationRequirementsMet() {
                if (CommonUtils.areNotificationsEnabled(ManageTilesFragment.this.getActivity())) {
                    return true;
                }
                if (Build.VERSION.SDK_INT >= 18) {
                    ManageTilesFragment.this.showEnableNotificationsDialog(Html.fromHtml(ManageTilesFragment.this.getString(R.string.notifications_disabled_on_phone_strapp_enabled_text)));
                    return false;
                }
                ManageTilesFragment.this.getDialogManager().showDialog(ManageTilesFragment.this.getActivity(), Integer.valueOf((int) R.string.notifications_disabled_on_phone_header), Integer.valueOf((int) R.string.notifications_not_supported_on_phone_text), DialogPriority.LOW);
                return false;
            }
        });
        this.mAppListView.setAdapter(this.mStrappInfoAdapter);
    }

    protected boolean checkStrappForLocationRequirement(StrappState currentStrapp) {
        UUID uuid = currentStrapp.getDefinition().getStrappId();
        if (!DefaultStrappUUID.STRAPP_BING_WEATHER.equals(uuid) || CommonUtils.isLocationEnabled(getActivity())) {
            return true;
        }
        showEnableLocationDialog();
        return false;
    }

    private void showEnableLocationDialog() {
        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.ManageTilesFragment.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
                ManageTilesFragment.this.startActivity(intent);
            }
        };
        getDialogManager().showDialog(getActivity(), Html.fromHtml(getString(R.string.location_disabled_on_phone_header_tiles)), Html.fromHtml(getString(R.string.location_disabled_on_phone_text_tiles)), getString(R.string.ok), okListener, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
    }
}
