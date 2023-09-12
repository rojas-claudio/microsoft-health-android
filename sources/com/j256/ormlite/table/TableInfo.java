package com.j256.ormlite.table;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.misc.SqlExceptionUtil;
import com.j256.ormlite.stmt.query.SimpleComparison;
import com.j256.ormlite.support.ConnectionSource;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class TableInfo<T, ID> {
    private static final FieldType[] NO_FOREIGN_COLLECTIONS = new FieldType[0];
    private final BaseDaoImpl<T, ID> baseDaoImpl;
    private final Constructor<T> constructor;
    private final Class<T> dataClass;
    private Map<String, FieldType> fieldNameMap;
    private final FieldType[] fieldTypes;
    private final boolean foreignAutoCreate;
    private final FieldType[] foreignCollections;
    private final FieldType idField;
    private final String tableName;

    public TableInfo(ConnectionSource connectionSource, BaseDaoImpl<T, ID> baseDaoImpl, Class<T> dataClass) throws SQLException {
        this(connectionSource.getDatabaseType(), baseDaoImpl, DatabaseTableConfig.fromClass(connectionSource, dataClass));
    }

    public TableInfo(DatabaseType databaseType, BaseDaoImpl<T, ID> baseDaoImpl, DatabaseTableConfig<T> tableConfig) throws SQLException {
        this.baseDaoImpl = baseDaoImpl;
        this.dataClass = tableConfig.getDataClass();
        this.tableName = tableConfig.getTableName();
        this.fieldTypes = tableConfig.getFieldTypes(databaseType);
        FieldType findIdFieldType = null;
        boolean foreignAutoCreate = false;
        int foreignCollectionCount = 0;
        FieldType[] arr$ = this.fieldTypes;
        for (FieldType fieldType : arr$) {
            if (fieldType.isId() || fieldType.isGeneratedId() || fieldType.isGeneratedIdSequence()) {
                if (findIdFieldType != null) {
                    throw new SQLException("More than 1 idField configured for class " + this.dataClass + " (" + findIdFieldType + "," + fieldType + ")");
                }
                findIdFieldType = fieldType;
            }
            foreignAutoCreate = fieldType.isForeignAutoCreate() ? true : foreignAutoCreate;
            if (fieldType.isForeignCollection()) {
                foreignCollectionCount++;
            }
        }
        this.idField = findIdFieldType;
        this.constructor = tableConfig.getConstructor();
        this.foreignAutoCreate = foreignAutoCreate;
        if (foreignCollectionCount == 0) {
            this.foreignCollections = NO_FOREIGN_COLLECTIONS;
            return;
        }
        this.foreignCollections = new FieldType[foreignCollectionCount];
        int foreignCollectionCount2 = 0;
        FieldType[] arr$2 = this.fieldTypes;
        for (FieldType fieldType2 : arr$2) {
            if (fieldType2.isForeignCollection()) {
                this.foreignCollections[foreignCollectionCount2] = fieldType2;
                foreignCollectionCount2++;
            }
        }
    }

    public Class<T> getDataClass() {
        return this.dataClass;
    }

    public String getTableName() {
        return this.tableName;
    }

    public FieldType[] getFieldTypes() {
        return this.fieldTypes;
    }

    public FieldType getFieldTypeByColumnName(String columnName) {
        if (this.fieldNameMap == null) {
            Map<String, FieldType> map = new HashMap<>();
            FieldType[] arr$ = this.fieldTypes;
            for (FieldType fieldType : arr$) {
                map.put(fieldType.getColumnName().toLowerCase(), fieldType);
            }
            this.fieldNameMap = map;
        }
        FieldType fieldType2 = this.fieldNameMap.get(columnName.toLowerCase());
        if (fieldType2 != null) {
            return fieldType2;
        }
        FieldType[] arr$2 = this.fieldTypes;
        for (FieldType fieldType22 : arr$2) {
            if (fieldType22.getFieldName().equals(columnName)) {
                throw new IllegalArgumentException("You should use columnName '" + fieldType22.getColumnName() + "' for table " + this.tableName + " instead of fieldName '" + fieldType22.getFieldName() + "'");
            }
        }
        throw new IllegalArgumentException("Unknown column name '" + columnName + "' in table " + this.tableName);
    }

    public FieldType getIdField() {
        return this.idField;
    }

    public Constructor<T> getConstructor() {
        return this.constructor;
    }

    public String objectToString(T object) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(object.getClass().getSimpleName());
        FieldType[] arr$ = this.fieldTypes;
        for (FieldType fieldType : arr$) {
            sb.append(' ').append(fieldType.getColumnName()).append(SimpleComparison.EQUAL_TO_OPERATION);
            try {
                sb.append(fieldType.extractJavaFieldValue(object));
            } catch (Exception e) {
                throw new IllegalStateException("Could not generate toString of field " + fieldType, e);
            }
        }
        return sb.toString();
    }

    public T createObject() throws SQLException {
        T instance;
        ObjectFactory<T> factory = null;
        try {
            if (this.baseDaoImpl != null) {
                factory = this.baseDaoImpl.getObjectFactory();
            }
            if (factory == null) {
                instance = this.constructor.newInstance(new Object[0]);
            } else {
                instance = factory.createObject(this.constructor, this.baseDaoImpl.getDataClass());
            }
            wireNewInstance(this.baseDaoImpl, instance);
            return instance;
        } catch (Exception e) {
            throw SqlExceptionUtil.create("Could not create object for " + this.constructor.getDeclaringClass(), e);
        }
    }

    public boolean isUpdatable() {
        return this.idField != null && this.fieldTypes.length > 1;
    }

    public boolean isForeignAutoCreate() {
        return this.foreignAutoCreate;
    }

    public FieldType[] getForeignCollections() {
        return this.foreignCollections;
    }

    public boolean hasColumnName(String columnName) {
        FieldType[] arr$ = this.fieldTypes;
        for (FieldType fieldType : arr$) {
            if (fieldType.getColumnName().equals(columnName)) {
                return true;
            }
        }
        return false;
    }

    private static <T, ID> void wireNewInstance(BaseDaoImpl<T, ID> baseDaoImpl, T instance) {
        if (instance instanceof BaseDaoEnabled) {
            BaseDaoEnabled<T, ID> daoEnabled = (BaseDaoEnabled) instance;
            daoEnabled.setDao(baseDaoImpl);
        }
    }
}
