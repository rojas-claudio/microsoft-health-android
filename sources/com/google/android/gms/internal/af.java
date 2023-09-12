package com.google.android.gms.internal;

import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import java.util.Map;
/* loaded from: classes.dex */
public final class af implements ai {
    private final ag ey;

    public af(ag agVar) {
        this.ey = agVar;
    }

    @Override // com.google.android.gms.internal.ai
    public void a(cq cqVar, Map<String, String> map) {
        String str = map.get(WorkoutSummary.NAME);
        if (str == null) {
            cn.q("App event with no name parameter.");
        } else {
            this.ey.a(str, map.get("info"));
        }
    }
}
