package com.jayway.jsonpath.spi.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPathException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class GsonJsonProvider extends AbstractJsonProvider {
    private static final Logger logger = LoggerFactory.getLogger(GsonJsonProvider.class);
    private static final JsonParser parser = new JsonParser();
    private static final Gson gson = new GsonBuilder().create();

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Object unwrap(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof JsonElement) {
            JsonElement e = (JsonElement) o;
            if (e.isJsonNull()) {
                return null;
            }
            if (e.isJsonPrimitive()) {
                JsonPrimitive p = e.getAsJsonPrimitive();
                if (p.isString()) {
                    return p.getAsString();
                }
                if (p.isBoolean()) {
                    return Boolean.valueOf(p.getAsBoolean());
                }
                if (p.isNumber()) {
                    return unwrapNumber(p.getAsNumber());
                }
                return o;
            }
            return o;
        }
        return o;
    }

    private static Number unwrapNumber(Number n) {
        if (n instanceof LazilyParsedNumber) {
            LazilyParsedNumber lpn = (LazilyParsedNumber) n;
            BigDecimal bigDecimal = new BigDecimal(lpn.toString());
            if (bigDecimal.scale() <= 0) {
                if (bigDecimal.compareTo(new BigDecimal(Integer.MAX_VALUE)) <= 0) {
                    Number unwrapped = Integer.valueOf(bigDecimal.intValue());
                    return unwrapped;
                }
                Number unwrapped2 = Long.valueOf(bigDecimal.longValue());
                return unwrapped2;
            }
            Number unwrapped3 = Double.valueOf(bigDecimal.doubleValue());
            return unwrapped3;
        }
        return n;
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object parse(String json) throws InvalidJsonException {
        return parser.parse(json);
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object parse(InputStream jsonStream, String charset) throws InvalidJsonException {
        try {
            return parser.parse(new InputStreamReader(jsonStream, charset));
        } catch (UnsupportedEncodingException e) {
            throw new JsonPathException(e);
        }
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public String toJson(Object obj) {
        return obj.toString();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object createArray() {
        return new JsonArray();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object createMap() {
        return new JsonObject();
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public boolean isArray(Object obj) {
        return (obj instanceof JsonArray) || (obj instanceof List);
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Object getArrayIndex(Object obj, int idx) {
        return toJsonArray(obj).get(idx);
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public void setArrayIndex(Object array, int index, Object newValue) {
        if (!isArray(array)) {
            throw new UnsupportedOperationException();
        }
        toJsonArray(array).set(index, createJsonElement(newValue));
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Object getMapValue(Object obj, String key) {
        JsonObject jsonObject = toJsonObject(obj);
        JsonElement o = jsonObject.get(key);
        return !jsonObject.has(key) ? UNDEFINED : unwrap(o);
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public void setProperty(Object obj, Object key, Object value) {
        int index;
        if (isMap(obj)) {
            toJsonObject(obj).add(key.toString(), createJsonElement(value));
            return;
        }
        JsonArray array = toJsonArray(obj);
        if (key != null) {
            index = key instanceof Integer ? ((Integer) key).intValue() : Integer.parseInt(key.toString());
        } else {
            index = array.size();
        }
        if (index == array.size()) {
            array.add(createJsonElement(value));
        } else {
            array.set(index, createJsonElement(value));
        }
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public void removeProperty(Object obj, Object key) {
        if (isMap(obj)) {
            toJsonObject(obj).remove(key.toString());
            return;
        }
        JsonArray array = toJsonArray(obj);
        int index = key instanceof Integer ? ((Integer) key).intValue() : Integer.parseInt(key.toString());
        array.remove(index);
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public boolean isMap(Object obj) {
        return obj instanceof JsonObject;
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Collection<String> getPropertyKeys(Object obj) {
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : toJsonObject(obj).entrySet()) {
            keys.add(entry.getKey());
        }
        return keys;
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public int length(Object obj) {
        if (isArray(obj)) {
            return toJsonArray(obj).size();
        }
        if (isMap(obj)) {
            return toJsonObject(obj).entrySet().size();
        }
        if (obj instanceof JsonElement) {
            JsonElement element = toJsonElement(obj);
            if (element.isJsonPrimitive()) {
                return element.toString().length();
            }
        }
        throw new JsonPathException(new StringBuilder().append("length operation can not applied to ").append(obj).toString() != null ? obj.getClass().getName() : "null");
    }

    /* JADX WARN: Generic types in debug info not equals: java.lang.Object != java.util.List<java.lang.Object> */
    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Iterable<?> toIterable(Object obj) {
        if (isArray(obj)) {
            JsonArray arr = toJsonArray(obj);
            List<Object> values = new ArrayList<>(arr.size());
            Iterator<JsonElement> it = arr.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                values.add(unwrap(o));
            }
            return values;
        }
        JsonObject jsonObject = toJsonObject(obj);
        List<Object> values2 = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            values2.add(unwrap(entry.getValue()));
        }
        return values2;
    }

    private JsonElement createJsonElement(Object o) {
        return gson.toJsonTree(o);
    }

    private JsonArray toJsonArray(Object o) {
        return (JsonArray) o;
    }

    private JsonObject toJsonObject(Object o) {
        return (JsonObject) o;
    }

    private JsonElement toJsonElement(Object o) {
        return (JsonElement) o;
    }
}
