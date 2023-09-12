package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.EvaluationListener;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.TypeRef;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class JsonReader implements ParseContext, DocumentContext {
    private static final Logger logger = LoggerFactory.getLogger(JsonReader.class);
    private final Configuration configuration;
    private Object json;
    private Object patch;

    public JsonReader() {
        this(Configuration.defaultConfiguration());
    }

    public JsonReader(Configuration configuration) {
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        this.configuration = configuration;
    }

    private JsonReader(Object json, Configuration configuration) {
        Utils.notNull(json, "json can not be null", new Object[0]);
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        this.configuration = configuration;
        this.json = json;
    }

    @Override // com.jayway.jsonpath.ParseContext
    public DocumentContext parse(Object json) {
        Utils.notNull(json, "json object can not be null", new Object[0]);
        this.json = json;
        return this;
    }

    @Override // com.jayway.jsonpath.ParseContext
    public DocumentContext parse(String json) {
        Utils.notEmpty(json, "json string can not be null or empty", new Object[0]);
        this.json = this.configuration.jsonProvider().parse(json);
        return this;
    }

    @Override // com.jayway.jsonpath.ParseContext
    public DocumentContext parse(InputStream json) {
        return parse(json, "UTF-8");
    }

    @Override // com.jayway.jsonpath.ParseContext
    public DocumentContext parse(InputStream json, String charset) {
        Utils.notNull(json, "json input stream can not be null", new Object[0]);
        Utils.notNull(json, "charset can not be null", new Object[0]);
        try {
            this.json = this.configuration.jsonProvider().parse(json, charset);
            return this;
        } finally {
            Utils.closeQuietly(json);
        }
    }

    @Override // com.jayway.jsonpath.ParseContext
    public DocumentContext parse(File json) throws IOException {
        FileInputStream fis;
        Utils.notNull(json, "json file can not be null", new Object[0]);
        FileInputStream fis2 = null;
        try {
            fis = new FileInputStream(json);
        } catch (Throwable th) {
            th = th;
        }
        try {
            parse((InputStream) fis);
            Utils.closeQuietly(fis);
            return this;
        } catch (Throwable th2) {
            th = th2;
            fis2 = fis;
            Utils.closeQuietly(fis2);
            throw th;
        }
    }

    @Override // com.jayway.jsonpath.ReadContext, com.jayway.jsonpath.WriteContext
    public Configuration configuration() {
        return this.configuration;
    }

    @Override // com.jayway.jsonpath.ReadContext, com.jayway.jsonpath.WriteContext
    public Object json() {
        return this.json;
    }

    @Override // com.jayway.jsonpath.ReadContext, com.jayway.jsonpath.WriteContext
    public String jsonString() {
        return this.configuration.jsonProvider().toJson(this.json);
    }

    @Override // com.jayway.jsonpath.ReadContext
    public <T> T read(String path, Predicate... filters) {
        Utils.notEmpty(path, "path can not be null or empty", new Object[0]);
        return (T) read(JsonPath.compile(path, filters));
    }

    @Override // com.jayway.jsonpath.ReadContext
    public <T> T read(String path, Class<T> type, Predicate... filters) {
        return (T) convert(read(path, filters), type, this.configuration);
    }

    @Override // com.jayway.jsonpath.ReadContext
    public <T> T read(JsonPath path) {
        Utils.notNull(path, "path can not be null", new Object[0]);
        return (T) path.read(this.json, this.configuration);
    }

    @Override // com.jayway.jsonpath.ReadContext
    public <T> T read(JsonPath path, Class<T> type) {
        return (T) convert(read(path), type, this.configuration);
    }

    @Override // com.jayway.jsonpath.ReadContext
    public <T> T read(JsonPath path, TypeRef<T> type) {
        return (T) convert(read(path), type, this.configuration);
    }

    @Override // com.jayway.jsonpath.ReadContext
    public <T> T read(String path, TypeRef<T> type) {
        return (T) convert(read(path, new Predicate[0]), type, this.configuration);
    }

    @Override // com.jayway.jsonpath.ReadContext
    public ReadContext limit(int maxResults) {
        return withListeners(new LimitingEvaluationListener(maxResults));
    }

    @Override // com.jayway.jsonpath.ReadContext
    public ReadContext withListeners(EvaluationListener... listener) {
        return new JsonReader(this.json, this.configuration.setEvaluationListeners(listener));
    }

    private <T> T convert(Object obj, Class<T> targetType, Configuration configuration) {
        return (T) configuration.mappingProvider().map(obj, targetType, configuration);
    }

    private <T> T convert(Object obj, TypeRef<T> targetType, Configuration configuration) {
        return (T) configuration.mappingProvider().map(obj, targetType, configuration);
    }

    @Override // com.jayway.jsonpath.WriteContext
    public DocumentContext set(String path, Object newValue, Predicate... filters) {
        return set(JsonPath.compile(path, filters), newValue);
    }

    @Override // com.jayway.jsonpath.WriteContext
    public DocumentContext set(JsonPath path, Object newValue) {
        List<String> modified = (List) path.set(this.json, newValue, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String p : modified) {
                logger.debug("Set path {} new value {}", p, newValue);
            }
        }
        return this;
    }

    @Override // com.jayway.jsonpath.WriteContext
    public DocumentContext delete(String path, Predicate... filters) {
        return delete(JsonPath.compile(path, filters));
    }

    @Override // com.jayway.jsonpath.WriteContext
    public DocumentContext delete(JsonPath path) {
        List<String> modified = (List) path.delete(this.json, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String str : modified) {
                logger.debug("Delete path {}");
            }
        }
        return this;
    }

    @Override // com.jayway.jsonpath.WriteContext
    public DocumentContext add(String path, Object value, Predicate... filters) {
        return add(JsonPath.compile(path, filters), value);
    }

    @Override // com.jayway.jsonpath.WriteContext
    public DocumentContext add(JsonPath path, Object value) {
        List<String> modified = (List) path.add(this.json, value, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String p : modified) {
                logger.debug("Add path {} new value {}", p, value);
            }
        }
        return this;
    }

    @Override // com.jayway.jsonpath.WriteContext
    public DocumentContext put(String path, String key, Object value, Predicate... filters) {
        return put(JsonPath.compile(path, filters), key, value);
    }

    @Override // com.jayway.jsonpath.WriteContext
    public DocumentContext put(JsonPath path, String key, Object value) {
        List<String> modified = (List) path.put(this.json, key, value, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String p : modified) {
                logger.debug("Put path {} key {} value {}", p, key, value);
            }
        }
        return this;
    }

    /* loaded from: classes.dex */
    private final class LimitingEvaluationListener implements EvaluationListener {
        final int limit;

        private LimitingEvaluationListener(int limit) {
            this.limit = limit;
        }

        @Override // com.jayway.jsonpath.EvaluationListener
        public EvaluationListener.EvaluationContinuation resultFound(EvaluationListener.FoundResult found) {
            return found.index() == this.limit + (-1) ? EvaluationListener.EvaluationContinuation.ABORT : EvaluationListener.EvaluationContinuation.CONTINUE;
        }
    }
}
