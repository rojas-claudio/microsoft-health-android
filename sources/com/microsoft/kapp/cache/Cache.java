package com.microsoft.kapp.cache;

import com.microsoft.kapp.cache.models.CacheItem;
import java.io.InputStream;
import java.util.List;
/* loaded from: classes.dex */
public interface Cache {
    void cleanup();

    InputStream getCache(String str);

    CacheItem getCacheItem(String str);

    CacheItem getCacheItemStream(String str);

    void handleLogout();

    CacheItem peekCacheItem(String str);

    boolean putCacheItem(CacheItem cacheItem);

    void removeAll();

    boolean removeItem(CacheItem cacheItem);

    boolean removeItem(String str);

    boolean removeItemsForTag(String str);

    boolean removeItemsForTags(List<String> list);
}
