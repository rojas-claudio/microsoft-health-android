package com.jayway.jsonpath;
/* loaded from: classes.dex */
public interface WriteContext {
    DocumentContext add(JsonPath jsonPath, Object obj);

    DocumentContext add(String str, Object obj, Predicate... predicateArr);

    Configuration configuration();

    DocumentContext delete(JsonPath jsonPath);

    DocumentContext delete(String str, Predicate... predicateArr);

    <T> T json();

    String jsonString();

    DocumentContext put(JsonPath jsonPath, String str, Object obj);

    DocumentContext put(String str, String str2, Object obj, Predicate... predicateArr);

    DocumentContext set(JsonPath jsonPath, Object obj);

    DocumentContext set(String str, Object obj, Predicate... predicateArr);
}
