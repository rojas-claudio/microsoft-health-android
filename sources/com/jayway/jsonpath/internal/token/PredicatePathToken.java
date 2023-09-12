package com.jayway.jsonpath.internal.token;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.internal.PathRef;
import java.util.Arrays;
import java.util.Collection;
/* loaded from: classes.dex */
public class PredicatePathToken extends PathToken {
    private static final String[] FRAGMENTS = {"[?]", "[?,?]", "[?,?,?]", "[?,?,?,?]", "[?,?,?,?,?]"};
    private final Collection<Predicate> predicates;

    public PredicatePathToken(Predicate filter) {
        this.predicates = Arrays.asList(filter);
    }

    public PredicatePathToken(Collection<Predicate> predicates) {
        this.predicates = predicates;
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public void evaluate(String currentPath, PathRef ref, Object model, EvaluationContextImpl ctx) {
        if (ctx.jsonProvider().isMap(model)) {
            if (accept(model, ctx.rootDocument(), ctx.configuration(), ctx)) {
                PathRef op = ctx.forUpdate() ? ref : PathRef.NO_OP;
                if (isLeaf()) {
                    ctx.addResult(currentPath, op, model);
                } else {
                    next().evaluate(currentPath, op, model, ctx);
                }
            }
        } else if (ctx.jsonProvider().isArray(model)) {
            int idx = 0;
            Iterable<?> objects = ctx.jsonProvider().toIterable(model);
            for (Object idxModel : objects) {
                if (accept(idxModel, ctx.rootDocument(), ctx.configuration(), ctx)) {
                    handleArrayIndex(idx, currentPath, model, ctx);
                }
                idx++;
            }
        } else {
            throw new InvalidPathException(String.format("Filter: %s can not be applied to primitives. Current context is: %s", toString(), model));
        }
    }

    public boolean accept(Object obj, Object root, Configuration configuration, EvaluationContextImpl evaluationContext) {
        Predicate.PredicateContext ctx = new PredicateContextImpl(obj, root, configuration, evaluationContext.documentEvalCache());
        for (Predicate predicate : this.predicates) {
            if (!predicate.apply(ctx)) {
                return false;
            }
        }
        return true;
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public String getPathFragment() {
        return FRAGMENTS[this.predicates.size() - 1];
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    boolean isTokenDefinite() {
        return false;
    }
}
