package com.microsoft.kapp.models.strapp;

import com.microsoft.kapp.diagnostics.Validate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public class StrappStateCollection extends LinkedHashMap<UUID, StrappState> {
    private static final long serialVersionUID = 2839457283839485839L;

    public void put(StrappState strappState) {
        Validate.notNull(strappState, "strappState");
        put(strappState.getDefinition().getStrappId(), strappState);
    }

    public StrappStateCollection addAll(StrappStateCollection collection) {
        for (Map.Entry<UUID, StrappState> entry : collection.entrySet()) {
            if (!containsKey(entry.getKey())) {
                put(entry.getKey(), entry.getValue());
            } else {
                get(entry.getKey()).setIsEnabled(true);
            }
        }
        return this;
    }
}
