package com.j256.ormlite.dao;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes.dex */
public class LruObjectCache implements ObjectCache {
    private final int capacity;
    private final ConcurrentHashMap<Class<?>, Map<Object, Object>> classMaps = new ConcurrentHashMap<>();

    public LruObjectCache(int capacity) {
        this.capacity = capacity;
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public synchronized <T> void registerClass(Class<T> clazz) {
        Map<Object, Object> objectMap = this.classMaps.get(clazz);
        if (objectMap == null) {
            Map<Object, Object> objectMap2 = Collections.synchronizedMap(new LimitedLinkedHashMap(this.capacity));
            this.classMaps.put(clazz, objectMap2);
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T, ID> T get(Class<T> clazz, ID id) {
        Map<Object, Object> objectMap = getMapForClass(clazz);
        if (objectMap == null) {
            return null;
        }
        return (T) objectMap.get(id);
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T, ID> void put(Class<T> clazz, ID id, T data) {
        Map<Object, Object> objectMap = getMapForClass(clazz);
        if (objectMap != null) {
            objectMap.put(id, data);
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T> void clear(Class<T> clazz) {
        Map<Object, Object> objectMap = getMapForClass(clazz);
        if (objectMap != null) {
            objectMap.clear();
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public void clearAll() {
        for (Map<Object, Object> objectMap : this.classMaps.values()) {
            objectMap.clear();
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T, ID> void remove(Class<T> clazz, ID id) {
        Map<Object, Object> objectMap = getMapForClass(clazz);
        if (objectMap != null) {
            objectMap.remove(id);
        }
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T, ID> T updateId(Class<T> clazz, ID oldId, ID newId) {
        T t;
        Map<Object, Object> objectMap = getMapForClass(clazz);
        if (objectMap == null || (t = (T) objectMap.remove(oldId)) == null) {
            return null;
        }
        objectMap.put(newId, t);
        return t;
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public <T> int size(Class<T> clazz) {
        Map<Object, Object> objectMap = getMapForClass(clazz);
        if (objectMap == null) {
            return 0;
        }
        return objectMap.size();
    }

    @Override // com.j256.ormlite.dao.ObjectCache
    public int sizeAll() {
        int size = 0;
        for (Map<Object, Object> objectMap : this.classMaps.values()) {
            size += objectMap.size();
        }
        return size;
    }

    private Map<Object, Object> getMapForClass(Class<?> clazz) {
        Map<Object, Object> objectMap = this.classMaps.get(clazz);
        if (objectMap == null) {
            return null;
        }
        return objectMap;
    }

    /* loaded from: classes.dex */
    private static class LimitedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = -4566528080395573236L;
        private final int capacity;

        public LimitedLinkedHashMap(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > this.capacity;
        }
    }
}
