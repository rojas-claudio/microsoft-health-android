package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import java.util.Locale;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class GoalBaseDto implements DeserializedObjectValidation, Parcelable {
    @SerializedName(TelemetryConstants.Events.SendFeedbackComplete.Dimensions.DESCRIPTION)
    private String mDescription;
    @SerializedName("EndTime")
    private DateTime mEndTime;
    @SerializedName(TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME)
    private String mName;
    @SerializedName("StartTime")
    private DateTime mStartTime;
    private static final String TAG = GoalBaseDto.class.getSimpleName();
    public static final Parcelable.Creator<GoalBaseDto> CREATOR = new Parcelable.Creator<GoalBaseDto>() { // from class: com.microsoft.krestsdk.models.GoalBaseDto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalBaseDto createFromParcel(Parcel source) {
            return new GoalBaseDto(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalBaseDto[] newArray(int size) {
            return new GoalBaseDto[size];
        }
    };

    /* loaded from: classes.dex */
    public enum BestEventType {
        Unknown,
        FastestPaceRun,
        FurthestRun,
        MostCalorieRun,
        FastestSplitRun,
        MostCalorieWorkout,
        FurthestBikeRide,
        FastestBikeRide,
        MostCaloriesRide,
        LargestGain,
        LongestDurationWorkout
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public DateTime getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(DateTime startTime) {
        this.mStartTime = startTime;
    }

    public DateTime getEndTime() {
        return this.mEndTime;
    }

    public void setEndTime(DateTime endTime) {
        this.mEndTime = endTime;
    }

    public BestEventType getPersonalBestType() {
        String name = this.mName.toLowerCase(Locale.US);
        BestEventType type = BestEventType.Unknown;
        if (name.contains("run pace")) {
            BestEventType type2 = BestEventType.FastestPaceRun;
            return type2;
        } else if (name.contains("furthest run")) {
            BestEventType type3 = BestEventType.FurthestRun;
            return type3;
        } else if (name.contains("run split")) {
            BestEventType type4 = BestEventType.FastestSplitRun;
            return type4;
        } else if (name.contains("duration exercise")) {
            BestEventType type5 = BestEventType.LongestDurationWorkout;
            return type5;
        } else if (name.contains("burned exercise")) {
            BestEventType type6 = BestEventType.MostCalorieWorkout;
            return type6;
        } else if (name.contains("run split")) {
            BestEventType type7 = BestEventType.FastestSplitRun;
            return type7;
        } else if (name.contains("fastest speed ride")) {
            BestEventType type8 = BestEventType.FastestBikeRide;
            return type8;
        } else if (name.contains("furthest ride")) {
            BestEventType type9 = BestEventType.FurthestBikeRide;
            return type9;
        } else if (name.contains("largest elevation gain ride")) {
            BestEventType type10 = BestEventType.LargestGain;
            return type10;
        } else if (name.contains("most calories burned ride")) {
            BestEventType type11 = BestEventType.MostCaloriesRide;
            return type11;
        } else if (name.contains("burned run")) {
            BestEventType type12 = BestEventType.MostCalorieRun;
            return type12;
        } else {
            KLog.e(TAG, "Unmapped personal best type found! ");
            KLog.logPrivate(TAG, "Goal Name: " + this.mName);
            return type;
        }
    }

    @Override // com.microsoft.krestsdk.models.DeserializedObjectValidation
    public void validateDeserializedObject() {
        Validate.notNullOrEmpty(this.mName, TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME);
        Validate.notNull(this.mStartTime, "StartTime");
        Validate.notNull(this.mEndTime, "EndTime");
        Validate.isTrue(this.mStartTime.isBefore(this.mEndTime), "StartTime must be before EndTime.");
    }

    public GoalBaseDto() {
    }

    public GoalBaseDto(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mDescription);
        dest.writeLong(this.mStartTime.getMillis());
        dest.writeLong(this.mEndTime.getMillis());
    }

    public void readFromParcel(Parcel in) {
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mStartTime = new DateTime(in.readLong());
        this.mEndTime = new DateTime(in.readLong());
    }
}
