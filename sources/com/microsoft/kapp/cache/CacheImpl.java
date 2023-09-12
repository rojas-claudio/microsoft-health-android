package com.microsoft.kapp.cache;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.microsoft.kapp.cache.models.CacheItem;
import com.microsoft.kapp.cache.models.CacheTag;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class CacheImpl implements Cache {
    private static final String NETWORK_CACHE_FOLDER = "responseCache";
    private static final String TAG = Cache.class.getName();
    private Context mContext;
    private DBHelper mHelper;

    public CacheImpl(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mHelper = new DBHelper(context);
        this.mContext = context;
        File cacheDir = new File(context.getFilesDir(), NETWORK_CACHE_FOLDER);
        cacheDir.mkdirs();
    }

    @Override // com.microsoft.kapp.cache.Cache
    public CacheItem getCacheItem(String key) {
        CacheItem cacheItem = peekCacheItem(key);
        if (cacheItem != null) {
            try {
                if (cacheItem.getResponseFilePath() != null) {
                    cacheItem.setResponse(FileUtils.readFileContent(cacheItem.getResponseFilePath()));
                }
            } catch (IOException e) {
                KLog.d(TAG, "exception while reading response cache for file" + cacheItem.getResponse(), e);
                cacheItem.setResponse(null);
            }
        }
        return cacheItem;
    }

    @Override // com.microsoft.kapp.cache.Cache
    public CacheItem getCacheItemStream(String key) {
        CacheItem cacheItem = peekCacheItem(key);
        if (cacheItem != null) {
            try {
                if (cacheItem.getResponseFilePath() != null) {
                    cacheItem.setResponseStream(FileUtils.getInputStream(cacheItem.getResponseFilePath()));
                }
            } catch (IOException e) {
                KLog.d(TAG, "exception while reading response cache for file" + cacheItem.getResponseFilePath(), e);
                cacheItem.setResponseStream(null);
            }
        }
        return cacheItem;
    }

    @Override // com.microsoft.kapp.cache.Cache
    public boolean putCacheItem(CacheItem item) {
        String responseFilePath;
        Validate.notNull(item, "CacheItem");
        try {
            if (item.isResponseStream()) {
                responseFilePath = createResponseCacheFile(item.getResponseStream());
            } else {
                responseFilePath = createResponseCacheFile(item.getResponse());
            }
            if (responseFilePath == null) {
                return false;
            }
            item.setKey(item.getKey());
            item.setResponseFilePath(responseFilePath);
            this.mHelper.getCacheItemDao().createOrUpdate(item);
            for (CacheTag tag : item.getTags()) {
                this.mHelper.getCacheTagsDao().createOrUpdate(tag);
            }
            return true;
        } catch (SQLException ex) {
            KLog.d(TAG, "unable to create/update cache item", ex);
            return false;
        }
    }

    @Override // com.microsoft.kapp.cache.Cache
    public boolean removeItem(CacheItem item) {
        Validate.notNull(item, "CacheItem");
        try {
            for (CacheTag tag : item.getTags()) {
                this.mHelper.getCacheTagsDao().delete((Dao<CacheTag, Integer>) tag);
            }
            removeCacheItem(item);
            return true;
        } catch (SQLException ex) {
            KLog.d(TAG, "unable to create/update cache item", ex);
            return false;
        }
    }

    @Override // com.microsoft.kapp.cache.Cache
    public boolean removeItemsForTag(String tag) {
        return removeItemsForTags(new ArrayList(Arrays.asList(tag)));
    }

    @Override // com.microsoft.kapp.cache.Cache
    public boolean removeItemsForTags(List<String> tags) {
        if (tags == null) {
            return true;
        }
        try {
            QueryBuilder<CacheTag, Integer> qb = this.mHelper.getCacheTagsDao().queryBuilder();
            qb.distinct().where().in("Tag", tags);
            GenericRawResults<UO> queryRaw = this.mHelper.getCacheTagsDao().queryRaw(qb.prepareStatementString(), this.mHelper.getCacheTagsDao().getRawRowMapper(), new String[0]);
            if (queryRaw == 0) {
                return true;
            }
            for (UO cacheTag : queryRaw) {
                this.mHelper.getCacheTagsDao().delete((Dao<CacheTag, Integer>) cacheTag);
                CacheItem itemToDelete = cacheTag.getCacheEntry();
                if (itemToDelete != null) {
                    removeItem(cacheTag.getCacheEntry());
                }
            }
            return true;
        } catch (SQLException ex) {
            KLog.d(TAG, "unable to delete items for tags" + Arrays.toString(tags.toArray()), ex);
            return false;
        }
    }

    @Override // com.microsoft.kapp.cache.Cache
    public boolean removeItem(String key) {
        Validate.notNull(key, "cache key");
        try {
            CacheItem item = getCacheItem(key);
            if (item == null) {
                return true;
            }
            for (CacheTag tag : item.getTags()) {
                this.mHelper.getCacheTagsDao().delete((Dao<CacheTag, Integer>) tag);
            }
            removeCacheItem(item);
            return true;
        } catch (SQLException ex) {
            KLog.d(TAG, "unable to delete cache item", ex);
            return false;
        }
    }

    @Override // com.microsoft.kapp.cache.Cache
    public void removeAll() {
        try {
            this.mHelper.Clear();
            FileUtils.cleanupDir(new File(this.mContext.getFilesDir(), NETWORK_CACHE_FOLDER));
        } catch (Exception ex) {
            KLog.d(TAG, "exception when cleaning up cache DB", ex);
        }
    }

    @Override // com.microsoft.kapp.cache.Cache
    public void cleanup() {
        try {
            List<CacheItem> items = this.mHelper.getCacheItemDao().queryForAll();
            for (CacheItem item : items) {
                if (item.getExpirationTime().isBefore(DateTime.now())) {
                    removeItem(item);
                }
            }
            FileUtils.cleanupDir(new File(this.mContext.getFilesDir(), NETWORK_CACHE_FOLDER));
        } catch (Exception ex) {
            KLog.d(TAG, "exception when cleaning up cache DB", ex);
        }
    }

    @Override // com.microsoft.kapp.cache.Cache
    public void handleLogout() {
    }

    @Override // com.microsoft.kapp.cache.Cache
    public CacheItem peekCacheItem(String key) {
        Validate.notNull(key, "cache key");
        try {
            CacheItem cacheItem = this.mHelper.getCacheItemDao().queryForId(key);
            return cacheItem;
        } catch (SQLException ex) {
            KLog.d(TAG, "unable to find cache with key" + key, ex);
            return null;
        }
    }

    @Override // com.microsoft.kapp.cache.Cache
    public InputStream getCache(String key) {
        CacheItem cacheItemStream = getCacheItemStream(key);
        if (cacheItemStream == null) {
            return null;
        }
        InputStream ins = cacheItemStream.getResponseStream();
        return ins;
    }

    private void removeCacheItem(CacheItem item) throws SQLException {
        Validate.notNull(item, "item");
        if (!FileUtils.deleteFile(item.getResponseFilePath())) {
            KLog.d(TAG, "unable to delete file" + item.getResponseFilePath());
        }
        this.mHelper.getCacheItemDao().delete((Dao<CacheItem, String>) item);
    }

    private String createResponseCacheFile(String response) {
        File responseCacheFile = ensureNewFile();
        try {
            FileUtils.writeStringToFile(responseCacheFile, response);
            return responseCacheFile.getAbsolutePath();
        } catch (IOException e) {
            KLog.d(TAG, "unable to write cache file" + responseCacheFile.getAbsolutePath());
            return null;
        }
    }

    private String createResponseCacheFile(InputStream response) {
        File responseCacheFile = ensureNewFile();
        try {
            FileUtils.writeStreamToFile(responseCacheFile, response);
            return responseCacheFile.getAbsolutePath();
        } catch (IOException e) {
            KLog.d(TAG, "unable to write cache file" + responseCacheFile.getAbsolutePath());
            return null;
        }
    }

    private File ensureNewFile() {
        File responseCacheFile;
        boolean fileExists;
        do {
            responseCacheFile = new File(this.mContext.getFilesDir(), "responseCache/" + UUID.randomUUID().toString());
            fileExists = responseCacheFile.exists();
        } while (fileExists);
        return responseCacheFile;
    }
}
