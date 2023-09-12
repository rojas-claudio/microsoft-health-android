package com.jayway.jsonpath.spi.json;

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPathException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import net.minidev.json.writer.JsonReaderI;
/* loaded from: classes.dex */
public class JsonSmartJsonProvider extends AbstractJsonProvider {
    private final JsonReaderI<?> mapper;
    private final int parseMode;

    public JsonSmartJsonProvider() {
        this(-1, JSONValue.defaultReader.DEFAULT_ORDERED);
    }

    public JsonSmartJsonProvider(int parseMode) {
        this(parseMode, JSONValue.defaultReader.DEFAULT_ORDERED);
    }

    public JsonSmartJsonProvider(int parseMode, JsonReaderI<?> mapper) {
        this.parseMode = parseMode;
        this.mapper = mapper;
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object createArray() {
        return this.mapper.createArray();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object createMap() {
        return this.mapper.createObject();
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object parse(String json) {
        try {
            return createParser().parse(json, this.mapper);
        } catch (ParseException e) {
            throw new InvalidJsonException(e);
        }
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public Object parse(InputStream jsonStream, String charset) throws InvalidJsonException {
        try {
            return createParser().parse(new InputStreamReader(jsonStream, charset), this.mapper);
        } catch (UnsupportedEncodingException e) {
            throw new JsonPathException(e);
        } catch (ParseException e2) {
            throw new InvalidJsonException(e2);
        }
    }

    @Override // com.jayway.jsonpath.spi.json.JsonProvider
    public String toJson(Object obj) {
        if (obj instanceof Map) {
            return JSONObject.toJSONString((Map) obj, JSONStyle.LT_COMPRESS);
        }
        if (obj instanceof List) {
            return JSONArray.toJSONString((List) obj, JSONStyle.LT_COMPRESS);
        }
        throw new UnsupportedOperationException(obj.getClass().getName() + " can not be converted to JSON");
    }

    private JSONParser createParser() {
        return new JSONParser(this.parseMode);
    }
}
