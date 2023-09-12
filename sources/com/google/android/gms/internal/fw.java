package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class fw implements Parcelable.Creator<fv> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fv fvVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = fvVar.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fvVar.getVersionCode());
        }
        if (di.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fvVar.getAboutMe(), true);
        }
        if (di.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) fvVar.dD(), i, true);
        }
        if (di.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, fvVar.getBirthday(), true);
        }
        if (di.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fvVar.getBraggingRights(), true);
        }
        if (di.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, fvVar.getCircledByCount());
        }
        if (di.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) fvVar.dE(), i, true);
        }
        if (di.contains(8)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, fvVar.getCurrentLocation(), true);
        }
        if (di.contains(9)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, fvVar.getDisplayName(), true);
        }
        if (di.contains(12)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 12, fvVar.getGender());
        }
        if (di.contains(14)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 14, fvVar.getId(), true);
        }
        if (di.contains(15)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 15, (Parcelable) fvVar.dF(), i, true);
        }
        if (di.contains(16)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 16, fvVar.isPlusUser());
        }
        if (di.contains(19)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 19, (Parcelable) fvVar.dG(), i, true);
        }
        if (di.contains(18)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 18, fvVar.getLanguage(), true);
        }
        if (di.contains(21)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 21, fvVar.getObjectType());
        }
        if (di.contains(20)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 20, fvVar.getNickname(), true);
        }
        if (di.contains(23)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 23, fvVar.dI(), true);
        }
        if (di.contains(22)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 22, fvVar.dH(), true);
        }
        if (di.contains(25)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 25, fvVar.getRelationshipStatus());
        }
        if (di.contains(24)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 24, fvVar.getPlusOneCount());
        }
        if (di.contains(27)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 27, fvVar.getUrl(), true);
        }
        if (di.contains(26)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 26, fvVar.getTagline(), true);
        }
        if (di.contains(29)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 29, fvVar.isVerified());
        }
        if (di.contains(28)) {
            com.google.android.gms.common.internal.safeparcel.b.b(parcel, 28, fvVar.dJ(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: D */
    public fv createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int i = 0;
        String str = null;
        fv.a aVar = null;
        String str2 = null;
        String str3 = null;
        int i2 = 0;
        fv.b bVar = null;
        String str4 = null;
        String str5 = null;
        int i3 = 0;
        String str6 = null;
        fv.c cVar = null;
        boolean z = false;
        String str7 = null;
        fv.d dVar = null;
        String str8 = null;
        int i4 = 0;
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        int i5 = 0;
        int i6 = 0;
        String str9 = null;
        String str10 = null;
        ArrayList arrayList3 = null;
        boolean z2 = false;
        while (parcel.dataPosition() < j) {
            int i7 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i7)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i7);
                    hashSet.add(1);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(2);
                    break;
                case 3:
                    hashSet.add(3);
                    aVar = (fv.a) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i7, fv.a.CREATOR);
                    break;
                case 4:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(4);
                    break;
                case 5:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(5);
                    break;
                case 6:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i7);
                    hashSet.add(6);
                    break;
                case 7:
                    hashSet.add(7);
                    bVar = (fv.b) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i7, fv.b.CREATOR);
                    break;
                case 8:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(8);
                    break;
                case 9:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(9);
                    break;
                case 10:
                case 11:
                case 13:
                case 17:
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i7);
                    break;
                case 12:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i7);
                    hashSet.add(12);
                    break;
                case 14:
                    str6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(14);
                    break;
                case 15:
                    hashSet.add(15);
                    cVar = (fv.c) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i7, fv.c.CREATOR);
                    break;
                case 16:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i7);
                    hashSet.add(16);
                    break;
                case 18:
                    str7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(18);
                    break;
                case 19:
                    hashSet.add(19);
                    dVar = (fv.d) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i7, fv.d.CREATOR);
                    break;
                case 20:
                    str8 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(20);
                    break;
                case 21:
                    i4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i7);
                    hashSet.add(21);
                    break;
                case 22:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i7, fv.f.CREATOR);
                    hashSet.add(22);
                    break;
                case 23:
                    arrayList2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i7, fv.g.CREATOR);
                    hashSet.add(23);
                    break;
                case 24:
                    i5 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i7);
                    hashSet.add(24);
                    break;
                case 25:
                    i6 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i7);
                    hashSet.add(25);
                    break;
                case 26:
                    str9 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(26);
                    break;
                case 27:
                    str10 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i7);
                    hashSet.add(27);
                    break;
                case 28:
                    arrayList3 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i7, fv.h.CREATOR);
                    hashSet.add(28);
                    break;
                case 29:
                    z2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i7);
                    hashSet.add(29);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fv(hashSet, i, str, aVar, str2, str3, i2, bVar, str4, str5, i3, str6, cVar, z, str7, dVar, str8, i4, arrayList, arrayList2, i5, i6, str9, str10, arrayList3, z2);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ai */
    public fv[] newArray(int i) {
        return new fv[i];
    }
}
