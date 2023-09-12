package com.jayway.jsonpath.internal.token;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.EvaluationListener;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.internal.EvaluationAbortException;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.Path;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.spi.json.JsonProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class EvaluationContextImpl implements EvaluationContext {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationContextImpl.class);
    private final Configuration configuration;
    private final boolean forUpdate;
    private final Path path;
    private final Object pathResult;
    private final Object rootDocument;
    private final List<PathRef> updateOperations;
    private final Object valueResult;
    private final HashMap<Path, Object> documentEvalCache = new HashMap<>();
    private int resultIndex = 0;

    public EvaluationContextImpl(Path path, Object rootDocument, Configuration configuration, boolean forUpdate) {
        Utils.notNull(path, "path can not be null", new Object[0]);
        Utils.notNull(rootDocument, "root can not be null", new Object[0]);
        Utils.notNull(configuration, "configuration can not be null", new Object[0]);
        this.forUpdate = forUpdate;
        this.path = path;
        this.rootDocument = rootDocument;
        this.configuration = configuration;
        this.valueResult = configuration.jsonProvider().createArray();
        this.pathResult = configuration.jsonProvider().createArray();
        this.updateOperations = new ArrayList();
    }

    public HashMap<Path, Object> documentEvalCache() {
        return this.documentEvalCache;
    }

    public boolean forUpdate() {
        return this.forUpdate;
    }

    public void addResult(String path, PathRef operation, Object model) {
        if (this.forUpdate) {
            this.updateOperations.add(operation);
        }
        this.configuration.jsonProvider().setProperty(this.valueResult, Integer.valueOf(this.resultIndex), model);
        this.configuration.jsonProvider().setProperty(this.pathResult, Integer.valueOf(this.resultIndex), path);
        this.resultIndex++;
        if (!configuration().getEvaluationListeners().isEmpty()) {
            int idx = this.resultIndex - 1;
            for (EvaluationListener listener : configuration().getEvaluationListeners()) {
                EvaluationListener.EvaluationContinuation continuation = listener.resultFound(new FoundResultImpl(idx, path, model));
                if (EvaluationListener.EvaluationContinuation.ABORT == continuation) {
                    throw new EvaluationAbortException();
                }
            }
        }
    }

    public JsonProvider jsonProvider() {
        return this.configuration.jsonProvider();
    }

    public Set<Option> options() {
        return this.configuration.getOptions();
    }

    @Override // com.jayway.jsonpath.internal.EvaluationContext
    public Configuration configuration() {
        return this.configuration;
    }

    @Override // com.jayway.jsonpath.internal.EvaluationContext
    public Object rootDocument() {
        return this.rootDocument;
    }

    @Override // com.jayway.jsonpath.internal.EvaluationContext
    public Collection<PathRef> updateOperations() {
        Collections.sort(this.updateOperations);
        return Collections.unmodifiableCollection(this.updateOperations);
    }

    @Override // com.jayway.jsonpath.internal.EvaluationContext
    public <T> T getValue() {
        return (T) getValue(true);
    }

    @Override // com.jayway.jsonpath.internal.EvaluationContext
    public <T> T getValue(boolean unwrap) {
        if (this.path.isDefinite()) {
            if (this.resultIndex == 0) {
                throw new PathNotFoundException("No results for path: " + this.path.toString());
            }
            T t = (T) jsonProvider().getArrayIndex(this.valueResult, 0);
            if (t != null && unwrap) {
                Object value = jsonProvider().unwrap(t);
                return (T) value;
            }
            return t;
        }
        return (T) this.valueResult;
    }

    @Override // com.jayway.jsonpath.internal.EvaluationContext
    public <T> T getPath() {
        if (this.resultIndex == 0) {
            throw new PathNotFoundException("No results for path: " + this.path.toString());
        }
        return (T) this.pathResult;
    }

    @Override // com.jayway.jsonpath.internal.EvaluationContext
    public List<String> getPathList() {
        List<String> res = new ArrayList<>();
        if (this.resultIndex > 0) {
            Iterable<?> objects = this.configuration.jsonProvider().toIterable(this.pathResult);
            for (Object o : objects) {
                res.add((String) o);
            }
        }
        return res;
    }

    /* loaded from: classes.dex */
    private class FoundResultImpl implements EvaluationListener.FoundResult {
        private final int index;
        private final String path;
        private final Object result;

        private FoundResultImpl(int index, String path, Object result) {
            this.index = index;
            this.path = path;
            this.result = result;
        }

        @Override // com.jayway.jsonpath.EvaluationListener.FoundResult
        public int index() {
            return this.index;
        }

        @Override // com.jayway.jsonpath.EvaluationListener.FoundResult
        public String path() {
            return this.path;
        }

        @Override // com.jayway.jsonpath.EvaluationListener.FoundResult
        public Object result() {
            return this.result;
        }
    }
}
