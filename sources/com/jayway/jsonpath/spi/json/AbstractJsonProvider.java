package com.jayway.jsonpath.spi.json;

import com.jayway.jsonpath.JsonPathException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public abstract class AbstractJsonProvider implements JsonProvider {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJsonProvider.class);

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public boolean isArray(Object obj) {
        return obj instanceof List;
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object getArrayIndex(Object obj, int idx) {
        return ((List) obj).get(idx);
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public final Object getArrayIndex(Object obj, int idx, boolean unwrap) {
        return getArrayIndex(obj, idx);
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public void setArrayIndex(Object array, int index, Object newValue) {
        if (!isArray(array)) {
            throw new UnsupportedOperationException();
        }
        ((List) array).set(index, newValue);
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object getMapValue(Object obj, String key) {
        Map m = (Map) obj;
        return !m.containsKey(key) ? JsonProvider.UNDEFINED : m.get(key);
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public void setProperty(Object obj, Object key, Object value) {
        int index;
        if (isMap(obj)) {
            ((Map) obj).put(key.toString(), value);
            return;
        }
        List list = (List) obj;
        if (key != null) {
            index = key instanceof Integer ? ((Integer) key).intValue() : Integer.parseInt(key.toString());
        } else {
            index = list.size();
        }
        list.add(index, value);
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public void removeProperty(Object obj, Object key) {
        if (isMap(obj)) {
            ((Map) obj).remove(key.toString());
            return;
        }
        List list = (List) obj;
        int index = key instanceof Integer ? ((Integer) key).intValue() : Integer.parseInt(key.toString());
        list.remove(index);
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public boolean isMap(Object obj) {
        return obj instanceof Map;
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Collection<String> getPropertyKeys(Object obj) {
        if (isArray(obj)) {
            throw new UnsupportedOperationException();
        }
        return ((Map) obj).keySet();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public int length(Object obj) {
        if (isArray(obj)) {
            return ((List) obj).size();
        }
        if (isMap(obj)) {
            return getPropertyKeys(obj).size();
        }
        if (obj instanceof String) {
            return ((String) obj).length();
        }
        throw new JsonPathException(new StringBuilder().append("length operation can not applied to ").append(obj).toString() != null ? obj.getClass().getName() : "null");
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Iterable<? extends Object> toIterable(Object obj) {
        return isArray(obj) ? (Iterable) obj : ((Map) obj).values();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object unwrap(Object obj) {
        return obj;
    }
}
