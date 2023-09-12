package com.microsoft.kapp.services.bedrock.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.logging.LogConstants;
/* loaded from: classes.dex */
public class BedrockImageCollection {
    @SerializedName("caption")
    private String mCaption;
    @SerializedName(LogConstants.DIAGNOSTIC_IMAGES_FOLDER)
    private BedrockImage[] mImages;

    public String getCaption() {
        return this.mCaption;
    }

    public void setCaption(String caption) {
        this.mCaption = caption;
    }

    public BedrockImage[] getImages() {
        return this.mImages;
    }

    public void setImages(BedrockImage[] images) {
        this.mImages = images;
    }
}
