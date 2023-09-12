package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.internal.token.EvaluationContextImpl;
import com.jayway.jsonpath.internal.token.PathToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class CompiledPath implements Path {
    private static final Logger logger = LoggerFactory.getLogger(CompiledPath.class);
    private final boolean isRootPath;
    private final PathToken root;

    public CompiledPath(PathToken root, boolean isRootPath) {
        this.root = root;
        this.isRootPath = isRootPath;
    }

    @Override // com.jayway.jsonpath.internal.Path
    public boolean isRootPath() {
        return this.isRootPath;
    }

    @Override // com.jayway.jsonpath.internal.Path
    public EvaluationContext evaluate(Object document, Object rootDocument, Configuration configuration, boolean forUpdate) {
        if (logger.isDebugEnabled()) {
            logger.debug("Evaluating path: {}", toString());
        }
        EvaluationContextImpl ctx = new EvaluationContextImpl(this, rootDocument, configuration, forUpdate);
        try {
            PathRef op = ctx.forUpdate() ? PathRef.createRoot(rootDocument) : PathRef.NO_OP;
            this.root.evaluate("", op, document, ctx);
        } catch (EvaluationAbortException e) {
        }
        return ctx;
    }

    @Override // com.jayway.jsonpath.internal.Path
    public EvaluationContext evaluate(Object document, Object rootDocument, Configuration configuration) {
        return evaluate(document, rootDocument, configuration, false);
    }

    @Override // com.jayway.jsonpath.internal.Path
    public boolean isDefinite() {
        return this.root.isPathDefinite();
    }

    public String toString() {
        return this.root.toString();
    }
}
