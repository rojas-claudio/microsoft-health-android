package com.shinobicontrols.charts;

import com.shinobicontrols.charts.ag;
/* loaded from: classes.dex */
class PropertyChangedEvent extends ag<Handler> {
    static final ag.b a = new ag.b();

    /* loaded from: classes.dex */
    public interface Handler extends ag.a {
        void onPropertyChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.ag
    public ag.b a() {
        return a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.shinobicontrols.charts.ag
    public void a(Handler handler) {
        handler.onPropertyChanged();
    }
}
