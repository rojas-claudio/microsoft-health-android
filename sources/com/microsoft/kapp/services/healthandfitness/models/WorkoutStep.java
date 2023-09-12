package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.krestsdk.models.CompletionType;
import com.microsoft.krestsdk.models.DisplaySubType;
import java.io.Serializable;
/* loaded from: classes.dex */
public class WorkoutStep implements Serializable {
    private static final long serialVersionUID = -8518776886442824857L;
    @SerializedName("ckt")
    private WorkoutCircuit[] mCircuitList;
    @SerializedName("kbrandcolorhex")
    private long mKBrandColorHex;
    @SerializedName("kbrandlogo")
    private String mKBrandLogo;
    @SerializedName("kbrandname")
    private String mKBrandName;
    @SerializedName("kcompletiontype")
    private int mKCompletionType;
    @SerializedName("kcompletionvalue")
    private int mKCompletionValue;
    @SerializedName("KDisplaySubType")
    private DisplaySubType mKDisplaySubType;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public WorkoutCircuit[] getCircuitList() {
        return this.mCircuitList;
    }

    public void setCircuitList(WorkoutCircuit[] mCircuitList) {
        this.mCircuitList = mCircuitList;
    }

    public CompletionType getCompletionType() {
        return CompletionType.valueOf(this.mKCompletionType);
    }

    public void setCompletionType(CompletionType completionType) {
        this.mKCompletionType = completionType.ordinal();
    }

    public int getCompletionValue() {
        return this.mKCompletionValue;
    }

    public void setCompletionValue(int completionValue) {
        this.mKCompletionValue = completionValue;
    }

    public DisplaySubType getDisplaySubType() {
        return this.mKDisplaySubType;
    }

    public void setDisplySubType(DisplaySubType displaySubType) {
        this.mKDisplaySubType = displaySubType;
    }
}
