package com.microsoft.kapp.services.bedrock.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
/* loaded from: classes.dex */
public class BedrockImage {
    @SerializedName("height")
    private int mHeight;
    @SerializedName(WorkoutSummary.NAME)
    private BedrockImageType mType;
    @SerializedName("url")
    private String mURL;
    @SerializedName("width")
    private int mWidth;

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public BedrockImageType getType() {
        return this.mType;
    }

    public void setType(BedrockImageType type) {
        this.mType = type;
    }

    public String getURL() {
        return this.mURL;
    }

    public void setURL(String url) {
        this.mURL = url;
    }
}
