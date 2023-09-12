package com.microsoft.kapp;

import com.microsoft.krestsdk.models.EventType;
/* loaded from: classes.dex */
public interface OnEventModifiedListener {
    void onEventDeleted(EventType eventType, String str);

    void onEventRenamed(EventType eventType, String str, String str2);
}
