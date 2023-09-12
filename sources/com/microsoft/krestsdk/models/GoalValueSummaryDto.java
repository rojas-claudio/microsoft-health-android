package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class GoalValueSummaryDto implements DeserializedObjectValidation, Parcelable {
    public static final Parcelable.Creator<GoalValueSummaryDto> CREATOR = new Parcelable.Creator<GoalValueSummaryDto>() { // from class: com.microsoft.krestsdk.models.GoalValueSummaryDto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueSummaryDto createFromParcel(Parcel source) {
            return new GoalValueSummaryDto(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueSummaryDto[] newArray(int size) {
            return new GoalValueSummaryDto[size];
        }
    };
    @SerializedName("CurrentThreshold")
    private GoalValueRecordDto mCurrentThreshold;
    @SerializedName("CurrentValue")
    private GoalValueCurrent mCurrentValue;
    @SerializedName("ValueTemplate")
    private GoalValueTemplateDto mValueTemplate;

    public GoalValueRecordDto getCurrentThreshold() {
        return this.mCurrentThreshold;
    }

    public void setCurrentThreshold(GoalValueRecordDto currentThreshold) {
        this.mCurrentThreshold = currentThreshold;
    }

    public GoalValueCurrent getCurrentvalue() {
        return this.mCurrentValue;
    }

    public void setCurrentValue(GoalValueCurrent currentValue) {
        this.mCurrentValue = currentValue;
    }

    public GoalValueTemplateDto getValueTemplate() {
        return this.mValueTemplate;
    }

    public void setValueTemplate(GoalValueTemplateDto valueTemplate) {
        this.mValueTemplate = valueTemplate;
    }

    @Override // com.microsoft.krestsdk.models.DeserializedObjectValidation
    public void validateDeserializedObject() {
        Validate.notNull(this.mValueTemplate, "ValueTemplate");
    }

    public GoalValueSummaryDto(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mCurrentThreshold, flags);
        dest.writeParcelable(this.mValueTemplate, flags);
        dest.writeParcelable(this.mCurrentValue, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mCurrentThreshold = (GoalValueRecordDto) in.readParcelable(GoalValueRecordDto.class.getClassLoader());
        this.mValueTemplate = (GoalValueTemplateDto) in.readParcelable(GoalValueTemplateDto.class.getClassLoader());
        this.mCurrentValue = (GoalValueCurrent) in.readParcelable(GoalValueCurrent.class.getClassLoader());
    }
}
