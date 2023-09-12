package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GoalOperationAddDto extends GoalOperationDto {
    @SerializedName("TemplateId")
    private String mTemplateId;
    @SerializedName("ValueHistory")
    private List<GoalValueHistoryDto> mValueHistory;

    public String getTemplateId() {
        return this.mTemplateId;
    }

    public void setTemplateId(String templateId) {
        this.mTemplateId = templateId;
    }

    public List<GoalValueHistoryDto> getValueHistory() {
        if (this.mValueHistory == null) {
            this.mValueHistory = new ArrayList();
        }
        return this.mValueHistory;
    }
}
