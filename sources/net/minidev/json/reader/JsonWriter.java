package net.minidev.json.reader;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minidev.json.JSONAware;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.JSONStreamAware;
import net.minidev.json.JSONStreamAwareEx;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
/* loaded from: classes.dex */
public class JsonWriter {
    private ConcurrentHashMap<Class<?>, JsonWriterI<?>> data = new ConcurrentHashMap<>();
    private LinkedList<WriterByInterface> writerInterfaces = new LinkedList<>();
    public static final JsonWriterI<JSONStreamAwareEx> JSONStreamAwareWriter = new JsonWriterI<JSONStreamAwareEx>() { // from class: net.minidev.json.reader.JsonWriter.1
        @Override // net.minidev.json.reader.JsonWriterI
        public /* bridge */ /* synthetic */ void writeJSONString(Object x0, Appendable x1, JSONStyle x2) throws IOException {
            writeJSONString((AnonymousClass1) ((JSONStreamAwareEx) x0), x1, x2);
        }

        public <E extends JSONStreamAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            value.writeJSONString(out);
        }
    };
    public static final JsonWriterI<JSONStreamAwareEx> JSONStreamAwareExWriter = new JsonWriterI<JSONStreamAwareEx>() { // from class: net.minidev.json.reader.JsonWriter.2
        @Override // net.minidev.json.reader.JsonWriterI
        public /* bridge */ /* synthetic */ void writeJSONString(Object x0, Appendable x1, JSONStyle x2) throws IOException {
            writeJSONString((AnonymousClass2) ((JSONStreamAwareEx) x0), x1, x2);
        }

        public <E extends JSONStreamAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            value.writeJSONString(out, compression);
        }
    };
    public static final JsonWriterI<JSONAwareEx> JSONJSONAwareExWriter = new JsonWriterI<JSONAwareEx>() { // from class: net.minidev.json.reader.JsonWriter.3
        @Override // net.minidev.json.reader.JsonWriterI
        public /* bridge */ /* synthetic */ void writeJSONString(Object x0, Appendable x1, JSONStyle x2) throws IOException {
            writeJSONString((AnonymousClass3) ((JSONAwareEx) x0), x1, x2);
        }

        public <E extends JSONAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            out.append(value.toJSONString(compression));
        }
    };
    public static final JsonWriterI<JSONAware> JSONJSONAwareWriter = new JsonWriterI<JSONAware>() { // from class: net.minidev.json.reader.JsonWriter.4
        @Override // net.minidev.json.reader.JsonWriterI
        public /* bridge */ /* synthetic */ void writeJSONString(Object x0, Appendable x1, JSONStyle x2) throws IOException {
            writeJSONString((AnonymousClass4) ((JSONAware) x0), x1, x2);
        }

        public <E extends JSONAware> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            out.append(value.toJSONString());
        }
    };
    public static final JsonWriterI<Iterable<? extends Object>> JSONIterableWriter = new JsonWriterI<Iterable<? extends Object>>() { // from class: net.minidev.json.reader.JsonWriter.5
        @Override // net.minidev.json.reader.JsonWriterI
        public /* bridge */ /* synthetic */ void writeJSONString(Object x0, Appendable x1, JSONStyle x2) throws IOException {
            writeJSONString((AnonymousClass5) ((Iterable) x0), x1, x2);
        }

        public <E extends Iterable<? extends Object>> void writeJSONString(E list, Appendable out, JSONStyle compression) throws IOException {
            boolean first = true;
            compression.arrayStart(out);
            for (Object value : list) {
                if (first) {
                    first = false;
                    compression.arrayfirstObject(out);
                } else {
                    compression.arrayNextElm(out);
                }
                if (value == null) {
                    out.append("null");
                } else {
                    JSONValue.writeJSONString(value, out, compression);
                }
                compression.arrayObjectEnd(out);
            }
            compression.arrayStop(out);
        }
    };
    public static final JsonWriterI<Enum<?>> EnumWriter = new JsonWriterI<Enum<?>>() { // from class: net.minidev.json.reader.JsonWriter.6
        @Override // net.minidev.json.reader.JsonWriterI
        public /* bridge */ /* synthetic */ void writeJSONString(Object x0, Appendable x1, JSONStyle x2) throws IOException {
            writeJSONString((AnonymousClass6) ((Enum) x0), x1, x2);
        }

        public <E extends Enum<?>> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            String s = value.name();
            compression.writeString(out, s);
        }
    };
    public static final JsonWriterI<Map<String, ? extends Object>> JSONMapWriter = new JsonWriterI<Map<String, ? extends Object>>() { // from class: net.minidev.json.reader.JsonWriter.7
        @Override // net.minidev.json.reader.JsonWriterI
        public /* bridge */ /* synthetic */ void writeJSONString(Object x0, Appendable x1, JSONStyle x2) throws IOException {
            writeJSONString((AnonymousClass7) ((Map) x0), x1, x2);
        }

        public <E extends Map<String, ? extends Object>> void writeJSONString(E map, Appendable out, JSONStyle compression) throws IOException {
            boolean first = true;
            compression.objectStart(out);
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object v = entry.getValue();
                if (v != null || !compression.ignoreNull()) {
                    if (first) {
                        compression.objectFirstStart(out);
                        first = false;
                    } else {
                        compression.objectNext(out);
                    }
                    String key = entry.getKey().toString();
                    JsonWriter.writeJSONKV(key, v, out, compression);
                }
            }
            compression.objectStop(out);
        }
    };
    public static final JsonWriterI<Object> beansWriterASM = new BeansWriterASM();
    public static final JsonWriterI<Object> beansWriter = new BeansWriter();
    public static final JsonWriterI<Object> arrayWriter = new ArrayWriter();

    public JsonWriter() {
        init();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void remapField(Class<T> type, String fromJava, String toJson) {
        JsonWriterI map = getWrite(type);
        if (!(map instanceof BeansWriterASMRemap)) {
            map = new BeansWriterASMRemap();
            registerWriter(map, type);
        }
        ((BeansWriterASMRemap) map).renameField(fromJava, toJson);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class WriterByInterface {
        public Class<?> _interface;
        public JsonWriterI<?> _writer;

        public WriterByInterface(Class<?> _interface, JsonWriterI<?> _writer) {
            this._interface = _interface;
            this._writer = _writer;
        }
    }

    public JsonWriterI getWriterByInterface(Class<?> clazz) {
        Iterator i$ = this.writerInterfaces.iterator();
        while (i$.hasNext()) {
            WriterByInterface w = i$.next();
            if (w._interface.isAssignableFrom(clazz)) {
                return w._writer;
            }
        }
        return null;
    }

    public JsonWriterI getWrite(Class cls) {
        return this.data.get(cls);
    }

    public void init() {
        registerWriter(new JsonWriterI<String>() { // from class: net.minidev.json.reader.JsonWriter.8
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(String value, Appendable out, JSONStyle compression) throws IOException {
                compression.writeString(out, value);
            }
        }, String.class);
        registerWriter(new JsonWriterI<Boolean>() { // from class: net.minidev.json.reader.JsonWriter.9
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(Boolean value, Appendable out, JSONStyle compression) throws IOException {
                out.append(value.toString());
            }
        }, Boolean.class);
        registerWriter(new JsonWriterI<Double>() { // from class: net.minidev.json.reader.JsonWriter.10
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(Double value, Appendable out, JSONStyle compression) throws IOException {
                if (value.isInfinite()) {
                    out.append("null");
                } else {
                    out.append(value.toString());
                }
            }
        }, Double.class);
        registerWriter(new JsonWriterI<Date>() { // from class: net.minidev.json.reader.JsonWriter.11
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(Date value, Appendable out, JSONStyle compression) throws IOException {
                out.append('\"');
                JSONValue.escape(value.toString(), out, compression);
                out.append('\"');
            }
        }, Date.class);
        registerWriter(new JsonWriterI<Float>() { // from class: net.minidev.json.reader.JsonWriter.12
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(Float value, Appendable out, JSONStyle compression) throws IOException {
                if (value.isInfinite()) {
                    out.append("null");
                } else {
                    out.append(value.toString());
                }
            }
        }, Float.class);
        registerWriter(new JsonWriterI<Number>() { // from class: net.minidev.json.reader.JsonWriter.13
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(Number value, Appendable out, JSONStyle compression) throws IOException {
                out.append(value.toString());
            }
        }, Integer.class, Long.class, Byte.class, Short.class, BigInteger.class);
        registerWriter(new JsonWriterI<Boolean>() { // from class: net.minidev.json.reader.JsonWriter.14
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(Boolean value, Appendable out, JSONStyle compression) throws IOException {
                out.append(value.toString());
            }
        }, Boolean.class);
        registerWriter(new JsonWriterI<Boolean>() { // from class: net.minidev.json.reader.JsonWriter.15
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(Boolean value, Appendable out, JSONStyle compression) throws IOException {
                out.append(value.toString());
            }
        }, Boolean.class);
        registerWriter(new JsonWriterI<int[]>() { // from class: net.minidev.json.reader.JsonWriter.16
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(int[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (int b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Integer.toString(b));
                }
                compression.arrayStop(out);
            }
        }, int[].class);
        registerWriter(new JsonWriterI<short[]>() { // from class: net.minidev.json.reader.JsonWriter.17
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(short[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (short b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Short.toString(b));
                }
                compression.arrayStop(out);
            }
        }, short[].class);
        registerWriter(new JsonWriterI<long[]>() { // from class: net.minidev.json.reader.JsonWriter.18
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(long[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (long b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Long.toString(b));
                }
                compression.arrayStop(out);
            }
        }, long[].class);
        registerWriter(new JsonWriterI<float[]>() { // from class: net.minidev.json.reader.JsonWriter.19
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(float[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (float b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Float.toString(b));
                }
                compression.arrayStop(out);
            }
        }, float[].class);
        registerWriter(new JsonWriterI<double[]>() { // from class: net.minidev.json.reader.JsonWriter.20
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(double[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (double b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Double.toString(b));
                }
                compression.arrayStop(out);
            }
        }, double[].class);
        registerWriter(new JsonWriterI<boolean[]>() { // from class: net.minidev.json.reader.JsonWriter.21
            @Override // net.minidev.json.reader.JsonWriterI
            public void writeJSONString(boolean[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (boolean b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Boolean.toString(b));
                }
                compression.arrayStop(out);
            }
        }, boolean[].class);
        addInterfaceWriterLast(JSONStreamAwareEx.class, JSONStreamAwareExWriter);
        addInterfaceWriterLast(JSONStreamAware.class, JSONStreamAwareWriter);
        addInterfaceWriterLast(JSONAwareEx.class, JSONJSONAwareExWriter);
        addInterfaceWriterLast(JSONAware.class, JSONJSONAwareWriter);
        addInterfaceWriterLast(Map.class, JSONMapWriter);
        addInterfaceWriterLast(Iterable.class, JSONIterableWriter);
        addInterfaceWriterLast(Enum.class, EnumWriter);
    }

    /* renamed from: net.minidev.json.reader.JsonWriter$22  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass22 implements JsonWriterI<float[]> {
        AnonymousClass22() {
        }

        @Override // net.minidev.json.reader.JsonWriterI
        public void writeJSONString(float[] value, Appendable out, JSONStyle compression) throws IOException {
            boolean needSep = false;
            compression.arrayStart(out);
            for (float b : value) {
                if (needSep) {
                    compression.objectNext(out);
                } else {
                    needSep = true;
                }
                out.append(Float.toString(b));
            }
            compression.arrayStop(out);
        }
    }

    public void addInterfaceWriterFirst(Class<?> cls, JsonWriterI<?> writer) {
        this.writerInterfaces.addFirst(new WriterByInterface(cls, writer));
    }

    public void addInterfaceWriterLast(Class<?> cls, JsonWriterI<?> writer) {
        this.writerInterfaces.addLast(new WriterByInterface(cls, writer));
    }

    public <T> void registerWriter(JsonWriterI<T> writer, Class<?>... cls) {
        for (Class<?> c : cls) {
            this.data.put(c, writer);
        }
    }

    /* renamed from: net.minidev.json.reader.JsonWriter$23  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass23 implements JsonWriterI<double[]> {
        AnonymousClass23() {
        }

        @Override // net.minidev.json.reader.JsonWriterI
        public void writeJSONString(double[] value, Appendable out, JSONStyle compression) throws IOException {
            boolean needSep = false;
            compression.arrayStart(out);
            for (double b : value) {
                if (needSep) {
                    compression.objectNext(out);
                } else {
                    needSep = true;
                }
                out.append(Double.toString(b));
            }
            compression.arrayStop(out);
        }
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
        compression.objectEndOfKey(out);
        if (value instanceof String) {
            compression.writeString(out, (String) value);
        } else {
            JSONValue.writeJSONString(value, out, compression);
        }
        compression.objectElmStop(out);
    }

    /* renamed from: net.minidev.json.reader.JsonWriter$24  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass24 implements JsonWriterI<boolean[]> {
        AnonymousClass24() {
        }

        @Override // net.minidev.json.reader.JsonWriterI
        public void writeJSONString(boolean[] value, Appendable out, JSONStyle compression) throws IOException {
            boolean needSep = false;
            compression.arrayStart(out);
            for (boolean b : value) {
                if (needSep) {
                    compression.objectNext(out);
                } else {
                    needSep = true;
                }
                out.append(Boolean.toString(b));
            }
            compression.arrayStop(out);
        }
    }
}
