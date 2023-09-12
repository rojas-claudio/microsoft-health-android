package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
/* loaded from: classes.dex */
public class InsightMetadata {
    @SerializedName("Class")
    private String mClass;
    @SerializedName("Enabled")
    private boolean mEnabled;
    @SerializedName("ForceDeviceDisplay")
    private boolean mForceDeviceDisplay;
    @SerializedName("ForceDeviceNudge")
    private boolean mForceDeviceNudge;
    @SerializedName("GroupId")
    private int mGroupId;
    @SerializedName("Importance")
    private String mImportance;
    @SerializedName("InsightID")
    private int mInsightID;
    @SerializedName(TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME)
    private String mName;
    @SerializedName("OptOut")
    private boolean mOptOut;
    @SerializedName("ParentInsightID")
    private String mParentInsightID;
    @SerializedName("TimeSensitivity")
    private String mTimeSensitivity;
    @SerializedName("Timeframe")
    private int mTimeframe;
    @SerializedName("Tracker")
    private InsightTracker mTracker;
    @SerializedName("Type")
    private String mType;

    public int getInsightID() {
        return this.mInsightID;
    }

    public void setInsightID(int insightId) {
        this.mInsightID = insightId;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean getEnabled() {
        return this.mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public String getParentInsightID() {
        return this.mParentInsightID;
    }

    public void setParentInsightID(String parentInsightId) {
        this.mParentInsightID = parentInsightId;
    }

    public String getClassValue() {
        return this.mClass;
    }

    public void setClassValue(String classValue) {
        this.mClass = classValue;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getImportance() {
        return this.mImportance;
    }

    public void setImportance(String importance) {
        this.mImportance = importance;
    }

    public String getTimeSensitivity() {
        return this.mTimeSensitivity;
    }

    public void setTimeSensitivity(String timeSensitivity) {
        this.mTimeSensitivity = timeSensitivity;
    }

    public boolean getForceDeviceNudge() {
        return this.mForceDeviceNudge;
    }

    public void setForceDeviceNudge(boolean forceDeviceNudge) {
        this.mForceDeviceNudge = forceDeviceNudge;
    }

    public boolean getForceDeviceDisplay() {
        return this.mForceDeviceDisplay;
    }

    public void setForceDeviceDisplay(boolean forceDeviceDisplay) {
        this.mForceDeviceDisplay = forceDeviceDisplay;
    }

    public int getTimeFrame() {
        return this.mTimeframe;
    }

    public void setTimeFrame(int timeframe) {
        this.mTimeframe = timeframe;
    }

    public boolean getOptOut() {
        return this.mOptOut;
    }

    public void setOptOut(boolean optOut) {
        this.mOptOut = optOut;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public void setGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    public InsightTracker getTracker() {
        return this.mTracker;
    }

    public void setTracker(InsightTracker mTracker) {
        this.mTracker = mTracker;
    }
}
