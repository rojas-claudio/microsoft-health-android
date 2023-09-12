package com.jayway.jsonpath.internal.token;

import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.Utils;
import java.util.List;
/* loaded from: classes.dex */
public class PropertyPathToken extends PathToken {
    private final List<String> properties;

    public PropertyPathToken(List<String> properties) {
        this.properties = properties;
    }

    public List<String> getProperties() {
        return this.properties;
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        if (!ctx.jsonProvider().isMap(model)) {
            throw new PathNotFoundException("Property " + getPathFragment() + " not found in path " + currentPath);
        }
        handleObjectProperty(currentPath, model, ctx, this.properties);
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    boolean isTokenDefinite() {
        return true;
    }

    @Override // com.jayway.jsonpath.internal.token.PathToken
    public String getPathFragment() {
        return "[" + Utils.join(", ", "'", this.properties) + "]";
    }
}
