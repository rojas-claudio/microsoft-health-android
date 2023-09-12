package com.microsoft.krestsdk.services;

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
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public interface RestService {
    public static final int HTTP_ERROR_CODE_UNAUTHORIZE = 401;
    public static final String SIGN_IN_REQUIRED_INTENT = "Kapp.signin";

    void addDevice(ConnectedDevice connectedDevice) throws KRestException;

    String addFavoriteWorkoutPlan(String str) throws KRestException;

    void addFavoriteWorkoutPlan(String str, Callback<Void> callback);

    void addGoal(GoalOperationAddDto goalOperationAddDto, Callback<GoalOperationResultDto> callback);

    void addGoals(ArrayList<GoalOperationAddDto> arrayList, Callback<GoalOperationResultDto> callback);

    void deleteEvent(String str, Callback<Void> callback);

    void deleteGoal(String str, Callback<Void> callback);

    void downloadAppConfiguration(AppConfigInfo appConfigInfo, Callback<Void> callback);

    void getActiveCaloriesGoals(Callback<List<GoalDto>> callback, boolean z);

    void getActiveGoalByType(Callback<List<GoalDto>> callback, boolean z, CategoryType categoryType, GoalType goalType);

    void getActiveStepsGoals(Callback<List<GoalDto>> callback, boolean z);

    void getAppConfigurationInfo(String str, Callback<AppConfigInfo> callback);

    void getAvailableTMaGRegions(ActivityScopedCallback<GolfRegionResponse> activityScopedCallback);

    void getAvailableTMaGStates(int i, ActivityScopedCallback<GolfStateResponse> activityScopedCallback);

    void getBestGoalsByCategory(CategoryType categoryType, Callback<List<GoalDto>> callback);

    void getBikeEventById(boolean z, String str, ArrayList<ExpandType> arrayList, Callback<BikeEvent> callback);

    void getConnectedApps(Callback<Partners> callback);

    CurrentGuidedWorkout getCurrentGuidedWorkout() throws KRestException;

    void getDailySummaries(LocalDate localDate, LocalDate localDate2, Callback<List<UserDailySummary>> callback);

    byte[] getDeviceGolfCourse(String str, String str2) throws KRestException;

    void getDeviceWorkout(String str, int i, int i2, int i3, int i4, String str2, Callback<byte[]> callback) throws KRestException;

    byte[] getDeviceWorkout(String str, int i, int i2, int i3, int i4, String str2) throws KRestException;

    void getEventsSince(DateTime dateTime, boolean z, Callback<List<UserEvent>> callback);

    void getExerciseEventById(String str, ArrayList<ExpandType> arrayList, Callback<ExerciseEvent> callback);

    List<FavoriteWorkoutPlan> getFavoriteWorkoutPlans() throws KRestException;

    void getFavoriteWorkoutPlans(Callback<List<FavoriteWorkoutPlan>> callback);

    void getFeaturedWorkouts(int i, String str, Callback<List<FeaturedWorkout>> callback);

    void getGoalTemplates(Callback<List<GoalTemplateDto>> callback);

    void getGoals(Callback<List<GoalDto>> callback);

    void getGoals(Callback<List<GoalDto>> callback, boolean z);

    void getGolfCourseDetail(String str, Callback<GolfCourse> callback);

    void getGolfCoursesByName(String str, CourseFilters courseFilters, int i, int i2, Callback<GolfCourseSearchResults> callback);

    void getGolfCoursesByRegion(int i, CourseFilters courseFilters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback);

    void getGolfCoursesByState(int i, int i2, CourseFilters courseFilters, ActivityScopedCallback<GolfCourseSearchResults> activityScopedCallback);

    void getGolfEventById(String str, ArrayList<ExpandType> arrayList, Callback<GolfEvent> callback);

    void getGolfEventById(String str, boolean z, Callback<GolfEvent> callback);

    void getGuidedWorkoutEventById(String str, Callback<GuidedWorkoutEvent> callback);

    void getGuidedWorkoutEventById(String str, ArrayList<ExpandType> arrayList, Callback<GuidedWorkoutEvent> callback);

    void getGuidedWorkoutEventById(String str, boolean z, Callback<GuidedWorkoutEvent> callback);

    void getInsights(Callback<List<InsightMetadata>> callback);

    SyncedWorkoutInfo getLastSyncedWorkout() throws KRestException;

    void getLastSyncedWorkout(Callback<SyncedWorkoutInfo> callback);

    void getNearbyCourseList(double d, double d2, CourseFilters courseFilters, int i, int i2, Callback<GolfCourseSearchResults> callback);

    GuidedWorkoutEvent getPostGuidedWorkoutDetails() throws KRestException;

    void getPostGuidedWorkoutDetails(Callback<List<GuidedWorkoutEvent>> callback);

    void getRaisedInsights(Callback<List<RaisedInsight>> callback, RaisedInsightQuery raisedInsightQuery);

    void getRecentBikeEvents(boolean z, DateTime dateTime, int i, Callback<List<BikeEvent>> callback);

    void getRecentCourseList(Callback<GolfCourseSearchResults> callback);

    void getRecentExerciseEvents(DateTime dateTime, int i, boolean z, Callback<List<ExerciseEvent>> callback);

    void getRecentGolfEvents(DateTime dateTime, int i, boolean z, Callback<List<GolfEvent>> callback);

    void getRecentGuidedWorkoutEvents(DateTime dateTime, int i, boolean z, Callback<List<GuidedWorkoutEvent>> callback);

    void getRecentRunEvents(boolean z, DateTime dateTime, int i, Callback<List<RunEvent>> callback);

    void getRecentSleepEvents(DateTime dateTime, int i, boolean z, Callback<List<SleepEvent>> callback);

    void getRecentUserEvents(DateTime dateTime, int i, Callback<List<UserEvent>> callback);

    void getRunEventById(boolean z, String str, ArrayList<ExpandType> arrayList, Callback<RunEvent> callback);

    void getSleepEventById(String str, ArrayList<ExpandType> arrayList, Callback<SleepEvent> callback);

    void getSleepEvents(LocalDate localDate, LocalDate localDate2, Callback<List<SleepEvent>> callback);

    FavoriteWorkoutPlan getSubscribedWorkoutPlan() throws KRestException;

    void getSubscribedWorkoutPlan(Callback<FavoriteWorkoutPlan> callback);

    void getTopBikeEvents(boolean z, int i, boolean z2, Callback<List<BikeEvent>> callback);

    void getTopExerciseEvents(int i, boolean z, Callback<List<ExerciseEvent>> callback);

    void getTopGolfEvents(int i, ArrayList<ExpandType> arrayList, Callback<List<GolfEvent>> callback);

    void getTopGolfEvents(int i, boolean z, Callback<List<GolfEvent>> callback);

    void getTopGuidedWorkoutEvents(int i, boolean z, Callback<List<GuidedWorkoutEvent>> callback);

    void getTopRunEvents(boolean z, int i, Callback<List<RunEvent>> callback);

    void getTopRunEvents(boolean z, int i, boolean z2, Callback<List<RunEvent>> callback);

    void getTopSleepEvents(int i, boolean z, Callback<List<SleepEvent>> callback);

    void getTopUserEvents(int i, Callback<List<UserEvent>> callback);

    void getUserActivitiesByHour(LocalDate localDate, Callback<List<UserActivity>> callback);

    void getUserEventById(String str, Callback<UserEvent> callback);

    void getUserProfile(Callback<UserProfile> callback);

    List<ScheduledWorkout> getWorkoutPlanSchedules(String str) throws KRestException;

    List<ScheduledWorkout> getWorkoutPlanSchedules(String str, int i) throws KRestException;

    List<ScheduledWorkout> getWorkoutPlanSchedules(String str, int i, String str2) throws KRestException;

    List<ScheduledWorkout> getWorkoutPlanSchedules(String str, String str2) throws KRestException;

    void getWorkoutPlanSchedules(String str, int i, Callback<List<ScheduledWorkout>> callback);

    void getWorkoutPlanSchedules(String str, int i, String str2, Callback<List<ScheduledWorkout>> callback);

    void getWorkoutPlanSchedules(String str, Callback<List<ScheduledWorkout>> callback);

    void nameEvent(String str, JsonObject jsonObject, Callback<Void> callback);

    String removeFavoriteWorkoutPlan(String str) throws KRestException;

    void removeFavoriteWorkoutPlan(String str, Callback<Void> callback);

    void skipWorkout(String str, int i, int i2, int i3, Callback<Void> callback);

    String subscribeToWorkoutPlan(String str) throws KRestException;

    void subscribeToWorkoutPlan(String str, Callback<String[]> callback);

    String unsubscribeFromWorkoutPlan(String str) throws KRestException;

    void unsubscribeFromWorkoutPlan(String str, Callback<Void> callback);

    void updateGoal(GoalOperationUpdateDto goalOperationUpdateDto, Callback<GoalOperationResultDto> callback);

    void updateLastSyncedWorkout(String str, int i, int i2, int i3, int i4) throws KRestException;

    void updateLastSyncedWorkoutManual(String str, int i, int i2, int i3) throws KRestException;

    void updateUserProfile(UserProfile userProfile, Callback<Void> callback);

    String uploadSensorData(SensorData sensorData) throws KRestException;

    /* loaded from: classes.dex */
    public enum ExpandType {
        SEQUENCES("Sequences"),
        MAPPOINTS("MapPoints"),
        INFO("Info"),
        EVIDENCE("Evidences");
        
        public String name;

        ExpandType(String typeName) {
            this.name = typeName;
        }
    }
}
