package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GoalValueHistoryDto implements DeserializedObjectValidation, Parcelable {
    public static final Parcelable.Creator<GoalValueHistoryDto> CREATOR = new Parcelable.Creator<GoalValueHistoryDto>() { // from class: com.microsoft.krestsdk.models.GoalValueHistoryDto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueHistoryDto createFromParcel(Parcel source) {
            return new GoalValueHistoryDto(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalValueHistoryDto[] newArray(int size) {
            return new GoalValueHistoryDto[size];
        }
    };
    @SerializedName("HistoryRecords")
    private List<GoalValueRecordDto> mHistoryRecords;
    @SerializedName("HistoryThresholds")
    private List<GoalValueRecordDto> mHistoryThresholds;
    @SerializedName("ValueTemplate")
    private GoalValueTemplateDto mValueTemplate;

    public GoalValueTemplateDto getValueTemplate() {
        return this.mValueTemplate;
    }

    public void setValueTemplate(GoalValueTemplateDto valueTemplate) {
        this.mValueTemplate = valueTemplate;
    }

    public List<GoalValueRecordDto> getHistoryThresholds() {
        if (this.mHistoryThresholds == null) {
            this.mHistoryThresholds = new ArrayList();
        }
        return this.mHistoryThresholds;
    }

    public List<GoalValueRecordDto> getHistoryRecords() {
        if (this.mHistoryRecords == null) {
            this.mHistoryRecords = new ArrayList();
        }
        return this.mHistoryRecords;
    }

    @Override // com.microsoft.krestsdk.models.DeserializedObjectValidation
    public void validateDeserializedObject() {
        Validate.notNull(this.mValueTemplate, "ValueTemplate");
        this.mValueTemplate.validateDeserializedObject();
    }

    public GoalValueHistoryDto() {
    }

    public GoalValueHistoryDto(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mValueTemplate, flags);
        dest.writeTypedList(this.mHistoryThresholds);
        dest.writeTypedList(this.mHistoryRecords);
    }

    public void readFromParcel(Parcel in) {
        this.mValueTemplate = (GoalValueTemplateDto) in.readParcelable(GoalValueTemplateDto.class.getClassLoader());
        this.mHistoryThresholds = new ArrayList();
        in.readTypedList(this.mHistoryThresholds, GoalValueRecordDto.CREATOR);
        this.mHistoryRecords = new ArrayList();
        in.readTypedList(this.mHistoryRecords, GoalValueRecordDto.CREATOR);
    }
}
