package com.j256.ormlite.stmt.mapped;

import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.GeneratedKeyHolder;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class MappedCreate<T, ID> extends BaseMappedStatement<T, ID> {
    private String dataClassName;
    private final String queryNextSequenceStmt;
    private int versionFieldTypeIndex;

    private MappedCreate(TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes, String queryNextSequenceStmt, int versionFieldTypeIndex) {
        super(tableInfo, statement, argFieldTypes);
        this.dataClassName = tableInfo.getDataClass().getSimpleName();
        this.queryNextSequenceStmt = queryNextSequenceStmt;
        this.versionFieldTypeIndex = versionFieldTypeIndex;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int insert(DatabaseType databaseType, DatabaseConnection databaseConnection, T data, ObjectCache objectCache) throws SQLException {
        Object foreignObj;
        boolean assignId;
        KeyHolder keyHolder = null;
        if (this.idField != null) {
            if (this.idField.isAllowGeneratedIdInsert() && !this.idField.isObjectsFieldValueDefault(data)) {
                assignId = false;
            } else {
                assignId = true;
            }
            if (this.idField.isSelfGeneratedId() && this.idField.isGeneratedId()) {
                if (assignId) {
                    this.idField.assignField(data, this.idField.generateId(), false, objectCache);
                }
            } else if (this.idField.isGeneratedIdSequence() && databaseType.isSelectSequenceBeforeInsert()) {
                if (assignId) {
                    assignSequenceId(databaseConnection, data, objectCache);
                }
            } else if (this.idField.isGeneratedId() && assignId) {
                keyHolder = new KeyHolder();
            }
        }
        try {
            if (this.tableInfo.isForeignAutoCreate()) {
                FieldType[] arr$ = this.tableInfo.getFieldTypes();
                for (FieldType fieldType : arr$) {
                    if (fieldType.isForeignAutoCreate() && (foreignObj = fieldType.extractRawJavaFieldValue(data)) != null && fieldType.getForeignIdField().isObjectsFieldValueDefault(foreignObj)) {
                        fieldType.createWithForeignDao(foreignObj);
                    }
                }
            }
            Object[] args = getFieldObjects(data);
            Object versionDefaultValue = null;
            if (this.versionFieldTypeIndex >= 0 && args[this.versionFieldTypeIndex] == null) {
                FieldType versionFieldType = this.argFieldTypes[this.versionFieldTypeIndex];
                versionDefaultValue = versionFieldType.moveToNextValue(null);
                args[this.versionFieldTypeIndex] = versionFieldType.convertJavaFieldToSqlArgValue(versionDefaultValue);
            }
            try {
                int rowC = databaseConnection.insert(this.statement, args, this.argFieldTypes, keyHolder);
                logger.debug("insert data with statement '{}' and {} args, changed {} rows", this.statement, Integer.valueOf(args.length), Integer.valueOf(rowC));
                if (args.length > 0) {
                    logger.trace("insert arguments: {}", (Object) args);
                }
                if (rowC > 0) {
                    if (versionDefaultValue != null) {
                        this.argFieldTypes[this.versionFieldTypeIndex].assignField(data, versionDefaultValue, false, null);
                    }
                    if (keyHolder != null) {
                        Number key = keyHolder.getKey();
                        if (key == null) {
                            throw new SQLException("generated-id key was not set by the update call");
                        }
                        if (key.longValue() == 0) {
                            throw new SQLException("generated-id key must not be 0 value");
                        }
                        assignIdValue(data, key, "keyholder", objectCache);
                    }
                    if (objectCache != 0 && foreignCollectionsAreAssigned(this.tableInfo.getForeignCollections(), data)) {
                        Object id = this.idField.extractJavaFieldValue(data);
                        objectCache.put(this.clazz, id, data);
                    }
                }
                return rowC;
            } catch (SQLException e) {
                logger.debug("insert data with statement '{}' and {} args, threw exception: {}", this.statement, Integer.valueOf(args.length), e);
                if (args.length > 0) {
                    logger.trace("insert arguments: {}", (Object) args);
                }
                throw e;
            }
        } catch (SQLException e2) {
            throw SqlExceptionUtil.create("Unable to run insert stmt on object " + data + ": " + this.statement, e2);
        }
    }

    public static <T, ID> MappedCreate<T, ID> build(DatabaseType databaseType, TableInfo<T, ID> tableInfo) {
        int argFieldC;
        StringBuilder sb = new StringBuilder(128);
        appendTableName(databaseType, sb, "INSERT INTO ", tableInfo.getTableName());
        int argFieldC2 = 0;
        int versionFieldTypeIndex = -1;
        FieldType[] arr$ = tableInfo.getFieldTypes();
        for (FieldType fieldType : arr$) {
            if (isFieldCreatable(databaseType, fieldType)) {
                if (fieldType.isVersion()) {
                    versionFieldTypeIndex = argFieldC2;
                }
                argFieldC2++;
            }
        }
        FieldType[] argFieldTypes = new FieldType[argFieldC2];
        if (argFieldC2 == 0) {
            databaseType.appendInsertNoColumns(sb);
        } else {
            boolean first = true;
            sb.append('(');
            FieldType[] arr$2 = tableInfo.getFieldTypes();
            int len$ = arr$2.length;
            int i$ = 0;
            int argFieldC3 = 0;
            while (i$ < len$) {
                FieldType fieldType2 = arr$2[i$];
                if (isFieldCreatable(databaseType, fieldType2)) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }
                    appendFieldColumnName(databaseType, sb, fieldType2, null);
                    argFieldC = argFieldC3 + 1;
                    argFieldTypes[argFieldC3] = fieldType2;
                } else {
                    argFieldC = argFieldC3;
                }
                i$++;
                argFieldC3 = argFieldC;
            }
            sb.append(") VALUES (");
            boolean first2 = true;
            for (FieldType fieldType3 : tableInfo.getFieldTypes()) {
                if (isFieldCreatable(databaseType, fieldType3)) {
                    if (first2) {
                        first2 = false;
                    } else {
                        sb.append(",");
                    }
                    sb.append("?");
                }
            }
            sb.append(")");
        }
        FieldType idField = tableInfo.getIdField();
        String queryNext = buildQueryNextSequence(databaseType, idField);
        return new MappedCreate<>(tableInfo, sb.toString(), argFieldTypes, queryNext, versionFieldTypeIndex);
    }

    private boolean foreignCollectionsAreAssigned(FieldType[] foreignCollections, Object data) throws SQLException {
        for (FieldType fieldType : foreignCollections) {
            if (fieldType.extractJavaFieldValue(data) == null) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFieldCreatable(DatabaseType databaseType, FieldType fieldType) {
        if (fieldType.isForeignCollection() || fieldType.isReadOnly()) {
            return false;
        }
        if (databaseType.isIdSequenceNeeded() && databaseType.isSelectSequenceBeforeInsert()) {
            return true;
        }
        return !fieldType.isGeneratedId() || fieldType.isSelfGeneratedId() || fieldType.isAllowGeneratedIdInsert();
    }

    private static String buildQueryNextSequence(DatabaseType databaseType, FieldType idField) {
        String seqName;
        if (idField == null || (seqName = idField.getGeneratedIdSequence()) == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(64);
        databaseType.appendSelectNextValFromSequence(sb, seqName);
        return sb.toString();
    }

    private void assignSequenceId(DatabaseConnection databaseConnection, T data, ObjectCache objectCache) throws SQLException {
        long seqVal = databaseConnection.queryForLong(this.queryNextSequenceStmt);
        logger.debug("queried for sequence {} using stmt: {}", Long.valueOf(seqVal), this.queryNextSequenceStmt);
        if (seqVal == 0) {
            throw new SQLException("Should not have returned 0 for stmt: " + this.queryNextSequenceStmt);
        }
        assignIdValue(data, Long.valueOf(seqVal), "sequence", objectCache);
    }

    private void assignIdValue(T data, Number val, String label, ObjectCache objectCache) throws SQLException {
        this.idField.assignIdValue(data, val, objectCache);
        if (logger.isLevelEnabled(Log.Level.DEBUG)) {
            logger.debug("assigned id '{}' from {} to '{}' in {} object", new Object[]{val, label, this.idField.getFieldName(), this.dataClassName});
        }
    }

    /* loaded from: classes.dex */
    private static class KeyHolder implements GeneratedKeyHolder {
        Number key;

        private KeyHolder() {
        }

        public Number getKey() {
            return this.key;
        }

        @Override // com.j256.ormlite.support.GeneratedKeyHolder
        public void addKey(Number key) throws SQLException {
            if (this.key == null) {
                this.key = key;
                return;
            }
            throw new SQLException("generated key has already been set to " + this.key + ", now set to " + key);
        }
    }
}
