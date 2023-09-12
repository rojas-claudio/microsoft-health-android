package net.minidev.json.writer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.minidev.json.parser.ParseException;
/* loaded from: classes.dex */
public class MapperRemapped<T> extends JsonReaderI<T> {
    private JsonReaderI<T> parent;
    private Map<String, String> rename;

    public MapperRemapped(JsonReaderI<T> parent) {
        super(parent.base);
        this.parent = parent;
        this.rename = new HashMap();
    }

    public void renameField(String source, String dest) {
        this.rename.put(source, dest);
    }

    private String rename(String key) {
        String k2 = this.rename.get(key);
        return k2 != null ? k2 : key;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void setValue(Object current, String key, Object value) throws ParseException, IOException {
        this.parent.setValue(current, rename(key), value);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object getValue(Object current, String key) {
        return this.parent.getValue(current, rename(key));
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Type getType(String key) {
        return this.parent.getType(rename(key));
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<?> startArray(String key) throws ParseException, IOException {
        return this.parent.startArray(rename(key));
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<?> startObject(String key) throws ParseException, IOException {
        return this.parent.startObject(rename(key));
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createObject() {
        return this.parent.createObject();
    }
}
