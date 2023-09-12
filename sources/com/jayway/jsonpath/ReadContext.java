package com.jayway.jsonpath;
/* loaded from: classes.dex */
public interface ReadContext {
    Configuration configuration();

    <T> T json();

    String jsonString();

    ReadContext limit(int i);

    <T> T read(JsonPath jsonPath);

    <T> T read(JsonPath jsonPath, TypeRef<T> typeRef);

    <T> T read(JsonPath jsonPath, Class<T> cls);

    <T> T read(String str, TypeRef<T> typeRef);

    <T> T read(String str, Class<T> cls, Predicate... predicateArr);

    <T> T read(String str, Predicate... predicateArr);

    ReadContext withListeners(EvaluationListener... evaluationListenerArr);
}
