package com.jayway.jsonpath.spi.mapper;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.TypeRef;
/* loaded from: classes.dex */
public interface MappingProvider {
    <T> T map(Object obj, TypeRef<T> typeRef, Configuration configuration);

    <T> T map(Object obj, Class<T> cls, Configuration configuration);
}
