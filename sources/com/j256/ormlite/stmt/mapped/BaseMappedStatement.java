package com.j256.ormlite.stmt.mapped;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
import java.util.List;
/* loaded from: classes.dex */
public abstract class BaseMappedStatement<T, ID> {
    protected static Logger logger = LoggerFactory.getLogger(BaseMappedStatement.class);
    protected final FieldType[] argFieldTypes;
    protected final Class<T> clazz;
    protected final FieldType idField;
    protected final String statement;
    protected final TableInfo<T, ID> tableInfo;

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseMappedStatement(TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes) {
        this.tableInfo = tableInfo;
        this.clazz = tableInfo.getDataClass();
        this.idField = tableInfo.getIdField();
        this.statement = statement;
        this.argFieldTypes = argFieldTypes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object[] getFieldObjects(Object data) throws SQLException {
        Object[] objects = new Object[this.argFieldTypes.length];
        for (int i = 0; i < this.argFieldTypes.length; i++) {
            FieldType fieldType = this.argFieldTypes[i];
            if (fieldType.isAllowGeneratedIdInsert()) {
                objects[i] = fieldType.getFieldValueIfNotDefault(data);
            } else {
                objects[i] = fieldType.extractJavaFieldToSqlArgValue(data);
            }
            if (objects[i] == null && fieldType.getDefaultValue() != null) {
                objects[i] = fieldType.getDefaultValue();
            }
        }
        return objects;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object convertIdToFieldObject(ID id) throws SQLException {
        return this.idField.convertJavaFieldToSqlArgValue(id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void appendWhereFieldEq(DatabaseType databaseType, FieldType fieldType, StringBuilder sb, List<FieldType> fieldTypeList) {
        sb.append("WHERE ");
        appendFieldColumnName(databaseType, sb, fieldType, fieldTypeList);
        sb.append("= ?");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void appendTableName(DatabaseType databaseType, StringBuilder sb, String prefix, String tableName) {
        if (prefix != null) {
            sb.append(prefix);
        }
        databaseType.appendEscapedEntityName(sb, tableName);
        sb.append(' ');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void appendFieldColumnName(DatabaseType databaseType, StringBuilder sb, FieldType fieldType, List<FieldType> fieldTypeList) {
        databaseType.appendEscapedEntityName(sb, fieldType.getColumnName());
        if (fieldTypeList != null) {
            fieldTypeList.add(fieldType);
        }
        sb.append(' ');
    }

    public String toString() {
        return "MappedStatement: " + this.statement;
    }
}
