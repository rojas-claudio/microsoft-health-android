package com.shinobicontrols.charts;

import com.shinobicontrols.charts.ag;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
class al {
    private final HashMap<ag.b, HashSet<ag.a>> a = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public am a(ag.b bVar, ag.a aVar) {
        HashSet<ag.a> hashSet = this.a.get(bVar);
        if (hashSet == null) {
            hashSet = new HashSet<>();
            this.a.put(bVar, hashSet);
        }
        return a(hashSet, aVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(ag<?> agVar) {
        a(agVar.a(), agVar);
    }

    private am a(final HashSet<ag.a> hashSet, final ag.a aVar) {
        hashSet.add(aVar);
        return new am() { // from class: com.shinobicontrols.charts.al.1
            @Override // com.shinobicontrols.charts.am
            public void a() {
                hashSet.remove(aVar);
            }
        };
    }

    private void a(ag.b bVar, ag<?> agVar) {
        HashSet<ag.a> hashSet = this.a.get(bVar);
        if (hashSet != null && hashSet.size() > 0) {
            agVar.a((Set<? extends ag.a>) hashSet);
        }
    }
}
