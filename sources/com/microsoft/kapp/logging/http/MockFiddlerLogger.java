package com.microsoft.kapp.logging.http;
/* loaded from: classes.dex */
public class MockFiddlerLogger implements FiddlerLogger {
    @Override // com.microsoft.kapp.logging.http.FiddlerLogger
    public void logHttpCall(HttpTransaction transaction) {
    }

    @Override // com.microsoft.kapp.logging.http.FiddlerLogger
    public String createArchive(String baseFolderLocation) {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.FiddlerLogger
    public void cleanup() {
    }
}
