package com.microsoft.krestsdk.models.deserializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserEvent;
import java.lang.reflect.Type;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class UserEventDeserializer implements JsonDeserializer<UserEvent> {
    private static final String TAG = UserEventDeserializer.class.getSimpleName();
    private static final Gson userEventGson = new GsonBuilder().registerTypeAdapter(DateTime.class, new IsoDateTimeDeserializer()).create();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public UserEvent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(UserEvent.EventTypeJSONMemberName);
        EventType eventType = EventType.getValueOf(prim.getAsString());
        switch (eventType) {
            case Running:
                return (UserEvent) context.deserialize(json, RunEvent.class);
            case Sleeping:
                return (UserEvent) context.deserialize(json, SleepEvent.class);
            case Workout:
                return (UserEvent) context.deserialize(json, ExerciseEvent.class);
            case GuidedWorkout:
                return (UserEvent) context.deserialize(json, GuidedWorkoutEvent.class);
            case Biking:
                return (UserEvent) context.deserialize(json, BikeEvent.class);
            case Golf:
                return (UserEvent) context.deserialize(json, GolfEvent.class);
            default:
                KLog.logPrivate(TAG, String.format("deserialize(): Unknown EventType [%s]", eventType.toString()));
                return (UserEvent) userEventGson.fromJson(json, (Class<Object>) UserEvent.class);
        }
    }
}
