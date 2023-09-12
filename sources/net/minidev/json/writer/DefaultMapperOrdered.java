package net.minidev.json.writer;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONAwareEx;
/* loaded from: classes.dex */
public class DefaultMapperOrdered extends JsonReaderI<JSONAwareEx> {
    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultMapperOrdered(JsonReader base) {
        super(base);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<JSONAwareEx> startObject(String key) {
        return this.base.DEFAULT_ORDERED;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<JSONAwareEx> startArray(String key) {
        return this.base.DEFAULT_ORDERED;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void setValue(Object current, String key, Object value) {
        ((Map) current).put(key, value);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createObject() {
        return new LinkedHashMap();
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void addValue(Object current, Object value) {
        ((JSONArray) current).add(value);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createArray() {
        return new JSONArray();
    }
}
