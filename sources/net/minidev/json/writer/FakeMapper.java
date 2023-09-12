package net.minidev.json.writer;
/* loaded from: classes.dex */
public class FakeMapper extends JsonReaderI<Object> {
    public static JsonReaderI<Object> DEFAULT = new FakeMapper();

    private FakeMapper() {
        super(null);
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<?> startObject(String key) {
        return this;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public JsonReaderI<?> startArray(String key) {
        return this;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void setValue(Object current, String key, Object value) {
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public void addValue(Object current, Object value) {
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createObject() {
        return null;
    }

    @Override // net.minidev.json.writer.JsonReaderI
    public Object createArray() {
        return null;
    }
}
