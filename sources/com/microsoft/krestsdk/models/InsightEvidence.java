package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.services.KCloudConstants;
/* loaded from: classes.dex */
public class InsightEvidence implements Parcelable {
    public static final Parcelable.Creator<InsightEvidence> CREATOR = new Parcelable.Creator<InsightEvidence>() { // from class: com.microsoft.krestsdk.models.InsightEvidence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InsightEvidence createFromParcel(Parcel in) {
            return new InsightEvidence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public InsightEvidence[] newArray(int size) {
            return new InsightEvidence[size];
        }
    };
    @SerializedName(KCloudConstants.VALUE_TEMPLATE_EVENTID)
    private String mEventId;
    @SerializedName(UserEvent.EventTypeJSONMemberName)
    private String mEventType;
    @SerializedName(Constants.NOTIFICATION_MESSAGE)
    private String mMessage;
    @SerializedName("Role")
    private String mRole;

    public String getMessage() {
        return this.mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getRole() {
        return this.mRole;
    }

    public void setRole(String role) {
        this.mRole = role;
    }

    public String getEventId() {
        return this.mEventId;
    }

    public void setEventId(String eventId) {
        this.mEventId = eventId;
    }

    public String getEventType() {
        return this.mEventType;
    }

    public void setEventType(String eventType) {
        this.mEventType = eventType;
    }

    protected InsightEvidence(Parcel in) {
        this.mMessage = in.readString();
        this.mRole = in.readString();
        this.mEventId = in.readString();
        this.mEventType = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mMessage);
        dest.writeString(this.mRole);
        dest.writeString(this.mEventId);
        dest.writeString(this.mEventType);
    }
}
