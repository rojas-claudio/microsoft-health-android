package com.j256.ormlite.field.types;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.support.DatabaseResults;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class EnumIntegerType extends BaseEnumType {
    private static final EnumIntegerType singleTon = new EnumIntegerType();

    public static EnumIntegerType getSingleton() {
        return singleTon;
    }

    private EnumIntegerType() {
        super(SqlType.INTEGER, new Class[0]);
    }

    protected EnumIntegerType(SqlType sqlType, Class<?>[] classes) {
        super(sqlType, classes);
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Integer.valueOf(Integer.parseInt(defaultStr));
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return Integer.valueOf(results.getInt(columnPos));
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        if (fieldType != null) {
            Integer valInteger = (Integer) sqlArg;
            Map<Integer, Enum<?>> enumIntMap = (Map) fieldType.getDataTypeConfigObj();
            if (enumIntMap == null) {
                return enumVal(fieldType, valInteger, null, fieldType.getUnknownEnumVal());
            }
            return enumVal(fieldType, valInteger, enumIntMap.get(valInteger), fieldType.getUnknownEnumVal());
        }
        return sqlArg;
    }

    @Override // com.j256.ormlite.field.BaseFieldConverter, com.j256.ormlite.field.FieldConverter
    public Object javaToSqlArg(FieldType fieldType, Object obj) {
        Enum<?> enumVal = (Enum) obj;
        return Integer.valueOf(enumVal.ordinal());
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public boolean isEscapedValue() {
        return false;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public Object makeConfigObject(FieldType fieldType) throws SQLException {
        Map<Integer, Enum<?>> enumIntMap = new HashMap<>();
        Enum<?>[] constants = (Enum[]) fieldType.getType().getEnumConstants();
        if (constants == null) {
            throw new SQLException("Field " + fieldType + " improperly configured as type " + this);
        }
        for (Enum<?> enumVal : constants) {
            enumIntMap.put(Integer.valueOf(enumVal.ordinal()), enumVal);
        }
        return enumIntMap;
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.FieldConverter
    public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos) throws SQLException {
        return sqlArgToJava(fieldType, Integer.valueOf(Integer.parseInt(stringValue)), columnPos);
    }

    @Override // com.j256.ormlite.field.types.BaseDataType, com.j256.ormlite.field.DataPersister
    public Class<?> getPrimaryClass() {
        return Integer.TYPE;
    }
}
