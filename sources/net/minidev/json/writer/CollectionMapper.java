package net.minidev.json.writer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import net.minidev.asm.BeansAccess;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONUtil;
/* loaded from: classes.dex */
public class CollectionMapper {

    /* loaded from: classes.dex */
    public static class MapType<T> extends JsonReaderI<T> {
        final BeansAccess<?> ba;
        final Class<?> instance;
        final Class<?> keyClass;
        final Type keyType;
        final Class<?> rawClass;
        JsonReaderI<?> subMapper;
        final ParameterizedType type;
        final Class<?> valueClass;
        final Type valueType;

        public MapType(JsonReader base, ParameterizedType type) {
            super(base);
            this.type = type;
            this.rawClass = (Class) type.getRawType();
            if (this.rawClass.isInterface()) {
                this.instance = JSONObject.class;
            } else {
                this.instance = this.rawClass;
            }
            this.ba = BeansAccess.get(this.instance, JSONUtil.JSON_SMART_FIELD_FILTER);
            this.keyType = type.getActualTypeArguments()[0];
            this.valueType = type.getActualTypeArguments()[1];
            if (this.keyType instanceof Class) {
                this.keyClass = (Class) this.keyType;
            } else {
                this.keyClass = (Class) ((ParameterizedType) this.keyType).getRawType();
            }
            if (this.valueType instanceof Class) {
                this.valueClass = (Class) this.valueType;
            } else {
                this.valueClass = (Class) ((ParameterizedType) this.valueType).getRawType();
            }
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object createObject() {
            try {
                return this.instance.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            } catch (InstantiationException e2) {
                e2.printStackTrace();
                return null;
            }
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startArray(String key) {
            if (this.subMapper == null) {
                this.subMapper = this.base.getMapper(this.valueType);
            }
            return this.subMapper;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startObject(String key) {
            if (this.subMapper == null) {
                this.subMapper = this.base.getMapper(this.valueType);
            }
            return this.subMapper;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public void setValue(Object current, String key, Object value) {
            ((Map) current).put(JSONUtil.convertToX(key, this.keyClass), JSONUtil.convertToX(value, this.valueClass));
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object getValue(Object current, String key) {
            return ((Map) current).get(JSONUtil.convertToX(key, this.keyClass));
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Type getType(String key) {
            return this.type;
        }
    }

    /* loaded from: classes.dex */
    public static class MapClass<T> extends JsonReaderI<T> {
        final BeansAccess<?> ba;
        final Class<?> instance;
        JsonReaderI<?> subMapper;
        final Class<?> type;

        public MapClass(JsonReader base, Class<?> type) {
            super(base);
            this.type = type;
            if (type.isInterface()) {
                this.instance = JSONObject.class;
            } else {
                this.instance = type;
            }
            this.ba = BeansAccess.get(this.instance, JSONUtil.JSON_SMART_FIELD_FILTER);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object createObject() {
            return this.ba.newInstance();
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startArray(String key) {
            return this.base.DEFAULT;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startObject(String key) {
            return this.base.DEFAULT;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public void setValue(Object current, String key, Object value) {
            ((Map) current).put(key, value);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object getValue(Object current, String key) {
            return ((Map) current).get(key);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Type getType(String key) {
            return this.type;
        }
    }

    /* loaded from: classes.dex */
    public static class ListType<T> extends JsonReaderI<T> {
        final BeansAccess<?> ba;
        final Class<?> instance;
        final Class<?> rawClass;
        JsonReaderI<?> subMapper;
        final ParameterizedType type;
        final Class<?> valueClass;
        final Type valueType;

        public ListType(JsonReader base, ParameterizedType type) {
            super(base);
            this.type = type;
            this.rawClass = (Class) type.getRawType();
            if (this.rawClass.isInterface()) {
                this.instance = JSONArray.class;
            } else {
                this.instance = this.rawClass;
            }
            this.ba = BeansAccess.get(this.instance, JSONUtil.JSON_SMART_FIELD_FILTER);
            this.valueType = type.getActualTypeArguments()[0];
            if (this.valueType instanceof Class) {
                this.valueClass = (Class) this.valueType;
            } else {
                this.valueClass = (Class) ((ParameterizedType) this.valueType).getRawType();
            }
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object createArray() {
            return this.ba.newInstance();
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startArray(String key) {
            if (this.subMapper == null) {
                this.subMapper = this.base.getMapper(this.type.getActualTypeArguments()[0]);
            }
            return this.subMapper;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startObject(String key) {
            if (this.subMapper == null) {
                this.subMapper = this.base.getMapper(this.type.getActualTypeArguments()[0]);
            }
            return this.subMapper;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public void addValue(Object current, Object value) {
            ((List) current).add(JSONUtil.convertToX(value, this.valueClass));
        }
    }

    /* loaded from: classes.dex */
    public static class ListClass<T> extends JsonReaderI<T> {
        final BeansAccess<?> ba;
        final Class<?> instance;
        JsonReaderI<?> subMapper;
        final Class<?> type;

        public ListClass(JsonReader base, Class<?> clazz) {
            super(base);
            this.type = clazz;
            if (clazz.isInterface()) {
                this.instance = JSONArray.class;
            } else {
                this.instance = clazz;
            }
            this.ba = BeansAccess.get(this.instance, JSONUtil.JSON_SMART_FIELD_FILTER);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object createArray() {
            return this.ba.newInstance();
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startArray(String key) {
            return this.base.DEFAULT;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startObject(String key) {
            return this.base.DEFAULT;
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public void addValue(Object current, Object value) {
            ((List) current).add(value);
        }
    }
}
