package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dm;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class ActivityRecognitionResult implements SafeParcelable {
    public static final ActivityRecognitionResultCreator CREATOR = new ActivityRecognitionResultCreator();
    public static final String EXTRA_ACTIVITY_RESULT = "com.google.android.location.internal.EXTRA_ACTIVITY_RESULT";
    private final int iM;
    List<DetectedActivity> ov;
    long ow;
    long ox;

    public ActivityRecognitionResult(int versionCode, List<DetectedActivity> probableActivities, long timeMillis, long elapsedRealtimeMillis) {
        this.iM = 1;
        this.ov = probableActivities;
        this.ow = timeMillis;
        this.ox = elapsedRealtimeMillis;
    }

    public ActivityRecognitionResult(DetectedActivity mostProbableActivity, long time, long elapsedRealtimeMillis) {
        this(Collections.singletonList(mostProbableActivity), time, elapsedRealtimeMillis);
    }

    public ActivityRecognitionResult(List<DetectedActivity> probableActivities, long time, long elapsedRealtimeMillis) {
        dm.b(probableActivities != null && probableActivities.size() > 0, "Must have at least 1 detected activity");
        this.iM = 1;
        this.ov = probableActivities;
        this.ow = time;
        this.ox = elapsedRealtimeMillis;
    }

    public static ActivityRecognitionResult extractResult(Intent intent) {
        if (hasResult(intent)) {
            return (ActivityRecognitionResult) intent.getExtras().get(EXTRA_ACTIVITY_RESULT);
        }
        return null;
    }

    public static boolean hasResult(Intent intent) {
        if (intent == null) {
            return false;
        }
        return intent.hasExtra(EXTRA_ACTIVITY_RESULT);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getActivityConfidence(int activityType) {
        for (DetectedActivity detectedActivity : this.ov) {
            if (detectedActivity.getType() == activityType) {
                return detectedActivity.getConfidence();
            }
        }
        return 0;
    }

    public long getElapsedRealtimeMillis() {
        return this.ox;
    }

    public DetectedActivity getMostProbableActivity() {
        return this.ov.get(0);
    }

    public List<DetectedActivity> getProbableActivities() {
        return this.ov;
    }

    public long getTime() {
        return this.ow;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public String toString() {
        return "ActivityRecognitionResult [probableActivities=" + this.ov + ", timeMillis=" + this.ow + ", elapsedRealtimeMillis=" + this.ox + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        ActivityRecognitionResultCreator.a(this, out, flags);
    }
}
