package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.json.JsonSmartJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonSmartMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import java.util.EnumSet;
import java.util.Set;
/* loaded from: classes.dex */
public final class DefaultsImpl implements Configuration.Defaults {
    public static final DefaultsImpl INSTANCE = new DefaultsImpl();
    private final MappingProvider mappingProvider = new JsonSmartMappingProvider();

    @Override // com.jayway.jsonpath.Configuration.Defaults
    public JsonProvider jsonProvider() {
        return new JsonSmartJsonProvider();
    }

    @Override // com.jayway.jsonpath.Configuration.Defaults
    public Set<Option> options() {
        return EnumSet.noneOf(Option.class);
    }

    @Override // com.jayway.jsonpath.Configuration.Defaults
    public MappingProvider mappingProvider() {
        return this.mappingProvider;
    }

    private DefaultsImpl() {
    }
}
