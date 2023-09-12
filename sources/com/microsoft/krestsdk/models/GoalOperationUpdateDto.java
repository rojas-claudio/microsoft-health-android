package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GoalOperationUpdateDto extends GoalOperationDto {
    @SerializedName("Id")
    private String mId;
    @SerializedName("ValueHistory")
    private List<GoalValueHistoryDto> mValueHistory;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public List<GoalValueHistoryDto> getValueHistory() {
        if (this.mValueHistory == null) {
            this.mValueHistory = new ArrayList();
        }
        return this.mValueHistory;
    }
}
