package com.jayway.jsonpath;

import com.jayway.jsonpath.spi.mapper.MappingException;
/* loaded from: classes.dex */
public interface Predicate {

    /* loaded from: classes.dex */
    public interface PredicateContext {
        Configuration configuration();

        Object item();

        <T> T item(Class<T> cls) throws MappingException;

        Object root();
    }

    boolean apply(PredicateContext predicateContext);
}
