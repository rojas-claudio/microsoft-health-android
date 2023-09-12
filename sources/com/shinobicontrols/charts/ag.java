package com.shinobicontrols.charts;

import com.shinobicontrols.charts.ag.a;
import java.util.Set;
/* loaded from: classes.dex */
abstract class ag<THandler extends a> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface a {
    }

    /* loaded from: classes.dex */
    static class b {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract b a();

    abstract void a(THandler thandler);

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() == getClass()) {
            return ((ag) o).a().equals(a());
        }
        return false;
    }

    public int hashCode() {
        return a().hashCode();
    }

    void b(a aVar) {
        THandler c = c(aVar);
        if (c != null) {
            a((ag<THandler>) c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Set<? extends a> set) {
        for (a aVar : set) {
            b(aVar);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    THandler c(a aVar) {
        return aVar;
    }
}
