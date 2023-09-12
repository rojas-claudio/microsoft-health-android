package com.microsoft.kapp.logging.models;

import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class FeedbackMetadata {
    @SerializedName("date")
    DateTime date;
    @SerializedName("generatedBy")
    String generatedBy;
    @SerializedName("id")
    String id;
    @SerializedName(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION)
    String version;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getDate() {
        return this.date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getGeneratedBy() {
        return this.generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }
}
