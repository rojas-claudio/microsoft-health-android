package com.microsoft.kapp.fragments;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.CargoKit;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.BluetoothAdapterHelper;
import com.microsoft.band.util.bluetooth.BluetoothAdapterObserver;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.activities.DeviceConnectActivity;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.adapters.OobeDeviceListAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.LinkedHashMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class OobeBluetoothConnectionFragment extends BaseFragment {
    private static final int MAXIMUM_NUMBER_OF_RETRIES = 3;
    private static final int NO_SELECTION = -1;
    public static final ParcelUuid PARCEL_UUID_CARGO_BLUETOOTH_PROTOCOL = ParcelUuid.fromString(BandDeviceConstants.GUID_CARGO_BLUETOOTH_PROTOCOL);
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int SEARCHING_HELP_TIMEOUT_MILLIS = 30000;
    private TextView mCancelButton;
    private View mDeviceConnectionLayout;
    private View mDeviceImageLayout;
    private ListView mDeviceList;
    private OobeDeviceListAdapter mDeviceListAdapter;
    private TextView mHelpText;
    private TextView mHelpTextChevron;
    private View mHelpTextLayout;
    private Interstitial mInterstitial;
    private ProgressBar mLoadingProgressBar;
    private ImageView mNoDevicesFound;
    private Button mPairButton;
    private View mSearchingLayout;
    @Inject
    SensorUtils mSensorUtils;
    @Inject
    SettingsProvider mSettingsProvider;
    private TextView mSubtitle;
    private DeviceConnectionTimeoutTask mTimeoutTask;
    private TextView mTitle;
    private LoadingState mCurrentState = LoadingState.IDLE;
    private boolean mIsOnLandingScreen = true;
    private LinkedHashMap<String, BluetoothDevice> mDevicesArrayList = new LinkedHashMap<>();
    public final BroadcastReceiver mReceiver = new BluetoothEventsBroadcastReceiver();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum LoadingState {
        SEARCHING,
        CONNECTING,
        IDLE,
        ERROR
    }

    /* loaded from: classes.dex */
    public interface OnPairingCompleteListener {
        void onPairingComplete();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_device_connect, container, false);
        this.mTitle = (TextView) ViewUtils.getValidView(view, R.id.oobe_title, TextView.class);
        this.mTitle.setText(R.string.pair_band_landing_title);
        this.mSubtitle = (TextView) ViewUtils.getValidView(view, R.id.oobe_subtitle, TextView.class);
        this.mSubtitle.setText(R.string.pair_band_landing_subtitle);
        this.mPairButton = (Button) ViewUtils.getValidView(view, R.id.oobe_confirm, Button.class);
        this.mPairButton.setVisibility(8);
        this.mPairButton.setBackground(getResources().getDrawable(R.drawable.button_oobe_grey));
        this.mPairButton.setTextColor(getResources().getColor(R.color.GreyColor));
        this.mCancelButton = (TextView) ViewUtils.getValidView(view, R.id.oobe_cancel, TextView.class);
        this.mCancelButton.setVisibility(0);
        this.mDeviceList = (ListView) ViewUtils.getValidView(view, R.id.found_devices, ListView.class);
        this.mInterstitial = (Interstitial) ViewUtils.getValidView(view, R.id.interstitial, Interstitial.class);
        this.mDeviceListAdapter = new OobeDeviceListAdapter(getActivity(), this.mDevicesArrayList);
        this.mLoadingProgressBar = (ProgressBar) ViewUtils.getValidView(view, R.id.loading_progress_bar, ProgressBar.class);
        this.mNoDevicesFound = (ImageView) ViewUtils.getValidView(view, R.id.oobe_no_devices_found, ImageView.class);
        this.mSearchingLayout = (View) ViewUtils.getValidView(view, R.id.searching_gizmo, View.class);
        this.mDeviceList.setEmptyView(this.mSearchingLayout);
        this.mDeviceList.setAdapter((ListAdapter) this.mDeviceListAdapter);
        this.mDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (OobeBluetoothConnectionFragment.this.mCurrentState != LoadingState.CONNECTING) {
                    OobeBluetoothConnectionFragment.this.setIndicatorState(LoadingState.CONNECTING);
                    OobeBluetoothConnectionFragment.this.mDeviceListAdapter.setPairingItem(position);
                    OobeBluetoothConnectionFragment.this.mDeviceListAdapter.notifyDataSetChanged();
                    BluetoothDevice device = (BluetoothDevice) OobeBluetoothConnectionFragment.this.mDevicesArrayList.values().toArray()[position];
                    BluetoothAdapterHelper.cancelDiscovery();
                    OobeBluetoothConnectionFragment.this.mTimeoutTask = new DeviceConnectionTimeoutTask(OobeBluetoothConnectionFragment.this);
                    OobeBluetoothConnectionFragment.this.mTimeoutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    if (device.getBondState() == 12) {
                        OobeBluetoothConnectionFragment.this.startConnectingWithDevice(device, 3);
                    } else {
                        new PairDeviceTask(OobeBluetoothConnectionFragment.this, device).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    }
                }
            }
        });
        this.mDeviceImageLayout = (View) ViewUtils.getValidView(view, R.id.device_representation_layout, View.class);
        this.mDeviceConnectionLayout = (View) ViewUtils.getValidView(view, R.id.device_connection_layout, View.class);
        ((View) ViewUtils.getValidView(view, R.id.cargo_representation, View.class)).setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OobeBluetoothConnectionFragment.this.startSearchingForDevices();
                OobeBluetoothConnectionFragment.this.mHelpTextLayout.setVisibility(8);
            }
        });
        ((View) ViewUtils.getValidView(view, R.id.neon_representation, View.class)).setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OobeBluetoothConnectionFragment.this.startSearchingForDevices();
            }
        });
        this.mHelpTextLayout = (View) ViewUtils.getValidView(view, R.id.help_text_layout, View.class);
        this.mHelpText = (TextView) ViewUtils.getValidView(view, R.id.help_text_textview, TextView.class);
        this.mHelpTextChevron = (TextView) ViewUtils.getValidView(view, R.id.oobe_pairing_help_text_chevron, TextView.class);
        this.mHelpText.setText(R.string.pair_band_learn_more);
        this.mHelpTextLayout.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(Constants.BAND_HOME_URL));
                OobeBluetoothConnectionFragment.this.startActivity(intent);
            }
        });
        this.mCancelButton.setVisibility(0);
        if (userCanConnectWithPhone()) {
            this.mCancelButton.setText(R.string.oobe_no_band_button);
        } else {
            this.mCancelButton.setText(R.string.cancel);
        }
        this.mCancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (OobeBluetoothConnectionFragment.this.userCanConnectWithPhone()) {
                    OobeBluetoothConnectionFragment.this.mSettingsProvider.setFreStatus(FreStatus.DEVICE_CONNECTION_SKIPPED);
                    FreUtils.devicePairingRedirect(OobeBluetoothConnectionFragment.this.getActivity(), OobeBluetoothConnectionFragment.this.mSettingsProvider);
                } else if (OobeBluetoothConnectionFragment.this.mSettingsProvider.isDeviceConnectionFlow()) {
                    OobeBluetoothConnectionFragment.this.getDialogManager().showDialog(OobeBluetoothConnectionFragment.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.firmware_update_cancel_dialog_text), R.string.yes, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.5.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            DeviceConnectActivity activity = (DeviceConnectActivity) OobeBluetoothConnectionFragment.this.getActivity();
                            if (activity != null) {
                                activity.onBackPressed();
                            }
                        }
                    }, R.string.no, null, DialogPriority.LOW);
                } else {
                    DeviceConnectActivity activity = (DeviceConnectActivity) OobeBluetoothConnectionFragment.this.getActivity();
                    if (activity != null) {
                        activity.onBackPressed();
                    }
                }
            }
        });
        this.mPairButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View clickView) {
                if (OobeBluetoothConnectionFragment.this.mCurrentState != LoadingState.CONNECTING && OobeBluetoothConnectionFragment.this.mCurrentState != LoadingState.SEARCHING) {
                    OobeBluetoothConnectionFragment.this.setIndicatorState(LoadingState.SEARCHING);
                    OobeBluetoothConnectionFragment.this.mDeviceListAdapter.setPairingItem(-1);
                    OobeBluetoothConnectionFragment.this.startDeviceDiscovery();
                }
            }
        });
        IntentFilter filter = new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        filter.addAction(CargoConstants.ACTION_PAIRING_REACTION_FINISHED);
        filter.addAction("android.bluetooth.device.action.FOUND");
        getActivity().registerReceiver(this.mReceiver, filter);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean userCanConnectWithPhone() {
        return this.mIsOnLandingScreen && this.mSensorUtils.isKitkatWithStepSensor() && !this.mSettingsProvider.isSensorLoggingEnabled() && !this.mSettingsProvider.isDeviceConnectionFlow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pairDevice(BluetoothDevice bluetoothDevice) {
        CargoKit.pairWithBand(bluetoothDevice);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSearchingForDevices() {
        if (this.mDeviceList.getChildCount() == 0) {
            this.mSearchingLayout.setVisibility(0);
        }
        this.mLoadingProgressBar.setVisibility(0);
        this.mDeviceImageLayout.setVisibility(8);
        this.mIsOnLandingScreen = false;
        this.mDeviceConnectionLayout.setVisibility(0);
        this.mTitle.setText(R.string.pair_band_title);
        this.mSubtitle.setText(R.string.pair_band_subtitle);
        setIndicatorState(LoadingState.SEARCHING);
        startDeviceDiscovery();
        this.mPairButton.setText(R.string.fre_bluetooth_connect_refresh_button);
        this.mPairButton.setVisibility(0);
        this.mHelpText.setText(R.string.pair_band_helper_text);
        this.mHelpTextChevron.setVisibility(8);
        this.mHelpTextLayout.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View layout) {
                Telemetry.logEvent(TelemetryConstants.Events.OobeHavingTroubleClicked.EVENT_NAME);
                OobeBluetoothConnectionFragment.this.displayHelpDialog();
            }
        });
        this.mHelpTextLayout.setClickable(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayHelpDialog() {
        getDialogManager().showDialog(getActivity(), Html.fromHtml(getString(R.string.oobe_bluetooth_error_title)), Html.fromHtml(getString(R.string.oobe_bluetooth_error_device_not_connecting_text)), getString(R.string.oobe_bluetooth_error_device_not_connecting_okay), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.BLUETOOTH_SETTINGS");
                OobeBluetoothConnectionFragment.this.startActivity(intent);
            }
        }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIndicatorState(LoadingState state) {
        if (isAdded() && !this.mIsOnLandingScreen) {
            cancelHelpTimeout();
            this.mInterstitial.setVisibility(8);
            switch (state) {
                case SEARCHING:
                    this.mLoadingProgressBar.setVisibility(0);
                    setConnectingUIState();
                    break;
                case CONNECTING:
                    this.mInterstitial.setVisibility(0);
                    setConnectingUIState();
                    break;
                case ERROR:
                    deviceNotAbleToConnect();
                    setIdleUiState(this.mDeviceList.getChildCount() == 0);
                    break;
                case IDLE:
                    setIdleUiState(this.mDeviceList.getChildCount() == 0);
                    break;
            }
            this.mCurrentState = state;
        }
    }

    private void setIdleUiState(boolean shouldShowError) {
        if (shouldShowError) {
            this.mNoDevicesFound.setVisibility(0);
            this.mSearchingLayout.setVisibility(8);
        }
        this.mPairButton.setVisibility(0);
        this.mPairButton.setText(R.string.fre_bluetooth_connect_refresh_button);
        this.mLoadingProgressBar.setVisibility(8);
        this.mHelpText.setText(R.string.device_pairing_having_trouble_button);
        this.mHelpTextChevron.setVisibility(0);
        this.mHelpTextLayout.setClickable(true);
        this.mHelpTextLayout.setVisibility(0);
    }

    private void setConnectingUIState() {
        this.mCancelButton.setText(R.string.cancel);
        this.mNoDevicesFound.setVisibility(8);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.OOBE_BLUETOOTH_PAIRING);
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(this.mReceiver);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            startDeviceDiscovery();
        } else {
            setIndicatorState(LoadingState.IDLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDeviceDiscovery() {
        if (!CargoKit.isBluetoothEnabled()) {
            Intent enableIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            startActivityForResult(enableIntent, 1);
            return;
        }
        cancelHelpTimeout();
        StartDiscoveryTask timeoutTask = new StartDiscoveryTask(this);
        timeoutTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPairingSuccess() {
        cancelHelpTimeout();
        if (getActivity() instanceof DeviceConnectActivity) {
            ((DeviceConnectActivity) getActivity()).onPairingComplete();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addBluetoothDevice(final BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice.getName() != null) {
            getActivity().runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.9
                @Override // java.lang.Runnable
                public void run() {
                    OobeBluetoothConnectionFragment.this.mDevicesArrayList.put(bluetoothDevice.getAddress(), bluetoothDevice);
                    OobeBluetoothConnectionFragment.this.mDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /* loaded from: classes.dex */
    private final class BluetoothEventsBroadcastReceiver extends BroadcastReceiver {
        private BluetoothEventsBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device;
            boolean isCargoDevice = false;
            try {
                String action = intent.getAction();
                if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                    OobeBluetoothConnectionFragment.this.setIndicatorState(LoadingState.IDLE);
                } else if ("android.bluetooth.device.action.FOUND".equals(action)) {
                    BluetoothDevice device2 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (device2 != null) {
                        BluetoothClass bluetoothClass = device2.getBluetoothClass();
                        if (bluetoothClass != null && bluetoothClass.getDeviceClass() == 1796 && bluetoothClass.hasService(2097152)) {
                            isCargoDevice = true;
                        }
                        if (isCargoDevice) {
                            OobeBluetoothConnectionFragment.this.addBluetoothDevice(device2);
                        }
                    }
                } else if (CargoConstants.ACTION_PAIRING_REACTION_FINISHED.equals(action) && (device = (BluetoothDevice) intent.getParcelableExtra(CargoConstants.EXTRA_REGISTERED_DEVICE)) != null) {
                    OobeBluetoothConnectionFragment.this.startConnectingWithDevice(device, 0);
                }
            } catch (Exception exception) {
                KLog.w(CargoConstants.DISCOVERY_TAG, "Encountered exception receiving a bluetooth event", exception);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startConnectingWithDevice(final BluetoothDevice device, final int retries) {
        KLog.w(this.TAG, "Connecting to Cargo Device: Name = %s, Mac = %s", device.getName(), device.getAddress());
        ConnectToDeviceTask connectToDeviceTask = new ConnectToDeviceTask(this, this.mCargoConnection, this.mSettingsProvider, new Callback<Boolean>() { // from class: com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment.10
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                if (retries < 3) {
                    OobeBluetoothConnectionFragment.this.startConnectingWithDevice(device, retries + 1);
                } else {
                    OobeBluetoothConnectionFragment.this.deviceNotAbleToConnect();
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(Boolean result) {
                if (result.booleanValue()) {
                    OobeBluetoothConnectionFragment.this.notifyPairingSuccess();
                } else {
                    OobeBluetoothConnectionFragment.this.deviceNotInOobe();
                }
            }
        }, device);
        connectToDeviceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class StartDiscoveryTask extends ScopedAsyncTask<Void, Void, Void> {
        public StartDiscoveryTask(OnTaskListener onTaskListener) {
            super(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(Void... params) {
            for (BluetoothDevice bluetoothDevice : BluetoothAdapterHelper.getPairedDevices()) {
                if (CargoKit.isCargoDevice(bluetoothDevice)) {
                    KLog.w(CargoConstants.DISCOVERY_TAG, "Found paired Device: Name = %s, MAc = %s", bluetoothDevice.getName(), bluetoothDevice.getAddress());
                    OobeBluetoothConnectionFragment.this.addBluetoothDevice(bluetoothDevice);
                }
            }
            BluetoothAdapterObserver.startDiscovery();
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void success) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DeviceConnectionTimeoutTask extends ScopedAsyncTask<Void, Void, Void> {
        public DeviceConnectionTimeoutTask(OnTaskListener onTaskListener) {
            super(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(Void... params) {
            try {
                Thread.sleep(30000L);
                return null;
            } catch (InterruptedException e) {
                KLog.d(this.TAG, "Discovery Timeout Interrupted", e);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void success) {
            if (OobeBluetoothConnectionFragment.this.isAdded()) {
                OobeBluetoothConnectionFragment.this.setIndicatorState(LoadingState.ERROR);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ConnectToDeviceTask extends ScopedAsyncTask<Void, Void, Boolean> {
        private Callback<Boolean> mCallback;
        private CargoConnection mCargoConnection;
        private DeviceInfo mDeviceInfo;
        private SettingsProvider mSettingsProvider;

        public ConnectToDeviceTask(OnTaskListener onTaskListener, CargoConnection cargoConnection, SettingsProvider settingsProvider, Callback<Boolean> callback, BluetoothDevice bluetoothDevice) {
            super(onTaskListener);
            this.mCargoConnection = cargoConnection;
            this.mSettingsProvider = settingsProvider;
            this.mCallback = callback;
            this.mDeviceInfo = new DeviceInfo(bluetoothDevice.getName(), bluetoothDevice.getAddress());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Boolean doInBackground(Void... params) {
            try {
                boolean complete = this.mCargoConnection.isOobeCompletedForDevice(this.mDeviceInfo);
                this.mSettingsProvider.setOobeUiBandVersion(this.mCargoConnection.isUsingV2Band(this.mDeviceInfo) ? BandVersion.NEON : BandVersion.CARGO);
                return Boolean.valueOf(!complete);
            } catch (Exception ex) {
                setException(ex);
                KLog.d(this.TAG, "Device failed when trying to check pairing, so we'll wait a second and try again", ex);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Boolean success) {
            if (success != null) {
                this.mCallback.callback(success);
            } else {
                this.mCallback.onError(getException());
            }
        }
    }

    /* loaded from: classes.dex */
    private class PairDeviceTask extends ScopedAsyncTask<Void, Void, Void> {
        BluetoothDevice mDevice;

        public PairDeviceTask(OnTaskListener onTaskListener, BluetoothDevice device) {
            super(onTaskListener);
            this.mDevice = device;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(Void... params) {
            OobeBluetoothConnectionFragment.this.pairDevice(this.mDevice);
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void result) {
        }
    }

    private void cancelHelpTimeout() {
        if (this.mTimeoutTask != null) {
            this.mTimeoutTask.cancel(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deviceNotAbleToConnect() {
        this.mDeviceListAdapter.setPairingItem(-1);
        this.mDeviceListAdapter.notifyDataSetChanged();
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.oobe_bluetooth_error_title), Integer.valueOf((int) R.string.oobe_bluetooth_error_device_not_responding), DialogPriority.HIGH);
        setIndicatorState(LoadingState.IDLE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deviceNotInOobe() {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.oobe_bluetooth_error_title), Integer.valueOf((int) R.string.oobe_bluetooth_error_device_not_in_oobe), DialogPriority.HIGH);
        setIndicatorState(LoadingState.IDLE);
    }
}
