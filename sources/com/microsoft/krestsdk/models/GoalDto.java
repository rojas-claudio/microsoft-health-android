package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class GoalDto extends GoalBaseDto implements Parcelable {
    @SerializedName("Category")
    private CategoryType mCategory;
    @SerializedName("Id")
    private String mId;
    @SerializedName("LastUpdateTime")
    private DateTime mLastUpdateTime;
    @SerializedName("Status")
    private GoalStatus mStatus;
    @SerializedName("Type")
    private GoalType mType;
    @SerializedName("ValueHistory")
    private List<GoalValueHistoryDto> mValueHistory;
    @SerializedName("ValueSummary")
    private List<GoalValueSummaryDto> mValueSummary;
    private static final String TAG = GoalDto.class.getSimpleName();
    public static final Parcelable.Creator<GoalDto> CREATOR = new Parcelable.Creator<GoalDto>() { // from class: com.microsoft.krestsdk.models.GoalDto.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalDto createFromParcel(Parcel in) {
            return new GoalDto(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GoalDto[] newArray(int size) {
            return new GoalDto[size];
        }
    };

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public GoalType getType() {
        return this.mType;
    }

    public void setType(GoalType type) {
        this.mType = type;
    }

    public void setLastUpdateTime(DateTime lastUpdateTime) {
        this.mLastUpdateTime = lastUpdateTime;
    }

    public DateTime getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public List<GoalValueSummaryDto> getValueSummary() {
        if (this.mValueSummary == null) {
            this.mValueSummary = new ArrayList();
        }
        return this.mValueSummary;
    }

    public void setValueSummary(List<GoalValueSummaryDto> valueSummary) {
        this.mValueSummary = valueSummary;
    }

    public List<GoalValueHistoryDto> getValueHistory() {
        if (this.mValueHistory == null) {
            this.mValueHistory = new ArrayList();
        }
        return this.mValueHistory;
    }

    public void setValueHistory(List<GoalValueHistoryDto> valueHistory) {
        this.mValueHistory = valueHistory;
    }

    public GoalStatus getStatus() {
        return this.mStatus;
    }

    public void setStatus(GoalStatus status) {
        this.mStatus = status;
    }

    @Override // com.microsoft.krestsdk.models.GoalBaseDto, com.microsoft.krestsdk.models.DeserializedObjectValidation
    public void validateDeserializedObject() {
        super.validateDeserializedObject();
        Validate.notNullOrEmpty(this.mId, "Id");
        Validate.notNull(this.mType, "Type");
        Validate.notNull(this.mLastUpdateTime, "LastUpdateTime");
        if (this.mValueSummary == null) {
            Validate.notNull(this.mValueHistory, "ValueHistory");
            Validate.isTrue(this.mValueHistory.size() > 0, "ValueHistory should contain at least one item.");
            this.mValueHistory.get(0).validateDeserializedObject();
            return;
        }
        Validate.isTrue(this.mValueSummary.size() > 0, "ValueSummary should at least contain one item.");
        if (this.mValueSummary.size() > 1) {
            KLog.i(TAG, "Encountered a ValueSummary with more than 1 item.");
        }
        this.mValueSummary.get(0).validateDeserializedObject();
    }

    public GoalDto() {
    }

    public GoalDto(Parcel source) {
        super(source);
    }

    @Override // com.microsoft.krestsdk.models.GoalBaseDto, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mId);
        dest.writeInt((this.mType != null ? Integer.valueOf(this.mType.ordinal()) : null).intValue());
        dest.writeLong(this.mLastUpdateTime.getMillis());
        dest.writeTypedList(this.mValueSummary);
        dest.writeTypedList(this.mValueHistory);
        dest.writeInt((this.mCategory != null ? Integer.valueOf(this.mCategory.ordinal()) : null).intValue());
        dest.writeInt((this.mStatus != null ? Integer.valueOf(this.mStatus.ordinal()) : null).intValue());
    }

    @Override // com.microsoft.krestsdk.models.GoalBaseDto
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        this.mId = in.readString();
        int goalTypeOrdinal = in.readInt();
        this.mType = goalTypeOrdinal > -1 ? GoalType.valueOf(goalTypeOrdinal) : null;
        this.mLastUpdateTime = new DateTime(in.readLong());
        this.mValueSummary = new ArrayList();
        in.readTypedList(this.mValueSummary, GoalValueSummaryDto.CREATOR);
        this.mValueHistory = new ArrayList();
        in.readTypedList(this.mValueHistory, GoalValueHistoryDto.CREATOR);
        int goalCategoryOrdinal = in.readInt();
        this.mCategory = goalCategoryOrdinal != -1 ? CategoryType.valueOf(goalCategoryOrdinal) : null;
        int goalStatusOrdinal = in.readInt();
        this.mStatus = goalStatusOrdinal != -1 ? GoalStatus.valueOf(goalStatusOrdinal) : null;
    }
}
