package com.shinobicontrols.charts;

import com.shinobicontrols.charts.PropertyChangedEvent;
/* loaded from: classes.dex */
public abstract class SeriesStyle {
    final dj<Boolean> n = new dj<>(false);
    private final al a = new al();

    /* loaded from: classes.dex */
    public enum FillStyle {
        NONE,
        FLAT,
        GRADIENT
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(SeriesStyle seriesStyle) {
        if (seriesStyle != null) {
            this.n.b(Boolean.valueOf(seriesStyle.c()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c() {
        return this.n.a.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(boolean z) {
        synchronized (x.a) {
            this.n.a(Boolean.valueOf(z));
            d();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void d() {
        this.a.a(new PropertyChangedEvent());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public am a(PropertyChangedEvent.Handler handler) {
        return this.a.a(PropertyChangedEvent.a, handler);
    }
}
