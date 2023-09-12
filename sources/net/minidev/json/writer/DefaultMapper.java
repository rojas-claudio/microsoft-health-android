package net.minidev.json.writer;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.JSONObject;
/* loaded from: classes.dex */
public class DefaultMapper<T> extends JsonReaderI<T> {
    /* JADX INFO: Access modifiers changed from: protected */
    public DefaultMapper(JsonReader base) {
        super(base);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<JSONAwareEx> startObject(String key) {
        return this.base.DEFAULT;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<JSONAwareEx> startArray(String key) {
        return this.base.DEFAULT;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createObject() {
        return new JSONObject();
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createArray() {
        return new JSONArray();
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void setValue(Object current, String key, Object value) {
        ((JSONObject) current).put(key, value);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void addValue(Object current, Object value) {
        ((JSONArray) current).add(value);
    }
}
