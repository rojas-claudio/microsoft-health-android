package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class GoalValueTemplateDto implements DeserializedObjectValidation, Parcelable {
    public static final Parcelable.Creator<GoalValueTemplateDto> CREATOR = new Parcelable.Creator<GoalValueTemplateDto>() { // from class: com.microsoft.krestsdk.models.GoalValueTemplateDto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueTemplateDto createFromParcel(Parcel source) {
            return new GoalValueTemplateDto(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueTemplateDto[] newArray(int size) {
            return new GoalValueTemplateDto[size];
        }
    };
    @SerializedName(TelemetryConstants.Events.SendFeedbackComplete.Dimensions.DESCRIPTION)
    private String mDescription;
    @SerializedName(TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME)
    private String mName;
    @SerializedName("Recommended")
    private Object mRecommended;
    @SerializedName("Threshold")
    private Object mThreshold;

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

    public Object getThreshold() {
        return this.mThreshold;
    }

    public void setThreshold(Object threshold) {
        this.mThreshold = threshold;
    }

    public Object getRecommended() {
        return this.mRecommended;
    }

    public void setRecommended(Object recommended) {
        this.mRecommended = recommended;
    }

    @Override // com.microsoft.krestsdk.models.DeserializedObjectValidation
    public void validateDeserializedObject() {
        Validate.notNullOrEmpty(this.mName, TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME);
    }

    public GoalValueTemplateDto() {
    }

    public GoalValueTemplateDto(Parcel source) {
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
        dest.writeDouble(((Double) this.mThreshold).doubleValue());
        dest.writeDouble(((Double) this.mRecommended).doubleValue());
    }

    public void readFromParcel(Parcel in) {
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mThreshold = Double.valueOf(in.readDouble());
        this.mRecommended = Double.valueOf(in.readDouble());
    }
}
