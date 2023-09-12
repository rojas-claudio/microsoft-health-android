package com.microsoft.kapp.services.bedrock.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class BedrockEntityList {
    @SerializedName("entities")
    private BedrockEntity[] mEntities;
    @SerializedName("collectionId")
    private String mId;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public BedrockEntity[] getEntities() {
        return this.mEntities;
    }

    public void setEntities(BedrockEntity[] entities) {
        this.mEntities = entities;
    }
}
