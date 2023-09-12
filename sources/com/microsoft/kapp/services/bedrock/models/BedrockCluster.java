package com.microsoft.kapp.services.bedrock.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class BedrockCluster {
    @SerializedName("entityList")
    private BedrockEntityList mBedrockEntityList;

    public BedrockEntityList getBedrockEntityList() {
        return this.mBedrockEntityList;
    }

    public void setBedrockEntityList(BedrockEntityList bedrockEntityList) {
        this.mBedrockEntityList = bedrockEntityList;
    }
}
