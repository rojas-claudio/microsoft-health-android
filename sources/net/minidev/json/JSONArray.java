package net.minidev.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.reader.JsonWriter;
/* loaded from: classes.dex */
public class JSONArray extends ArrayList<Object> implements List<Object>, JSONAwareEx, JSONStreamAwareEx {
    private static final long serialVersionUID = 9106884089231309568L;

    public static String toJSONString(List<? extends Object> list) {
        return toJSONString(list, JSONValue.COMPRESSION);
    }

    public static String toJSONString(List<? extends Object> list, JSONStyle compression) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSONString(list, sb, compression);
        } catch (IOException e) {
        }
        return sb.toString();
    }

    public static void writeJSONString(Iterable<? extends Object> list, Appendable out, JSONStyle compression) throws IOException {
        if (list == null) {
            out.append("null");
        } else {
            JsonWriter.JSONIterableWriter.writeJSONString(list, out, compression);
        }
    }

    public static void writeJSONString(List<? extends Object> list, Appendable out) throws IOException {
        writeJSONString(list, out, JSONValue.COMPRESSION);
    }

    public void merge(Object o2) {
        JSONObject.merge(this, o2);
    }

    @Override // net.minidev.json.JSONAware
    public String toJSONString() {
        return toJSONString(this, JSONValue.COMPRESSION);
    }

    @Override // net.minidev.json.JSONAwareEx
    public String toJSONString(JSONStyle compression) {
        return toJSONString(this, compression);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return toJSONString();
    }

    public String toString(JSONStyle compression) {
        return toJSONString(compression);
    }

    @Override // net.minidev.json.JSONStreamAware
    public void writeJSONString(Appendable out) throws IOException {
        writeJSONString(this, out, JSONValue.COMPRESSION);
    }

    @Override // net.minidev.json.JSONStreamAwareEx
    public void writeJSONString(Appendable out, JSONStyle compression) throws IOException {
        writeJSONString(this, out, compression);
    }
}
