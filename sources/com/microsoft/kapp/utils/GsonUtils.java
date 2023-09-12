package com.microsoft.kapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.models.deserializers.EnumTypeAdapterFactory;
import com.microsoft.krestsdk.models.deserializers.IsoDateTimeDeserializer;
import com.microsoft.krestsdk.models.deserializers.UserEventDeserializer;
import com.microsoft.krestsdk.models.serializers.IsoDateTimeSerializer;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public final class GsonUtils {
    private static final Gson CUSTOM_GSON_SERIALIZER = new GsonBuilder().registerTypeAdapter(DateTime.class, new IsoDateTimeSerializer()).create();
    private static final Gson CUSTOM_GSON_SERIALIZER_PRETTY_PRINT = new GsonBuilder().registerTypeAdapter(DateTime.class, new IsoDateTimeSerializer()).setPrettyPrinting().create();
    private static final Gson CUSTOM_GSON_DESERIALIZER = new GsonBuilder().registerTypeAdapterFactory(new EnumTypeAdapterFactory()).registerTypeAdapter(DateTime.class, new IsoDateTimeDeserializer()).registerTypeAdapter(UserEvent.class, new UserEventDeserializer()).create();

    public static Gson getCustomDeserializer() {
        return CUSTOM_GSON_DESERIALIZER;
    }

    public static Gson getCustomSerializer() {
        return CUSTOM_GSON_SERIALIZER;
    }

    public static Gson getCustomSerializerWithPrettyPrinting() {
        return CUSTOM_GSON_SERIALIZER_PRETTY_PRINT;
    }
}
