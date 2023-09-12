package com.microsoft.krestsdk.models.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
/* loaded from: classes.dex */
public class IsoDateTimeSerializer implements JsonSerializer<DateTime> {
    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(DateTime dateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        String iso8601Date = dateTime != null ? fmt.print(dateTime) : "";
        return new JsonPrimitive(iso8601Date);
    }
}
