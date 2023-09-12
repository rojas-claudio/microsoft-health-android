package com.j256.ormlite.field.types;

import com.j256.ormlite.field.BaseFieldConverter;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.support.DatabaseResults;
import java.lang.reflect.Field;
import java.sql.SQLException;
/* loaded from: classes.dex */
public abstract class BaseDataType extends BaseFieldConverter implements DataPersister {
    private final Class<?>[] classes;
    private final SqlType sqlType;

    @Override // com.j256.ormlite.field.FieldConverter
    public abstract Object parseDefaultString(FieldType fieldType, String str) throws SQLException;

    @Override // com.j256.ormlite.field.FieldConverter
    public abstract Object resultToSqlArg(FieldType fieldType, DatabaseResults databaseResults, int i) throws SQLException;

    public BaseDataType(SqlType sqlType, Class<?>[] classes) {
        this.sqlType = sqlType;
        this.classes = classes;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isValidForField(Field field) {
        if (this.classes.length == 0) {
            return true;
        }
        Class<?>[] arr$ = this.classes;
        for (Class<?> clazz : arr$) {
            if (clazz.isAssignableFrom(field.getType())) {
                return true;
            }
        }
        return false;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public Class<?> getPrimaryClass() {
        if (this.classes.length == 0) {
            return null;
        }
        return this.classes[0];
    }

    @Override // com.j256.ormlite.field.DataPersister
    public Object makeConfigObject(FieldType fieldType) throws SQLException {
        return null;
    }

    @Override // com.j256.ormlite.field.FieldConverter
    public SqlType getSqlType() {
        return this.sqlType;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public Class<?>[] getAssociatedClasses() {
        return this.classes;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public String[] getAssociatedClassNames() {
        return null;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public Object convertIdNumber(Number number) {
        return null;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isValidGeneratedType() {
        return false;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isEscapedDefaultValue() {
        return isEscapedValue();
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isEscapedValue() {
        return true;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isPrimitive() {
        return false;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isComparable() {
        return true;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isAppropriateId() {
        return true;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isArgumentHolderRequired() {
        return false;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isSelfGeneratedId() {
        return false;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public Object generateId() {
        throw new IllegalStateException("Should not have tried to generate this type");
    }

    @Override // com.j256.ormlite.field.DataPersister
    public int getDefaultWidth() {
        return 0;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean dataIsEqual(Object fieldObj1, Object fieldObj2) {
        if (fieldObj1 == null) {
            return fieldObj2 == null;
        } else if (fieldObj2 != null) {
            return fieldObj1.equals(fieldObj2);
        } else {
            return false;
        }
    }

    @Override // com.j256.ormlite.field.DataPersister
    public boolean isValidForVersion() {
        return false;
    }

    @Override // com.j256.ormlite.field.DataPersister
    public Object moveToNextValue(Object currentValue) {
        return null;
    }

    @Override // com.j256.ormlite.field.FieldConverter
    public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos) throws SQLException {
        return parseDefaultString(fieldType, stringValue);
    }
}
