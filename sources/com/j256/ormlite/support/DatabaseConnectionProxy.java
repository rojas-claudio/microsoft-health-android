package com.j256.ormlite.support;

import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.stmt.StatementBuilder;
import java.sql.SQLException;
import java.sql.Savepoint;
/* loaded from: classes.dex */
public class DatabaseConnectionProxy implements DatabaseConnection {
    private final DatabaseConnection proxy;

    public DatabaseConnectionProxy(DatabaseConnection proxy) {
        this.proxy = proxy;
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public boolean isAutoCommitSupported() throws SQLException {
        if (this.proxy == null) {
            return false;
        }
        return this.proxy.isAutoCommitSupported();
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public boolean isAutoCommit() throws SQLException {
        if (this.proxy == null) {
            return false;
        }
        return this.proxy.isAutoCommit();
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (this.proxy != null) {
            this.proxy.setAutoCommit(autoCommit);
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public Savepoint setSavePoint(String name) throws SQLException {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.setSavePoint(name);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void commit(Savepoint savePoint) throws SQLException {
        if (this.proxy != null) {
            this.proxy.commit(savePoint);
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void rollback(Savepoint savePoint) throws SQLException {
        if (this.proxy != null) {
            this.proxy.rollback(savePoint);
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public int executeStatement(String statementStr, int resultFlags) throws SQLException {
        if (this.proxy == null) {
            return 0;
        }
        return this.proxy.executeStatement(statementStr, resultFlags);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public CompiledStatement compileStatement(String statement, StatementBuilder.StatementType type, FieldType[] argFieldTypes, int resultFlags) throws SQLException {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.compileStatement(statement, type, argFieldTypes, resultFlags);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public int insert(String statement, Object[] args, FieldType[] argfieldTypes, GeneratedKeyHolder keyHolder) throws SQLException {
        if (this.proxy == null) {
            return 0;
        }
        return this.proxy.insert(statement, args, argfieldTypes, keyHolder);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public int update(String statement, Object[] args, FieldType[] argfieldTypes) throws SQLException {
        if (this.proxy == null) {
            return 0;
        }
        return this.proxy.update(statement, args, argfieldTypes);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public int delete(String statement, Object[] args, FieldType[] argfieldTypes) throws SQLException {
        if (this.proxy == null) {
            return 0;
        }
        return this.proxy.delete(statement, args, argfieldTypes);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public <T> Object queryForOne(String statement, Object[] args, FieldType[] argfieldTypes, GenericRowMapper<T> rowMapper, ObjectCache objectCache) throws SQLException {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.queryForOne(statement, args, argfieldTypes, rowMapper, objectCache);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public long queryForLong(String statement) throws SQLException {
        if (this.proxy == null) {
            return 0L;
        }
        return this.proxy.queryForLong(statement);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public long queryForLong(String statement, Object[] args, FieldType[] argFieldTypes) throws SQLException {
        if (this.proxy == null) {
            return 0L;
        }
        return this.proxy.queryForLong(statement, args, argFieldTypes);
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void close() throws SQLException {
        if (this.proxy != null) {
            this.proxy.close();
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public void closeQuietly() {
        if (this.proxy != null) {
            this.proxy.closeQuietly();
        }
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public boolean isClosed() throws SQLException {
        if (this.proxy == null) {
            return true;
        }
        return this.proxy.isClosed();
    }

    @Override // com.j256.ormlite.support.DatabaseConnection
    public boolean isTableExists(String tableName) throws SQLException {
        if (this.proxy == null) {
            return false;
        }
        return this.proxy.isTableExists(tableName);
    }
}
