package com.jayway.jsonpath.spi.mapper;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.TypeRef;
/* loaded from: classes.dex */
public class JacksonMappingProvider implements MappingProvider {
    private final ObjectMapper objectMapper;

    public JacksonMappingProvider() {
        this(new ObjectMapper());
    }

    public JacksonMappingProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override // com.jayway.jsonpath.spi.mapper.MappingProvider
    public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
        if (source == null) {
            return null;
        }
        try {
            return (T) this.objectMapper.convertValue(source, targetType);
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }

    @Override // com.jayway.jsonpath.spi.mapper.MappingProvider
    public <T> T map(Object source, TypeRef<T> targetType, Configuration configuration) {
        if (source == null) {
            return null;
        }
        JavaType type = this.objectMapper.getTypeFactory().constructType(targetType.getType());
        try {
            return (T) this.objectMapper.convertValue(source, type);
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }
}
