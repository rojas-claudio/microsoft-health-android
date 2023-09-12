package com.jayway.jsonpath;
/* loaded from: classes.dex */
public interface EvaluationListener {

    /* loaded from: classes.dex */
    public enum EvaluationContinuation {
        CONTINUE,
        ABORT
    }

    /* loaded from: classes.dex */
    public interface FoundResult {
        int index();

        String path();

        Object result();
    }

    EvaluationContinuation resultFound(FoundResult foundResult);
}
