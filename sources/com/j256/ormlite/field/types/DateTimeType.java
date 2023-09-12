package com.j256.ormlite.field.types;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.DatabaseResults;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class DateTimeType extends BaseDataType {
    private static final DateTimeType singleTon = new DateTimeType();
    private static Class<?> dateTimeClass = null;
    private static Method getMillisMethod = null;
    private static Constructor<?> millisConstructor = null;
    private static final String[] associatedClassNames = {"org.joda.time.DateTime"};

    private DateTimeType() {
        super(SqlType.LONG, new Class[0]);
    }

    protected DateTimeType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    public static DateTimeType getSingleton() {
        return singleTon;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public String[] getAssociatedClassNames() {
        return associatedClassNames;
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        try {
            Method method = getMillisMethod();
            if (javaObject == null) {
                return null;
            }
            return method.invoke(javaObject, new Object[0]);
        } catch (Exception e) {
            throw SqlExceptionUtil.create("Could not use reflection to get millis from Joda DateTime: " + javaObject, e);
        }
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Long.valueOf(Long.parseLong(defaultStr));
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return Long.valueOf(results.getLong(columnPos));
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        try {
            Constructor<?> constructor = getConstructor();
            return constructor.newInstance((Long) sqlArg);
        } catch (Exception e) {
            throw SqlExceptionUtil.create("Could not use reflection to construct a Joda DateTime", e);
        }
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isEscapedValue() {
        return false;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isAppropriateId() {
        return false;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public Class<?> getPrimaryClass() {
        try {
            return getDateTimeClass();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private Method getMillisMethod() throws Exception {
        if (getMillisMethod == null) {
            Class<?> clazz = getDateTimeClass();
            getMillisMethod = clazz.getMethod("getMillis", new Class[0]);
        }
        return getMillisMethod;
    }

    private Constructor<?> getConstructor() throws Exception {
        if (millisConstructor == null) {
            Class<?> clazz = getDateTimeClass();
            millisConstructor = clazz.getConstructor(Long.TYPE);
        }
        return millisConstructor;
    }

    private Class<?> getDateTimeClass() throws ClassNotFoundException {
        if (dateTimeClass == null) {
            dateTimeClass = Class.forName("org.joda.time.DateTime");
        }
        return dateTimeClass;
    }
}
