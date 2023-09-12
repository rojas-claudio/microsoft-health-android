package com.j256.ormlite.dao;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public class ReferenceObjectCache implements ObjectCache {
    private final ConcurrentHashMap<Class<?>, Map<Object, Reference<Object>>> classMaps = new ConcurrentHashMap<>();
    private final boolean useWeak;

    public ReferenceObjectCache(boolean useWeak) {
        this.useWeak = useWeak;
    }

    public static ReferenceObjectCache makeWeakCache() {
        return new ReferenceObjectCache(true);
    }

    public static ReferenceObjectCache makeSoftCache() {
        return new ReferenceObjectCache(false);
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public synchronized <T> void registerClass(Class<T> clazz) {
        Map<Object, Reference<Object>> objectMap = this.classMaps.get(clazz);
        if (objectMap == null) {
            Map<Object, Reference<Object>> objectMap2 = new ConcurrentHashMap<>();
            this.classMaps.put(clazz, objectMap2);
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T, ID> T get(Class<T> clazz, ID id) {
        Reference<Object> ref;
        Map<Object, Reference<Object>> objectMap = getMapForClass(clazz);
        if (objectMap == null || (ref = objectMap.get(id)) == null) {
            return null;
        }
        T t = (T) ref.get();
        if (t == null) {
            objectMap.remove(id);
            return null;
        }
        return t;
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T, ID> void put(Class<T> clazz, ID id, T data) {
        Map<Object, Reference<Object>> objectMap = getMapForClass(clazz);
        if (objectMap != null) {
            if (this.useWeak) {
                objectMap.put(id, new WeakReference(data));
            } else {
                objectMap.put(id, new SoftReference(data));
            }
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T> void clear(Class<T> clazz) {
        Map<Object, Reference<Object>> objectMap = getMapForClass(clazz);
        if (objectMap != null) {
            objectMap.clear();
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public void clearAll() {
        for (Map<Object, Reference<Object>> objectMap : this.classMaps.values()) {
            objectMap.clear();
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T, ID> void remove(Class<T> clazz, ID id) {
        Map<Object, Reference<Object>> objectMap = getMapForClass(clazz);
        if (objectMap != null) {
            objectMap.remove(id);
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T, ID> T updateId(Class<T> clazz, ID oldId, ID newId) {
        Reference<Object> ref;
        Map<Object, Reference<Object>> objectMap = getMapForClass(clazz);
        if (objectMap == null || (ref = objectMap.remove(oldId)) == null) {
            return null;
        }
        objectMap.put(newId, ref);
        return (T) ref.get();
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T> int size(Class<T> clazz) {
        Map<Object, Reference<Object>> objectMap = getMapForClass(clazz);
        if (objectMap == null) {
            return 0;
        }
        return objectMap.size();
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public int sizeAll() {
        int size = 0;
        for (Map<Object, Reference<Object>> objectMap : this.classMaps.values()) {
            size += objectMap.size();
        }
        return size;
    }

    public <T> void cleanNullReferences(Class<T> clazz) {
        Map<Object, Reference<Object>> objectMap = getMapForClass(clazz);
        if (objectMap != null) {
            cleanMap(objectMap);
        }
    }

    public <T> void cleanNullReferencesAll() {
        for (Map<Object, Reference<Object>> objectMap : this.classMaps.values()) {
            cleanMap(objectMap);
        }
    }

    private void cleanMap(Map<Object, Reference<Object>> objectMap) {
        Iterator<Map.Entry<Object, Reference<Object>>> iterator = objectMap.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue().get() == null) {
                iterator.remove();
            }
        }
    }

    private Map<Object, Reference<Object>> getMapForClass(Class<?> clazz) {
        Map<Object, Reference<Object>> objectMap = this.classMaps.get(clazz);
        if (objectMap == null) {
            return null;
        }
        return objectMap;
    }
}
