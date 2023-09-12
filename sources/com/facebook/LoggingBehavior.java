package com.facebook;
/* loaded from: classes.dex */
public enum LoggingBehavior {
    REQUESTS,
    INCLUDE_ACCESS_TOKENS,
    INCLUDE_RAW_RESPONSES,
    CACHE,
    APP_EVENTS,
    DEVELOPER_ERRORS;
    
    @Deprecated
    public static final LoggingBehavior INSIGHTS = APP_EVENTS;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static LoggingBehavior[] valuesCustom() {
        LoggingBehavior[] valuesCustom = values();
        int length = valuesCustom.length;
        LoggingBehavior[] loggingBehaviorArr = new LoggingBehavior[length];
        System.arraycopy(valuesCustom, 0, loggingBehaviorArr, 0, length);
        return loggingBehaviorArr;
    }
}
