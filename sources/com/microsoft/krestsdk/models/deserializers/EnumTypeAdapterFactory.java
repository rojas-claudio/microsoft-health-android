package com.microsoft.krestsdk.models.deserializers;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
/* loaded from: classes.dex */
public class EnumTypeAdapterFactory implements TypeAdapterFactory {
    @Override // com.google.gson.TypeAdapterFactory
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class rawType = typeToken.getRawType();
        if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
            return null;
        }
        if (!rawType.isEnum()) {
            Class<? super T> rawType2 = rawType.getSuperclass();
            rawType = (Class<? super Object>) rawType2;
        }
        return new EnumTypeAdapter(rawType);
    }
}
