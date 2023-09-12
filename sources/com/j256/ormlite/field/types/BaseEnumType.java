package com.j256.ormlite.field.types;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import java.lang.reflect.Field;
import java.sql.SQLException;
/* loaded from: classes.dex */
public abstract class BaseEnumType extends BaseDataType {
    /* JADX INFO: Access modifiers changed from: protected */
    public BaseEnumType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Enum<?> enumVal(FieldType fieldType, Object val, Enum<?> enumVal, Enum<?> unknownEnumVal) throws SQLException {
        if (enumVal == null) {
            if (unknownEnumVal == null) {
                throw new SQLException("Cannot get enum value of '" + val + "' for field " + fieldType);
            }
            return unknownEnumVal;
        }
        return enumVal;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isValidForField(Field field) {
        return field.getType().isEnum();
    }
}
