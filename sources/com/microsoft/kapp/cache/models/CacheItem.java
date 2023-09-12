package com.microsoft.kapp.cache.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.joda.time.DateTime;
@DatabaseTable
/* loaded from: classes.dex */
public class CacheItem {
    @DatabaseField(canBeNull = false, columnName = "Expiration")
    private DateTime mExpirationTime;
    private boolean mIsResonseStream;
    @DatabaseField(columnName = "id", id = true)
    private String mKey;
    private String mResponse;
    @DatabaseField(canBeNull = false, columnName = "ResponseFilePath")
    private String mResponseFilePath;
    private InputStream mResponseStream;
    @ForeignCollectionField
    private Collection<CacheTag> mTags;
    @DatabaseField(canBeNull = false, columnName = "CacheVersion")
    private Integer mVersion;

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getKey() {
        return this.mKey;
    }

    public void setResponseFilePath(String responseFilePath) {
        this.mResponseFilePath = responseFilePath;
    }

    public String getResponseFilePath() {
        return this.mResponseFilePath;
    }

    public void setResponse(String response) {
        this.mResponse = response;
    }

    public String getResponse() {
        return this.mResponse;
    }

    public void setResponseStream(InputStream response) {
        this.mResponseStream = response;
        this.mIsResonseStream = true;
    }

    public InputStream getResponseStream() {
        return this.mResponseStream;
    }

    public void setTags(List<CacheTag> tags) {
        this.mTags = tags;
    }

    public List<CacheTag> getTags() {
        ArrayList<CacheTag> itemList = new ArrayList<>();
        for (CacheTag item : this.mTags) {
            itemList.add(item);
        }
        return itemList;
    }

    public DateTime getExpirationTime() {
        return this.mExpirationTime;
    }

    public void setExpirationTime(DateTime expirationTime) {
        this.mExpirationTime = expirationTime;
    }

    public Integer getVersion() {
        return this.mVersion;
    }

    public void setVersion(Integer version) {
        this.mVersion = version;
    }

    public boolean isResponseStream() {
        return this.mIsResonseStream;
    }
}
