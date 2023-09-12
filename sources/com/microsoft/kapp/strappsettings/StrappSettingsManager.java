package com.microsoft.kapp.strappsettings;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import com.google.gson.Gson;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.command.BikeDisplayMetricType;
import com.microsoft.band.device.command.RunDisplayMetricType;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.models.strapp.StrappState;
import com.microsoft.kapp.models.strapp.StrappStateCollection;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.finance.StockCompanyInformation;
import com.microsoft.kapp.utils.AppConfigurationManager;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.BandVersion;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public class StrappSettingsManager {
    private static final int MAX_CANNED_REPLIES = 4;
    private static final String TAG = StrappSettingsManager.class.getSimpleName();
    private AppConfigurationManager mAppConfigurationManager;
    private boolean mAreNotificationsChanged;
    private BandVersion mBandVersion;
    CargoConnection mCargoConnection;
    Context mContext;
    private boolean mIsOrderChanged;
    StrappStateCollection mRetrievedStrapps;
    SettingsProvider mSettingsProvider;
    BikeDisplayMetricType[] mTransactionBikeMetrics;
    Integer mTransactionBikeSplitDistance;
    ArrayList<String> mTransactionNotificationCenterApps;
    StrappStateCollection mTransactionOrderedEnabledStrapps;
    RunDisplayMetricType[] mTransactionRunMetrics;
    String mTransactionStarbucksCardNumber;
    ArrayList<StockCompanyInformation> mTransactionStockList;
    HashMap<UUID, Boolean> mTransactionNotificationsEnabled = new HashMap<>();
    HashMap<UUID, String[]> mTransactionAutoReplies = new HashMap<>();

    /* loaded from: classes.dex */
    public interface CommitCallback {
        void onStrappSettingsManagerCommitFail();

        void onStrappSettingsManagerCommitSuccess();
    }

    public StrappSettingsManager(CargoConnection cargoConnection, SettingsProvider settingsProvider, AppConfigurationManager appConfigurationManager, Context context) {
        Validate.notNull(cargoConnection, "cargoConnection", new Object[0]);
        Validate.notNull(settingsProvider, "settingsProvider", new Object[0]);
        Validate.notNull(appConfigurationManager, "appConfiguration", new Object[0]);
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO, new Object[0]);
        this.mCargoConnection = cargoConnection;
        this.mSettingsProvider = settingsProvider;
        this.mAppConfigurationManager = appConfigurationManager;
        this.mContext = context;
    }

    public void clearTransaction() {
        clearTransactionStrapps();
        clearTransactionSettings();
    }

    public void setBandVersion(BandVersion version) {
        this.mBandVersion = version;
    }

    public BandVersion getBandVersion() {
        return this.mBandVersion;
    }

    public void clearTransactionStrapps() {
        this.mTransactionOrderedEnabledStrapps = null;
        this.mIsOrderChanged = false;
    }

    public void clearTransactionSettings() {
        this.mTransactionNotificationsEnabled.clear();
        this.mTransactionAutoReplies.clear();
        this.mTransactionRunMetrics = null;
        this.mTransactionBikeMetrics = null;
        this.mTransactionBikeSplitDistance = null;
        this.mTransactionStockList = null;
        this.mTransactionStarbucksCardNumber = null;
        this.mTransactionNotificationCenterApps = null;
        this.mAreNotificationsChanged = false;
    }

    public boolean isTransactionPending() {
        return isTransactionStrappsPending() || isTransactionSettingsPending();
    }

    public boolean isTransactionStrappsPending() {
        return this.mTransactionOrderedEnabledStrapps != null || this.mAreNotificationsChanged;
    }

    public boolean isTransactionSettingsPending() {
        return (!this.mAreNotificationsChanged && this.mTransactionAutoReplies.isEmpty() && this.mTransactionRunMetrics == null && this.mTransactionBikeMetrics == null && this.mTransactionStockList == null && this.mTransactionStarbucksCardNumber == null && this.mTransactionBikeSplitDistance == null && this.mTransactionNotificationCenterApps == null) ? false : true;
    }

    public void clearRetrievedStrapps() {
        Validate.isTrue(!isTransactionPending());
        this.mRetrievedStrapps = null;
    }

    public void setRetrievedStrapps(StrappStateCollection retrievedStrapps) {
        Validate.notNull(retrievedStrapps, "strappStateCollection", new Object[0]);
        this.mRetrievedStrapps = deepClone(retrievedStrapps);
    }

    public StrappStateCollection getRetrievedStrapps() {
        return deepClone(this.mRetrievedStrapps);
    }

    public void setTransactionStrapps(StrappStateCollection transactionStrapps) {
        Validate.notNull(transactionStrapps, "strappStateCollection", new Object[0]);
        if (this.mTransactionOrderedEnabledStrapps != null) {
            Validate.isTrue(transactionStrapps.size() == this.mTransactionOrderedEnabledStrapps.size(), "Size mismatch between re-ordered set and currently transacted set of strapps.", new Object[0]);
        } else {
            Validate.isTrue(transactionStrapps.size() == this.mRetrievedStrapps.size(), "Size mismatch between re-ordered set and initial retrieved set of strapps.", new Object[0]);
        }
        this.mTransactionOrderedEnabledStrapps = deepClone(transactionStrapps);
    }

    public StrappStateCollection getTransactionStrapps() {
        return deepClone(this.mTransactionOrderedEnabledStrapps);
    }

    public void enableTransactionStrapp(UUID strappId, StrappState strappState) {
        Validate.notNull(this.mRetrievedStrapps, "mRetrievedStrapps", new Object[0]);
        if (this.mTransactionOrderedEnabledStrapps == null) {
            this.mTransactionOrderedEnabledStrapps = deepClone(this.mRetrievedStrapps);
        }
        this.mTransactionOrderedEnabledStrapps.put(strappId, strappState);
    }

    public void disableTransactionStrapp(UUID strappId) {
        Validate.notNull(this.mRetrievedStrapps, "mRetrievedStrapps", new Object[0]);
        if (this.mTransactionOrderedEnabledStrapps == null) {
            this.mTransactionOrderedEnabledStrapps = deepClone(this.mRetrievedStrapps);
        }
        this.mTransactionOrderedEnabledStrapps.remove(strappId);
    }

    private StrappStateCollection deepClone(StrappStateCollection original) {
        if (original == null) {
            return null;
        }
        return (StrappStateCollection) new Gson().fromJson(new Gson().toJson(original), (Class<Object>) StrappStateCollection.class);
    }

    public void setTransactionNotificationsEnabled(UUID strappId, boolean notificationsEnabled) {
        this.mTransactionNotificationsEnabled.put(strappId, Boolean.valueOf(notificationsEnabled));
    }

    public void setTransactionNotificationsChanged(boolean areChanged) {
        this.mAreNotificationsChanged = areChanged;
    }

    public void setTransactionAutoReplies(UUID strappId, String... replies) {
        Validate.notNull(strappId, "strappId", new Object[0]);
        Validate.notNull(replies, "replies", new Object[0]);
        Validate.isTrue(replies.length == 4);
        String[] rolledUpCannedReplies = new String[4];
        for (int i = 0; i < rolledUpCannedReplies.length; i++) {
            rolledUpCannedReplies[i] = "";
        }
        int replyCount = 0;
        for (int i2 = 0; i2 < replies.length; i2++) {
            if (!StringUtil.isNullOrEmpty(replies[i2])) {
                rolledUpCannedReplies[replyCount] = replies[i2];
                replyCount++;
            }
        }
        this.mTransactionAutoReplies.put(strappId, rolledUpCannedReplies);
    }

    public void setTransactionRunMetrics(RunDisplayMetricType... runMetrics) {
        Validate.notNull(runMetrics);
        this.mTransactionRunMetrics = (RunDisplayMetricType[]) runMetrics.clone();
    }

    public void setTransactionBikeMetrics(BikeDisplayMetricType... bikeMetrics) {
        Validate.notNull(bikeMetrics);
        this.mTransactionBikeMetrics = (BikeDisplayMetricType[]) bikeMetrics.clone();
    }

    public BikeDisplayMetricType[] getBikingMetricsOrder() {
        return this.mTransactionBikeMetrics != null ? this.mTransactionBikeMetrics : this.mSettingsProvider.getBikingMetricsOrder(this.mBandVersion);
    }

    public void setTransactionBikeSplitDistance(int splitDistance) {
        this.mTransactionBikeSplitDistance = Integer.valueOf(splitDistance);
    }

    public int getBikeSplitDistance() {
        return this.mTransactionBikeSplitDistance != null ? this.mTransactionBikeSplitDistance.intValue() : this.mSettingsProvider.getBikeSplitDistance();
    }

    public void setTransactionStockList(List<StockCompanyInformation> stockList) {
        Validate.notNull(stockList);
        this.mTransactionStockList = new ArrayList<>(stockList);
    }

    public void setTransactionNotificationCenterApps(ArrayList<String> appList) {
        Validate.notNull(appList);
        this.mTransactionNotificationCenterApps = appList;
    }

    public void setStarbucksCardNumber(String starbucksCardNumber) {
        this.mTransactionStarbucksCardNumber = starbucksCardNumber;
    }

    public void completeTransactionStrapps(StrappStateCollection savedStrapps) {
        Validate.notNull(savedStrapps, "savedStrapps", new Object[0]);
        this.mRetrievedStrapps = savedStrapps;
        this.mTransactionOrderedEnabledStrapps = null;
        this.mIsOrderChanged = false;
    }

    public void commitTransactionSettingsAsync(CommitCallback callback, StrappStateCollection currentlySavedStrapps) {
        Validate.notNull(callback, "callback", new Object[0]);
        CommitSettingsTask task = new CommitSettingsTask(callback, currentlySavedStrapps);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* loaded from: classes.dex */
    private class CommitSettingsTask extends AsyncTask<Void, Void, Boolean> {
        WeakReference<CommitCallback> mCallback;
        WeakReference<StrappStateCollection> mCurrentlySavedStrapps;

        CommitSettingsTask(CommitCallback callback, StrappStateCollection currentlySavedStrapps) {
            this.mCallback = new WeakReference<>(callback);
            this.mCurrentlySavedStrapps = new WeakReference<>(currentlySavedStrapps);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... params) {
            StrappStateCollection currentlySavedStrapps = this.mCurrentlySavedStrapps.get();
            if (currentlySavedStrapps == null) {
                return false;
            }
            try {
                if (!StrappSettingsManager.this.mTransactionNotificationsEnabled.isEmpty()) {
                    StrappSettingsManager.this.mTransactionNotificationsEnabled.clear();
                }
                if (!StrappSettingsManager.this.mTransactionAutoReplies.isEmpty()) {
                    for (UUID strappId : StrappSettingsManager.this.mTransactionAutoReplies.keySet()) {
                        String[] replies = StrappSettingsManager.this.mTransactionAutoReplies.get(strappId);
                        String m1 = replies[0] == null ? "" : replies[0];
                        String m2 = replies[1] == null ? "" : replies[1];
                        String m3 = replies[2] == null ? "" : replies[2];
                        String m4 = replies[3] == null ? "" : replies[3];
                        if (DefaultStrappUUID.STRAPP_CALLS.equals(strappId)) {
                            StrappSettingsManager.this.mCargoConnection.savePhoneCallResponses(m1, m2, m3, m4);
                            StrappSettingsManager.this.mSettingsProvider.setCallsAutoReply(1, m1);
                            StrappSettingsManager.this.mSettingsProvider.setCallsAutoReply(2, m2);
                            StrappSettingsManager.this.mSettingsProvider.setCallsAutoReply(3, m3);
                            StrappSettingsManager.this.mSettingsProvider.setCallsAutoReply(4, m4);
                        } else if (!DefaultStrappUUID.STRAPP_MESSAGING.equals(strappId)) {
                            KLog.w(StrappSettingsManager.TAG, "UUID (" + strappId + ") is invalid for saving auto replies.");
                        } else {
                            StrappSettingsManager.this.mCargoConnection.saveSmsResponses(m1, m2, m3, m4);
                            StrappSettingsManager.this.mSettingsProvider.setMessagingAutoReply(1, m1);
                            StrappSettingsManager.this.mSettingsProvider.setMessagingAutoReply(2, m2);
                            StrappSettingsManager.this.mSettingsProvider.setMessagingAutoReply(3, m3);
                            StrappSettingsManager.this.mSettingsProvider.setMessagingAutoReply(4, m4);
                        }
                    }
                    StrappSettingsManager.this.mTransactionAutoReplies.clear();
                }
                if (StrappSettingsManager.this.mTransactionRunMetrics != null) {
                    if (StrappSettingsManager.this.mCargoConnection.setRunMetricsOrder(StrappSettingsManager.this.mTransactionRunMetrics)) {
                        StrappSettingsManager.this.mSettingsProvider.setRunMetricsOrder(StrappSettingsManager.this.mBandVersion, StrappSettingsManager.this.mTransactionRunMetrics);
                        StrappSettingsManager.this.mTransactionRunMetrics = null;
                        Telemetry.logEvent(TelemetryConstants.Events.RUN_DATA_POINTS_CHANGE);
                    } else {
                        return false;
                    }
                }
                if (StrappSettingsManager.this.mTransactionBikeMetrics != null) {
                    if (StrappSettingsManager.this.mCargoConnection.setBikeMetricsOrder(StrappSettingsManager.this.mTransactionBikeMetrics)) {
                        StrappSettingsManager.this.mSettingsProvider.setBikingMetricsOrder(StrappSettingsManager.this.mBandVersion, StrappSettingsManager.this.mTransactionBikeMetrics);
                        StrappSettingsManager.this.mTransactionBikeMetrics = null;
                        Telemetry.logEvent(TelemetryConstants.Events.BIKE_DATA_POINTS_CHANGE);
                    } else {
                        return false;
                    }
                }
                if (StrappSettingsManager.this.mTransactionBikeSplitDistance != null) {
                    if (!StrappSettingsManager.this.mCargoConnection.setBikeSplitsDistance(StrappSettingsManager.this.mTransactionBikeSplitDistance)) {
                        return false;
                    }
                    StrappSettingsManager.this.mSettingsProvider.setBikeSplitDistance(StrappSettingsManager.this.mTransactionBikeSplitDistance);
                    StrappSettingsManager.this.mTransactionBikeSplitDistance = null;
                }
                if (StrappSettingsManager.this.mTransactionStockList != null) {
                    StrappSettingsManager.this.mSettingsProvider.saveStockCompanies(StrappSettingsManager.this.mTransactionStockList);
                    HashMap<String, String> telemetryProperties = new HashMap<>();
                    telemetryProperties.put(TelemetryConstants.Events.StockWatchListChange.Dimensions.NUMBER_OF_SYMBOLS, String.valueOf(StrappSettingsManager.this.mTransactionStockList.size()));
                    Telemetry.logEvent(TelemetryConstants.Events.StockWatchListChange.EVENT_NAME, telemetryProperties, null);
                    StrappSettingsManager.this.mTransactionStockList = null;
                }
                if (StrappSettingsManager.this.mTransactionStarbucksCardNumber != null) {
                    StrappSettingsManager.this.mSettingsProvider.saveStarbucksCardNumber(StrappSettingsManager.this.mTransactionStarbucksCardNumber);
                }
                if (StrappSettingsManager.this.mTransactionNotificationCenterApps != null) {
                    StrappSettingsManager.this.mSettingsProvider.saveNotificationCenterApps(StrappSettingsManager.this.mTransactionNotificationCenterApps);
                }
                return true;
            } catch (Exception ex) {
                KLog.e(StrappSettingsManager.TAG, "Caught Exception while saving strapp settings.", ex);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean result) {
            super.onPostExecute((CommitSettingsTask) result);
            CommitCallback callback = this.mCallback.get();
            if (callback != null) {
                if (result.booleanValue()) {
                    callback.onStrappSettingsManagerCommitSuccess();
                } else {
                    callback.onStrappSettingsManagerCommitFail();
                }
            }
        }
    }

    public String getMessagingAutoReply(int replyNum) {
        boolean z = true;
        Validate.isTrue((replyNum < 1 || replyNum > 4) ? false : false);
        if (this.mTransactionAutoReplies.containsKey(DefaultStrappUUID.STRAPP_MESSAGING)) {
            return this.mTransactionAutoReplies.get(DefaultStrappUUID.STRAPP_MESSAGING)[replyNum - 1];
        }
        int defaultReplyId = getMessagingReplyDefaultId(replyNum);
        return this.mSettingsProvider.getMessagingAutoReply(replyNum, this.mContext.getString(defaultReplyId));
    }

    public String getCallsAutoReply(int replyNum) {
        boolean z = true;
        Validate.isTrue((replyNum < 1 || replyNum > 4) ? false : false);
        if (this.mTransactionAutoReplies.containsKey(DefaultStrappUUID.STRAPP_CALLS)) {
            return this.mTransactionAutoReplies.get(DefaultStrappUUID.STRAPP_CALLS)[replyNum - 1];
        }
        int defaultReplyId = getCallAutoReplyDefaultId(replyNum);
        return this.mSettingsProvider.getCallsAutoReply(replyNum, this.mContext.getString(defaultReplyId));
    }

    public static int getMessagingReplyDefaultId(int replyNum) {
        switch (replyNum) {
            case 1:
                return R.string.strapp_auto_replies_messaging_default_reply1;
            case 2:
                return R.string.strapp_auto_replies_messaging_default_reply2;
            case 3:
                return R.string.strapp_auto_replies_messaging_default_reply3;
            case 4:
                return R.string.strapp_auto_replies_messaging_default_reply4;
            default:
                return -1;
        }
    }

    public static int getCallAutoReplyDefaultId(int replyNum) {
        switch (replyNum) {
            case 1:
                return R.string.strapp_auto_replies_calls_default_reply1;
            case 2:
                return R.string.strapp_auto_replies_calls_default_reply2;
            case 3:
                return R.string.strapp_auto_replies_calls_default_reply3;
            case 4:
                return R.string.strapp_auto_replies_calls_default_reply4;
            default:
                return -1;
        }
    }

    public static List<Pair<Integer, UUID>> getExercisePickerOptions() {
        List<Pair<Integer, UUID>> options = new LinkedList<>();
        options.add(new Pair<>(Integer.valueOf((int) R.string.strapp_exercise_picker_option), UUID.fromString("24794770-5084-49BB-838C-504BC6663B30")));
        return options;
    }

    public String getStarbucksCardNumber() {
        return this.mTransactionStarbucksCardNumber != null ? this.mTransactionStarbucksCardNumber : this.mSettingsProvider.getStarbucksCardNumber();
    }

    public ArrayList<String> getNotificationCenterApps() {
        return this.mTransactionNotificationCenterApps != null ? this.mTransactionNotificationCenterApps : this.mSettingsProvider.getNotificationCenterApps();
    }

    public boolean isMessagingDeviceNotificationsEnabled() {
        return this.mTransactionNotificationsEnabled.containsKey(DefaultStrappUUID.STRAPP_MESSAGING) ? this.mTransactionNotificationsEnabled.get(DefaultStrappUUID.STRAPP_MESSAGING).booleanValue() : this.mSettingsProvider.isMessagingDeviceNotificationsEnabled();
    }

    public boolean isCallsDeviceNotificationsEnabled() {
        return this.mTransactionNotificationsEnabled.containsKey(DefaultStrappUUID.STRAPP_CALLS) ? this.mTransactionNotificationsEnabled.get(DefaultStrappUUID.STRAPP_CALLS).booleanValue() : this.mSettingsProvider.isCallsDeviceNotificationsEnabled();
    }

    public boolean isCalendarDeviceNotificationsEnabled() {
        return this.mTransactionNotificationsEnabled.containsKey(DefaultStrappUUID.STRAPP_CALENDAR) ? this.mTransactionNotificationsEnabled.get(DefaultStrappUUID.STRAPP_CALENDAR).booleanValue() : this.mSettingsProvider.isCalendarDeviceNotificationsEnabled();
    }

    public boolean isEmailDeviceNotificationsEnabled() {
        return this.mTransactionNotificationsEnabled.containsKey(DefaultStrappUUID.STRAPP_EMAIL) ? this.mTransactionNotificationsEnabled.get(DefaultStrappUUID.STRAPP_EMAIL).booleanValue() : this.mSettingsProvider.isEmailDeviceNotificationsEnabled();
    }

    public boolean isFacebookDeviceNotificationsEnabled() {
        return this.mTransactionNotificationsEnabled.containsKey(DefaultStrappUUID.STRAPP_FACEBOOK) ? this.mTransactionNotificationsEnabled.get(DefaultStrappUUID.STRAPP_FACEBOOK).booleanValue() : this.mSettingsProvider.isFacebookDeviceNotificationsEnabled();
    }

    public boolean isFacebookMessengerDeviceNotificationsEnabled() {
        return this.mTransactionNotificationsEnabled.containsKey(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER) ? this.mTransactionNotificationsEnabled.get(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER).booleanValue() : this.mSettingsProvider.isFacebookMessengerDeviceNotificationsEnabled();
    }

    public boolean isNotificationCenterDeviceNotificationsEnabled() {
        return this.mTransactionNotificationsEnabled.containsKey(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER) ? this.mTransactionNotificationsEnabled.get(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER).booleanValue() : this.mSettingsProvider.isNotificationCenterDeviceNotificationsEnabled();
    }

    public boolean isTwitterDeviceNotificationsEnabled() {
        return this.mTransactionNotificationsEnabled.containsKey(DefaultStrappUUID.STRAPP_TWITTER) ? this.mTransactionNotificationsEnabled.get(DefaultStrappUUID.STRAPP_TWITTER).booleanValue() : this.mSettingsProvider.isTwitterDeviceNotificationsEnabled();
    }

    public List<StockCompanyInformation> getStockCompanies() {
        return this.mTransactionStockList != null ? this.mTransactionStockList : this.mAppConfigurationManager.getStockInformation();
    }

    public boolean isOrderChanged() {
        return this.mIsOrderChanged;
    }

    public void setIsOrderChanged(boolean isChanged) {
        this.mIsOrderChanged = isChanged;
    }

    public boolean areSettingsEnabledForStrapp(UUID uuid) {
        if (uuid.equals(DefaultStrappUUID.STRAPP_CALLS)) {
            return isCallsDeviceNotificationsEnabled();
        }
        if (uuid.equals(DefaultStrappUUID.STRAPP_MESSAGING)) {
            return isMessagingDeviceNotificationsEnabled();
        }
        if (uuid.equals(DefaultStrappUUID.STRAPP_CALENDAR)) {
            return isCalendarDeviceNotificationsEnabled();
        }
        if (uuid.equals(DefaultStrappUUID.STRAPP_EMAIL)) {
            return isEmailDeviceNotificationsEnabled();
        }
        if (uuid.equals(DefaultStrappUUID.STRAPP_FACEBOOK)) {
            return isFacebookDeviceNotificationsEnabled();
        }
        if (uuid.equals(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER)) {
            return isFacebookMessengerDeviceNotificationsEnabled();
        }
        if (uuid.equals(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER)) {
            return isNotificationCenterDeviceNotificationsEnabled();
        }
        if (uuid.equals(DefaultStrappUUID.STRAPP_TWITTER)) {
            return isTwitterDeviceNotificationsEnabled();
        }
        return true;
    }

    public boolean isNotificationStatusManagedTile(UUID appId) {
        return appId.equals(DefaultStrappUUID.STRAPP_CALLS) || appId.equals(DefaultStrappUUID.STRAPP_MESSAGING) || appId.equals(DefaultStrappUUID.STRAPP_CALENDAR) || appId.equals(DefaultStrappUUID.STRAPP_EMAIL) || appId.equals(DefaultStrappUUID.STRAPP_FACEBOOK) || appId.equals(DefaultStrappUUID.STRAPP_FACEBOOK_MESSENGER) || appId.equals(DefaultStrappUUID.STRAPP_NOTIFICATION_CENTER) || appId.equals(DefaultStrappUUID.STRAPP_TWITTER);
    }

    public RunDisplayMetricType[] getRunMetricsOrder() {
        return this.mTransactionRunMetrics != null ? this.mTransactionRunMetrics : this.mSettingsProvider.getRunMetricsOrder(this.mBandVersion);
    }

    public void setStrappStatesRetrievedFromBand(List<CargoStrapp> appList) {
        for (CargoStrapp strapp : appList) {
            if (!this.mTransactionNotificationsEnabled.containsKey(strapp.getId())) {
                setTransactionNotificationsEnabled(strapp.getId(), (strapp.getSettingMask() & 1) != 0);
            }
        }
    }

    public int getRunMetricStrings() {
        return this.mBandVersion == BandVersion.CARGO ? R.array.cargo_run_metric_values : R.array.neon_run_metric_values;
    }

    public int getBikeMetricStrings() {
        return this.mBandVersion == BandVersion.CARGO ? R.array.cargo_bike_metric_values : R.array.neon_bike_metric_values;
    }
}
