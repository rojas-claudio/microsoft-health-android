package com.j256.ormlite.field.types;

import com.j256.ormlite.field.SqlType;
/* loaded from: classes.dex */
public class LongType extends LongObjectType {
    private static final LongType singleTon = new LongType();

    public static LongType getSingleton() {
        return singleTon;
    }

    private LongType() {
        super(SqlType.LONG, new Class[]{Long.TYPE});
    }

    protected LongType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isPrimitive() {
        return true;
    }
}
