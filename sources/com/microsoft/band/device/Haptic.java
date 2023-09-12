package com.microsoft.band.device;

import com.microsoft.band.notifications.VibrationType;
/* loaded from: classes.dex */
public enum Haptic {
    SYSTEM_BATTERY_CHARGING(0),
    SYSTEM_BATTERY_FULL(1),
    SYSTEM_BATTERY_LOW(2),
    SYSTEM_BATTERY_CRITICAL(3),
    SYSTEM_SHUT_DOWN(4),
    SYSTEM_START_UP(5),
    SYSTEM_BUTTON_FEEDBACK(6),
    TOAST_TEXT_MESSAGE(7),
    TOAST_MISSED_CALL(8),
    TOAST_VOICE_MAIL(9),
    TOAST_FACEBOOK(10),
    TOAST_TWITTER(11),
    TOAST_ME_INSIGHTS(12),
    TOAST_WEATHER(13),
    TOAST_FINANCE(14),
    TOAST_SPORTS(15),
    ALERT_INCOMING_CALL(16),
    ALERT_ALARM(17),
    ALERT_TIMER(18),
    ALERT_CALENDAR(19),
    VOICE_LISTEN(20),
    VOICE_DONE(21),
    VOICE_ALERT(22),
    EXERCISE_RUN_LAP(23),
    EXERCISE_RUN_GPS_LOCK(24),
    EXERCISE_RUN_GPS_ERROR(25),
    EXERCISE_WORKOUT_TIMER(26),
    EXERCISE_GUIDED_WORKOUT_TIMER(27),
    EXERCISE_GUIDED_WORKOUT_COMPLETE(28),
    EXERCISE_GUIDED_WORKOUT_CIRCUIT_COMPLETE(29),
    INVALID_TONE(255);
    
    public final byte mValue;

    Haptic(int v) {
        this.mValue = (byte) (v & 255);
    }

    public static Haptic valueOf(byte i) {
        Haptic[] arr$ = values();
        for (Haptic haptic : arr$) {
            if (haptic.mValue == i) {
                return haptic;
            }
        }
        return INVALID_TONE;
    }

    public static Haptic vibrationToHaptic(VibrationType type) {
        switch (type) {
            case NOTIFICATION_ONE_TONE:
                return TOAST_TEXT_MESSAGE;
            case NOTIFICATION_TWO_TONE:
                return ALERT_INCOMING_CALL;
            case NOTIFICATION_ALARM:
                return ALERT_ALARM;
            case NOTIFICATION_TIMER:
                return ALERT_TIMER;
            case ONE_TONE_HIGH:
                return EXERCISE_GUIDED_WORKOUT_TIMER;
            case TWO_TONE_HIGH:
                return EXERCISE_GUIDED_WORKOUT_CIRCUIT_COMPLETE;
            case THREE_TONE_HIGH:
                return EXERCISE_GUIDED_WORKOUT_COMPLETE;
            case RAMP_UP:
                return SYSTEM_START_UP;
            case RAMP_DOWN:
                return SYSTEM_SHUT_DOWN;
            default:
                return INVALID_TONE;
        }
    }
}
