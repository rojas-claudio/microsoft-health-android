package com.microsoft.kapp.services;
/* loaded from: classes.dex */
public class ServiceException extends Exception {
    private static final long serialVersionUID = 8817136086106403494L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
