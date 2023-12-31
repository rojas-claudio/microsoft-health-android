package com.j256.ormlite.stmt;

import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
/* loaded from: classes.dex */
public class RawRowMapperImpl<T, ID> implements RawRowMapper<T> {
    private final TableInfo<T, ID> tableInfo;

    public RawRowMapperImpl(TableInfo<T, ID> tableInfo) {
        this.tableInfo = tableInfo;
    }

    @Override // com.j256.ormlite.dao.RawRowMapper
    public T mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
        T rowObj = this.tableInfo.createObject();
        for (int i = 0; i < columnNames.length; i++) {
            if (i < resultColumns.length) {
                FieldType fieldType = this.tableInfo.getFieldTypeByColumnName(columnNames[i]);
                Object fieldObj = fieldType.convertStringToJavaField(resultColumns[i], i);
                fieldType.assignField(rowObj, fieldObj, false, null);
            }
        }
        return rowObj;
    }
}
