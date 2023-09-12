package com.jayway.jsonpath.internal.token;

import com.jayway.jsonpath.internal.PathRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: classes.dex */
public class RootPathToken extends PathToken {
    private static final Logger logger = LoggerFactory.getLogger(RootPathToken.class);
    private PathToken tail = this;
    private int tokenCount = 1;

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public int getTokenCount() {
        return this.tokenCount;
    }

    public RootPathToken append(PathToken next) {
        this.tail = this.tail.appendTailToken(next);
        this.tokenCount++;
        return this;
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public void evaluate(String currentPath, PathRef pathRef, Object model, EvaluationContextImpl ctx) {
        if (isLeaf()) {
            PathRef op = ctx.forUpdate() ? pathRef : PathRef.NO_OP;
            ctx.addResult("$", op, model);
            return;
        }
        next().evaluate("$", pathRef, model, ctx);
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public String getPathFragment() {
        return "$";
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    boolean isTokenDefinite() {
        return true;
    }
}
