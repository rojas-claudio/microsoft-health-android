package com.j256.ormlite.field.types;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/* loaded from: classes.dex */
public abstract class BaseDateType extends BaseDataType {
    protected static final DateStringFormatConfig defaultDateFormatConfig = new DateStringFormatConfig("yyyy-MM-dd HH:mm:ss.SSSSSS");

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseDateType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static DateStringFormatConfig convertDateStringConfig(FieldType fieldType, DateStringFormatConfig defaultDateFormatConfig2) {
        DateStringFormatConfig configObj;
        return (fieldType == null || (configObj = (DateStringFormatConfig) fieldType.getDataTypeConfigObj()) == null) ? defaultDateFormatConfig2 : configObj;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Date parseDateString(DateStringFormatConfig formatConfig, String dateStr) throws ParseException {
        DateFormat dateFormat = formatConfig.getDateFormat();
        return dateFormat.parse(dateStr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String normalizeDateString(DateStringFormatConfig formatConfig, String dateStr) throws ParseException {
        DateFormat dateFormat = formatConfig.getDateFormat();
        Date date = dateFormat.parse(dateStr);
        return dateFormat.format(date);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class DateStringFormatConfig {
        final String dateFormatStr;
        private final ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() { // from class: com.j256.ormlite.field.types.BaseDateType.DateStringFormatConfig.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.lang.ThreadLocal
            public DateFormat initialValue() {
                return new SimpleDateFormat(DateStringFormatConfig.this.dateFormatStr);
            }
        };

        public DateStringFormatConfig(String dateFormatStr) {
            this.dateFormatStr = dateFormatStr;
        }

        public DateFormat getDateFormat() {
            return this.threadLocal.get();
        }

        public String toString() {
            return this.dateFormatStr;
        }
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isValidForVersion() {
        return true;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public Object moveToNextValue(Object currentValue) {
        long newVal = System.currentTimeMillis();
        if (currentValue == null) {
            return new Date(newVal);
        }
        if (newVal == ((Date) currentValue).getTime()) {
            return new Date(1 + newVal);
        }
        return new Date(newVal);
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isValidForField(Field field) {
        return field.getType() == Date.class;
    }
}
