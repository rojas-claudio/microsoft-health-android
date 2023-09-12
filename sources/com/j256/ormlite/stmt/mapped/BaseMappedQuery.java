package com.j256.ormlite.stmt.mapped;

import com.j256.ormlite.dao.BaseForeignCollection;
import com.j256.ormlite.dao.ObjectCache;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.GenericRowMapper;
import com.j256.ormlite.support.DatabaseResults;
import com.j256.ormlite.table.TableInfo;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class BaseMappedQuery<T, ID> extends BaseMappedStatement<T, ID> implements GenericRowMapper<T> {
    private Map<String, Integer> columnPositions;
    private Object parent;
    private Object parentId;
    protected final FieldType[] resultsFieldTypes;

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseMappedQuery(TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes, FieldType[] resultsFieldTypes) {
        super(tableInfo, statement, argFieldTypes);
        this.columnPositions = null;
        this.parent = null;
        this.parentId = null;
        this.resultsFieldTypes = resultsFieldTypes;
    }

    @Override // com.j256.ormlite.stmt.GenericRowMapper
    public T mapRow(DatabaseResults results) throws SQLException {
        Map<String, Integer> colPosMap;
        BaseForeignCollection<?, ?> collection;
        if (this.columnPositions == null) {
            colPosMap = new HashMap<>();
        } else {
            colPosMap = this.columnPositions;
        }
        ObjectCache objectCache = results.getObjectCache();
        if (objectCache != null) {
            Object id = this.idField.resultToJava(results, colPosMap);
            T cachedInstance = (T) objectCache.get(this.clazz, id);
            if (cachedInstance != null) {
                return cachedInstance;
            }
        }
        T instance = this.tableInfo.createObject();
        ID id2 = null;
        boolean foreignCollections = false;
        FieldType[] arr$ = this.resultsFieldTypes;
        for (FieldType fieldType : arr$) {
            if (fieldType.isForeignCollection()) {
                foreignCollections = true;
            } else {
                Object val = fieldType.resultToJava(results, colPosMap);
                if (val != null && this.parent != null && fieldType.getField().getType() == this.parent.getClass() && val.equals(this.parentId)) {
                    fieldType.assignField(instance, this.parent, true, objectCache);
                } else {
                    fieldType.assignField(instance, val, false, objectCache);
                }
                if (fieldType == this.idField) {
                    id2 = val;
                }
            }
        }
        if (foreignCollections) {
            FieldType[] arr$2 = this.resultsFieldTypes;
            for (FieldType fieldType2 : arr$2) {
                if (fieldType2.isForeignCollection() && (collection = fieldType2.buildForeignCollection(instance, id2)) != null) {
                    fieldType2.assignField(instance, collection, false, objectCache);
                }
            }
        }
        if (objectCache != null && id2 != null) {
            objectCache.put(this.clazz, id2, instance);
        }
        if (this.columnPositions == null) {
            this.columnPositions = colPosMap;
        }
        return instance;
    }

    public void setParentInformation(Object parent, Object parentId) {
        this.parent = parent;
        this.parentId = parentId;
    }
}
