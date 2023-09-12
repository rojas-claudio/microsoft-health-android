package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.google.android.gms.plus.model.moments.Moment;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
/* loaded from: classes.dex */
public final class fs extends dw implements SafeParcelable, Moment {
    public static final ft CREATOR = new ft();
    private static final HashMap<String, dw.a<?, ?>> rH = new HashMap<>();
    private final int iM;
    private final Set<Integer> rI;
    private String sD;
    private fq sG;
    private fq sH;
    private String sm;
    private String sx;

    static {
        rH.put("id", dw.a.g("id", 2));
        rH.put("result", dw.a.a("result", 4, fq.class));
        rH.put("startDate", dw.a.g("startDate", 5));
        rH.put("target", dw.a.a("target", 6, fq.class));
        rH.put("type", dw.a.g("type", 7));
    }

    public fs() {
        this.iM = 1;
        this.rI = new HashSet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fs(Set<Integer> set, int i, String str, fq fqVar, String str2, fq fqVar2, String str3) {
        this.rI = set;
        this.iM = i;
        this.sm = str;
        this.sG = fqVar;
        this.sx = str2;
        this.sH = fqVar2;
        this.sD = str3;
    }

    public fs(Set<Integer> set, String str, fq fqVar, String str2, fq fqVar2, String str3) {
        this.rI = set;
        this.iM = 1;
        this.sm = str;
        this.sG = fqVar;
        this.sx = str2;
        this.sH = fqVar2;
        this.sD = str3;
    }

    @Override // com.google.android.gms.internal.dw
    protected Object D(String str) {
        return null;
    }

    @Override // com.google.android.gms.internal.dw
    protected boolean E(String str) {
        return false;
    }

    @Override // com.google.android.gms.internal.dw
    protected boolean a(dw.a aVar) {
        return this.rI.contains(Integer.valueOf(aVar.bw()));
    }

    @Override // com.google.android.gms.internal.dw
    protected Object b(dw.a aVar) {
        switch (aVar.bw()) {
            case 2:
                return this.sm;
            case 3:
            default:
                throw new IllegalStateException("Unknown safe parcelable id=" + aVar.bw());
            case 4:
                return this.sG;
            case 5:
                return this.sx;
            case 6:
                return this.sH;
            case 7:
                return this.sD;
        }
    }

    @Override // com.google.android.gms.internal.dw
    public HashMap<String, dw.a<?, ?>> bp() {
        return rH;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dA() {
        return this.sH;
    }

    @Override // com.google.android.gms.common.data.Freezable
    /* renamed from: dB */
    public fs freeze() {
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        ft ftVar = CREATOR;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<Integer> di() {
        return this.rI;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fq dz() {
        return this.sG;
    }

    public boolean equals(Object obj) {
        if (obj instanceof fs) {
            if (this == obj) {
                return true;
            }
            fs fsVar = (fs) obj;
            for (dw.a<?, ?> aVar : rH.values()) {
                if (a(aVar)) {
                    if (fsVar.a(aVar) && b(aVar).equals(fsVar.b(aVar))) {
                    }
                    return false;
                } else if (fsVar.a(aVar)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public String getId() {
        return this.sm;
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public ItemScope getResult() {
        return this.sG;
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public String getStartDate() {
        return this.sx;
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public ItemScope getTarget() {
        return this.sH;
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public String getType() {
        return this.sD;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasId() {
        return this.rI.contains(2);
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasResult() {
        return this.rI.contains(4);
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasStartDate() {
        return this.rI.contains(5);
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasTarget() {
        return this.rI.contains(6);
    }

    @Override // com.google.android.gms.plus.model.moments.Moment
    public boolean hasType() {
        return this.rI.contains(7);
    }

    public int hashCode() {
        int i = 0;
        Iterator<dw.a<?, ?>> it = rH.values().iterator();
        while (true) {
            int i2 = i;
            if (!it.hasNext()) {
                return i2;
            }
            dw.a<?, ?> next = it.next();
            if (a(next)) {
                i = b(next).hashCode() + i2 + next.bw();
            } else {
                i = i2;
            }
        }
    }

    @Override // com.google.android.gms.common.data.Freezable
    public boolean isDataValid() {
        return true;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        ft ftVar = CREATOR;
        ft.a(this, out, flags);
    }
}
