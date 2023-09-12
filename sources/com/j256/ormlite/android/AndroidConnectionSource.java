package com.j256.ormlite.android;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.db.SqliteAndroidDatabaseType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.BaseConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseConnectionProxyFactory;
import com.microsoft.kapp.utils.Constants;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class AndroidConnectionSource extends BaseConnectionSource implements ConnectionSource {
    private static DatabaseConnectionProxyFactory connectionProxyFactory;
    private static final Logger logger = LoggerFactory.getLogger(AndroidConnectionSource.class);
    private boolean cancelQueriesEnabled;
    private DatabaseConnection connection;
    private final DatabaseType databaseType;
    private final SQLiteOpenHelper helper;
    private volatile boolean isOpen;
    private final SQLiteDatabase sqliteDatabase;

    public AndroidConnectionSource(SQLiteOpenHelper helper) {
        this.connection = null;
        this.isOpen = true;
        this.databaseType = new SqliteAndroidDatabaseType();
        this.cancelQueriesEnabled = false;
        this.helper = helper;
        this.sqliteDatabase = null;
    }

    public AndroidConnectionSource(SQLiteDatabase sqliteDatabase) {
        this.connection = null;
        this.isOpen = true;
        this.databaseType = new SqliteAndroidDatabaseType();
        this.cancelQueriesEnabled = false;
        this.helper = null;
        this.sqliteDatabase = sqliteDatabase;
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public DatabaseConnection getReadOnlyConnection() throws SQLException {
        return getReadWriteConnection();
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public DatabaseConnection getReadWriteConnection() throws SQLException {
        SQLiteDatabase db;
        DatabaseConnection conn = getSavedConnection();
        if (conn == null) {
            if (this.connection == null) {
                if (this.sqliteDatabase == null) {
                    try {
                        db = this.helper.getWritableDatabase();
                    } catch (android.database.SQLException e) {
                        throw SqlExceptionUtil.create("Getting a writable database from helper " + this.helper + " failed", e);
                    }
                } else {
                    db = this.sqliteDatabase;
                }
                this.connection = new AndroidDatabaseConnection(db, true, this.cancelQueriesEnabled);
                if (connectionProxyFactory != null) {
                    this.connection = connectionProxyFactory.createProxy(this.connection);
                }
                logger.trace("created connection {} for db {}, helper {}", this.connection, db, this.helper);
            } else {
                logger.trace("{}: returning read-write connection {}, helper {}", this, this.connection, this.helper);
            }
            return this.connection;
        }
        return conn;
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public void releaseConnection(DatabaseConnection connection) {
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public boolean saveSpecialConnection(DatabaseConnection connection) throws SQLException {
        return saveSpecial(connection);
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public void clearSpecialConnection(DatabaseConnection connection) {
        clearSpecial(connection, logger);
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public void close() {
        this.isOpen = false;
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public void closeQuietly() {
        close();
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }

    @Override // com.j256.ormlite.support.ConnectionSource
    public boolean isOpen() {
        return this.isOpen;
    }

    public static void setDatabaseConnectionProxyFactory(DatabaseConnectionProxyFactory connectionProxyFactory2) {
        connectionProxyFactory = connectionProxyFactory2;
    }

    public boolean isCancelQueriesEnabled() {
        return this.cancelQueriesEnabled;
    }

    public void setCancelQueriesEnabled(boolean cancelQueriesEnabled) {
        this.cancelQueriesEnabled = cancelQueriesEnabled;
    }

    public String toString() {
        return getClass().getSimpleName() + Constants.CHAR_AT + Integer.toHexString(super.hashCode());
    }
}
