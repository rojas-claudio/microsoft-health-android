package com.microsoft.kapp.cache;

import java.io.InputStream;
import java.util.List;
/* loaded from: classes.dex */
public class MockCacheServiceImpl implements CacheService {
    @Override // com.microsoft.kapp.cache.CacheService
    public String getCachedResponse(String uriKey) {
        return null;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean put(String uriKey, String response, List<String> tags) {
        return true;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean remove(String uriKey) {
        return true;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean removeForTag(String tag) {
        return true;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean removeForTags(List<String> tags) {
        return true;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public void removeAll() {
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public void cleanup() {
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public void handleLogout() {
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean put(String uriKey, InputStream response, List<String> tags) {
        return false;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean isResponseCached(String requestUrl) {
        return false;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public InputStream getCachedResponseAsStream(String requestUrl) {
        return null;
    }
}
