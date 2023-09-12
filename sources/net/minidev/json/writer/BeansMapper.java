package net.minidev.json.writer;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import net.minidev.asm.Accessor;
import net.minidev.asm.BeansAccess;
import net.minidev.asm.ConvertDate;
import net.minidev.json.JSONUtil;
/* loaded from: classes.dex */
public abstract class BeansMapper<T> extends JsonReaderI<T> {
    public static JsonReaderI<Date> MAPPER_DATE = new ArraysMapper<Date>(null) { // from class: net.minidev.json.writer.BeansMapper.1
        @Override // net.minidev.json.writer.ArraysMapper, net.minidev.json.writer.JsonReaderI
        public Date convert(Object current) {
            return ConvertDate.convertToDate(current);
        }
    };

    @Override // net.minidev.json.writer.JsonReaderI
    public abstract Object getValue(Object obj, String str);

    public BeansMapper(JsonReader base) {
        super(base);
    }

    /* loaded from: classes.dex */
    public static class Bean<T> extends JsonReaderI<T> {
        final BeansAccess<T> ba;
        final Class<T> clz;
        final HashMap<String, Accessor> index;

        public Bean(JsonReader base, Class<T> clz) {
            super(base);
            this.clz = clz;
            this.ba = BeansAccess.get(clz, JSONUtil.JSON_SMART_FIELD_FILTER);
            this.index = this.ba.getMap();
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public void setValue(Object current, String key, Object value) {
            this.ba.set((BeansAccess<T>) current, key, value);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object getValue(Object current, String key) {
            return this.ba.get((BeansAccess<T>) current, key);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Type getType(String key) {
            Accessor nfo = this.index.get(key);
            return nfo.getGenericType();
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startArray(String key) {
            Accessor nfo = this.index.get(key);
            if (nfo == null) {
                throw new RuntimeException("Can not find Array '" + key + "' field in " + this.clz);
            }
            return this.base.getMapper(nfo.getGenericType());
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startObject(String key) {
            Accessor f = this.index.get(key);
            if (f == null) {
                throw new RuntimeException("Can not find Object '" + key + "' field in " + this.clz);
            }
            return this.base.getMapper(f.getGenericType());
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object createObject() {
            return this.ba.newInstance();
        }
    }

    /* loaded from: classes.dex */
    public static class BeanNoConv<T> extends JsonReaderI<T> {
        final BeansAccess<T> ba;
        final Class<T> clz;
        final HashMap<String, Accessor> index;

        public BeanNoConv(JsonReader base, Class<T> clz) {
            super(base);
            this.clz = clz;
            this.ba = BeansAccess.get(clz, JSONUtil.JSON_SMART_FIELD_FILTER);
            this.index = this.ba.getMap();
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public void setValue(Object current, String key, Object value) {
            this.ba.set((BeansAccess<T>) current, key, value);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object getValue(Object current, String key) {
            return this.ba.get((BeansAccess<T>) current, key);
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Type getType(String key) {
            Accessor nfo = this.index.get(key);
            return nfo.getGenericType();
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startArray(String key) {
            Accessor nfo = this.index.get(key);
            if (nfo == null) {
                throw new RuntimeException("Can not set " + key + " field in " + this.clz);
            }
            return this.base.getMapper(nfo.getGenericType());
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public JsonReaderI<?> startObject(String key) {
            Accessor f = this.index.get(key);
            if (f == null) {
                throw new RuntimeException("Can not set " + key + " field in " + this.clz);
            }
            return this.base.getMapper(f.getGenericType());
        }

        @Override // net.minidev.json.writer.JsonReaderI
        public Object createObject() {
            return this.ba.newInstance();
        }
    }
}
