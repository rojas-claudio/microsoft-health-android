package net.minidev.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.minidev.json.reader.JsonWriter;
/* loaded from: classes.dex */
public class JSONObject extends HashMap<String, Object> implements JSONAware, JSONAwareEx, JSONStreamAwareEx {
    private static final long serialVersionUID = -503443796854799292L;

    public JSONObject() {
    }

    public static String escape(String s) {
        return JSONValue.escape(s);
    }

    public static String toJSONString(Map<String, ? extends Object> map) {
        return toJSONString(map, JSONValue.COMPRESSION);
    }

    public static String toJSONString(Map<String, ? extends Object> map, JSONStyle compression) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSON(map, sb, compression);
        } catch (IOException e) {
        }
        return sb.toString();
    }

    public static void writeJSONKV(String key, Object value, Appendable out, JSONStyle compression) throws IOException {
        if (key == null) {
            out.append("null");
        } else if (!compression.mustProtectKey(key)) {
            out.append(key);
        } else {
            out.append('\"');
            JSONValue.escape(key, out, compression);
            out.append('\"');
        }
        out.append(':');
        if (value instanceof String) {
            compression.writeString(out, (String) value);
        } else {
            JSONValue.writeJSONString(value, out, compression);
        }
    }

    public String getAsString(String key) {
        Object obj = get(key);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public Number getAsNumber(String key) {
        Object obj = get(key);
        if (obj == null) {
            return null;
        }
        return (Number) obj;
    }

    public JSONObject(Map<String, ?> map) {
        super(map);
    }

    public static void writeJSON(Map<String, ? extends Object> map, Appendable out) throws IOException {
        writeJSON(map, out, JSONValue.COMPRESSION);
    }

    public static void writeJSON(Map<String, ? extends Object> map, Appendable out, JSONStyle compression) throws IOException {
        if (map == null) {
            out.append("null");
        } else {
            JsonWriter.JSONMapWriter.writeJSONString(map, out, compression);
        }
    }

    @Override // net.minidev.json.JSONStreamAware
    public void writeJSONString(Appendable out) throws IOException {
        writeJSON(this, out, JSONValue.COMPRESSION);
    }

    @Override // net.minidev.json.JSONStreamAwareEx
    public void writeJSONString(Appendable out, JSONStyle compression) throws IOException {
        writeJSON(this, out, compression);
    }

    public void merge(Object o2) {
        merge(this, o2);
    }

    protected static JSONObject merge(JSONObject o1, Object o2) {
        if (o2 != null) {
            if (o2 instanceof JSONObject) {
                return merge(o1, (JSONObject) o2);
            }
            throw new RuntimeException("JSON megre can not merge JSONObject with " + o2.getClass());
        }
        return o1;
    }

    private static JSONObject merge(JSONObject o1, JSONObject o2) {
        if (o2 != null) {
            for (String key : o1.keySet()) {
                Object value1 = o1.get(key);
                Object value2 = o2.get(key);
                if (value2 != null) {
                    if (value1 instanceof JSONArray) {
                        o1.put(key, merge((JSONArray) value1, value2));
                    } else if (value1 instanceof JSONObject) {
                        o1.put(key, merge((JSONObject) value1, value2));
                    } else if (!value1.equals(value2)) {
                        if (value1.getClass().equals(value2.getClass())) {
                            throw new RuntimeException("JSON merge can not merge two " + value1.getClass().getName() + " Object together");
                        }
                        throw new RuntimeException("JSON merge can not merge " + value1.getClass().getName() + " with " + value2.getClass().getName());
                    }
                }
            }
            for (String key2 : o2.keySet()) {
                if (!o1.containsKey(key2)) {
                    o1.put(key2, o2.get(key2));
                }
            }
        }
        return o1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static JSONArray merge(JSONArray o1, Object o2) {
        if (o2 != null) {
            if (o1 instanceof JSONArray) {
                return merge(o1, (JSONArray) o2);
            }
            o1.add(o2);
            return o1;
        }
        return o1;
    }

    private static JSONArray merge(JSONArray o1, JSONArray o2) {
        o1.addAll(o2);
        return o1;
    }

    @Override // net.minidev.json.JSONAware
    public String toJSONString() {
        return toJSONString(this, JSONValue.COMPRESSION);
    }

    @Override // net.minidev.json.JSONAwareEx
    public String toJSONString(JSONStyle compression) {
        return toJSONString(this, compression);
    }

    public String toString(JSONStyle compression) {
        return toJSONString(this, compression);
    }

    @Override // java.util.AbstractMap
    public String toString() {
        return toJSONString(this, JSONValue.COMPRESSION);
    }
}
