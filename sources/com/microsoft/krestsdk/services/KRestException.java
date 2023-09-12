package com.microsoft.krestsdk.services;
/* loaded from: classes.dex */
public class KRestException extends Exception {
    private static final long serialVersionUID = -4928351050073646657L;

    public KRestException(String message) {
        super(message);
    }

    public KRestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
