package com.jayway.jsonpath.internal.token;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.spi.json.JsonProvider;
import java.util.List;
/* loaded from: classes.dex */
public abstract class PathToken {
    private PathToken next;
    private PathToken prev;
    private Boolean definite = null;
    private Boolean upstreamDefinite = null;

    public abstract void evaluate(String str, PathRef pathRef, Object obj, EvaluationContextImpl evaluationContextImpl);

    abstract String getPathFragment();

    abstract boolean isTokenDefinite();

    /* JADX INFO: Access modifiers changed from: package-private */
    public PathToken appendTailToken(PathToken next) {
        this.next = next;
        this.next.prev = this;
        return next;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleObjectProperty(String currentPath, Object model, EvaluationContextImpl ctx, List<String> properties) {
        Object propertyVal;
        if (properties.size() == 1) {
            String property = properties.get(0);
            String evalPath = currentPath + "['" + property + "']";
            Object propertyVal2 = readObjectProperty(property, model, ctx);
            if (propertyVal2 == JsonProvider.UNDEFINED) {
                if (isLeaf()) {
                    if (ctx.options().contains(Option.DEFAULT_PATH_LEAF_TO_NULL)) {
                        propertyVal2 = null;
                    } else if (!ctx.options().contains(Option.SUPPRESS_EXCEPTIONS) || ctx.options().contains(Option.REQUIRE_PROPERTIES)) {
                        throw new PathNotFoundException("No results for path: " + evalPath);
                    } else {
                        return;
                    }
                } else if (isUpstreamDefinite() || ctx.options().contains(Option.REQUIRE_PROPERTIES) || ctx.options().contains(Option.SUPPRESS_EXCEPTIONS)) {
                    throw new PathNotFoundException("Missing property in path " + evalPath);
                } else {
                    return;
                }
            }
            PathRef pathRef = ctx.forUpdate() ? PathRef.create(model, property) : PathRef.NO_OP;
            if (isLeaf()) {
                ctx.addResult(evalPath, pathRef, propertyVal2);
                return;
            } else {
                next().evaluate(evalPath, pathRef, propertyVal2, ctx);
                return;
            }
        }
        String evalPath2 = currentPath + "[" + Utils.join(", ", "'", properties) + "]";
        if (!isLeaf()) {
            throw new InvalidPathException("Multi properties can only be used as path leafs: " + evalPath2);
        }
        Object merged = ctx.jsonProvider().createMap();
        for (String property2 : properties) {
            if (hasProperty(property2, model, ctx)) {
                propertyVal = readObjectProperty(property2, model, ctx);
                if (propertyVal == JsonProvider.UNDEFINED) {
                    if (ctx.options().contains(Option.DEFAULT_PATH_LEAF_TO_NULL)) {
                        propertyVal = null;
                    }
                }
                ctx.jsonProvider().setProperty(merged, property2, propertyVal);
            } else if (ctx.options().contains(Option.DEFAULT_PATH_LEAF_TO_NULL)) {
                propertyVal = null;
                ctx.jsonProvider().setProperty(merged, property2, propertyVal);
            } else if (ctx.options().contains(Option.REQUIRE_PROPERTIES)) {
                throw new PathNotFoundException("Missing property in path " + evalPath2);
            }
        }
        ctx.addResult(evalPath2, ctx.forUpdate() ? PathRef.create(model, properties) : PathRef.NO_OP, merged);
    }

    private static boolean hasProperty(String property, Object model, EvaluationContextImpl ctx) {
        return ctx.jsonProvider().getPropertyKeys(model).contains(property);
    }

    private static Object readObjectProperty(String property, Object model, EvaluationContextImpl ctx) {
        return ctx.jsonProvider().getMapValue(model, property);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleArrayIndex(int index, String currentPath, Object model, EvaluationContextImpl ctx) {
        String evalPath = currentPath + "[" + index + "]";
        PathRef pathRef = ctx.forUpdate() ? PathRef.create(model, index) : PathRef.NO_OP;
        try {
            Object evalHit = ctx.jsonProvider().getArrayIndex(model, index);
            if (isLeaf()) {
                ctx.addResult(evalPath, pathRef, evalHit);
            } else {
                next().evaluate(evalPath, pathRef, evalHit, ctx);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new PathNotFoundException("Index out of bounds when evaluating path " + evalPath);
        }
    }

    PathToken prev() {
        return this.prev;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PathToken next() {
        if (isLeaf()) {
            throw new IllegalStateException("Current path token is a leaf");
        }
        return this.next;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLeaf() {
        return this.next == null;
    }

    boolean isRoot() {
        return this.prev == null;
    }

    boolean isUpstreamDefinite() {
        if (this.upstreamDefinite != null) {
            return this.upstreamDefinite.booleanValue();
        }
        boolean isUpstreamDefinite = isTokenDefinite();
        if (isUpstreamDefinite && !isRoot()) {
            isUpstreamDefinite = this.prev.isPathDefinite();
        }
        this.upstreamDefinite = Boolean.valueOf(isUpstreamDefinite);
        return isUpstreamDefinite;
    }

    public int getTokenCount() {
        int cnt = 1;
        PathToken token = this;
        while (!token.isLeaf()) {
            token = token.next();
            cnt++;
        }
        return cnt;
    }

    public boolean isPathDefinite() {
        if (this.definite != null) {
            return this.definite.booleanValue();
        }
        boolean isDefinite = isTokenDefinite();
        if (isDefinite && !isLeaf()) {
            isDefinite = this.next.isPathDefinite();
        }
        this.definite = Boolean.valueOf(isDefinite);
        return isDefinite;
    }

    public String toString() {
        return isLeaf() ? getPathFragment() : getPathFragment() + next().toString();
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
