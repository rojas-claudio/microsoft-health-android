package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public abstract class dw {

    /* loaded from: classes.dex */
    public static class a<I, O> implements SafeParcelable {
        public static final dx CREATOR = new dx();
        private final int iM;
        protected final boolean lA;
        protected final int lB;
        protected final boolean lC;
        protected final String lD;
        protected final int lE;
        protected final Class<? extends dw> lF;
        protected final String lG;
        private dz lH;
        private b<I, O> lI;
        protected final int lz;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, dr drVar) {
            this.iM = i;
            this.lz = i2;
            this.lA = z;
            this.lB = i3;
            this.lC = z2;
            this.lD = str;
            this.lE = i4;
            if (str2 == null) {
                this.lF = null;
                this.lG = null;
            } else {
                this.lF = ec.class;
                this.lG = str2;
            }
            if (drVar == null) {
                this.lI = null;
            } else {
                this.lI = (b<I, O>) drVar.bl();
            }
        }

        protected a(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends dw> cls, b<I, O> bVar) {
            this.iM = 1;
            this.lz = i;
            this.lA = z;
            this.lB = i2;
            this.lC = z2;
            this.lD = str;
            this.lE = i3;
            this.lF = cls;
            if (cls == null) {
                this.lG = null;
            } else {
                this.lG = cls.getCanonicalName();
            }
            this.lI = bVar;
        }

        public static a a(String str, int i, b<?, ?> bVar, boolean z) {
            return new a(bVar.bn(), z, bVar.bo(), false, str, i, null, bVar);
        }

        public static <T extends dw> a<T, T> a(String str, int i, Class<T> cls) {
            return new a<>(11, false, 11, false, str, i, cls, null);
        }

        public static <T extends dw> a<ArrayList<T>, ArrayList<T>> b(String str, int i, Class<T> cls) {
            return new a<>(11, true, 11, true, str, i, cls, null);
        }

        public static a<Integer, Integer> d(String str, int i) {
            return new a<>(0, false, 0, false, str, i, null, null);
        }

        public static a<Double, Double> e(String str, int i) {
            return new a<>(4, false, 4, false, str, i, null, null);
        }

        public static a<Boolean, Boolean> f(String str, int i) {
            return new a<>(6, false, 6, false, str, i, null, null);
        }

        public static a<String, String> g(String str, int i) {
            return new a<>(7, false, 7, false, str, i, null, null);
        }

        public static a<ArrayList<String>, ArrayList<String>> h(String str, int i) {
            return new a<>(7, true, 7, true, str, i, null, null);
        }

        public void a(dz dzVar) {
            this.lH = dzVar;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public dr bA() {
            if (this.lI == null) {
                return null;
            }
            return dr.a(this.lI);
        }

        public HashMap<String, a<?, ?>> bB() {
            dm.e(this.lG);
            dm.e(this.lH);
            return this.lH.H(this.lG);
        }

        public int bn() {
            return this.lz;
        }

        public int bo() {
            return this.lB;
        }

        public a<I, O> bs() {
            return new a<>(this.iM, this.lz, this.lA, this.lB, this.lC, this.lD, this.lE, this.lG, bA());
        }

        public boolean bt() {
            return this.lA;
        }

        public boolean bu() {
            return this.lC;
        }

        public String bv() {
            return this.lD;
        }

        public int bw() {
            return this.lE;
        }

        public Class<? extends dw> bx() {
            return this.lF;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public String by() {
            if (this.lG == null) {
                return null;
            }
            return this.lG;
        }

        public boolean bz() {
            return this.lI != null;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            dx dxVar = CREATOR;
            return 0;
        }

        public I f(O o) {
            return this.lI.f(o);
        }

        public int getVersionCode() {
            return this.iM;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Field\n");
            sb.append("            versionCode=").append(this.iM).append('\n');
            sb.append("                 typeIn=").append(this.lz).append('\n');
            sb.append("            typeInArray=").append(this.lA).append('\n');
            sb.append("                typeOut=").append(this.lB).append('\n');
            sb.append("           typeOutArray=").append(this.lC).append('\n');
            sb.append("        outputFieldName=").append(this.lD).append('\n');
            sb.append("      safeParcelFieldId=").append(this.lE).append('\n');
            sb.append("       concreteTypeName=").append(by()).append('\n');
            if (bx() != null) {
                sb.append("     concreteType.class=").append(bx().getCanonicalName()).append('\n');
            }
            sb.append("          converterName=").append(this.lI == null ? "null" : this.lI.getClass().getCanonicalName()).append('\n');
            return sb.toString();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            dx dxVar = CREATOR;
            dx.a(this, out, flags);
        }
    }

    /* loaded from: classes.dex */
    public interface b<I, O> {
        int bn();

        int bo();

        I f(O o);
    }

    private void a(StringBuilder sb, a aVar, Object obj) {
        if (aVar.bn() == 11) {
            sb.append(aVar.bx().cast(obj).toString());
        } else if (aVar.bn() != 7) {
            sb.append(obj);
        } else {
            sb.append("\"");
            sb.append(ei.I((String) obj));
            sb.append("\"");
        }
    }

    private void a(StringBuilder sb, a aVar, ArrayList<Object> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                a(sb, aVar, obj);
            }
        }
        sb.append("]");
    }

    protected abstract Object D(String str);

    protected abstract boolean E(String str);

    protected boolean F(String str) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected boolean G(String str) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public <O, I> I a(a<I, O> aVar, Object obj) {
        return ((a) aVar).lI != null ? aVar.f(obj) : obj;
    }

    protected boolean a(a aVar) {
        return aVar.bo() == 11 ? aVar.bu() ? G(aVar.bv()) : F(aVar.bv()) : E(aVar.bv());
    }

    protected Object b(a aVar) {
        String bv = aVar.bv();
        if (aVar.bx() != null) {
            dm.a(D(aVar.bv()) == null, "Concrete field shouldn't be value object: " + aVar.bv());
            HashMap<String, Object> br = aVar.bu() ? br() : bq();
            if (br != null) {
                return br.get(bv);
            }
            try {
                return getClass().getMethod("get" + Character.toUpperCase(bv.charAt(0)) + bv.substring(1), new Class[0]).invoke(this, new Object[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return D(aVar.bv());
    }

    public abstract HashMap<String, a<?, ?>> bp();

    public HashMap<String, Object> bq() {
        return null;
    }

    public HashMap<String, Object> br() {
        return null;
    }

    public String toString() {
        HashMap<String, a<?, ?>> bp = bp();
        StringBuilder sb = new StringBuilder(100);
        for (String str : bp.keySet()) {
            a<?, ?> aVar = bp.get(str);
            if (a(aVar)) {
                Object a2 = a(aVar, b(aVar));
                if (sb.length() == 0) {
                    sb.append("{");
                } else {
                    sb.append(",");
                }
                sb.append("\"").append(str).append("\":");
                if (a2 == null) {
                    sb.append("null");
                } else {
                    switch (aVar.bo()) {
                        case 8:
                            sb.append("\"").append(ef.b((byte[]) a2)).append("\"");
                            continue;
                        case 9:
                            sb.append("\"").append(ef.c((byte[]) a2)).append("\"");
                            continue;
                        case 10:
                            ej.a(sb, (HashMap) a2);
                            continue;
                        default:
                            if (aVar.bt()) {
                                a(sb, (a) aVar, (ArrayList) a2);
                                break;
                            } else {
                                a(sb, aVar, a2);
                                continue;
                            }
                    }
                }
            }
        }
        if (sb.length() > 0) {
            sb.append("}");
        } else {
            sb.append("{}");
        }
        return sb.toString();
    }
}
