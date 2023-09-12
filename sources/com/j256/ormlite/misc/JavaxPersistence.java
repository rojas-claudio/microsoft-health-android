package com.j256.ormlite.misc;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collection;
/* loaded from: classes.dex */
public class JavaxPersistence {
    public static DatabaseFieldConfig createFieldConfig(DatabaseType databaseType, Field field) throws SQLException {
        Annotation columnAnnotation = null;
        Annotation basicAnnotation = null;
        Annotation idAnnotation = null;
        Annotation generatedValueAnnotation = null;
        Annotation oneToOneAnnotation = null;
        Annotation manyToOneAnnotation = null;
        Annotation joinColumnAnnotation = null;
        Annotation enumeratedAnnotation = null;
        Annotation versionAnnotation = null;
        Annotation[] arr$ = field.getAnnotations();
        for (Annotation annotation : arr$) {
            Class<?> annotationClass = annotation.annotationType();
            if (annotationClass.getName().equals("javax.persistence.Column")) {
                columnAnnotation = annotation;
            }
            if (annotationClass.getName().equals("javax.persistence.Basic")) {
                basicAnnotation = annotation;
            }
            if (annotationClass.getName().equals("javax.persistence.Id")) {
                idAnnotation = annotation;
            }
            if (annotationClass.getName().equals("javax.persistence.GeneratedValue")) {
                generatedValueAnnotation = annotation;
            }
            if (annotationClass.getName().equals("javax.persistence.OneToOne")) {
                oneToOneAnnotation = annotation;
            }
            if (annotationClass.getName().equals("javax.persistence.ManyToOne")) {
                manyToOneAnnotation = annotation;
            }
            if (annotationClass.getName().equals("javax.persistence.JoinColumn")) {
                joinColumnAnnotation = annotation;
            }
            if (annotationClass.getName().equals("javax.persistence.Enumerated")) {
                enumeratedAnnotation = annotation;
            }
            if (annotationClass.getName().equals("javax.persistence.Version")) {
                versionAnnotation = annotation;
            }
        }
        if (columnAnnotation == null && basicAnnotation == null && idAnnotation == null && oneToOneAnnotation == null && manyToOneAnnotation == null && enumeratedAnnotation == null && versionAnnotation == null) {
            return null;
        }
        DatabaseFieldConfig config = new DatabaseFieldConfig();
        String fieldName = field.getName();
        if (databaseType.isEntityNamesMustBeUpCase()) {
            fieldName = fieldName.toUpperCase();
        }
        config.setFieldName(fieldName);
        if (columnAnnotation != null) {
            try {
                Method method = columnAnnotation.getClass().getMethod(WorkoutSummary.NAME, new Class[0]);
                String name = (String) method.invoke(columnAnnotation, new Object[0]);
                if (name != null && name.length() > 0) {
                    config.setColumnName(name);
                }
                Method method2 = columnAnnotation.getClass().getMethod("columnDefinition", new Class[0]);
                String columnDefinition = (String) method2.invoke(columnAnnotation, new Object[0]);
                if (columnDefinition != null && columnDefinition.length() > 0) {
                    config.setColumnDefinition(columnDefinition);
                }
                Method method3 = columnAnnotation.getClass().getMethod("length", new Class[0]);
                config.setWidth(((Integer) method3.invoke(columnAnnotation, new Object[0])).intValue());
                Method method4 = columnAnnotation.getClass().getMethod("nullable", new Class[0]);
                Boolean nullable = (Boolean) method4.invoke(columnAnnotation, new Object[0]);
                if (nullable != null) {
                    config.setCanBeNull(nullable.booleanValue());
                }
                Method method5 = columnAnnotation.getClass().getMethod("unique", new Class[0]);
                Boolean unique = (Boolean) method5.invoke(columnAnnotation, new Object[0]);
                if (unique != null) {
                    config.setUnique(unique.booleanValue());
                }
            } catch (Exception e) {
                throw SqlExceptionUtil.create("Problem accessing fields from the @Column annotation for field " + field, e);
            }
        }
        if (basicAnnotation != null) {
            try {
                Method method6 = basicAnnotation.getClass().getMethod("optional", new Class[0]);
                Boolean optional = (Boolean) method6.invoke(basicAnnotation, new Object[0]);
                if (optional == null) {
                    config.setCanBeNull(true);
                } else {
                    config.setCanBeNull(optional.booleanValue());
                }
            } catch (Exception e2) {
                throw SqlExceptionUtil.create("Problem accessing fields from the @Basic annotation for field " + field, e2);
            }
        }
        if (idAnnotation != null) {
            if (generatedValueAnnotation == null) {
                config.setId(true);
            } else {
                config.setGeneratedId(true);
            }
        }
        if (oneToOneAnnotation != null || manyToOneAnnotation != null) {
            if (Collection.class.isAssignableFrom(field.getType()) || ForeignCollection.class.isAssignableFrom(field.getType())) {
                config.setForeignCollection(true);
                if (joinColumnAnnotation != null) {
                    try {
                        Method method7 = joinColumnAnnotation.getClass().getMethod(WorkoutSummary.NAME, new Class[0]);
                        String name2 = (String) method7.invoke(joinColumnAnnotation, new Object[0]);
                        if (name2 != null && name2.length() > 0) {
                            config.setForeignCollectionColumnName(name2);
                        }
                        Method method8 = joinColumnAnnotation.getClass().getMethod("fetch", new Class[0]);
                        Object fetchType = method8.invoke(joinColumnAnnotation, new Object[0]);
                        if (fetchType != null && fetchType.toString().equals("EAGER")) {
                            config.setForeignCollectionEager(true);
                        }
                    } catch (Exception e3) {
                        throw SqlExceptionUtil.create("Problem accessing fields from the @JoinColumn annotation for field " + field, e3);
                    }
                }
            } else {
                config.setForeign(true);
                if (joinColumnAnnotation != null) {
                    try {
                        Method method9 = joinColumnAnnotation.getClass().getMethod(WorkoutSummary.NAME, new Class[0]);
                        String name3 = (String) method9.invoke(joinColumnAnnotation, new Object[0]);
                        if (name3 != null && name3.length() > 0) {
                            config.setColumnName(name3);
                        }
                        Method method10 = joinColumnAnnotation.getClass().getMethod("nullable", new Class[0]);
                        Boolean nullable2 = (Boolean) method10.invoke(joinColumnAnnotation, new Object[0]);
                        if (nullable2 != null) {
                            config.setCanBeNull(nullable2.booleanValue());
                        }
                        Method method11 = joinColumnAnnotation.getClass().getMethod("unique", new Class[0]);
                        Boolean unique2 = (Boolean) method11.invoke(joinColumnAnnotation, new Object[0]);
                        if (unique2 != null) {
                            config.setUnique(unique2.booleanValue());
                        }
                    } catch (Exception e4) {
                        throw SqlExceptionUtil.create("Problem accessing fields from the @JoinColumn annotation for field " + field, e4);
                    }
                }
            }
        }
        if (enumeratedAnnotation != null) {
            try {
                Method method12 = enumeratedAnnotation.getClass().getMethod("value", new Class[0]);
                Object typeValue = method12.invoke(enumeratedAnnotation, new Object[0]);
                if (typeValue != null && typeValue.toString().equals("STRING")) {
                    config.setDataType(DataType.ENUM_STRING);
                } else {
                    config.setDataType(DataType.ENUM_INTEGER);
                }
            } catch (Exception e5) {
                throw SqlExceptionUtil.create("Problem accessing fields from the @Enumerated annotation for field " + field, e5);
            }
        }
        if (versionAnnotation != null) {
            config.setVersion(true);
        }
        if (config.getDataPersister() == null) {
            config.setDataPersister(DataPersisterManager.lookupForField(field));
        }
        config.setUseGetSet((DatabaseFieldConfig.findGetMethod(field, false) == null || DatabaseFieldConfig.findSetMethod(field, false) == null) ? false : true);
        return config;
    }

    public static String getEntityName(Class<?> clazz) {
        Annotation entityAnnotation = null;
        Annotation[] arr$ = clazz.getAnnotations();
        for (Annotation annotation : arr$) {
            Class<?> annotationClass = annotation.annotationType();
            if (annotationClass.getName().equals("javax.persistence.Entity")) {
                entityAnnotation = annotation;
            }
        }
        if (entityAnnotation == null) {
            return null;
        }
        try {
            Method method = entityAnnotation.getClass().getMethod(WorkoutSummary.NAME, new Class[0]);
            String name = (String) method.invoke(entityAnnotation, new Object[0]);
            if (name != null) {
                if (name.length() > 0) {
                    return name;
                }
            }
            return null;
        } catch (Exception e) {
            throw new IllegalStateException("Could not get entity name from class " + clazz, e);
        }
    }
}
