package net.minidev.json.writer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONAware;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.JSONObject;
import net.minidev.json.writer.ArraysMapper;
import net.minidev.json.writer.BeansMapper;
import net.minidev.json.writer.CollectionMapper;
/* loaded from: classes.dex */
public class JsonReader {
    public JsonReaderI<JSONAwareEx> DEFAULT;
    public JsonReaderI<JSONAwareEx> DEFAULT_ORDERED;
    private final ConcurrentHashMap<Type, JsonReaderI<?>> cache = new ConcurrentHashMap<>(100);

    public JsonReader() {
        this.cache.put(Date.class, BeansMapper.MAPPER_DATE);
        this.cache.put(int[].class, ArraysMapper.MAPPER_PRIM_INT);
        this.cache.put(Integer[].class, ArraysMapper.MAPPER_INT);
        this.cache.put(short[].class, ArraysMapper.MAPPER_PRIM_INT);
        this.cache.put(Short[].class, ArraysMapper.MAPPER_INT);
        this.cache.put(long[].class, ArraysMapper.MAPPER_PRIM_LONG);
        this.cache.put(Long[].class, ArraysMapper.MAPPER_LONG);
        this.cache.put(byte[].class, ArraysMapper.MAPPER_PRIM_BYTE);
        this.cache.put(Byte[].class, ArraysMapper.MAPPER_BYTE);
        this.cache.put(char[].class, ArraysMapper.MAPPER_PRIM_CHAR);
        this.cache.put(Character[].class, ArraysMapper.MAPPER_CHAR);
        this.cache.put(float[].class, ArraysMapper.MAPPER_PRIM_FLOAT);
        this.cache.put(Float[].class, ArraysMapper.MAPPER_FLOAT);
        this.cache.put(double[].class, ArraysMapper.MAPPER_PRIM_DOUBLE);
        this.cache.put(Double[].class, ArraysMapper.MAPPER_DOUBLE);
        this.cache.put(boolean[].class, ArraysMapper.MAPPER_PRIM_BOOL);
        this.cache.put(Boolean[].class, ArraysMapper.MAPPER_BOOL);
        this.DEFAULT = new DefaultMapper(this);
        this.DEFAULT_ORDERED = new DefaultMapperOrdered(this);
        this.cache.put(JSONAwareEx.class, this.DEFAULT);
        this.cache.put(JSONAware.class, this.DEFAULT);
        this.cache.put(JSONArray.class, this.DEFAULT);
        this.cache.put(JSONObject.class, this.DEFAULT);
    }

    public <T> void remapField(Class<T> type, String fromJson, String toJava) {
        JsonReaderI<T> map = getMapper((Class) type);
        if (!(map instanceof MapperRemapped)) {
            MapperRemapped mapperRemapped = new MapperRemapped(map);
            registerReader(type, mapperRemapped);
            map = mapperRemapped;
        }
        ((MapperRemapped) map).renameField(fromJson, toJava);
    }

    public <T> void registerReader(Class<T> type, JsonReaderI<T> mapper) {
        this.cache.put(type, mapper);
    }

    public <T> JsonReaderI<T> getMapper(Type type) {
        return type instanceof ParameterizedType ? getMapper((ParameterizedType) type) : getMapper((Class) ((Class) type));
    }

    public <T> JsonReaderI<T> getMapper(Class<T> type) {
        JsonReaderI<?> bean;
        JsonReaderI<T> map = (JsonReaderI<T>) this.cache.get(type);
        if (map != null) {
            return map;
        }
        if (type instanceof Class) {
            if (Map.class.isAssignableFrom(type)) {
                map = new DefaultMapperCollection<>(this, type);
            } else if (List.class.isAssignableFrom(type)) {
                map = new DefaultMapperCollection<>(this, type);
            }
            if (map != null) {
                this.cache.put(type, map);
                return map;
            }
        }
        if (type.isArray()) {
            bean = new ArraysMapper.GenericMapper<>(this, type);
        } else if (List.class.isAssignableFrom(type)) {
            bean = new CollectionMapper.ListClass<>(this, type);
        } else if (Map.class.isAssignableFrom(type)) {
            bean = new CollectionMapper.MapClass<>(this, type);
        } else {
            bean = new BeansMapper.Bean<>(this, type);
        }
        this.cache.putIfAbsent(type, bean);
        return (JsonReaderI<T>) bean;
    }

    public <T> JsonReaderI<T> getMapper(ParameterizedType type) {
        JsonReaderI<T> map = (JsonReaderI<T>) this.cache.get(type);
        if (map != null) {
            return map;
        }
        Class<T> clz = (Class) type.getRawType();
        if (List.class.isAssignableFrom(clz)) {
            map = new CollectionMapper.ListType<>(this, type);
        } else if (Map.class.isAssignableFrom(clz)) {
            map = new CollectionMapper.MapType<>(this, type);
        }
        this.cache.putIfAbsent(type, map);
        return map;
    }
}
