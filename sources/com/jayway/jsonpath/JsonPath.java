package com.jayway.jsonpath;

import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.JsonReader;
import com.jayway.jsonpath.internal.Path;
import com.jayway.jsonpath.internal.PathCompiler;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.spi.json.JsonProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
/* loaded from: classes.dex */
public class JsonPath {
    private final Path path;

    private JsonPath(String jsonPath, Predicate[] filters) {
        Utils.notNull(jsonPath, "path can not be null", new Object[0]);
        this.path = PathCompiler.compile(jsonPath, filters);
    }

    public String getPath() {
        return this.path.toString();
    }

    public static boolean isPathDefinite(String path) {
        return compile(path, new Predicate[0]).isDefinite();
    }

    public boolean isDefinite() {
        return this.path.isDefinite();
    }

    public <T> T read(Object jsonObject) {
        return (T) read(jsonObject, Configuration.defaultConfiguration());
    }

    public <T> T read(Object jsonObject, Configuration configuration) {
        Object array;
        boolean optAsPathList = configuration.containsOption(Option.AS_PATH_LIST);
        boolean optAlwaysReturnList = configuration.containsOption(Option.ALWAYS_RETURN_LIST);
        boolean optSuppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
        try {
            if (optAsPathList) {
                array = (T) this.path.evaluate(jsonObject, jsonObject, configuration).getPath();
            } else {
                Object res = this.path.evaluate(jsonObject, jsonObject, configuration).getValue(false);
                if (optAlwaysReturnList && this.path.isDefinite()) {
                    array = (T) configuration.jsonProvider().createArray();
                    configuration.jsonProvider().setProperty(array, 0, res);
                } else {
                    array = (T) res;
                }
            }
            return (T) array;
        } catch (RuntimeException e) {
            if (!optSuppressExceptions) {
                throw e;
            }
            if (optAsPathList) {
                return (T) configuration.jsonProvider().createArray();
            }
            if (optAlwaysReturnList) {
                return (T) configuration.jsonProvider().createArray();
            }
            return (T) (this.path.isDefinite() ? null : configuration.jsonProvider().createArray());
        }
    }

    public <T> T set(Object jsonObject, Object newVal, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null", new Object[0]);
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        for (PathRef updateOperation : evaluationContext.updateOperations()) {
            updateOperation.set(newVal, configuration);
        }
        return (T) resultByConfiguration(jsonObject, configuration, evaluationContext);
    }

    public <T> T delete(Object jsonObject, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null", new Object[0]);
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        for (PathRef updateOperation : evaluationContext.updateOperations()) {
            updateOperation.delete(configuration);
        }
        return (T) resultByConfiguration(jsonObject, configuration, evaluationContext);
    }

    public <T> T add(Object jsonObject, Object value, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null", new Object[0]);
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        for (PathRef updateOperation : evaluationContext.updateOperations()) {
            updateOperation.add(value, configuration);
        }
        return (T) resultByConfiguration(jsonObject, configuration, evaluationContext);
    }

    public <T> T put(Object jsonObject, String key, Object value, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null", new Object[0]);
        Utils.notEmpty(key, "key can not be null or empty", new Object[0]);
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        for (PathRef updateOperation : evaluationContext.updateOperations()) {
            updateOperation.put(key, value, configuration);
        }
        return (T) resultByConfiguration(jsonObject, configuration, evaluationContext);
    }

    public <T> T read(String json) {
        return (T) read(json, Configuration.defaultConfiguration());
    }

    public <T> T read(String json, Configuration configuration) {
        Utils.notEmpty(json, "json can not be null or empty", new Object[0]);
        Utils.notNull(configuration, "jsonProvider can not be null", new Object[0]);
        return (T) read(configuration.jsonProvider().parse(json), configuration);
    }

    public <T> T read(URL jsonURL) throws IOException {
        return (T) read(jsonURL, Configuration.defaultConfiguration());
    }

    public <T> T read(File jsonFile) throws IOException {
        return (T) read(jsonFile, Configuration.defaultConfiguration());
    }

    public <T> T read(File jsonFile, Configuration configuration) throws IOException {
        FileInputStream fis;
        Utils.notNull(jsonFile, "json file can not be null", new Object[0]);
        Utils.isTrue(jsonFile.exists(), "json file does not exist");
        Utils.notNull(configuration, "jsonProvider can not be null", new Object[0]);
        FileInputStream fis2 = null;
        try {
            fis = new FileInputStream(jsonFile);
        } catch (Throwable th) {
            th = th;
        }
        try {
            T t = (T) read((InputStream) fis, configuration);
            Utils.closeQuietly(fis);
            return t;
        } catch (Throwable th2) {
            th = th2;
            fis2 = fis;
            Utils.closeQuietly(fis2);
            throw th;
        }
    }

    public <T> T read(InputStream jsonInputStream) throws IOException {
        return (T) read(jsonInputStream, Configuration.defaultConfiguration());
    }

    public <T> T read(InputStream jsonInputStream, Configuration configuration) throws IOException {
        Utils.notNull(jsonInputStream, "json input stream can not be null", new Object[0]);
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        return (T) read(jsonInputStream, "UTF-8", configuration);
    }

    public <T> T read(InputStream jsonInputStream, String charset, Configuration configuration) throws IOException {
        Utils.notNull(jsonInputStream, "json input stream can not be null", new Object[0]);
        Utils.notNull(charset, "charset can not be null", new Object[0]);
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        try {
            return (T) read(configuration.jsonProvider().parse(jsonInputStream, charset), configuration);
        } finally {
            Utils.closeQuietly(jsonInputStream);
        }
    }

    public static JsonPath compile(String jsonPath, Predicate... filters) {
        Utils.notEmpty(jsonPath, "json can not be null or empty", new Object[0]);
        return new JsonPath(jsonPath, filters);
    }

    public static <T> T read(Object json, String jsonPath, Predicate... filters) {
        return (T) parse(json).read(jsonPath, filters);
    }

    public static <T> T read(String json, String jsonPath, Predicate... filters) {
        return (T) new JsonReader().parse(json).read(jsonPath, filters);
    }

    public static <T> T read(URL jsonURL, String jsonPath, Predicate... filters) throws IOException {
        return (T) new JsonReader().parse(jsonURL).read(jsonPath, filters);
    }

    public static <T> T read(File jsonFile, String jsonPath, Predicate... filters) throws IOException {
        return (T) new JsonReader().parse(jsonFile).read(jsonPath, filters);
    }

    public static <T> T read(InputStream jsonInputStream, String jsonPath, Predicate... filters) throws IOException {
        return (T) new JsonReader().parse(jsonInputStream).read(jsonPath, filters);
    }

    public static ParseContext using(Configuration configuration) {
        return new JsonReader(configuration);
    }

    public static ParseContext using(JsonProvider provider) {
        return new JsonReader(Configuration.builder().jsonProvider(provider).build());
    }

    public static DocumentContext parse(Object json) {
        return new JsonReader().parse(json);
    }

    public static DocumentContext parse(String json) {
        return new JsonReader().parse(json);
    }

    public static DocumentContext parse(InputStream json) {
        return new JsonReader().parse(json);
    }

    public static DocumentContext parse(File json) throws IOException {
        return new JsonReader().parse(json);
    }

    public static DocumentContext parse(URL json) throws IOException {
        return new JsonReader().parse(json);
    }

    public static DocumentContext parse(Object json, Configuration configuration) {
        return new JsonReader(configuration).parse(json);
    }

    public static DocumentContext parse(String json, Configuration configuration) {
        return new JsonReader(configuration).parse(json);
    }

    public static DocumentContext parse(InputStream json, Configuration configuration) {
        return new JsonReader(configuration).parse(json);
    }

    public static DocumentContext parse(File json, Configuration configuration) throws IOException {
        return new JsonReader(configuration).parse(json);
    }

    public static DocumentContext parse(URL json, Configuration configuration) throws IOException {
        return new JsonReader(configuration).parse(json);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T resultByConfiguration(Object jsonObject, Configuration configuration, EvaluationContext evaluationContext) {
        if (configuration.containsOption(Option.AS_PATH_LIST)) {
            Object jsonObject2 = evaluationContext.getPathList();
            return (T) jsonObject2;
        }
        return jsonObject;
    }
}
