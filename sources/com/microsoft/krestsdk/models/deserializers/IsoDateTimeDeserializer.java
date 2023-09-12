package com.microsoft.krestsdk.models.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Date;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class IsoDateTimeDeserializer implements JsonDeserializer<DateTime> {
    @Override // com.google.gson.JsonDeserializer
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateTimeText = null;
        try {
            dateTimeText = json.getAsJsonPrimitive().getAsString();
            if (dateTimeText != null) {
                return new DateTime(dateTimeText);
            }
            return null;
        } catch (Exception e) {
            try {
                Date date = (Date) context.deserialize(json, Date.class);
                return new DateTime(date);
            } catch (Exception ex) {
                throw new JsonParseException("Expected date in ISO-8601 date time format: '" + dateTimeText + "'", ex);
            }
        }
    }
}
