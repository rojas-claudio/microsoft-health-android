package com.google.android.gms.internal;

import java.lang.ref.WeakReference;
import org.apache.commons.lang3.time.DateUtils;
/* loaded from: classes.dex */
public final class s {
    private final Runnable el;
    private v em;
    private boolean en = false;

    public s(final r rVar) {
        this.el = new Runnable() { // from class: com.google.android.gms.internal.s.1
            private final WeakReference<r> eo;

            {
                this.eo = new WeakReference<>(rVar);
            }

            @Override // java.lang.Runnable
            public void run() {
                s.this.en = false;
                r rVar2 = this.eo.get();
                if (rVar2 != null) {
                    rVar2.b(s.this.em);
                }
            }
        };
    }

    public void a(v vVar, long j) {
        if (this.en) {
            cn.q("An ad refresh is already scheduled.");
            return;
        }
        cn.o("Scheduling ad refresh " + j + " milliseconds from now.");
        this.em = vVar;
        this.en = true;
        cm.hO.postDelayed(this.el, j);
    }

    public void cancel() {
        cm.hO.removeCallbacks(this.el);
    }

    public void d(v vVar) {
        a(vVar, DateUtils.MILLIS_PER_MINUTE);
    }
}
