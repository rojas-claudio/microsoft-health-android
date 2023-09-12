package com.microsoft.kapp.services.golf;

import android.content.Context;
import android.util.Pair;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.models.golf.CourseDetails;
import com.microsoft.kapp.models.golf.GolfRegionResponse;
import com.microsoft.kapp.models.golf.GolfSearchResultsModel;
import com.microsoft.kapp.models.golf.GolfStateResponse;
import com.microsoft.krestsdk.models.GolfCourseSearchResults;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.services.KRestException;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public interface GolfService {
    public static final int GOLF_COURSE_PULL_NETWORK_ERROR = 3;
    public static final int GOLF_COURSE_PUSH_BLUETOOTH_ERROR = 4;
    public static final int GOLF_COURSE_PUSH_FAILED = 0;
    public static final int GOLF_COURSE_PUSH_FAILED_DEVICE_DISCONNECTED = 6;
    public static final int GOLF_COURSE_PUSH_FAILED_TILE_OPEN = 1;
    public static final int GOLF_COURSE_PUSH_SUCCEEDED = 2;
    public static final int GOLF_COURSE_PUSH_TIMEOUT_ERROR = 5;

    void findCoursesByName(String str, CourseFilters courseFilters, int i, int i2, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback);

    void getAvailableCoursesForRegion(int i, CourseFilters courseFilters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback);

    void getAvailableCoursesForState(int i, int i2, CourseFilters courseFilters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback);

    void getAvailableRegions(ActivityScopedCallback<GolfRegionResponse> activityScopedCallback);

    void getAvailableStates(int i, ActivityScopedCallback<GolfStateResponse> activityScopedCallback);

    void getCourseDetail(String str, Context context, Callback<CourseDetails> callback);

    void getCourseTees(String str, Callback<List<String>> callback);

    byte[] getDeviceGolfCourse(String str, String str2) throws KRestException;

    void getGolfEventById(String str, boolean z, Callback<GolfEvent> callback);

    void getNearbyCourses(double d, double d2, CourseFilters courseFilters, int i, int i2, Callback<GolfSearchResultsModel> callback);

    void getRecentCourses(Callback<GolfSearchResultsModel> callback);

    void getRecentGolfEvents(DateTime dateTime, int i, boolean z, Callback<List<GolfEvent>> callback);

    Pair<String, String> getSyncedGolfCourse();

    void getTopGolfEvents(int i, boolean z, Callback<List<GolfEvent>> callback);

    void isPartnerConnected(Callback<Boolean> callback);

    boolean pushGolfCourseDetailToDevice(String str, String str2);

    void updateLastSyncedGolfCourse(String str, String str2);
}
