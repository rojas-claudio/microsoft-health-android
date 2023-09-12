package com.j256.ormlite.table;

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.misc.JavaxPersistence;
import com.j256.ormlite.support.ConnectionSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class DatabaseTableConfig<T> {
    private Constructor<T> constructor;
    private Class<T> dataClass;
    private List<DatabaseFieldConfig> fieldConfigs;
    private FieldType[] fieldTypes;
    private String tableName;

    public DatabaseTableConfig() {
    }

    public DatabaseTableConfig(Class<T> dataClass, List<DatabaseFieldConfig> fieldConfigs) {
        this(dataClass, extractTableName(dataClass), fieldConfigs);
    }

    public DatabaseTableConfig(Class<T> dataClass, String tableName, List<DatabaseFieldConfig> fieldConfigs) {
        this.dataClass = dataClass;
        this.tableName = tableName;
        this.fieldConfigs = fieldConfigs;
    }

    private DatabaseTableConfig(Class<T> dataClass, String tableName, FieldType[] fieldTypes) {
        this.dataClass = dataClass;
        this.tableName = tableName;
        this.fieldTypes = fieldTypes;
    }

    public void initialize() {
        if (this.dataClass == null) {
            throw new IllegalStateException("dataClass was never set on " + getClass().getSimpleName());
        }
        if (this.tableName == null) {
            this.tableName = extractTableName(this.dataClass);
        }
    }

    public Class<T> getDataClass() {
        return this.dataClass;
    }

    public void setDataClass(Class<T> dataClass) {
        this.dataClass = dataClass;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setFieldConfigs(List<DatabaseFieldConfig> fieldConfigs) {
        this.fieldConfigs = fieldConfigs;
    }

    public void extractFieldTypes(ConnectionSource connectionSource) throws SQLException {
        if (this.fieldTypes == null) {
            if (this.fieldConfigs == null) {
                this.fieldTypes = extractFieldTypes(connectionSource, this.dataClass, this.tableName);
            } else {
                this.fieldTypes = convertFieldConfigs(connectionSource, this.tableName, this.fieldConfigs);
            }
        }
    }

    public FieldType[] getFieldTypes(DatabaseType databaseType) throws SQLException {
        if (this.fieldTypes == null) {
            throw new SQLException("Field types have not been extracted in table config");
        }
        return this.fieldTypes;
    }

    public List<DatabaseFieldConfig> getFieldConfigs() {
        return this.fieldConfigs;
    }

    public Constructor<T> getConstructor() {
        if (this.constructor == null) {
            this.constructor = findNoArgConstructor(this.dataClass);
        }
        return this.constructor;
    }

    public void setConstructor(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public static <T> DatabaseTableConfig<T> fromClass(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
        String tableName = extractTableName(clazz);
        if (connectionSource.getDatabaseType().isEntityNamesMustBeUpCase()) {
            tableName = tableName.toUpperCase();
        }
        return new DatabaseTableConfig<>(clazz, tableName, extractFieldTypes(connectionSource, clazz, tableName));
    }

    public static <T> String extractTableName(Class<T> clazz) {
        DatabaseTable databaseTable = (DatabaseTable) clazz.getAnnotation(DatabaseTable.class);
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName().length() > 0) {
            return databaseTable.tableName();
        }
        String name = JavaxPersistence.getEntityName(clazz);
        if (name == null) {
            return clazz.getSimpleName().toLowerCase();
        }
        return name;
    }

    public static <T> Constructor<T> findNoArgConstructor(Class<T> dataClass) {
        try {
            for (Constructor<?> constructor : dataClass.getDeclaredConstructors()) {
                Constructor<T> con = (Constructor<T>) constructor;
                if (con.getParameterTypes().length == 0) {
                    if (!con.isAccessible()) {
                        try {
                            con.setAccessible(true);
                        } catch (SecurityException e) {
                            throw new IllegalArgumentException("Could not open access to constructor for " + dataClass);
                        }
                    }
                    return con;
                }
            }
            if (dataClass.getEnclosingClass() == null) {
                throw new IllegalArgumentException("Can't find a no-arg constructor for " + dataClass);
            }
            throw new IllegalArgumentException("Can't find a no-arg constructor for " + dataClass + ".  Missing static on inner class?");
        } catch (Exception e2) {
            throw new IllegalArgumentException("Can't lookup declared constructors for " + dataClass, e2);
        }
    }

    private static <T> FieldType[] extractFieldTypes(ConnectionSource connectionSource, Class<T> clazz, String tableName) throws SQLException {
        List<FieldType> fieldTypes = new ArrayList<>();
        for (Class<T> cls = clazz; cls != null; cls = cls.getSuperclass()) {
            Field[] arr$ = cls.getDeclaredFields();
            for (Field field : arr$) {
                FieldType fieldType = FieldType.createFieldType(connectionSource, tableName, field, clazz);
                if (fieldType != null) {
                    fieldTypes.add(fieldType);
                }
            }
        }
        if (fieldTypes.isEmpty()) {
            throw new IllegalArgumentException("No fields have a " + DatabaseField.class.getSimpleName() + " annotation in " + clazz);
        }
        return (FieldType[]) fieldTypes.toArray(new FieldType[fieldTypes.size()]);
    }

    private FieldType[] convertFieldConfigs(ConnectionSource connectionSource, String tableName, List<DatabaseFieldConfig> fieldConfigs) throws SQLException {
        Field field;
        List<FieldType> fieldTypes = new ArrayList<>();
        for (DatabaseFieldConfig fieldConfig : fieldConfigs) {
            FieldType fieldType = null;
            Class<T> cls = this.dataClass;
            while (true) {
                if (cls == null) {
                    break;
                }
                try {
                    field = cls.getDeclaredField(fieldConfig.getFieldName());
                } catch (NoSuchFieldException e) {
                }
                if (field == null) {
                    cls = cls.getSuperclass();
                } else {
                    fieldType = new FieldType(connectionSource, tableName, field, fieldConfig, this.dataClass);
                    break;
                }
            }
            if (fieldType == null) {
                throw new SQLException("Could not find declared field with name '" + fieldConfig.getFieldName() + "' for " + this.dataClass);
            }
            fieldTypes.add(fieldType);
        }
        if (fieldTypes.isEmpty()) {
            throw new SQLException("No fields were configured for class " + this.dataClass);
        }
        return (FieldType[]) fieldTypes.toArray(new FieldType[fieldTypes.size()]);
    }
}
