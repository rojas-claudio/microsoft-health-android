package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Configuration;
/* loaded from: classes.dex */
public interface Path {
    EvaluationContext evaluate(Object obj, Object obj2, Configuration configuration);

    EvaluationContext evaluate(Object obj, Object obj2, Configuration configuration, boolean z);

    boolean isDefinite();

    boolean isRootPath();
}
