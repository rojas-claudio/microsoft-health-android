package com.microsoft.kapp.logging;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.models.LogLevel;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class LogContext implements Parcelable {
    public static final Parcelable.Creator<LogContext> CREATOR = new Parcelable.Creator<LogContext>() { // from class: com.microsoft.kapp.logging.LogContext.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LogContext createFromParcel(Parcel in) {
            return new LogContext(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LogContext[] newArray(int size) {
            return new LogContext[size];
        }
    };
    private Throwable mException;
    private Bitmap mImage;
    private LogLevel mLogLevel;
    private String mMessage;
    private StackTraceElement[] mStackTrace;
    private String mTag;
    private Date mTimestamp;

    /* loaded from: classes.dex */
    public static class Builder {
        private Throwable mException;
        private Bitmap mImage;
        private LogLevel mLogLevel;
        private String mMessage;
        private StackTraceElement[] mStackTrace;
        private String mTag;
        private Date mTimestamp = new Date();

        public Builder setLogLevel(LogLevel logLevel) {
            Validate.notNull(logLevel, "logLevel");
            this.mLogLevel = logLevel;
            return this;
        }

        public Builder setTag(String tag) {
            this.mTag = tag;
            return this;
        }

        public Builder setImage(Bitmap image) {
            this.mImage = image;
            return this;
        }

        public Builder setMessage(String message) {
            if (message == null) {
                message = "";
            }
            this.mMessage = message;
            return this;
        }

        public Builder setException(Throwable exception) {
            this.mException = exception;
            return this;
        }

        public Builder setStackTrace(StackTraceElement[] stackTrace) {
            this.mStackTrace = stackTrace;
            return this;
        }

        public LogContext build() {
            if (this.mLogLevel == null) {
                throw new IllegalStateException("LogLevel cannot be null");
            }
            if (this.mMessage == null || StringUtils.isEmpty(this.mMessage)) {
                throw new IllegalStateException("Message cannot be null or empty");
            }
            return new LogContext(this);
        }
    }

    private LogContext(Builder builder) {
        this.mTimestamp = builder.mTimestamp;
        this.mLogLevel = builder.mLogLevel;
        this.mTag = builder.mTag;
        this.mMessage = builder.mMessage;
        this.mException = builder.mException;
        this.mStackTrace = builder.mStackTrace;
        this.mImage = builder.mImage;
    }

    private LogContext(Parcel in) {
        this.mTimestamp = new Date(in.readLong());
        this.mLogLevel = LogLevel.valueOf(in.readInt());
        this.mTag = in.readString();
        this.mMessage = in.readString();
        this.mStackTrace = (StackTraceElement[]) in.readSerializable();
        this.mImage = (Bitmap) in.readParcelable(Bitmap.class.getClassLoader());
        try {
            this.mException = (Throwable) in.readSerializable();
        } catch (RuntimeException e) {
            this.mException = new Exception("Just failed to serialize the Throwed exception");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.lang.StackTraceElement[], java.io.Serializable] */
    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mTimestamp.getTime());
        dest.writeInt(this.mLogLevel.value());
        dest.writeString(this.mTag);
        dest.writeString(this.mMessage);
        dest.writeSerializable(this.mStackTrace);
        dest.writeParcelable(this.mImage, flags);
        try {
            dest.writeSerializable(this.mException);
        } catch (RuntimeException e) {
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Date getTimestamp() {
        return this.mTimestamp;
    }

    public LogLevel getLogLevel() {
        return this.mLogLevel;
    }

    public String getTag() {
        return this.mTag;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public Throwable getException() {
        return this.mException;
    }

    public Bitmap getImage() {
        return this.mImage;
    }

    public StackTraceElement[] getStackTrace() {
        return this.mStackTrace;
    }
}
