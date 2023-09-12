package com.jayway.jsonpath.spi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.jayway.jsonpath.InvalidJsonException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class JacksonJsonProvider extends AbstractJsonProvider {
    protected ObjectMapper objectMapper;
    protected ObjectReader objectReader;
    private static final Logger logger = LoggerFactory.getLogger(JacksonJsonProvider.class);
    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();
    private static final ObjectReader defaultObjectReader = defaultObjectMapper.reader().withType(Object.class);

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    public JacksonJsonProvider() {
        this(defaultObjectMapper, defaultObjectReader);
    }

    public JacksonJsonProvider(ObjectMapper objectMapper) {
        this(objectMapper, objectMapper.reader().withType(Object.class));
    }

    public JacksonJsonProvider(ObjectMapper objectMapper, ObjectReader objectReader) {
        this.objectMapper = objectMapper;
        this.objectReader = objectReader;
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object parse(String json) throws InvalidJsonException {
        try {
            return this.objectReader.readValue(json);
        } catch (IOException e) {
            logger.debug("Invalid JSON: \n" + json);
            throw new InvalidJsonException(e);
        }
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object parse(InputStream jsonStream, String charset) throws InvalidJsonException {
        try {
            return this.objectReader.readValue(new InputStreamReader(jsonStream, charset));
        } catch (IOException e) {
            throw new InvalidJsonException(e);
        }
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public String toJson(Object obj) {
        StringWriter writer = new StringWriter();
        try {
            JsonGenerator generator = this.objectMapper.getFactory().createGenerator(writer);
            this.objectMapper.writeValue(generator, obj);
            writer.flush();
            writer.close();
            generator.close();
            return writer.getBuffer().toString();
        } catch (IOException e) {
            throw new InvalidJsonException();
        }
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public List<Object> createArray() {
        return new LinkedList();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object createMap() {
        return new LinkedHashMap();
    }
}
