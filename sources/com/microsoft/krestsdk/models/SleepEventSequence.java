package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class SleepEventSequence extends UserEventSequence {
    public static final Parcelable.Creator<SleepEventSequence> CREATOR = new Parcelable.Creator<SleepEventSequence>() { // from class: com.microsoft.krestsdk.models.SleepEventSequence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepEventSequence createFromParcel(Parcel in) {
            return new SleepEventSequence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepEventSequence[] newArray(int size) {
            return new SleepEventSequence[size];
        }
    };
    @SerializedName("SequenceType")
    private SequenceType mSequenceType;
    @SerializedName("SleepTime")
    private int mSleepTime;
    @SerializedName("SleepType")
    private SleepTypes mSleepType;

    public SleepEventSequence() {
    }

    public SequenceType getSequenceType() {
        return this.mSequenceType;
    }

    public void setSequenceType(SequenceType type) {
        this.mSequenceType = type;
    }

    public int getSleepTime() {
        return this.mSleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.mSleepTime = sleepTime;
    }

    public SleepTypes getSleepType() {
        return this.mSleepType;
    }

    public void setSleepType(SleepTypes sleepType) {
        this.mSleepType = sleepType;
    }

    protected SleepEventSequence(Parcel in) {
        super(in);
        this.mSleepTime = in.readInt();
        int sleepTypeOrdinal = in.readInt();
        this.mSleepType = sleepTypeOrdinal != -1 ? SleepTypes.values()[sleepTypeOrdinal] : null;
        int sequenceTypeOrdinal = in.readInt();
        this.mSequenceType = sequenceTypeOrdinal != -1 ? SequenceType.values()[sequenceTypeOrdinal] : null;
    }

    @Override // com.microsoft.krestsdk.models.UserEventSequence, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mSleepTime);
        dest.writeInt(this.mSleepType != null ? this.mSleepType.ordinal() : -1);
        dest.writeInt(this.mSequenceType != null ? this.mSequenceType.ordinal() : -1);
    }
}
