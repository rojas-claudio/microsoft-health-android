package com.j256.ormlite.stmt;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import java.sql.SQLException;
/* loaded from: classes.dex */
public abstract class BaseArgumentHolder implements ArgumentHolder {
    private String columnName;
    private FieldType fieldType;
    private SqlType sqlType;

    protected abstract Object getValue();

    protected abstract boolean isValueSet();

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public abstract void setValue(Object obj);

    public BaseArgumentHolder() {
        this.columnName = null;
        this.fieldType = null;
        this.sqlType = null;
    }

    public BaseArgumentHolder(String columName) {
        this.columnName = null;
        this.fieldType = null;
        this.sqlType = null;
        this.columnName = columName;
    }

    public BaseArgumentHolder(SqlType sqlType) {
        this.columnName = null;
        this.fieldType = null;
        this.sqlType = null;
        this.sqlType = sqlType;
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public String getColumnName() {
        return this.columnName;
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public void setMetaInfo(String columnName) {
        if (this.columnName != null && !this.columnName.equals(columnName)) {
            throw new IllegalArgumentException("Column name cannot be set twice from " + this.columnName + " to " + columnName + ".  Using a SelectArg twice in query with different columns?");
        }
        this.columnName = columnName;
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public void setMetaInfo(FieldType fieldType) {
        if (this.fieldType != null && this.fieldType != fieldType) {
            throw new IllegalArgumentException("FieldType name cannot be set twice from " + this.fieldType + " to " + fieldType + ".  Using a SelectArg twice in query with different columns?");
        }
        this.fieldType = fieldType;
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public void setMetaInfo(String columnName, FieldType fieldType) {
        setMetaInfo(columnName);
        setMetaInfo(fieldType);
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public Object getSqlArgValue() throws SQLException {
        if (!isValueSet()) {
            throw new SQLException("Column value has not been set for " + this.columnName);
        }
        Object value = getValue();
        if (value == null) {
            return null;
        }
        if (this.fieldType != null) {
            if (this.fieldType.isForeign() && this.fieldType.getType() == value.getClass()) {
                FieldType idFieldType = this.fieldType.getForeignIdField();
                return idFieldType.extractJavaFieldValue(value);
            }
            return this.fieldType.convertJavaFieldToSqlArgValue(value);
        }
        return value;
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public FieldType getFieldType() {
        return this.fieldType;
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public SqlType getSqlType() {
        return this.sqlType;
    }

    public String toString() {
        if (!isValueSet()) {
            return "[unset]";
        }
        try {
            Object val = getSqlArgValue();
            if (val == null) {
                return "[null]";
            }
            return val.toString();
        } catch (SQLException e) {
            return "[could not get value: " + e + "]";
        }
    }
}
