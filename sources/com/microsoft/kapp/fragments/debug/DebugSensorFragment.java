package com.microsoft.kapp.fragments.debug;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.feedback.FeedbackUtilsV1;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.sensor.PhoneSensorDataProvider;
import com.microsoft.kapp.sensor.SensorDataDebugProvider;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.sensor.models.KSensorEventType;
import com.microsoft.kapp.sensor.models.StepSensorEvent;
import com.microsoft.kapp.sensor.service.SensorService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.FileUtils;
import com.microsoft.kapp.utils.ToastUtils;
import com.microsoft.kapp.utils.ViewUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class DebugSensorFragment extends BaseFragment {
    private static final String EVENT_DUMP_FILE = "sensorEvents.txt";
    private static final String EVENT_DUMP_ZIP_FILE = "SensorEvents.zip";
    @Inject
    Context mContext;
    private TextView mHardwareQueueSizeText;
    private LinearLayout mMainLayout;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    private TextView mOldestEventTimeText;
    @Inject
    PhoneSensorDataProvider mPhoneSensorDataProvider;
    private ProgressBar mProgress;
    private Button mSendEventsButton;
    @Inject
    SensorDataDebugProvider mSensorDataDebugProvider;
    private Spinner mSensorPhoneDataSpinner;
    private Spinner mSensorServiceSpinner;
    private TextView mSensorSupportedTextView;
    @Inject
    SensorUtils mSensorUtils;
    @Inject
    SettingsProvider mSettingsProvider;
    private TextView mStepsForToday;
    private Button mSyncButton;
    private TextView mTotalEventsText;
    private Spinner mUseLocalSensorDataSpinner;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.debug_fragment_sensors, container, false);
        this.mSensorSupportedTextView = (TextView) ViewUtils.getValidView(rootView, R.id.debug_sensor_supported_text, TextView.class);
        this.mSensorServiceSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.debug_sensor_enable_collection, Spinner.class);
        this.mUseLocalSensorDataSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.debug_sensor_enable_local_use, Spinner.class);
        this.mSensorPhoneDataSpinner = (Spinner) ViewUtils.getValidView(rootView, R.id.debug_sensor_enable_phone_view, Spinner.class);
        this.mOldestEventTimeText = (TextView) ViewUtils.getValidView(rootView, R.id.debug_sensor_oldest_event_text, TextView.class);
        this.mTotalEventsText = (TextView) ViewUtils.getValidView(rootView, R.id.debug_sensor_total_events_text, TextView.class);
        this.mSendEventsButton = (Button) ViewUtils.getValidView(rootView, R.id.debug_sensor_button_mail, Button.class);
        this.mSyncButton = (Button) ViewUtils.getValidView(rootView, R.id.debug_sensor_button_sync, Button.class);
        this.mProgress = (ProgressBar) ViewUtils.getValidView(rootView, R.id.debug_sensor_progress, ProgressBar.class);
        this.mMainLayout = (LinearLayout) ViewUtils.getValidView(rootView, R.id.debug_sensor_main_layout, LinearLayout.class);
        this.mStepsForToday = (TextView) ViewUtils.getValidView(rootView, R.id.debug_sensor_total_steps_text, TextView.class);
        this.mHardwareQueueSizeText = (TextView) ViewUtils.getValidView(rootView, R.id.debug_sensor_queue_size_text, TextView.class);
        populateData();
        return rootView;
    }

    private void populateData() {
        this.mProgress.setVisibility(0);
        this.mMainLayout.setVisibility(8);
        if (!this.mSensorUtils.isKitkatWithStepSensor()) {
            this.mSensorSupportedTextView.setText("Unsupported");
            this.mSensorServiceSpinner.setVisibility(8);
            this.mUseLocalSensorDataSpinner.setVisibility(8);
            this.mOldestEventTimeText.setVisibility(8);
            this.mTotalEventsText.setVisibility(8);
            this.mSendEventsButton.setVisibility(8);
        } else {
            this.mSensorSupportedTextView.setText("Supported");
            setSensorServiceSpinner();
            setUsePhoneDataSpinner();
            setUseLocalSensorDataSpinner();
            this.mStepsForToday.setText(String.valueOf(getTotalStepsForToday()));
            this.mOldestEventTimeText.setText(getOldestEventTimeText());
            this.mTotalEventsText.setText(String.valueOf(getTotalEventsCount()));
            this.mHardwareQueueSizeText.setText(String.valueOf(getFifoQueueSize()));
            setUpSendEventsButton();
            setUpSyncButton();
        }
        this.mProgress.setVisibility(8);
        this.mMainLayout.setVisibility(0);
    }

    @TargetApi(19)
    private int getFifoQueueSize() {
        if (!this.mSensorUtils.isKitkatWithStepSensor()) {
            return 0;
        }
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        Sensor stepCounter = sensorManager.getDefaultSensor(19);
        int queueSize = stepCounter.getFifoMaxEventCount();
        return queueSize;
    }

    private long getTotalStepsForToday() {
        return this.mSensorDataDebugProvider.getStepCountForTodayLocally();
    }

    private CharSequence getOldestEventTimeText() {
        DateTime oldestEvent = this.mPhoneSensorDataProvider.getOldestEventTime(KSensorEventType.STEP);
        return oldestEvent != null ? oldestEvent.toString() : "";
    }

    private long getTotalEventsCount() {
        return this.mSensorDataDebugProvider.getTotalStepEventCount();
    }

    private void setUseLocalSensorDataSpinner() {
        this.mUseLocalSensorDataSpinner.setSelection(this.mSettingsProvider.isUseLocalSensorDataEnabled() ? 0 : 1);
        this.mUseLocalSensorDataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.debug.DebugSensorFragment.1
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSensorFragment.this.mSettingsProvider.setIsUseLocalSensorDataEnabled(DebugSensorFragment.this.mUseLocalSensorDataSpinner.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setUsePhoneDataSpinner() {
        this.mSensorPhoneDataSpinner.setSelection(this.mSettingsProvider.isUsePhoneSensorData() ? 0 : 1);
        this.mSensorPhoneDataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.debug.DebugSensorFragment.2
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSensorFragment.this.mSettingsProvider.setIsUsePhoneSensorData(DebugSensorFragment.this.mSensorPhoneDataSpinner.getSelectedItemPosition() == 0);
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setSensorServiceSpinner() {
        this.mSensorServiceSpinner.setSelection(this.mSettingsProvider.isSensorLoggingEnabled() ? 0 : 1);
        this.mSensorServiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.microsoft.kapp.fragments.debug.DebugSensorFragment.3
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                DebugSensorFragment.this.mSettingsProvider.setIsSensorLoggingEnabled(DebugSensorFragment.this.mSensorServiceSpinner.getSelectedItemPosition() == 0);
                if (DebugSensorFragment.this.mSensorServiceSpinner.getSelectedItemPosition() == 0) {
                    if (DebugSensorFragment.this.mSensorUtils.isKitkatWithStepSensor()) {
                        Intent sensorServiceIntent = new Intent(DebugSensorFragment.this.mContext, SensorService.class);
                        DebugSensorFragment.this.mContext.startService(sensorServiceIntent);
                        return;
                    }
                    return;
                }
                DebugSensorFragment.this.getActivity().stopService(new Intent(DebugSensorFragment.this.getActivity(), SensorService.class));
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setUpSyncButton() {
        this.mSyncButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.debug.DebugSensorFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ToastUtils.showShortToast(DebugSensorFragment.this.mContext, "Starting Sync");
                DebugSensorFragment.this.mMultiDeviceManager.startSync();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.microsoft.kapp.fragments.debug.DebugSensorFragment$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View arg0) {
            ToastUtils.showLongToast(DebugSensorFragment.this.getActivity(), "Collecting sensor event data");
            AsyncTask<Void, Void, Void> sendEventsTask = new AsyncTask<Void, Void, Void>() { // from class: com.microsoft.kapp.fragments.debug.DebugSensorFragment.5.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public Void doInBackground(Void... params) {
                    List<StepSensorEvent> events = DebugSensorFragment.this.mSensorDataDebugProvider.getStepSensorEventsListForToday();
                    Uri zipFilePath = dumpEventsToFile(events);
                    if (zipFilePath != null) {
                        ArrayList<Uri> uriList = new ArrayList<>();
                        uriList.add(zipFilePath);
                        FeedbackUtilsV1.sendFeedbackAsync(DebugSensorFragment.this.getView(), DebugSensorFragment.this.getActivity(), DebugSensorFragment.this.mCargoConnection);
                        return null;
                    }
                    return null;
                }

                private Uri dumpEventsToFile(List<StepSensorEvent> events) {
                    if (events == null || events.isEmpty()) {
                        return null;
                    }
                    try {
                        File outputFile = new File(Environment.getExternalStorageDirectory(), DebugSensorFragment.EVENT_DUMP_FILE);
                        outputFile.delete();
                        outputFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(outputFile);
                        for (StepSensorEvent event : events) {
                            String line = event.getClass().getName() + "\t" + event.getEventTime().toString() + "\t" + event.getValue() + "\n";
                            fos.write(line.getBytes());
                        }
                        fos.close();
                        FileInputStream fis = new FileInputStream(outputFile);
                        List<Pair<String, InputStream>> inputStreams = new ArrayList<>();
                        inputStreams.add(new Pair<>(DebugSensorFragment.EVENT_DUMP_FILE, fis));
                        File zipFile = FileUtils.zip(inputStreams, Environment.getExternalStorageDirectory(), DebugSensorFragment.EVENT_DUMP_ZIP_FILE);
                        Uri zipFilePath = Uri.fromFile(zipFile);
                        return zipFilePath;
                    } catch (Exception e) {
                        Log.d(DebugSensorFragment.this.TAG, "unable to create events zip file");
                        DebugSensorFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.fragments.debug.DebugSensorFragment.5.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                ToastUtils.showLongToast(DebugSensorFragment.this.getActivity(), "Unable to create zip file with events");
                            }
                        });
                        return null;
                    }
                }
            };
            sendEventsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    private void setUpSendEventsButton() {
        this.mSendEventsButton.setOnClickListener(new AnonymousClass5());
    }
}
