package com.j256.ormlite.field.types;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDateType;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.support.DatabaseResults;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
/* loaded from: classes.dex */
public class DateType extends BaseDateType {
    private static final DateType singleTon = new DateType();

    public static DateType getSingleton() {
        return singleTon;
    }

    private DateType() {
        super(SqlType.DATE, new Class[]{Date.class});
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DateType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        BaseDateType.DateStringFormatConfig dateFormatConfig = convertDateStringConfig(fieldType, getDefaultDateFormatConfig());
        try {
            return new Timestamp(parseDateString(dateFormatConfig, defaultStr).getTime());
        } catch (ParseException e) {
            throw SqlExceptionUtil.create("Problems parsing default date string '" + defaultStr + "' using '" + dateFormatConfig + '\'', e);
        }
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getTimestamp(columnPos);
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        Timestamp value = (Timestamp) sqlArg;
        return new Date(value.getTime());
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        Date date = (Date) javaObject;
        return new Timestamp(date.getTime());
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isArgumentHolderRequired() {
        return true;
    }

    protected BaseDateType.DateStringFormatConfig getDefaultDateFormatConfig() {
        return defaultDateFormatConfig;
    }
}
