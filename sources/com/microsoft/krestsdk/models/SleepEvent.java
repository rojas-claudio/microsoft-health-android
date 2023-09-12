package com.microsoft.krestsdk.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.utils.Formatter;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SleepEvent extends UserEvent {
    public static final Parcelable.Creator<SleepEvent> CREATOR = new Parcelable.Creator<SleepEvent>() { // from class: com.microsoft.krestsdk.models.SleepEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepEvent createFromParcel(Parcel in) {
            return new SleepEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepEvent[] newArray(int size) {
            return new SleepEvent[size];
        }
    };
    @SerializedName("DayId")
    private DateTime mDayId;
    @SerializedName("FallAsleepTime")
    public DateTime mFallAsleepTime;
    @SerializedName("IsAutoDetected")
    private boolean mIsAutoSleep;
    @SerializedName("NumberOfWakeups")
    private int mNumberOfWakeups;
    @SerializedName("RestingHeartRate")
    private int mRestingHR;
    @SerializedName("Sequences")
    private SleepEventSequence[] mSequences;
    @SerializedName("SleepEfficiencyPercentage")
    private double mSleepEfficiencyPercentage;
    @SerializedName("SleepRestorationMsg")
    public String mSleepRestorationMsg;
    @SerializedName("SleepTime")
    private int mSleepTime;
    @SerializedName("SleepTimeline")
    private SleepTypeClassification[] mSleepTimeline;
    @SerializedName("TimeToFallAsleep")
    private int mTimeToFallAsleep;
    @SerializedName("TotalRestfulSleep")
    private int mTotalRestfulSleep;
    @SerializedName("TotalRestlessSleep")
    private int mTotalRestlessSleep;
    @SerializedName("WakeUpTime")
    public DateTime mWakeUpTime;

    public SleepEvent() {
    }

    public int getRestingHR() {
        return this.mRestingHR;
    }

    public void setRestingHR(int restingHR) {
        this.mRestingHR = restingHR;
    }

    public DateTime getDayId() {
        return this.mDayId;
    }

    public void setDayId(DateTime dayId) {
        this.mDayId = dayId;
    }

    public int getSleepTime() {
        return this.mSleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.mSleepTime = sleepTime;
    }

    public int getNumberOfWakeups() {
        return this.mNumberOfWakeups;
    }

    public void setNumberOfWakeups(int numberOfWakeups) {
        this.mNumberOfWakeups = numberOfWakeups;
    }

    public int getTimeToFallAsleep() {
        return this.mTimeToFallAsleep;
    }

    public void setTimeToFallAsleep(int timeToFallAsleep) {
        this.mTimeToFallAsleep = timeToFallAsleep;
    }

    public double getSleepEfficiencyPercentage() {
        return this.mSleepEfficiencyPercentage;
    }

    public void setSleepEfficiencyPercentage(double sleepEfficiencyPercentage) {
        this.mSleepEfficiencyPercentage = sleepEfficiencyPercentage;
    }

    public void setTotalRestfulSleep(int totalRestfulSleep) {
        this.mTotalRestfulSleep = totalRestfulSleep;
    }

    public void setTotalRestlessSleep(int totalRestlessSleep) {
        this.mTotalRestlessSleep = totalRestlessSleep;
    }

    public void setFallAsleepTime(DateTime fallAsleepTime) {
        this.mFallAsleepTime = fallAsleepTime;
    }

    public void setWakeUpTime(DateTime wakeUpTime) {
        this.mWakeUpTime = wakeUpTime;
    }

    public int getTotalRestfulSleep() {
        return this.mTotalRestfulSleep;
    }

    public int getTotalRestlessSleep() {
        return this.mTotalRestlessSleep;
    }

    public DateTime getFallAsleepTime() {
        return this.mFallAsleepTime;
    }

    public DateTime getWakeUpTime() {
        return this.mWakeUpTime;
    }

    public String getSleepRestorationMsg() {
        return this.mSleepRestorationMsg;
    }

    public void setSleepRestorationMsg(String sleepRestorationMsg) {
        this.mSleepRestorationMsg = sleepRestorationMsg;
    }

    public void setSleepTimeline(SleepTypeClassification[] sleepTimeline) {
        this.mSleepTimeline = sleepTimeline;
    }

    public SleepTypeClassification[] getSleepTimeline() {
        return this.mSleepTimeline;
    }

    public SleepEventSequence[] getSequences() {
        return this.mSequences;
    }

    public void setSequences(SleepEventSequence[] sequences) {
        this.mSequences = sequences;
    }

    @Override // com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, boolean isMetric) {
        return Formatter.formatSleepTime(context, getSleepTime());
    }

    @Override // com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, int styleResId, boolean isMetric) {
        return Formatter.formatSleepTime(context, styleResId, getSleepTime());
    }

    protected SleepEvent(Parcel in) {
        super(in);
        this.mDayId = new DateTime(in.readLong());
        this.mSleepTime = in.readInt();
        this.mNumberOfWakeups = in.readInt();
        this.mTimeToFallAsleep = in.readInt();
        this.mSleepEfficiencyPercentage = in.readDouble();
        this.mTotalRestfulSleep = in.readInt();
        this.mTotalRestlessSleep = in.readInt();
        this.mWakeUpTime = new DateTime(in.readLong());
        this.mFallAsleepTime = new DateTime(in.readLong());
        this.mSequences = (SleepEventSequence[]) in.createTypedArray(SleepEventSequence.CREATOR);
        this.mRestingHR = in.readInt();
        this.mIsAutoSleep = in.readByte() != 0;
        this.mSleepRestorationMsg = in.readString();
        this.mSleepTimeline = (SleepTypeClassification[]) in.createTypedArray(SleepTypeClassification.CREATOR);
    }

    @Override // com.microsoft.krestsdk.models.UserEvent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.mDayId.getMillis());
        dest.writeInt(this.mSleepTime);
        dest.writeInt(this.mNumberOfWakeups);
        dest.writeInt(this.mTimeToFallAsleep);
        dest.writeDouble(this.mSleepEfficiencyPercentage);
        dest.writeInt(this.mTotalRestfulSleep);
        dest.writeInt(this.mTotalRestlessSleep);
        dest.writeLong(this.mWakeUpTime.getMillis());
        dest.writeLong(this.mFallAsleepTime.getMillis());
        dest.writeTypedArray(this.mSequences, flags);
        dest.writeInt(this.mRestingHR);
        dest.writeByte((byte) (this.mIsAutoSleep ? 1 : 0));
        dest.writeString(this.mSleepRestorationMsg);
        dest.writeTypedArray(this.mSleepTimeline, flags);
    }

    public boolean getIsAutoSleep() {
        return this.mIsAutoSleep;
    }
}
