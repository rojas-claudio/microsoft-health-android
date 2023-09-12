package com.microsoft.kapp.services.bedrock;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.services.bedrock.models.BedrockImageId;
/* loaded from: classes.dex */
public interface BedrockRestService {
    void getBedrockImageUrl(BedrockImageId bedrockImageId, Callback<String> callback);
}
