package com.j256.ormlite.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.misc.VersionUtils;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.GeneratedKeyHolder;
import com.microsoft.kapp.utils.Constants;
import java.sql.SQLException;
import java.sql.Savepoint;
/* loaded from: classes.dex */
public class AndroidDatabaseConnection implements DatabaseConnection {
    private static final String ANDROID_VERSION = "VERSION__4.48__";
    private final boolean cancelQueriesEnabled;
    private final SQLiteDatabase db;
    private final boolean readWrite;
    private static Logger logger = LoggerFactory.getLogger(AndroidDatabaseConnection.class);
    private static final String[] NO_STRING_ARGS = new String[0];

    static {
        VersionUtils.checkCoreVersusAndroidVersions(ANDROID_VERSION);
    }

    public AndroidDatabaseConnection(SQLiteDatabase db, boolean readWrite) {
        this(db, readWrite, false);
    }

    public AndroidDatabaseConnection(SQLiteDatabase db, boolean readWrite, boolean cancelQueriesEnabled) {
        this.db = db;
        this.readWrite = readWrite;
        this.cancelQueriesEnabled = cancelQueriesEnabled;
        logger.trace("{}: db {} opened, read-write = {}", this, db, Boolean.valueOf(readWrite));
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public boolean isAutoCommitSupported() {
        return true;
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public boolean isAutoCommit() throws SQLException {
        try {
            boolean inTransaction = this.db.inTransaction();
            logger.trace("{}: in transaction is {}", this, Boolean.valueOf(inTransaction));
            return !inTransaction;
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("problems getting auto-commit from database", e);
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void setAutoCommit(boolean autoCommit) {
        if (autoCommit) {
            if (this.db.inTransaction()) {
                this.db.setTransactionSuccessful();
                this.db.endTransaction();
            }
        } else if (!this.db.inTransaction()) {
            this.db.beginTransaction();
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public Savepoint setSavePoint(String name) throws SQLException {
        try {
            this.db.beginTransaction();
            logger.trace("{}: save-point set with name {}", this, name);
            return new OurSavePoint(name);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("problems beginning transaction " + name, e);
        }
    }

    public boolean isReadWrite() {
        return this.readWrite;
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void commit(Savepoint savepoint) throws SQLException {
        try {
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
            if (savepoint == null) {
                logger.trace("{}: transaction is successfuly ended", this);
            } else {
                logger.trace("{}: transaction {} is successfuly ended", this, savepoint.getSavepointName());
            }
        } catch (android.database.SQLException e) {
            if (savepoint == null) {
                throw SqlExceptionUtil.create("problems commiting transaction", e);
            }
            throw SqlExceptionUtil.create("problems commiting transaction " + savepoint.getSavepointName(), e);
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void rollback(Savepoint savepoint) throws SQLException {
        try {
            this.db.endTransaction();
            if (savepoint == null) {
                logger.trace("{}: transaction is ended, unsuccessfuly", this);
            } else {
                logger.trace("{}: transaction {} is ended, unsuccessfuly", this, savepoint.getSavepointName());
            }
        } catch (android.database.SQLException e) {
            if (savepoint == null) {
                throw SqlExceptionUtil.create("problems rolling back transaction", e);
            }
            throw SqlExceptionUtil.create("problems rolling back transaction " + savepoint.getSavepointName(), e);
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public int executeStatement(String statementStr, int resultFlags) throws SQLException {
        return AndroidCompiledStatement.execSql(this.db, statementStr, statementStr, NO_STRING_ARGS);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public CompiledStatement compileStatement(String statement, StatementBuilder.StatementType type, FieldType[] argFieldTypes, int resultFlags) {
        CompiledStatement stmt = new AndroidCompiledStatement(statement, this.db, type, this.cancelQueriesEnabled);
        logger.trace("{}: compiled statement got {}: {}", this, stmt, statement);
        return stmt;
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public int insert(String statement, Object[] args, FieldType[] argFieldTypes, GeneratedKeyHolder keyHolder) throws SQLException {
        SQLiteStatement stmt = null;
        try {
            try {
                stmt = this.db.compileStatement(statement);
                bindArgs(stmt, args, argFieldTypes);
                long rowId = stmt.executeInsert();
                if (keyHolder != null) {
                    keyHolder.addKey(Long.valueOf(rowId));
                }
                logger.trace("{}: insert statement is compiled and executed, changed {}: {}", (Object) this, (Object) 1, (Object) statement);
                return 1;
            } catch (android.database.SQLException e) {
                throw SqlExceptionUtil.create("inserting to database failed: " + statement, e);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public int update(String statement, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        return update(statement, args, argFieldTypes, "updated");
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public int delete(String statement, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        return update(statement, args, argFieldTypes, "deleted");
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public <T> Object queryForOne(String statement, Object[] args, FieldType[] argFieldTypes, GenericRowMapper<T> rowMapper, ObjectCache objectCache) throws SQLException {
        Object mapRow;
        Cursor cursor = null;
        try {
            try {
                cursor = this.db.rawQuery(statement, toStrings(args));
                AndroidDatabaseResults results = new AndroidDatabaseResults(cursor, objectCache);
                logger.trace("{}: queried for one result: {}", this, statement);
                if (results.first()) {
                    mapRow = rowMapper.mapRow(results);
                    if (results.next()) {
                        mapRow = MORE_THAN_ONE;
                        if (cursor != null) {
                            cursor.close();
                        }
                    } else if (cursor != null) {
                        cursor.close();
                    }
                } else {
                    mapRow = null;
                }
                return mapRow;
            } catch (android.database.SQLException e) {
                throw SqlExceptionUtil.create("queryForOne from database failed: " + statement, e);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public long queryForLong(String statement) throws SQLException {
        SQLiteStatement stmt = null;
        try {
            try {
                stmt = this.db.compileStatement(statement);
                long result = stmt.simpleQueryForLong();
                logger.trace("{}: query for long simple query returned {}: {}", this, Long.valueOf(result), statement);
                return result;
            } catch (android.database.SQLException e) {
                throw SqlExceptionUtil.create("queryForLong from database failed: " + statement, e);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public long queryForLong(String statement, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        long result;
        Cursor cursor = null;
        try {
            try {
                cursor = this.db.rawQuery(statement, toStrings(args));
                AndroidDatabaseResults results = new AndroidDatabaseResults(cursor, null);
                if (results.first()) {
                    result = results.getLong(0);
                } else {
                    result = 0;
                }
                logger.trace("{}: query for long raw query returned {}: {}", this, Long.valueOf(result), statement);
                return result;
            } catch (android.database.SQLException e) {
                throw SqlExceptionUtil.create("queryForLong from database failed: " + statement, e);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void close() throws SQLException {
        try {
            this.db.close();
            logger.trace("{}: db {} closed", this, this.db);
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("problems closing the database connection", e);
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void closeQuietly() {
        try {
            close();
        } catch (SQLException e) {
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public boolean isClosed() throws SQLException {
        try {
            boolean isOpen = this.db.isOpen();
            logger.trace("{}: db {} isOpen returned {}", this, this.db, Boolean.valueOf(isOpen));
            return !isOpen;
        } catch (android.database.SQLException e) {
            throw SqlExceptionUtil.create("problems detecting if the database is closed", e);
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public boolean isTableExists(String tableName) {
        boolean result;
        Cursor cursor = this.db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'", null);
        try {
            if (cursor.getCount() > 0) {
                result = true;
            } else {
                result = false;
            }
            logger.trace("{}: isTableExists '{}' returned {}", this, tableName, Boolean.valueOf(result));
            return result;
        } finally {
            cursor.close();
        }
    }

    private int update(String statement, Object[] args, FieldType[] argFieldTypes, String label) throws SQLException {
        int result;
        SQLiteStatement stmt = null;
        try {
            try {
                stmt = this.db.compileStatement(statement);
                bindArgs(stmt, args, argFieldTypes);
                stmt.execute();
                try {
                    stmt = this.db.compileStatement("SELECT CHANGES()");
                    result = (int) stmt.simpleQueryForLong();
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (android.database.SQLException e) {
                    result = 1;
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Throwable th) {
                    if (stmt != null) {
                        stmt.close();
                    }
                    throw th;
                }
                logger.trace("{} statement is compiled and executed, changed {}: {}", label, Integer.valueOf(result), statement);
                return result;
            } catch (android.database.SQLException e2) {
                throw SqlExceptionUtil.create("updating database failed: " + statement, e2);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private void bindArgs(SQLiteStatement stmt, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg == null) {
                    stmt.bindNull(i + 1);
                } else {
                    SqlType sqlType = argFieldTypes[i].getSqlType();
                    switch (sqlType) {
                        case STRING:
                        case LONG_STRING:
                        case CHAR:
                            stmt.bindString(i + 1, arg.toString());
                            continue;
                        case BOOLEAN:
                        case BYTE:
                        case SHORT:
                        case INTEGER:
                        case LONG:
                            stmt.bindLong(i + 1, ((Number) arg).longValue());
                            continue;
                        case FLOAT:
                        case DOUBLE:
                            stmt.bindDouble(i + 1, ((Number) arg).doubleValue());
                            continue;
                        case BYTE_ARRAY:
                        case SERIALIZABLE:
                            stmt.bindBlob(i + 1, (byte[]) arg);
                            continue;
                        case DATE:
                        case BLOB:
                        case BIG_DECIMAL:
                            throw new SQLException("Invalid Android type: " + sqlType);
                        default:
                            throw new SQLException("Unknown sql argument type: " + sqlType);
                    }
                }
            }
        }
    }

    private String[] toStrings(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        String[] strings = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                strings[i] = null;
            } else {
                strings[i] = arg.toString();
            }
        }
        return strings;
    }

    public String toString() {
        return getClass().getSimpleName() + Constants.CHAR_AT + Integer.toHexString(super.hashCode());
    }

    /* loaded from: classes.dex */
    private static class OurSavePoint implements Savepoint {
        private String name;

        public OurSavePoint(String name) {
            this.name = name;
        }

        @Override // java.sql.Savepoint
        public int getSavepointId() {
            return 0;
        }

        @Override // java.sql.Savepoint
        public String getSavepointName() {
            return this.name;
        }
    }
}
