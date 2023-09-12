package com.jayway.jsonpath.internal.token;

import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.internal.PathRef;
import java.util.Arrays;
/* loaded from: classes.dex */
public class WildcardPathToken extends PathToken {
    @Override // com.jayway.jsonpath.internal.token.PathToken
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        if (ctx.jsonProvider().isMap(model)) {
            for (String property : ctx.jsonProvider().getPropertyKeys(model)) {
                handleObjectProperty(currentPath, model, ctx, Arrays.asList(property));
            }
        } else if (ctx.jsonProvider().isArray(model)) {
            for (int idx = 0; idx < ctx.jsonProvider().length(model); idx++) {
                try {
                    handleArrayIndex(idx, currentPath, model, ctx);
                } catch (PathNotFoundException p) {
                    if (ctx.options().contains(Option.REQUIRE_PROPERTIES)) {
                        throw p;
                    }
                }
            }
        }
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    boolean isTokenDefinite() {
        return false;
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public String getPathFragment() {
        return "[*]";
    }
}
