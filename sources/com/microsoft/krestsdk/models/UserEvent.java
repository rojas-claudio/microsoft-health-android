package com.microsoft.krestsdk.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.krestsdk.services.KCloudConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class UserEvent implements Parcelable {
    public static final String EventTypeJSONMemberName = "EventType";
    @SerializedName("CaloriesBurned")
    private int mCaloriesBurned;
    @SerializedName("CaloriesFromCarbs")
    private int mCaloriesFromCarbs;
    @SerializedName("CaloriesFromFat")
    private int mCaloriesFromFat;
    @SerializedName("DeliveryId")
    private int mDeliveryId;
    @SerializedName("Duration")
    private int mDuration;
    @SerializedName(KCloudConstants.VALUE_TEMPLATE_EVENTID)
    private String mEventId;
    @SerializedName(EventTypeJSONMemberName)
    private EventType mEventType;
    @SerializedName("Evidences")
    private List<Evidence> mEvidences;
    @SerializedName("Info")
    private UserActivity[] mInfo;
    @SerializedName(TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME)
    private String mName;
    @SerializedName("ParentEventId")
    private String mParentEventId;
    @SerializedName("PausedTime")
    private int mPausedTime;
    @SerializedName("PersonalBests")
    private ArrayList<String> mPersonalBestsList;
    @SerializedName("StartTime")
    private DateTime mStartTime;
    @SerializedName("UvExposure")
    private int mUvExposure;
    public static String EVIDENCE_ROLE_PRIMARY = "Primary";
    public static final Parcelable.Creator<UserEvent> CREATOR = new Parcelable.Creator<UserEvent>() { // from class: com.microsoft.krestsdk.models.UserEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserEvent createFromParcel(Parcel in) {
            return new UserEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserEvent[] newArray(int size) {
            return new UserEvent[size];
        }
    };

    public UserEvent() {
    }

    public EventType getEventType() {
        return this.mEventType;
    }

    public void setEventType(EventType eventType) {
        this.mEventType = eventType;
    }

    public String getEventId() {
        return this.mEventId;
    }

    public void setEventId(String eventId) {
        this.mEventId = eventId;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public int getPausedTime() {
        return this.mPausedTime;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public String getParentEventId() {
        return this.mParentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.mParentEventId = parentEventId;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getDeliveryId() {
        return this.mDeliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.mDeliveryId = deliveryId;
    }

    public DateTime getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(DateTime startTime) {
        this.mStartTime = startTime;
    }

    public DateTime getEndTime() {
        return getStartTime().plusSeconds(getDuration()).plusSeconds(getPausedTime());
    }

    public int getCaloriesBurned() {
        return this.mCaloriesBurned;
    }

    public int getCaloriesFromCarbs() {
        return this.mCaloriesFromCarbs;
    }

    public int getCaloriesFromFat() {
        return this.mCaloriesFromFat;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.mCaloriesBurned = caloriesBurned;
    }

    public UserActivity[] getInfo() {
        return this.mInfo;
    }

    public void setInfo(UserActivity[] mInfo) {
        this.mInfo = mInfo;
    }

    public List<Evidence> getEvidences() {
        return this.mEvidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        this.mEvidences = evidences;
    }

    public int getUvExposure() {
        return this.mUvExposure;
    }

    public Spannable getMainMetric(Context context, boolean isMetric) {
        return new SpannableString("");
    }

    public Spannable getMainMetric(Context context, int styleResId, boolean isMetric) {
        return new SpannableString("");
    }

    public List<RaisedInsight> getPrimaryRaisedInsights() {
        List<RaisedInsight> raisedInsights = new ArrayList<>();
        List<Evidence> evidences = getEvidences();
        if (evidences != null) {
            for (Evidence evidence : evidences) {
                RaisedInsight raisedInsight = evidence.getRaisedInsight();
                if (EVIDENCE_ROLE_PRIMARY.equalsIgnoreCase(evidence.getRole())) {
                    raisedInsights.add(raisedInsight);
                }
            }
        }
        return raisedInsights;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UserEvent(Parcel in) {
        int eventTypeOrdinal = in.readInt();
        this.mEventType = eventTypeOrdinal != -1 ? EventType.values()[eventTypeOrdinal] : null;
        this.mEventId = in.readString();
        this.mDuration = in.readInt();
        this.mParentEventId = in.readString();
        this.mName = in.readString();
        this.mDeliveryId = in.readInt();
        this.mStartTime = new DateTime(in.readLong());
        this.mCaloriesBurned = in.readInt();
        this.mCaloriesFromCarbs = in.readInt();
        this.mCaloriesFromFat = in.readInt();
        this.mPersonalBestsList = in.readArrayList(Object.class.getClassLoader());
        this.mInfo = (UserActivity[]) in.createTypedArray(UserActivity.CREATOR);
        this.mEvidences = in.createTypedArrayList(Evidence.CREATOR);
        this.mUvExposure = in.readInt();
    }

    public ArrayList<String> getPersonalBests() {
        return this.mPersonalBestsList;
    }

    public void setPersonalBestsList(ArrayList<String> personalBestsList) {
        this.mPersonalBestsList = personalBestsList;
    }

    public void addPersonalBest(String personalBest) {
        if (this.mPersonalBestsList == null) {
            this.mPersonalBestsList = new ArrayList<>();
        }
        if (!this.mPersonalBestsList.contains(personalBest)) {
            this.mPersonalBestsList.add(personalBest);
        }
    }

    public void addPersonalBests(ArrayList<String> personalBests) {
        Iterator i$ = personalBests.iterator();
        while (i$.hasNext()) {
            String name = i$.next();
            addPersonalBest(name);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mEventType != null ? this.mEventType.ordinal() : -1);
        dest.writeString(this.mEventId);
        dest.writeInt(this.mDuration);
        dest.writeString(this.mParentEventId);
        dest.writeString(this.mName);
        dest.writeInt(this.mDeliveryId);
        dest.writeLong(this.mStartTime.getMillis());
        dest.writeInt(this.mCaloriesBurned);
        dest.writeInt(this.mCaloriesFromCarbs);
        dest.writeInt(this.mCaloriesFromFat);
        dest.writeList(this.mPersonalBestsList);
        dest.writeTypedArray(this.mInfo, flags);
        dest.writeTypedList(this.mEvidences);
        dest.writeInt(this.mUvExposure);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
