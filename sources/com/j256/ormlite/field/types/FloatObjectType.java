package com.j256.ormlite.field.types;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.support.DatabaseResults;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class FloatObjectType extends BaseDataType {
    private static final FloatObjectType singleTon = new FloatObjectType();

    public static FloatObjectType getSingleton() {
        return singleTon;
    }

    private FloatObjectType() {
        super(SqlType.FLOAT, new Class[]{Float.class});
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FloatObjectType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return Float.valueOf(results.getFloat(columnPos));
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Float.valueOf(Float.parseFloat(defaultStr));
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isEscapedValue() {
        return false;
    }
}
