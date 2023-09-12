package com.shinobicontrols.charts;

import com.shinobicontrols.charts.ag;
/* loaded from: classes.dex */
class d extends ag<a> {
    static final ag.b a = new ag.b();
    private final Annotation b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface a extends ag.a {
        void a(Annotation annotation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(Annotation annotation) {
        this.b = annotation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.ag
    public ag.b a() {
        return a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.ag
    public void a(a aVar) {
        aVar.a(this.b);
    }
}
