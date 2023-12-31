package com.j256.ormlite.field.types;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDateType;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
/* loaded from: classes.dex */
public class SqlDateType extends DateType {
    private static final SqlDateType singleTon = new SqlDateType();
    private static final BaseDateType.DateStringFormatConfig sqlDateFormatConfig = new BaseDateType.DateStringFormatConfig("yyyy-MM-dd");

    public static SqlDateType getSingleton() {
        return singleTon;
    }

    private SqlDateType() {
        super(SqlType.DATE, new Class[]{Date.class});
    }

    protected SqlDateType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override // com.j256.ormlite.field.types.DateType, com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        Timestamp value = (Timestamp) sqlArg;
        return new Date(value.getTime());
    }

    @Override // com.j256.ormlite.field.types.DateType, com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        Date date = (Date) javaObject;
        return new Timestamp(date.getTime());
    }

    @Override // com.j256.ormlite.field.types.DateType
    protected BaseDateType.DateStringFormatConfig getDefaultDateFormatConfig() {
        return sqlDateFormatConfig;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos) {
        return sqlArgToJava(fieldType, Timestamp.valueOf(stringValue), columnPos);
    }

    @Override // com.j256.ormlite.field.types.BaseDateType, com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isValidForField(Field field) {
        return field.getType() == Date.class;
    }
}
