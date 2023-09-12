package com.microsoft.kapp.logging.notification;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.models.LogEntry;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class NotificationLogEntry implements Parcelable {
    public static final Parcelable.Creator<NotificationLogEntry> CREATOR = new Parcelable.Creator<NotificationLogEntry>() { // from class: com.microsoft.kapp.logging.notification.NotificationLogEntry.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotificationLogEntry createFromParcel(Parcel in) {
            return new NotificationLogEntry(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotificationLogEntry[] newArray(int size) {
            return new NotificationLogEntry[size];
        }
    };
    private LogEntry mContext;
    private int mId;
    private String mTag;

    public NotificationLogEntry(String tag, int id, LogEntry context) {
        Validate.notNullOrEmpty(tag, "tag");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mTag = tag;
        this.mId = id;
        this.mContext = context;
    }

    private NotificationLogEntry(Parcel in) {
        Validate.notNull(in, "in");
        this.mTag = in.readString();
        this.mId = in.readInt();
        this.mContext = (LogEntry) in.readParcelable(LogEntry.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        Validate.notNull(dest, "dest");
        dest.writeString(this.mTag);
        dest.writeInt(this.mId);
        dest.writeParcelable(this.mContext, flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getTag() {
        return this.mTag;
    }

    public int getId() {
        return this.mId;
    }

    public LogEntry getContext() {
        return this.mContext;
    }
}
