package com.google.android.gms.ads.mediation.customevent;

import com.google.ads.mediation.NetworkExtras;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class CustomEventExtras implements NetworkExtras {
    private final HashMap<String, Object> in = new HashMap<>();

    public Object getExtra(String label) {
        return this.in.get(label);
    }

    public void setExtra(String label, Object value) {
        this.in.put(label, value);
    }
}
