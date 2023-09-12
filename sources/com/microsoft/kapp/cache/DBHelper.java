package com.microsoft.kapp.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.microsoft.kapp.cache.models.CacheItem;
import com.microsoft.kapp.cache.models.CacheTag;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "cache.sqlite";
    private static final int DATABASE_VERSION = 5;
    private static final String TAG = DBHelper.class.getName();
    private Dao<CacheItem, String> cacheItemDao;
    private Dao<CacheTag, Integer> cacheTagsDao;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
        this.cacheItemDao = null;
        this.cacheTagsDao = null;
    }

    @Override // com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
    public void onCreate(SQLiteDatabase database, ConnectionSource connSource) {
        try {
            TableUtils.createTable(connSource, CacheItem.class);
            TableUtils.createTable(connSource, CacheTag.class);
        } catch (SQLException e) {
            Log.e(TAG, "unable to create cache DB");
        }
    }

    @Override // com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connSource, CacheItem.class, false);
            TableUtils.dropTable(connSource, CacheTag.class, false);
            onCreate(db, connSource);
        } catch (SQLException e) {
            Log.e(TAG, "fatal exception during onUpgrade", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<CacheItem, String> getCacheItemDao() {
        if (this.cacheItemDao == null) {
            try {
                this.cacheItemDao = getDao(CacheItem.class);
            } catch (SQLException e) {
                Log.d(TAG, "unable to create cacheitem data access obj");
            }
        }
        return this.cacheItemDao;
    }

    public Dao<CacheTag, Integer> getCacheTagsDao() {
        if (this.cacheTagsDao == null) {
            try {
                this.cacheTagsDao = getDao(CacheTag.class);
            } catch (SQLException e) {
                Log.d(TAG, "unable to create cache tag data access obj");
            }
        }
        return this.cacheTagsDao;
    }

    public void Clear() {
        try {
            TableUtils.clearTable(this.connectionSource, CacheItem.class);
            TableUtils.clearTable(this.connectionSource, CacheTag.class);
        } catch (SQLException e) {
            Log.d(TAG, "unable to clear cache DB");
        }
    }
}
