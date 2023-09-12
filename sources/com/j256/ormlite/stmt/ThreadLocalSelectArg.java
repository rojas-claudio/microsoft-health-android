package com.j256.ormlite.stmt;

import com.j256.ormlite.field.SqlType;
/* loaded from: classes.dex */
public class ThreadLocalSelectArg extends BaseArgumentHolder implements ArgumentHolder {
    private ThreadLocal<ValueWrapper> threadValue;

    public ThreadLocalSelectArg() {
        this.threadValue = new ThreadLocal<>();
    }

    public ThreadLocalSelectArg(String columnName, Object value) {
        super(columnName);
        this.threadValue = new ThreadLocal<>();
        setValue(value);
    }

    public ThreadLocalSelectArg(SqlType sqlType, Object value) {
        super(sqlType);
        this.threadValue = new ThreadLocal<>();
        setValue(value);
    }

    public ThreadLocalSelectArg(Object value) {
        this.threadValue = new ThreadLocal<>();
        setValue(value);
    }

    @Override // com.j256.ormlite.stmt.BaseArgumentHolder
    protected Object getValue() {
        ValueWrapper wrapper = this.threadValue.get();
        if (wrapper == null) {
            return null;
        }
        return wrapper.value;
    }

    @Override // com.j256.ormlite.stmt.BaseArgumentHolder, com.j256.ormlite.stmt.ArgumentHolder
    public void setValue(Object value) {
        this.threadValue.set(new ValueWrapper(value));
    }

    @Override // com.j256.ormlite.stmt.BaseArgumentHolder
    protected boolean isValueSet() {
        return this.threadValue.get() != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ValueWrapper {
        Object value;

        public ValueWrapper(Object value) {
            this.value = value;
        }
    }
}
