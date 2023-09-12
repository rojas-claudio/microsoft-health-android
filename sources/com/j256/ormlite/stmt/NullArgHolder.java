package com.j256.ormlite.stmt;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
/* loaded from: classes.dex */
public class NullArgHolder implements ArgumentHolder {
    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public String getColumnName() {
        return "null-holder";
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public void setValue(Object value) {
        throw new UnsupportedOperationException("Cannot set null on " + getClass());
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public void setMetaInfo(String columnName) {
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public void setMetaInfo(FieldType fieldType) {
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public void setMetaInfo(String columnName, FieldType fieldType) {
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public Object getSqlArgValue() {
        return null;
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public SqlType getSqlType() {
        return SqlType.STRING;
    }

    @Override // com.j256.ormlite.stmt.ArgumentHolder
    public FieldType getFieldType() {
        return null;
    }
}
