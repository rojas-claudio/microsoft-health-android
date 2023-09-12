package com.j256.ormlite.stmt.mapped;

import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class MappedUpdate<T, ID> extends BaseMappedStatement<T, ID> {
    private final FieldType versionFieldType;
    private final int versionFieldTypeIndex;

    private MappedUpdate(TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes, FieldType versionFieldType, int versionFieldTypeIndex) {
        super(tableInfo, statement, argFieldTypes);
        this.versionFieldType = versionFieldType;
        this.versionFieldTypeIndex = versionFieldTypeIndex;
    }

    public static <T, ID> MappedUpdate<T, ID> build(DatabaseType databaseType, TableInfo<T, ID> tableInfo) throws SQLException {
        int argFieldC;
        FieldType idField = tableInfo.getIdField();
        if (idField == null) {
            throw new SQLException("Cannot update " + tableInfo.getDataClass() + " because it doesn't have an id field");
        }
        StringBuilder sb = new StringBuilder(64);
        appendTableName(databaseType, sb, "UPDATE ", tableInfo.getTableName());
        boolean first = true;
        int argFieldC2 = 0;
        FieldType versionFieldType = null;
        int versionFieldTypeIndex = -1;
        FieldType[] arr$ = tableInfo.getFieldTypes();
        for (FieldType fieldType : arr$) {
            if (isFieldUpdatable(fieldType, idField)) {
                if (fieldType.isVersion()) {
                    versionFieldType = fieldType;
                    versionFieldTypeIndex = argFieldC2;
                }
                argFieldC2++;
            }
        }
        int argFieldC3 = argFieldC2 + 1;
        if (versionFieldType != null) {
            argFieldC3++;
        }
        FieldType[] argFieldTypes = new FieldType[argFieldC3];
        FieldType[] arr$2 = tableInfo.getFieldTypes();
        int len$ = arr$2.length;
        int i$ = 0;
        int argFieldC4 = 0;
        while (i$ < len$) {
            FieldType fieldType2 = arr$2[i$];
            if (isFieldUpdatable(fieldType2, idField)) {
                if (first) {
                    sb.append("SET ");
                    first = false;
                } else {
                    sb.append(", ");
                }
                appendFieldColumnName(databaseType, sb, fieldType2, null);
                argFieldC = argFieldC4 + 1;
                argFieldTypes[argFieldC4] = fieldType2;
                sb.append("= ?");
            } else {
                argFieldC = argFieldC4;
            }
            i$++;
            argFieldC4 = argFieldC;
        }
        sb.append(' ');
        appendWhereFieldEq(databaseType, idField, sb, null);
        int argFieldC5 = argFieldC4 + 1;
        argFieldTypes[argFieldC4] = idField;
        if (versionFieldType != null) {
            sb.append(" AND ");
            appendFieldColumnName(databaseType, sb, versionFieldType, null);
            sb.append("= ?");
            int i = argFieldC5 + 1;
            argFieldTypes[argFieldC5] = versionFieldType;
        }
        return new MappedUpdate<>(tableInfo, sb.toString(), argFieldTypes, versionFieldType, versionFieldTypeIndex);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int update(DatabaseConnection databaseConnection, T data, ObjectCache objectCache) throws SQLException {
        try {
            if (this.argFieldTypes.length <= 1) {
                return 0;
            }
            Object[] args = getFieldObjects(data);
            Object newVersion = null;
            if (this.versionFieldType != null) {
                Object newVersion2 = this.versionFieldType.extractJavaFieldValue(data);
                newVersion = this.versionFieldType.moveToNextValue(newVersion2);
                args[this.versionFieldTypeIndex] = this.versionFieldType.convertJavaFieldToSqlArgValue(newVersion);
            }
            int rowC = databaseConnection.update(this.statement, args, this.argFieldTypes);
            if (rowC > 0) {
                if (newVersion != null) {
                    this.versionFieldType.assignField(data, newVersion, false, null);
                }
                if (objectCache != 0) {
                    Object id = this.idField.extractJavaFieldValue(data);
                    Object obj = objectCache.get(this.clazz, id);
                    if (obj != null && obj != data) {
                        FieldType[] arr$ = this.tableInfo.getFieldTypes();
                        for (FieldType fieldType : arr$) {
                            if (fieldType != this.idField) {
                                fieldType.assignField(obj, fieldType.extractJavaFieldValue(data), false, objectCache);
                            }
                        }
                    }
                }
            }
            logger.debug("update data with statement '{}' and {} args, changed {} rows", this.statement, Integer.valueOf(args.length), Integer.valueOf(rowC));
            if (args.length > 0) {
                logger.trace("update arguments: {}", (Object) args);
                return rowC;
            }
            return rowC;
        } catch (SQLException e) {
            throw SqlExceptionUtil.create("Unable to run update stmt on object " + data + ": " + this.statement, e);
        }
    }

    private static boolean isFieldUpdatable(FieldType fieldType, FieldType idField) {
        return (fieldType == idField || fieldType.isForeignCollection() || fieldType.isReadOnly()) ? false : true;
    }
}
