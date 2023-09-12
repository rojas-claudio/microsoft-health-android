package com.microsoft.kapp.services.bedrock.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class BedrockResponseEnvelope {
    @SerializedName("clusters")
    private BedrockCluster[] mBedrockClusters;

    public BedrockCluster[] getBedrockClusters() {
        return this.mBedrockClusters;
    }

    public void setBedrockClusters(BedrockCluster[] bedrockClusters) {
        this.mBedrockClusters = bedrockClusters;
    }
}
