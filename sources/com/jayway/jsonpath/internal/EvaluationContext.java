package com.jayway.jsonpath.internal;

import com.jayway.jsonpath.Configuration;
import java.util.Collection;
import java.util.List;
/* loaded from: classes.dex */
public interface EvaluationContext {
    Configuration configuration();

    <T> T getPath();

    List<String> getPathList();

    <T> T getValue();

    <T> T getValue(boolean z);

    Object rootDocument();

    Collection<PathRef> updateOperations();
}
