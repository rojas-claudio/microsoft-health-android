package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class RaisedInsight implements Parcelable {
    public static final Parcelable.Creator<RaisedInsight> CREATOR = new Parcelable.Creator<RaisedInsight>() { // from class: com.microsoft.krestsdk.models.RaisedInsight.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RaisedInsight createFromParcel(Parcel in) {
            return new RaisedInsight(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RaisedInsight[] newArray(int size) {
            return new RaisedInsight[size];
        }
    };
    @SerializedName("Acknowledged")
    private boolean mAcknowledged;
    @SerializedName("CategoryPivot")
    private List<String> mCategoryPivot;
    @SerializedName("ComparisonPivot")
    private String mComparisonPivot;
    @SerializedName("DataUsedPivot")
    private List<String> mDataUsedPivot;
    @SerializedName("DeviceLine1_Help")
    private String mDeviceLine1_Help;
    @SerializedName("DeviceLine1_Msg")
    private String mDeviceLine1_Msg;
    @SerializedName("DeviceLine2_Help")
    private String mDeviceLine2_Help;
    @SerializedName("DeviceLine2_Msg")
    private String mDeviceLine2_Msg;
    @SerializedName("EffectiveDT")
    private DateTime mEffectiveDT;
    @SerializedName("ExpirationDT")
    private DateTime mExpirationDT;
    @SerializedName("Expired")
    private boolean mExpired;
    @SerializedName("IM_Action_Msg")
    private String mIMActionMsg;
    @SerializedName("IM_Help")
    private String mIM_Help;
    @SerializedName("IM_Msg")
    private String mIM_Msg;
    @SerializedName("InsightEvidence")
    private List<InsightEvidence> mInsightEvidence;
    @SerializedName("InsightId")
    private int mInsightId;
    @SerializedName("NoteworthyPivot")
    private String mNoteworthyPivot;
    @SerializedName("RaisedInsightId")
    private String mRaisedInsightId;
    @SerializedName("ScopePivot")
    private String mScopePivot;
    @SerializedName("TimespanPivot")
    private String mTimespanPivot;
    @SerializedName("TonePivot")
    private String mTonePivot;

    /* loaded from: classes.dex */
    public enum Pivot {
        STEPS(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_STEPS),
        CALORIES(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_CALORIES),
        DAY("Day"),
        WEEK("Week"),
        CAUTION("Caution"),
        WORSE("Worse"),
        SELF("Self"),
        PEOPLE_LIKE_YOU("PeopleLikeYou"),
        GLOBAL("Global");
        
        private String mPivot;

        Pivot(String pivot) {
            this.mPivot = pivot;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mPivot;
        }
    }

    public String getRaisedInsightId() {
        return this.mRaisedInsightId;
    }

    public void setRaisedInsightId(String raisedInsightId) {
        this.mRaisedInsightId = raisedInsightId;
    }

    public int getInsightId() {
        return this.mInsightId;
    }

    public void setInsightId(int insightId) {
        this.mInsightId = insightId;
    }

    public boolean getExpired() {
        return this.mExpired;
    }

    public void setExpired(boolean expired) {
        this.mExpired = expired;
    }

    public boolean getAcknowledged() {
        return this.mAcknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.mAcknowledged = acknowledged;
    }

    public DateTime getEffectiveDT() {
        return this.mEffectiveDT;
    }

    public void setEffectiveDT(DateTime effectiveDT) {
        this.mEffectiveDT = effectiveDT;
    }

    public String getIMMsg() {
        return this.mIM_Msg;
    }

    public void setIMMsg(String imMsg) {
        this.mIM_Msg = imMsg;
    }

    public String getIMHelp() {
        return this.mIM_Help;
    }

    public void setIMHelp(String imHelp) {
        this.mIM_Help = imHelp;
    }

    public String getIMActionMsg() {
        return this.mIMActionMsg;
    }

    public void setIMActionMsg(String IMActionMsg) {
        this.mIMActionMsg = IMActionMsg;
    }

    public String getDeviceLine1Msg() {
        return this.mDeviceLine1_Msg;
    }

    public void setDeviceLine1Msg(String deviceLine1Msg) {
        this.mDeviceLine1_Msg = deviceLine1Msg;
    }

    public String getDeviceLine1Help() {
        return this.mDeviceLine1_Help;
    }

    public void setDeviceLine1Help(String deviceLine1Help) {
        this.mDeviceLine1_Help = deviceLine1Help;
    }

    public String getDeviceLine2Msg() {
        return this.mDeviceLine2_Msg;
    }

    public void setDeviceLine2Msg(String deviceLine2Msg) {
        this.mDeviceLine2_Msg = deviceLine2Msg;
    }

    public String getDeviceLine2Help() {
        return this.mDeviceLine2_Help;
    }

    public void setDeviceLine2Help(String deviceLine2Help) {
        this.mDeviceLine2_Help = deviceLine2Help;
    }

    public List<InsightEvidence> getInsightEvidence() {
        return this.mInsightEvidence;
    }

    public void setInsightEvidence(List<InsightEvidence> insightEvidence) {
        this.mInsightEvidence = insightEvidence;
    }

    public List<String> getDataUsedPivot() {
        return this.mDataUsedPivot;
    }

    public void setDataUsedPivot(List<String> dataUsedPivot) {
        this.mDataUsedPivot = dataUsedPivot;
    }

    public String getComparisonPivot() {
        return this.mComparisonPivot;
    }

    public void setComparisonPivot(String comparisonPivot) {
        this.mComparisonPivot = comparisonPivot;
    }

    public List<String> getCategoryPivot() {
        return this.mCategoryPivot;
    }

    public void setCategoryPivot(List<String> categoryPivot) {
        this.mCategoryPivot = categoryPivot;
    }

    public String getTimespanPivot() {
        return this.mTimespanPivot;
    }

    public void setTimespanPivot(String timespanPivot) {
        this.mTimespanPivot = timespanPivot;
    }

    public String getTonePivot() {
        return this.mTonePivot;
    }

    public void setTonePivot(String tonePivot) {
        this.mTonePivot = tonePivot;
    }

    public String getScopePivot() {
        return this.mScopePivot;
    }

    public void setScopePivot(String scopePivot) {
        this.mScopePivot = scopePivot;
    }

    public String getNoteworthyPivot() {
        return this.mNoteworthyPivot;
    }

    public void setNoteworthyPivot(String noteworthyPivot) {
        this.mNoteworthyPivot = noteworthyPivot;
    }

    public DateTime getExpirationDT() {
        return this.mExpirationDT;
    }

    public void setExpirationDT(DateTime expirationDT) {
        this.mExpirationDT = expirationDT;
    }

    public RaisedInsight() {
    }

    protected RaisedInsight(Parcel in) {
        this.mRaisedInsightId = in.readString();
        this.mInsightId = in.readInt();
        this.mExpired = in.readByte() == 1;
        this.mAcknowledged = in.readByte() == 1;
        this.mEffectiveDT = new DateTime(in.readLong());
        this.mIM_Msg = in.readString();
        this.mIM_Help = in.readString();
        this.mDeviceLine1_Msg = in.readString();
        this.mDeviceLine1_Help = in.readString();
        this.mDeviceLine2_Msg = in.readString();
        this.mDeviceLine2_Help = in.readString();
        this.mInsightEvidence = in.createTypedArrayList(InsightEvidence.CREATOR);
        in.readStringList(this.mDataUsedPivot);
        this.mComparisonPivot = in.readString();
        in.readStringList(this.mCategoryPivot);
        this.mTimespanPivot = in.readString();
        this.mTonePivot = in.readString();
        this.mScopePivot = in.readString();
        this.mNoteworthyPivot = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mRaisedInsightId);
        dest.writeInt(this.mInsightId);
        dest.writeByte((byte) (this.mExpired ? 1 : 0));
        dest.writeByte((byte) (this.mAcknowledged ? 1 : 0));
        dest.writeLong(this.mEffectiveDT.getMillis());
        dest.writeString(this.mIM_Msg);
        dest.writeString(this.mIM_Help);
        dest.writeString(this.mDeviceLine1_Msg);
        dest.writeString(this.mDeviceLine1_Help);
        dest.writeString(this.mDeviceLine2_Msg);
        dest.writeString(this.mDeviceLine2_Help);
        dest.writeTypedList(this.mInsightEvidence);
        dest.writeStringList(this.mDataUsedPivot);
        dest.writeString(this.mComparisonPivot);
        dest.writeStringList(this.mCategoryPivot);
        dest.writeString(this.mTimespanPivot);
        dest.writeString(this.mTonePivot);
        dest.writeString(this.mScopePivot);
        dest.writeString(this.mNoteworthyPivot);
    }
}
