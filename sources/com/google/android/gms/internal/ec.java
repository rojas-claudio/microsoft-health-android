package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dw;
import com.unnamed.b.atv.model.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class ec extends dw implements SafeParcelable {
    public static final ed CREATOR = new ed();
    private final int iM;
    private final dz lH;
    private final Parcel lP;
    private final int lQ;
    private int lR;
    private int lS;
    private final String mClassName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ec(int i, Parcel parcel, dz dzVar) {
        this.iM = i;
        this.lP = (Parcel) dm.e(parcel);
        this.lQ = 2;
        this.lH = dzVar;
        if (this.lH == null) {
            this.mClassName = null;
        } else {
            this.mClassName = this.lH.bF();
        }
        this.lR = 2;
    }

    private ec(SafeParcelable safeParcelable, dz dzVar, String str) {
        this.iM = 1;
        this.lP = Parcel.obtain();
        safeParcelable.writeToParcel(this.lP, 0);
        this.lQ = 1;
        this.lH = (dz) dm.e(dzVar);
        this.mClassName = (String) dm.e(str);
        this.lR = 2;
    }

    public static <T extends dw & SafeParcelable> ec a(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        return new ec(t, b(t), canonicalName);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void a(dz dzVar, dw dwVar) {
        Class<?> cls = dwVar.getClass();
        if (dzVar.b((Class<? extends dw>) cls)) {
            return;
        }
        HashMap<String, dw.a<?, ?>> bp = dwVar.bp();
        dzVar.a(cls, dwVar.bp());
        for (String str : bp.keySet()) {
            dw.a<?, ?> aVar = bp.get(str);
            Class<? extends dw> bx = aVar.bx();
            if (bx != null) {
                try {
                    a(dzVar, bx.newInstance());
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Could not access object of type " + aVar.bx().getCanonicalName(), e);
                } catch (InstantiationException e2) {
                    throw new IllegalStateException("Could not instantiate an object of type " + aVar.bx().getCanonicalName(), e2);
                }
            }
        }
    }

    private void a(StringBuilder sb, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append(obj);
                return;
            case 7:
                sb.append("\"").append(ei.I(obj.toString())).append("\"");
                return;
            case 8:
                sb.append("\"").append(ef.b((byte[]) obj)).append("\"");
                return;
            case 9:
                sb.append("\"").append(ef.c((byte[]) obj));
                sb.append("\"");
                return;
            case 10:
                ej.a(sb, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i);
        }
    }

    private void a(StringBuilder sb, dw.a<?, ?> aVar, Parcel parcel, int i) {
        switch (aVar.bo()) {
            case 0:
                b(sb, aVar, a(aVar, Integer.valueOf(com.google.android.gms.common.internal.safeparcel.a.f(parcel, i))));
                return;
            case 1:
                b(sb, aVar, a(aVar, com.google.android.gms.common.internal.safeparcel.a.h(parcel, i)));
                return;
            case 2:
                b(sb, aVar, a(aVar, Long.valueOf(com.google.android.gms.common.internal.safeparcel.a.g(parcel, i))));
                return;
            case 3:
                b(sb, aVar, a(aVar, Float.valueOf(com.google.android.gms.common.internal.safeparcel.a.i(parcel, i))));
                return;
            case 4:
                b(sb, aVar, a(aVar, Double.valueOf(com.google.android.gms.common.internal.safeparcel.a.j(parcel, i))));
                return;
            case 5:
                b(sb, aVar, a(aVar, com.google.android.gms.common.internal.safeparcel.a.k(parcel, i)));
                return;
            case 6:
                b(sb, aVar, a(aVar, Boolean.valueOf(com.google.android.gms.common.internal.safeparcel.a.c(parcel, i))));
                return;
            case 7:
                b(sb, aVar, a(aVar, com.google.android.gms.common.internal.safeparcel.a.l(parcel, i)));
                return;
            case 8:
            case 9:
                b(sb, aVar, a(aVar, com.google.android.gms.common.internal.safeparcel.a.o(parcel, i)));
                return;
            case 10:
                b(sb, aVar, a(aVar, b(com.google.android.gms.common.internal.safeparcel.a.n(parcel, i))));
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown field out type = " + aVar.bo());
        }
    }

    private void a(StringBuilder sb, String str, dw.a<?, ?> aVar, Parcel parcel, int i) {
        sb.append("\"").append(str).append("\":");
        if (aVar.bz()) {
            a(sb, aVar, parcel, i);
        } else {
            b(sb, aVar, parcel, i);
        }
    }

    private void a(StringBuilder sb, HashMap<String, dw.a<?, ?>> hashMap, Parcel parcel) {
        HashMap<Integer, Map.Entry<String, dw.a<?, ?>>> b = b(hashMap);
        sb.append('{');
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        boolean z = false;
        while (parcel.dataPosition() < j) {
            int i = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            Map.Entry<String, dw.a<?, ?>> entry = b.get(Integer.valueOf(com.google.android.gms.common.internal.safeparcel.a.y(i)));
            if (entry != null) {
                if (z) {
                    sb.append(",");
                }
                a(sb, entry.getKey(), entry.getValue(), parcel, i);
                z = true;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        sb.append('}');
    }

    private static dz b(dw dwVar) {
        dz dzVar = new dz(dwVar.getClass());
        a(dzVar, dwVar);
        dzVar.bD();
        dzVar.bC();
        return dzVar;
    }

    public static HashMap<String, String> b(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap<>();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    private static HashMap<Integer, Map.Entry<String, dw.a<?, ?>>> b(HashMap<String, dw.a<?, ?>> hashMap) {
        HashMap<Integer, Map.Entry<String, dw.a<?, ?>>> hashMap2 = new HashMap<>();
        for (Map.Entry<String, dw.a<?, ?>> entry : hashMap.entrySet()) {
            hashMap2.put(Integer.valueOf(entry.getValue().bw()), entry);
        }
        return hashMap2;
    }

    private void b(StringBuilder sb, dw.a<?, ?> aVar, Parcel parcel, int i) {
        if (aVar.bu()) {
            sb.append("[");
            switch (aVar.bo()) {
                case 0:
                    ee.a(sb, com.google.android.gms.common.internal.safeparcel.a.q(parcel, i));
                    break;
                case 1:
                    ee.a(sb, com.google.android.gms.common.internal.safeparcel.a.s(parcel, i));
                    break;
                case 2:
                    ee.a(sb, com.google.android.gms.common.internal.safeparcel.a.r(parcel, i));
                    break;
                case 3:
                    ee.a(sb, com.google.android.gms.common.internal.safeparcel.a.t(parcel, i));
                    break;
                case 4:
                    ee.a(sb, com.google.android.gms.common.internal.safeparcel.a.u(parcel, i));
                    break;
                case 5:
                    ee.a(sb, com.google.android.gms.common.internal.safeparcel.a.v(parcel, i));
                    break;
                case 6:
                    ee.a(sb, com.google.android.gms.common.internal.safeparcel.a.p(parcel, i));
                    break;
                case 7:
                    ee.a(sb, com.google.android.gms.common.internal.safeparcel.a.w(parcel, i));
                    break;
                case 8:
                case 9:
                case 10:
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                case 11:
                    Parcel[] z = com.google.android.gms.common.internal.safeparcel.a.z(parcel, i);
                    int length = z.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        if (i2 > 0) {
                            sb.append(",");
                        }
                        z[i2].setDataPosition(0);
                        a(sb, aVar.bB(), z[i2]);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown field type out.");
            }
            sb.append("]");
            return;
        }
        switch (aVar.bo()) {
            case 0:
                sb.append(com.google.android.gms.common.internal.safeparcel.a.f(parcel, i));
                return;
            case 1:
                sb.append(com.google.android.gms.common.internal.safeparcel.a.h(parcel, i));
                return;
            case 2:
                sb.append(com.google.android.gms.common.internal.safeparcel.a.g(parcel, i));
                return;
            case 3:
                sb.append(com.google.android.gms.common.internal.safeparcel.a.i(parcel, i));
                return;
            case 4:
                sb.append(com.google.android.gms.common.internal.safeparcel.a.j(parcel, i));
                return;
            case 5:
                sb.append(com.google.android.gms.common.internal.safeparcel.a.k(parcel, i));
                return;
            case 6:
                sb.append(com.google.android.gms.common.internal.safeparcel.a.c(parcel, i));
                return;
            case 7:
                sb.append("\"").append(ei.I(com.google.android.gms.common.internal.safeparcel.a.l(parcel, i))).append("\"");
                return;
            case 8:
                sb.append("\"").append(ef.b(com.google.android.gms.common.internal.safeparcel.a.o(parcel, i))).append("\"");
                return;
            case 9:
                sb.append("\"").append(ef.c(com.google.android.gms.common.internal.safeparcel.a.o(parcel, i)));
                sb.append("\"");
                return;
            case 10:
                Bundle n = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i);
                Set<String> keySet = n.keySet();
                keySet.size();
                sb.append("{");
                boolean z2 = true;
                for (String str : keySet) {
                    if (!z2) {
                        sb.append(",");
                    }
                    sb.append("\"").append(str).append("\"");
                    sb.append(TreeNode.NODES_ID_SEPARATOR);
                    sb.append("\"").append(ei.I(n.getString(str))).append("\"");
                    z2 = false;
                }
                sb.append("}");
                return;
            case 11:
                Parcel y = com.google.android.gms.common.internal.safeparcel.a.y(parcel, i);
                y.setDataPosition(0);
                a(sb, aVar.bB(), y);
                return;
            default:
                throw new IllegalStateException("Unknown field type out");
        }
    }

    private void b(StringBuilder sb, dw.a<?, ?> aVar, Object obj) {
        if (aVar.bt()) {
            b(sb, aVar, (ArrayList) obj);
        } else {
            a(sb, aVar.bn(), obj);
        }
    }

    private void b(StringBuilder sb, dw.a<?, ?> aVar, ArrayList<?> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(",");
            }
            a(sb, aVar.bn(), arrayList.get(i));
        }
        sb.append("]");
    }

    @Override // com.google.android.gms.internal.dw
    protected Object D(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.internal.dw
    protected boolean E(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public Parcel bH() {
        switch (this.lR) {
            case 0:
                this.lS = com.google.android.gms.common.internal.safeparcel.b.k(this.lP);
                com.google.android.gms.common.internal.safeparcel.b.C(this.lP, this.lS);
                this.lR = 2;
                break;
            case 1:
                com.google.android.gms.common.internal.safeparcel.b.C(this.lP, this.lS);
                this.lR = 2;
                break;
        }
        return this.lP;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public dz bI() {
        switch (this.lQ) {
            case 0:
                return null;
            case 1:
                return this.lH;
            case 2:
                return this.lH;
            default:
                throw new IllegalStateException("Invalid creation type: " + this.lQ);
        }
    }

    @Override // com.google.android.gms.internal.dw
    public HashMap<String, dw.a<?, ?>> bp() {
        if (this.lH == null) {
            return null;
        }
        return this.lH.H(this.mClassName);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        ed edVar = CREATOR;
        return 0;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // com.google.android.gms.internal.dw
    public String toString() {
        dm.a(this.lH, "Cannot convert to JSON on client side.");
        Parcel bH = bH();
        bH.setDataPosition(0);
        StringBuilder sb = new StringBuilder(100);
        a(sb, this.lH.H(this.mClassName), bH);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        ed edVar = CREATOR;
        ed.a(this, out, flags);
    }
}
