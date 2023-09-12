package com.microsoft.band.service.logger;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class LoggerEvent implements Parcelable {
    public static final Parcelable.Creator<LoggerEvent> CREATOR = new Parcelable.Creator<LoggerEvent>() { // from class: com.microsoft.band.service.logger.LoggerEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LoggerEvent createFromParcel(Parcel source) {
            return new LoggerEvent(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LoggerEvent[] newArray(int size) {
            return new LoggerEvent[0];
        }
    };
    public final String mFormat;
    public final String mName;
    public final Object[] mParams;
    public final String mTag;
    public final long mTimestamp;

    LoggerEvent(Parcel source) {
        this.mParams = source.readArray(LoggerEvent.class.getClassLoader());
        this.mTimestamp = source.readLong();
        this.mTag = source.readString();
        this.mName = source.readString();
        this.mFormat = source.readString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoggerEvent(long timestamp, String tag, String name, String format, Object[] params) {
        this.mTimestamp = timestamp;
        this.mTag = tag;
        this.mName = name;
        this.mFormat = format;
        this.mParams = params;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeArray(this.mParams);
        dest.writeLong(this.mTimestamp);
        dest.writeString(this.mTag);
        dest.writeString(this.mName);
        dest.writeString(this.mFormat);
    }
}
