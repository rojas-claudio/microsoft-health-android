package com.facebook.internal;

import android.content.Context;
import android.os.Bundle;
/* loaded from: classes.dex */
final class LikeStatusClient extends PlatformServiceClient {
    private String objectId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LikeStatusClient(Context context, String applicationId, String objectId) {
        super(context, NativeProtocol.MESSAGE_GET_LIKE_STATUS_REQUEST, NativeProtocol.MESSAGE_GET_LIKE_STATUS_REPLY, NativeProtocol.PROTOCOL_VERSION_20141001, applicationId);
        this.objectId = objectId;
    }

    @Override // com.facebook.internal.PlatformServiceClient
    protected void populateRequestBundle(Bundle data) {
        data.putString(NativeProtocol.EXTRA_OBJECT_ID, this.objectId);
    }
}
