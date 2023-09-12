package com.j256.ormlite.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.j256.ormlite.android.compat.ApiCompatibility;
import com.j256.ormlite.android.compat.ApiCompatibilityUtils;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseResults;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class AndroidCompiledStatement implements CompiledStatement {
    private List<Object> args;
    private final boolean cancelQueriesEnabled;
    private ApiCompatibility.CancellationHook cancellationHook;
    private Cursor cursor;
    private final SQLiteDatabase db;
    private Integer max;
    private final String sql;
    private final StatementBuilder.StatementType type;
    private static Logger logger = LoggerFactory.getLogger(AndroidCompiledStatement.class);
    private static final String[] NO_STRING_ARGS = new String[0];
    private static final ApiCompatibility apiCompatibility = ApiCompatibilityUtils.getCompatibility();

    public AndroidCompiledStatement(String sql, SQLiteDatabase db, StatementBuilder.StatementType type, boolean cancelQueriesEnabled) {
        this.sql = sql;
        this.db = db;
        this.type = type;
        this.cancelQueriesEnabled = cancelQueriesEnabled;
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public int getColumnCount() throws SQLException {
        return getCursor().getColumnCount();
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public String getColumnName(int column) throws SQLException {
        return getCursor().getColumnName(column);
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public DatabaseResults runQuery(ObjectCache objectCache) throws SQLException {
        if (!this.type.isOkForQuery()) {
            throw new IllegalArgumentException("Cannot call query on a " + this.type + " statement");
        }
        return new AndroidDatabaseResults(getCursor(), objectCache);
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public int runUpdate() throws SQLException {
        String finalSql;
        if (!this.type.isOkForUpdate()) {
            throw new IllegalArgumentException("Cannot call update on a " + this.type + " statement");
        }
        if (this.max == null) {
            finalSql = this.sql;
        } else {
            finalSql = this.sql + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + this.max;
        }
        return execSql(this.db, "runUpdate", finalSql, getArgArray());
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public int runExecute() throws SQLException {
        if (!this.type.isOkForExecute()) {
            throw new IllegalArgumentException("Cannot call execute on a " + this.type + " statement");
        }
        return execSql(this.db, "runExecute", this.sql, getArgArray());
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public void close() throws SQLException {
        if (this.cursor != null) {
            try {
                this.cursor.close();
            } catch (android.database.SQLException e) {
                throw SqlExceptionUtil.create("Problems closing Android cursor", e);
            }
        }
        this.cancellationHook = null;
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public void closeQuietly() {
        try {
            close();
        } catch (SQLException e) {
        }
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public void cancel() {
        if (this.cancellationHook != null) {
            this.cancellationHook.cancel();
        }
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public void setObject(int parameterIndex, Object obj, SqlType sqlType) throws SQLException {
        isInPrep();
        if (this.args == null) {
            this.args = new ArrayList();
        }
        if (obj == null) {
            this.args.add(parameterIndex, null);
            return;
        }
        switch (sqlType) {
            case STRING:
            case LONG_STRING:
            case DATE:
            case BOOLEAN:
            case CHAR:
            case BYTE:
            case SHORT:
            case INTEGER:
            case LONG:
            case FLOAT:
            case DOUBLE:
                this.args.add(parameterIndex, obj.toString());
                return;
            case BYTE_ARRAY:
            case SERIALIZABLE:
                this.args.add(parameterIndex, obj);
                return;
            case BLOB:
            case BIG_DECIMAL:
                throw new SQLException("Invalid Android type: " + sqlType);
            default:
                throw new SQLException("Unknown sql argument type: " + sqlType);
        }
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public void setMaxRows(int max) throws SQLException {
        isInPrep();
        this.max = Integer.valueOf(max);
    }

    @Override // com.j256.ormlite.support.CompiledStatement
    public void setQueryTimeout(long millis) {
    }

    public Cursor getCursor() throws SQLException {
        if (this.cursor == null) {
            String finalSql = null;
            try {
                if (this.max == null) {
                    finalSql = this.sql;
                } else {
                    finalSql = this.sql + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + this.max;
                }
                if (this.cancelQueriesEnabled) {
                    this.cancellationHook = apiCompatibility.createCancellationHook();
                }
                this.cursor = apiCompatibility.rawQuery(this.db, finalSql, getStringArray(), this.cancellationHook);
                this.cursor.moveToFirst();
                logger.trace("{}: started rawQuery cursor for: {}", this, finalSql);
            } catch (android.database.SQLException e) {
                throw SqlExceptionUtil.create("Problems executing Android query: " + finalSql, e);
            }
        }
        return this.cursor;
    }

    public String toString() {
        return getClass().getSimpleName() + Constants.CHAR_AT + Integer.toHexString(super.hashCode());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int execSql(SQLiteDatabase db, String label, String finalSql, Object[] argArray) throws SQLException {
        int result;
        try {
            db.execSQL(finalSql, argArray);
            SQLiteStatement stmt = null;
            try {
                stmt = db.compileStatement("SELECT CHANGES()");
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
            logger.trace("executing statement {} changed {} rows: {}", label, Integer.valueOf(result), finalSql);
            return result;
        } catch (android.database.SQLException e2) {
            throw SqlExceptionUtil.create("Problems executing " + label + " Android statement: " + finalSql, e2);
        }
    }

    private void isInPrep() throws SQLException {
        if (this.cursor != null) {
            throw new SQLException("Query already run. Cannot add argument values.");
        }
    }

    private Object[] getArgArray() {
        return this.args == null ? NO_STRING_ARGS : this.args.toArray(new Object[this.args.size()]);
    }

    private String[] getStringArray() {
        return this.args == null ? NO_STRING_ARGS : (String[]) this.args.toArray(new String[this.args.size()]);
    }
}
