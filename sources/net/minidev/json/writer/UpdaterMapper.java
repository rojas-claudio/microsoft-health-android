package net.minidev.json.writer;

import java.io.IOException;
import java.lang.reflect.Type;
import net.minidev.json.parser.ParseException;
/* loaded from: classes.dex */
public class UpdaterMapper<T> extends JsonReaderI<T> {
    final JsonReaderI<?> mapper;
    final T obj;

    public UpdaterMapper(JsonReader base, T obj) {
        super(base);
        if (obj == null) {
            throw new NullPointerException("can not update null Object");
        }
        this.obj = obj;
        this.mapper = base.getMapper((Class) obj.getClass());
    }

    public UpdaterMapper(JsonReader base, T obj, Type type) {
        super(base);
        if (obj == null) {
            throw new NullPointerException("can not update null Object");
        }
        this.obj = obj;
        this.mapper = base.getMapper(type);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<?> startObject(String key) throws ParseException, IOException {
        Object bean = this.mapper.getValue(this.obj, key);
        return bean == null ? this.mapper.startObject(key) : new UpdaterMapper(this.base, bean, this.mapper.getType(key));
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<?> startArray(String key) throws ParseException, IOException {
        return this.mapper.startArray(key);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void setValue(Object current, String key, Object value) throws ParseException, IOException {
        this.mapper.setValue(current, key, value);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void addValue(Object current, Object value) throws ParseException, IOException {
        this.mapper.addValue(current, value);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createObject() {
        return this.obj != null ? this.obj : this.mapper.createObject();
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createArray() {
        return this.obj != null ? this.obj : this.mapper.createArray();
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public T convert(Object current) {
        return this.obj != null ? this.obj : (T) this.mapper.convert(current);
    }
}
