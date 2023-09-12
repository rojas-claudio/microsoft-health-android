package com.j256.ormlite.stmt.query;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.ArgumentHolder;
import com.j256.ormlite.stmt.ColumnArg;
import com.j256.ormlite.stmt.SelectArg;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
/* loaded from: classes.dex */
abstract class BaseComparison implements Comparison {
    private static final String NUMBER_CHARACTERS = "0123456789.-+";
    protected final String columnName;
    protected final FieldType fieldType;
    private final Object value;

    @Override // com.j256.ormlite.stmt.query.Comparison
    public abstract void appendOperation(StringBuilder sb);

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseComparison(String columnName, FieldType fieldType, Object value, boolean isComparison) throws SQLException {
        if (isComparison && fieldType != null && !fieldType.isComparable()) {
            throw new SQLException("Field '" + columnName + "' is of data type " + fieldType.getDataPersister() + " which can not be compared");
        }
        this.columnName = columnName;
        this.fieldType = fieldType;
        this.value = value;
    }

    @Override // com.j256.ormlite.stmt.query.Clause
    public void appendSql(DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> argList) throws SQLException {
        if (tableName != null) {
            databaseType.appendEscapedEntityName(sb, tableName);
            sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
        }
        databaseType.appendEscapedEntityName(sb, this.columnName);
        sb.append(' ');
        appendOperation(sb);
        appendValue(databaseType, sb, argList);
    }

    @Override // com.j256.ormlite.stmt.query.Comparison
    public String getColumnName() {
        return this.columnName;
    }

    @Override // com.j256.ormlite.stmt.query.Comparison
    public void appendValue(DatabaseType databaseType, StringBuilder sb, List<ArgumentHolder> argList) throws SQLException {
        appendArgOrValue(databaseType, this.fieldType, sb, argList, this.value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void appendArgOrValue(DatabaseType databaseType, FieldType fieldType, StringBuilder sb, List<ArgumentHolder> argList, Object argOrValue) throws SQLException {
        boolean appendSpace = true;
        if (argOrValue == null) {
            throw new SQLException("argument for '" + fieldType.getFieldName() + "' is null");
        }
        if (argOrValue instanceof ArgumentHolder) {
            sb.append('?');
            ArgumentHolder argHolder = (ArgumentHolder) argOrValue;
            argHolder.setMetaInfo(this.columnName, fieldType);
            argList.add(argHolder);
        } else if (argOrValue instanceof ColumnArg) {
            ColumnArg columnArg = (ColumnArg) argOrValue;
            String tableName = columnArg.getTableName();
            if (tableName != null) {
                databaseType.appendEscapedEntityName(sb, tableName);
                sb.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
            }
            databaseType.appendEscapedEntityName(sb, columnArg.getColumnName());
        } else if (fieldType.isArgumentHolderRequired()) {
            sb.append('?');
            ArgumentHolder argHolder2 = new SelectArg();
            argHolder2.setMetaInfo(this.columnName, fieldType);
            argHolder2.setValue(argOrValue);
            argList.add(argHolder2);
        } else if (fieldType.isForeign() && fieldType.getType().isAssignableFrom(argOrValue.getClass())) {
            FieldType idFieldType = fieldType.getForeignIdField();
            appendArgOrValue(databaseType, idFieldType, sb, argList, idFieldType.extractJavaFieldValue(argOrValue));
            appendSpace = false;
        } else if (fieldType.isEscapedValue()) {
            databaseType.appendEscapedWord(sb, fieldType.convertJavaFieldToSqlArgValue(argOrValue).toString());
        } else if (fieldType.isForeign()) {
            String value = fieldType.convertJavaFieldToSqlArgValue(argOrValue).toString();
            if (value.length() > 0 && NUMBER_CHARACTERS.indexOf(value.charAt(0)) < 0) {
                throw new SQLException("Foreign field " + fieldType + " does not seem to be producing a numerical value '" + value + "'. Maybe you are passing the wrong object to comparison: " + this);
            }
            sb.append(value);
        } else {
            sb.append(fieldType.convertJavaFieldToSqlArgValue(argOrValue));
        }
        if (appendSpace) {
            sb.append(' ');
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.columnName).append(' ');
        appendOperation(sb);
        sb.append(' ');
        sb.append(this.value);
        return sb.toString();
    }
}
