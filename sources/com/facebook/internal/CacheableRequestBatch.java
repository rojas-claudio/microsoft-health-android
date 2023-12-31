package com.facebook.internal;

import com.facebook.Request;
import com.facebook.RequestBatch;
/* loaded from: classes.dex */
public class CacheableRequestBatch extends RequestBatch {
    private String cacheKey;
    private boolean forceRoundTrip;

    public CacheableRequestBatch() {
    }

    public CacheableRequestBatch(Request... requests) {
        super(requests);
    }

    public final String getCacheKeyOverride() {
        return this.cacheKey;
    }

    public final void setCacheKeyOverride(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public final boolean getForceRoundTrip() {
        return this.forceRoundTrip;
    }

    public final void setForceRoundTrip(boolean forceRoundTrip) {
        this.forceRoundTrip = forceRoundTrip;
    }
}
