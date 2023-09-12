package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class GoalValueRecordDto implements DeserializedObjectValidation, Parcelable {
    public static final Parcelable.Creator<GoalValueRecordDto> CREATOR = new Parcelable.Creator<GoalValueRecordDto>() { // from class: com.microsoft.krestsdk.models.GoalValueRecordDto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueRecordDto createFromParcel(Parcel source) {
            return new GoalValueRecordDto(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueRecordDto[] newArray(int size) {
            return new GoalValueRecordDto[size];
        }
    };
    @SerializedName("Extension")
    private String mExtension;
    @SerializedName("UpdateTime")
    private DateTime mUpdateTime;
    @SerializedName(TelemetryConstants.Events.ShakeDialogPreferences.Dimensions.VALUE)
    private Object mValue;

    public Object getValue() {
        return this.mValue;
    }

    public void setValue(Object value) {
        this.mValue = value;
    }

    public DateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.mUpdateTime = updateTime;
    }

    public void setExtension(String extension) {
        this.mExtension = extension;
    }

    public String getExtension() {
        return this.mExtension;
    }

    @Override // com.microsoft.krestsdk.models.DeserializedObjectValidation
    public void validateDeserializedObject() {
        Validate.notNull(this.mValue, TelemetryConstants.Events.ShakeDialogPreferences.Dimensions.VALUE);
        Validate.isTrue(this.mValue instanceof Double, "Value data type is not supported");
        Validate.notNull(this.mUpdateTime, "UpdateTime");
    }

    public GoalValueRecordDto() {
    }

    public GoalValueRecordDto(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(((Double) this.mValue).doubleValue());
        dest.writeLong(this.mUpdateTime.getMillis());
        dest.writeString(this.mExtension);
    }

    public void readFromParcel(Parcel in) {
        this.mValue = Double.valueOf(in.readDouble());
        this.mUpdateTime = new DateTime(in.readLong());
        this.mExtension = in.readString();
    }
}
