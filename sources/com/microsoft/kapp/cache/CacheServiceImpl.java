package com.microsoft.kapp.cache;

import com.microsoft.kapp.cache.models.CacheItem;
import com.microsoft.kapp.cache.models.CacheTag;
import com.microsoft.kapp.diagnostics.Validate;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class CacheServiceImpl implements CacheService {
    private Cache mCache;

    public CacheServiceImpl(Cache cache) {
        Validate.notNull(cache, "cache");
        this.mCache = cache;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public String getCachedResponse(String uriKey) {
        Validate.notNull(uriKey, "cache key");
        CacheItem item = this.mCache.getCacheItem(uriKey);
        if (item != null) {
            if (item.getExpirationTime().isAfterNow() && item.getResponse() != null) {
                return item.getResponse();
            }
            this.mCache.removeItem(item);
        }
        return null;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public InputStream getCachedResponseAsStream(String uriKey) {
        Validate.notNull(uriKey, "cache key");
        CacheItem item = this.mCache.getCacheItemStream(uriKey);
        if (item != null) {
            if (item.getExpirationTime().isAfterNow() && item.getResponseStream() != null) {
                return item.getResponseStream();
            }
            this.mCache.removeItem(item);
        }
        return null;
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean put(String uriKey, String response, List<String> tags) {
        Validate.notNull(uriKey, "cache key");
        Validate.notNull(response, "response");
        CacheItem item = new CacheItem();
        item.setKey(uriKey);
        item.setResponse(response);
        item.setExpirationTime(calculateExpirationTime(tags));
        item.setVersion(1);
        List<CacheTag> taglist = new ArrayList<>();
        if (tags != null) {
            for (String tag : tags) {
                CacheTag tagValue = new CacheTag();
                tagValue.setTag(tag);
                tagValue.setCacheEntry(item);
                taglist.add(tagValue);
            }
            item.setTags(taglist);
        }
        return this.mCache.putCacheItem(item);
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean put(String uriKey, InputStream response, List<String> tags) {
        Validate.notNull(uriKey, "cache key");
        Validate.notNull(response, "response");
        Validate.notNullOrEmpty(tags, "tags");
        CacheItem item = new CacheItem();
        item.setKey(uriKey);
        item.setResponseStream(response);
        item.setExpirationTime(calculateExpirationTime(tags));
        item.setVersion(1);
        List<CacheTag> taglist = new ArrayList<>();
        if (tags != null) {
            for (String tag : tags) {
                CacheTag tagValue = new CacheTag();
                tagValue.setTag(tag);
                tagValue.setCacheEntry(item);
                taglist.add(tagValue);
            }
            item.setTags(taglist);
        }
        return this.mCache.putCacheItem(item);
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean remove(String uriKey) {
        Validate.notNull(uriKey, "cache key");
        return this.mCache.removeItem(uriKey);
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean removeForTag(String tag) {
        return this.mCache.removeItemsForTag(tag);
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public void removeAll() {
        this.mCache.removeAll();
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean removeForTags(List<String> tags) {
        return this.mCache.removeItemsForTags(tags);
    }

    private DateTime calculateExpirationTime(List<String> tags) {
        int expirationMins = 10080;
        for (String tag : tags) {
            Integer expiration = CacheUtils.DefaultExpirationTimesInMins.get(tag);
            if (expiration != null && expiration.intValue() < expirationMins) {
                expirationMins = expiration.intValue();
            }
        }
        return DateTime.now().plusMinutes(expirationMins);
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public void cleanup() {
        this.mCache.cleanup();
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public void handleLogout() {
        removeAll();
        this.mCache.handleLogout();
    }

    @Override // com.microsoft.kapp.cache.CacheService
    public boolean isResponseCached(String requestUrl) {
        CacheItem item = this.mCache.peekCacheItem(requestUrl);
        return item != null && item.getExpirationTime().isAfterNow();
    }
}
