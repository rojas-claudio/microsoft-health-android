package com.microsoft.krestsdk.models.deserializers;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Enum;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    public static final String UNKOWN_ENUM = "Unknown";
    private final Map<String, T> nameToConstant = new HashMap();
    private final Map<T, String> constantToName = new HashMap();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.gson.TypeAdapter
    public /* bridge */ /* synthetic */ void write(JsonWriter x0, Object x1) throws IOException {
        write(x0, (JsonWriter) ((Enum) x1));
    }

    public EnumTypeAdapter(Class<T> classOfT) {
        T[] enumConstants;
        try {
            for (T constant : classOfT.getEnumConstants()) {
                String name = constant.name();
                SerializedName annotation = (SerializedName) classOfT.getField(name).getAnnotation(SerializedName.class);
                name = annotation != null ? annotation.value() : name;
                this.nameToConstant.put(name, constant);
                this.constantToName.put(constant, name);
            }
        } catch (NoSuchFieldException e) {
            throw new AssertionError();
        }
    }

    @Override // com.google.gson.TypeAdapter
    public T read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        T e = this.nameToConstant.get(in.nextString());
        if (e == null) {
            return this.nameToConstant.get("Unknown");
        }
        return e;
    }

    public void write(JsonWriter out, T value) throws IOException {
        out.value(value == null ? null : this.constantToName.get(value));
    }
}
