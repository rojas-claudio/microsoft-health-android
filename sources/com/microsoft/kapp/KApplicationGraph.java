package com.microsoft.kapp;

import dagger.ObjectGraph;
/* loaded from: classes.dex */
public class KApplicationGraph {
    private static ObjectGraph mApplicationGraph;

    public static void setApplicationGraph(ObjectGraph applicationGraph) {
        mApplicationGraph = applicationGraph;
    }

    public static ObjectGraph getApplicationGraph() {
        return mApplicationGraph;
    }
}
