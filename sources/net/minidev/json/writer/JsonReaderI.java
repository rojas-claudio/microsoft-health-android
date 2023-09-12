package net.minidev.json.writer;

import java.io.IOException;
import java.lang.reflect.Type;
import net.minidev.json.parser.ParseException;
/* loaded from: classes.dex */
public abstract class JsonReaderI<T> {
    private static String ERR_MSG = "Invalid or non Implemented status";
    public final JsonReader base;

    public JsonReaderI(JsonReader base) {
        this.base = base;
    }

    public JsonReaderI<?> startObject(String key) throws ParseException, IOException {
        throw new RuntimeException(ERR_MSG + " startObject(String key) in " + getClass() + " key=" + key);
    }

    public JsonReaderI<?> startArray(String key) throws ParseException, IOException {
        throw new RuntimeException(ERR_MSG + " startArray in " + getClass() + " key=" + key);
    }

    public void setValue(Object current, String key, Object value) throws ParseException, IOException {
        throw new RuntimeException(ERR_MSG + " setValue in " + getClass() + " key=" + key);
    }

    public Object getValue(Object current, String key) {
        throw new RuntimeException(ERR_MSG + " getValue(Object current, String key) in " + getClass() + " key=" + key);
    }

    public Type getType(String key) {
        throw new RuntimeException(ERR_MSG + " getType(String key) in " + getClass() + " key=" + key);
    }

    public void addValue(Object current, Object value) throws ParseException, IOException {
        throw new RuntimeException(ERR_MSG + " addValue(Object current, Object value) in " + getClass());
    }

    public Object createObject() {
        throw new RuntimeException(ERR_MSG + " createObject() in " + getClass());
    }

    public Object createArray() {
        throw new RuntimeException(ERR_MSG + " createArray() in " + getClass());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T convert(Object current) {
        return current;
    }
}
