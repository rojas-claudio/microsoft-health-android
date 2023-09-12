package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public class dz implements SafeParcelable {
    public static final ea CREATOR = new ea();
    private final int iM;
    private final HashMap<String, HashMap<String, dw.a<?, ?>>> lJ;
    private final ArrayList<a> lK;
    private final String lL;

    /* loaded from: classes.dex */
    public static class a implements SafeParcelable {
        public static final eb CREATOR = new eb();
        final String className;
        final ArrayList<b> lM;
        final int versionCode;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(int i, String str, ArrayList<b> arrayList) {
            this.versionCode = i;
            this.className = str;
            this.lM = arrayList;
        }

        a(String str, HashMap<String, dw.a<?, ?>> hashMap) {
            this.versionCode = 1;
            this.className = str;
            this.lM = a(hashMap);
        }

        private static ArrayList<b> a(HashMap<String, dw.a<?, ?>> hashMap) {
            if (hashMap == null) {
                return null;
            }
            ArrayList<b> arrayList = new ArrayList<>();
            for (String str : hashMap.keySet()) {
                arrayList.add(new b(str, hashMap.get(str)));
            }
            return arrayList;
        }

        HashMap<String, dw.a<?, ?>> bG() {
            HashMap<String, dw.a<?, ?>> hashMap = new HashMap<>();
            int size = this.lM.size();
            for (int i = 0; i < size; i++) {
                b bVar = this.lM.get(i);
                hashMap.put(bVar.lN, bVar.lO);
            }
            return hashMap;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            eb ebVar = CREATOR;
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            eb ebVar = CREATOR;
            eb.a(this, out, flags);
        }
    }

    /* loaded from: classes.dex */
    public static class b implements SafeParcelable {
        public static final dy CREATOR = new dy();
        final String lN;
        final dw.a<?, ?> lO;
        final int versionCode;

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(int i, String str, dw.a<?, ?> aVar) {
            this.versionCode = i;
            this.lN = str;
            this.lO = aVar;
        }

        b(String str, dw.a<?, ?> aVar) {
            this.versionCode = 1;
            this.lN = str;
            this.lO = aVar;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            dy dyVar = CREATOR;
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            dy dyVar = CREATOR;
            dy.a(this, out, flags);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public dz(int i, ArrayList<a> arrayList, String str) {
        this.iM = i;
        this.lK = null;
        this.lJ = b(arrayList);
        this.lL = (String) dm.e(str);
        bC();
    }

    public dz(Class<? extends dw> cls) {
        this.iM = 1;
        this.lK = null;
        this.lJ = new HashMap<>();
        this.lL = cls.getCanonicalName();
    }

    private static HashMap<String, HashMap<String, dw.a<?, ?>>> b(ArrayList<a> arrayList) {
        HashMap<String, HashMap<String, dw.a<?, ?>>> hashMap = new HashMap<>();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            a aVar = arrayList.get(i);
            hashMap.put(aVar.className, aVar.bG());
        }
        return hashMap;
    }

    public HashMap<String, dw.a<?, ?>> H(String str) {
        return this.lJ.get(str);
    }

    public void a(Class<? extends dw> cls, HashMap<String, dw.a<?, ?>> hashMap) {
        this.lJ.put(cls.getCanonicalName(), hashMap);
    }

    public boolean b(Class<? extends dw> cls) {
        return this.lJ.containsKey(cls.getCanonicalName());
    }

    public void bC() {
        for (String str : this.lJ.keySet()) {
            HashMap<String, dw.a<?, ?>> hashMap = this.lJ.get(str);
            for (String str2 : hashMap.keySet()) {
                hashMap.get(str2).a(this);
            }
        }
    }

    public void bD() {
        for (String str : this.lJ.keySet()) {
            HashMap<String, dw.a<?, ?>> hashMap = this.lJ.get(str);
            HashMap<String, dw.a<?, ?>> hashMap2 = new HashMap<>();
            for (String str2 : hashMap.keySet()) {
                hashMap2.put(str2, hashMap.get(str2).bs());
            }
            this.lJ.put(str, hashMap2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<a> bE() {
        ArrayList<a> arrayList = new ArrayList<>();
        for (String str : this.lJ.keySet()) {
            arrayList.add(new a(str, this.lJ.get(str)));
        }
        return arrayList;
    }

    public String bF() {
        return this.lL;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        ea eaVar = CREATOR;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String str : this.lJ.keySet()) {
            sb.append(str).append(":\n");
            HashMap<String, dw.a<?, ?>> hashMap = this.lJ.get(str);
            for (String str2 : hashMap.keySet()) {
                sb.append("  ").append(str2).append(": ");
                sb.append(hashMap.get(str2));
            }
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        ea eaVar = CREATOR;
        ea.a(this, out, flags);
    }
}
