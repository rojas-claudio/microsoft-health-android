package com.j256.ormlite.stmt;

import com.j256.ormlite.field.SqlType;
/* loaded from: classes.dex */
public class SelectArg extends BaseArgumentHolder implements ArgumentHolder {
    private boolean hasBeenSet;
    private Object value;

    public SelectArg() {
        this.hasBeenSet = false;
        this.value = null;
    }

    public SelectArg(String columnName, Object value) {
        super(columnName);
        this.hasBeenSet = false;
        this.value = null;
        setValue(value);
    }

    public SelectArg(SqlType sqlType, Object value) {
        super(sqlType);
        this.hasBeenSet = false;
        this.value = null;
        setValue(value);
    }

    public SelectArg(SqlType sqlType) {
        super(sqlType);
        this.hasBeenSet = false;
        this.value = null;
    }

    public SelectArg(Object value) {
        this.hasBeenSet = false;
        this.value = null;
        setValue(value);
    }

    @Override // com.j256.ormlite.stmt.BaseArgumentHolder
    protected Object getValue() {
        return this.value;
    }

    @Override // com.j256.ormlite.stmt.BaseArgumentHolder, com.j256.ormlite.stmt.ArgumentHolder
    public void setValue(Object value) {
        this.hasBeenSet = true;
        this.value = value;
    }

    @Override // com.j256.ormlite.stmt.BaseArgumentHolder
    protected boolean isValueSet() {
        return this.hasBeenSet;
    }
}
