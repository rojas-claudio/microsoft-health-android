package com.jayway.jsonpath.spi.json;

import com.jayway.jsonpath.InvalidJsonException;
import java.io.InputStream;
import java.util.Collection;
/* loaded from: classes.dex */
public interface JsonProvider {
    public static final Object UNDEFINED = new Object();

    Object createArray();

    Object createMap();

    Object getArrayIndex(Object obj, int i);

    @Deprecated
    Object getArrayIndex(Object obj, int i, boolean z);

    Object getMapValue(Object obj, String str);

    Collection<String> getPropertyKeys(Object obj);

    boolean isArray(Object obj);

    boolean isMap(Object obj);

    int length(Object obj);

    Object parse(InputStream inputStream, String str) throws InvalidJsonException;

    Object parse(String str) throws InvalidJsonException;

    void removeProperty(Object obj, Object obj2);

    void setArrayIndex(Object obj, int i, Object obj2);

    void setProperty(Object obj, Object obj2, Object obj3);

    Iterable<?> toIterable(Object obj);

    String toJson(Object obj);

    Object unwrap(Object obj);
}
