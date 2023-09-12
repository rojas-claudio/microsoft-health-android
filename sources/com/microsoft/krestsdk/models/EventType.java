package com.microsoft.krestsdk.models;
/* loaded from: classes.dex */
public enum EventType {
    Unknown,
    Driving,
    Running,
    Biking,
    Sleeping,
    Workout,
    Walking,
    Golf,
    GuidedWorkout;

    public static EventType getValueOf(String str) {
        EventType eventType = Unknown;
        try {
            EventType eventType2 = valueOf(str);
            return eventType2;
        } catch (IllegalArgumentException e) {
            return eventType;
        }
    }
}
