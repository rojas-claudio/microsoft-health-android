package com.j256.ormlite.stmt.mapped;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.stmt.ArgumentHolder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class MappedPreparedStmt<T, ID> extends BaseMappedQuery<T, ID> implements PreparedQuery<T>, PreparedDelete<T>, PreparedUpdate<T> {
    private final ArgumentHolder[] argHolders;
    private final Long limit;
    private final StatementBuilder.StatementType type;

    public MappedPreparedStmt(TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes, FieldType[] resultFieldTypes, ArgumentHolder[] argHolders, Long limit, StatementBuilder.StatementType type) {
        super(tableInfo, statement, argFieldTypes, resultFieldTypes);
        this.argHolders = argHolders;
        this.limit = limit;
        this.type = type;
    }

    @Override // com.j256.ormlite.stmt.PreparedStmt
    public CompiledStatement compile(DatabaseConnection databaseConnection, StatementBuilder.StatementType type) throws SQLException {
        return compile(databaseConnection, type, -1);
    }

    @Override // com.j256.ormlite.stmt.PreparedStmt
    public CompiledStatement compile(DatabaseConnection databaseConnection, StatementBuilder.StatementType type, int resultFlags) throws SQLException {
        if (this.type != type) {
            throw new SQLException("Could not compile this " + this.type + " statement since the caller is expecting a " + type + " statement.  Check your QueryBuilder methods.");
        }
        CompiledStatement stmt = databaseConnection.compileStatement(this.statement, type, this.argFieldTypes, resultFlags);
        return assignStatementArguments(stmt);
    }

    @Override // com.j256.ormlite.stmt.PreparedStmt
    public String getStatement() {
        return this.statement;
    }

    @Override // com.j256.ormlite.stmt.PreparedStmt
    public StatementBuilder.StatementType getType() {
        return this.type;
    }

    @Override // com.j256.ormlite.stmt.PreparedStmt
    public void setArgumentHolderValue(int index, Object value) throws SQLException {
        if (index < 0) {
            throw new SQLException("argument holder index " + index + " must be >= 0");
        }
        if (this.argHolders.length <= index) {
            throw new SQLException("argument holder index " + index + " is not valid, only " + this.argHolders.length + " in statement (index starts at 0)");
        }
        this.argHolders[index].setValue(value);
    }

    private CompiledStatement assignStatementArguments(CompiledStatement stmt) throws SQLException {
        SqlType sqlType;
        boolean ok = false;
        try {
            if (this.limit != null) {
                stmt.setMaxRows(this.limit.intValue());
            }
            Object[] argValues = null;
            if (logger.isLevelEnabled(Log.Level.TRACE) && this.argHolders.length > 0) {
                argValues = new Object[this.argHolders.length];
            }
            for (int i = 0; i < this.argHolders.length; i++) {
                Object argValue = this.argHolders[i].getSqlArgValue();
                FieldType fieldType = this.argFieldTypes[i];
                if (fieldType == null) {
                    sqlType = this.argHolders[i].getSqlType();
                } else {
                    sqlType = fieldType.getSqlType();
                }
                stmt.setObject(i, argValue, sqlType);
                if (argValues != null) {
                    argValues[i] = argValue;
                }
            }
            logger.debug("prepared statement '{}' with {} args", this.statement, Integer.valueOf(this.argHolders.length));
            if (argValues != null) {
                logger.trace("prepared statement arguments: {}", (Object) argValues);
            }
            ok = true;
            return stmt;
        } finally {
            if (!ok) {
                stmt.close();
            }
        }
    }
}
