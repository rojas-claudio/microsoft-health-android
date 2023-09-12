package com.microsoft.kapp.services.golf;

import android.content.Context;
import android.util.Pair;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.CourseDetails;
import com.microsoft.kapp.models.golf.GolfRegionResponse;
import com.microsoft.kapp.models.golf.GolfSearchResultsModel;
import com.microsoft.kapp.models.golf.GolfStateResponse;
import com.microsoft.kapp.services.PartnerType;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.golf.PushGolfToDeviceOperation;
import com.microsoft.krestsdk.models.GolfCourse;
import com.microsoft.krestsdk.models.GolfCourseSearchResults;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GolfTee;
import com.microsoft.krestsdk.models.Partner;
import com.microsoft.krestsdk.models.Partners;
import com.microsoft.krestsdk.services.KCloudConstants;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.RestService;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class GolfServiceImpl implements GolfService {
    private static final String TAG = GolfServiceImpl.class.getSimpleName();
    private RestService mRestService;
    private SettingsProvider mSettingsProvider;
    private final AtomicBoolean mIsGolfCourseSyncing = new AtomicBoolean(false);
    private final Callback<Void> mUnlockCallback = new Callback<Void>() { // from class: com.microsoft.kapp.services.golf.GolfServiceImpl.1
        @Override // com.microsoft.kapp.Callback
        public void onError(Exception ex) {
            GolfServiceImpl.this.mIsGolfCourseSyncing.set(false);
        }

        @Override // com.microsoft.kapp.Callback
        public void callback(Void result) {
            GolfServiceImpl.this.mIsGolfCourseSyncing.set(false);
        }
    };
    private GolfCourseNotificationHandler mNotificationHandler = new GolfCourseNotificationHandlerImpl();

    public GolfServiceImpl(RestService restService, SettingsProvider settingsProvider) {
        this.mRestService = restService;
        this.mSettingsProvider = settingsProvider;
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getTopGolfEvents(int count, boolean expandSequences, Callback<List<GolfEvent>> callback) {
        this.mRestService.getTopGolfEvents(count, expandSequences, callback);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getGolfEventById(String eventId, boolean expandSequences, Callback<GolfEvent> callback) {
        this.mRestService.getGolfEventById(eventId, expandSequences, callback);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getRecentGolfEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<GolfEvent>> callback) {
        this.mRestService.getRecentGolfEvents(startTime, count, expandSequences, callback);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void isPartnerConnected(final Callback<Boolean> callback) {
        Validate.notNull(callback, "callback");
        this.mRestService.getConnectedApps(new Callback<Partners>() { // from class: com.microsoft.kapp.services.golf.GolfServiceImpl.2
            @Override // com.microsoft.kapp.Callback
            public void callback(Partners result) {
                boolean hasTMAG = false;
                if (result != null && result.getPartners() != null) {
                    for (Partner partner : result.getPartners()) {
                        if (PartnerType.TMag.toString().equalsIgnoreCase(partner.getName()) || PartnerType.TaylorMadeGolf.toString().equalsIgnoreCase(partner.getName())) {
                            hasTMAG = true;
                            continue;
                        } else {
                            hasTMAG = false;
                            continue;
                        }
                        if (hasTMAG) {
                            break;
                        }
                    }
                }
                callback.callback(Boolean.valueOf(hasTMAG));
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.w(GolfServiceImpl.TAG, "error while fetching connected apps");
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getCourseDetail(String courseId, final Context context, final Callback<CourseDetails> callback) {
        this.mRestService.getGolfCourseDetail(courseId, new Callback<GolfCourse>() { // from class: com.microsoft.kapp.services.golf.GolfServiceImpl.3
            CourseDetails courseDetails = null;

            @Override // com.microsoft.kapp.Callback
            public void callback(GolfCourse result) {
                if (result != null) {
                    this.courseDetails = new CourseDetails(result, context);
                    callback.callback(this.courseDetails);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getCourseTees(String courseId, final Callback<List<String>> callback) {
        this.mRestService.getGolfCourseDetail(courseId, new Callback<GolfCourse>() { // from class: com.microsoft.kapp.services.golf.GolfServiceImpl.4
            List<String> teeNames = Collections.emptyList();

            @Override // com.microsoft.kapp.Callback
            public void callback(GolfCourse result) {
                if (result != null && result.getTees() != null && result.getTees().length > 0) {
                    GolfTee[] tees = result.getTees();
                    for (GolfTee tee : tees) {
                        this.teeNames.add(tee.getName());
                    }
                    callback.callback(this.teeNames);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getAvailableStates(int regionId, ActivityScopedCallback<GolfStateResponse> activityScopedCallback) {
        this.mRestService.getAvailableTMaGStates(regionId, activityScopedCallback);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getAvailableCoursesForState(int stateId, int numberOfCourses, CourseFilters filters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback) {
        this.mRestService.getGolfCoursesByState(stateId, numberOfCourses, filters, activityScopedCallback);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getAvailableCoursesForRegion(int regionId, CourseFilters filters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback) {
        this.mRestService.getGolfCoursesByRegion(regionId, filters, activityScopedCallback);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getAvailableRegions(ActivityScopedCallback<GolfRegionResponse> activityScopedCallback) {
        this.mRestService.getAvailableTMaGRegions(activityScopedCallback);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getNearbyCourses(double latitude, double longitude, CourseFilters filters, int page, int count, final Callback<GolfSearchResultsModel> callback) {
        this.mRestService.getNearbyCourseList(latitude, longitude, filters, page, count, new Callback<GolfCourseSearchResults>() { // from class: com.microsoft.kapp.services.golf.GolfServiceImpl.5
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfCourseSearchResults result) {
                GolfSearchResultsModel resultModel = new GolfSearchResultsModel();
                resultModel.addSearchResults(result, true);
                callback.callback(resultModel);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void getRecentCourses(final Callback<GolfSearchResultsModel> callback) {
        this.mRestService.getRecentCourseList(new Callback<GolfCourseSearchResults>() { // from class: com.microsoft.kapp.services.golf.GolfServiceImpl.6
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfCourseSearchResults result) {
                GolfSearchResultsModel resultModel = new GolfSearchResultsModel();
                resultModel.addSearchResults(result, false);
                callback.callback(resultModel);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public Pair<String, String> getSyncedGolfCourse() {
        return this.mSettingsProvider.getSyncedGolfCourse();
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public byte[] getDeviceGolfCourse(String courseId, String teeId) throws KRestException {
        return this.mRestService.getDeviceGolfCourse(courseId, teeId);
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void updateLastSyncedGolfCourse(String courseId, String teeId) {
        this.mSettingsProvider.updateLastSyncedGolfCourse(courseId, teeId, DateTime.now());
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public boolean pushGolfCourseDetailToDevice(String courseId, String teeId) {
        Validate.notNullOrEmpty(courseId, "courseId");
        Validate.notNullOrEmpty(teeId, KCloudConstants.GOLF_COURSE_TEE_ID);
        if (this.mIsGolfCourseSyncing.compareAndSet(false, true)) {
            new PushGolfToDeviceOperation(courseId, teeId, this.mNotificationHandler, this.mUnlockCallback, null).execute();
            return true;
        }
        return false;
    }

    @Override // com.microsoft.kapp.services.golf.GolfService
    public void findCoursesByName(String query, CourseFilters filters, int page, int count, ActivityScopedCallback<GolfCourseSearchResults> callback) {
        this.mRestService.getGolfCoursesByName(query, filters, page, count, callback);
    }
}
