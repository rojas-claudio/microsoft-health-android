package net.minidev.json.writer;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class DefaultMapperCollection<T> extends JsonReaderI<T> {
    Class<T> clz;

    public DefaultMapperCollection(JsonReader base, Class<T> clz) {
        super(base);
        this.clz = clz;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<T> startObject(String key) {
        return this;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<T> startArray(String key) {
        return this;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createObject() {
        try {
            Constructor<T> c = this.clz.getConstructor(new Class[0]);
            return c.newInstance(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createArray() {
        try {
            Constructor<T> c = this.clz.getConstructor(new Class[0]);
            return c.newInstance(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void setValue(Object current, String key, Object value) {
        ((Map) current).put(key, value);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void addValue(Object current, Object value) {
        ((List) current).add(value);
    }
}
