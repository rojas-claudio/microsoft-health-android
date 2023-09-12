package com.microsoft.kapp.mocks;

import android.os.AsyncTask;
import com.google.gson.JsonObject;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.models.golf.GolfRegionResponse;
import com.microsoft.kapp.models.golf.GolfStateResponse;
import com.microsoft.kapp.services.golf.CourseFilters;
import com.microsoft.krestsdk.models.AppConfigInfo;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.CategoryType;
import com.microsoft.krestsdk.models.ConnectedDevice;
import com.microsoft.krestsdk.models.CurrentGuidedWorkout;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.FeaturedWorkout;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalOperationAddDto;
import com.microsoft.krestsdk.models.GoalOperationResultDto;
import com.microsoft.krestsdk.models.GoalOperationUpdateDto;
import com.microsoft.krestsdk.models.GoalTemplateDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GolfCourse;
import com.microsoft.krestsdk.models.GolfCourseSearchResults;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.InsightMetadata;
import com.microsoft.krestsdk.models.Partners;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.models.UserProfile;
import com.microsoft.krestsdk.models.sensor.SensorData;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.RaisedInsightQuery;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class MockRestService implements RestService {
    @Override // com.microsoft.krestsdk.services.RestService
    public void getDailySummaries(LocalDate startDate, LocalDate endDate, final Callback<List<UserDailySummary>> callback) {
        DummyTask task = new DummyTask(new Callback<String>() { // from class: com.microsoft.kapp.mocks.MockRestService.1
            @Override // com.microsoft.kapp.Callback
            public void callback(String response) {
                callback.callback(null);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
        task.execute(new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getUserProfile(final Callback<UserProfile> callback) {
        DummyTask task = new DummyTask(new Callback<String>() { // from class: com.microsoft.kapp.mocks.MockRestService.2
            @Override // com.microsoft.kapp.Callback
            public void callback(String response) {
                callback.callback(null);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
        task.execute(new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getEventsSince(DateTime time, boolean expandMapPoints, Callback<List<UserEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getSleepEvents(LocalDate startDayId, LocalDate endDayId, Callback<List<SleepEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void updateUserProfile(UserProfile updatedProfile, Callback<Void> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentSleepEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<SleepEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopSleepEvents(int count, boolean expandSequences, Callback<List<SleepEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopRunEvents(boolean isMetric, int count, Callback<List<RunEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentRunEvents(boolean isMetric, DateTime startTime, int count, Callback<List<RunEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRunEventById(boolean isMetric, String eventId, ArrayList<RestService.ExpandType> expandTypes, Callback<RunEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopExerciseEvents(int count, boolean expandSequences, Callback<List<ExerciseEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopGuidedWorkoutEvents(int count, boolean expandSequencesfinal, Callback<List<GuidedWorkoutEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentGuidedWorkoutEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<GuidedWorkoutEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentExerciseEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<ExerciseEvent>> callback) {
    }

    public void getExerciseEventsByDate(DateTime startDate, DateTime endDate, Callback<List<ExerciseEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getExerciseEventById(String eventId, ArrayList<RestService.ExpandType> expandTypes, Callback<ExerciseEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getUserEventById(String eventId, Callback<UserEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGuidedWorkoutEventById(String eventId, Callback<GuidedWorkoutEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getBestGoalsByCategory(CategoryType category, Callback<List<GoalDto>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getUserActivitiesByHour(LocalDate date, final Callback<List<UserActivity>> callback) {
        DummyTask task = new DummyTask(new Callback<String>() { // from class: com.microsoft.kapp.mocks.MockRestService.3
            @Override // com.microsoft.kapp.Callback
            public void callback(String response) {
                if (callback != null) {
                    callback.callback(null);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                if (callback != null) {
                    callback.onError(ex);
                }
            }
        });
        task.execute(new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getInsights(Callback<List<InsightMetadata>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRaisedInsights(Callback<List<RaisedInsight>> raisedInsightCallback, RaisedInsightQuery query) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGoalTemplates(Callback<List<GoalTemplateDto>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void addGoal(GoalOperationAddDto operationAdd, Callback<GoalOperationResultDto> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void addGoals(ArrayList<GoalOperationAddDto> operations, Callback<GoalOperationResultDto> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void updateGoal(GoalOperationUpdateDto operationUpdate, Callback<GoalOperationResultDto> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void deleteGoal(String goalId, Callback<Void> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void deleteEvent(String eventId, Callback<Void> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGoals(Callback<List<GoalDto>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getWorkoutPlanSchedules(String workoutPlanId, int workoutInstanceID, String locale, Callback<List<ScheduledWorkout>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<ScheduledWorkout> getWorkoutPlanSchedules(String workoutPlanId, int workoutInstanceId, String locale) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getWorkoutPlanSchedules(String workoutPlanId, int workoutInstanceId, Callback<List<ScheduledWorkout>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getWorkoutPlanSchedules(String workoutPlanId, Callback<List<ScheduledWorkout>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public byte[] getDeviceWorkout(String workoutPlanId, int workoutPlanInstanceId, int workoutIndex, int day, int week, String locale) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getDeviceWorkout(String workoutPlanId, int workoutPlanInstanceId, int workoutIndex, int day, int week, String locale, Callback callback) throws KRestException {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getFavoriteWorkoutPlans(Callback<List<FavoriteWorkoutPlan>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void addFavoriteWorkoutPlan(String workoutPlanId, Callback callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void removeFavoriteWorkoutPlan(String workoutPlanId, Callback callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void subscribeToWorkoutPlan(String workoutPlanId, Callback callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void unsubscribeFromWorkoutPlan(String workoutPlanId, Callback callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getSubscribedWorkoutPlan(Callback<FavoriteWorkoutPlan> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void skipWorkout(String workoutPlanId, int workoutIndex, int day, int week, Callback callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<FavoriteWorkoutPlan> getFavoriteWorkoutPlans() throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public FavoriteWorkoutPlan getSubscribedWorkoutPlan() throws KRestException {
        return null;
    }

    /* loaded from: classes.dex */
    private static class DummyTask extends AsyncTask<String, Void, String> {
        private Callback<String> mCallback;

        public DummyTask(Callback<String> callback) {
            this.mCallback = callback;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public String doInBackground(String... params) {
            try {
                Thread.sleep(1000L);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String result) {
            super.onPostExecute((DummyTask) result);
            this.mCallback.callback("");
        }
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getSleepEventById(String eventId, ArrayList<RestService.ExpandType> expandTypes, Callback<SleepEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopRunEvents(boolean isMetric, int count, boolean expandSequencesAndMappoints, Callback<List<RunEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGoals(Callback<List<GoalDto>> callback, boolean isHistory) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getActiveCaloriesGoals(Callback<List<GoalDto>> callback, boolean isHistory) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getActiveStepsGoals(Callback<List<GoalDto>> callback, boolean isHistory) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getActiveGoalByType(Callback<List<GoalDto>> callback, boolean isHistory, CategoryType category, GoalType type) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopUserEvents(int count, Callback<List<UserEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentUserEvents(DateTime startTime, int count, Callback<List<UserEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void nameEvent(String eventId, JsonObject nameOfEvent, Callback<Void> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getFeaturedWorkouts(int age, String gender, final Callback<List<FeaturedWorkout>> callback) {
        DummyTask task = new DummyTask(new Callback<String>() { // from class: com.microsoft.kapp.mocks.MockRestService.4
            @Override // com.microsoft.kapp.Callback
            public void callback(String response) {
                callback.callback(null);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        });
        task.execute(new String[0]);
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getPostGuidedWorkoutDetails(Callback<List<GuidedWorkoutEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGuidedWorkoutEventById(String eventId, ArrayList<RestService.ExpandType> expandTypes, Callback<GuidedWorkoutEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGuidedWorkoutEventById(String eventId, boolean hasSequences, Callback<GuidedWorkoutEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void updateLastSyncedWorkout(String workoutPlanId, int workoutPlanInstanceId, int workoutIndex, int day, int week) throws KRestException {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void updateLastSyncedWorkoutManual(String workoutPlanId, int workoutIndex, int day, int week) throws KRestException {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getLastSyncedWorkout(Callback<SyncedWorkoutInfo> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public SyncedWorkoutInfo getLastSyncedWorkout() throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String addFavoriteWorkoutPlan(String workoutPlanId) throws KRestException {
        return workoutPlanId;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String unsubscribeFromWorkoutPlan(String workoutPlanId) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String removeFavoriteWorkoutPlan(String workoutPlanId) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String subscribeToWorkoutPlan(String workoutPlanId) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<ScheduledWorkout> getWorkoutPlanSchedules(String workoutPlanId, String locale) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public GuidedWorkoutEvent getPostGuidedWorkoutDetails() throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<ScheduledWorkout> getWorkoutPlanSchedules(String workoutPlanId, int workoutInstanceId) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public List<ScheduledWorkout> getWorkoutPlanSchedules(String workoutPlanId) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopBikeEvents(boolean isMetric, int count, boolean expandSequencesAndMappoints, Callback<List<BikeEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentBikeEvents(boolean isMetric, DateTime startTime, int count, Callback<List<BikeEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getBikeEventById(boolean isMetric, String eventId, ArrayList<RestService.ExpandType> expandTypes, Callback<BikeEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public String uploadSensorData(SensorData sensorData) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void addDevice(ConnectedDevice device) throws KRestException {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopGolfEvents(int count, ArrayList<RestService.ExpandType> expandTypes, Callback<List<GolfEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfEventById(String eventId, ArrayList<RestService.ExpandType> expandTypes, Callback<GolfEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getTopGolfEvents(int count, boolean expandSequences, Callback<List<GolfEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfEventById(String eventId, boolean expandSequences, Callback<GolfEvent> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentGolfEvents(DateTime startTime, int count, boolean expandSequences, Callback<List<GolfEvent>> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfCourseDetail(String courseId, Callback<GolfCourse> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getNearbyCourseList(double latitude, double longitude, CourseFilters filters, int page, int count, Callback<GolfCourseSearchResults> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getConnectedApps(Callback<Partners> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getRecentCourseList(Callback<GolfCourseSearchResults> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getAvailableTMaGStates(int regionId, ActivityScopedCallback<GolfStateResponse> activityScopedCallback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getAvailableTMaGRegions(ActivityScopedCallback<GolfRegionResponse> activityScopedCallback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfCoursesByState(int stateId, int numberOfCourses, CourseFilters filters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfCoursesByRegion(int regionId, CourseFilters filters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getGolfCoursesByName(String name, CourseFilters filters, int page, int count, Callback<GolfCourseSearchResults> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public byte[] getDeviceGolfCourse(String courseId, String teeId) throws KRestException {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public CurrentGuidedWorkout getCurrentGuidedWorkout() {
        return null;
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void downloadAppConfiguration(AppConfigInfo appConfigInfo, Callback<Void> callback) {
    }

    @Override // com.microsoft.krestsdk.services.RestService
    public void getAppConfigurationInfo(String fusEndPoint, Callback<AppConfigInfo> callback) {
    }
}
