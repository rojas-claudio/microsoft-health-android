package com.microsoft.kapp.logging.http;
/* loaded from: classes.dex */
public interface FiddlerLogger {
    void cleanup();

    String createArchive(String str);

    void logHttpCall(HttpTransaction httpTransaction);
}
