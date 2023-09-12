package com.microsoft.kapp.logging.http;
/* loaded from: classes.dex */
public class KHTTPHeader {
    public String mName;
    public String mValue;

    public KHTTPHeader() {
    }

    public KHTTPHeader(String name, String value) {
        this.mName = name;
        this.mValue = value;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getValue() {
        return this.mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }
}
