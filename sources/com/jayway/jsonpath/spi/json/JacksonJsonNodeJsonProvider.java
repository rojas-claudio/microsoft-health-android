package com.jayway.jsonpath.spi.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPathException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class JacksonJsonNodeJsonProvider extends AbstractJsonProvider {
    protected ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(JacksonJsonProvider.class);
    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    public JacksonJsonNodeJsonProvider() {
        this(defaultObjectMapper);
    }

    public JacksonJsonNodeJsonProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object parse(String json) throws InvalidJsonException {
        try {
            return this.objectMapper.readTree(json);
        } catch (IOException e) {
            logger.debug("Invalid JSON: \n" + json);
            throw new InvalidJsonException(e);
        }
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object parse(InputStream jsonStream, String charset) throws InvalidJsonException {
        try {
            return this.objectMapper.readTree(new InputStreamReader(jsonStream, charset));
        } catch (IOException e) {
            throw new InvalidJsonException(e);
        }
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public String toJson(Object obj) {
        if (!(obj instanceof JsonNode)) {
            throw new JsonPathException("Not a JSON Node");
        }
        return obj.toString();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object createArray() {
        return JsonNodeFactory.instance.arrayNode();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object createMap() {
        return JsonNodeFactory.instance.objectNode();
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Object unwrap(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof JsonNode) {
            JsonNode e = (JsonNode) o;
            if (e.isValueNode()) {
                if (e.isTextual()) {
                    return e.asText();
                }
                if (e.isBoolean()) {
                    return Boolean.valueOf(e.asBoolean());
                }
                if (e.isInt()) {
                    return Integer.valueOf(e.asInt());
                }
                if (e.isLong()) {
                    return Long.valueOf(e.asLong());
                }
                if (e.isBigDecimal()) {
                    return e.decimalValue();
                }
                if (e.isDouble()) {
                    return Double.valueOf(e.doubleValue());
                }
                if (e.isFloat()) {
                    return Float.valueOf(e.floatValue());
                }
                if (e.isBigDecimal()) {
                    return e.decimalValue();
                }
                if (e.isNull()) {
                    return null;
                }
                return o;
            }
            return o;
        }
        return o;
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public boolean isArray(Object obj) {
        return (obj instanceof ArrayNode) || (obj instanceof List);
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
        toJsonArray(array).insert(index, createJsonElement(newValue));
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Object getMapValue(Object obj, String key) {
        ObjectNode jsonObject = toJsonObject(obj);
        JsonNode o = jsonObject.get(key);
        return !jsonObject.has(key) ? UNDEFINED : unwrap(o);
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public void setProperty(Object obj, Object key, Object value) {
        int index;
        if (isMap(obj)) {
            toJsonObject(obj).put(key.toString(), createJsonElement(value));
            return;
        }
        ArrayNode array = toJsonArray(obj);
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
        ArrayNode array = toJsonArray(obj);
        int index = key instanceof Integer ? ((Integer) key).intValue() : Integer.parseInt(key.toString());
        array.remove(index);
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public boolean isMap(Object obj) {
        return obj instanceof ObjectNode;
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Collection<String> getPropertyKeys(Object obj) {
        ArrayList arrayList = new ArrayList();
        Iterator<String> iter = toJsonObject(obj).fieldNames();
        while (iter.hasNext()) {
            arrayList.add(iter.next());
        }
        return arrayList;
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public int length(Object obj) {
        if (isArray(obj)) {
            return toJsonArray(obj).size();
        }
        if (isMap(obj)) {
            return toJsonObject(obj).size();
        }
        if (obj instanceof TextNode) {
            TextNode element = (TextNode) obj;
            return element.size();
        }
        throw new JsonPathException(new StringBuilder().append("length operation can not applied to ").append(obj).toString() != null ? obj.getClass().getName() : "null");
    }

    @Override // com.jayway.jsonpath.spi.json.AbstractJsonProvider, com.jayway.jsonpath.spi.json.JsonProvider
    public Iterable<?> toIterable(Object obj) {
        List<Object> values;
        if (isArray(obj)) {
            ArrayNode arr = toJsonArray(obj);
            values = new ArrayList<>(arr.size());
            Iterator it = arr.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                values.add(unwrap(o));
            }
        } else {
            values = new ArrayList<>();
            Iterator<JsonNode> iter = toJsonObject(obj).elements();
            while (iter.hasNext()) {
                values.add(unwrap(iter.next()));
            }
        }
        return values;
    }

    private JsonNode createJsonElement(Object o) {
        return this.objectMapper.valueToTree(o);
    }

    private ArrayNode toJsonArray(Object o) {
        return (ArrayNode) o;
    }

    private ObjectNode toJsonObject(Object o) {
        return (ObjectNode) o;
    }
}
