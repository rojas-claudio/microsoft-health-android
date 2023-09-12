package com.microsoft.kapp.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.widget.TextView;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.kapp.DeviceErrorState;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.HomeTileNoDataFragment;
import com.microsoft.kapp.fragments.bike.BikeDetailsSplitFragment;
import com.microsoft.kapp.fragments.bike.BikeDetailsSummaryFragment;
import com.microsoft.kapp.fragments.calories.CaloriesDailyFragment;
import com.microsoft.kapp.fragments.calories.CaloriesWeeklyFragment;
import com.microsoft.kapp.fragments.exercise.ExerciseDetailsSummaryFragmentV1;
import com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment;
import com.microsoft.kapp.fragments.golf.GolfScorecardFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutDetailsFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutSummaryFragment;
import com.microsoft.kapp.fragments.run.RunDetailsSplitFragmentV1;
import com.microsoft.kapp.fragments.run.RunDetailsSummaryFragmentV1;
import com.microsoft.kapp.fragments.sleep.SleepDetailsSummaryFragment;
import com.microsoft.kapp.fragments.steps.StepDailyFragment;
import com.microsoft.kapp.fragments.steps.StepWeeklyFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.multidevice.KSyncResult;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GoalBaseDto;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalValueHistoryDto;
import com.microsoft.krestsdk.models.GoalValueRecordDto;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.microsoft.krestsdk.models.UserEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
/* loaded from: classes.dex */
public class CommonUtils {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String TAG;

    static {
        $assertionsDisabled = !CommonUtils.class.desiredAssertionStatus();
        TAG = CommonUtils.class.getSimpleName();
    }

    public static String truncateString(String str, int maxLength) {
        Validate.notNull(str, "str");
        if (str.length() > maxLength) {
            return str.substring(0, maxLength);
        }
        return str;
    }

    public static boolean areDoublesEqual(double a, double b) {
        return Math.abs(a - b) < 9.999999747378752E-6d;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean areNotificationsEnabled(Context context) {
        String enabledNotificationListeners = Settings.Secure.getString(context.getContentResolver(), Constants.ENABLED_NOTIFICATION_LISTENERS);
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(context.getPackageName());
    }

    public static boolean areNotificationsSupported() {
        return Build.VERSION.SDK_INT > 17;
    }

    public static Integer getResIdForDeviceErrorState(DeviceErrorState deviceErrorState) {
        switch (deviceErrorState) {
            case CONNECTION_FAILED:
                return Integer.valueOf((int) R.string.error_connection_failed);
            case DEVICE_NOT_OWNED:
                return Integer.valueOf((int) R.string.error_device_not_owned);
            case MULTIPLE_DEVICES_BONDED:
                return Integer.valueOf((int) R.string.multiple_devices_error_text);
            case NO_DEVICES_BONDED:
                return Integer.valueOf((int) R.string.error_no_devices_bonded);
            case USER_HAS_NO_DEVICE:
                return Integer.valueOf((int) R.string.error_user_has_no_device);
            case USER_HAS_NO_DEVICE_AND_DEVICE_IN_USE:
                return Integer.valueOf((int) R.string.error_user_has_no_device_and_device_in_use);
            default:
                return null;
        }
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getStepsFragmentData() {
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.home_tile_title_daily), StepDailyFragment.class));
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.home_tile_title_weekly), StepWeeklyFragment.class));
        return fragmentsData;
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getCaloriesFragmentData() {
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.home_tile_title_daily), CaloriesDailyFragment.class));
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.home_tile_title_weekly), CaloriesWeeklyFragment.class));
        return fragmentsData;
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getRunFragmentData(RunEvent runEvent) {
        Validate.notNull(runEvent, "runEvent");
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.details_summary_title), RunDetailsSummaryFragmentV1.class));
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.details_splits_title), RunDetailsSplitFragmentV1.class));
        return fragmentsData;
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getBikeFragmentData(BikeEvent bikeEvent) {
        Validate.notNull(bikeEvent, "bikeEvent");
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.details_summary_title), BikeDetailsSummaryFragment.class));
        if (bikeEvent.hasGpsData()) {
            fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.details_splits_title), BikeDetailsSplitFragment.class));
        }
        return fragmentsData;
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getSleepFragmentData(SleepEvent sleepEvent) {
        Validate.notNull(sleepEvent, "sleepEvent");
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.home_tile_title_summary), SleepDetailsSummaryFragment.class));
        return fragmentsData;
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getExerciseFragmentData(ExerciseEvent exerciseEvent) {
        Validate.notNull(exerciseEvent, "exerciseEvent");
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.home_tile_title_summary), ExerciseDetailsSummaryFragmentV1.class));
        return fragmentsData;
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getGuidedWorkoutFragmentData(GuidedWorkoutEvent guidedWorkoutEvent) {
        Validate.notNull(guidedWorkoutEvent, "guidedWorkoutEvent");
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.home_tile_title_summary), GuidedWorkoutSummaryFragment.class));
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.guided_workout_details_title), GuidedWorkoutDetailsFragment.class));
        return fragmentsData;
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getEmptyFragmentsData() {
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.empty), HomeTileNoDataFragment.class));
        return fragmentsData;
    }

    public static List<UserActivity> getEmptyUserActivity(DateTime dateTime) {
        List<UserActivity> emptyUserActivityList = new ArrayList<>();
        UserActivity userActivity = new UserActivity();
        userActivity.setTimeOfDay(dateTime);
        userActivity.setCaloriesBurned(0);
        emptyUserActivityList.add(userActivity);
        return emptyUserActivityList;
    }

    public static List<UserDailySummary> getEmptyUserDailySummaries(DateTime startDate) {
        List<UserDailySummary> emptyUserDailySummaries = new ArrayList<>();
        UserDailySummary userDailySummary = new UserDailySummary();
        userDailySummary.setTimeOfDay(startDate);
        userDailySummary.setCaloriesBurned(0);
        emptyUserDailySummaries.add(userDailySummary);
        return emptyUserDailySummaries;
    }

    public static List<UserDailySummary> getEmptyUserDailySummaries(LocalDate startDate) {
        return getEmptyUserDailySummaries(startDate.toDateTime(new LocalTime()));
    }

    public static int getCargoExceptionErrorMesgBody(KSyncResult result) {
        Integer resId = getSDEErrorMessageFromSyncResult(result);
        if (resId != null) {
            return resId.intValue();
        }
        Integer resId2 = getDeviceErrorMessageFromSyncResult(result);
        if (resId2 != null) {
            return resId2.intValue();
        }
        Integer resId3 = getCloudErrorMessageFromSyncResult(result);
        if (resId3 != null) {
            return resId3.intValue();
        }
        return Integer.valueOf((int) R.string.sync_failed_system_exception).intValue();
    }

    public static int getCargoExceptionErrorMesgTitle(KSyncResult result) {
        if (result == null || result.getStatus() == null) {
            return R.string.sync_failed_system_title;
        }
        switch (result.getStatus()) {
            case CLOUD_ERROR:
                return R.string.sync_failed_cloud_title;
            case DEVICE_ERROR:
                return R.string.sync_failed_band_title;
            case SDE_ERROR:
                return R.string.sync_failed_system_title;
            case UNKNOWN_ERROR:
                return R.string.sync_failed_system_title;
            case SUCCESS:
                if ($assertionsDisabled) {
                    return R.string.sync_failed_system_title;
                }
                throw new AssertionError(result.getStatus());
            default:
                return R.string.sync_failed_system_title;
        }
    }

    private static Integer getCloudErrorMessageFromSyncResult(KSyncResult result) {
        if (result.getStatus() != KSyncResult.SyncResultCode.CLOUD_ERROR) {
            return null;
        }
        Integer resId = Integer.valueOf((int) R.string.sync_failed_cloud_exception);
        return resId;
    }

    private static Integer getDeviceErrorMessageFromSyncResult(KSyncResult result) {
        if (result.getStatus() != KSyncResult.SyncResultCode.DEVICE_ERROR) {
            return null;
        }
        Integer resId = Integer.valueOf((int) R.string.sync_failed_device_exception);
        return resId;
    }

    private static Integer getSDEErrorMessageFromSyncResult(KSyncResult result) {
        if (result.getStatus() != KSyncResult.SyncResultCode.SDE_ERROR || result.getSDEError() == null) {
            return null;
        }
        switch (result.getSDEError()) {
            case WRONG_DEVICE:
                Integer resId = Integer.valueOf((int) R.string.sync_failed_sde_wrong_band_connected);
                return resId;
            default:
                Integer resId2 = Integer.valueOf((int) R.string.sync_failed_sde_wrong_band_connected);
                return resId2;
        }
    }

    public static KSyncResult getSyncResultFromKDKSyncResult(BandServiceMessage.Response error) {
        KSyncResult result = new KSyncResult(KSyncResult.SyncResultCode.UNKNOWN_ERROR);
        if (error != null) {
            switch (error) {
                case DEVICE_NOT_CONNECTED_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.DEVICE_ERROR);
                    result.setDeviceErrorCode(KSyncResult.DeviceErrorCode.DEVICE_NOT_CONNECTED);
                    break;
                case DEVICE_NOT_BONDED_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.DEVICE_ERROR);
                    result.setDeviceErrorCode(KSyncResult.DeviceErrorCode.DEVICE_NOT_BONDED);
                    break;
                case SERVICE_CLOUD_NETWORK_NOT_AVAILABLE_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                    result.setCloudError(KSyncResult.CloudErrorState.UNKNOWN);
                    break;
                case SERVICE_CLOUD_AUTHENTICATION_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                    result.setCloudError(KSyncResult.CloudErrorState.UNKNOWN);
                    break;
                case INVALID_SESSION_TOKEN_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                    result.setCloudError(KSyncResult.CloudErrorState.UNKNOWN);
                    break;
                case SERVICE_CLOUD_REQUEST_FAILED_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                    result.setCloudError(KSyncResult.CloudErrorState.UNKNOWN);
                    break;
                case SERVICE_CLOUD_OPERATION_FAILED_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                    result.setCloudError(KSyncResult.CloudErrorState.UNKNOWN);
                    break;
                case OPERATION_TIMEOUT_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                    result.setCloudError(KSyncResult.CloudErrorState.UNKNOWN);
                    break;
                case DEVICE_FIRMWARE_VERSION_INCOMPATIBLE_ERROR:
                    result.setStatus(KSyncResult.SyncResultCode.DEVICE_ERROR);
                    result.setDeviceErrorCode(KSyncResult.DeviceErrorCode.DEVICE_INCOMPATIBLE_FIRMWARE_VERSION);
                    break;
                default:
                    switch (error.getCategory()) {
                        case 3:
                            result.setStatus(KSyncResult.SyncResultCode.DEVICE_ERROR);
                            result.setDeviceErrorCode(KSyncResult.DeviceErrorCode.UNKNOWN);
                            break;
                        case 4:
                            result.setStatus(KSyncResult.SyncResultCode.CLOUD_ERROR);
                            result.setCloudError(KSyncResult.CloudErrorState.UNKNOWN);
                            break;
                        case 5:
                        case 6:
                        default:
                            result.setStatus(KSyncResult.SyncResultCode.UNKNOWN_ERROR);
                            break;
                        case 7:
                            result.setStatus(KSyncResult.SyncResultCode.DEVICE_ERROR);
                            result.setDeviceErrorCode(KSyncResult.DeviceErrorCode.BLUETOOTH_ERROR);
                            break;
                    }
            }
        }
        return result;
    }

    public static Bundle convertListMapToBundle(Map<String, List<String>> map) {
        if (map == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            bundle.putStringArrayList(entry.getKey(), (ArrayList) entry.getValue());
        }
        return bundle;
    }

    public static Bundle convertStringMapToBundle(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }
        return bundle;
    }

    public static HashMap<String, List<String>> extractStringListMapFromBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        HashMap<String, List<String>> map = new HashMap<>();
        for (String key : bundle.keySet()) {
            map.put(key, bundle.getStringArrayList(key));
        }
        return map;
    }

    public static HashMap<String, String> extractStringMapFromBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        for (String key : bundle.keySet()) {
            map.put(key, bundle.getString(key));
        }
        return map;
    }

    public static void sortEventList(List<UserEvent> mergedUserList) {
        Collections.sort(mergedUserList, new Comparator<UserEvent>() { // from class: com.microsoft.kapp.utils.CommonUtils.1
            @Override // java.util.Comparator
            public int compare(UserEvent event1, UserEvent event2) {
                return event2.getStartTime().compareTo((ReadableInstant) event1.getStartTime());
            }
        });
    }

    public static void sortUserDailySummaryDesc(List<UserDailySummary> userDailySummaryList) {
        Collections.sort(userDailySummaryList, new Comparator<UserDailySummary>() { // from class: com.microsoft.kapp.utils.CommonUtils.2
            @Override // java.util.Comparator
            public int compare(UserDailySummary summary1, UserDailySummary summary2) {
                return summary2.getTimeOfDay().compareTo((ReadableInstant) summary1.getTimeOfDay());
            }
        });
    }

    public static String calculateHnFFilterString(Map<String, String> filterNamesIdsMapping, Map<String, List<String>> filterCriteria) {
        if (filterNamesIdsMapping == null || filterCriteria == null || filterCriteria.size() == 0) {
            return "";
        }
        StringBuilder filterString = new StringBuilder();
        boolean firstFilter = true;
        for (Map.Entry<String, List<String>> filterEntries : filterCriteria.entrySet()) {
            List<String> filterValues = filterEntries.getValue();
            if (Validate.isNotNullNotEmpty(filterValues)) {
                if (firstFilter) {
                    firstFilter = false;
                } else {
                    filterString.append("%7E");
                }
                filterString.append(filterEntries.getKey());
                boolean firstFilterValue = true;
                for (String filterValue : filterValues) {
                    if (filterValue != null) {
                        if (firstFilterValue) {
                            filterString.append("%7C");
                        } else {
                            filterString.append(";");
                        }
                        firstFilterValue = false;
                        filterString.append(filterNamesIdsMapping.get(filterValue));
                    }
                }
            }
        }
        return filterString.toString();
    }

    public static String calculateHnFFitnessPlanSelectionFilterString(Map<String, String> filterNamesIdsMapping, Map<String, String> filterCriteria) {
        if (filterNamesIdsMapping == null || filterCriteria == null || filterCriteria.size() == 0) {
            return "";
        }
        StringBuilder filterString = new StringBuilder();
        boolean firstFilter = true;
        for (Map.Entry<String, String> filterEntrie : filterCriteria.entrySet()) {
            String filterValue = filterEntrie.getValue();
            if (filterValue != null) {
                if (firstFilter) {
                    firstFilter = false;
                } else {
                    filterString.append("%7E");
                }
                filterString.append(filterEntrie.getKey());
                filterString.append("%7C");
                filterString.append(filterNamesIdsMapping.get(filterValue));
            }
        }
        return filterString.toString();
    }

    public static Bitmap drawTextOnBitmap(Bitmap originalBitmap, String text) {
        if (originalBitmap == null) {
            return null;
        }
        if (text == null || text.isEmpty()) {
            return originalBitmap;
        }
        int pictureHeight = originalBitmap.getHeight();
        int pictureWidth = originalBitmap.getWidth();
        int TEXT_SIZE = pictureWidth / 20;
        int TEXT_SIZE_IN_CANVAS = TEXT_SIZE >> 1;
        int LINE_HEIGHT = pictureWidth / 13;
        Paint paint = new Paint();
        paint.setTextSize(TEXT_SIZE);
        int TEXT_SPACE_PER_LINE = (int) (paint.getTextSize() * 2.0f);
        int maxCharsPerLine = (pictureWidth - (TEXT_SIZE << 1)) / TEXT_SIZE_IN_CANVAS;
        String[] lines = separateTextIntoLines(text, maxCharsPerLine);
        if (lines == null) {
            lines = new String[0];
        }
        int textAreaHeight = lines.length * TEXT_SPACE_PER_LINE;
        Bitmap modifiedBitmap = Bitmap.createBitmap(pictureWidth, pictureHeight + textAreaHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(modifiedBitmap);
        paint.setColor(-1);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        canvas.drawBitmap(originalBitmap, 0.0f, textAreaHeight, paint);
        paint.setColor(-16777216);
        paint.setAntiAlias(true);
        int yPos = LINE_HEIGHT;
        String[] arr$ = lines;
        for (String line : arr$) {
            canvas.drawText(line, TEXT_SIZE, yPos, paint);
            yPos += LINE_HEIGHT;
        }
        return modifiedBitmap;
    }

    public static String[] separateTextIntoLines(String text, int maxCharsPerLine) {
        String[] words = text.split("\\s+");
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (words != null) {
            int len$ = words.length;
            for (int i$ = 0; i$ < len$; i$++) {
                String word = words[i$];
                if (sb.length() + word.length() >= maxCharsPerLine && sb.length() > 0) {
                    lines.add(sb.toString());
                    sb.delete(0, sb.length());
                }
                if (sb.length() == 0) {
                    while (word.length() >= maxCharsPerLine) {
                        lines.add(word.substring(0, maxCharsPerLine));
                        word = word.substring(maxCharsPerLine);
                    }
                    sb.append(word);
                } else {
                    sb.append(' ').append(word);
                }
            }
        }
        if (sb.length() > 0) {
            lines.add(sb.toString());
        }
        return (String[]) lines.toArray(new String[lines.size()]);
    }

    public static UserDailySummary getUserDailySummary(HomeData homeData) {
        List<UserDailySummary> dailySummaries = homeData.getUserDailySummaries();
        if (dailySummaries == null || dailySummaries.size() <= 0) {
            return null;
        }
        UserDailySummary userDailySummary = dailySummaries.get(dailySummaries.size() - 1);
        return userDailySummary;
    }

    public static void setResultAndExit(Activity activity, int resultCode) {
        if (activity != null) {
            activity.setResult(resultCode);
            activity.finish();
        }
    }

    public static String getAgeGroup(int age) {
        if (age < 18) {
            return "<18";
        }
        if (age < 25) {
            return "18-24";
        }
        int lowerBound = (age / 5) * 5;
        int upperBound = lowerBound + 4;
        return lowerBound + "-" + upperBound;
    }

    public static String getPersonalBestDisplayString(GoalBaseDto.BestEventType eventType, Context context) {
        if (context == null) {
            return null;
        }
        switch (eventType) {
            case FastestPaceRun:
                String personalBestStr = context.getResources().getString(R.string.best_run_pace_name);
                return personalBestStr;
            case FastestSplitRun:
                String personalBestStr2 = context.getResources().getString(R.string.best_run_split_name);
                return personalBestStr2;
            case FurthestRun:
                String personalBestStr3 = context.getResources().getString(R.string.best_run_distance_name);
                return personalBestStr3;
            case LongestDurationWorkout:
                String personalBestStr4 = context.getResources().getString(R.string.best_exercise_duration_name);
                return personalBestStr4;
            case MostCalorieRun:
                String personalBestStr5 = context.getResources().getString(R.string.best_run_calories_name);
                return personalBestStr5;
            case MostCalorieWorkout:
                String personalBestStr6 = context.getResources().getString(R.string.best_exercise_calories_name);
                return personalBestStr6;
            case FastestBikeRide:
                String personalBestStr7 = context.getResources().getString(R.string.best_bike_pace_name);
                return personalBestStr7;
            case FurthestBikeRide:
                String personalBestStr8 = context.getResources().getString(R.string.best_bike_distance_name);
                return personalBestStr8;
            case LargestGain:
                String personalBestStr9 = context.getResources().getString(R.string.best_bike_gain_name);
                return personalBestStr9;
            case MostCaloriesRide:
                String personalBestStr10 = context.getResources().getString(R.string.best_bike_calories_name);
                return personalBestStr10;
            case Unknown:
                return "";
            default:
                return null;
        }
    }

    public static void updatePersonalBests(List<GoalDto> personalBests, UserEvent event, Context context) {
        if (personalBests != null && event != null) {
            for (GoalDto goal : personalBests) {
                if (goal.getValueHistory() != null && !goal.getValueHistory().isEmpty()) {
                    GoalValueHistoryDto history = goal.getValueHistory().get(goal.getValueHistory().size() - 1);
                    if (history.getHistoryRecords() != null) {
                        if (!history.getHistoryRecords().isEmpty()) {
                            GoalValueRecordDto record = history.getHistoryRecords().get(history.getHistoryRecords().size() - 1);
                            if (record.getExtension() != null && record.getExtension().split("EventId:").length > 1) {
                                String eventId = record.getExtension().split("EventId:")[1].split("_")[0];
                                try {
                                    if (event.getEventId().equals(eventId)) {
                                        if (goal.getName() != null) {
                                            event.addPersonalBest(getPersonalBestDisplayString(goal.getPersonalBestType(), context));
                                        } else {
                                            KLog.e(TAG, "error loading personal best event. Invalid goal name");
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    KLog.e(TAG, "error loading personal best event. Invalid event ID", ex);
                                }
                            } else {
                                KLog.e(TAG, "error loading personal best event. Invalid personal best event ID");
                            }
                        }
                    } else {
                        KLog.e(TAG, "error loading personal best event. Invalid personal best history record data");
                    }
                } else {
                    KLog.e(TAG, "error loading personal best event. Invalid personal best value history data");
                }
            }
        }
    }

    public static boolean isDateToday(DateTime date) {
        return DateTimeComparator.getDateOnlyInstance().compare(date, DateTime.now()) == 0;
    }

    public static void applyCommonStyles(TextView view, FontManager fontManager, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
        if (a != null) {
            try {
                int fontFamilyId = a.getInt(0, 0);
                view.setTypeface(fontManager.getFontFace(fontFamilyId));
            } finally {
                a.recycle();
            }
        }
    }

    public static void applyCommonStyles(Paint paint, FontManager fontManager, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
        if (a != null) {
            try {
                int fontFamilyId = a.getInt(0, 0);
                paint.setTypeface(fontManager.getFontFace(fontFamilyId));
            } finally {
                a.recycle();
            }
        }
    }

    public static int getLocalTimeZoneOffsetInMinsAtInstant(DateTime datetime) {
        int offsetMs = DateTimeZone.getDefault().getOffset(datetime);
        return (offsetMs / 1000) / 60;
    }

    public static List<Pair<Integer, Class<? extends BaseFragment>>> getGolfFragmentData(GolfEvent golfEvent) {
        Validate.notNull(golfEvent, "golfEvent");
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = new ArrayList<>();
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.golf_page_title_scorecard), GolfScorecardFragment.class));
        fragmentsData.add(new Pair<>(Integer.valueOf((int) R.string.home_tile_title_summary), GolfDetailsSummaryFragment.class));
        return fragmentsData;
    }

    @TargetApi(19)
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), "location_mode");
            } catch (Settings.SettingNotFoundException ex) {
                KLog.w(TAG, "exception checking location info", ex);
            }
            return locationMode != 0;
        }
        String locationProviders = Settings.Secure.getString(context.getContentResolver(), "location_providers_allowed");
        return !TextUtils.isEmpty(locationProviders);
    }
}
