package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class GoalTemplateDto implements DeserializedObjectValidation {
    @SerializedName(TelemetryConstants.Events.SendFeedbackComplete.Dimensions.DESCRIPTION)
    private String mDescription;
    @SerializedName("GoalValueTemplates")
    private List<GoalValueTemplateDto> mGoalValueTemplates;
    @SerializedName("Id")
    private String mId;
    @SerializedName(TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME)
    private String mName;
    @SerializedName("Type")
    private GoalType mType;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public GoalType getType() {
        return this.mType;
    }

    public void setType(GoalType type) {
        this.mType = type;
    }

    public List<GoalValueTemplateDto> getGoalValueTemplates() {
        if (this.mGoalValueTemplates == null) {
            this.mGoalValueTemplates = new ArrayList();
        }
        return this.mGoalValueTemplates;
    }

    @Override // com.microsoft.krestsdk.models.DeserializedObjectValidation
    public void validateDeserializedObject() {
        Validate.notNull(this.mId, "Id");
        Validate.notNullOrEmpty(this.mName, TelemetryConstants.TimedEvents.Cloud.Golf.SEARCH_TYPE_NAME);
        Validate.notNull(this.mType, "Type");
        Validate.notNull(this.mGoalValueTemplates, "GoalValueTemplates");
        Validate.isTrue(this.mGoalValueTemplates.size() == 1, "GoalValueTemplate should only contain one item.");
        GoalValueTemplateDto templateValue = this.mGoalValueTemplates.get(0);
        templateValue.validateDeserializedObject();
        if (this.mType == GoalType.CALORIE || this.mType == GoalType.STEP) {
            Validate.notNull(templateValue.getThreshold(), "Threshold");
        }
    }
}
