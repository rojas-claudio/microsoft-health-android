package com.microsoft.kapp.logging.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.services.KCloudConstants;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class LogEntry implements Parcelable {
    public static final Parcelable.Creator<LogEntry> CREATOR = new Parcelable.Creator<LogEntry>() { // from class: com.microsoft.kapp.logging.models.LogEntry.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LogEntry createFromParcel(Parcel in) {
            return new LogEntry(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LogEntry[] newArray(int size) {
            return new LogEntry[size];
        }
    };
    @SerializedName(KCloudConstants.CATEGORY_TYPE)
    private String category;
    @SerializedName(Constants.FRE_INTENT_EXTRA_INFO)
    private LogEntryContext context;
    @SerializedName("data")
    private Map<String, String> data;
    @SerializedName("exception")
    private LogEntryException exception;
    @SerializedName("fileReference")
    private String fileReference;
    private transient Bitmap image;
    @SerializedName("level")
    private LogLevel level;
    @SerializedName("message")
    private String message;
    private transient Throwable originException;
    @SerializedName("time")
    private DateTime time;
    @SerializedName("type")
    private LogEntryType type;

    public DateTime getTime() {
        return this.time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public LogLevel getLevel() {
        return this.level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogEntryType getType() {
        return this.type;
    }

    public void setType(LogEntryType type) {
        this.type = type;
    }

    public String getFileReference() {
        return this.fileReference;
    }

    public void setFileReference(String fileReference) {
        this.fileReference = fileReference;
    }

    public LogEntryContext getContext() {
        return this.context;
    }

    public void setContext(LogEntryContext context) {
        this.context = context;
    }

    public LogEntryException getException() {
        return this.exception;
    }

    public void setException(LogEntryException exception) {
        this.exception = exception;
    }

    public Throwable getOriginException() {
        return this.originException;
    }

    public void setOriginException(Throwable originException) {
        this.originException = originException;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Map getData() {
        return this.data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public LogEntry() {
    }

    protected LogEntry(Parcel in) {
        long tmpTime = in.readLong();
        this.time = tmpTime != -1 ? new DateTime(tmpTime) : null;
        this.level = (LogLevel) in.readValue(LogLevel.class.getClassLoader());
        this.category = in.readString();
        this.message = in.readString();
        this.type = (LogEntryType) in.readValue(LogEntryType.class.getClassLoader());
        this.context = (LogEntryContext) in.readValue(LogEntryContext.class.getClassLoader());
        this.exception = (LogEntryException) in.readValue(LogEntryException.class.getClassLoader());
        this.fileReference = in.readString();
        this.data = new HashMap();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            this.data.put(key, value);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.time != null ? this.time.getMillis() : -1L);
        dest.writeValue(this.level);
        dest.writeString(this.category);
        dest.writeString(this.message);
        dest.writeValue(this.type);
        dest.writeValue(this.context);
        dest.writeValue(this.exception);
        dest.writeString(this.fileReference);
        int size = this.data.size();
        dest.writeInt(size);
        if (size > 0) {
            for (Map.Entry<String, String> entry : this.data.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }
    }
}
