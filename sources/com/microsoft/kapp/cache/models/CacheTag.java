package com.microsoft.kapp.cache.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable
/* loaded from: classes.dex */
public class CacheTag {
    @DatabaseField(columnName = "CacheEntry", foreign = true, foreignAutoRefresh = true)
    private CacheItem mCacheEntry;
    @DatabaseField(columnName = "Tag")
    private String mTag;
    @DatabaseField(columnName = "id", generatedId = true)
    private int mid;

    public int getId() {
        return this.mid;
    }

    public void setId(int id) {
        this.mid = id;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public CacheItem getCacheEntry() {
        return this.mCacheEntry;
    }

    public void setCacheEntry(CacheItem cacheEntry) {
        this.mCacheEntry = cacheEntry;
    }
}
