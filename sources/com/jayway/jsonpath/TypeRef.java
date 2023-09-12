package com.jayway.jsonpath;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/* loaded from: classes.dex */
public abstract class TypeRef<T> implements Comparable<TypeRef<T>> {
    protected final Type type;

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return compareTo((TypeRef) ((TypeRef) obj));
    }

    protected TypeRef() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("No type info in TypeRef");
        }
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }

    public int compareTo(TypeRef<T> o) {
        return 0;
    }
}
