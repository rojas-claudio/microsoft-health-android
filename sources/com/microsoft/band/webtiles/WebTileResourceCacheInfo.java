package com.microsoft.band.webtiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public final class WebTileResourceCacheInfo {
    private static final String JSON_KEY_ETAG = "etag";
    private static final String JSON_KEY_FEED_ITEMS = "feed_items";
    private static final String JSON_KEY_LAST_MODIFIED = "last_modified";
    private String mETag;
    private List<String> mFeedItemIds = new ArrayList();
    private String mLastModified;

    public WebTileResourceCacheInfo() {
    }

    public String getETag() {
        return this.mETag;
    }

    public void setETag(String mETag) {
        this.mETag = mETag;
    }

    public String getLastModified() {
        return this.mLastModified;
    }

    public void setLastModified(String lastModified) {
        this.mLastModified = lastModified;
    }

    public List<String> getFeedItemIds() {
        return this.mFeedItemIds;
    }

    public void setFeedItemIds(List<String> mFeedItemIds) {
        this.mFeedItemIds = mFeedItemIds;
    }

    public void addFeedItemIds(List<String> feedItemIds) {
        this.mFeedItemIds.addAll(0, feedItemIds);
    }

    private void setFeedItemIds(JSONArray jsonArray) throws JSONException {
        int size = jsonArray.length();
        for (int i = 0; i < size; i++) {
            this.mFeedItemIds.add(jsonArray.getString(i));
        }
    }

    public WebTileResourceCacheInfo(JSONObject object) throws JSONException {
        if (object.has(JSON_KEY_ETAG)) {
            setETag(object.getString(JSON_KEY_ETAG));
        }
        if (object.has(JSON_KEY_LAST_MODIFIED)) {
            setLastModified(object.getString(JSON_KEY_LAST_MODIFIED));
        }
        if (object.has(JSON_KEY_FEED_ITEMS)) {
            setFeedItemIds(object.getJSONArray(JSON_KEY_FEED_ITEMS));
        }
    }

    public String toJsonString() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.mETag != null) {
            json.put(JSON_KEY_ETAG, this.mETag);
        }
        if (this.mLastModified != null) {
            json.put(JSON_KEY_LAST_MODIFIED, this.mLastModified);
        }
        if (this.mFeedItemIds.size() > 0) {
            json.put(JSON_KEY_FEED_ITEMS, new JSONArray((Collection) this.mFeedItemIds));
        }
        return json.toString();
    }
}
