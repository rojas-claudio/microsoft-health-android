package com.microsoft.kapp.services.bedrock.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
/* loaded from: classes.dex */
public class BedrockEntity {
    @SerializedName(WorkoutSummary.IMAGE)
    private BedrockImageCollection mBedrockImageCollection;

    public BedrockImageCollection getBedrockImageCollection() {
        return this.mBedrockImageCollection;
    }

    public void setBedrockImageCollection(BedrockImageCollection bedrockImageCollection) {
        this.mBedrockImageCollection = bedrockImageCollection;
    }
}
