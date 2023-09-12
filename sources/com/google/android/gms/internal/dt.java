package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class dt implements SafeParcelable, dw.b<String, Integer> {
    public static final du CREATOR = new du();
    private final int iM;
    private final HashMap<String, Integer> lu;
    private final HashMap<Integer, String> lv;
    private final ArrayList<a> lw;

    /* loaded from: classes.dex */
    public static final class a implements SafeParcelable {
        public static final dv CREATOR = new dv();
        final String lx;
        final int ly;
        final int versionCode;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(int i, String str, int i2) {
            this.versionCode = i;
            this.lx = str;
            this.ly = i2;
        }

        a(String str, int i) {
            this.versionCode = 1;
            this.lx = str;
            this.ly = i;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            dv dvVar = CREATOR;
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            dv dvVar = CREATOR;
            dv.a(this, out, flags);
        }
    }

    public dt() {
        this.iM = 1;
        this.lu = new HashMap<>();
        this.lv = new HashMap<>();
        this.lw = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public dt(int i, ArrayList<a> arrayList) {
        this.iM = i;
        this.lu = new HashMap<>();
        this.lv = new HashMap<>();
        this.lw = null;
        a(arrayList);
    }

    private void a(ArrayList<a> arrayList) {
        Iterator<a> it = arrayList.iterator();
        while (it.hasNext()) {
            a next = it.next();
            c(next.lx, next.ly);
        }
    }

    @Override // com.google.android.gms.internal.dw.b
    /* renamed from: a */
    public String f(Integer num) {
        String str = this.lv.get(num);
        return (str == null && this.lu.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<a> bm() {
        ArrayList<a> arrayList = new ArrayList<>();
        for (String str : this.lu.keySet()) {
            arrayList.add(new a(str, this.lu.get(str).intValue()));
        }
        return arrayList;
    }

    @Override // com.google.android.gms.internal.dw.b
    public int bn() {
        return 7;
    }

    @Override // com.google.android.gms.internal.dw.b
    public int bo() {
        return 0;
    }

    public dt c(String str, int i) {
        this.lu.put(str, Integer.valueOf(i));
        this.lv.put(Integer.valueOf(i), str);
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        du duVar = CREATOR;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        du duVar = CREATOR;
        du.a(this, out, flags);
    }
}
