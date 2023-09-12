package com.j256.ormlite.stmt.mapped;

import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class MappedQueryForId<T, ID> extends BaseMappedQuery<T, ID> {
    private final String label;

    /* JADX INFO: Access modifiers changed from: protected */
    public MappedQueryForId(TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes, FieldType[] resultsFieldTypes, String label) {
        super(tableInfo, statement, argFieldTypes, resultsFieldTypes);
        this.label = label;
    }

    public T execute(DatabaseConnection databaseConnection, ID id, ObjectCache objectCache) throws SQLException {
        T result;
        if (objectCache == null || (result = (T) objectCache.get(this.clazz, id)) == null) {
            Object[] args = {convertIdToFieldObject(id)};
            T t = (T) databaseConnection.queryForOne(this.statement, args, this.argFieldTypes, this, objectCache);
            if (t == null) {
                logger.debug("{} using '{}' and {} args, got no results", this.label, this.statement, Integer.valueOf(args.length));
            } else if (t == DatabaseConnection.MORE_THAN_ONE) {
                logger.error("{} using '{}' and {} args, got >1 results", this.label, this.statement, Integer.valueOf(args.length));
                logArgs(args);
                throw new SQLException(this.label + " got more than 1 result: " + this.statement);
            } else {
                logger.debug("{} using '{}' and {} args, got 1 result", this.label, this.statement, Integer.valueOf(args.length));
            }
            logArgs(args);
            return t;
        }
        return result;
    }

    public static <T, ID> MappedQueryForId<T, ID> build(DatabaseType databaseType, TableInfo<T, ID> tableInfo, FieldType idFieldType) throws SQLException {
        if (idFieldType == null && (idFieldType = tableInfo.getIdField()) == null) {
            throw new SQLException("Cannot query-for-id with " + tableInfo.getDataClass() + " because it doesn't have an id field");
        }
        String statement = buildStatement(databaseType, tableInfo, idFieldType);
        return new MappedQueryForId<>(tableInfo, statement, new FieldType[]{idFieldType}, tableInfo.getFieldTypes(), "query-for-id");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static <T, ID> String buildStatement(DatabaseType databaseType, TableInfo<T, ID> tableInfo, FieldType idFieldType) {
        StringBuilder sb = new StringBuilder(64);
        appendTableName(databaseType, sb, "SELECT * FROM ", tableInfo.getTableName());
        appendWhereFieldEq(databaseType, idFieldType, sb, null);
        return sb.toString();
    }

    private void logArgs(Object[] args) {
        if (args.length > 0) {
            logger.trace("{} arguments: {}", this.label, args);
        }
    }
}
