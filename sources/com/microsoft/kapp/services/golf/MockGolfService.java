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
public class MockGolfService implements GolfService {
    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getTopGolfEvents(int count, boolean expandSequences, Callback<List<GolfEvent>> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getGolfEventById(String eventId, boolean expandSequences, Callback<GolfEvent> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getRecentGolfEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<GolfEvent>> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getCourseDetail(String courseId, Context context, Callback<CourseDetails> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getNearbyCourses(double latitude, double longitude, CourseFilters filters, int page, int count, Callback<GolfSearchResultsModel> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getRecentCourses(Callback<GolfSearchResultsModel> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void isPartnerConnected(Callback<Boolean> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getCourseTees(String courseId, Callback<List<String>> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getAvailableStates(int regionId, ActivityScopedCallback<GolfStateResponse> activityScopedCallback) {
        activityScopedCallback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getAvailableCoursesForState(int stateId, int numberOfCourses, CourseFilters filters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback) {
        activityScopedCallback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getAvailableCoursesForRegion(int regionId, CourseFilters filters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback) {
        activityScopedCallback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getAvailableRegions(ActivityScopedCallback<GolfRegionResponse> activityScopedCallback) {
        activityScopedCallback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void findCoursesByName(String query, CourseFilters filters, int page, int count, ActivityScopedCallback<GolfCourseSearchResults> callback) {
        callback.callback(null);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public Pair<String, String> getSyncedGolfCourse() {
        return null;
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public byte[] getDeviceGolfCourse(String courseId, String teeId) throws KRestException {
        return null;
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void updateLastSyncedGolfCourse(String courseId, String teeId) {
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public boolean pushGolfCourseDetailToDevice(String golfCourse, String teeId) {
        return false;
    }
}
