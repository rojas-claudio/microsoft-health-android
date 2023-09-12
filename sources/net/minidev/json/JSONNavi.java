package net.minidev.json;

import com.microsoft.kapp.utils.Constants;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import net.minidev.json.writer.JsonReaderI;
/* loaded from: classes.dex */
public class JSONNavi<T> {
    private static final JSONStyle ERROR_COMPRESS = new JSONStyle(2);
    private Object current;
    private boolean failure;
    private String failureMessage;
    private JsonReaderI<? super T> mapper;
    private Object missingKey;
    private Stack<Object> path;
    private boolean readonly;
    private T root;
    private Stack<Object> stack;

    public static JSONNavi<JSONAwareEx> newInstance() {
        return new JSONNavi<>(JSONValue.defaultReader.DEFAULT_ORDERED);
    }

    public static JSONNavi<JSONObject> newInstanceObject() {
        JSONNavi<JSONObject> o = new JSONNavi<>((JsonReaderI<? super JSONObject>) JSONValue.defaultReader.getMapper((Class) JSONObject.class));
        o.object();
        return o;
    }

    public static JSONNavi<JSONArray> newInstanceArray() {
        JSONNavi<JSONArray> o = new JSONNavi<>((JsonReaderI<? super JSONArray>) JSONValue.defaultReader.getMapper((Class) JSONArray.class));
        o.array();
        return o;
    }

    public JSONNavi(JsonReaderI<? super T> mapper) {
        this.stack = new Stack<>();
        this.path = new Stack<>();
        this.failure = false;
        this.readonly = false;
        this.missingKey = null;
        this.mapper = mapper;
    }

    public JSONNavi(String json) {
        this.stack = new Stack<>();
        this.path = new Stack<>();
        this.failure = false;
        this.readonly = false;
        this.missingKey = null;
        this.root = (T) JSONValue.parse(json);
        this.current = this.root;
        this.readonly = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JSONNavi(String json, JsonReaderI<T> mapper) {
        this.stack = new Stack<>();
        this.path = new Stack<>();
        this.failure = false;
        this.readonly = false;
        this.missingKey = null;
        this.root = (T) JSONValue.parse(json, (JsonReaderI<Object>) mapper);
        this.mapper = mapper;
        this.current = this.root;
        this.readonly = true;
    }

    public JSONNavi(String json, Class<T> mapTo) {
        this.stack = new Stack<>();
        this.path = new Stack<>();
        this.failure = false;
        this.readonly = false;
        this.missingKey = null;
        this.root = (T) JSONValue.parse(json, (Class<Object>) mapTo);
        this.mapper = JSONValue.defaultReader.getMapper((Class) mapTo);
        this.current = this.root;
        this.readonly = true;
    }

    public JSONNavi<T> root() {
        this.current = this.root;
        this.stack.clear();
        this.path.clear();
        this.failure = false;
        this.missingKey = null;
        this.failureMessage = null;
        return this;
    }

    public boolean hasFailure() {
        return this.failure;
    }

    public Object getCurrentObject() {
        return this.current;
    }

    public Collection<String> getKeys() {
        if (this.current instanceof Map) {
            return ((Map) this.current).keySet();
        }
        return null;
    }

    public int getSize() {
        if (this.current == null) {
            return 0;
        }
        if (isArray()) {
            return ((List) this.current).size();
        }
        if (isObject()) {
            return ((Map) this.current).size();
        }
        return 1;
    }

    public String getString(String key) {
        if (!hasKey(key)) {
            return null;
        }
        at(key);
        String v = asString();
        up();
        return v;
    }

    public int getInt(String key) {
        if (!hasKey(key)) {
            return 0;
        }
        at(key);
        int v = asInt();
        up();
        return v;
    }

    public Integer getInteger(String key) {
        if (!hasKey(key)) {
            return null;
        }
        at(key);
        Integer v = asIntegerObj();
        up();
        return v;
    }

    public double getDouble(String key) {
        if (!hasKey(key)) {
            return Constants.SPLITS_ACCURACY;
        }
        at(key);
        double v = asDouble();
        up();
        return v;
    }

    public boolean hasKey(String key) {
        if (isObject()) {
            return o(this.current).containsKey(key);
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JSONNavi<?> at(String key) {
        if (!this.failure) {
            if (!isObject()) {
                object();
            }
            if (!(this.current instanceof Map)) {
                return failure("current node is not an Object", key);
            }
            if (!o(this.current).containsKey(key)) {
                if (this.readonly) {
                    return failure("current Object have no key named " + key, key);
                }
                this.stack.add(this.current);
                this.path.add(key);
                this.current = null;
                this.missingKey = key;
                return this;
            }
            Object next = o(this.current).get(key);
            this.stack.add(this.current);
            this.path.add(key);
            this.current = next;
            return this;
        }
        return this;
    }

    public Object get(String key) {
        if (!this.failure) {
            if (!isObject()) {
                object();
            }
            if (!(this.current instanceof Map)) {
                return failure("current node is not an Object", key);
            }
            return o(this.current).get(key);
        }
        return this;
    }

    public Object get(int index) {
        if (!this.failure) {
            if (!isArray()) {
                array();
            }
            if (!(this.current instanceof List)) {
                return failure("current node is not an List", Integer.valueOf(index));
            }
            return a(this.current).get(index);
        }
        return this;
    }

    public JSONNavi<T> set(String key, String value) {
        object();
        if (!this.failure) {
            o(this.current).put(key, value);
        }
        return this;
    }

    public JSONNavi<T> set(String key, Number value) {
        object();
        if (!this.failure) {
            o(this.current).put(key, value);
        }
        return this;
    }

    public JSONNavi<T> set(String key, long value) {
        return set(key, Long.valueOf(value));
    }

    public JSONNavi<T> set(String key, int value) {
        return set(key, Integer.valueOf(value));
    }

    public JSONNavi<T> set(String key, double value) {
        return set(key, Double.valueOf(value));
    }

    public JSONNavi<T> set(String key, float value) {
        return set(key, Float.valueOf(value));
    }

    public JSONNavi<T> add(Object... values) {
        array();
        if (!this.failure) {
            List<Object> list = a(this.current);
            for (Object o : values) {
                list.add(o);
            }
        }
        return this;
    }

    public String asString() {
        if (this.current == null) {
            return null;
        }
        if (this.current instanceof String) {
            return (String) this.current;
        }
        return this.current.toString();
    }

    public double asDouble() {
        if (this.current instanceof Number) {
            return ((Number) this.current).doubleValue();
        }
        return Double.NaN;
    }

    public Double asDoubleObj() {
        if (this.current == null) {
            return null;
        }
        if (this.current instanceof Number) {
            if (this.current instanceof Double) {
                return (Double) this.current;
            }
            return Double.valueOf(((Number) this.current).doubleValue());
        }
        return Double.valueOf(Double.NaN);
    }

    public double asFloat() {
        if (this.current instanceof Number) {
            return ((Number) this.current).floatValue();
        }
        return Double.NaN;
    }

    public Float asFloatObj() {
        if (this.current == null) {
            return null;
        }
        if (this.current instanceof Number) {
            if (this.current instanceof Float) {
                return (Float) this.current;
            }
            return Float.valueOf(((Number) this.current).floatValue());
        }
        return Float.valueOf(Float.NaN);
    }

    public int asInt() {
        if (this.current instanceof Number) {
            return ((Number) this.current).intValue();
        }
        return 0;
    }

    public Integer asIntegerObj() {
        if (this.current != null && (this.current instanceof Number)) {
            if (this.current instanceof Integer) {
                return (Integer) this.current;
            }
            if (this.current instanceof Long) {
                Long l = (Long) this.current;
                if (l.longValue() == l.intValue()) {
                    return Integer.valueOf(l.intValue());
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public long asLong() {
        if (this.current instanceof Number) {
            return ((Number) this.current).longValue();
        }
        return 0L;
    }

    public Long asLongObj() {
        if (this.current != null && (this.current instanceof Number)) {
            if (this.current instanceof Long) {
                return (Long) this.current;
            }
            if (this.current instanceof Integer) {
                return Long.valueOf(((Number) this.current).longValue());
            }
            return null;
        }
        return null;
    }

    public boolean asBoolean() {
        if (this.current instanceof Boolean) {
            return ((Boolean) this.current).booleanValue();
        }
        return false;
    }

    public Boolean asBooleanObj() {
        if (this.current != null && (this.current instanceof Boolean)) {
            return (Boolean) this.current;
        }
        return null;
    }

    public JSONNavi<T> object() {
        if (!this.failure) {
            if (this.current == null && this.readonly) {
                failure("Can not create Object child in readonly", null);
            }
            if (this.current != null) {
                if (!isObject()) {
                    if (isArray()) {
                        failure("can not use Object feature on Array.", null);
                    }
                    failure("Can not use current possition as Object", null);
                }
            } else {
                this.current = this.mapper.createObject();
            }
            if (this.root == null) {
                this.root = (T) this.current;
            } else {
                store();
            }
        }
        return this;
    }

    public JSONNavi<T> array() {
        if (!this.failure) {
            if (this.current == null && this.readonly) {
                failure("Can not create Array child in readonly", null);
            }
            if (this.current != null) {
                if (!isArray()) {
                    if (isObject()) {
                        failure("can not use Object feature on Array.", null);
                    }
                    failure("Can not use current possition as Object", null);
                }
            } else {
                this.current = this.mapper.createArray();
            }
            if (this.root == null) {
                this.root = (T) this.current;
            } else {
                store();
            }
        }
        return this;
    }

    public JSONNavi<T> set(Number num) {
        if (!this.failure) {
            this.current = num;
            store();
        }
        return this;
    }

    public JSONNavi<T> set(Boolean bool) {
        if (!this.failure) {
            this.current = bool;
            store();
        }
        return this;
    }

    public JSONNavi<T> set(String text) {
        if (!this.failure) {
            this.current = text;
            store();
        }
        return this;
    }

    public T getRoot() {
        return this.root;
    }

    private void store() {
        Object parent = this.stack.peek();
        if (isObject(parent)) {
            o(parent).put((String) this.missingKey, this.current);
        } else if (isArray(parent)) {
            int index = ((Number) this.missingKey).intValue();
            List<Object> lst = a(parent);
            while (lst.size() <= index) {
                lst.add(null);
            }
            lst.set(index, this.current);
        }
    }

    public boolean isArray() {
        return isArray(this.current);
    }

    public boolean isObject() {
        return isObject(this.current);
    }

    private boolean isArray(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof List;
    }

    private boolean isObject(Object obj) {
        if (obj == null) {
            return false;
        }
        return obj instanceof Map;
    }

    private List<Object> a(Object obj) {
        return (List) obj;
    }

    private Map<String, Object> o(Object obj) {
        return (Map) obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JSONNavi<?> at(int index) {
        if (!this.failure) {
            if (!(this.current instanceof List)) {
                return failure("current node is not an Array", Integer.valueOf(index));
            }
            List<Object> lst = (List) this.current;
            if (index < 0 && (index = index + lst.size()) < 0) {
                index = 0;
            }
            if (index >= lst.size()) {
                if (this.readonly) {
                    return failure("Out of bound exception for index", Integer.valueOf(index));
                }
                this.stack.add(this.current);
                this.path.add(Integer.valueOf(index));
                this.current = null;
                this.missingKey = Integer.valueOf(index);
                return this;
            }
            Object next = lst.get(index);
            this.stack.add(this.current);
            this.path.add(Integer.valueOf(index));
            this.current = next;
            return this;
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JSONNavi<?> atNext() {
        if (!this.failure) {
            if (!(this.current instanceof List)) {
                return failure("current node is not an Array", null);
            }
            List<Object> lst = (List) this.current;
            return at(lst.size());
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JSONNavi<?> up(int level) {
        while (true) {
            int level2 = level;
            level = level2 - 1;
            if (level2 <= 0 || this.stack.size() <= 0) {
                break;
            }
            this.current = this.stack.pop();
            this.path.pop();
        }
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JSONNavi<?> up() {
        if (this.stack.size() > 0) {
            this.current = this.stack.pop();
            this.path.pop();
        }
        return this;
    }

    public String toString() {
        return this.failure ? JSONValue.toJSONString(this.failureMessage, ERROR_COMPRESS) : JSONValue.toJSONString(this.root);
    }

    public String toString(JSONStyle compression) {
        return this.failure ? JSONValue.toJSONString(this.failureMessage, compression) : JSONValue.toJSONString(this.root, compression);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private JSONNavi<?> failure(String err, Object jPathPostfix) {
        this.failure = true;
        StringBuilder sb = new StringBuilder();
        sb.append("Error: ");
        sb.append(err);
        sb.append(" at ");
        sb.append(getJPath());
        if (jPathPostfix != null) {
            if (jPathPostfix instanceof Integer) {
                sb.append('[').append(jPathPostfix).append(']');
            } else {
                sb.append('/').append(jPathPostfix);
            }
        }
        this.failureMessage = sb.toString();
        return this;
    }

    public String getJPath() {
        StringBuilder sb = new StringBuilder();
        Iterator i$ = this.path.iterator();
        while (i$.hasNext()) {
            Object o = i$.next();
            if (o instanceof String) {
                sb.append('/').append(o.toString());
            } else {
                sb.append('[').append(o.toString()).append(']');
            }
        }
        return sb.toString();
    }
}
